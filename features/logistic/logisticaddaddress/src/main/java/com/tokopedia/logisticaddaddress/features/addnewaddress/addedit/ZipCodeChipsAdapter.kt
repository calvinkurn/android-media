package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.chips_item.view.*

/**
 * Created by fwidjaja on 2019-05-29.
 */
class ZipCodeChipsAdapter(context: Context?, private var actionListener: ActionListener) : RecyclerView.Adapter<ZipCodeChipsAdapter.ViewHolder>() {
    var zipCodes = mutableListOf<String>()

    interface ActionListener {
        fun onZipCodeClicked(zipCode: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_item, parent, false))
    }

    override fun getItemCount(): Int {
        return zipCodes.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        holder.itemView.tv_chips_item.apply {
            text = zipCodes[position]
            setTextColor(res.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            setOnClickListener {
                setTextColor(res.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G300))
                actionListener.onZipCodeClicked(zipCodes[position])
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}