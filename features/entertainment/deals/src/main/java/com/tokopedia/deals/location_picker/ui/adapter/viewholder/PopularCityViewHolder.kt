package com.tokopedia.deals.location_picker.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.location_picker.listener.DealsLocationListener
import com.tokopedia.deals.location_picker.model.visitor.PopularCityModel
import com.tokopedia.deals.location_picker.ui.adapter.DealsPoularCityAdapter

class PopularCityViewHolder(itemView: View, locationListener: DealsLocationListener): AbstractViewHolder<PopularCityModel>(itemView) {

    private val cityRv: RecyclerView = itemView.findViewById(R.id.rv_city_results)
    private val cityAdapter: DealsPoularCityAdapter = DealsPoularCityAdapter(locationListener)

    init {
        cityRv.adapter = cityAdapter
        cityRv.layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }

    override fun bind(element: PopularCityModel?) {
        element?.let {
            cityAdapter.cityList = element.cityList
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_popular_city_list
    }

}