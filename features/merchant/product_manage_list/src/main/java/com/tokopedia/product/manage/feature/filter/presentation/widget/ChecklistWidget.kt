package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
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

    fun bind(element: ChecklistViewModel, checklistClickListener: ChecklistClickListener) {
        this.checklist.text = element.name
        if(element.isSelected) {
            this.checklist.isChecked = true
        }
        this.checklist.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
        }
        this.setOnClickListener {
            checklistClickListener.onChecklistClick(element)
        }
    }
}

interface ChecklistClickListener {
    fun onChecklistClick(element: ChecklistViewModel)
}