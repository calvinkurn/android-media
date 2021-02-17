package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferPagerAdapter
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater_offers.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
            paymentOptionViewPager.pageMargin = PAGE_MARGIN.dpToPx(it.resources.displayMetrics)
        }
    }

    private fun onPayLaterDataLoaded(data: PayLaterProductData) {
        payLaterViewModel.getPayLaterApplicationStatus(true)
    }

    private fun onPayLaterDataLoadingFail(throwable: Throwable) {
        payLaterViewModel.getPayLaterApplicationStatus(false)
        payLaterOffersShimmerGroup.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                pdpSimulationCallback?.showNoNetworkView()
                return
            }
            is IllegalStateException -> {
                payLaterOffersGlobalError.setType(GlobalError.PAGE_FULL)
            }
            else -> {
                payLaterOffersGlobalError.setType(GlobalError.SERVER_ERROR)
            }
        }
        payLaterOffersGlobalError.show()
        payLaterOffersGlobalError.setActionClickListener {
            payLaterOffersGlobalError.hide()
            payLaterOffersShimmerGroup.visible()
            payLaterViewModel.getPayLaterProductData()
        }
    }

    // set payLater + application status data in pager adapter
    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        payLaterOffersShimmerGroup.gone()
        payLaterDataGroup.visible()
        val payLaterProductList = ArrayList<PayLaterItemProductData>()
        payLaterProductList.addAll(payLaterViewModel.getPayLaterOptions())
        paymentOptionViewPager.post {
            pagerAdapter.setPaymentData(payLaterProductList, data.applicationDetailList
                    ?: arrayListOf())
        }
    }

    private fun onPayLaterApplicationLoadingFail(throwable: Throwable) {
        // set payLater data in view pager
        paymentOptionViewPager.post {
            if (payLaterViewModel.getPayLaterOptions().isNotEmpty()) {
                payLaterOffersShimmerGroup.gone()
                payLaterDataGroup.visible()
                pagerAdapter.setPaymentData(payLaterViewModel.getPayLaterOptions(), arrayListOf())
            }
        }
    }

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