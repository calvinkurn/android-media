package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.popular_city_chips_item.view.*
import android.support.v4.content.ContextCompat


/**
 * Created by fwidjaja on 2019-05-29.
 */
class ZipCodeChipsAdapter(context: Context?, private var actionListener: ActionListener) : RecyclerView.Adapter<ZipCodeChipsAdapter.ViewHolder>() {
    var zipCodes = mutableListOf<String>()
    private var drawablePressed = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips_pressed) }
    private var drawableDefault = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips) }

    interface ActionListener {
        fun onZipCodeClicked(zipCode: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popular_city_chips_item, parent, false))
    }

    override fun getItemCount(): Int {
        return zipCodes.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        holder.itemView.tv_city_chips_item.text = zipCodes[position]
        holder.itemView.tv_city_chips_item.setOnClickListener {
            holder.itemView.tv_city_chips_item.background = drawablePressed
            holder.itemView.tv_city_chips_item.setTextColor(res.getColor(R.color.tkpd_green))
            actionListener.onZipCodeClicked(zipCodes[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}