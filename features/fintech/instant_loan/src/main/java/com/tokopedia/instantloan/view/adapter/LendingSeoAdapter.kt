package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlLendingSeoData

class LendingSeoAdapter(lendingSeoList: ArrayList<GqlLendingSeoData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val seoList = lendingSeoList
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.il_lending_seo_item, null)
        return LendingSeoAdapter.LeSeoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LendingSeoAdapter.LeSeoViewHolder).bindData(seoList.get(position), position)
    }

    class LeSeoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var heading: TextView
        var subHeading: TextView
        var context: Context

        init {
            context = view.context
            heading = view.findViewById(R.id.il_seo_heading)
            subHeading = view.findViewById(R.id.il_seo_subheading)
        }

        fun bindData(seoItem: GqlLendingSeoData, position: Int) {
            heading.text = seoItem.seoHead
            subHeading.text = seoItem.seoBody
        }
    }
}