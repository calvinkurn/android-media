package com.tokopedia.pdpsimulation.paylater.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.common.presentation.fragment.PdpSimulationFragment
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterAllData
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterOfferPagerAdapter
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater_offers.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PayLaterOffersFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    lateinit var payLaterProductList: List<PayLaterAllData>


    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider =
            ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val pagerAdapter: PayLaterOfferPagerAdapter by lazy {
        PayLaterOfferPagerAdapter(childFragmentManager, 0)
    }
    var pdpSimulationCallback: PdpSimulationCallback? = null
    var productAmount: Long = 0


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

    }

    private fun generateSortFilter(paylaterProduct: PayLaterGetSimulation) {
        val filterData = ArrayList<SortFilterItem>()
        paylaterProduct.productList?.let {
            for (i in it.indices) {
                it[i].text?.let { name ->
                    if (i == 0) {
                        filterData.add(
                            SortFilterItem(
                                name,
                                ChipsUnify.TYPE_SELECTED,
                                ChipsUnify.SIZE_SMALL
                            ) {
                                selectOtherTenure(i, name)
                            })
                        pdpSimulationCallback?.sendAnalytics(
                            PdpSimulationEvent.PayLater.TenureSortFilterClicker(
                                name
                            )
                        )
                    } else {
                        filterData.add(SortFilterItem(name) {
                            selectOtherTenure(i, name)
                        })
                    }
                }
            }
        }
        sortFilter.addItem(filterData)


    }

    private fun selectOtherTenure(position: Int, name: String) {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.TenureSortFilterClicker(
                name
            )
        )
        paymentOptionViewPager.post {
            payLaterProductList[position].detail?.let { detailList ->
                pagerAdapter.setPaymentData(detailList)
            }
        }


    }


    private fun observeViewModel() {
        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> payLaterAvailableDataLoad(it.data)
                is Fail -> payLaterAvailableDataLoadFail(it.throwable)
            }
        })


    }

    private fun payLaterAvailableDataLoadFail(throwable: Throwable) {
        payLaterOffersShimmerGroup.gone()
        emptyStateError.gone()
        payLaterDataGroup.gone()
        payLaterOffersGlobalError.visible()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                payLaterOffersGlobalError.setType(GlobalError.NO_CONNECTION)
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
            payLaterViewModel.getPayLaterAvailableDetail(productAmount)
            val parentFrag: PdpSimulationFragment =
                this.parentFragment as PdpSimulationFragment
            parentFrag.reloadProductDetail()
        }
    }

    private fun payLaterAvailableDataLoad(paylaterProduct: PayLaterGetSimulation) {
        payLaterOffersGlobalError.gone()
        if (paylaterProduct.productList == null || paylaterProduct.productList.isEmpty()) {
            emptyStateError.visible()
            payLaterOffersShimmerGroup.gone()
            payLaterDataGroup.gone()
            emptyStateError.setPrimaryCTAClickListener {
                emptyStateError.gone()
                payLaterOffersShimmerGroup.visible()
                payLaterViewModel.getPayLaterAvailableDetail(productAmount)
            }
        } else {
            payLaterProductList = paylaterProduct.productList
            emptyStateError.gone()
            payLaterOffersShimmerGroup.gone()
            payLaterDataGroup.visible()
            generateSortFilter(paylaterProduct)
            paymentOptionViewPager.post {
                payLaterProductList[0].detail?.let { detailList ->
                    pagerAdapter.setPaymentData(detailList)
                }
            }
        }
    }

    fun setViewPagerAdapterListner() {
        paymentOptionViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val detail = pagerAdapter.getPaymentDetailByPosition(position)
                detail?.let {
                    onPageSelectedByUser(it)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun onPageSelectedByUser(it: Detail) {
        if (!it.isInvoke) {
            pdpSimulationCallback?.sendAnalytics(
                PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
                    it.gateway_detail?.name ?: "",
                    it.cta?.name ?: "",
                    it.tenure ?: 0
                )
            )
            it.isInvoke = true
        }
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }

    private fun renderTabAndViewPager() {
        context?.let {
            paymentOptionViewPager.adapter = pagerAdapter
            setViewPagerAdapterListner()
            paymentOptionViewPager.pageMargin = PAGE_MARGIN.dpToPx(it.resources.displayMetrics)
        }
    }


    companion object {
        const val PAGE_MARGIN = 16

        @JvmStatic
        fun newInstance(
            pdpSimulationCallback: PdpSimulationCallback,
            bundle: Bundle
        ): PayLaterOffersFragment {
            val detailFragment = PayLaterOffersFragment()
            detailFragment.pdpSimulationCallback = pdpSimulationCallback
            detailFragment.productAmount = bundle.getLong(PRODUCT_PRICE)
            return detailFragment
        }
    }

}
