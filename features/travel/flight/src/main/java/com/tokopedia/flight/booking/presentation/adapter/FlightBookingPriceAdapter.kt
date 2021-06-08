package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.booking.data.FlightCart
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import kotlinx.android.synthetic.main.item_flight_booking_v3_price.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPriceAdapter: RecyclerView.Adapter<FlightBookingPriceAdapter.ViewHolder>() {

    var routePriceList: List<FlightCart.PriceDetail> = listOf()
    var amenityPriceList: List<FlightCart.PriceDetail> = listOf()
    var othersPriceList: List<FlightCart.PriceDetail> = listOf()
    lateinit var listener: PriceListener
    var routePriceCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = routePriceList.size + othersPriceList.size + amenityPriceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < routePriceList.size) holder.bind(routePriceList[position])
        else if (position < amenityPriceList.size + routePriceList.size) holder.bind(amenityPriceList[position - routePriceList.size])
        else holder.bind(othersPriceList[position - routePriceList.size - amenityPriceList.size])
    }

    fun updateRoutePriceList(list: List<FlightCart.PriceDetail>) {
        this.routePriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    fun updateOthersPriceList(list: List<FlightCart.PriceDetail>) {
        this.othersPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    fun updateAmenityPriceList(list: List<FlightCart.PriceDetail>) {
        this.amenityPriceList = list
        countTotalPrice()
        notifyDataSetChanged()
    }

    private fun countTotalPrice() {
        var totalPrice = 0
        for (item in routePriceList) totalPrice += item.priceNumeric
        for (item in othersPriceList) totalPrice += item.priceNumeric
        for (item in amenityPriceList) totalPrice += item.priceNumeric
        listener.onPriceChangeListener(FlightCurrencyFormatUtil.convertToIdrPrice(totalPrice), totalPrice)
    }

    fun getTotalPriceWithoutAmenities(): Int {
        var totalPrice = 0
        for (item in routePriceList) totalPrice += item.priceNumeric
        for (item in othersPriceList) totalPrice += item.priceNumeric
        return totalPrice
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

