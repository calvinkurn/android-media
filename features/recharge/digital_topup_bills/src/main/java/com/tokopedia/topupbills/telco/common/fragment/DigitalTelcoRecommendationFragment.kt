package com.tokopedia.topupbills.telco.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.viewmodel.SharedTelcoViewModel
import com.tokopedia.topupbills.telco.common.widget.DigitalTelcoRecentTransactionWidget
import javax.inject.Inject

class DigitalTelcoRecommendationFragment : BaseDaggerFragment() {

    private lateinit var viewModel: SharedTelcoViewModel
    private lateinit var recentNumbersWidget: DigitalTelcoRecentTransactionWidget

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedTelcoViewModel::class.java)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoRecommendationFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(DigitalTelcoComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_telco_recommendation, container, false)
        recentNumbersWidget = view.findViewById(R.id.recent_transaction)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recommendations.observe(this, Observer {
            recentNumbersWidget.setRecentNumbers(it)
        })

        viewModel.titleMenu.observe(this, Observer {
            recentNumbersWidget.toggleTitle(it)
        })

        recentNumbersWidget.setListenerRecentTelco(object : TopupBillsRecentNumberListener {
            override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int,
                                             position: Int) {
                topupBillsRecommendation.position = position
                viewModel.setSelectedRecentNumber(topupBillsRecommendation)
            }

            override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {
                topupAnalytics.impressionEnhanceCommerceRecentTransaction(topupBillsTrackRecentList)
            }
        })
    }

    companion object {

        fun newInstance(): Fragment {
            return DigitalTelcoRecommendationFragment()
        }
    }
}