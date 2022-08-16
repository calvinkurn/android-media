package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.databinding.ShcWidgetErrorStateViewBinding

/**
 * Created by @ilhamsuaib on 15/08/22.
 */

class WidgetErrorStateView : LinearLayout {

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

    private var binding: ShcWidgetErrorStateViewBinding? = null
    private var onErrorCallback: (() -> Unit)? = null

    private fun initView(context: Context) {
        binding = ShcWidgetErrorStateViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        showErrorState()
    }

    fun setOnReloadClicked(action: () -> Unit) {
        this.onErrorCallback = action
    }

    private fun showErrorState() {
        binding?.run {
            imgWidgetOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_404)

            btnShcReloadErrorState.setOnClickListener {
                onErrorCallback?.invoke()
            }
        }
    }
}