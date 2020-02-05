package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.TravelAgent
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_info.*
import javax.inject.Inject

class UmrahTravelAgentInfoFragment(private val listener: UmrahTravelAgentInfoListener) : Fragment(){

    var travelAgent : TravelAgent = TravelAgent()

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_info,container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        travelAgent = listener.getDataInfo()
        buildDesc(travelAgent.description)
        tv_umrah_travel_info_address.text = travelAgent.address
        tv_umrah_travel_info_years.text = travelAgent.ui.establishedSince
        tv_umrah_travel_info_pilgrims.text = getString(R.string.umrah_travel_pilgrims_info,travelAgent.ui.pilgrimsPerYear)
    }

    fun buildDesc(desc: String){
        tv_umrah_travel_info_desc.truncateDescription = true
        tv_umrah_travel_info_desc.setDescription(desc)
        tv_umrah_travel_info_desc.buildView()
    }

    interface UmrahTravelAgentInfoListener{
        fun getDataInfo():TravelAgent
    }
}