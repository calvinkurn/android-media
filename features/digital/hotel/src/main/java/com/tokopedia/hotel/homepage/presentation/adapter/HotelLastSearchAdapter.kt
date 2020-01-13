package com.tokopedia.hotel.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelLastSearchViewHolder

/**
 * @author by furqan on 10/04/19
 */
class HotelLastSearchAdapter(private var viewModels: List<String>) : RecyclerView.Adapter<HotelLastSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelLastSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(HotelLastSearchViewHolder.LAYOUT, parent, false)
        return HotelLastSearchViewHolder(view)
    }

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelLastSearchViewHolder, position: Int) {
    }

}