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
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterArgsDescriptor
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.helper.ActionHandler
import com.tokopedia.pdpsimulation.paylater.helper.BottomSheetNavigator
import com.tokopedia.pdpsimulation.paylater.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactoryImpl
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterSimulationAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterSimulationTenureAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_pdp_simulation.*
import kotlinx.android.synthetic.main.product_detail.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class PdpSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var isProductDetailShown: Boolean = false

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
            (arguments?.getString(PARAM_PRODUCT_TENURE) ?: "0").toInt()
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
            invokeAnalytics = { sendEvent(it) }
        )
    )

    private fun handleAction(detail: Detail) {
        ActionHandler.handleClickNavigation(context, detail, payLaterArgsDescriptor.productId,
            openHowToUse = { bottomSheetNavigator.showBottomSheet(PayLaterActionStepsBottomSheet::class.java, it) },
            openGoPay = { bottomSheetNavigator.showBottomSheet(PayLaterTokopediaGopayBottomsheet::class.java, it) }
        )
    }

    private fun openInstallmentBottomSheet(detail: Detail) {
        bottomSheetNavigator.showBottomSheet(
            PayLaterInstallmentFeeInfo::class.java,
            ActionHandler.getInstallmentBundle(detail)
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
        initArguments()
        initViews()
        payLaterViewModel.getProductDetail(payLaterArgsDescriptor.productId)
    }

    private fun initArguments() {
        payLaterViewModel.defaultTenure = payLaterArgsDescriptor.defaultTenure
    }

    private fun initViews() {
        rvPayLaterSimulation.visible()
        rvPayLaterOption.visible()
        rvPayLaterSimulation.adapter = tenureAdapter
        rvPayLaterSimulation.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPayLaterOption.adapter = simulationAdapter
        rvPayLaterOption.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        simulationAdapter.showLoadingInAdapter()
    }

    private fun observeViewModel() {
        payLaterViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> productDetailSuccess(it.data)
                is Fail -> showErrorUI(it.throwable)
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
        productInfoShimmer.gone()
        showSimulationViews()
        setTenureAdapter(data)
        setSimulationAdapter(data)
    }

    private fun setTenureAdapter(data: ArrayList<SimulationUiModel>) {
        val defaultSelectedSimulation = payLaterViewModel.defaultSelectedSimulation
        tenureAdapter.setData(data)
        tenureAdapter.lastSelectedPosition = defaultSelectedSimulation
    }

    private fun setSimulationAdapter(data: ArrayList<SimulationUiModel>) {
        val defaultSelectedSimulation = payLaterViewModel.defaultSelectedSimulation
        if (defaultSelectedSimulation >= 0) {
            rvPayLaterSimulation.scrollToPosition(defaultSelectedSimulation)
            simulationAdapter.addAllElements(
                data[defaultSelectedSimulation].simulationList ?: arrayListOf()
            )
        }
        val event = PayLaterAnalyticsBase().apply {
            tenureOption = defaultSelectedSimulation
            action = PdpSimulationAnalytics.IMPRESSION_PAYLATER
            //Todo partner name, userstatus
        }
        sendEvent(event)
    }

    private fun simulationFailed(throwable: Throwable) {
        // show product detail only after simulation has been done
        if (isProductDetailShown)
            productDetail.visible()
        showErrorUI(throwable)
    }

    private fun showErrorUI(throwable: Throwable) {
        hideSimulationViews()
        when (throwable) {
            is PdpSimulationException.PayLaterEmptyDataException,
            is PdpSimulationException.PayLaterNullDataException -> showEmptyInstallmentView()
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun showEmptyInstallmentView() {
        emptyStateInstallment.visible()
        emptyStateInstallment.setPrimaryCTAClickListener {
            emptyStateInstallment.gone()
            showSimulationViews()
            payLaterViewModel.getProductDetail(payLaterArgsDescriptor.productId)
        }
    }

    private fun showSimulationViews() {
        // show product detail only after simulation has been done
        if (isProductDetailShown)
            productDetail.visible()
        rvPayLaterSimulation.visible()
        rvPayLaterOption.visible()
        payLaterBorder.visible()
    }

    private fun hideSimulationViews() {
        productInfoShimmer.gone()
        rvPayLaterSimulation.gone()
        rvPayLaterOption.gone()
        if (!isProductDetailShown)
            payLaterBorder.gone()
    }

    private fun setGlobalErrors(errorType: Int) {
        payLaterSimulationGlobalError.setType(errorType)
        payLaterSimulationGlobalError.visible()
        payLaterSimulationGlobalError.setActionClickListener {
            payLaterSimulationGlobalError.gone()
            productInfoShimmer.visible()
            showSimulationViews()
            payLaterViewModel.getProductDetail(payLaterArgsDescriptor.productId)
        }
    }

    /**
     * THis method called on product detail api success
     */
    private fun productDetailSuccess(data: GetProductV3) {
        payLaterViewModel.getPayLaterAvailableDetail(data.price ?: 0.0, payLaterArgsDescriptor.productId)
        if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || data.price?.equals(0.0) == true)
            productDetail.gone()
        else
            setProductDetailView(data)
    }

    /**
     * This method called to set view for the product image,price and variant
     */
    private fun setProductDetailView(data: GetProductV3) {
        isProductDetailShown = true
        data.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                productDetail.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        data.productName?.let {
            productDetail.productName.text = it
        }

        productDetail.productPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.price ?: 0.0, false)

        showProductVariant(data)
    }

    /**
     * THis method set data for the product variant
     * @param data  is the product detail data
     */
    private fun showProductVariant(data: GetProductV3) {
        data.variant?.let { variant ->
            if (variant.products.isNotEmpty() && variant.selections.isNotEmpty()) {
                var combination = -1
                for (i in variant.products.indices) {
                    if (payLaterArgsDescriptor.productId == variant.products[i].productID) {
                        combination = variant.products[i].combination[0] ?: -1
                        break
                    }
                }
                if (combination != -1)
                    productDetail.productVariant.text =
                        variant.selections[0].options[combination]?.value ?: ""
            } else {
                productDetail.productVariant.gone()
            }
        }

    }

    private fun sendEvent(event: PayLaterAnalyticsBase) = activity?.let {
        (it as PdpSimulationCallback).sendAnalytics(event)
    }

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