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

    private val productAmount: Long by lazy {
        arguments?.getLong(PRODUCT_PRICE) ?: 0
    }

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

    /**
     * This method generate the sort filter
     * @param paylaterProduct contain all detail of the tenure and we are displaying sort filter accordingly
     */

    private fun generateSortFilter(paylaterProduct: PayLaterGetSimulation) {
        val filterData = ArrayList<SortFilterItem>()
        paylaterProduct.productList?.let {
            setFilterItem(it, filterData)
        }
        sortFilter.addItem(filterData)
    }


    /**
     * This method set the filter item for the sort
     * @param filterData this is the filter data variable where we will add thew sort filter
     * @param list this is the list of data
     */

    private fun setFilterItem(list: List<PayLaterAllData>, filterData: ArrayList<SortFilterItem>) {
        for (i in list.indices) {
            list[i].text?.let { name ->
                if (i == payLaterViewModel.sortPosition) {
                    filterData.add(
                        SortFilterItem(
                            name,
                            ChipsUnify.TYPE_SELECTED,
                            ChipsUnify.SIZE_SMALL
                        ) {
                            selectOtherTenure(i, name)
                        })
                    // This analytics is to track the default selected one
                    pdpSimulationCallback?.sendAnalytics(
                        PdpSimulationEvent.PayLater.TenureSortFilterClicker(
                            name
                        )
                    )
                    payLaterProductList[payLaterViewModel.sortPosition].detail[payLaterViewModel.partnerDisplayPosition]?.let { detail ->
                        onPageSelectedByUser(detail)
                    }
                } else {
                    filterData.add(SortFilterItem(name, size = ChipsUnify.SIZE_SMALL) {
                        selectOtherTenure(i, name)
                    })
                }
            }
        }
    }

    /**
     * This contain click logic for anu sortFilter
     * @param position position of the sort filter clicked
     * @param name name of the sort filter clicked
     */

    private fun selectOtherTenure(position: Int, name: String) {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.TenureSortFilterClicker(
                name
            )
        )
        paymentOptionViewPager.post {

            payLaterProductList[position].detail[viewPagerPartnerDisplay(payLaterProductList[position])]
                ?.let { detail ->
                    onPageSelectedByUser(detail)
                }
            payLaterProductList[position].detail.let { detailList ->
                pagerAdapter.setPaymentData(detailList as List<Detail>)
                payLaterViewModel.sortPosition = position
            }

        }


    }

    /**
     * Method is to calculate which partner to display when a particular tenure is selected
     * @param payLaterAllData contain all the partners detail for a particular tenure
     * @return position of the partner which i have to display when i select a particular tenure
     */
    private fun viewPagerPartnerDisplay(payLaterAllData: PayLaterAllData): Int {
        return when {
            payLaterAllData.detail.isEmpty() -> 0
            payLaterAllData.detail.size <= payLaterViewModel.partnerDisplayPosition -> payLaterAllData.detail.size-1
            else -> payLaterViewModel.partnerDisplayPosition
        }

    }


    private fun observeViewModel() {
        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success ->
                    payLaterAvailableDataLoad(it.data)
                is Fail ->
                    payLaterAvailableDataLoadFail(it.throwable)

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
            pdpSimulationCallback?.reloadProductDetail()
        }
    }

    /**
     * This method pass network response to each viewpager for the paylater partner
     * @param paylaterProduct contains detail of the paylater partner
     */
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
            sortFilter.visibility = View.VISIBLE
            generateSortFilter(paylaterProduct)
            paymentOptionViewPager.post {
                payLaterProductList[payLaterViewModel.sortPosition].detail.let { detailList ->
                    pagerAdapter.setPaymentData(detailList as List<Detail>)
                }
            }
        }
    }

    private fun setViewPagerAdapterListner() {
        paymentOptionViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                payLaterViewModel.partnerDisplayPosition = position
                val detail = pagerAdapter.getPaymentDetailByPosition(position)
                detail?.let {
                    onPageSelectedByUser(it)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun onPageSelectedByUser(detail: Detail) {
        if (!detail.isInvoke) {
            pdpSimulationCallback?.sendAnalytics(
                PdpSimulationEvent.PayLater.PayLaterProductImpressionEvent(
                    detail.gateway_detail?.name ?: "",
                    detail.cta?.name ?: "",
                    detail.tenure ?: 0
                )
            )
            detail.isInvoke = true
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
        const val PAGE_MARGIN = 6

        @JvmStatic
        fun newInstance(
            pdpSimulationCallback: PdpSimulationCallback,
            bundle: Bundle
        ): PayLaterOffersFragment {
            val detailFragment = PayLaterOffersFragment()
            detailFragment.pdpSimulationCallback = pdpSimulationCallback
            detailFragment.arguments = bundle
            return detailFragment
        }
    }

    override fun onResume() {
        super.onResume()
        if (payLaterViewModel.refreshData) {
            payLaterDataGroup.visibility = View.GONE
            payLaterOffersShimmerGroup.visibility = View.VISIBLE
            sortFilter.visibility = View.INVISIBLE
        }
    }


}
