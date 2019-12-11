package com.tokopedia.digital.productV2.presentation.fragment

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
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.presentation.viewmodel.DigitalProductViewModel
import com.tokopedia.digital.productV2.presentation.viewmodel.SharedDigitalProductViewModel
import kotlinx.android.synthetic.main.fragment_digital_recommendation.*
import javax.inject.Inject

class DigitalProductRecentTransactionFragment: BaseDaggerFragment(), TopupBillsRecentTransactionWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: SharedDigitalProductViewModel

    private lateinit var recommendationList: ArrayList<TopupBillsRecommendation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_recommendation, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedDigitalProductViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            recommendationList = it.getParcelableArrayList(EXTRA_PARAM_RECOMMENDATION) ?: arrayListOf()
        }

        with(recent_transaction_widget) {
            setListener(this@DigitalProductRecentTransactionFragment)
            if (::recommendationList.isInitialized && recommendationList.isNotEmpty()) setRecentNumbers(recommendationList)
        }
    }

    override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int, position: Int) {
        viewModel.setRecommendationItem(topupBillsRecommendation)
        // TODO: Add tracking
    }

    override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {
        // TODO: Add tracking
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalProductComponent::class.java).inject(this)
    }

    companion object {
        private const val EXTRA_PARAM_RECOMMENDATION = "EXTRA_PARAM_RECOMMENDATION"

        fun newInstance(recommendation: List<TopupBillsRecommendation>): DigitalProductRecentTransactionFragment {
            val fragment = DigitalProductRecentTransactionFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_PARAM_RECOMMENDATION, ArrayList(recommendation))
            fragment.arguments = bundle
            return fragment
        }
    }

}