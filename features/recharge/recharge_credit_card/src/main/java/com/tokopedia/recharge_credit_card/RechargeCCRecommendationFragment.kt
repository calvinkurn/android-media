package com.tokopedia.recharge_credit_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.recharge_credit_card.databinding.FragmentRechargeCcRecommendationBinding
import com.tokopedia.recharge_credit_card.di.RechargeCCComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RechargeCCRecommendationFragment(
    private val recommendations: List<TopupBillsRecommendation>,
    private val isShowTitle: Boolean,
    private val listener: TopupBillsRecentNumberListener
): BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentRechargeCcRecommendationBinding>()

    override fun getScreenName(): String {
        return RechargeCCRecommendationFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(RechargeCCComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRechargeCcRecommendationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecentTransactionWidget()
    }

    private fun initRecentTransactionWidget() {
        binding?.recentTransactionWidget?.run {
            if (recommendations.isNotEmpty()) {
                setRecentNumbers(recommendations)
                setListener(listener)
                setListenerRecentNumber(listener)
                toggleTitle(isShowTitle)
            }
        }
    }
}
