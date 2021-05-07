package com.tokopedia.flight.promo_chips.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.R
import com.tokopedia.flight.promo_chips.presentation.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.presentation.adapter.FlightPromoChipsAdapterTypeFactory
import com.tokopedia.flight.promo_chips.presentation.adapter.viewholder.FlightPromoChipsViewHolder
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.include_flight_promo_chips.view.*

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
        setBackground()
    }

    private fun setBackground(){
        recyclerView.setBackgroundResource(R.drawable.bg_flight_promo_chips_container)
    }

    fun setListener(listener: PromoChipsListener) {
        this.listener = listener
    }

    fun renderPromoList(airlineList: List<AirlinePrice>) {
        flight_promo_chips_widget_title.show()

        listPromo = airlineList
        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(airlineList)

        adapter = FlightPromoChipsAdapter(context, FlightPromoChipsAdapterTypeFactory(object : FlightPromoChipsViewHolder.OnFlightPromoChipsListener {
            override fun onItemClicked(airlinePrice: AirlinePrice, adapterPosition: Int) {
                listener.onClickPromoChips(airlinePrice, adapterPosition)
            }

            override fun onItemUnselected() {
                listener.onUnselectChips()
            }
        }))

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter.renderList(dataCollection)
    }

    fun resetState(){
        if(adapter.itemCount > 0 && adapter.selectedPosition != FlightPromoChipsAdapter.SELECTED_POSITION_INIT){
            recyclerView.findViewHolderForAdapterPosition(adapter.selectedPosition).let {
                (it as FlightPromoChipsViewHolder).itemView.performClick()
            }
        }
    }

    interface PromoChipsListener{
        fun onClickPromoChips(airlinePrice: AirlinePrice, position: Int)
        fun onUnselectChips()
    }
}