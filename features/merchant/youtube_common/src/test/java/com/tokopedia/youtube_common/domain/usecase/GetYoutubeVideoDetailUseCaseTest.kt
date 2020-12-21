package com.tokopedia.youtube_common.domain.usecase

import android.webkit.URLUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.youtube_common.YoutubeCommonConstant.ENDPOINT_URL
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class GetYoutubeVideoDetailUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var restRepository: RestRepository

    private val getYoutubeVideoDetailUseCase by lazy {
        GetYoutubeVideoDetailUseCase(restRepository)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `check restRepository getResponses success`() {
        runBlocking {
            mockkStatic(URLUtil::class)
            every {
                URLUtil.isValidUrl(ENDPOINT_URL)
            } returns true
            coEvery {
                restRepository.getResponses(getYoutubeVideoDetailUseCase.restRequestList)
            } returns mapOf()
            val response = getYoutubeVideoDetailUseCase.executeOnBackground()
            coVerify {
                restRepository.getResponses(getYoutubeVideoDetailUseCase.restRequestList)
            }
            assertNotNull(response)
        }
    }
}
