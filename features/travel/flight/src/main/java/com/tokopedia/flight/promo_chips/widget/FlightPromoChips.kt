package com.tokopedia.flight.promo_chips.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.R
import com.tokopedia.flight.promo_chips.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.adapter.FlightPromoChipsAdapterTypeFactory
import com.tokopedia.flight.promo_chips.adapter.viewholder.FlightPromoChipsViewHolder
import com.tokopedia.flight.promo_chips.model.AirlinePrice
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * author by astidhiyaa on 16/02/21.
 */

class FlightPromoChips @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {
    private var recyclerView: RecyclerView
    private lateinit var adapter: FlightPromoChipsAdapter
    private lateinit var listener: PromoChipsListener
    private lateinit var listPromo: List<AirlinePrice>

    init {
        val view = View.inflate(context, R.layout.include_flight_promo_chips, this)
        recyclerView = view.findViewById(R.id.recycler_view_promo_chips)
    }

    fun setListener(listener: PromoChipsListener) {
        this.listener = listener
    }

    fun renderPromoList(airlineList: List<AirlinePrice>) {
        listPromo = airlineList
        recyclerView.visibility = View.VISIBLE
        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(airlineList)

        adapter = FlightPromoChipsAdapter(context, FlightPromoChipsAdapterTypeFactory(object : FlightPromoChipsViewHolder.OnFlightPromoChipsListener {
            override fun onItemClicked(airlinePrice: AirlinePrice, adapterPosition: Int) {
                listener.onClickPromoChips(airlinePrice, adapterPosition)
            }
        }))

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter.renderList(dataCollection)
    }

    interface PromoChipsListener{
        fun onClickPromoChips(airlinePrice: AirlinePrice, position: Int)
    }
}