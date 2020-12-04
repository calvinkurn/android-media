package com.tokopedia.rechargegeneral.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import kotlinx.android.synthetic.main.fragment_recharge_general_recommendation.*
import javax.inject.Inject

class RechargeGeneralRecentTransactionFragment: BaseDaggerFragment(), TopupBillsRecentNumberListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: SharedRechargeGeneralViewModel

    private lateinit var recommendationList: ArrayList<TopupBillsRecommendation>
    private var showTitle = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_general_recommendation, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedRechargeGeneralViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            recommendationList = it.getParcelableArrayList(EXTRA_PARAM_RECOMMENDATION) ?: arrayListOf()
            showTitle = it.getBoolean(EXTRA_PARAM_SHOW_TITLE, true)
        }

        with(recent_transaction_widget) {
            setListener(this@RechargeGeneralRecentTransactionFragment)
            if (::recommendationList.isInitialized && recommendationList.isNotEmpty()) {
                setRecentNumbers(recommendationList)
                toggleTitle(showTitle)
            }
        }
    }

    override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int, position: Int) {
        topupBillsRecommendation.position = position
        viewModel.setRecommendationItem(topupBillsRecommendation)
    }

    override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(RechargeGeneralComponent::class.java).inject(this)
    }

    companion object {
        private const val EXTRA_PARAM_RECOMMENDATION = "EXTRA_PARAM_RECOMMENDATION"
        private const val EXTRA_PARAM_SHOW_TITLE = "EXTRA_PARAM_SHOW_TITLE"

        fun newInstance(recommendation: List<TopupBillsRecommendation>, showTitle: Boolean = true): RechargeGeneralRecentTransactionFragment {
            val fragment = RechargeGeneralRecentTransactionFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_PARAM_RECOMMENDATION, ArrayList(recommendation))
            bundle.putBoolean(EXTRA_PARAM_SHOW_TITLE, showTitle)
            fragment.arguments = bundle
            return fragment
        }
    }

}