package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResultUiModel
import com.tokopedia.logisticdata.domain.model.SuggestedPlace
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.bottomsheet_autocomplete_item.view.*

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutocompleteBottomSheetAdapter(private var actionListener: ActionListener) : RecyclerView.Adapter<AutocompleteBottomSheetAdapter.ViewHolder>() {

    var dataAutocompleteGeocode = mutableListOf<AutocompleteGeocodeResultUiModel>()
    var isAutocompleteGeocode = false
    private var dataAutocomplete = mutableListOf<SuggestedPlace>()

    interface ActionListener {
        fun onPoiListClicked(placeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_autocomplete_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (isAutocompleteGeocode) {
            dataAutocompleteGeocode.size
        } else {
            dataAutocomplete.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeName: String
        val placeAddress: String
        val placeId: String
        if (isAutocompleteGeocode) {
            placeName = dataAutocompleteGeocode[position].name
            placeAddress = dataAutocompleteGeocode[position].vicinity
            placeId = dataAutocompleteGeocode[position].placeId
        } else {
            placeName = dataAutocomplete[position].mainText
            placeAddress = dataAutocomplete[position].secondaryText
            placeId = dataAutocomplete[position].placeId
        }

        TextAndContentDescriptionUtil.setTextAndContentDescription(holder.itemView.place_name, placeName, holder.itemView.place_name.context.getString(R.string.content_desc_place_name))
        holder.itemView.place_address.text = placeAddress
        holder.itemView.rl_autocomplete_item.setOnClickListener {
            println("## MASUK ONCLICK - placeID : $placeId")
            actionListener.onPoiListClicked(placeId)
        }
    }

    fun addAutoComplete(list: List<SuggestedPlace>) {
        dataAutocomplete.clear()
        dataAutocomplete.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}