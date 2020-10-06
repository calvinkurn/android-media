package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener

class RecentSearchItemAdapter(private val listener: InitialStateItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_autocomplete_double_line_item, parent, false)
                RecentSearchDoubleLineItemViewHolder(itemView, listener)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_autocomplete_single_line_item, parent, false)
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