package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutoCompleteGeocodeResultUiModel
import kotlinx.android.synthetic.main.bottomsheet_autocomplete_geolocation_nearest_loc_item.view.*

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutoCompleteGeocodeBottomSheetAdapter(actionListener: AutoCompleteGeolocationListener) : RecyclerView.Adapter<AutoCompleteGeocodeBottomSheetAdapter.ViewHolder>() {

    var data = mutableListOf<AutoCompleteGeocodeResultUiModel>()
    private lateinit var actionListener: AutoCompleteGeolocationListener

    init {
        this.actionListener = actionListener
    }

    interface AutoCompleteGeolocationListener {
        fun onPoiListClicked(placeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_autocomplete_geolocation_nearest_loc_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.place_name.text = data[position].name
        holder.itemView.place_address.text = data[position].vicinity
        holder.itemView.rl_nearest_loc.setOnClickListener {
            data.run {
                val placeId = data[position].placeId
                println("## MASUK ONCLICK - placeID : $placeId")
                actionListener.onPoiListClicked(placeId)}
            }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        fun setData(autoCompleteGeocodeBottomSheetAdapter: AutoCompleteGeocodeBottomSheetAdapter, list: List<AutoCompleteGeocodeResultUiModel>) {
            autoCompleteGeocodeBottomSheetAdapter.data = list.toMutableList()
        }
    }

    fun setActionListener(actionListener: AutoCompleteGeolocationListener) {
        this.actionListener = actionListener
    }
}