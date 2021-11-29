package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.product.manage.databinding.WidgetChecklistBinding
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ChecklistWidget: BaseCustomView {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private var binding: WidgetChecklistBinding? = null

    val checklist: CheckboxUnify?
        get() = binding?.checklist

    private fun initView() {
        binding = WidgetChecklistBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(element: ChecklistUiModel, checklistClickListener: ChecklistClickListener) {
        checklist?.run {
            text = element.name
            isChecked = element.isSelected
            setOnClickListener {
                checklistClickListener.onChecklistClick(element)
            }
        }
    }

    fun updateChecklist(element: ChecklistUiModel, checklistClickListener: ChecklistClickListener) {
        checklist?.isChecked = element.isSelected
        checklist?.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
        }
    }
}

interface ChecklistClickListener {
    fun onChecklistClick(element: ChecklistUiModel)
}