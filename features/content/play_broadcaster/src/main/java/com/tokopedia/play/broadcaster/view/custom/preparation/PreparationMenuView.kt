package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationMenuBinding
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
class PreparationMenuView: ConstraintLayout {
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

    private val binding = ViewPlayBroPreparationMenuBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    init {
        binding.clBroSetTitle.setOnClickListener { mListener?.onClickSetTitle() }
        binding.clBroSetProduct.setOnClickListener { mListener?.onClickSetCover() }
        binding.clBroSetProduct.setOnClickListener { mListener?.onClickSetProduct() }
    }

    fun isSetTitleChecked(isChecked: Boolean) {
        binding.icBroTitleChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    fun isSetCoverChecked(isChecked: Boolean) {
        binding.icBroCoverChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    fun isSetProductChecked(isChecked: Boolean) {
        binding.icBroProductChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    interface Listener {
        fun onClickSetTitle()
        fun onClickSetCover()
        fun onClickSetProduct()
    }
}