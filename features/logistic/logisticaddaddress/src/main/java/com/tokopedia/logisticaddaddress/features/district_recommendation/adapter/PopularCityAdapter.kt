package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.chips_item.view.*
import androidx.core.content.ContextCompat


/**
 * Created by fwidjaja on 2019-05-29.
 */
class PopularCityAdapter(context: Context?, private var actionListener: ActionListener) : RecyclerView.Adapter<PopularCityAdapter.ViewHolder>() {
    var cityList = mutableListOf<String>()
    private var drawablePressed = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips_pressed) }
    private var drawableDefault = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips) }

    interface ActionListener {
        fun onCityChipClicked(city: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_item, parent, false))
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        holder.itemView.tv_chips_item.apply {
            text = cityList[position]
            setOnClickListener {
                background = drawablePressed
                setTextColor(res.getColor(R.color.tkpd_green))
                actionListener.onCityChipClicked(cityList[position])
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}