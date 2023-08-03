package com.tokopedia.mediauploader.common.compressor

import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.mediauploader.common.compressor.video.mp4.Mp4Movie
import com.tokopedia.mediauploader.common.compressor.video.mp4.Rotation
import java.io.File

object CompressionUtils {

    // 1 second between I-frames
    private const val I_FRAME_INTERVAL = 1

    /**
     * Setup movie with the height, width, and rotation values
     * @param rotation video rotation
     *
     * @return set movie with new values
     */
    fun setUpMP4Movie(rotation: Int, cacheFile: File): Mp4Movie {
        val movie = Mp4Movie()

        movie.apply {
            setCacheFile(cacheFile)
            setRotation(Rotation.map(rotation))
        }

        return movie
    }

    /**
     * Set output parameters like bitrate and frame rate
     */
    fun setOutputFileParameters(inputFormat: MediaFormat, outputFormat: MediaFormat, newBitrate: Int) {
        val newFrameRate = getFrameRate(inputFormat)
        val iFrameInterval = getIFrameIntervalRate(inputFormat)

        outputFormat.apply {
            setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            )

            setInteger(MediaFormat.KEY_FRAME_RATE, newFrameRate)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, iFrameInterval)

            // expected bps
            setInteger(MediaFormat.KEY_BIT_RATE, newBitrate)

            if (Build.VERSION.SDK_INT > 23) {
                getColorStandard(inputFormat)?.let {
                    setInteger(MediaFormat.KEY_COLOR_STANDARD, it)
                }

                getColorTransfer(inputFormat)?.let {
                    setInteger(MediaFormat.KEY_COLOR_TRANSFER, it)
                }

                getColorRange(inputFormat)?.let {
                    setInteger(MediaFormat.KEY_COLOR_RANGE, it)
                }
            }
        }
    }

    private fun getFrameRate(format: MediaFormat): Int {
        return if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
            format.getInteger(MediaFormat.KEY_FRAME_RATE)
        } else 30
    }

    private fun getIFrameIntervalRate(format: MediaFormat): Int {
        return if (format.containsKey(MediaFormat.KEY_I_FRAME_INTERVAL)) format.getInteger(
            MediaFormat.KEY_I_FRAME_INTERVAL
        ) else I_FRAME_INTERVAL
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getColorStandard(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_COLOR_STANDARD)) format.getInteger(
            MediaFormat.KEY_COLOR_STANDARD
        ) else null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getColorTransfer(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_COLOR_TRANSFER)) format.getInteger(
            MediaFormat.KEY_COLOR_TRANSFER
        ) else null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getColorRange(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_COLOR_RANGE)) format.getInteger(
            MediaFormat.KEY_COLOR_RANGE
        ) else null
    }

    fun findTrack(extractor: MediaExtractor, mimeTypePrefix: String): Int {
        val numTracks = extractor.trackCount
        for (i in 0 until numTracks) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)

            if (mime?.startsWith(mimeTypePrefix) == true) return i
        }

        return -5
    }

    fun hasQTI(): Boolean {
        val list = MediaCodecList(MediaCodecList.REGULAR_CODECS).codecInfos
        for (codec in list) {
            if (codec.name.contains("qti.avc")) {
                return true
            }
        }
        return false
    }
}
