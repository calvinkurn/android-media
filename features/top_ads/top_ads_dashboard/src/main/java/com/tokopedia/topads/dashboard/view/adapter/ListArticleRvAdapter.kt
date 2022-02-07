package com.tokopedia.topads.dashboard.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ListArticleRvAdapter :
    RecyclerView.Adapter<ListArticleRvAdapter.LatestReadingViewHolder>() {

    private val list = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestReadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return LatestReadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestReadingViewHolder, position: Int) {
        val item = list[holder.adapterPosition]

        with(holder) {

        }
    }

    fun addItems(data: List<Any>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class LatestReadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creditHistoryImage: ImageUnify = view.findViewById(R.id.creditHistoryImage)
        val txtTitle: Typography = view.findViewById(R.id.txtTitle)
        val txtDescription: Typography = view.findViewById(R.id.txtDescription)
    }

    companion object {
        private val layout = R.layout.item_rv_latest_reading
        fun createInstance() = ListArticleRvAdapter()
    }
}