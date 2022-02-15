package com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.helper.DataMapper
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.ActivationTenureAdapter
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet.Companion.CURRENT_QUANTITY
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet.Companion.CURRENT_VARINT
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet.Companion.GATEWAY_LIST
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet.Companion.SELECTED_GATEWAY
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_activation_checkout.*
import kotlinx.android.synthetic.main.paylater_activation_gateway_detail.view.*
import kotlinx.android.synthetic.main.paylater_activation_product_detail.view.*
import kotlinx.android.synthetic.main.product_detail.view.*
import javax.inject.Inject


class ActivationCheckoutFragment : BaseDaggerFragment(), ActivationListner {


    private lateinit var parentView: View

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterActivationViewModel: PayLaterActivationViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterActivationViewModel::class.java)
    }

    private var productId: String = ""
    private var shopId: String = ""

    private val gatewayCode: String by lazy {
        arguments?.getString(PARAM_GATEWAY_CODE) ?: ""
    }

    private var gateWayId = ""

    private val tenureSelected: String by lazy {
        arguments?.getString(PARAM_PRODUCT_TENURE) ?: "0"
    }

    private lateinit var activationTenureAdapter: ActivationTenureAdapter
    private lateinit var installmentModel: InstallmentDetails
    private var listOfTenureDetail: List<TenureDetail> = ArrayList()
    private lateinit var paylaterGetOptimizedModel: PaylaterGetOptimizedModel
    private lateinit var listOfGateway: PaylaterGetOptimizedModel
    private var selectedTenurePosition = 0
    var quantity = 1
    var isDisabled = false
    private var variantName = ""
    private var gatewayToChipMap: MutableMap<Int, CheckoutData> = HashMap()
    private var variantBottomSheetWatcher = false


    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
    }


    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        parentView = inflater.inflate(R.layout.fragment_activation_checkout, container, false)
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = arguments?.getString(PARAM_PRODUCT_ID, "").toString()
        gateWayId = arguments?.getString(PARAM_GATEWAY_ID) ?: "0"
        observerProductData()
        observerOtherDetail()
        observeCartDetail()
        initView()

        startAllLoaders()
        payLaterActivationViewModel.getProductDetail(productId)

    }

    private fun observeCartDetail() {
        payLaterActivationViewModel.addToCartLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isStatusError()) {
                        showToaster(it.data.getAtcErrorMessage())
                    } else {
                        sendOneClickAnalytics()
                        RouteManager.route(
                            context,
                            ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT
                        )
                    }
                }
                is Fail -> {

                }
            }
        }
    }

    private fun sendOneClickAnalytics() {
        try {
            gatewayToChipMap[gateWayId.toInt()]?.let { checkoutData ->
                checkoutData.userAmount?.let { limit ->
                    checkoutData.userState?.let { userStatus ->
                        sendAnalyticEvent(
                            PdpSimulationEvent.OccProceedToCheckout(
                                checkoutData.gateway_name, quantity.toString(),
                                checkoutData.tenureDetail[selectedTenurePosition].monthly_installment,
                                checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                                limit, variantName,userStatus
                            )
                        )
                    }
                }

            }

        } catch (e: Exception) {

        }

    }

    private fun showToaster(atcErrorMessage: String?) {
        atcErrorMessage?.let {
            Toaster.build(
                parentView,
                it,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
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

    fun updateSelectedTenure(gatewaySelected: Int) {
        this.gateWayId = gatewaySelected.toString()
    }

    private fun observerProductData() {
        payLaterActivationViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    try {
                        removeAllError()
                        productInfoActivationShimmer.visibility = View.GONE
                        detailHeader.visibility = View.VISIBLE
                        setProductData(it.data)
                        it.data.shopDetail?.shopId?.let {
                            shopId = it
                        }
                        it.data.stock?.let { productStock ->
                            detailHeader.quantityEditor.maxValue = productStock
                        }

                        payLaterActivationViewModel.getOptimizedCheckoutDetail(
                            productId,
                            payLaterActivationViewModel.price * quantity, gatewayCode
                        )
                        if (variantBottomSheetWatcher)
                            sendClickVariantAnalytic()
                    } catch (e: Exception) {
                        loaderhideOnCheckoutApi()
                        fullPageEmptyError()
                        removeBottomDetailForError()
                    }

                }
                is Fail -> {
                    loaderhideOnCheckoutApi()
                    fullPageGlobalError()
                    removeBottomDetailForError()
                }
            }


        }
    }

    private fun sendClickVariantAnalytic() {

    }


    private fun removeAllError() {
        globalErrorGroup.visibility = View.GONE
        nestedScrollView.visibility = View.VISIBLE

    }

    private fun fullPageGlobalError() {
        fullPageGLobalError.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE
    }

    private fun fullPageEmptyError() {
        fullPageGLobalError.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE
    }

    private fun observerOtherDetail() {
        payLaterActivationViewModel.payLaterActivationDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.checkoutData.isNotEmpty()) {
                        setGatewayToChipMap(it.data)
                        showBottomDetail()
                        loaderhideOnCheckoutApi()
                        paylaterGetOptimizedModel = it.data
                        removeErrorInTenure()
                        setTenureDetailData()
                        sendOccImpressionEvent()
                    } else {
                        loaderhideOnCheckoutApi()
                        showEmptyErrorInTenureDetail()
                        removeBottomDetailForError()
                    }
                }
                is Fail -> {
                    loaderhideOnCheckoutApi()
                    showGlobalErrorInTenureDetail()
                    removeBottomDetailForError()


                }
            }
        }
    }

    private fun sendOccImpressionEvent() {
        try {
            gatewayToChipMap[gateWayId.toInt()]?.let { checkoutData ->
                checkoutData.userAmount?.let { limit ->
                    checkoutData.userState?.let {userStatus ->
                        if(!isDisabled) {
                            sendAnalyticEvent(
                                PdpSimulationEvent.OccImpressionEvent(
                                    checkoutData.gateway_name, quantity.toString(),
                                    checkoutData.tenureDetail[selectedTenurePosition].monthly_installment,
                                    checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                                    limit, variantName, userStatus
                                )
                            )
                        }
                    }
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

    private fun setGatewayToChipMap(data: PaylaterGetOptimizedModel) {
        for (i in 0 until data.checkoutData.size) {
            gatewayToChipMap[data.checkoutData[i].gateway_id] = data.checkoutData[i]
        }
    }

    private fun setTenureDetailData() {
        gatewayToChipMap[gateWayId.toInt()]?.let {
            checkDisableLogic(it.disable)
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
        if (isDisabled) {
            gatewayDetailLayout.errorTicker.visibility = View.VISIBLE
            gatewayDetailLayout.errorTicker.setTextDescription(it.reason_long)
            proceedToCheckout.isEnabled = false
            priceBreakdown.isEnabled = false
        } else {
            gatewayDetailLayout.errorTicker.visibility = View.GONE
            proceedToCheckout.isEnabled = true
            priceBreakdown.isEnabled = true
        }
    }

    private fun showBottomDetail() {
        proceedToCheckout.isEnabled = true
        priceBreakdown.isEnabled = true
        paymentDuration.visibility = View.VISIBLE
    }

    private fun removeErrorInTenure() {
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.VISIBLE
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.GONE
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.GONE
    }

    private fun removeBottomDetailForError() {
        proceedToCheckout.isEnabled = false
        amountToPay.text = "Rp"
        paymentDuration.visibility = View.GONE
        priceBreakdown.isEnabled = false
    }

    private fun showEmptyErrorInTenureDetail() {
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.VISIBLE
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.GONE
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.GONE
        gatewayDetailLayout.errorTicker.visibility = View.GONE


    }

    private fun showGlobalErrorInTenureDetail() {
        gatewayDetailLayout.tenureDetailGlobalError.visibility = View.VISIBLE
        gatewayDetailLayout.tenureErrorHandlerGroup.visibility = View.GONE
        gatewayDetailLayout.tenureDetailEmptyStateError.visibility = View.GONE
        gatewayDetailLayout.errorTicker.visibility = View.GONE
    }

    private fun loaderhideOnCheckoutApi() {
        amountBottomDetailLoader.visibility = View.GONE
        gatewayDetailShimmer.visibility = View.GONE
        gatewayDetailLayout.visibility = View.VISIBLE
    }

    private fun checkDisableLogic(disable: Boolean) {
        isDisabled = disable
        this.isDisable()
    }


    private fun setSelectedTenure() {


        gatewayToChipMap[gateWayId.toInt()]?.let { checkoutData ->
            if (checkoutData.tenureDetail.isNotEmpty()) {

                checkoutData.tenureDetail.map {
                    it.isSelectedTenure = false
                }

                for (i in 0 until checkoutData.tenureDetail.size) {
                    checkoutData.tenureDetail[i].isSelectedTenure = false
                    if (tenureSelected.toInt() == checkoutData.tenureDetail[i].tenure) {
                        selectedTenurePosition = i
                        break
                    }
                }

                if(selectedTenurePosition >= checkoutData.tenureDetail.size && checkoutData.tenureDetail.isNotEmpty())
                {
                    checkoutData.tenureDetail[0].isSelectedTenure = true
                    selectedTenurePosition = 0
                }
                else
                    checkoutData.tenureDetail[selectedTenurePosition].isSelectedTenure = true


                DataMapper.mapToInstallationDetail(checkoutData.tenureDetail[selectedTenurePosition]).installmentDetails?.let {
                    installmentModel = it
                }
                DataMapper.mapToInstallationDetail(checkoutData.tenureDetail[selectedTenurePosition]).tenure?.let {
                    paymentDuration.text = it
                }
                DataMapper.mapToInstallationDetail(checkoutData.tenureDetail[selectedTenurePosition]).priceText?.let {
                    amountToPay.text = it
                }
            }
        }


    }

    private fun setTenureOptionsData(data: PaylaterGetOptimizedModel) {
        gatewayToChipMap[gateWayId.toInt()]?.let { it ->
            setGatewayProductImage(it)
            if (it.gateway_name.isNotBlank())
                gatewayDetailLayout.getwayBrandName.text =
                    it.gateway_name
            else
                gatewayDetailLayout.getwayBrandName.visibility = View.GONE
            if (it.subtitle.isNotBlank())
                gatewayDetailLayout.subheaderGateway.text =
                    it.subtitle
            else
                gatewayDetailLayout.subheaderGateway.visibility = View.GONE
            if (it.subtitle2.isNotBlank())
                gatewayDetailLayout.subheaderGatewayDetail.text =
                    it.subtitle2
            else
                gatewayDetailLayout.subheaderGatewayDetail.visibility = View.GONE
            gatewayDetailLayout.additionalDetail.text = data.footer
            setTenureDetail(it.tenureDetail)
        }


    }

    private fun setTenureDetail(tenureDetail: List<TenureDetail>) {
        this.listOfTenureDetail = tenureDetail
        activationTenureAdapter.updateList(tenureDetail)
    }

    private fun setGatewayProductImage(checkoutData: CheckoutData) {
        if (context.isDarkMode())
            gatewayDetailLayout.gatewayImg.setImageUrl(checkoutData.dark_img_url)
        else
            gatewayDetailLayout.gatewayImg.setImageUrl(checkoutData.light_img_url)

    }

    private fun setProductData(productData: GetProductV3) {
        productData.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                detailHeader.productDetailWidget.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        productData.productName.let {
            detailHeader.productDetailWidget.productName.text = it
        }

        detailHeader.productDetailWidget.productPrice.text =
            convertPriceValueToIdrFormat(productData.price ?: 0.0, false)

        showVariantProductHeader(productData)
    }

    private fun showVariantProductHeader(data: GetProductV3) {
        data.variant?.let { variant ->
            if (variant.products.isNotEmpty() && variant.selections.isNotEmpty()) {
                var combination = -1
                for (i in variant.products.indices) {
                    if (productId == variant.products[i].productID) {
                        combination = variant.products[i].combination[0] ?: -1
                        break
                    }
                }
                if (combination != -1) {
                    detailHeader.showVariantBottomSheet.visibility = View.VISIBLE
                    detailHeader.productDetailWidget.productVariant.text =
                        variant.selections[0].options[combination]?.value ?: ""
                }
                variantName = detailHeader.productDetailWidget.productVariant.text.toString()
            } else {
                detailHeader.productDetailWidget.productVariant.gone()
                detailHeader.showVariantBottomSheet.visibility = View.GONE
            }
        }
    }


    private fun initView() {
        addListeners()
        gatewayDetailLayout.recyclerTenureDetail.isNestedScrollingEnabled = false
        gatewayDetailLayout.recyclerTenureDetail.layoutManager = LinearLayoutManager(context)
        gatewayDetailLayout.recyclerTenureDetail.adapter = activationTenureAdapter
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
            payLaterActivationViewModel.getProductDetail(productId)
        }


        fullPageGLobalError.errorAction.setOnClickListener {
            fullPageGLobalError.visibility = View.GONE
            startAllLoaders()
            payLaterActivationViewModel.getProductDetail(productId)
        }

        gatewayDetailLayout.changePayLaterPartner.setOnClickListener {
            if (this::listOfGateway.isInitialized) {
                val bundle = Bundle().apply {
                    putParcelable(
                        GATEWAY_LIST,
                        listOfGateway
                    )
                    putString(
                        SELECTED_GATEWAY,
                        gateWayId
                    )
                    putString(
                        CURRENT_VARINT,
                        variantName
                    )
                    putInt(
                        CURRENT_QUANTITY,
                        quantity
                    )
                }

                SelectGateWayBottomSheet.show(bundle, childFragmentManager).setOnDismissListener {
                    setTenureDetailData()
                }
            }
        }
        quantityTextWatcher()

        detailHeader.quantityEditor.setValueChangedListener { newValue, _, _ ->
            quantity = newValue
            getCheckoutDetail()

        }

        activationTenureAdapter = ActivationTenureAdapter(listOf(), this)
        detailHeader.showVariantBottomSheet.setOnClickListener {
            sendVarintClickEvent()
            context?.let {
                AtcVariantHelper.goToAtcVariant(
                    context = it,
                    productId = productId,
                    pageSource = VariantPageSource.BNPL_PAGESOURCE,
                    isTokoNow = false,
                    shopId = shopId,
                    saveAfterClose = false
                ) { data, code ->
                    startActivityForResult(data, code)
                }
            }
        }

        priceBreakdown.setOnClickListener {
            if (this::installmentModel.isInitialized) {
                val bundle = Bundle().apply {
                    putParcelable(
                        PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL,
                        installmentModel
                    )
                }
                bottomSheetNavigator.showBottomSheet(PayLaterInstallmentFeeInfo::class.java, bundle)
            }

        }

        proceedToCheckout.setOnClickListener {
            payLaterActivationViewModel.addProductToCart(productId, quantity)
        }


    }

    private fun sendVarintClickEvent() {
        try {
            gatewayToChipMap[gateWayId.toInt()]?.let { checkoutData ->
                checkoutData.userAmount?.let { limit ->
                    checkoutData.userState?.let { userState->
                        sendAnalyticEvent(PdpSimulationEvent.OccChangeVariantClicked(
                            checkoutData.gateway_name, quantity.toString(),
                            checkoutData.tenureDetail[selectedTenurePosition].monthly_installment,
                            checkoutData.tenureDetail[selectedTenurePosition].tenure.toString(),
                            limit, variantName, userState
                        ))
                    }
                }

            }

        } catch (e: Exception) {

        }


    }

    private fun quantityTextWatcher() {
        detailHeader.quantityEditor.editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    detailHeader.quantityEditor.editText.clearFocus()
                    return true
                }

                return false
            }

        })
        detailHeader.quantityEditor.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    when {
                        s.toString().replace("[^0-9]".toRegex(), "")
                            .toInt() > detailHeader.quantityEditor.maxValue -> {
                            detailHeader.limiterMessage.visibility = View.VISIBLE
                            detailHeader.limiterMessage.text =
                                "Oops, stoknya tinggal ${detailHeader.quantityEditor.maxValue}"

                        }
                        s.toString().replace("[^0-9]".toRegex(), "").toInt() < 1 -> {
                            detailHeader.limiterMessage.visibility = View.VISIBLE
                            detailHeader.limiterMessage.text = "Min. beli 1 barang"

                        }
                        else -> detailHeader.limiterMessage.visibility = View.GONE
                    }
                }
            }

        })
    }

    private fun getCheckoutDetail() {
        amountBottomDetailLoader.visibility = View.VISIBLE
        gatewayDetailShimmer.visibility = View.VISIBLE
        gatewayDetailLayout.visibility = View.GONE
        payLaterActivationViewModel.getOptimizedCheckoutDetail(
            productId,
            payLaterActivationViewModel.price * quantity,
            gatewayCode
        )
    }

    override fun getScreenName(): String {
        return "Activation PayLater"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                if (this.selectedProductId.isNotBlank()) {
                    if (productId != this.selectedProductId) {
                        productId = this.selectedProductId
                        startAllLoaders()
                        payLaterActivationViewModel.getProductDetail(this.selectedProductId)
                    }

                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): ActivationCheckoutFragment {
            val fragment = ActivationCheckoutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isDisable(): Boolean {
        return isDisabled
    }


    override fun selectedTenure(
        tenureSelectedModel: TenureSelectedModel,
        newPositionToSelect: Int
    ) {
        tenureSelectedModel.installmentDetails?.let {
            installmentModel = it
        }
        tenureSelectedModel.priceText?.let {
            amountToPay.text = it
        }
        tenureSelectedModel.tenure?.let {
            paymentDuration.text = it
        }
        listOfTenureDetail[selectedTenurePosition].isSelectedTenure = false
        listOfTenureDetail[newPositionToSelect].isSelectedTenure = true
        selectedTenurePosition = newPositionToSelect
        activationTenureAdapter.updateList(listOfTenureDetail)

    }


}
