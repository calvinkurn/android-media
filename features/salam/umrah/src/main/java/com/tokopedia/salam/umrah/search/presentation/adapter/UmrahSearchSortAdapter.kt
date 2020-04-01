package com.tokopedia.salam.umrah.search.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahOption
import kotlinx.android.synthetic.main.item_umrah_home_page_bottom_sheet.view.*

/**
 * @author by M on 22/10/2019
 */
class UmrahSearchSortAdapter : RecyclerView.Adapter<UmrahSearchSortAdapter.UmrahSearchSortViewHolder>() {
    private lateinit var options: List<UmrahOption>
    private var selectedOptionQuery = "-"
    var listener: OnSortMenuSelected? = null

    fun addOptions(options: List<UmrahOption>) {
        this.options = options
        notifyDataSetChanged()
    }

    fun setSelectedOption(optionQuery: String) {
        for ((index, option) in options.withIndex()) {
            val oldState = option.isSelected
            option.isSelected = option.query == optionQuery
            if (option.isSelected) selectedOptionQuery = optionQuery
            if (option.isSelected != oldState) notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahSearchSortViewHolder {
        return UmrahSearchSortViewHolder(parent.inflateLayout(R.layout.item_umrah_search_bottom_sheet))
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: UmrahSearchSortViewHolder, position: Int) {
        holder.bind(options[position])
    }

    inner class UmrahSearchSortViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                radio_umrah_home_page_bottom_sheet.setOnClickListener {
                    val item = options[adapterPosition]
                    setSelectedOption(item.query)
                    listener?.onSelect(item)
                }
                setOnClickListener {
                    val item = options[adapterPosition]
                    setSelectedOption(item.query)
                    listener?.onSelect(item)
                }
            }
        }

        fun bind(item: UmrahOption) {
            with(itemView) {
                tv_umrah_home_page_bottom_sheet.text = item.displayText
                radio_umrah_home_page_bottom_sheet.isChecked = item.isSelected
            }
        }
    }

    interface OnSortMenuSelected {
        fun onSelect(option: UmrahOption)
    }
}