package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_checklist.view.*

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

    private fun initView() {
        View.inflate(context, com.tokopedia.product.manage.R.layout.widget_checklist, this)
    }

    fun bind(element: ChecklistUiModel, checklistClickListener: ChecklistClickListener) {
        this.checklist.text = element.name
        this.checklist.isChecked = element.isSelected
        checklist.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
        }
    }

    fun updateChecklist(element: ChecklistUiModel, checklistClickListener: ChecklistClickListener) {
        this.checklist.isChecked = element.isSelected
        checklist.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
        }
    }
}

interface ChecklistClickListener {
    fun onChecklistClick(element: ChecklistUiModel)
}