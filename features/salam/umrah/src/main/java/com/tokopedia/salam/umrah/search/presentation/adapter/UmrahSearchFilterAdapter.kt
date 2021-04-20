package com.tokopedia.salam.umrah.search.presentation.adapter


import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahOption
import kotlinx.android.synthetic.main.item_umrah_search_filter.view.*

/**
 * @author by M on 18/10/2019
 */
class UmrahSearchFilterAdapter : RecyclerView.Adapter<UmrahSearchFilterAdapter.UmrahSearchFilterViewHolder>() {
    private var selectedOptionQuery: String = "-"
    private lateinit var options: List<UmrahOption>
    private val maximumItemsShowed = 5

    var listener: OnItemSelected? = null

    fun addOptions(options: List<UmrahOption>) {
        this.options = options
        notifyDataSetChanged()
    }

    fun setSelectedOption(optionQuery: String) {
        selectedOptionQuery = optionQuery
        notifyDataSetChanged()
    }

    fun getSelectedItem() = selectedOptionQuery

    private fun limitTo5Items(options: List<UmrahOption>): List<UmrahOption> {
        val newOptions = mutableListOf<UmrahOption>()
        val last = if (options.size > maximumItemsShowed) maximumItemsShowed else options.size
        for (i in 0 until last) newOptions.add(options[i])
        return newOptions.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahSearchFilterViewHolder {
        return UmrahSearchFilterViewHolder(parent.inflateLayout(R.layout.item_umrah_search_filter))
    }

    override fun onBindViewHolder(holder: UmrahSearchFilterViewHolder, position: Int) {
        holder.bindItem(options[position])
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class UmrahSearchFilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.base_item_filter.setOnClickListener {
                val option = options[adapterPosition]
                setSelectedOption(option.query)
                listener?.onSelect(option)
            }
        }

        fun bindItem(item: UmrahOption) {
            with(itemView) {
                title.text = item.displayText
                base_item_filter.isSelected = item.query == selectedOptionQuery
                if (base_item_filter.isSelected) {
                    title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G400))
                } else {
                    title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
                }
            }
        }
    }

    interface OnItemSelected {
        fun onSelect(option: UmrahOption)
    }
}