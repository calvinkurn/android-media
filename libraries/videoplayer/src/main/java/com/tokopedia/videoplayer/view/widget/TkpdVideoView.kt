package com.tokopedia.videoplayer.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.videoplayer.R

/**
 * Created by isfaaghyth on 25/04/19.
 * github: @isfaaghyth
 */
class TkpdVideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): PlayerView(context, attrs, defStyleAttr) {

    companion object {
        private const val PORTRAIT = 1
        private const val LANDSCAPE = 2
        private const val SQUARE = 3

        private const val aspectRatio = .5625f
    }

    //attributes options
    private var attrRatioType: Int? = 0

    init {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.TkpdVideoView)
            attrRatioType = attributeArray.getInt(R.styleable.TkpdVideoView_type, 0)
            attributeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (attrRatioType) {
            PORTRAIT -> {
                val width = measuredWidth
                val height = Math.round(width * aspectRatio)
                setMeasuredDimension(width, height)
            }
            LANDSCAPE -> {
                val height = measuredHeight
                val width = Math.round(height * aspectRatio)
                setMeasuredDimension(width, height)
            }
            SQUARE -> {
                val size = View.MeasureSpec.getSize(widthMeasureSpec)
                setMeasuredDimension(size, size)
            }
            else -> {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

}