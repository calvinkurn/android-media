package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

class AutoCompleteListAdapter(private var listener: AutoCompleteItemListener): RecyclerView.Adapter<AutoCompleteListAdapter.AutoCompleteListViewHolder>() {

    private val autoCompleteData = mutableListOf<SuggestedPlace>()

    interface AutoCompleteItemListener {
        fun onItemClicked(placeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteListViewHolder {
        return AutoCompleteListViewHolder(parent.inflateLayout(R.layout.item_district_search_page))
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

    inner class AutoCompleteListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val placeName = itemView.findViewById<Typography>(R.id.search_place_name)
        private val placeAddress = itemView.findViewById<Typography>(R.id.search_place_address)
        private val rlAutoCompleteItem = itemView.findViewById<RelativeLayout>(R.id.rl_autocomplete_item)

        fun bindData(data: SuggestedPlace) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(placeName, data.mainText, placeName.context.getString(R.string.content_desc_place_name))
            placeAddress.text = data.secondaryText
            rlAutoCompleteItem.setOnClickListener {
                listener.onItemClicked(data.placeId)
            }
        }
    }

}