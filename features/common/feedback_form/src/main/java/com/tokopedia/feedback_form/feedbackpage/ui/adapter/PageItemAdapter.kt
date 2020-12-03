package com.tokopedia.feedback_form.feedbackpage.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.feedbackpage.domain.model.LabelsItem
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class PageItemAdapter(var listener: OnPageMenuSelected): RecyclerView.Adapter<PageItemAdapter.PageItemViewHolder>() {

    val pageItemList = mutableListOf<LabelsItem>()
    var pageData: List<LabelsItem> = listOf()
    var searchKey: String = ""

    fun renderData(data: List<LabelsItem>) {
        pageData = data
        pageItemList.clear()
        pageItemList.addAll(data)
        notifyDataSetChanged()
    }

    fun renderDataSearch(searchKey: String) {
        pageItemList.clear()
        for (page in pageData) {
            if (page.name.toLowerCase().contains(searchKey.toLowerCase())) {
                pageItemList.add(page)
                notifyDataSetChanged()
            }
        }
    }

    interface OnPageMenuSelected {
        fun onSelect(selection: Int, pageName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageItemViewHolder {
        return PageItemViewHolder(parent.inflateLayout(R.layout.item_page_name))
    }

    override fun getItemCount(): Int {
        return pageItemList.size
    }

    override fun onBindViewHolder(holder: PageItemViewHolder, position: Int) {
        holder.bind(pageItemList[position])
    }

    inner class PageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var itemPagesName = itemView.findViewById<Typography>(R.id.text_page_item)
        private var itemPagesRadio = itemView.findViewById<RadioButtonUnify>(R.id.item_page_radio)
        private var itemPages = itemView.findViewById<ConstraintLayout>(R.id.item_page_list)

        fun bind(data: LabelsItem) {
            itemPagesName.text = data.name

            itemPagesRadio.isChecked = data.isSelected
            itemPagesRadio.skipAnimation()

            itemPages.setOnClickListener {
                listener.onSelect(data.id, data.name)
            }

        }
    }



}