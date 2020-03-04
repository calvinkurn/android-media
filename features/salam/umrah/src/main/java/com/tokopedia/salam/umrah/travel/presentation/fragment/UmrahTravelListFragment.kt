package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelListAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelListViewModel
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class UmrahTravelListFragment : BaseListFragment<TravelAgent, UmrahTravelListAdapterTypeFactory>(), BaseEmptyViewHolder.Callback{

    @Inject
    lateinit var umrahTravelListViewModel: UmrahTravelListViewModel

    override fun getAdapterTypeFactory(): UmrahTravelListAdapterTypeFactory = UmrahTravelListAdapterTypeFactory(this)

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun loadData(page: Int) {
        requestData(page)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_list, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahTravelListViewModel.travelAgents.observe(this, Observer {
            when(it){
                is Success -> {
                    onSuccessResult(it.data)
                }

                is Fail -> {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, null, null, null, R.drawable.img_umrah_pdp_empty_state) {
                        loadInitialData()
                    }
                }
            }
        })
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_list

    private fun requestData(page: Int){
        umrahTravelListViewModel.requestTravelAgentsData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_common_travel_agents),
                page, true
        )
    }

    private fun onSuccessResult(data : UmrahTravelAgentsEntity){
        renderList(data.umrahTravelAgents, true)
    }

    override fun onEmptyButtonClicked() {

    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(t: TravelAgent?) {

    }

    companion object{
        fun createInstance() = UmrahTravelListFragment()
    }
}