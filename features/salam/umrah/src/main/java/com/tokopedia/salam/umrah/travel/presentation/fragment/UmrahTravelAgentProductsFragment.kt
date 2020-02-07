package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.travel.data.UmrahTravelProduct
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelProductAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class UmrahTravelAgentProductsFragment: BaseListFragment<UmrahTravelProduct, UmrahTravelProductAdapterTypeFactory>(), BaseEmptyViewHolder.Callback{

    var products : List<UmrahTravelProduct> =  emptyList()
    private val searchParam = UmrahSearchProductDataParam()
    private var slugName: String? = ""

    @Inject
    lateinit var umrahTravelProductViewModel: UmrahTravelProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = arguments?.getString(UmrahTravelFragment.EXTRA_SLUGNAME, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_products,container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahTravelProductViewModel.searchResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetResult(it.data)
                }
                is Fail -> {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, null, null, null, R.drawable.img_umrah_pdp_empty_state) {
                        loadInitialData()
                    }
                }

            }
        })
    }


    private fun onSuccessGetResult(data: List<UmrahTravelProduct>){
        renderList(data, data.size >= searchParam.limit)
    }

    private fun requestData(page:Int){
        slugName?.let {
            umrahTravelProductViewModel.getDataProductTravel(page,it,
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_search_product))
        }
    }

    override fun getAdapterTypeFactory(): UmrahTravelProductAdapterTypeFactory = UmrahTravelProductAdapterTypeFactory(this)

    override fun getScreenName(): String  = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun loadData(page: Int) {
        requestData(page)
    }

    override fun onEmptyButtonClicked() {
        RouteManager.route(context, ApplinkConst.SALAM_UMRAH)
    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(t: UmrahTravelProduct) {

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

    companion object {
        fun createInstance(slugName: String) =
                UmrahTravelAgentProductsFragment().also {
                    it.arguments = Bundle().apply {
                        putString(UmrahTravelFragment.EXTRA_SLUGNAME, slugName)
                    }
                }
    }
}
