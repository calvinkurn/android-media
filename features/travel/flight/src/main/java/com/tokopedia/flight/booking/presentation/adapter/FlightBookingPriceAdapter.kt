package com.tokopedia.flight.booking.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.booking.data.FlightPriceDetailEntity
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.databinding.ItemFlightBookingV3PriceBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPriceAdapter(
    private var priceListener: PriceListener? = null,
    private var priceDetailInfoListener: PriceDetailInfoListener? = null
) : RecyclerView.Adapter<FlightBookingPriceAdapter.ViewHolder>() {

    var routePriceList: List<FlightPriceDetailEntity> = listOf()
    var amenityPriceList: List<FlightPriceDetailEntity> = listOf()
    var othersPriceList: List<FlightPriceDetailEntity> = listOf()
    var adminPriceList: List<FlightPriceDetailEntity> = listOf()

    var routePriceCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFlightBookingV3PriceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            priceDetailInfoListener
        )

    override fun getItemCount(): Int = routePriceList.size + othersPriceList.size + amenityPriceList.size + adminPriceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < routePriceList.size) {
            holder.bind(routePriceList[position])
        } else if (position < amenityPriceList.size + routePriceList.size) {
            holder.bind(amenityPriceList[position - routePriceList.size])
        } else if (position < othersPriceList.size + amenityPriceList.size + routePriceList.size) {
            holder.bind(othersPriceList[position - routePriceList.size - amenityPriceList.size])
        } else {
            holder.bind(adminPriceList[position - routePriceList.size - amenityPriceList.size - othersPriceList.size])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRoutePriceList(list: List<FlightPriceDetailEntity>) {
        this.routePriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateOthersPriceList(list: List<FlightPriceDetailEntity>) {
        this.othersPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAmenityPriceList(list: List<FlightPriceDetailEntity>) {
        this.amenityPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdminFeePriceList(list: List<FlightPriceDetailEntity>) {
        this.adminPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    private fun countTotalPrice() {
        var totalPrice = 0
        for (item in routePriceList) totalPrice += item.priceNumeric
        for (item in othersPriceList) totalPrice += item.priceNumeric
        for (item in amenityPriceList) totalPrice += item.priceNumeric
        for (item in adminPriceList) totalPrice += item.priceNumeric
        priceListener?.onPriceChangeListener(FlightCurrencyFormatUtil.convertToIdrPrice(totalPrice), totalPrice)
    }

    fun getTotalPriceWithoutAmenities(): Int {
        var totalPrice = 0
        for (item in routePriceList) totalPrice += item.priceNumeric
        for (item in othersPriceList) totalPrice += item.priceNumeric
        for (item in adminPriceList) totalPrice += item.priceNumeric
        return totalPrice
    }

    class ViewHolder(
        private val binding: ItemFlightBookingV3PriceBinding,
        private val priceDetailInfoListener: PriceDetailInfoListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(price: FlightPriceDetailEntity) {
            with(binding) {
                tvPriceDescription.text = price.label
                tvPriceAmount.text = price.price

                iconPriceDetailInfo.shouldShowWithAction(price.popUpTitle.isNotEmpty() && price.popUpDescription.isNotEmpty()) {
                    iconPriceDetailInfo.setOnClickListener {
                        priceDetailInfoListener?.onInfoIconClick(price)
                    }
                }
            }
        }

        companion object {
            val LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_v3_price
        }
    }

    interface PriceListener {
        fun onPriceChangeListener(totalPrice: String, totalPriceNumeric: Int)
    }

    interface PriceDetailInfoListener {
        fun onInfoIconClick(priceDetail: FlightPriceDetailEntity)
    }
}
