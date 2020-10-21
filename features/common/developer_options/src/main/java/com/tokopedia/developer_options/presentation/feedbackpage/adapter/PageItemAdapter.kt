package com.tokopedia.developer_options.presentation.feedbackpage.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.LabelsItem
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class PageItemAdapter(var listener: OnPageMenuSelected): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val pageItemList = mutableListOf<LabelsItem>()

    fun renderData(data: List<LabelsItem>) {
        pageItemList.clear()
        pageItemList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnPageMenuSelected {
        fun onSelect(selection: Int, pageName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PageItemViewHolder(parent.inflateLayout(R.layout.item_page_name))
    }

    override fun getItemCount(): Int {
        return pageItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pagesItem = pageItemList[position]
        (holder as PageItemViewHolder).bind(pagesItem)
    }

    inner class PageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var itemPagesName = itemView.findViewById<Typography>(R.id.text_page_item)
        private var itemPagesRadio = itemView.findViewById<RadioButtonUnify>(R.id.item_page_radio)

        fun bind(data: LabelsItem ) {
            itemPagesName.text = data.name
            itemPagesRadio.skipAnimation()
            itemPagesRadio.setOnClickListener {
                listener.onSelect(data.id, data.name)
            }

        }
    }

}