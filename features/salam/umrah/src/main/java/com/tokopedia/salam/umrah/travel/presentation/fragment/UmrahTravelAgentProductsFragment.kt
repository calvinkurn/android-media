package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelAgentProductsAdapter
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_products.*

class UmrahTravelAgentProductsFragment(private val listener: UmrahTravelAgentProductListener): Fragment(){

    var products : List<UmrahProductModel.UmrahProduct> =  emptyList()
    val adapterProducts = UmrahTravelAgentProductsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_products,container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        products = listener.getDataProducts()
        adapterProducts.setList(products)
        rv_umrah_travel_products.apply {
            isNestedScrollingEnabled = false
            adapter = adapterProducts
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
            onFlingListener = object : RecyclerView.OnFlingListener() {
                override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                    rv_umrah_travel_products.dispatchNestedFling(velocityX.toFloat(), velocityY.toFloat(), false)
                    return false
                }
            }
        }

    }

    interface UmrahTravelAgentProductListener{
        fun getDataProducts():List<UmrahProductModel.UmrahProduct>
    }
}
