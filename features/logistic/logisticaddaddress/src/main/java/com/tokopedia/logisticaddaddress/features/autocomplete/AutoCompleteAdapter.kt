package com.tokopedia.logisticaddaddress.features.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AddressResultUi
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutoCompleteResultUi
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
                if (item is AutoCompleteResultUi) {
                    holder.bindAutoComplete(item)
                    holder.itemView.setOnClickListener { listener?.onResultClicked(item) }
                } else if (item is AddressResultUi) {
                    holder.bindSavedAddress(item)
                    holder.itemView.setOnClickListener { listener?.onResultClicked(item) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is AutoCompleteResultUi -> R.layout.item_autocomplete_result
        is AddressResultUi -> R.layout.item_autocomplete_result
        is LoadingType -> LoadingViewholder.LAYOUT
        is HeaderType -> R.layout.item_autocomplete_header
        is NoResultType -> R.layout.item_autocomplete_no_result
        else -> throw RuntimeException("View type not found!!")
    }

    fun setActionListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setData(items: List<AutoCompleteResultUi>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun setEmptyData(items: List<AddressResultUi>) {
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

    interface AutoCompleteVisitable
    data class LoadingType(val id: Int = 0) : AutoCompleteVisitable
    data class NoResultType(val id: Int = 0) : AutoCompleteVisitable
    data class HeaderType(val id: Int = 0) : AutoCompleteVisitable

    private class NoResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindAutoComplete(item: AutoCompleteResultUi) {
            itemView.tv_autocomplete_title.text = item.structuredFormatting.mainText
            itemView.tv_autocomplete_desc.text = item.structuredFormatting.secondaryText
        }

        fun bindSavedAddress(item: AddressResultUi) {
            itemView.tv_autocomplete_title.text = item.addrName
            itemView.tv_autocomplete_desc.text = item.address1
        }
    }
}