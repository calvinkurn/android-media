package com.tokopedia.common.travel.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.design.base.BaseCustomView
import kotlinx.android.synthetic.main.widget_travel_cross_selling.view.*

/**
 * @author by jessica on 2019-10-02
 */

class TravelCrossSellWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: TravelCrossSellAdapter

    init {
        View.inflate(context, R.layout.widget_travel_cross_selling, this)
    }

    fun buildView(travelCrossSelling: TravelCrossSelling) {
        title_tv.text = travelCrossSelling.meta.title

        cross_selling_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = TravelCrossSellAdapter()
        adapter.setItem(travelCrossSelling.items)
        cross_selling_rv.adapter = adapter
    }

    fun setListener(listener: TravelCrossSellAdapter.OnItemClickListener) {
        adapter.listener = listener
    }

}