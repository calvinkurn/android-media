package com.tokopedia.contactus.inboxtickets.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxFilterSelection
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class InboxFilterAdapter(
    private val dataSet: List<InboxFilterSelection>,
    private val onPick: (List<InboxFilterSelection>) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentSelectedOption = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(
            parent.context
        )
        val v = inflater.inflate(R.layout.layout_filter_item, parent, false)
        return FilterHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FilterHolder).setLocationDate(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    internal inner class FilterHolder constructor(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        private var filterText: TextView? = null
        private var ivTick: ImageView? = null
        private var locationDateItem: View? = null
        private var valueItem: String? = null

        private fun findingViewsId(view: View) {
            filterText = view.findViewById(R.id.tv_filter_txt)
            ivTick = view.findViewById(R.id.iv_tick)
            locationDateItem = view.findViewById(R.id.filter_item)
        }

        fun setLocationDate(value: InboxFilterSelection) {
            valueItem = value.name
            filterText?.text = valueItem
            if (value.isSelected) {
                currentSelectedOption = value.id
                ivTick?.show()
            } else {
                ivTick?.hide()
            }
            locationDateItem?.setOnClickListener { onClickFilterItem(value) }
        }

        private fun onClickFilterItem(newSelectedOption: InboxFilterSelection) {
            val prevSelected = currentSelectedOption
            currentSelectedOption = newSelectedOption.id
            dataSet[prevSelected].isSelected = false
            dataSet[currentSelectedOption].isSelected = true
            notifyItemChanged(currentSelectedOption)
            notifyItemChanged(prevSelected)
            onPick(dataSet)
        }

        init {
            findingViewsId(itemView)
        }
    }
}
