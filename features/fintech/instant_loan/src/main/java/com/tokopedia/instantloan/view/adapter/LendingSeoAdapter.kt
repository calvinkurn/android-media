package com.tokopedia.instantloan.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.instantloan.data.model.response.GqlLendingSeoData

class LendingSeoAdapter(private val seoList: ArrayList<GqlLendingSeoData>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(com.tokopedia.instantloan.R.layout.il_lending_seo_item, null)
        return LeSeoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LeSeoViewHolder).bindData(seoList[position])
    }

    class LeSeoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var heading: TextView
        var subHeading: TextView
        var context: Context

        init {
            context = view.context
            heading = view.findViewById(com.tokopedia.instantloan.R.id.il_seo_heading)
            subHeading = view.findViewById(com.tokopedia.instantloan.R.id.il_seo_subheading)
        }

        fun bindData(seoItem: GqlLendingSeoData) {
            heading.text = seoItem.seoHead
            subHeading.text = seoItem.seoBody
        }
    }
}