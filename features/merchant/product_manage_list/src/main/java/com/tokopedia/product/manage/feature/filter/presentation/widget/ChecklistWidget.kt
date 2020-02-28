package com.tokopedia.product.manage.feature.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.oldlist.R
import kotlinx.android.synthetic.main.widget_checklist.view.*
import kotlinx.android.synthetic.main.widget_select.view.*

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
        View.inflate(context, R.layout.widget_checklist, this)
    }

    fun bind(element: ChecklistViewModel, checklistClickListener: ChecklistClickListener) {
        this.checklist.text = element.name
        this.setOnClickListener {
            toggleChecked()
            checklistClickListener.onChecklistClick(element)
        }
    }

    private fun toggleChecked() {
        this.checklist.isChecked = !this.checklist.isChecked
    }
}

interface ChecklistClickListener {
    fun onChecklistClick(element: ChecklistViewModel)
}