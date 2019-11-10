package com.tokopedia.logisticaddaddress.features.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.autocomplete.model.*
import kotlinx.android.synthetic.main.item_autocomplete_result.view.*

class AutoCompleteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<AutoCompleteVisitable> = mutableListOf()
    private var listener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_autocomplete_result -> ResultViewHolder(view)
            R.layout.item_autocomplete_no_result -> NoResultViewHolder(view)
            R.layout.item_autocomplete_header -> HeaderViewHolder(view)
            else -> LoadingViewholder(view)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is ResultViewHolder -> {
                if (item is SuggestedPlace) {
                    holder.bindAutoComplete(item)
                } else if (item is SavedAddress) {
                    holder.bindSavedAddress(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is SuggestedPlace -> R.layout.item_autocomplete_result
        is SavedAddress -> R.layout.item_autocomplete_result
        is LoadingType -> LoadingViewholder.LAYOUT
        is HeaderType -> R.layout.item_autocomplete_header
        is NoResultType -> R.layout.item_autocomplete_no_result
    }

    fun setActionListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setData(items: List<SuggestedPlace>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun setEmptyData(items: List<SavedAddress>) {
        data.clear()
        data.add(HeaderType())
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun setNoResult() {
        data.clear()
        data.add(NoResultType())
        notifyDataSetChanged()
    }

    fun setLoading() {
        data.clear()
        data.add(LoadingType())
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onResultClicked(data: AutoCompleteVisitable)
    }

    private inner class NoResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindAutoComplete(item: SuggestedPlace) {
            itemView.tv_autocomplete_title.text = item.mainText
            itemView.tv_autocomplete_desc.text = item.secondaryText
            itemView.setOnClickListener { listener?.onResultClicked(item) }
        }

        fun bindSavedAddress(item: SavedAddress) {
            itemView.tv_autocomplete_title.text = item.addrName
            itemView.tv_autocomplete_desc.text = item.address1
            itemView.setOnClickListener { listener?.onResultClicked(item) }
        }
    }
}