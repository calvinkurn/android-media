package com.tokopedia.videoplayer.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import com.google.android.exoplayer2.ui.PlayerView

/**
 * @author by yfsx on 20/03/19.
 */
class VideoPlayerView @JvmOverloads constructor(
        context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
): VideoView(context, attributeSet, defStyleAttr) {

    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0

    fun setSize(width: Int, height: Int) {
        mVideoHeight = height
        mVideoWidth = width
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.getDefaultSize(mVideoWidth, widthMeasureSpec)
        var height = View.getDefaultSize(mVideoHeight, heightMeasureSpec)
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (mVideoWidth * height > width * mVideoHeight) {
                height = width * mVideoHeight / mVideoWidth
            } else if (mVideoWidth * height < width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight
            } else {
            }
        }
        setMeasuredDimension(width, height)
    }

    fun applyCustomController(context: Context) : MediaController {
        val mediaController = MediaController(context)
        mediaController.scrollBarStyle
        return mediaController
    }
}