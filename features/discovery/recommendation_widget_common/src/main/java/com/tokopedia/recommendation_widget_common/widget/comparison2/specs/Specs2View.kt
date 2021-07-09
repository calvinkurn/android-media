package com.tokopedia.recommendation_widget_common.widget.comparison2.specs

import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsListModel
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.view_specs.view.*

class Specs2View: FrameLayout  {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_specs, this)
    }

    fun setSpecsInfo(specsListModel: SpecsListModel) {
        val layoutManager = LinearLayoutManager(context)
        rootView.rv_specs.layoutManager = layoutManager
        rootView.rv_specs.adapter =
            Specs2Adapter(specsListModel)
        rootView.rv_specs.suppressLayout(true)
    }
}