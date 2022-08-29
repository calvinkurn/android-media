package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Information
import com.tokopedia.unifyprinciples.Typography

class InformationRvAdapter(private val list: List<Information>) :
    RecyclerView.Adapter<InformationRvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_topads_dash_information, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]

        holder.apply {
            title.text = item.title
            subTitle.text = item.subTitle
        }
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(R.id.txt_title)
        val subTitle = view.findViewById<Typography>(R.id.txt_sub_title)
    }
}