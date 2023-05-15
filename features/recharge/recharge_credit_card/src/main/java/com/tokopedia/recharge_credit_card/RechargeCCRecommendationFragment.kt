package com.tokopedia.recharge_credit_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_credit_card.databinding.FragmentRechargeCcRecommendationBinding
import com.tokopedia.recharge_credit_card.di.RechargeCCComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RechargeCCRecommendationFragment() : BaseDaggerFragment() {

    private var recommendations: ArrayList<TopupBillsRecommendation> = arrayListOf()
    private var isShowTitle: Boolean = false
    private var listener: TopupBillsRecentNumberListener? = null

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
        arguments?.let {
            recommendations = it.getParcelableArrayList(EXTRA_LIST_RECOMMENDATION) ?: arrayListOf()
            isShowTitle = it.getBoolean(EXTRA_IS_SHOW_TITLE, false)
        }
        initRecentTransactionWidget()
    }

    fun setListener(listener: TopupBillsRecentNumberListener) {
        this.listener = listener
    }

    private fun initRecentTransactionWidget() {
        binding?.recentTransactionWidget?.run {
            listener?.let {
                if (recommendations.isNotEmpty()) {
                    hideEmptyState()
                    setRecentNumbers(recommendations)
                    setListener(it)
                    setListenerRecentNumber(it)
                    toggleTitle(isShowTitle)
                } else {
                    showEmptyState()
                }
            }
        }
    }

    private fun showEmptyState() {
        binding?.run {
            recentTransactionWidget.hide()
            recentTransactionEmptyStateWidget.show()
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            recentTransactionWidget.show()
            recentTransactionEmptyStateWidget.hide()
        }
    }

    companion object {
        private const val EXTRA_IS_SHOW_TITLE = "IS_SHOW_TITLE"
        private const val EXTRA_LIST_RECOMMENDATION = "LIST_RECOMMENDATION"
        fun newInstance(recommendations: List<TopupBillsRecommendation>, isShowTitle: Boolean):
            Fragment {
            val fragment = RechargeCCRecommendationFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_IS_SHOW_TITLE, isShowTitle)
            bundle.putParcelableArrayList(EXTRA_LIST_RECOMMENDATION, ArrayList(recommendations))
            fragment.arguments = bundle
            return fragment
        }
    }
}
