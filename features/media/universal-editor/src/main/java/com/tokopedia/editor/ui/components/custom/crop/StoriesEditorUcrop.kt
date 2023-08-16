package com.tokopedia.editor.ui.components.custom.crop

import android.content.Context
import android.graphics.Color
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

    init {
        LayoutInflater.from(context).inflate(R.layout.ucrop_clone_layout, this, true)
        mGestureCropImageView = findViewById<View>(R.id.image_view_crop) as GestureCropImageViewStories
        mViewOverlay = findViewById<View>(R.id.view_overlay) as OverlayViewStories
        val a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView)
        mViewOverlay!!.processStyledAttributesOpen(a)
        mGestureCropImageView!!.processStyledAttributesOpen(a)
        a.recycle()
        setListenersToViews()

        mGestureCropImageView!!.setBackgroundColor(Color.BLACK)
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
}
