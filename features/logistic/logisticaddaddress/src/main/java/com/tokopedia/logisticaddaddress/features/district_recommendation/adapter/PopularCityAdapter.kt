package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.chips_item.view.*
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.chips_unify_item.view.*


/**
 * Created by fwidjaja on 2019-05-29.
 */
class PopularCityAdapter(context: Context?, private var actionListener: ActionListener) : RecyclerView.Adapter<PopularCityAdapter.ViewHolder>() {
    var cityList = mutableListOf<String>()
    private var lastIndex = -1

    interface ActionListener {
        fun onCityChipClicked(city: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_unify_item, parent, false))
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        holder.itemView.chips_item.apply {
            chipText = cityList[position]
            chipType = ChipsUnify.TYPE_NORMAL
            chipSize = ChipsUnify.SIZE_MEDIUM
            setOnClickListener {
                notifyItemChanged(lastIndex)
                lastIndex = position
                chipType = ChipsUnify.TYPE_SELECTED
                actionListener.onCityChipClicked(cityList[position])
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}