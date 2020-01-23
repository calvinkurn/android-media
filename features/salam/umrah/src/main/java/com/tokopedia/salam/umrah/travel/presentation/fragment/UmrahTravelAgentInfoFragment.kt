package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahProductModel

class UmrahTravelAgentInfoFragment(private val listener: UmrahTravelAgentInfoListener) : Fragment(){

    var travelAgent : TravelAgent = TravelAgent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_info,container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        travelAgent = listener.getDataInfo()

    }

    interface UmrahTravelAgentInfoListener{
        fun getDataInfo():TravelAgent
    }
}