package com.tokopedia.logisticorder.view.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticorder.R
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.chips_unify_item.view.*

class TippingValueAdapter : RecyclerView.Adapter<TippingValueAdapter.ViewHolder>() {

    var tippingValueList = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chips_unify_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.chips_item.apply {
            chipText = "Rp" + tippingValueList[position].toString()
            chipType = ChipsUnify.TYPE_NORMAL
            chipSize = ChipsUnify.SIZE_MEDIUM
            setOnClickListener {
                chipType = ChipsUnify.TYPE_SELECTED
            }

        }
    }

    override fun getItemCount(): Int {
        return tippingValueList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}