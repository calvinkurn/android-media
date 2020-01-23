package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahTravelActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelAgentViewPagerAdapter
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent.*
import java.util.*
import javax.inject.Inject

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelFragment: BaseDaggerFragment(){

    @Inject
    lateinit var umrahTravelViewModel: UmrahTravelViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var umrahTravelAgentViewPagerAdapter: UmrahTravelAgentViewPagerAdapter


    private var slugName : String ? = ""

    override fun getScreenName(): String = getString(R.string.umrah_travel_agent_title)

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = savedInstanceState?.getString(EXTRA_SLUG_NAME)
                ?: arguments?.getString(EXTRA_SLUG_NAME) ?: ""
    }
    private fun requestData(){
        slugName?.let {
            umrahTravelViewModel.requestPdpData(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_travel_by_slugname), it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        umrahTravelViewModel.travelAgentData.observe(this, Observer{
            when (it) {
                is Success ->{
                    setupAll(it.data)
                }
                is Fail ->{

                }

            }

        })
    }

    companion object{
        fun getInstance(slugName: String) =
                UmrahTravelFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUG_NAME, slugName)
                    }
                }
    }

    private fun setupAll(travelAgentBySlugName: UmrahTravelAgentBySlugNameEntity){
        setupTravelAgent(travelAgentBySlugName.umrahTravelAgentBySlug)
        setupViewPager(travelAgentBySlugName)
    }

    private fun setupViewPager(travelAgentBySlugName: UmrahTravelAgentBySlugNameEntity){
        umrahTravelAgentViewPagerAdapter = UmrahTravelAgentViewPagerAdapter(childFragmentManager,travelAgentBySlugName)
        vp_umrah_travel_agent.adapter = umrahTravelAgentViewPagerAdapter
        tl_umrah_travel_agent.setupWithViewPager(vp_umrah_travel_agent)
    }

    private fun setupTravelAgent(travelAgent: TravelAgent){
        val umrahItemWidgetModelData: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = travelAgent.name
            imageUri = travelAgent.imageUrl
            desc = travelAgent.permissionOfUmrah
        }

        iw_umrah_travel_agent.apply {
            umrahItemWidgetModel = umrahItemWidgetModelData
            buildView()
            setPermissionPdp()
            setVerifiedTravel()
        }
    }
}