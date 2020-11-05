package com.tokopedia.contactus.inboxticket2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class InboxFilterAdapter(private val dataSet: List<String>,
                         private var selected : Int,
                         private val mPresenter: InboxListContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.layout_filter_item, parent, false)
        return FilterHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FilterHolder).setLocationDate(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    internal inner class FilterHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var filterText: TextView? = null
        private var tvDayTime: ImageView? = null
        private var locationDateItem: View? = null
        private var valueItem: String? = null

        private fun findingViewsId(view: View) {
            filterText = view.findViewById(R.id.tv_filter_txt)
            tvDayTime = view.findViewById(R.id.iv_tick)
            locationDateItem = view.findViewById(R.id.filter_item)
        }

        fun setLocationDate(value: String) {
            val mContext = itemView.context
            valueItem = value
            filterText?.text = valueItem
            if (adapterPosition == selected) {
                tvDayTime?.show()
            } else {
                tvDayTime?.hide()
            }
            locationDateItem?.setOnClickListener { onClickFilterItem() }
        }

        private fun onClickFilterItem() {
            val prevSelected = selected
            selected = adapterPosition
            notifyItemChanged(selected)
            notifyItemChanged(prevSelected)
            mPresenter.setFilter(adapterPosition)
        }

        init {
            findingViewsId(itemView)
        }
    }

}