package com.tokopedia.hotel.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.homepage.presentation.widget.HotelHomepagePopularCitiesWidget
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_hotel_homepage_popular_city_view_holder.view.*
import kotlin.math.min

/**
 * @author by jessica on 03/11/20
 */

class HotelHomepagePopularCitiesAdapter(private val popularCities: List<PopularSearch>,
                                        val listener: HotelHomepagePopularCitiesWidget.ActionListener? = null) : RecyclerView.Adapter<HotelHomepagePopularCitiesAdapter.ViewHolder>() {

    var itemShowCount = min(popularCities.size, MIN_ITEM_SHOW)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = itemShowCount

    fun minimizeItems() {
        itemShowCount = MIN_ITEM_SHOW
        notifyDataSetChanged()
    }

    fun maximizeItems() {
        itemShowCount = popularCities.size
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popularCities[position])
    }

    class ViewHolder(view: View, val listener: HotelHomepagePopularCitiesWidget.ActionListener?) : RecyclerView.ViewHolder(view) {
        fun bind(popularSearch: PopularSearch) {
            with(itemView) {
                iv_hotel_popular_city.loadImage(popularSearch.image)
                tv_hotel_popular_city_title.text = popularSearch.name
                tv_hotel_popular_city_subtitle.text = popularSearch.metaDescription

                setOnClickListener {
                    listener?.onPopularCityClicked(popularSearch)
                }
            }
        }
    }

    companion object {
        const val MIN_ITEM_SHOW = 4
        val LAYOUT = R.layout.layout_hotel_homepage_popular_city_view_holder
    }
}