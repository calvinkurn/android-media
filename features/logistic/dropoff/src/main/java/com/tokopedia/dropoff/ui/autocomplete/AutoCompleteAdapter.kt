package com.tokopedia.dropoff.ui.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.dropoff.R
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dropoff.databinding.ItemAutocompleteResultBinding
import com.tokopedia.logisticCommon.domain.model.*
import com.tokopedia.baselist.R as baselistR

class AutoCompleteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<AutoCompleteVisitable> = mutableListOf()
    private var listener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.item_autocomplete_result -> ResultViewHolder(
                ItemAutocompleteResultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_autocomplete_no_result -> NoResultViewHolder(view)
            R.layout.item_autocomplete_header -> HeaderViewHolder(view)
            else -> ShimmeringViewHolder(view)
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
        is LoadingType -> baselistR.layout.item_shimmering_list
        is HeaderType -> R.layout.item_autocomplete_header
        is NoResultType -> R.layout.item_autocomplete_no_result
        is Place -> R.layout.item_autocomplete_result
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

    /**
     *  Set as inner class and private to hide access from outside of the class while having access
     *  to member of the adapter, e.g. listener
     * */
    private inner class NoResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class ShimmeringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class ResultViewHolder(val binding: ItemAutocompleteResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindAutoComplete(item: SuggestedPlace) {
            binding.tvAutocompleteTitle.text = item.mainText
            binding.tvAutocompleteDesc.text = item.secondaryText
            binding.root.setOnClickListener { listener?.onResultClicked(item) }
        }

        fun bindSavedAddress(item: SavedAddress) {
            binding.tvAutocompleteTitle.text = item.addrName
            binding.tvAutocompleteDesc.text = item.address1
            binding.root.setOnClickListener { listener?.onResultClicked(item) }
        }
    }
}
