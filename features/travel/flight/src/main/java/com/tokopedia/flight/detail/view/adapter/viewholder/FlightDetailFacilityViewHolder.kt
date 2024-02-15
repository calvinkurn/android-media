package com.tokopedia.flight.detail.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightDetailFacilityAmenityBinding
import com.tokopedia.flight.databinding.ItemFlightDetailFacilityBinding
import com.tokopedia.flight.databinding.ItemFlightDetailFacilityInfoBinding
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModel
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.search.data.cloud.single.Amenity
import com.tokopedia.media.loader.loadImageWithError
import java.util.*

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityViewHolder(val binding: ItemFlightDetailFacilityBinding) : AbstractViewHolder<FlightDetailRouteModel>(binding.root) {

    private val adapterInfo: ListInfoAdapter
    private val adapterAmenity: AmenityAdapter

    init {
        binding.recyclerViewInfo.layoutManager = LinearLayoutManager(itemView.context)
        adapterInfo = ListInfoAdapter()
        binding.recyclerViewInfo.adapter = adapterInfo
        adapterAmenity = AmenityAdapter()
        binding.recyclerViewAmenity.adapter = adapterAmenity
        binding.recyclerViewAmenity.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    }

    override fun bind(route: FlightDetailRouteModel) {
        if (route.infos.isEmpty()) {
            binding.separatorInfo.visibility = View.GONE
        }
        setInfos(route.infos)
        setDefaultAmenities(route)
        binding.headerDetailFlight.airlineName.text = route.airlineName
        binding.headerDetailFlight.airlineCode.text = String.format("%s-%s", route.airlineCode, route.flightNumber)
        binding.headerDetailFlight.airlineIcon.loadImageWithError(route.airlineLogo, R.drawable.flight_ic_airline_default)
    }

    private fun setDefaultAmenities(flightDetailRouteViewModel: FlightDetailRouteModel) {
        if (flightDetailRouteViewModel.amenities.isNotEmpty()) {
            binding.containerAmenity.visibility = View.VISIBLE
            binding.recyclerViewAmenity.visibility = View.VISIBLE
            binding.separatorInfo.visibility = View.VISIBLE
            binding.titleInfo.visibility = View.VISIBLE
            adapterAmenity.addData(flightDetailRouteViewModel.amenities)
        } else {
            binding.separatorInfo.visibility = View.GONE
            binding.containerAmenity.visibility = View.GONE
            binding.recyclerViewAmenity.visibility = View.GONE
            binding.titleInfo.visibility = View.GONE
        }
    }

    private fun setInfos(infos: List<FlightDetailRouteInfoModel>) {
        if (infos.isNotEmpty()) {
            adapterInfo.addData(infos)
            binding.separatorInfo.visibility = View.VISIBLE
            binding.recyclerViewInfo.visibility = View.VISIBLE
        } else {
            binding.separatorInfo.visibility = View.GONE
            binding.recyclerViewInfo.visibility = View.GONE
        }
    }

    private inner class ListInfoAdapter : RecyclerView.Adapter<FlightDetailFacilityInfoViewHolder>() {

        var infoList: MutableList<FlightDetailRouteInfoModel> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailFacilityInfoViewHolder {
            return FlightDetailFacilityInfoViewHolder(
                ItemFlightDetailFacilityInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: FlightDetailFacilityInfoViewHolder, position: Int) {
            holder.bindData(infoList[position])
        }

        override fun getItemCount(): Int {
            return infoList.size
        }

        fun addData(infos: List<FlightDetailRouteInfoModel>?) {
            infos?.let {
                infoList.clear()
                infoList.addAll(it)
                notifyDataSetChanged()
            }
        }

    }

    private inner class AmenityAdapter : RecyclerView.Adapter<FlightDetailFacilityAmenityViewHolder>() {

        var amenityList: MutableList<Amenity> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailFacilityAmenityViewHolder {
            return FlightDetailFacilityAmenityViewHolder(
                ItemFlightDetailFacilityAmenityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: FlightDetailFacilityAmenityViewHolder, position: Int) {
            holder.bindData(amenityList[position])
        }

        override fun getItemCount(): Int {
            return amenityList.size
        }

        fun addData(amenities: List<Amenity>?) {
            amenities?.let {
                amenityList.clear()
                amenityList.addAll(it)
                notifyDataSetChanged()
            }
        }

    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_flight_detail_facility
    }
}
