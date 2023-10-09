package com.tokopedia.editor.ui.widget.crop

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.editor.R
import com.yalantis.ucrop.callback.CropBoundsChangeListener
import com.yalantis.ucrop.callback.OverlayViewChangeListener

class StoryEditorUCropLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mGestureCropImageView: StoryGestureCropImageView? = null
    private var mViewOverlay: StoryOverlayView? = null

    var listener: Listener? = null

    init {

        LayoutInflater
            .from(context)
            .inflate(
                R.layout.ucrop_stories_layout,
                this,
                true
            )

        mGestureCropImageView = findViewById(R.id.image_view_crop)
        mViewOverlay = findViewById(R.id.view_overlay)

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.editor_placement_ucrop)

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
