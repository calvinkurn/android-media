package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpPromoListSpaceID
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_emoney_promo_list.*
import javax.inject.Inject

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpPromoListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }
    private val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_promo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

    private fun renderView() {
        val promoData = (topUpBillsViewModel.menuDetailData.value as Success).data.promos
        emoneyPdpPromoListWidget.setPromoList(promoData)
        emoneyPdpPromoListWidget.toggleTitle(false)

        emoneyPdpPromoListWidget.getRecyclerView().addItemDecoration(EmoneyPdpPromoListSpaceID())
    }
}