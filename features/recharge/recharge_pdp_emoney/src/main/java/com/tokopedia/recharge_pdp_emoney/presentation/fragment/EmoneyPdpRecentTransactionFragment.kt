package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpRecentTransactionAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.RecentTransactionViewHolder
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_emoney_recent_number.*
import javax.inject.Inject

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpRecentTransactionFragment : BaseDaggerFragment(), RecentTransactionViewHolder.ActionListener {
    override fun getScreenName(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }
    private val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_recent_number, container, false)
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recommendationData = (topUpBillsViewModel.menuDetailData.value as Success).data.recommendations

        emoneyRecentNumberList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        emoneyRecentNumberList.adapter = EmoneyPdpRecentTransactionAdapter(recommendationData, this)
    }

    override fun onClickItem(item: TopupBillsRecommendation) {
        emoneyPdpViewModel.setSelectedRecentNumber(item)
    }
}