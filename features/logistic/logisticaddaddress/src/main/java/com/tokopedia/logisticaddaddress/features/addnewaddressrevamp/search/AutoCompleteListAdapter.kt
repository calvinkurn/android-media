package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictSearchPageBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

class AutoCompleteListAdapter(private var listener: AutoCompleteItemListener): RecyclerView.Adapter<AutoCompleteListAdapter.AutoCompleteListViewHolder>() {

    private val autoCompleteData = mutableListOf<SuggestedPlace>()

    interface AutoCompleteItemListener {
        fun onItemClicked(placeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteListViewHolder {
        val binding = ItemDistrictSearchPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AutoCompleteListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return autoCompleteData.size
    }

    override fun onBindViewHolder(holder: AutoCompleteListViewHolder, position: Int) {
        holder.bindData(autoCompleteData[position])
    }

    fun setData(data: List<SuggestedPlace>) {
        autoCompleteData.clear()
        autoCompleteData.addAll(data)
        notifyDataSetChanged()
    }

    inner class AutoCompleteListViewHolder(binding: ItemDistrictSearchPageBinding): RecyclerView.ViewHolder(binding.root) {
        private val placeName = binding.searchPlaceName
        private val placeAddress = binding.searchPlaceAddress
        private val rlAutoCompleteItem = binding.rlAutocompleteItem

        fun bindData(data: SuggestedPlace) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(placeName, data.mainText, placeName.context.getString(R.string.content_desc_place_name))
            placeAddress.text = data.secondaryText
            rlAutoCompleteItem.setOnClickListener {
                listener.onItemClicked(data.placeId)
            }
        }
    }

}