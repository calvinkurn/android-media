package com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
import com.tokopedia.shopdiscount.databinding.FragmentManageProductDiscountBinding
import com.tokopedia.shopdiscount.databinding.LayoutManageProductDiscountHeaderBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageProductDiscountActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.activity.ShopDiscountManageProductDiscountMultiLocActivity
import com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel.ShopDiscountManageProductDiscountViewModel
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.set_period.presentation.bottomsheet.SetPeriodBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE
import com.tokopedia.shopdiscount.utils.extension.parseTo
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

class ShopDiscountManageProductDiscountFragment : BaseDaggerFragment() {

    companion object {
        const val PRODUCT_DATA_ARG = "product_data_arg"
        const val MODE_ARG = "mode_arg"
        private const val NUMBER_PATTERN = "#,###,###"
        private const val DISCOUNT_PERCENTAGE_MAX_DIGIT = 2

        fun createInstance(
            mode: String,
            productData: ShopDiscountSetupProductUiModel.SetupProductData
        ) = ShopDiscountManageProductDiscountFragment().apply {
            arguments = Bundle().apply {
                putString(MODE_ARG, mode)
                putParcelable(PRODUCT_DATA_ARG, productData)
            }
        }
    }

    private var viewBinding by autoClearedNullable<FragmentManageProductDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageProductDiscountFragment::class.java.canonicalName.orEmpty()

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
    private var textFieldDiscountPrice: TextFieldUnify2? = null
    private var textFieldDiscountPercentage: TextFieldUnify2? = null
    private var quantityEditorMaxOrder: QuantityEditorUnify? = null
    private var buttonApply: UnifyButton? = null

    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ShopDiscountManageProductDiscountViewModel::class.java) }
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
                putExtra(ShopDiscountManageProductDiscountActivity.PRODUCT_DATA_RESULT, productData)
            }
            activity?.setResult(Activity.RESULT_OK, intentData)
            activity?.finish()
        }
    }

    private fun setupQuantityEditorMaxOrder() {
        quantityEditorMaxOrder?.setValueChangedListener { newValue, _, _ ->
            viewModel.updateMaxOrderData(newValue)
        }
    }

    private fun configDiscountPeriodSection(
        productData: ShopDiscountSetupProductUiModel.SetupProductData,
        slashPriceBenefitData: ShopDiscountSellerInfoUiModel
    ) {
        discountPeriodSection?.setOnClickListener {
            openSetPeriodBottomSheet(productData)
        }
        when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                setDiscountPeriodBasedOnBenefit(slashPriceBenefitData)
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                setDiscountPeriodBasedOnExistingData(productData)
            }
        }
    }

    private fun openSetPeriodBottomSheet(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val bottomSheet = SetPeriodBottomSheet.newInstance(
            productData.slashPriceInfo.startDate.time,
            productData.slashPriceInfo.endDate.time,
            productData.slashPriceInfo.slashPriceStatusId
        )
        bottomSheet.setOnApplyClickListener {
            setDiscountPeriodBasedOnBottomSheetResult(it)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun setDiscountPeriodBasedOnBenefit(slashPriceBenefitData: ShopDiscountSellerInfoUiModel) {
        val startDate = viewModel.defaultStartDate
        val endDate = if (slashPriceBenefitData.isUseVps) {
            viewModel.getVpsPackageDefaultEndDate(slashPriceBenefitData)
        } else {
            viewModel.getMembershipDefaultEndDate()
        }
        viewModel.updateProductDiscountPeriodData(
            startDate,
            endDate
        )
    }

    private fun setDiscountPeriodBasedOnExistingData(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData,
    ) {
        val startDate = setupProductData.slashPriceInfo.startDate
        val endDate = setupProductData.slashPriceInfo.endDate
        viewModel.updateProductDiscountPeriodData(
            startDate,
            endDate
        )
    }

    private fun getFormattedDiscountPeriod(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData,
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
            appendText(getString(R.string.sd_percent))
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
    }

    private fun observeInputValidation() {
        viewModel.inputValidation.observe(viewLifecycleOwner, { errorValidation ->
            buttonApply?.isEnabled = errorValidation == NONE
            when (errorValidation) {
                ERROR_PRICE_MAX -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        "Maks. ${viewModel.getMaxDiscountPrice().getCurrencyFormatted()}"
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ERROR_PRICE_MIN -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        "Min ${viewModel.getMinDiscountPrice().getCurrencyFormatted()}"
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                NONE -> {
                    textFieldDiscountPrice?.textInputLayout?.error = null
                    textFieldDiscountPercentage?.textInputLayout?.error = null
                }
            }
        })
    }

    private fun observeUpdatedDiscountPercentageData() {
        viewModel.updatedDiscountPercentageData.observe(viewLifecycleOwner, {
            textFieldDiscountPercentage?.textInputLayout?.editText?.apply {
                removeTextChangedListener(textFieldDiscountPercentageWatcher)
                setText(it.toString())
                addTextChangedListener(textFieldDiscountPercentageWatcher)
            }
        })
    }

    private fun observeUpdatedDiscountPriceData() {
        viewModel.updatedDiscountPriceData.observe(viewLifecycleOwner, {
            textFieldDiscountPrice?.textInputLayout?.editText?.apply {
                removeTextChangedListener(textFieldDiscountPriceWatcher)
                setText(it.toString())
                addTextChangedListener(textFieldDiscountPriceWatcher)
            }
        })
    }

    private fun observeUpdatedDiscountPeriodData() {
        viewModel.updatedDiscountPeriodData.observe(viewLifecycleOwner, {
            it?.let {
                updateDiscountPeriodText(it)
            }
        })
    }

    private fun updateDiscountPeriodText(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        textDiscountPeriodRange?.text = getFormattedDiscountPeriod(productData)
    }

    private fun observeSellerBenefitLiveData() {
        viewModel.slashPriceBenefitLiveData.observe(viewLifecycleOwner, {
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
        })
    }

    private fun populateProductData(
        slashPriceBenefitData: ShopDiscountSellerInfoUiModel,
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        val isMultiLoc = productData.productStatus.isMultiLoc
        configTickerMultiLoc(isMultiLoc)
        imageProduct?.loadImage(productData.productImageUrl)
        textProductName?.text = productData.productName
        textProductOriginalPrice?.text = getOriginalPriceFormatted(productData.mappedResultData)
        setTotalStockAndLocationData(productData)
        configDiscountPeriodSection(productData, slashPriceBenefitData)
        setProductPriceData(productData)
        configQuantityEditorMaxOrder(productData)
        checkShouldValidateInputAfterPopulateData()
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
            intent.putExtra(ShopDiscountManageProductDiscountMultiLocActivity.MODE_PARAM, mode)
            startActivity(intent)
        }
    }

    private fun checkShouldValidateInputAfterPopulateData() {
        val initialDiscountPrice = textFieldDiscountPrice?.editText?.text?.toString()?.toIntOrZero()
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
        textFieldDiscountPrice?.editText?.setText(productData.mappedResultData.minDisplayedPrice.toString())
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
            mappedResultData.maxOriginalPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
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
                textDiscountPeriodRange = layoutFieldContainer.textDiscountPeriodRange
                textFieldDiscountPrice = layoutFieldContainer.textFieldPrice
                textFieldDiscountPercentage = layoutFieldContainer.textFieldDiscount
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