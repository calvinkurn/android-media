package com.tokopedia.mediauploader.common.compressor.video.mp4

import android.media.MediaCodec
import android.media.MediaFormat
import com.googlecode.mp4parser.util.Matrix
import java.io.File
import java.util.*

class Mp4Movie {

    private var matrix = Matrix.ROTATE_0
    private val tracks = ArrayList<Track>()
    private var cacheFile: File? = null

    fun getMatrix(): Matrix? = matrix

    fun setCacheFile(file: File) {
        cacheFile = file
    }

    fun setRotation(rotation: Rotation?) {
        matrix = when (rotation) {
            Rotation.R90 -> Matrix.ROTATE_90
            Rotation.R180 -> Matrix.ROTATE_180
            Rotation.R270 -> Matrix.ROTATE_270
            else -> Matrix.ROTATE_0
        }
    }

    fun getTracks(): ArrayList<Track> = tracks

    fun getCacheFile(): File? = cacheFile

    fun addSample(trackIndex: Int, offset: Long, bufferInfo: MediaCodec.BufferInfo) {
        if (trackIndex < 0 || trackIndex >= tracks.size) {
            return
        }
        val track = tracks[trackIndex]
        track.addSample(offset, bufferInfo)
    }

    fun addTrack(mediaFormat: MediaFormat, isAudio: Boolean): Int {
        tracks.add(Track(tracks.size, mediaFormat, isAudio))
        return tracks.size - 1
    }
}
