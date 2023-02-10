package com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.InstallmentBottomSheetDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.helper.ActivationHelper
import com.tokopedia.pdpsimulation.activateCheckout.helper.ActivationHelper.setTextToDisplay
import com.tokopedia.pdpsimulation.activateCheckout.helper.ActivationHelper.showToaster
import com.tokopedia.pdpsimulation.activateCheckout.helper.BottomSheetType
import com.tokopedia.pdpsimulation.activateCheckout.helper.BundleData
import com.tokopedia.pdpsimulation.activateCheckout.helper.DataMapper
import com.tokopedia.pdpsimulation.activateCheckout.helper.OccBundleHelper
import com.tokopedia.pdpsimulation.activateCheckout.helper.OccBundleHelper.setBundleForInstalmentBottomSheet
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.ActivationTenureAdapter
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.ShowToasterException
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_CODE
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import com.tokopedia.pdpsimulation.paylater.helper.BottomSheetNavigator
import com.tokopedia.pdpsimulation.paylater.helper.PayLaterHelper.convertPriceValueToIdrFormat
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_activation_checkout.*
import kotlinx.android.synthetic.main.paylater_activation_gateway_detail.view.*
import kotlinx.android.synthetic.main.paylater_activation_product_detail.*
import kotlinx.android.synthetic.main.paylater_activation_product_detail.view.*
import kotlinx.android.synthetic.main.product_detail.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class ActivationCheckoutFragment : BaseDaggerFragment(), ActivationListner {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterActivationViewModel: PayLaterActivationViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterActivationViewModel::class.java)
    }

    private lateinit var activationTenureAdapter: ActivationTenureAdapter
    private var installmentModel: InstallmentDetails? = null
    private var listOfTenureDetail: List<TenureDetail> = ArrayList()
    private lateinit var paylaterGetOptimizedModel: PaylaterGetOptimizedModel
    private lateinit var listOfGateway: PaylaterGetOptimizedModel
    private var selectedTenurePosition = 0
    var quantity = 1
    var isDisabledPartner = false
    var isDisableTenure = false
    var itemProductStock = 1

    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
    }

    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_activation_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgument()
        observerProductData()
        observerTenureDetail()
        observeCartDetail()
        initView()
        startAllLoaders()
        payLaterActivationViewModel.getProductDetail(payLaterActivationViewModel.selectedProductId)
    }

    private fun initArgument() {
        val productId = arguments?.getString(PARAM_PRODUCT_ID, "").toString()
        val gateWayId = arguments?.getString(PARAM_GATEWAY_ID) ?: "0"
        val tenureSelected = arguments?.getString(PARAM_PRODUCT_TENURE) ?: "0"
        val gatewayCode = arguments?.getString(PARAM_GATEWAY_CODE) ?: ""
        payLaterActivationViewModel.selectedProductId = productId
        payLaterActivationViewModel.selectedGatewayId = gateWayId
        payLaterActivationViewModel.selectedTenureSelected = tenureSelected
        payLaterActivationViewModel.selectedGatewayCode = gatewayCode
    }

    private fun observeCartDetail() {
        payLaterActivationViewModel.addToCartLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success ->
                    if (payLaterActivationViewModel.occRedirectionUrl.isNotEmpty())
                        RouteManager.route(
                            context,
                            payLaterActivationViewModel.occRedirectionUrl
                        )
                is Fail ->
                    when (it.throwable) {
                        is ShowToasterException -> baseLayoutForActivation.showToaster(it.throwable.message)
                    }
            }
        }
    }

    private fun startAllLoaders() {
        productInfoActivationShimmer.visibility = View.VISIBLE
        globalErrorGroup.visibility = View.GONE
        detailHeader.visibility = View.GONE
        amountBottomDetailLoader.visibility = View.VISIBLE
        gatewayDetailShimmer.visibility = View.VISIBLE
        gatewayDetailLayout.visibility = View.GONE
    }

    fun updateSelectedTenure(gatewaySelected: String) {
        payLaterActivationViewModel.selectedGatewayId = gatewaySelected
    }

    private fun observerProductData() {
        payLaterActivationViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessProductData(it)
                }
                is Fail -> {
                    onFailProductData(it)
                }
            }
        }
    }

    private fun onFailProductData(it: Fail) {
        loaderhideOnCheckoutApi()
        removeBottomDetailForError()
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> fullPageGlobalError(
                GlobalError.NO_CONNECTION
            )
            is IllegalStateException -> fullPageEmptyError()
            else -> fullPageGlobalError(GlobalError.SERVER_ERROR)
        }
    }

    private fun onSuccessProductData(it: Success<GetProductV3>) {
        removeAllError()
        productInfoActivationShimmer.visibility = View.GONE
        detailHeader.visibility = View.VISIBLE
        setProductData(it.data)
        payLaterActivationViewModel.getOptimizedCheckoutDetail(
            payLaterActivationViewModel.selectedProductId,
            payLaterActivationViewModel.price * quantity,
            payLaterActivationViewModel.selectedGatewayCode,
            payLaterActivationViewModel.shopId.orEmpty(),
        )
    }


    private fun removeAllError() {
        globalErrorGroup.visibility = View.GONE
        nestedScrollView.visibility = View.VISIBLE
    }

    private fun fullPageGlobalError(errorType: Int) {
        fullPageGLobalError.setType(errorType)
        fullPageGLobalError.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE
    }

    private fun fullPageEmptyError() {
        fullPageGLobalError.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE
    }

    private fun observerTenureDetail() {
        payLaterActivationViewModel.payLaterActivationDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessTenureData(it)
                }
                is Fail -> {
                    onFailTenureData(it)
                }
            }
        }
    }

    private fun onFailTenureData(it: Fail) {
        loaderhideOnCheckoutApi()
        removeBottomDetailForError()
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> showGlobalErrorInTenureDetail(
                GlobalError.NO_CONNECTION
            )
            is IllegalStateException -> showEmptyErrorInTenureDetail()
            else -> showGlobalErrorInTenureDetail(GlobalError.SERVER_ERROR)
        }
    }

    private fun onSuccessTenureData(it: Success<PaylaterGetOptimizedModel>) {
        showBottomDetail()
        loaderhideOnCheckoutApi()
        paylaterGetOptimizedModel = it.data
        removeErrorInTenure()
        setTenureDetailData()
    }


    private fun sendOccImpressionEvent() {
        try {
            payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->
                if (!isDisabledPartner) {
                    sendAnalyticEvent(
                        PdpSimulationEvent.OccImpressionEvent(
                            payLaterActivationViewModel.selectedProductId,
                            checkoutData.userState ?: "",
                            checkoutData.gateway_name.orEmpty(),
                            checkoutData.tenureDetail[selectedTenurePosition].monthly_installment.orEmpty(),
                            checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                            quantity.toString(),
                            checkoutData.userAmount ?: "",
                            payLaterActivationViewModel.variantName
                        )
                    )
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun sendAnalyticEvent(event: PdpSimulationEvent) {
        activity?.let {
            (it as PdpSimulationCallback).sendOtherAnalytics(event)
        }
    }

    private fun setTenureDetailData() {
        payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let {
            checkDisablePartnerLogic(it.disable)
            sendOccImpressionEvent()
            listOfGateway = paylaterGetOptimizedModel
            setSelectedTenure()
            setTenureOptionsData(paylaterGetOptimizedModel)
            setTickerVisibility(it)
        } ?: run {
            loaderhideOnCheckoutApi()
            showEmptyErrorInTenureDetail()
            removeBottomDetailForError()
        }
    }

    private fun setTickerVisibility(it: CheckoutData) {
        when {
            isDisabledPartner -> {
                gatewayDetailLayout.errorTicker.visibility = View.VISIBLE
                gatewayDetailLayout.errorTicker.setTextDescription(it.reason_long.orEmpty())
                proceedToCheckout.isEnabled = false
                priceBreakdown.visibility = View.GONE

            }
            isDisableTenure -> {
                proceedToCheckout.isEnabled = false
                priceBreakdown.visibility = View.GONE
            }
            else -> {
                gatewayDetailLayout.errorTicker.visibility = View.GONE
                proceedToCheckout.isEnabled = true
                priceBreakdown.visibility = View.VISIBLE
                priceBreakdown.isEnabled = true
            }
        }
    }

    private fun showBottomDetail() {
        proceedToCheckout.isEnabled = true
        priceBreakdown.isEnabled = true
        paymentDuration.visibility = View.VISIBLE
    }

    private fun removeErrorInTenure() {
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.VISIBLE
        gatewayDetailLayout.changePayLaterPartner.visibility = View.VISIBLE
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.GONE
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.GONE
    }

    private fun removeBottomDetailForError() {
        proceedToCheckout.isEnabled = false
        amountToPay.text = "Rp-"
        paymentDuration.visibility = View.GONE
        priceBreakdown.isEnabled = false
    }

    private fun showEmptyErrorInTenureDetail() {
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.VISIBLE
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.GONE
        gatewayDetailLayout.changePayLaterPartner.visibility = View.GONE
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.GONE
        gatewayDetailLayout.errorTicker.visibility = View.GONE
    }

    private fun showGlobalErrorInTenureDetail(errorType: Int) {
        gatewayDetailLayout.tenureDetailGlobalError.setType(errorType)
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.VISIBLE
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.GONE
        gatewayDetailLayout.changePayLaterPartner.visibility = View.GONE
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.GONE
        gatewayDetailLayout.errorTicker.visibility = View.GONE
    }

    private fun loaderhideOnCheckoutApi() {
        amountBottomDetailLoader.visibility = View.GONE
        gatewayDetailShimmer.visibility = View.GONE
        gatewayDetailLayout.visibility = View.VISIBLE
    }

    /**
     * Set the global value if the Partner value is disable
     */
    private fun checkDisablePartnerLogic(disable: Boolean) {
        isDisabledPartner = disable
        this.checkIsDisablePartner()
    }


    private fun setSelectedTenure() {
        payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->
            if (checkoutData.tenureDetail.isNotEmpty()) {
                checkoutData.tenureDetail.map {
                    it.isSelectedTenure = false
                }
                for (i in 0 until checkoutData.tenureDetail.size) {
                    if (payLaterActivationViewModel.selectedTenureSelected.toIntOrZero() == checkoutData.tenureDetail[i].tenure) {
                        selectedTenurePosition = i
                        break
                    }
                }
                if (checkoutData.tenureDetail[selectedTenurePosition].tenureDisable) {
                    setSelectToMaxTenureData(checkoutData)
                }

                if (selectedTenurePosition >= checkoutData.tenureDetail.size && checkoutData.tenureDetail.isNotEmpty() && !checkoutData.tenureDetail[0].tenureDisable) {
                    checkoutData.tenureDetail[0].isSelectedTenure = true
                    selectedTenurePosition = 0
                } else
                    checkoutData.tenureDetail[selectedTenurePosition].isSelectedTenure = true
                setBottomDetailData(checkoutData)

            }
        }
    }

    private fun setBottomDetailData(checkoutData: CheckoutData) {
        if (!checkoutData.tenureDetail[selectedTenurePosition].tenureDisable) {
            isDisableTenure = false
            setTenureData(DataMapper.mapToInstallationDetail(checkoutData.tenureDetail[selectedTenurePosition]))
        } else
            isDisableTenure = true
    }

    private fun setSelectToMaxTenureData(checkoutData: CheckoutData) {
        var maxEnabledTenure = -1
        for (i in 0 until checkoutData.tenureDetail.size) {
            if (!checkoutData.tenureDetail[i].tenureDisable) {
                if (maxEnabledTenure < checkoutData.tenureDetail[i].tenure && checkoutData.tenureDetail[selectedTenurePosition].tenureDisable) {
                    maxEnabledTenure = checkoutData.tenureDetail[i].tenure
                    selectedTenurePosition = i
                }
            }
        }
    }

    private fun setTenureData(tenureSelectedModel: TenureSelectedModel?) {
        tenureSelectedModel?.also { tenureSelectedDetail ->
            tenureSelectedDetail.installmentDetails?.let { installmentDetails ->
                this.installmentModel = installmentDetails
            }
            tenureSelectedDetail.tenure?.let { tenure ->
                paymentDuration.text = "x$tenure"
            }
            amountToPay.text = tenureSelectedDetail.priceText.orEmpty()
        }
    }

    private fun setTenureOptionsData(data: PaylaterGetOptimizedModel) {
        payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { it ->
            setGatewayProductImage(it)

            gatewayDetailLayout.getwayBrandName.setTextToDisplay(it.gateway_name)
            gatewayDetailLayout.subheaderGateway.setTextToDisplay(it.subtitle)
            gatewayDetailLayout.subheaderGatewayDetail.setTextToDisplay(it.subtitle2)

            if (it.tenureDetail.isNotEmpty()) {
                gatewayDetailLayout.additionalDetail.text = data.footer.orEmpty()
            } else {
                gatewayDetailLayout.additionalDetail.text = ""
                removeBottomDetailForError()
            }
            if (isDisabledPartner || isDisableTenure)
                removeBottomDetailForError()
            if (payLaterActivationViewModel.gatewayToChipMap.size <= 1)
                gatewayDetailLayout.changePayLaterPartner.visibility = View.GONE
            else
                gatewayDetailLayout.changePayLaterPartner.visibility = View.VISIBLE
            setTenureDetail(it.tenureDetail)
        }
    }

    private fun setTenureDetail(tenureDetail: List<TenureDetail>) {
        this.listOfTenureDetail = tenureDetail
        activationTenureAdapter.updateList(tenureDetail)
    }

    private fun setGatewayProductImage(checkoutData: CheckoutData) {
        if (context.isDarkMode())
            checkoutData.dark_img_url?.let { gatewayDetailLayout.gatewayImg.setImageUrl(it) }
        else
            checkoutData.light_img_url?.let { gatewayDetailLayout.gatewayImg.setImageUrl(it) }

    }

    private fun setProductData(productData: GetProductV3) {

        productData.stock?.let { productStock ->
            productStockLogic(productStock)
        }
        productData.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                detailHeader.productDetailWidget.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        detailHeader.productDetailWidget.productName.text = productData.productName.orEmpty()
        detailHeader.productDetailWidget.productPrice.text = productPriceValue(productData)
        showVariantProductHeader()
    }

    private fun productPriceValue(productData: GetProductV3) =
        if ((productData.campaingnDetail?.discountedPrice ?: 0.0) != 0.0)
            convertPriceValueToIdrFormat(productData.campaingnDetail?.discountedPrice ?: 0.0, false)
        else
            convertPriceValueToIdrFormat(productData.price ?: 0.0, false)

    private fun productStockLogic(productStock: Int) {
        detailHeader.quantityEditor.maxValue = productStock
        detailHeader.quantityEditor.addButton.isEnabled = productStock > MINIMUM_THRESHOLD_QUANTITY
        detailHeader.quantityEditor.subtractButton.isEnabled =
            detailHeader.quantityEditor.editText.text.toString()
                .toIntOrZero() > MINIMUM_THRESHOLD_QUANTITY
        val currentDetailQuantityValue = detailHeader.quantityEditor.editText.text.toString()
        try {
            if (currentDetailQuantityValue.replace("[^0-9]".toRegex(), "").toIntOrZero() > productStock) {
                detailHeader.quantityEditor.editText.setText(productStock.toString())
                quantity = productStock
            }
        } catch (e: java.lang.Exception) {
            detailHeader.quantityEditor.editText.setText("0")
            quantity = 0
        }
    }

    private fun showVariantProductHeader() {
        if (payLaterActivationViewModel.variantName.isNullOrEmpty()) {
            detailHeader.productDetailWidget.productVariant.gone()
            detailHeader.showVariantBottomSheet.visibility = View.GONE
        } else {
            detailHeader.showVariantBottomSheet.visibility = View.VISIBLE
            detailHeader.productDetailWidget.productVariant.text =
                payLaterActivationViewModel.variantName
        }
    }

    private fun initView() {
        addListeners()
        gatewayDetailLayout.recyclerTenureDetail.isNestedScrollingEnabled = false
        gatewayDetailLayout.recyclerTenureDetail.layoutManager = LinearLayoutManager(context)
        gatewayDetailLayout.recyclerTenureDetail.adapter = activationTenureAdapter
        detailHeader.limiterMessage.visibility = View.GONE
    }

    private fun addListeners() {
        gatewayDetailLayout.tenureDetailEmptyStateError.emptyStateCTAID.setOnClickListener {
            gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.GONE
            getCheckoutDetail()
        }
        gatewayDetailLayout.tenureDetailGlobalError.errorAction.setOnClickListener {
            gatewayDetailLayout.tenureDetailGlobalError.visibility = View.GONE
            getCheckoutDetail()
        }
        fullPageEmptyState.emptyStateCTAID.setOnClickListener {
            fullPageEmptyState.visibility = View.GONE
            startAllLoaders()
            payLaterActivationViewModel.getProductDetail(payLaterActivationViewModel.selectedProductId)
        }
        fullPageGLobalError.errorAction.setOnClickListener {
            fullPageGLobalError.visibility = View.GONE
            startAllLoaders()
            payLaterActivationViewModel.getProductDetail(payLaterActivationViewModel.selectedProductId)
        }
        gatewayDetailLayout.changePayLaterPartner.setOnClickListener {
            changePartnerLogic()
        }
        quantityTextWatcher()
        detailHeader.quantityEditor.setValueChangedListener { newValue, _, _ ->
            quantity = newValue
            getCheckoutDetail()
        }
        activationTenureAdapter = ActivationTenureAdapter(listOf(), this)
        detailHeader.showVariantBottomSheet.setOnClickListener {
            sendVarintClickEvent()
            openVariantBottomSheet()
        }
        priceBreakdown.setOnClickListener {
            openPriceBreakDownBottomSheet()
        }
        proceedToCheckout.setOnClickListener {
            sendCTAClickEvent()
            payLaterActivationViewModel.addProductToCart(
                payLaterActivationViewModel.selectedProductId,
                quantity
            )
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun sendCTAClickEvent() {
        payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->
            sendAnalyticEvent(
                PdpSimulationEvent.ClickCTACheckoutPage(
                    payLaterActivationViewModel.selectedProductId,
                    checkoutData.userState.orEmpty(),
                    checkoutData.gateway_name.orEmpty(),
                    checkoutData.tenureDetail.getOrNull(selectedTenurePosition)?.monthly_installment.orEmpty(),
                    checkoutData.tenureDetail.getOrNull(selectedTenurePosition)?.tenure.toString(),
                    quantity.toString(),
                    checkoutData.userAmount ?: "",
                    payLaterActivationViewModel.variantName,
                    checkoutData.tenureDetail.getOrNull(selectedTenurePosition)?.promoName.orEmpty()
                )
            )
        }
    }

    private fun changePartnerLogic() {
        if (this::listOfGateway.isInitialized) {
            sendChangePartnerClickEvent()
            payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.tenureDetail?.let {
                openBottomSheet(it)
            }

        }
    }

    private fun openPriceBreakDownBottomSheet() {
        installmentModel?.let {
            val bundle = setBundleForInstalmentBottomSheet(
                InstallmentBottomSheetDetail(
                    installmentDetail = it,
                    gatwayToChipMap = payLaterActivationViewModel.gatewayToChipMap,
                    selectedProductPrice = payLaterActivationViewModel.price.toString(),
                    gatewayIdSelected = payLaterActivationViewModel.selectedGatewayId,
                    selectedTenure = selectedTenurePosition
                )
            )
            bottomSheetNavigator.showBottomSheet(PayLaterInstallmentFeeInfo::class.java, bundle)
        }
    }

    private fun openVariantBottomSheet() {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = payLaterActivationViewModel.selectedProductId,
                pageSource = VariantPageSource.BNPL_PAGESOURCE,
                isTokoNow = false,
                shopId = payLaterActivationViewModel.shopId.orEmpty(),
                saveAfterClose = false
            ) { data, code ->
                startActivityForResult(data, code)
            }
        }
    }

    private fun openBottomSheet(it: List<TenureDetail>) {
        ActivationHelper.navigateToBottomSheet(
            BottomSheetType.GateWayBottomSheet(
                OccBundleHelper.setBundleForBottomSheetPartner(
                    BundleData(
                    it,
                    selectedTenurePosition,
                    listOfGateway,
                    payLaterActivationViewModel.selectedGatewayId,
                    payLaterActivationViewModel.variantName,
                    payLaterActivationViewModel.selectedProductId,
                    payLaterActivationViewModel.selectedTenureSelected,
                    quantity
                    )
                )
            ),
            childFragmentManager
        ) {
            setTenureDetailData()
        }
    }

    private fun sendChangePartnerClickEvent() {
        try {
            payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->
                sendAnalyticEvent(
                    PdpSimulationEvent.OccChangePartnerClicked(
                        payLaterActivationViewModel.selectedProductId,
                        checkoutData.userState ?: "",
                        checkoutData.gateway_name.orEmpty(),
                        checkoutData.tenureDetail[selectedTenurePosition].monthly_installment.orEmpty(),
                        checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                        quantity.toString(),
                        checkoutData.userAmount ?: "",
                        payLaterActivationViewModel.variantName,
                    )
                )
            }
        } catch (e: Exception) {

        }
    }

    private fun sendVarintClickEvent() {
        try {
            payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->

                sendAnalyticEvent(
                    PdpSimulationEvent.OccChangeVariantClicked(
                        payLaterActivationViewModel.selectedProductId,
                        checkoutData.userState ?: "",
                        checkoutData.gateway_name.orEmpty(),
                        checkoutData.tenureDetail[selectedTenurePosition].monthly_installment.orEmpty(),
                        checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                        quantity.toString(),
                        checkoutData.userAmount ?: "",
                        payLaterActivationViewModel.variantName,
                    )
                )


            }
        } catch (e: Exception) {
        }
    }

    @SuppressLint("SetTextI18n")
    private fun quantityTextWatcher() {
        detailHeader.quantityEditor.editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    detailHeader.quantityEditor.editText.clearFocus()
                    closeKeyboard()
                    return true
                }
                return false
            }
        })
        detailHeader.quantityEditor.editText.afterTextChanged { s ->
            if (s.isNotBlank()) {

                val mQuantity = try {
                    s.replace("[^0-9]".toRegex(), "").toIntOrZero()
                } catch (e: Exception) {
                    1
                }

                if (mQuantity >= detailHeader.quantityEditor.maxValue || itemProductStock == 0) {
                    detailHeader.limiterMessage.visibility = View.VISIBLE
                    detailHeader.limiterMessage.text =
                        "${getString(R.string.paylater_occ_quantity_overflow)} ${detailHeader.quantityEditor.maxValue}"
                } else if (mQuantity < 1) {
                    detailHeader.limiterMessage.visibility = View.VISIBLE
                    detailHeader.limiterMessage.text =
                        getString(R.string.paylater_occ_min_quantity)
                } else
                    detailHeader.limiterMessage.visibility = View.GONE
            }
        }
    }

    private fun closeKeyboard() {
        context?.let {
            val imm: InputMethodManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(quantityEditor.editText.windowToken, 0)
        }
    }

    private fun getCheckoutDetail() {
        amountBottomDetailLoader.visibility = View.VISIBLE
        gatewayDetailShimmer.visibility = View.VISIBLE
        gatewayDetailLayout.visibility = View.GONE
        payLaterActivationViewModel.getOptimizedCheckoutDetail(
            payLaterActivationViewModel.selectedProductId,
            payLaterActivationViewModel.price * quantity,
            payLaterActivationViewModel.selectedGatewayCode,
            payLaterActivationViewModel.shopId.orEmpty(),
        )
    }

    override fun getScreenName(): String {
        return "Activation PayLater"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                if (this.selectedProductId.isNotBlank()) {
                    if (payLaterActivationViewModel.selectedProductId != this.selectedProductId) {
                        payLaterActivationViewModel.selectedProductId = this.selectedProductId
                        startAllLoaders()
                        payLaterActivationViewModel.getProductDetail(this.selectedProductId)
                    }

                }
            }
        }
    }

    override fun checkIsDisablePartner(): Boolean {
        return isDisabledPartner
    }

    @SuppressLint("PII Data Exposure")
    override fun selectedTenure(
        tenureSelectedModel: TenureSelectedModel,
        newPositionToSelect: Int,
        promoName: String,
    ) {
        tenureSelectedModel.installmentDetails?.let {
            installmentModel = it
        }
        amountToPay.text = tenureSelectedModel.priceText.orEmpty()
        paymentDuration.text = "x${tenureSelectedModel.tenure.orEmpty()}"
        sendTenureSelectedAnalytics(newPositionToSelect, promoName)
        updateRecyclerViewData(newPositionToSelect, tenureSelectedModel)
    }

    @SuppressLint("PII Data Exposure")
    private fun sendTenureSelectedAnalytics(newPositionToSelect: Int, promoName: String) {
        payLaterActivationViewModel.gatewayToChipMap[payLaterActivationViewModel.selectedGatewayId]?.let { checkoutData ->
            sendAnalyticEvent(
                PdpSimulationEvent.ClickTenureEvent(
                    payLaterActivationViewModel.selectedProductId,
                    checkoutData.userState ?: "",
                    payLaterActivationViewModel.price.toString(),
                    checkoutData.tenureDetail[newPositionToSelect].tenure.toString(),
                    checkoutData.gateway_name.orEmpty(),
                    promoName
                )
            )
        }
    }

    private fun updateRecyclerViewData(
        newPositionToSelect: Int,
        tenureSelectedModel: TenureSelectedModel
    ) {
        listOfTenureDetail[selectedTenurePosition].isSelectedTenure = false
        listOfTenureDetail[newPositionToSelect].isSelectedTenure = true
        activationTenureAdapter.updatePartialList(listOfTenureDetail)
        activationTenureAdapter.notifyItemChanged(selectedTenurePosition)
        activationTenureAdapter.notifyItemChanged(newPositionToSelect)
        selectedTenurePosition = newPositionToSelect
        payLaterActivationViewModel.selectedTenureSelected = tenureSelectedModel.tenure.toString()
    }

    companion object {
        const val MINIMUM_THRESHOLD_QUANTITY = 1

        @JvmStatic
        fun newInstance(bundle: Bundle): ActivationCheckoutFragment {
            val fragment = ActivationCheckoutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
