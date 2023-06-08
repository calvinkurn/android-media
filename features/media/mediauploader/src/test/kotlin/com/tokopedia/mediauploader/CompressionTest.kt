package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.video.internal.VideoCompressor
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.compressor.data.Result
import com.tokopedia.mediauploader.util.BaseUploaderTest
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepository
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepositoryImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CompressionTest : BaseUploaderTest() {

    private val compressor = mockk<VideoCompressor>()
    private val metadata = mockk<VideoMetaDataExtractor>()
    private val cache = mockk<AnalyticsCacheDataStore>()

    private lateinit var repository: VideoCompressionRepository

    @Before
    fun setUp() {
        // cache analytics
        every { cache.key(any(), any()) } returns ""
        coEvery { cache.setCompressionInfo(any(), any()) } just Runs
        coEvery { cache.getData(any()) } returns UploaderTracker()

        repository = VideoCompressionRepositoryImpl(
            compressor,
            metadata,
            cache
        )
    }

    @Test
    fun compress_video_successful() {
        runBlocking {
            // Given
            val expectedResult = generateMockVideoFile()
            val originalVideoPath = "original.mp4"
            val policyMinBitrate = 123
            val policyMinRes = 123

            val param = VideoCompressionParam(
                sourceId = "abc-xyz",
                videoPath = originalVideoPath,
                bitrate = policyMinBitrate,
                resolution = policyMinRes
            )

            // When
            coEvery { compressor.compress(any(), any(), any()) } returns Result(
                success = true,
                failureMessage = null,
                path = expectedResult.path
            )

            every { metadata.extract(any()) } returns VideoInfo(
                width = policyMinRes,
                height = policyMinRes,
                bitrate = 456 // bitrate meta-data extraction
            )

            // Then
            val result = repository(null, param)
            assertEquals(expectedResult.path, result)
        }
    }

    @Test
    fun compress_video_fail_when_hit_min_bitrate_threshold() {
        runBlocking {
            // Given
            val expectedResult = generateMockVideoFile()
            val originalVideoPath = "original.mp4"
            val policyMinBitrate = 123
            val policyMinRes = 123

            val param = VideoCompressionParam(
                sourceId = "abc-xyz",
                videoPath = originalVideoPath,
                bitrate = policyMinBitrate,
                resolution = policyMinRes
            )

            // When
            coEvery { compressor.compress(any(), any(), any()) } returns Result(
                success = true,
                failureMessage = null,
                path = expectedResult.path
            )

            every { metadata.extract(any()) } returns VideoInfo(
                width = policyMinRes,
                height = policyMinRes,
                bitrate = 0 // bitrate meta-data extraction
            )

            // Then
            val result = repository(null, param)
            assert(result == originalVideoPath)
        }
    }

    @Test
    fun compress_video_fail_when_hit_min_resolution() {
        runBlocking {
            // Given
            val expectedResult = generateMockVideoFile()
            val originalVideoPath = "original.mp4"
            val policyMinBitrate = 123
            val policyMinRes = 123

            val param = VideoCompressionParam(
                sourceId = "abc-xyz",
                videoPath = originalVideoPath,
                bitrate = policyMinBitrate,
                resolution = policyMinRes
            )

            // When
            coEvery { compressor.compress(any(), any(), any()) } returns Result(
                success = true,
                failureMessage = null,
                path = expectedResult.path
            )

            every { metadata.extract(any()) } returns VideoInfo(
                width = 0,
                height = 0,
                bitrate = 0
            )

            // Then
            val result = repository(null, param)
            assert(result == originalVideoPath)
        }
    }

    @Test
    fun compress_video_fail_when_cannot_extract_metadata() {
        runBlocking {
            // Given
            val expectedResult = generateMockVideoFile()
            val originalVideoPath = "original.mp4"
            val policyMinBitrate = 123
            val policyMinRes = 123

            val param = VideoCompressionParam(
                sourceId = "abc-xyz",
                videoPath = originalVideoPath,
                bitrate = policyMinBitrate,
                resolution = policyMinRes
            )

            // When
            coEvery { compressor.compress(any(), any(), any()) } returns Result(
                success = true,
                failureMessage = null,
                path = expectedResult.path
            )

            every { metadata.extract(any()) } returns null

            // Then
            val result = repository(null, param)
            assert(result == originalVideoPath)
        }
    }
}
