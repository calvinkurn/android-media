package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R

class LatestReadingTopAdsDashboardRvAdapter :
    RecyclerView.Adapter<LatestReadingTopAdsDashboardRvAdapter.LatestReadingViewHolder>() {

    private val list = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestReadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return LatestReadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestReadingViewHolder, position: Int) {

    }

    fun addItems() {

    }

    override fun getItemCount() = list.size

    inner class LatestReadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private val layout = R.layout.item_rv_latest_reading
        fun createInstance() = LatestReadingTopAdsDashboardRvAdapter()
    }
}