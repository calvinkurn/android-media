package com.tokopedia.videoplayer.view.widget

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView
import com.tokopedia.videoplayer.R
import kotlinx.coroutines.*

/**
 * @author by yfsx on 20/03/19.
 */
class VideoPlayerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0
    private var shouldMeasure: Boolean = true
    private var autoSize: Boolean = false

    init {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayerView)
            shouldMeasure = attributeArray.getBoolean(R.styleable.VideoPlayerView_onMeasure, true)
            autoSize = attributeArray.getBoolean(R.styleable.VideoPlayerView_autoSize, false)
            attributeArray.recycle()
        }
    }

    fun setSize(width: Int, height: Int) {
        mVideoHeight = height
        mVideoWidth = width
    }

    override fun setVideoURI(uri: Uri?) {
        if (autoSize) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(uri.toString(), emptyMap())
                    yield()
                    mVideoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
                    mVideoHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
                }
                super.setVideoURI(uri)
            }
        } else super.setVideoURI(uri)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (shouldMeasure) {
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
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDetachedFromWindow() {
        job.cancelChildren()
        super.onDetachedFromWindow()
    }
}