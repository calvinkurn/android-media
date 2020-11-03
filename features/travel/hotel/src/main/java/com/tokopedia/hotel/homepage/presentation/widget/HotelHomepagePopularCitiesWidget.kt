package com.tokopedia.hotel.homepage.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.homepage.presentation.adapter.HotelHomepagePopularCitiesAdapter
import com.tokopedia.hotel.homepage.presentation.adapter.HotelLastSearchAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import kotlinx.android.synthetic.main.layout_hotel_homepage_popular_city.view.*

/**
 * @author by jessica on 03/11/20
 */
class HotelHomepagePopularCitiesWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :BaseCustomView(context, attrs, defStyleAttr){

    lateinit var adapter: HotelHomepagePopularCitiesAdapter

    init {
        View.inflate(context, R.layout.layout_hotel_homepage_popular_city, this)
        hide()
    }

    fun addPopularCities(popularCities: List<PopularSearch>) {
        rv_popular_cities.layoutManager = GridLayoutManager(context, 2)
        adapter = HotelHomepagePopularCitiesAdapter(popularCities)
        rv_popular_cities.adapter = adapter

        //init recycler view
        if (popularCities.size > HotelHomepagePopularCitiesAdapter.MIN_ITEM_SHOW) {
            btn_homepage_popular_cities_see_more.show()
            btn_homepage_popular_cities_see_more.setOnClickListener {
                if (adapter.itemShowCount > HotelHomepagePopularCitiesAdapter.MIN_ITEM_SHOW) adapter.minimizeItems()
                else adapter.maximizeItems()
            }
        } else btn_homepage_popular_cities_see_more.hide()
    }
}