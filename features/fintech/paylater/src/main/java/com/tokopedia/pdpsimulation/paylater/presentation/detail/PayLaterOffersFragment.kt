package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.PaylaterGetSimulationV2
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferPagerAdapter
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater_offers.*
import javax.inject.Inject

class PayLaterOffersFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val pagerAdapter: PayLaterOfferPagerAdapter by lazy {
        PayLaterOfferPagerAdapter(childFragmentManager, 0)
    }
    var pdpSimulationCallback: PdpSimulationCallback? = null

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_paylater_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
        observeViewModel()

        val filterData = ArrayList<SortFilterItem>()
        filterData.add(SortFilterItem(
            "abc"
        ) { })
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))
        filterData.add(SortFilterItem("abc"))

        sortFilter.addItem(filterData)
    }


    private fun observeViewModel() {
        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> payLaterAvailableDataLoad(it.data)
                is Fail -> payLaterAvailableDataLoadFail()
            }
        })


    }

    private fun payLaterAvailableDataLoadFail() {

    }

    private fun payLaterAvailableDataLoad(paylaterProduct: PaylaterGetSimulationV2) {

        val payLaterProduct = paylaterProduct.data
        payLaterOffersShimmerGroup.gone()
        payLaterDataGroup.visible()
        paymentOptionViewPager.post {
            pagerAdapter.setPaymentData(payLaterProduct!![0].detail!!)
        }
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }

    private fun renderTabAndViewPager() {
        context?.let {
            paymentOptionViewPager.adapter = pagerAdapter
            paymentOptionViewPager.pageMargin = PAGE_MARGIN.dpToPx(it.resources.displayMetrics)
        }
    }

//
//    // set payLater + application status data in pager adapter
//    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {
//        payLaterOffersShimmerGroup.gone()
//        payLaterDataGroup.visible()
//        val payLaterProductList = ArrayList<PayLaterItemProductData>()
//        payLaterProductList.addAll(payLaterViewModel.getPayLaterOptions())
//        paymentOptionViewPager.post {
//            pagerAdapter.setPaymentData(payLaterProductList, data.applicationDetailList
//                    ?: arrayListOf())
//        }
//    }
//
//    private fun onPayLaterApplicationLoadingFail() {
//        // set payLater data in view pager
//        paymentOptionViewPager.post {
//            if (payLaterViewModel.getPayLaterOptions().isNotEmpty()) {
//                try {
//                    payLaterOffersShimmerGroup.gone()
//                    payLaterDataGroup.visible()
//                    pagerAdapter.setPaymentData(payLaterViewModel.getPayLaterOptions(), arrayListOf())
//                } catch (e: Exception) {
//                    Timber.e(e)
//                }
//            }
//        }
//    }

    companion object {
        const val PAGE_MARGIN = 16

        @JvmStatic
        fun newInstance(pdpSimulationCallback: PdpSimulationCallback): PayLaterOffersFragment {
            val detailFragment = PayLaterOffersFragment()
            detailFragment.pdpSimulationCallback = pdpSimulationCallback
            return detailFragment
        }
    }

}
