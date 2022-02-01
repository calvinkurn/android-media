package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R

class RingkasanTopAdsDashboardRvAdapter :
    RecyclerView.Adapter<RingkasanTopAdsDashboardRvAdapter.RingkasanViewHolder>() {

    private val list = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingkasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return RingkasanViewHolder(view)
    }

    override fun onBindViewHolder(holder: RingkasanViewHolder, position: Int) {

    }

    override fun getItemCount() = 5

    inner class RingkasanViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private val layout = R.layout.item_rv_ringkasan
        fun createInstance() = RingkasanTopAdsDashboardRvAdapter()
    }
}