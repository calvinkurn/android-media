package com.tokopedia.tokofood.feature.search.srp.presentation.fragment

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.TokofoodSearchResultAdapterTypeFactory
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.TokofoodSearchResultDiffer
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.TokofoodSearchResultPageAdapter
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.viewholder.MerchantSearchResultViewHolder
import javax.inject.Inject

class TokofoodSearchResultPageFragment : BaseMultiFragment(), IBaseMultiFragment,
    MerchantSearchResultViewHolder.TokoFoodMerchantSearchResultListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter by lazy {
        TokofoodSearchResultPageAdapter(
            typeFactory = TokofoodSearchResultAdapterTypeFactory(this, this),
            differ = TokofoodSearchResultDiffer()
        )
    }

    override fun getFragmentToolbar(): Toolbar? = null

    override fun getFragmentTitle(): String? = null

    override fun onBranchButtonClicked(branchApplink: String) {
        TODO("Not yet implemented")
    }

    override fun onClickRetryError() {
        TODO("Not yet implemented")
    }

    override fun onClickMerchant(merchant: Merchant, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onImpressMerchant(merchant: Merchant, position: Int) {
        TODO("Not yet implemented")
    }



}