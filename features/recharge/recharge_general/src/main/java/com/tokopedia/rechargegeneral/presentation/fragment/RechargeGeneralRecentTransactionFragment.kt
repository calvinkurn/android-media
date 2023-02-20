package com.tokopedia.rechargegeneral.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.rechargegeneral.databinding.FragmentRechargeGeneralRecommendationBinding
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class RechargeGeneralRecentTransactionFragment : BaseDaggerFragment(), TopupBillsRecentNumberListener {

    private var binding by autoClearedNullable<FragmentRechargeGeneralRecommendationBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: SharedRechargeGeneralViewModel

    private lateinit var recommendationList: ArrayList<TopupBillsRecommendation>
    private var showTitle = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeGeneralRecommendationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedRechargeGeneralViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            recommendationList = it.getParcelableArrayList(EXTRA_PARAM_RECOMMENDATION) ?: arrayListOf()
            showTitle = it.getBoolean(EXTRA_PARAM_SHOW_TITLE, true)
        }

        binding?.recentTransactionWidget?.run {
            setListener(this@RechargeGeneralRecentTransactionFragment)
            if (::recommendationList.isInitialized && recommendationList.isNotEmpty()) {
                setRecentNumbers(recommendationList)
                toggleTitle(showTitle)
            }
        }
    }

    override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: String, position: Int) {
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
