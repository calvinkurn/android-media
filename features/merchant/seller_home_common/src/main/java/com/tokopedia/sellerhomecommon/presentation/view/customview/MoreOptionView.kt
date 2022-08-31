package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.databinding.ShcMoreOptionViewBinding

/**
 * Created by @ilhamsuaib on 27/08/22.
 */

class MoreOptionView : RelativeLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var binding: ShcMoreOptionViewBinding? = null
    private var onClickMore: (() -> Unit)? = null
    private var onClickCancel: (() -> Unit)? = null

    private fun initView(context: Context) {
        binding = ShcMoreOptionViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        binding?.run {
            icShcMoreOption.setOnClickListener {
                onClickMore?.invoke()
            }

            tvShcMoreOptionCancel.setOnClickListener {
                onClickCancel?.invoke()
            }

            showCheckingMode(false)
        }
    }

    fun showCheckingMode(isCheckingMode: Boolean) {
        binding?.run {
            if (isCheckingMode) {
                tvShcMoreOptionCancel.visible()
                icShcMoreOption.gone()
            } else {
                tvShcMoreOptionCancel.gone()
                icShcMoreOption.visible()
            }
        }
    }

    fun setOnMoreClicked(onClickMore: () -> Unit) {
        this.onClickMore = onClickMore
    }

    fun setOnCancelClicked(onClickCancel: () -> Unit) {
        this.onClickCancel = onClickCancel
    }
}