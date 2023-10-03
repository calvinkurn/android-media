package com.tokopedia.editor.ui.widget.crop

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.yalantis.ucrop.callback.CropBoundsChangeListener
import com.yalantis.ucrop.callback.OverlayViewChangeListener
import com.tokopedia.editor.R

class StoryEditorUCropLayout(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs, 0) {
    private var mGestureCropImageView: StoryGestureCropImageView? = null
    private var mViewOverlay: StoryOverlayView? = null

    var listener: Listener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.ucrop_stories_layout, this, true)
        mGestureCropImageView =
            findViewById<View>(R.id.image_view_crop) as StoryGestureCropImageView
        mViewOverlay = findViewById<View>(R.id.view_overlay) as StoryOverlayView

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView)
        mViewOverlay?.let {
            it.processStyledAttributesOpen(attribute)
            it.listener = object : StoryOverlayView.Listener {
                override fun onAspectRatioChange() {
                    // adjust image load positioning due to changes on overlay position placement
                    mGestureCropImageView?.postTranslate(0f, -(it.gapDifferent))
                }
            }
        }
        mGestureCropImageView?.let {
            it.processStyledAttributesOpen(attribute)
            it.setBackgroundColor(Color.BLACK)
            it.listener = object : StoryCropImageView.Listener {
                override fun onFinish() {
                    listener?.onFinish()
                }
            }
        }

        attribute.recycle()
        setListenersToViews()
    }

    private fun setListenersToViews() {
        mGestureCropImageView?.cropBoundsChangeListener =
            CropBoundsChangeListener { cropRatio -> mViewOverlay?.setTargetAspectRatio(cropRatio) }
        mViewOverlay?.overlayViewChangeListener =
            OverlayViewChangeListener { cropRect -> mGestureCropImageView?.setCropRect(cropRect) }
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    fun getCropImageView(): StoryGestureCropImageView? {
        return mGestureCropImageView
    }

    fun getOverlayView(): StoryOverlayView? {
        return mViewOverlay
    }

    interface Listener {
        fun onFinish()
    }
}
