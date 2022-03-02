package com.tokopedia.pdpsimulation.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterArgsDescriptor
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.helper.BottomSheetNavigator
import com.tokopedia.pdpsimulation.paylater.helper.PayLaterBundleGenerator
import com.tokopedia.pdpsimulation.paylater.helper.PayLaterHelper
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactoryImpl
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterSimulationAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterSimulationTenureAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pdp_simulation.*
import kotlinx.android.synthetic.main.product_detail.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PdpSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    //private lateinit var payLaterArgsDescriptor: PayLaterArgsDescriptor

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
    }

    private val payLaterArgsDescriptor: PayLaterArgsDescriptor by lazy(LazyThreadSafetyMode.NONE) {
        PayLaterArgsDescriptor(
            arguments?.getString(PARAM_PRODUCT_ID) ?: "",
            (arguments?.getString(PARAM_PRODUCT_TENURE) ?: "0").toInt(),
            arguments?.getString(PARAM_PRODUCT_URL) ?: "",
        )
    }

    private val simulationAdapter: PayLaterSimulationAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PayLaterSimulationAdapter(getAdapterTypeFactory())
    }
    private val tenureAdapter: PayLaterSimulationTenureAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PayLaterSimulationTenureAdapter { payLaterList ->
            simulationAdapter.addAllElements(payLaterList)
            rvPayLaterOption.scrollToPosition(0)
        }
    }

    private fun getAdapterTypeFactory() = PayLaterAdapterFactoryImpl(
        interaction = PayLaterOptionInteraction(
            onCtaClicked = { handleAction(it) },
            installementDetails = { openInstallmentBottomSheet(it) },
            seeMoreOptions = { simulationAdapter.updateOptionList(it) },
            invokeAnalytics = { sendEvent(it) },
            retryLoading = { retryLoading() }
        )
    )

    private fun handleAction(detail: Detail) {

        val customUrl = detail.cta.android_url +
                "?productID=${payLaterArgsDescriptor.productId}" +
                "&tenure=${detail.tenure}" +
                "&productURL=${payLaterArgsDescriptor.productUrl}" +
                "&gatewayCode=${detail.gatewayDetail?.gatewayCode}" +
                "&gatewayID=${detail.gatewayDetail?.gateway_id}"

        PayLaterHelper.handleClickNavigation(context, detail, customUrl,
            openHowToUse = {
                bottomSheetNavigator.showBottomSheet(
                    PayLaterActionStepsBottomSheet::class.java,
                    it
                )
            },
            openGoPay = {
                bottomSheetNavigator.showBottomSheet(
                    PayLaterTokopediaGopayBottomsheet::class.java,
                    it
                )
            }
        )
    }

    private fun openInstallmentBottomSheet(detail: Detail) {
        bottomSheetNavigator.showBottomSheet(
            PayLaterInstallmentFeeInfo::class.java,
            PayLaterBundleGenerator.getInstallmentBundle(detail.installementDetails)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pdp_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        payLaterViewModel.defaultTenure = payLaterArgsDescriptor.defaultTenure
        payLaterViewModel.getProductDetail(payLaterArgsDescriptor.productId)
    }

    private fun initViews() {
        rvPayLaterSimulation.adapter = tenureAdapter
        rvPayLaterSimulation.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPayLaterOption.adapter = simulationAdapter
        rvPayLaterOption.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvPayLaterOption.setHasFixedSize(true)
        rvPayLaterSimulation.setHasFixedSize(true)
        simulationAdapter.showLoadingInAdapter()
    }

    private fun observeViewModel() {
        payLaterViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> productDetailSuccess(it.data)
                is Fail -> onProductDetailFailed(it.throwable)
            }
        }

        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> setSimulationView(it.data)
                is Fail -> simulationFailed(it.throwable)
            }
        }
    }

    private fun setSimulationView(data: ArrayList<SimulationUiModel>) {
        // hide shimmer
        productInfoShimmer.gone()
        showSimulationViews(data.isNotEmpty())
        setTenureAdapter(data)
        setSimulationAdapter(data)
    }

    private fun setTenureAdapter(data: ArrayList<SimulationUiModel>) {
        val defaultSelectedSimulation = payLaterViewModel.defaultSelectedSimulation
        tenureAdapter.setData(data)
        tenureAdapter.lastSelectedPosition = defaultSelectedSimulation
    }

    // set paylater options for selected simulation
    private fun setSimulationAdapter(data: ArrayList<SimulationUiModel>) {
        val defaultSelectedSimulation = payLaterViewModel.defaultSelectedSimulation
        if (defaultSelectedSimulation >= 0) {
            rvPayLaterSimulation.scrollToPosition(defaultSelectedSimulation)
            simulationAdapter.addAllElements(
                data.getOrNull(defaultSelectedSimulation)?.simulationList ?: arrayListOf()
            )
        }
        if (data.isNotEmpty())
            sendEvent(
                PayLaterBundleGenerator.getPayLaterImpressionEvent(
                    data,
                    defaultSelectedSimulation
                )
            )
    }

    private fun simulationFailed(throwable: Throwable) {
        // show product detail only after simulation has been done
        productDetail.visible()
        productInfoShimmer.gone()
        rvPayLaterSimulation.gone()
        simulationAdapter.showSimulationLoadingFailed(throwable)
    }

    private fun onProductDetailFailed(throwable: Throwable) {
        hideSimulationViews()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun retryLoading() {
        // hide data views
        productDetail.gone()
        // show shimmer views
        productInfoShimmer.visible()
        rvPayLaterSimulation.visible()
        rvPayLaterOption.visible()
        payLaterBorder.visible()
        simulationAdapter.showLoadingInAdapter()
        payLaterViewModel.getProductDetail(payLaterArgsDescriptor.productId)
    }

    private fun showSimulationViews(isDataValid: Boolean) {
        // show product detail only after simulation has been done
        if (payLaterViewModel.productDetailLiveData.value is Success)
            productDetail.visible()
        if (isDataValid)
            rvPayLaterSimulation.visible()
        else rvPayLaterSimulation.gone()
        rvPayLaterOption.visible()
        payLaterBorder.visible()
    }

    private fun hideSimulationViews() {
        productInfoShimmer.gone()
        rvPayLaterSimulation.gone()
        rvPayLaterOption.gone()
        payLaterBorder.gone()
    }

    private fun setGlobalErrors(errorType: Int) {
        payLaterSimulationGlobalError.setType(errorType)
        payLaterSimulationGlobalError.visible()
        payLaterSimulationGlobalError.setActionClickListener {
            payLaterSimulationGlobalError.gone()
            retryLoading()
        }
    }

    // THis method called on product detail api success
    private fun productDetailSuccess(data: GetProductV3) {
        payLaterViewModel.getPayLaterAvailableDetail(
            data.price
                ?: 0.0, payLaterArgsDescriptor.productId
        )
        setProductDetailView(data)
    }

    // This method called to set view for the product image,price and variant
    private fun setProductDetailView(data: GetProductV3) {
        data.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                productDetail.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        data.productName?.let { productDetail.productName.text = it }
        productDetail.productPrice.text =
            PayLaterHelper.convertPriceValueToIdrFormat(data.price ?: 0.0, false)

        showProductVariant(data)
    }

    /**
     * THis method set data for the product variant
     * @param data  is the product detail data
     */
    private fun showProductVariant(data: GetProductV3) {
        data.variant?.let { variant ->
            if (variant.products.isNotEmpty() && variant.selections.isNotEmpty()) {
                val combinationIdx = variant.products.find {
                    payLaterArgsDescriptor.productId == it.productID
                }?.combination?.getOrNull(0) ?: -1

                if (combinationIdx != -1)
                    productDetail.productVariant.text =
                        variant.selections[0].options[combinationIdx]?.value
                            ?: ""
            } else {
                productDetail.productVariant.gone()
            }
        }

    }

    private fun sendEvent(event: PayLaterAnalyticsBase) =
        activity?.let { (it as PdpSimulationCallback).sendAnalytics(event) }

    override fun getScreenName() = "PayLater & Cicilan"
    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): PdpSimulationFragment {
            val fragment = PdpSimulationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}