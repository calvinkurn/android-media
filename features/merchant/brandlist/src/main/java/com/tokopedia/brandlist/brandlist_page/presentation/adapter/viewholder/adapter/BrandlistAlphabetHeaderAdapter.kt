package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.brandlist.R
import com.tokopedia.kotlin.extensions.view.inflateLayout

class BrandlistAlphabetHeaderAdapter (val listener: BrandlistHeaderBrandInterface):
        RecyclerView.Adapter<BrandlistAlphabetHeaderAdapter.BrandlistAlphabetHeaderViewHolder>()  {


    private val DEFAULT_SELECTED_POSITION = 1
    private var headerList: MutableList<String> = mutableListOf()
    var selectedPosition = DEFAULT_SELECTED_POSITION

    fun updateDataHeaderList(headerList: MutableList<String>) {
        this.headerList = headerList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandlistAlphabetHeaderViewHolder {
        return BrandlistAlphabetHeaderViewHolder(parent.inflateLayout(R.layout.brandlist_item_alphabet_header_chip))
    }

    override fun getItemCount(): Int {
        return headerList.size
    }

    override fun onBindViewHolder(holder: BrandlistAlphabetHeaderViewHolder, position: Int) {
        holder.bindData(headerList[position], position)
    }

    inner class BrandlistAlphabetHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        var chipTextView: TextView
        var chipContainer: LinearLayout

        init {
            context = itemView.context
            chipContainer = itemView.findViewById(R.id.chip_alphabet_header)
            chipTextView = itemView.findViewById(R.id.chip_textview)
        }

        fun bindData(headerItem: String, position: Int) {
            println("headerItem: $headerItem")
            chipTextView.text = headerItem
        }
    }

}