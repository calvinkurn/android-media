package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.*
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
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

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paylater_offers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterActivityResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPayLaterDataLoaded(it.data)
                is Fail -> onPayLaterDataLoadingFail(it.throwable)
            }
        })

        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPayLaterApplicationStatusLoaded(it.data)
                is Fail -> onPayLaterApplicationLoadingFail(it.throwable)
            }
        })
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }

    private fun renderTabAndViewPager() {
        context?.let {
            paymentOptionViewPager.adapter = pagerAdapter
            // @Todo 16 dp to constants
            paymentOptionViewPager.pageMargin = PAGE_MARGIN.dpToPx(it.resources.displayMetrics)
        }
    }

    private fun onPayLaterDataLoaded(data: PayLaterProductData) {
        // hide loading
        if (data.productList?.isNullOrEmpty() == false)
            payLaterViewModel.getPayLaterApplicationStatus()
    }

    private fun onPayLaterDataLoadingFail(throwable: Throwable) {
        // show error layout
    }

    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        // set payLater + application status data in pager adapter
        val payLaterProductList = ArrayList<PayLaterItemProductData>()
        payLaterProductList.addAll(payLaterViewModel.getPayLaterOptions())
        // @Todo remove below lines
        payLaterProductList.add(payLaterProductList[0])
        payLaterProductList.add(payLaterProductList[0])
        paymentOptionViewPager.post {
            pagerAdapter.setPaymentData(payLaterProductList, arrayListOf())
        }
    }

    private fun onPayLaterApplicationLoadingFail(throwable: Throwable) {
        // set payLater data in view pager
        paymentOptionViewPager.post {
            pagerAdapter.setPaymentData(payLaterViewModel.getPayLaterOptions(), arrayListOf())

        }
    }

    companion object {
        const val PAGE_MARGIN = 16
        @JvmStatic
        fun newInstance() =
                PayLaterOffersFragment()
    }
}