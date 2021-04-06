package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.view_specs.view.*

class SpecsView: FrameLayout  {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_specs, this)
    }

    fun setSpecsInfo(specsListModel: SpecsListModel) {
        rootView.rv_specs.layoutManager = LinearLayoutManager(context)
        rootView.rv_specs.adapter =
            SpecsAdapter(specsListModel)
        rootView.rv_specs.suppressLayout(true)
    }
}