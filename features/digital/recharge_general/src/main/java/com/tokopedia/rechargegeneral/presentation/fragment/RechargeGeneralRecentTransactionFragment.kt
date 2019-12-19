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
import com.tokopedia.common.topupbills.widget.TopupBillsRecentTransactionWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import kotlinx.android.synthetic.main.fragment_digital_recommendation.*
import javax.inject.Inject

class RechargeGeneralRecentTransactionFragment: BaseDaggerFragment(), TopupBillsRecentTransactionWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: SharedRechargeGeneralViewModel

    private lateinit var recommendationList: ArrayList<TopupBillsRecommendation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_recommendation, container, false)
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
        }

        with(recent_transaction_widget) {
            setListener(this@RechargeGeneralRecentTransactionFragment)
            if (::recommendationList.isInitialized && recommendationList.isNotEmpty()) setRecentNumbers(recommendationList, false)
        }
    }

    override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int, position: Int) {
        viewModel.setRecommendationItem(topupBillsRecommendation)
        // TODO: Add tracking
    }

    override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {
        // TODO: Add tracking
    }

    fun toggleTitleVisibility(value: Boolean) {
        recent_transaction_widget.toggleTitleVisibility(value)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(RechargeGeneralComponent::class.java).inject(this)
    }

    companion object {
        private const val EXTRA_PARAM_RECOMMENDATION = "EXTRA_PARAM_RECOMMENDATION"

        fun newInstance(recommendation: List<TopupBillsRecommendation>): RechargeGeneralRecentTransactionFragment {
            val fragment = RechargeGeneralRecentTransactionFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_PARAM_RECOMMENDATION, ArrayList(recommendation))
            fragment.arguments = bundle
            return fragment
        }
    }

}