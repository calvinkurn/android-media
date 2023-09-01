package com.tokopedia.editor.ui.components.custom.crop

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.yalantis.ucrop.callback.CropBoundsChangeListener
import com.yalantis.ucrop.callback.OverlayViewChangeListener
import com.yalantis.ucrop.view.OverlayView
import com.tokopedia.editor.R

class StoriesEditorUcrop(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs, 0) {
    private var mGestureCropImageView: GestureCropImageViewStories? = null
    private var mViewOverlay: OverlayViewStories? = null

    var listener: Listener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.ucrop_stories_layout, this, true)
        mGestureCropImageView = findViewById<View>(R.id.image_view_crop) as GestureCropImageViewStories
        mViewOverlay = findViewById<View>(R.id.view_overlay) as OverlayViewStories

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView)
        mViewOverlay?.processStyledAttributesOpen(attribute)
        mGestureCropImageView?.let {
            it.processStyledAttributesOpen(attribute)
            it.setBackgroundColor(Color.BLACK)
            it.listener = object: CropImageViewStories.Listener {
                override fun onFinish() {
                    // adjust image load positioning due to changes on overlay position placement
                    it.postTranslate(0f, -(mViewOverlay?.topGap ?: 0f))
                    listener?.onFinish()
                }
            }
        }

        attribute.recycle()
        setListenersToViews()
    }

    private fun setListenersToViews() {
        mGestureCropImageView!!.cropBoundsChangeListener =
            CropBoundsChangeListener { cropRatio -> mViewOverlay!!.setTargetAspectRatio(cropRatio) }
        mViewOverlay!!.overlayViewChangeListener =
            OverlayViewChangeListener { cropRect -> mGestureCropImageView!!.setCropRect(cropRect) }
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    fun getCropImageView(): GestureCropImageViewStories? {
        return mGestureCropImageView
    }

    fun getOverlayView(): OverlayView? {
        return mViewOverlay
    }

    /**
     * Method for reset state for UCropImageView such as rotation, scale, translation.
     * Be careful: this method recreate UCropImageView instance and reattach it to layout.
     */
    fun resetCropImageView() {
        removeView(mGestureCropImageView)
        mGestureCropImageView = GestureCropImageViewStories(context)
        setListenersToViews()
        mGestureCropImageView!!.setCropRect(getOverlayView()!!.cropViewRect)
        addView(mGestureCropImageView, 0)
    }

    interface Listener {
        fun onFinish()
    }
}
