package com.tokopedia.flight.detail.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModel
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.search.data.cloud.single.Amenity
import java.util.*

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityViewHolder(itemView: View) : AbstractViewHolder<FlightDetailRouteModel>(itemView) {

    private val adapterInfo: ListInfoAdapter
    private val adapterAmenity: AmenityAdapter

    private val listInfo: RecyclerView = itemView.findViewById<View>(R.id.recycler_view_info) as RecyclerView
    private val gridAmenity: RecyclerView = itemView.findViewById<View>(R.id.recycler_view_amenity) as RecyclerView
    private val imageAirline: AppCompatImageView = itemView.findViewById<View>(R.id.airline_icon) as AppCompatImageView
    private val airlineName: TextView = itemView.findViewById<View>(R.id.airline_name) as TextView
    private val airlineCode: TextView = itemView.findViewById<View>(R.id.airline_code) as TextView
    private val separatorInfoView: View = itemView.findViewById(R.id.separator_info) as View
    private val facilityInfoTextView: TextView = itemView.findViewById<View>(R.id.title_info) as TextView
    private val containerAmenity: LinearLayout = itemView.findViewById(R.id.container_amenity)

    init {
        listInfo.layoutManager = LinearLayoutManager(itemView.context)
        adapterInfo = ListInfoAdapter()
        listInfo.adapter = adapterInfo
        adapterAmenity = AmenityAdapter()
        gridAmenity.adapter = adapterAmenity
        gridAmenity.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    }

    override fun bind(route: FlightDetailRouteModel) {
        if (route.infos.isEmpty()) {
            separatorInfoView.visibility = View.GONE
        }
        adapterInfo.addData(route.infos)
        setDefaultAmenities(route)
        airlineName.text = route.airlineName
        airlineCode.text = String.format("%s-%s", route.airlineCode, route.flightNumber)
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.airlineLogo,
                ContextCompat.getDrawable(itemView.context, R.drawable.flight_ic_airline_default)
        )
    }

    private fun setDefaultAmenities(flightDetailRouteViewModel: FlightDetailRouteModel) {
        if (flightDetailRouteViewModel.amenities.isNotEmpty()) {
            containerAmenity.visibility = View.VISIBLE
            gridAmenity.visibility = View.VISIBLE
            separatorInfoView.visibility = View.VISIBLE
            facilityInfoTextView.visibility = View.VISIBLE
            adapterAmenity.addData(flightDetailRouteViewModel.amenities)
        } else {
            separatorInfoView.visibility = View.GONE
            containerAmenity.visibility = View.GONE
            gridAmenity.visibility = View.GONE
            facilityInfoTextView.visibility = View.GONE
        }
    }

    private inner class ListInfoAdapter : RecyclerView.Adapter<FlightDetailFacilityInfoViewHolder>() {

        var infoList: MutableList<FlightDetailRouteInfoModel> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailFacilityInfoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flight_detail_facility_info, parent, false)
            return FlightDetailFacilityInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: FlightDetailFacilityInfoViewHolder, position: Int) {
            holder.bindData(infoList[position])
        }

        override fun getItemCount(): Int {
            return infoList.size
        }

        fun addData(infos: List<FlightDetailRouteInfoModel>?) {
            infoList.clear()
            infoList.addAll(infos!!)
            notifyDataSetChanged()
        }

    }

    private inner class AmenityAdapter : RecyclerView.Adapter<FlightDetailFacilityAmenityViewHolder>() {

        var amenityList: MutableList<Amenity> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailFacilityAmenityViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flight_detail_facility_amenity, parent, false)
            return FlightDetailFacilityAmenityViewHolder(view)
        }

        override fun onBindViewHolder(holder: FlightDetailFacilityAmenityViewHolder, position: Int) {
            holder.bindData(amenityList[position])
        }

        override fun getItemCount(): Int {
            return amenityList.size
        }

        fun addData(amenities: List<Amenity>?) {
            amenityList.clear()
            amenityList.addAll(amenities!!)
            notifyDataSetChanged()
        }

    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_flight_detail_facility
    }
}