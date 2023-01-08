package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationCoverFormBinding
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins

/**
 * Created By : Jonathan Darwin on January 26, 2022
 */
class CoverFormView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayBroPreparationCoverFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    init {
        binding.icCloseCoverForm.setOnClickListener { mListener?.onCloseCoverForm() }
        binding.clCoverFormPreview.setOnClickListener {
            mListener?.onClickCoverPreview(binding.clCoverFormPreview.isCoverAvailable)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.icCloseCoverForm.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    fun setCover(imageUrl: String) {
        binding.clCoverFormPreview.setCover(imageUrl)
    }

    fun setInitialCover() {
        binding.clCoverFormPreview.setInitialCover()
    }

    fun setTitle(title: String) {
        binding.clCoverFormPreview.setTitle(title)
    }

    fun setAuthorName(authorName: String) {
        binding.clCoverFormPreview.setAuthorName(authorName)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onCloseCoverForm()
        fun onClickCoverPreview(isEditCover: Boolean)
    }
}