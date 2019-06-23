package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompletePredictionUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResultUiModel
import kotlinx.android.synthetic.main.bottomsheet_autocomplete_item.view.*

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutocompleteBottomSheetAdapter(private var actionListener: ActionListener) : RecyclerView.Adapter<AutocompleteBottomSheetAdapter.ViewHolder>() {

    var dataAutocompleteGeocode = mutableListOf<AutocompleteGeocodeResultUiModel>()
    var dataAutocomplete = mutableListOf<AutocompletePredictionUiModel>()
    var isAutocompleteGeocode = false

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
            placeName = dataAutocomplete[position].prediction.mainText
            placeAddress = dataAutocomplete[position].prediction.secondaryText
            placeId = dataAutocomplete[position].placeId
        }

        holder.itemView.place_name.text = placeName
        holder.itemView.place_address.text = placeAddress
        holder.itemView.rl_autocomplete_item.setOnClickListener {
            println("## MASUK ONCLICK - placeID : $placeId")
            actionListener.onPoiListClicked(placeId)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}