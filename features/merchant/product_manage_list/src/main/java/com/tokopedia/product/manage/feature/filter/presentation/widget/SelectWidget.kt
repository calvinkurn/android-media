package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.product.manage.databinding.WidgetSelectBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.unifycomponents.BaseCustomView

class SelectWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var isVisible = false

    private var binding: WidgetSelectBinding? = null

    private fun init() {
        binding = WidgetSelectBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(element: SelectUiModel, selectClickListener: SelectClickListener) {
        binding?.title?.text = element.name
        if (element.isSelected) {
            binding?.check?.visibility = View.VISIBLE
            isVisible = true
        } else {
            binding?.check?.visibility = View.GONE
        }
        this.setOnClickListener {
            selectClickListener.onSelectClick(element)
        }
    }

    fun bindPayload(element: SelectUiModel, selectClickListener: SelectClickListener) {
        binding?.check?.visibility = if (element.isSelected ) View.VISIBLE else View.GONE
        this.setOnClickListener {
            selectClickListener.onSelectClick(element)
        }
    }
}

interface SelectClickListener {
    fun onSelectClick(element: SelectUiModel)
}