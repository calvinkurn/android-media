package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

class RecentSearchItemAdapter(
    private val listener: RecentSearchListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val TEMPLATE_ONE_LINE = "list_single_line"
        private const val TYPE_ONE_LINE = 1
        private const val TYPE_TWO_LINE = 2
    }

    private var data: List<BaseItemInitialStateSearch> = ArrayList()

    fun setData(data: List<BaseItemInitialStateSearch>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position].template){
            TEMPLATE_ONE_LINE -> TYPE_ONE_LINE
            else -> TYPE_TWO_LINE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_TWO_LINE -> {
                val itemView = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        RecentSearchDoubleLineItemViewHolder.LAYOUT,
                        parent,
                        false
                    )
                RecentSearchDoubleLineItemViewHolder(itemView, listener)
            }
            else -> {
                val itemView = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        RecentSearchSingleLineItemViewHolder.LAYOUT,
                        parent,
                        false
                    )
                RecentSearchSingleLineItemViewHolder(itemView, listener)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE_TWO_LINE -> (holder as RecentSearchDoubleLineItemViewHolder).bind(data[position])
            else -> (holder as RecentSearchSingleLineItemViewHolder).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}