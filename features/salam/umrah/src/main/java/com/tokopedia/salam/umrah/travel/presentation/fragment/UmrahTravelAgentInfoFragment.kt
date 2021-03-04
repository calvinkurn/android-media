package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.presentation.widget.TruncateDescriptionWidget
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelFragment.Companion.EXTRA_SLUGNAME
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_info.*
import javax.inject.Inject

class UmrahTravelAgentInfoFragment : BaseDaggerFragment(), TruncateDescriptionWidget.TruncateDescriptionTrackingListener {

    var travelAgent: TravelAgent = TravelAgent()

    @Inject
    lateinit var umrahTravelInfoViewModel: UmrahTravelInfoViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics


    private var slugName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = arguments?.getString(EXTRA_SLUGNAME, "")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
    }

    fun setupAll(data: TravelAgent) {
        travelAgent = data
        buildDesc(travelAgent.description)
        tv_umrah_travel_info_address.text = travelAgent.address
        tv_umrah_travel_info_years.text = travelAgent.ui.establishedSince
        tv_umrah_travel_info_pilgrims.text = getString(R.string.umrah_travel_pilgrims_info, travelAgent.ui.pilgrimsPerYear)
    }

    fun buildDesc(desc: String) {
        tv_umrah_travel_info_desc.truncateDescription = true
        tv_umrah_travel_info_desc.setDescription(desc)
        tv_umrah_travel_info_desc.buildView(this)
    }


    private fun requestData() {
        slugName?.let {
            umrahTravelInfoViewModel.requestTravelData(
                    UmrahQuery.UMRAH_TRAVEL_BY_SLUGNAME, it)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        umrahTravelInfoViewModel.travelAgentData.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupAll(it.data.umrahTravelAgentBySlug)
                }
                is Fail -> {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, it.throwable.message, null, null, R.drawable.umrah_img_empty_search_png) {
                        requestData()
                    }
                }

            }

        })
    }

    override fun onClicked() {
        umrahTrackingUtil.umrahTravelAgentClickSelengkapnya()
    }

    companion object {
        fun createInstance(slugName: String) =
                UmrahTravelAgentInfoFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUGNAME, slugName)
                    }
                }
    }
}