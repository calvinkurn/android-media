package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R

class SpecsView: FrameLayout  {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_specs, this)
    }

    fun setSpecsInfo(specsListModel: SpecsListModel) {

        val rv_specs = rootView.findViewById<RecyclerView>(R.id.rv_specs)
        rv_specs.layoutManager = LinearLayoutManager(context)
        rv_specs.adapter =
            SpecsAdapter(specsListModel)
        rv_specs.suppressLayout(true)
    }
}