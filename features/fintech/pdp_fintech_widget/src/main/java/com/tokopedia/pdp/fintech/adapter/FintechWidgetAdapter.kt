package com.tokopedia.pdp.fintech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter.MyViewHolder
import com.tokopedia.pdp_fintech.R

class FintechWidgetAdapter : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fintech_invidual_chip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 3
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}