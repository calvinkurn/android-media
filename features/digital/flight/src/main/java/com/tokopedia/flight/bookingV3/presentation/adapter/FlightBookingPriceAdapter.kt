package com.tokopedia.flight.bookingV3.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import kotlinx.android.synthetic.main.item_flight_booking_v3_price.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPriceAdapter: RecyclerView.Adapter<FlightBookingPriceAdapter.ViewHolder>() {

    var routePriceList: List<FlightCart.PriceDetail> = listOf()
    var amenityPriceList: List<FlightCart.PriceDetail> = listOf()
    lateinit var listener: PriceListener
    var routePriceCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = routePriceList.size + amenityPriceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < routePriceList.size) holder.bind(routePriceList[position])
        else holder.bind(amenityPriceList[position - routePriceList.size])
    }

    fun updateRoutePriceList(list: List<FlightCart.PriceDetail>) {
        this.routePriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    fun updateAmenityPriceList(list: List<FlightCart.PriceDetail>) {
        this.amenityPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    fun countTotalPrice() {
        var totalPrice = 0
        for (item in routePriceList) totalPrice += item.priceNumeric
        for (item in amenityPriceList) totalPrice += item.priceNumeric
        listener.onPriceChangeListener(FlightCurrencyFormatUtil.convertToIdrPrice(totalPrice), totalPrice)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(price: FlightCart.PriceDetail) {
            with(view) {
                tv_price_description.text = price.label
                tv_price_amount.text = price.price
            }
        }


        companion object {
            val LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_v3_price
        }
    }

    interface PriceListener{
        fun onPriceChangeListener(totalPrice: String, totalPriceNumeric: Int)
    }

}

