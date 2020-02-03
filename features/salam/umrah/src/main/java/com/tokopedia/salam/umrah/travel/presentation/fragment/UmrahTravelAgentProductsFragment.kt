package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelAgentProductsAdapter
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelProductAdapterTypeFactory
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_products.*

class UmrahTravelAgentProductsFragment(private val listener: UmrahTravelAgentProductListener): BaseListFragment<UmrahProductModel.UmrahProduct, UmrahTravelProductAdapterTypeFactory>(), BaseEmptyViewHolder.Callback{

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
        renderList(products)
    }

    override fun getAdapterTypeFactory(): UmrahTravelProductAdapterTypeFactory = UmrahTravelProductAdapterTypeFactory(this)

    override fun getScreenName(): String  = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun loadData(page: Int) {

    }

    override fun onEmptyButtonClicked() {
        RouteManager.route(context, ApplinkConst.SALAM_UMRAH)
    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(t: UmrahProductModel.UmrahProduct?) {

    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_products

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.umrah_img_empty_search_png
        emptyModel.title = getString(R.string.umrah_search_empty_title)
        emptyModel.content = getString(R.string.umrah_search_empty_subtitle)
        emptyModel.buttonTitle = getString(R.string.umrah_empty_button)
        return emptyModel
    }


    interface UmrahTravelAgentProductListener{
        fun getDataProducts():List<UmrahProductModel.UmrahProduct>
    }
}
