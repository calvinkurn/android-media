package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.FragmentEmoneyRecentNumberBinding
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpRecentTransactionAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.RecentTransactionViewHolder
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpRecentTransactionFragment : BaseDaggerFragment(), RecentTransactionViewHolder.ActionListener {
    override fun getScreenName(): String = ""

    private var binding by autoCleared<FragmentEmoneyRecentNumberBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmoneyRecentNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recommendationData = arguments?.getParcelableArrayList<TopupBillsRecommendation>(EXTRA_RECENT_TRANSACTION)
                ?: arrayListOf()
        binding.emoneyRecentNumberList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.emoneyRecentNumberList.adapter = EmoneyPdpRecentTransactionAdapter(recommendationData, this)

    }

    override fun onClickItem(item: TopupBillsRecommendation) {
        emoneyPdpViewModel.setSelectedRecentNumber(item)
    }

    companion object {
        const val EXTRA_RECENT_TRANSACTION = "EXTRA_RECENT_TRANSACTION"

        fun newInstance(recentTransactions: ArrayList<TopupBillsRecommendation>): Fragment = EmoneyPdpRecentTransactionFragment().also {
            it.arguments = Bundle().apply {
                putParcelableArrayList(EXTRA_RECENT_TRANSACTION, recentTransactions)
            }
        }
    }
}