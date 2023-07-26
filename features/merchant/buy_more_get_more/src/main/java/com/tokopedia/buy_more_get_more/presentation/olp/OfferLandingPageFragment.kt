package com.tokopedia.buy_more_get_more.presentation.olp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.buy_more_get_more.utils.BundleConstant
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class OfferLandingPageFragment :
    BaseDaggerFragment(),
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        @JvmStatic
        fun newInstance(shopId: String) = OfferLandingPageFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_SHOP_ID, shopId)
            }
        }
    }

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()

    @Inject
    lateinit var viewModel: OfferLandingPageViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerBuyMoreGetMoreComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferLandingPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        viewModel.getOfferingIndo(listOf(0), "2323")
    }

    private fun setupObservables() {
        viewModel.offeringInfo.observe(viewLifecycleOwner) { offerInfoForBuyer ->
            Log.d("Masuk", offerInfoForBuyer.offeringJsonData)
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
            Log.d("Masuk", throwable.localizedMessage)
        }
    }
}
