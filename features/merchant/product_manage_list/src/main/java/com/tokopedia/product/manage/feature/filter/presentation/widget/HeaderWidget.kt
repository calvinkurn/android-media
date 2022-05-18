package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.product.manage.databinding.WidgetHeaderBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography

class HeaderWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var binding: WidgetHeaderBinding? = null

    val title: Typography?
        get() = binding?.title
    val arrow: UnifyImageButton?
        get() = binding?.arrow

    private fun init() {
        binding = WidgetHeaderBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(headerText: String) {
        title?.text = headerText
    }
}

interface ShowChipsListener {
    fun onShowChips(element: FilterUiModel)
}