package com.tokopedia.deals.checkout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMap
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_DETAIL
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_VERIFY
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import com.tokopedia.deals.databinding.FragmentDealsCheckoutBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DealsCheckoutFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentDealsCheckoutBinding>()
    private val viewModel by viewModels<DealsCheckoutViewModel> { viewModelFactory }
    private var dealsDetail: ProductDetailData? = null
    private var dealsVerify: EventVerifyResponse? = null
    private var dealsItemMap: ItemMapResponse? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsCheckoutComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsDetail = it.getParcelable(EXTRA_DEAL_DETAIL)
            dealsVerify = it.getParcelable(EXTRA_DEAL_VERIFY)
            dealsItemMap = dealsVerify?.metadata?.itemMap?.first()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsCheckoutBinding.inflate(inflater)
        return binding?.root
    }

    companion object {
        fun createInstance(productDetailData: ProductDetailData?, verifyData: EventVerifyResponse?): DealsCheckoutFragment {
            val fragment = DealsCheckoutFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_DEAL_DETAIL, productDetailData)
                putParcelable(EXTRA_DEAL_VERIFY, verifyData)
            }
            return fragment
        }
    }
}
