package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictSearchPageBinding

class AutoCompleteListAdapter(private var listener: AutoCompleteItemListener): RecyclerView.Adapter<AutoCompleteListAdapterViewHolder>() {

    private val autoCompleteData = mutableListOf<SuggestedPlace>()

    interface AutoCompleteItemListener {
        fun onItemClicked(placeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteListAdapterViewHolder {
        val binding = ItemDistrictSearchPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = AutoCompleteListAdapterViewHolder.getViewHolder(binding)
        return viewHolder.apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(autoCompleteData[adapterPosition].placeId)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return autoCompleteData.size
    }

    override fun onBindViewHolder(holder: AutoCompleteListAdapterViewHolder, position: Int) {
        holder.bindData(autoCompleteData[position])
    }

    fun setData(data: List<SuggestedPlace>) {
        autoCompleteData.clear()
        autoCompleteData.addAll(data)
        notifyDataSetChanged()
    }

}