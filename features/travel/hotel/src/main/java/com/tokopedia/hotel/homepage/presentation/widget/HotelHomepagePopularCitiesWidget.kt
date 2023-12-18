package com.tokopedia.hotel.homepage.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.LayoutHotelHomepagePopularCityBinding
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.homepage.presentation.adapter.HotelHomepagePopularCitiesAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by jessica on 03/11/20
 */
class HotelHomepagePopularCitiesWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutHotelHomepagePopularCityBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    lateinit var adapter: HotelHomepagePopularCitiesAdapter
    var listener: ActionListener? = null

    init {
        hide()
    }

    fun setActionListener(listener: ActionListener) {
        this.listener = listener
    }

    fun addPopularCities(popularCities: List<PopularSearch>) {
        with(binding) {
            rvPopularCities.layoutManager = GridLayoutManager(context, RV_SPAN)
            adapter = HotelHomepagePopularCitiesAdapter(popularCities, listener)
            rvPopularCities.adapter = adapter

            // init recycler view
            if (popularCities.size > HotelHomepagePopularCitiesAdapter.MIN_ITEM_SHOW) {
                btnHomepagePopularCitiesSeeMore.show()
                btnHomepagePopularCitiesSeeMore.setOnClickListener {
                    if (adapter.itemShowCount > HotelHomepagePopularCitiesAdapter.MIN_ITEM_SHOW) {
                        adapter.minimizeItems()
                        btnHomepagePopularCitiesSeeMore.text = resources.getString(R.string.hotel_homepage_popular_city_button_see_less)
                    } else {
                        adapter.maximizeItems()
                        btnHomepagePopularCitiesSeeMore.text = resources.getString(R.string.hotel_homepage_popular_city_button_see_less)
                    }
                }
            } else {
                btnHomepagePopularCitiesSeeMore.hide()
            }
        }
    }

    interface ActionListener {
        fun onPopularCityClicked(popularSearch: PopularSearch)
    }

    companion object {
        private const val RV_SPAN = 2
    }
}
