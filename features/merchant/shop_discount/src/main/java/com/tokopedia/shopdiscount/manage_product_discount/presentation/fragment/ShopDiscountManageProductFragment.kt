package com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentManageProductDiscountBinding
import com.tokopedia.shopdiscount.databinding.LayoutManageProductDiscountHeaderBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageProductActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountMultiLocEduActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel.ShopDiscountManageProductViewModel
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.set_period.presentation.bottomsheet.SetPeriodBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_R2_ABUSIVE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_START_DATE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE
import com.tokopedia.shopdiscount.utils.constant.UrlConstant.SELLER_EDU_R2_ABUSIVE_URL
import com.tokopedia.shopdiscount.utils.extension.digitsOnly
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
import com.tokopedia.shopdiscount.utils.formatter.SpannableHelper
import com.tokopedia.shopdiscount.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

class ShopDiscountManageProductFragment : BaseDaggerFragment() {

    companion object {
        const val PRODUCT_DATA_ARG = "product_data_arg"
        const val MODE_ARG = "mode_arg"
        private const val NUMBER_PATTERN = "#,###,###"
        private const val DISCOUNT_PERCENTAGE_MAX_DIGIT = 2

        fun createInstance(
            mode: String,
            productData: ShopDiscountSetupProductUiModel.SetupProductData
        ) = ShopDiscountManageProductFragment().apply {
            arguments = Bundle().apply {
                putString(MODE_ARG, mode)
                putParcelable(PRODUCT_DATA_ARG, productData)
            }
        }
    }

    private var viewBinding by autoClearedNullable<FragmentManageProductDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageProductFragment::class.java.canonicalName.orEmpty()

    private var mode: String = ""
    private var headerUnify: HeaderUnify? = null
    private var tickerMultiLoc: Ticker? = null
    private var loadingSpinner: View? = null
    private var containerProductDataLayout: View? = null
    private var layoutGlobalError: GlobalError? = null
    private var imageProduct: ImageUnify? = null
    private var textProductName: Typography? = null
    private var textProductOriginalPrice: Typography? = null
    private var textStockAndLocation: Typography? = null
    private var imageIconProduct: ImageUnify? = null
    private var discountPeriodSection: View? = null
    private var textDiscountPeriodRange: Typography? = null
    private var textDiscountPeriodError: Typography? = null
    private var textFieldDiscountPrice: TextFieldUnify2? = null
    private var textFieldDiscountPercentage: TextFieldUnify2? = null
    private var tickerR2AbusiveError: Ticker? = null
    private var quantityEditorMaxOrder: QuantityEditorUnify? = null
    private var buttonApply: UnifyButton? = null
    private var selectedPeriodChip: Int = 0

    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ShopDiscountManageProductViewModel::class.java) }
    private var textFieldDiscountPercentageWatcher: NumberThousandSeparatorTextWatcher? = null
    private var textFieldDiscountPriceWatcher: NumberThousandSeparatorTextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentManageProductDiscountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentsData()
        observeLiveData()
        initView()
        setupToolbar()
        setupTextFieldDiscountPrice()
        setupTextFieldDiscountPercentage()
        setupQuantityEditorMaxOrder()
        setupButtonApply()
        getSellerSlashPriceBenefit()
    }

    private fun setupButtonApply() {
        buttonApply?.setOnClickListener {
            val productData = viewModel.getProductData()
            val intentData = Intent().apply {
                putExtra(ShopDiscountManageProductActivity.PRODUCT_DATA_RESULT, productData)
            }
            activity?.setResult(Activity.RESULT_OK, intentData)
            activity?.finish()
        }
    }

    private fun setupQuantityEditorMaxOrder() {
        quantityEditorMaxOrder?.setValueChangedListener { newValue, _, _ ->
            viewModel.updateMaxOrderData(newValue)
        }
        quantityEditorMaxOrder?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateMaxOrderData(quantityEditorMaxOrder?.getValue().orZero())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun configDiscountPeriodSection(
        productData: ShopDiscountSetupProductUiModel.SetupProductData,
        slashPriceBenefitData: ShopDiscountSellerInfoUiModel
    ) {
        discountPeriodSection?.setOnClickListener {
            openSetPeriodBottomSheet(productData)
        }
        val startDateUnix = productData.slashPriceInfo.startDate.time
        if (startDateUnix < 0) {
            setDiscountPeriodBasedOnBenefit(slashPriceBenefitData)
        } else {
            setDiscountPeriodBasedOnExistingData(productData)
        }
    }

    private fun openSetPeriodBottomSheet(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val bottomSheet = SetPeriodBottomSheet.newInstance(
            productData.slashPriceInfo.startDate.time,
            productData.slashPriceInfo.endDate.time,
            productData.slashPriceInfo.slashPriceStatusId,
            selectedPeriodChip,
            mode
        )
        bottomSheet.setOnApplyClickListener { setPeriodResultModel, selectedPeriodChip ->
            this.selectedPeriodChip = selectedPeriodChip
            setDiscountPeriodBasedOnBottomSheetResult(setPeriodResultModel)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun setDiscountPeriodBasedOnBenefit(slashPriceBenefitData: ShopDiscountSellerInfoUiModel) {
        viewModel.getDiscountPeriodDataBasedOnBenefit(slashPriceBenefitData)
    }

    private fun setDiscountPeriodBasedOnExistingData(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        val startDate = setupProductData.slashPriceInfo.startDate
        val endDate = setupProductData.slashPriceInfo.endDate
        viewModel.updateProductDiscountPeriodData(
            startDate,
            endDate
        )
    }

    private fun getFormattedDiscountPeriod(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData
    ): String {
        val startDate = setupProductData.slashPriceInfo.startDate
        val endDate = setupProductData.slashPriceInfo.endDate
        val startDateFormatted = startDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
        val endDateFormatted = endDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
        return String.format(
            getString(R.string.product_detail_start_date_end_date_format),
            startDateFormatted,
            endDateFormatted
        )
    }

    private fun setDiscountPeriodBasedOnBottomSheetResult(
        setPeriodResultUiModel: SetPeriodResultUiModel
    ) {
        val startDate = setPeriodResultUiModel.startDate
        val endDate = setPeriodResultUiModel.endDate
        viewModel.updateProductDiscountPeriodData(startDate, endDate)
    }

    private fun setupTextFieldDiscountPrice() {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)
        textFieldDiscountPrice?.apply {
            textInputLayout.errorIconDrawable = null
            textInputLayout.editText?.let {
                textFieldDiscountPriceWatcher = NumberThousandSeparatorTextWatcher(
                    it,
                    numberFormatter
                ) { price, formattedNumber ->
                    it.setText(formattedNumber)
                    it.setSelection(it.text?.length.orZero())
                    viewModel.updateDiscountPrice(price)
                    viewModel.validateInput()
                }
                it.addTextChangedListener(textFieldDiscountPriceWatcher)
            }
        }
    }

    private fun setupTextFieldDiscountPercentage() {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)
        textFieldDiscountPercentage?.apply {
            appendText("%   ")
            textInputLayout.errorIconDrawable = null
            textInputLayout.editText?.let {
                it.filters = arrayOf(InputFilter.LengthFilter(DISCOUNT_PERCENTAGE_MAX_DIGIT))
                textFieldDiscountPercentageWatcher = NumberThousandSeparatorTextWatcher(
                    it,
                    numberFormatter
                ) { discountPercent, formattedNumber ->
                    it.setText(formattedNumber)
                    it.setSelection(it.text?.length.orZero())
                    viewModel.updateDiscountPercent(discountPercent)
                    viewModel.validateInput()
                }
                it.addTextChangedListener(textFieldDiscountPercentageWatcher)
            }
        }
    }

    private fun observeLiveData() {
        observeSellerBenefitLiveData()
        observeUpdatedDiscountPeriodData()
        observeUpdatedDiscountPercentageData()
        observeUpdatedDiscountPriceData()
        observeInputValidation()
        observeDiscountPeriodDataBasedOnBenefitLiveData()
    }

    private fun observeDiscountPeriodDataBasedOnBenefitLiveData() {
        viewModel.discountPeriodDataBasedOnBenefitLiveData.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.updateProductDiscountPeriodData(it.first, it.second)
            }
        }
    }

    private fun observeInputValidation() {
        viewModel.inputValidation.observe(viewLifecycleOwner) { errorValidation ->
            buttonApply?.isEnabled = errorValidation == NONE
            tickerR2AbusiveError?.hide()
            textDiscountPeriodError?.hide()
            textFieldDiscountPrice?.textInputLayout?.setOnClickListener(null)
            textFieldDiscountPrice?.textInputLayout?.error = null
            textFieldDiscountPercentage?.textInputLayout?.error = null
            when (errorValidation) {
                ERROR_PRICE_MAX -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        String.format(
                            getString(R.string.shop_discount_manage_product_error_max_price_format),
                            viewModel.getMaxDiscountPrice().getCurrencyFormatted()
                        )
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ERROR_PRICE_MIN -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        String.format(
                            getString(R.string.shop_discount_manage_product_error_min_price_format),
                            viewModel.getMinDiscountPrice().getCurrencyFormatted()
                        )
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ERROR_R2_ABUSIVE -> {
                    textFieldDiscountPrice?.textInputLayout?.error = getR2AbusiveErrorMessage()
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                    textFieldDiscountPrice?.textInputLayout?.setOnClickListener {
                        redirectToWebViewR2AbusiveSellerInformation()
                    }
                    tickerR2AbusiveError?.apply {
                        show()
                        val averageSoldPrice =
                            viewModel.getProductData().listProductWarehouse.firstOrNull()?.avgSoldPrice.orZero()
                                .getCurrencyFormatted()
                        val tickerDesc = String.format(
                            getString(R.string.shop_discount_manage_product_error_r2_abusive_ticker_desc_format),
                            averageSoldPrice
                        )
                        setHtmlDescription(tickerDesc)
                        setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                applyRecommendedSoldPriceToDiscountedPrice()
                            }

                            override fun onDismiss() {
                            }
                        })
                    }
                }
                ERROR_START_DATE -> {
                    textDiscountPeriodError?.show()
                }
            }
        }
    }

    private fun applyRecommendedSoldPriceToDiscountedPrice() {
        val averageSoldPrice =
            viewModel.getProductData().listProductWarehouse.firstOrNull()?.avgSoldPrice.orZero()
        textFieldDiscountPriceWatcher?.isForceTextChanged = true
        textFieldDiscountPrice?.editText?.setText(averageSoldPrice.toString())
    }

    private fun getR2AbusiveErrorMessage(): SpannableString {
        val errorMessageFormat =
            getString(R.string.shop_discount_manage_product_error_r2_abusive_format)
        val errorStringToBeColorSpanned =
            getString(R.string.shop_discount_manage_product_error_r2_abusive_spanned_text)
        val errorMessage = SpannableString(
            String.format(
                errorMessageFormat,
                errorStringToBeColorSpanned
            )
        )
        SpannableHelper.setSpannedColorString(
            context,
            errorMessage,
            errorStringToBeColorSpanned,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        return errorMessage
    }

    private fun redirectToWebViewR2AbusiveSellerInformation() {
        RouteManager.route(
            context,
            String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                SELLER_EDU_R2_ABUSIVE_URL
            )
        )
    }

    private fun observeUpdatedDiscountPercentageData() {
        viewModel.updatedDiscountPercentageData.observe(viewLifecycleOwner) {
            textFieldDiscountPercentage?.textInputLayout?.editText?.apply {
                removeTextChangedListener(textFieldDiscountPercentageWatcher)
                setText(it.toString())
                addTextChangedListener(textFieldDiscountPercentageWatcher)
            }
        }
    }

    private fun observeUpdatedDiscountPriceData() {
        viewModel.updatedDiscountPriceData.observe(viewLifecycleOwner) {
            textFieldDiscountPrice?.textInputLayout?.editText?.apply {
                removeTextChangedListener(textFieldDiscountPriceWatcher)
                setText(it.toString())
                addTextChangedListener(textFieldDiscountPriceWatcher)
            }
        }
    }

    private fun observeUpdatedDiscountPeriodData() {
        viewModel.updatedDiscountPeriodData.observe(viewLifecycleOwner) {
            it?.let {
                updateDiscountPeriodText(it)
                checkShouldValidateInputAfterPopulateData(
                    viewModel.getProductData().mappedResultData.minDisplayedPrice
                )
            }
        }
    }

    private fun updateDiscountPeriodText(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        textDiscountPeriodRange?.text = getFormattedDiscountPeriod(productData)
    }

    private fun observeSellerBenefitLiveData() {
        viewModel.slashPriceBenefitLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    showProductDataContent()
                    populateProductData(it.data, viewModel.getProductData())
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorState(errorMessage)
                }
            }
        }
    }

    private fun populateProductData(
        slashPriceBenefitData: ShopDiscountSellerInfoUiModel,
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        val isMultiLoc = productData.productStatus.isMultiLoc
        configTickerMultiLoc(isMultiLoc)
        imageProduct?.loadImage(productData.productImageUrl)
        textProductName?.text = productData.productName
        textProductOriginalPrice?.apply {
            show()
            text = getOriginalPriceFormatted(productData.mappedResultData)
        }
        setTotalStockAndLocationData(productData)
        configDiscountPeriodSection(productData, slashPriceBenefitData)
        setProductPriceData(productData)
        configQuantityEditorMaxOrder(productData)
    }

    private fun configTickerMultiLoc(isMultiLoc: Boolean) {
        tickerMultiLoc?.shouldShowWithAction(isMultiLoc) {
            val description = getString(
                R.string.shop_discount_manage_product_discount_multi_loc_ticker_desc
            )
            tickerMultiLoc?.closeButtonVisibility = View.VISIBLE
            tickerMultiLoc?.setHtmlDescription(description)
            tickerMultiLoc?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    redirectToMultiLocEduPage()
                }

                override fun onDismiss() {
                }
            })
        }
    }

    private fun redirectToMultiLocEduPage() {
        context?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalSellerapp.SHOP_DISCOUNT_MANAGE_PRODUCT_MULTI_LOC_DISCOUNT
            )
            intent.putExtra(ShopDiscountMultiLocEduActivity.MODE_PARAM, mode)
            startActivity(intent)
        }
    }

    private fun checkShouldValidateInputAfterPopulateData(initialDiscountPrice: Int) {
        if (initialDiscountPrice.isMoreThanZero()) {
            viewModel.validateInput()
        }
    }

    private fun configQuantityEditorMaxOrder(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        quantityEditorMaxOrder?.apply {
            maxValue = productData.stock.toIntOrZero()
            setValue(productData.listProductWarehouse.firstOrNull()?.maxOrder.toIntOrZero())
        }
    }

    private fun setProductPriceData(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)
        val priceData = productData.mappedResultData.minDisplayedPrice.toString().digitsOnly()
        val formattedPriceData = numberFormatter.format(priceData)
        textFieldDiscountPrice?.editText?.setText(formattedPriceData)
        textFieldDiscountPercentage?.editText?.setText(productData.mappedResultData.minDiscountPercentage.toString())
    }

    private fun setTotalStockAndLocationData(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val totalStock = productData.stock
        if (totalStock.toIntOrZero().isZero()) {
            imageIconProduct?.hide()
            textStockAndLocation?.hide()
        } else {
            imageIconProduct?.show()
            textStockAndLocation?.apply {
                show()
                text = MethodChecker.fromHtml(getTotalStockAndLocationData(productData))
            }
        }
    }

    private fun getOriginalPriceFormatted(
        mappedResultData: ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData
    ): String {
        return RangeFormatterUtil.getFormattedRangeString(
            mappedResultData.minOriginalPrice,
            mappedResultData.maxOriginalPrice,
            {
                it.getCurrencyFormatted()
            },
            { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_product_discount_original_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
    }

    private fun getTotalStockAndLocationData(
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): String {
        val totalStock = uiModel.stock
        val totalLocation = uiModel.mappedResultData.totalLocation
        return when {
            shouldUseNonVariantMultiLocTotalStockFormat(uiModel.productStatus) -> {
                String.format(
                    getString(R.string.sd_total_stock_multiple_location_plain),
                    totalStock,
                    totalLocation
                )
            }
            else -> {
                String.format(
                    getString(R.string.sd_total_stock_plain),
                    totalStock
                )
            }
        }
    }

    private fun shouldUseNonVariantMultiLocTotalStockFormat(
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ): Boolean {
        return !productStatus.isVariant && productStatus.isMultiLoc
    }

    private fun showErrorState(errorMessage: String) {
        layoutGlobalError?.apply {
            show()
            errorSecondaryAction.hide()
            errorTitle.text = errorMessage
            setActionClickListener {
                getSellerSlashPriceBenefit()
            }
        }
    }

    private fun showProductDataContent() {
        containerProductDataLayout?.show()
    }

    private fun hideLoading() {
        loadingSpinner?.hide()
    }

    private fun getSellerSlashPriceBenefit() {
        showLoading()
        hideErrorState()
        viewModel.getSlashPriceBenefit()
    }

    private fun hideErrorState() {
        layoutGlobalError?.hide()
    }

    private fun showLoading() {
        loadingSpinner?.show()
    }

    private fun getArgumentsData() {
        arguments?.let {
            mode = it.getString(MODE_ARG).orEmpty()
            viewModel.setProductData(
                it.getParcelable(PRODUCT_DATA_ARG)
                    ?: ShopDiscountSetupProductUiModel.SetupProductData()
            )
        }
    }

    private fun initView() {
        viewBinding?.let {
            headerUnify = it.headerUnify
            tickerMultiLoc = it.layoutTickerMultiLoc.tickerMultiLoc
            loadingSpinner = it.loadingSpinner
            containerProductDataLayout = it.containerProductDataLayout
            layoutGlobalError = it.layoutGlobalError
            initViewManageDiscountHeader(it.manageProductDiscountHeader)
            it.layoutFieldContainer.let { layoutFieldContainer ->
                discountPeriodSection = layoutFieldContainer.discountPeriodSection
                textDiscountPeriodError = layoutFieldContainer.textDiscountPeriodError
                textDiscountPeriodRange = layoutFieldContainer.textDiscountPeriodRange
                textFieldDiscountPrice = layoutFieldContainer.textFieldPrice
                textFieldDiscountPercentage = layoutFieldContainer.textFieldDiscount
                tickerR2AbusiveError = layoutFieldContainer.tickerR2AbusiveError
                quantityEditorMaxOrder = layoutFieldContainer.quantityEditorMaxOrder
            }
            buttonApply = it.buttonApply
        }
    }

    private fun initViewManageDiscountHeader(binding: LayoutManageProductDiscountHeaderBinding) {
        imageProduct = binding.imgProduct
        textProductName = binding.textProductName
        textProductOriginalPrice = binding.textOriginalPrice
        textStockAndLocation = binding.textTotalStockAndLocation
        imageIconProduct = binding.iconProduct
    }

    private fun setupToolbar() {
        headerUnify?.apply {
            title = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    getString(R.string.shop_discount_manage_product_discount_manage_toolbar_title)
                }
                ShopDiscountManageDiscountMode.UPDATE -> {
                    getString(R.string.shop_discount_manage_product_discount_edit_toolbar_title)
                }
                else -> {
                    ""
                }
            }
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    fun onBackPressed() {
        activity?.finish()
    }
}
