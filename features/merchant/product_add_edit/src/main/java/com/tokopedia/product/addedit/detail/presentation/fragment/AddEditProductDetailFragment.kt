package com.tokopedia.product.addedit.detail.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.AddEditProductFragment
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.DOUBLE_ZERO
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.FIRST_CATEGORY_SELECTED
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.LIGHT_BULB_ICON
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.ROUND_GREEN_CHECK_MARK_ICON
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.TOP_SCROLL_MARGIN
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.common.util.StringValidationUtil.filterDigit
import com.tokopedia.product.addedit.databinding.FragmentAddEditProductDetailLayoutBinding
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailModule
import com.tokopedia.product.addedit.detail.di.DaggerAddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionByKeyword
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionSuggestedPriceGet
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import com.tokopedia.product.addedit.detail.presentation.adapter.WholeSalePriceInputAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_FULL_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_USED
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_LENGTH_PRICE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.NEW_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PRICE_RECOMMENDATION_BANNER_URL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE_IMPROVEMENT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SPECIFICATION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DETAIL_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.USED_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.customview.TypoCorrectionView
import com.tokopedia.product.addedit.detail.presentation.dialog.PriceSuggestionBottomSheet
import com.tokopedia.product.addedit.detail.presentation.dialog.PriceSuggestionInfoBottomSheet
import com.tokopedia.product.addedit.detail.presentation.dialog.ServiceFeeBottomSheet
import com.tokopedia.product.addedit.detail.presentation.dialog.TitleValidationBottomSheet
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PriceSuggestion
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.imagepicker.ImagePickerAddEditNavigation
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FIRST_MOVED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragmentArgs
import com.tokopedia.product.addedit.specification.presentation.activity.AddEditProductSpecificationActivity
import com.tokopedia.product.addedit.tooltip.model.NumericWithDescriptionTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.MediaImprovementTracker
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantDetailActivity
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.SHOWCASE_PICKER_RESULT_REQUEST_CODE
import com.tokopedia.shop.common.constant.ShopStatusLevelDef
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class AddEditProductDetailFragment : AddEditProductFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener,
        WholeSaleInputViewHolder.OnAddButtonClickListener,
        AddEditProductPerformanceMonitoringListener, PriceSuggestionBottomSheet.Listener {

    companion object {
        const val AMOUNT_CATEGORY_RECOM_DEFAULT = 3
        private fun getDurationUnit(type: Int) =
                when (type) {
                    UNIT_DAY -> R.string.label_day
                    UNIT_WEEK -> R.string.label_week
                    else -> -1
                }
    }

    @Inject
    lateinit var viewModel: AddEditProductDetailViewModel

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    private var binding by autoClearedNullable<FragmentAddEditProductDetailLayoutBinding>()

    private var selectedDurationPosition: Int = UNIT_DAY
    private var isPreOrderFirstTime = true
    private var countTouchPhoto = 0
    private var hasCategoryFromPicker = false
    private var isFragmentVisible = false
    private var needToSetCategoryName = false

    private val scrollViewParent by lazy { binding?.scrollViewParent }

    // product photo
    private var addProductPhotoButton: AppCompatTextView? = null
    private var productPhotosView: RecyclerView? = null
    private var productPhotoAdapter: ProductPhotoAdapter? = null
    private var photoItemTouchHelper: ItemTouchHelper? = null
    private var productPictureList: List<PictureInputModel>? = null

    // product name
    private var productNameField: TextFieldUnify2? = null
    private var productNameRecView: RecyclerView? = null
    private var productNameRecShimmering: View? = null
    private var productNameRecAdapter: NameRecommendationAdapter? = null
    private var typoCorrection: TypoCorrectionView? = null

    // product category
    private var productCategoryId: String = ""
    private var productCategoryName: String = ""
    private var productCategoryLayout: ViewGroup? = null
    private var productCategoryRecListView: ListUnify? = null
    private var productCategoryPickerButton: AppCompatTextView? = null
    private var categoryAlertDialog: DialogUnify? = null
    private var additionalInfoView: Typography? = null
    private var commissionInfoTipsView: TipsUnify? = null

    // product specification
    private var productSpecificationLayout: ViewGroup? = null
    private var productSpecificationTextView: Typography? = null
    private var productSpecificationHeaderTextView: Typography? = null
    private var addProductSpecificationButton: Typography? = null
    private var productSpecificationReloadLayout: View? = null
    private var productSpecificationReloadButton: Typography? = null
    private var tooltipSpecificationRequired: View? = null

    // product price
    private var productPriceField: TextFieldUnify2? = null
    private var productPriceLayout: ViewGroup? = null
    private var productPriceVariantTicker: Ticker? = null

    // product price suggestion
    private var productPriceSuggestionLayout: ViewGroup? = null
    private var priceSuggestionLayout: ViewGroup? = null
    private var priceSuggestionShimmering: ViewGroup? = null
    private var priceSuggestionStatusView: ImageUnify? = null
    private var priceSuggestionLabelView: Typography? = null
    private var priceSuggestionRangeView: Typography? = null
    private var priceSuggestionCtaView: Typography? = null
    private var priceSuggestionBottomSheet: PriceSuggestionBottomSheet? = null
    private var priceSuggestionInfoBottomSheet: PriceSuggestionInfoBottomSheet? = null

    // product wholesale price
    private var productWholeSaleSwitch: SwitchUnify? = null
    private var productWholeSaleInputLayout: ViewGroup? = null
    private var productWholeSaleInputFormsView: RecyclerView? = null
    private var wholeSaleInputFormsAdapter: WholeSalePriceInputAdapter? = null
    private var addNewWholeSalePriceButton: AppCompatTextView? = null

    // product stock
    private var productStockField: TextFieldUnify? = null
    private var productMinOrderField: TextFieldUnify? = null
    private var tvProductStockHeader: Typography? = null

    // product pre order
    private var preOrderSwitch: SwitchUnify? = null
    private var preOrderInputLayout: ViewGroup? = null
    private var preOrderDurationField: TextFieldUnify? = null
    private var preOrderDurationUnitField: TextFieldUnify? = null

    // product conditions
    private var productConditionListView: ListUnify? = null
    private val productConditions = ArrayList<ListItemUnify>()
    private var isProductConditionNew = true

    // product sku
    private var productSkuField: TextFieldUnify? = null

    // product show case
    private var productShowCasesView: Typography? = null
    private var addProductShowCaseButton: Typography? = null
    private var productShowCasesReloadLayout: View? = null
    private var productShowCasesReloadButton: Typography? = null

    // button continue
    private var submitButton: UnifyButton? = null

    // static content bottom sheets
    private var serviceFeeBottomSheet: ServiceFeeBottomSheet? = null

    // PLT monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun getScreenName(): String {
        return getString(R.string.product_add_edit_detail)
    }

    override fun initInjector() {
        DaggerAddEditProductDetailComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .addEditProductDetailModule(AddEditProductDetailModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // start PLT monitoring
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cacheManagerId = AddEditProductDetailFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

            // set detail and variant input model
            cacheManagerId.run {
                viewModel.productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                        ?: ProductInputModel()
                var pictureIndex = 0
                viewModel.productPhotoPaths = viewModel.productInputModel.detailInputModel.imageUrlOrPathList.map { urlOrPath ->
                    if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) viewModel.productInputModel.detailInputModel.pictureList[pictureIndex++].urlThumbnail
                    else urlOrPath
                }.toMutableList()
                viewModel.isEditing = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isAdding = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isDrafting = saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isFirstMoved = saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java)
                        ?: false
            }
        }
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        selectedDurationPosition = viewModel.productInputModel.detailInputModel.preorder.timeUnit

        if (viewModel.isAdding) {
            ProductAddMainTracking.trackScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddEditProductDetailLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        // set navigation highlight
        highlightNavigationButton(PageIndicator.INDICATOR_DETAIL_PAGE)

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        setupProductPhotoViews()
        setupProductNameViews()
        setupProductCategoryViews()
        setupSpecificationViews()
        setupProductPriceViews()
        setupPriceSuggestionViews()
        setupWholesaleViews()
        setupStockViews()
        setupPreOrderViews()
        setupProductConditionViews()
        setupProductSkuViews()
        setupProductShowcaseViews()
        setupProductSubmitButtonViews()
        setupServiceFeeBottomSheet()

        // fill the form with detail input model
        fillProductDetailForm(viewModel.productInputModel.detailInputModel)
        initPriceSuggestion(viewModel.isEditing)
        initProductShowcaseValue()
        initCommissionInfo(shopId)
        setupDefaultFieldMessage()
        setupSpecificationField()
        enableProductNameField()
        onFragmentResult()
        setupBackPressed()

        subscribeToProductNameInputStatus()
        subscribeToProductNameRecommendation()
        subscribeToCategoryRecommendation()
        subscribeToProductPriceInputStatus()
        subscribeToWholeSaleSwitchStatus()
        subscribeToProductStockInputStatus()
        subscribeToOrderQuantityInputStatus()
        subscribeToPreOrderSwitchStatus()
        subscribeToPreOrderDurationInputStatus()
        subscribeToProductSkuInputStatus()
        subscribeToShopShowCases()
        subscribeToShopInfo()
        subscribeToCommissionInfo()
        subscribeToAnnotationCategoryData()
        subscribeToSpecificationText()
        subscribeToHasRequiredSpecification()
        subscribeToSelectedSpecificationList()
        subscribeToInputStatus()
        subscribeToProductNameValidationFromNetwork()
        subscribeToMaxStockThreshold()
        if (viewModel.isEditing) subscribeToEditProductPriceSuggestion()
        else subscribeToAddProductPriceSuggestion()

        // stop PLT monitoring, because no API hit at load page
        stopPreparePagePerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            inputAllDataInProductInputModel()
            outState.putString(KEY_SAVE_INSTANCE_INPUT_MODEL, mapObjectToJson(viewModel.productInputModel))
            outState.putBoolean(KEY_SAVE_INSTANCE_ISADDING, viewModel.isAdding)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISEDITING, viewModel.isEditing)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISDRAFTING, viewModel.isDrafting)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED, viewModel.isFirstMoved)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val productInputModelJson = savedInstanceState.getString(KEY_SAVE_INSTANCE_INPUT_MODEL)
            viewModel.isAdding = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISADDING)
            viewModel.isEditing = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISEDITING)
            viewModel.isDrafting = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISDRAFTING)
            viewModel.isFirstMoved = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED)

            if (!productInputModelJson.isNullOrBlank()) {
                //set product input model and and ui of the page
                val productInputModel = mapJsonToObject(productInputModelJson, ProductInputModel::class.java)
                viewModel.productInputModel = productInputModel
                if (!productInputModel.detailInputModel.imageUrlOrPathList.isNullOrEmpty()) {
                    viewModel.productPhotoPaths = productInputModel.detailInputModel.imageUrlOrPathList as MutableList<String>
                }
                fillProductDetailForm(productInputModel.detailInputModel)
                setupButton()
            }
            // only need set category, no need to get category list
            needToSetCategoryName = true
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentVisible = false
        removeObservers()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_DETAIL_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_DETAIL_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_DETAIL_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun setupButton() {
        if (viewModel.isAdding && viewModel.isFirstMoved) {
            submitButton?.text = getString(R.string.action_continue)
        } else {
            submitButton?.text = getString(R.string.action_save)
        }
    }

    private fun removeObservers() {
        viewModel.isProductNameInputError.removeObservers(this)
        viewModel.productNameRecommendations.removeObservers(this)
        viewModel.productCategoryRecommendationLiveData.removeObservers(this)
        viewModel.isProductPriceInputError.removeObservers(this)
        viewModel.isWholeSalePriceActivated.removeObservers(this)
        viewModel.isProductStockInputError.removeObservers(this)
        viewModel.isOrderQuantityInputError.removeObservers(this)
        viewModel.isPreOrderActivated.removeObservers(this)
        viewModel.isPreOrderDurationInputError.removeObservers(this)
        viewModel.isInputValid.removeObservers(this)
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.removeObservers(this)
    }

    private fun submitInputData() {
        val isAdding = viewModel.isAdding
        val isDrafting = viewModel.isDrafting
        val isFirstMoved = viewModel.isFirstMoved
        if (isAdding && isFirstMoved) moveToDescriptionActivity()
        else if (isAdding && !isDrafting) submitInput()
        else submitInputEdit()
    }

    private fun updateAddNewWholeSalePriceButtonVisibility() {
        wholeSaleInputFormsAdapter?.itemCount?.let {
            if (it >= AddEditProductDetailConstants.MAX_WHOLESALE_PRICES) {
                addNewWholeSalePriceButton?.visibility = View.GONE
            }
        }
    }

    private fun validateInput() {

        var requestedFocus = false

        // product photo validation
        productPhotoAdapter?.let { viewModel.validateProductPhotoInput(it.itemCount) }

        // product price validation
        val productPriceInput = productPriceField?.getEditableValue().toString().replace(".", "")
        viewModel.validateProductPriceInput(productPriceInput)
        viewModel.isProductPriceInputError.value?.run {
            if (this && !requestedFocus) {
                productPriceField?.requestFocus()
                requestedFocus = true
            }
        }

        // product wholesale input validation
        viewModel.isWholeSalePriceActivated.value?.run {
            if (this) validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
        }

        // product stock validation
        val productStockInput = productStockField?.getEditableValue().toString()
        viewModel.validateProductStockInput(productStockInput)
        viewModel.isProductStockInputError.value?.run {
            if (this && !requestedFocus) {
                productStockField?.requestFocus()
                requestedFocus = true
            }
        }

        // product minimum order validation
        val orderQuantityInput = productMinOrderField?.getEditableValue().toString()
        viewModel.validateProductMinOrderInput(productStockInput, orderQuantityInput)
        viewModel.isOrderQuantityInputError.value?.run {
            if (this && !requestedFocus) {
                productMinOrderField?.requestFocus()
                requestedFocus = true
            }
        }

        // pre order duration validation
        val preOrderDurationInput = preOrderDurationField?.getEditableValue().toString()
        viewModel.validatePreOrderDurationInput(selectedDurationPosition, preOrderDurationInput)
        viewModel.isPreOrderDurationInputError.value?.run {
            if (this && !requestedFocus) {
                preOrderDurationField?.requestFocus()
                requestedFocus = true
            }
        }

        // product sku validation
        val productSkuInput = productSkuField?.getEditableValue().toString()
        viewModel.validateProductSkuInput(productSkuInput)
        viewModel.isProductSkuInputError.value?.run {
            if (this && !requestedFocus) {
                productSkuField?.requestFocus()
                requestedFocus = true
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val result = ImagePickerResultExtractor.extract(data)
                    val newUpdatedPhotos = viewModel.updateProductPhotos(result.imageUrlOrPathList,
                            result.originalImageUrl, result.isEditted)
                    productPictureList = newUpdatedPhotos.pictureList
                    productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)
                    productPhotoAdapter?.let {
                        viewModel.validateProductPhotoInput(it.itemCount)
                    }
                }
                REQUEST_CODE_IMAGE_IMPROVEMENT -> {
                    updateImageListFromIntentData(data)
                }
                REQUEST_CODE_CATEGORY -> {
                    hasCategoryFromPicker = true
                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_FULL_NAME)

                    productCategoryId = categoryId.toString()
                    productCategoryName = categoryName ?: ""

                    val categoryRecommendationResult = viewModel.productCategoryRecommendationLiveData.value
                    val categoryList = if (categoryRecommendationResult != null && categoryRecommendationResult is Success) {
                        productCategoryRecListView?.getSelected(categoryRecommendationResult.data)
                    } else {
                        null
                    }
                    if (categoryList != null) {
                        categoryList.getShownRadioButton()?.isChecked = false
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickSaveOtherCategory(shopId)
                        }
                    }
                    productCategoryLayout?.show()
                    productCategoryRecListView?.setToDisplayText(categoryName.orEmpty(), requireContext())

                    // clear specification, get new annotation spec
                    getAnnotationCategory()

                    // only need set category, no need to get category list
                    needToSetCategoryName = true

                    // in edit case, product change occurred therefore new price suggestion is needed
                    getAddProductPriceSuggestion(keyword = productNameField.getText(), categoryL3 = productCategoryId)

                    // display commission rate based on category
                    if (!viewModel.isFreeOfServiceFee) { viewModel.getCommissionInfo(categoryId.toInt()) }
                }
                SHOWCASE_PICKER_RESULT_REQUEST_CODE -> {
                    val selectedShowcaseList: ArrayList<ShowcaseItemPicker> = data.getParcelableArrayListExtra(EXTRA_PICKER_SELECTED_SHOWCASE)
                            ?: ArrayList()
                    // update the view model state
                    viewModel.updateProductShowCases(selectedShowcaseList)
                    if (selectedShowcaseList.isNotEmpty()) {
                        // display the show case names with comma separator
                        displayProductShowCaseNames(selectedShowcaseList.map { it.showcaseName })
                    } else displayProductShowCaseTips()
                }
                REQUEST_CODE_SPECIFICATION -> {
                    val cacheManagerId = data.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID).orEmpty()
                    val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

                    saveInstanceCacheManager.get(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL,
                            ProductInputModel::class.java, viewModel.productInputModel)?.apply {
                        viewModel.updateSelectedSpecification(detailInputModel.specifications.orEmpty())
                    }
                }
                REQUEST_CODE_VARIANT_DETAIL_DIALOG_EDIT -> {
                    val cacheManagerId = data.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID).orEmpty()
                    val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

                    viewModel.productInputModel = saveInstanceCacheManager.get(
                        EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: return

                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
        //countTouchPhoto is used for count how many times images hit
        // we use this because startDrag(viewHolder) can hit tracker two times
        // we use this because startDrag(viewHolder) can hit tracker two times
        countTouchPhoto += 1
        //countTouchPhoto can increment 1 every time we come or back to this page
        //if we back from ActivityOnResult countTouchPhoto still increment,
        //to avoid that we have to make sure the value of countTouchPhoto must be 1
        if (countTouchPhoto > 2) {
            countTouchPhoto = 1
        }
        // tracker only hit when there are two images of product
        if (productPhotoAdapter?.itemCount ?: 0 > 1) {
            // to avoid double hit tracker when dragging or touching image product, we have to put if here
            if (countTouchPhoto == 2) {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.trackDragPhoto(shopId)
                } else {
                    ProductAddMainTracking.trackDragPhoto(shopId)
                }
            }
        }
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        // validate when 1 photo item is removed
        val photoCount = productPhotoAdapter?.itemCount ?: 0
        viewModel.validateProductPhotoInput(photoCount - 1)

        // tracking
        if (viewModel.isEditing) {
            ProductEditMainTracking.trackRemovePhoto(shopId)
        } else {
            ProductAddMainTracking.trackRemovePhoto(shopId)
        }
    }

    override fun onNameItemClicked(productName: String) {
        var newProductName = productName
        val maxLengthKeyword = context?.resources?.getInteger(R.integer.max_product_name_length).orZero()

        if (productName.trim().length > maxLengthKeyword) {
            newProductName = productName.take(maxLengthKeyword)
        }

        productNameRecView?.hide()
        productNameField.updateText(newProductName)
        getCategoryRecommendation(newProductName) // get recommendation

        if (viewModel.isAdding) {
            ProductAddMainTracking.clickProductNameRecom(shopId, productName)
        }
    }

    override fun onWholeSaleQuantityItemTextChanged(position: Int, input: String) {
        if (productWholeSaleInputFormsView?.layoutManager?.itemCount == wholeSaleInputFormsAdapter?.itemCount) {
            val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
            val quantityField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_quantity)
            val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
            val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position) ?: ""
            val errorMessage = viewModel.validateProductWholeSaleQuantityInput(input, minOrderInput, previousQuantity)
            // to avoid enable button submit when we edit the last of whole sale
            if (position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
                viewModel.isTheLastOfWholeSale.value = errorMessage.isNotEmpty()
            }
            quantityField?.setError(errorMessage.isNotEmpty())
            quantityField?.setMessage(errorMessage)
            updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
            wholeSaleInputFormsAdapter?.run {
                if (input.isNotBlank()) this.updateWholeSaleQuantityInputModel(position, input)
            }
        }
    }

    override fun onWholeSalePriceItemTextChanged(position: Int, input: String) {
        if (productWholeSaleInputFormsView?.layoutManager?.itemCount == wholeSaleInputFormsAdapter?.itemCount) {
            val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
            val priceField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_price)
            val productPriceInput = productPriceField?.editText?.editableText.toString().replace(".", "")
            val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position)?.replace(".", "")
                    ?: ""
            val errorMessage = viewModel.validateProductWholeSalePriceInput(input, productPriceInput, previousPrice)
            // to avoid enable button submit when we edit the last of whole sale
            if (position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
                viewModel.isTheLastOfWholeSale.value = errorMessage.isNotEmpty()
            }
            priceField?.setError(errorMessage.isNotEmpty())
            priceField?.setMessage(errorMessage)
            updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
            wholeSaleInputFormsAdapter?.run {
                if (input.isNotBlank()) this.updateWholeSalePriceInputModel(position, input)
            }
        }
    }

    private fun onFragmentResult() {
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.observe(viewLifecycleOwner, { bundle ->
            setNavigationResult(bundle, REQUEST_KEY_ADD_MODE)
            removeNavigationResult(REQUEST_KEY_ADD_MODE)
            findNavController().navigateUp()
        })
    }

    private fun sendDataBack() {
        if (viewModel.isAdding && !viewModel.isDrafting) {
            var dataBackPressed = NO_DATA
            if (viewModel.isFirstMoved) {
                inputAllDataInProductInputModel()
                dataBackPressed = DETAIL_DATA
                viewModel.productInputModel.requestCode = arrayOf(DETAIL_DATA, NO_DATA, NO_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_DETAIL)
        }
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendDataBack()

                if (viewModel.isEditing) {
                    ProductEditMainTracking.trackBack(shopId)
                } else {
                    ProductAddMainTracking.trackBack(shopId)
                }
            }
        })
    }

    private fun inputAllDataInProductInputModel() {
        viewModel.productInputModel.isDataChanged = true
        viewModel.productInputModel.detailInputModel.apply {
            productName = productNameField.getText()
            price = productPriceField.getTextBigIntegerOrZero()
            stock = productStockField.getTextIntOrZero()
            condition = if (isProductConditionNew) CONDITION_NEW else CONDITION_USED
            minOrder = productMinOrderField.getTextIntOrZero()
            sku = productSkuField.getText()
            imageUrlOrPathList = viewModel.productPhotoPaths
            if (!productPictureList.isNullOrEmpty()) pictureList = productPictureList ?: listOf()
            if (productCategoryId.isNotBlank()) categoryId = productCategoryId
            if (productCategoryName.isNotBlank()) categoryName = productCategoryName
            preorder.apply {
                duration = preOrderDurationField.getTextIntOrZero()
                timeUnit = selectedDurationPosition
                isActive = preOrderSwitch?.isChecked ?: false
            }
            wholesaleList = getWholesaleInput()
            productShowCases = viewModel.productShowCases
            specifications = viewModel.selectedSpecificationList.value
        }
    }

    private fun validateWholeSaleInput(viewModel: AddEditProductDetailViewModel, wholesaleInputForms: RecyclerView?, itemCount: Int?, specialIndex: Int = -1, isAddingWholeSale: Boolean = false) {
        itemCount?.let {
            var wholeSaleErrorCounter = 0
            for (index in 0 until it) {
                // to avoid counting error of whole sale that we removed
                if (specialIndex == index) {
                    continue
                }
                val productWholeSaleFormView = wholesaleInputForms?.layoutManager?.getChildAt(index)
                // Minimum amount
                val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                productWholeSaleQuantityField?.textFieldInput?.editableText?.run {
                    val fieldText = toString().filterDigit()
                    val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
                    val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(index)
                            ?: ""
                    val errorMessage = viewModel.validateProductWholeSaleQuantityInput(fieldText, minOrderInput, previousQuantity)
                    productWholeSaleQuantityField.setError(errorMessage.isNotEmpty())
                    productWholeSaleQuantityField.setMessage(errorMessage)
                }
                val isQuantityError = productWholeSaleQuantityField?.isTextFieldError
                isQuantityError?.let { isError -> if (isError) wholeSaleErrorCounter++ }

                // Product price
                val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)
                productWholeSalePriceField?.textFieldInput?.editableText?.run {
                    val wholeSalePriceInput = this.toString().filterDigit()
                    val productPriceInput = productPriceField?.editText?.editableText.toString().filterDigit()
                    val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(index)?.filterDigit()
                            ?: ""
                    val errorMessage = viewModel.validateProductWholeSalePriceInput(wholeSalePriceInput, productPriceInput, previousPrice)
                    productWholeSalePriceField.setError(errorMessage.isNotEmpty())
                    productWholeSalePriceField.setMessage(errorMessage)
                }
                val isPriceError = productWholeSalePriceField?.isTextFieldError
                isPriceError?.let { isError -> if (isError) wholeSaleErrorCounter++ }
            }
            viewModel.wholeSaleErrorCounter.value = wholeSaleErrorCounter
            viewModel.isAddingWholeSale = isAddingWholeSale
            viewModel.isAddingValidationWholeSale = wholeSaleErrorCounter > 0
        }
    }

    private fun updateWholeSaleErrorCounter(viewModel: AddEditProductDetailViewModel, wholesaleInputForms: RecyclerView?) {
        wholesaleInputForms?.childCount?.let {
            var wholeSaleErrorCounter = 0
            for (index in 0 until it) {
                val productWholeSaleFormView = wholesaleInputForms.layoutManager?.getChildAt(index)
                val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                val isQuantityError = productWholeSaleQuantityField?.isTextFieldError
                isQuantityError?.let { isError -> if (isError) wholeSaleErrorCounter++ }
                val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)
                val isPriceError = productWholeSalePriceField?.isTextFieldError
                isPriceError?.let { isError -> if (isError) wholeSaleErrorCounter++ }
            }
            viewModel.wholeSaleErrorCounter.value = wholeSaleErrorCounter
        }
    }

    private fun getWholesaleInput(): MutableList<WholeSaleInputModel> {
        val inputResult: ArrayList<WholeSaleInputModel> = ArrayList()
        productWholeSaleSwitch?.isChecked?.run {
            if (this) {
                productWholeSaleInputFormsView?.childCount?.let {
                    for (index in 0 until it) {
                        val productWholeSaleFormView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(index)
                        val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)
                        val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                        val item = WholeSaleInputModel(
                                productWholeSalePriceField.getTextIntOrZero().toString(),
                                productWholeSaleQuantityField.getTextIntOrZero().toString()
                        )
                        inputResult.add(item)
                    }
                }
            }
        }
        return inputResult
    }

    @Suppress("RedundantIf", "LiftReturnOrAssignment")
    private fun fillProductDetailForm(detailInputModel: DetailInputModel) {

        // product photo
        productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)

        // product name
        productNameField.setText(detailInputModel.productName)

        // product price
        val productPrice = detailInputModel.price
        if (productPrice != Int.ZERO.toBigInteger()) {
            productPriceField?.editText?.setText(InputPriceUtil.formatProductPriceInput(detailInputModel.price.toString()))
        }
        if (viewModel.hasVariants) {
            productPriceVariantTicker?.isVisible = true
            productPriceLayout?.isVisible = false
            productStockField?.isVisible = false
            productSkuField?.isVisible = false
            tvProductStockHeader?.text = getString(R.string.add_product_order_header)
            productPriceVariantTicker?.setHtmlDescription(
                if (GlobalConfig.isSellerApp()) {
                    getString(R.string.text_has_variant_price_ticker)
                } else {
                    getString(R.string.text_has_variant_price_ticker_mainapp)
                }
            )
        }

        // product category
        if (detailInputModel.categoryName.isNotBlank()) {
            productCategoryLayout?.show()
            productCategoryRecListView?.setToDisplayText(detailInputModel.categoryName, requireContext())
            productCategoryId = detailInputModel.categoryId
        }

        // product wholesale
        val wholesalePriceExist = detailInputModel.wholesaleList.isNotEmpty()
        if (wholesalePriceExist) {
            productWholeSaleSwitch?.isChecked = true
            wholeSaleInputFormsAdapter?.setWholeSaleInputModels(detailInputModel.wholesaleList)
            viewModel.isWholeSalePriceActivated.value = true
            updateAddNewWholeSalePriceButtonVisibility()
        }

        // product pre order
        val isPreOrder = detailInputModel.preorder.isActive
        if (isPreOrder) {
            preOrderSwitch?.isChecked = true
            preOrderDurationUnitField?.textFieldInput?.setText(getString(getDurationUnit(detailInputModel.preorder.timeUnit)))
            preOrderDurationField?.textFieldInput?.setText(detailInputModel.preorder.duration.toString())
            viewModel.isPreOrderActivated.value = true
        }

        // product stock
        productStockField?.textFieldInput?.setText(detailInputModel.stock.toString())
        if (viewModel.hasVariants) productStockField?.textFieldInput?.isEnabled = false

        // product min order
        productMinOrderField?.textFieldInput?.setText(detailInputModel.minOrder.toString())

        // product condition
        productConditionListView?.onLoadFinish {

            if (detailInputModel.condition == CONDITION_NEW) {
                isProductConditionNew = true
                productConditionListView?.setSelected(productConditions, NEW_PRODUCT_INDEX) {}
            } else {
                isProductConditionNew = false
                productConditionListView?.setSelected(productConditions, USED_PRODUCT_INDEX) {}
            }

            // list item click listener
            productConditionListView?.run {
                this.setOnItemClickListener { _, _, position, _ ->
                    setSelected(productConditions, position) {
                        if (position == NEW_PRODUCT_INDEX) isProductConditionNew = true
                        else isProductConditionNew = false
                    }
                    productPriceSuggestionLayout?.isVisible = isPriceSuggestionVisible()
                }
            }

            productConditions.forEachIndexed { index, listItemUnify ->
                listItemUnify.setTextColorToUnify(requireContext())
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    productConditionListView?.setSelected(productConditions, index) {
                        if (index == NEW_PRODUCT_INDEX) isProductConditionNew = true
                        else isProductConditionNew = false
                    }
                    productPriceSuggestionLayout?.isVisible = isPriceSuggestionVisible()
                }
            }
        }

        // product sku
        productSkuField?.textFieldInput?.setText(detailInputModel.sku)

        // product showcases
        viewModel.updateProductShowCases(ArrayList(detailInputModel.productShowCases))
        val productShowCases = viewModel.productShowCases.map { it.showcaseName }
        if (!productShowCases.contains("")) displayProductShowCaseNames(productShowCases)
    }

    private fun subscribeToProductNameInputStatus() {
        viewModel.isProductNameInputError.observe(viewLifecycleOwner, {
            val productNameInput = productNameField?.getEditableValue().toString()
            val validationResult = viewModel.productNameValidationResult
            productNameField?.isInputError = it
            productNameField?.setMessage(viewModel.productNameMessage)
            typoCorrection?.hide()
            productNameRecAdapter?.setProductNameRecommendations(emptyList())
            productNameRecView?.isVisible = !it

            // if product name input has no issue
            if (!it) {
                // show product name recommendations
                productNameRecAdapter?.setProductNameInput(productNameInput)
                viewModel.getProductNameRecommendation(query = productNameInput)

                // show category recommendations to the product that has no variants and no category name before
                getCategoryRecommendation(productNameInput)

                // update product name field icon warning or success
                when {
                    validationResult.isNegativeKeyword -> {
                        showProductNameIconNegative()
                    }
                    validationResult.isTypoDetected -> {
                        showProductNameIconTypo()
                        typoCorrection?.setKeywords(validationResult.typoCorrections)
                    }
                    else -> { showProductNameIconSuccess() }
                }
            } else {
                // keep the category
                if (viewModel.isAdding && !viewModel.hasVariants) {
                    productCategoryRecListView?.setData(ArrayList(emptyList()))
                }

                // update icon product name field error
                hideProductNameLoadingIndicator()
                showProductNameIconError()
            }

            if (needToSetCategoryName) {
                productCategoryLayout?.show()
                if (productCategoryName.isBlank()) {
                    productCategoryName = viewModel.productInputModel.detailInputModel.categoryName
                }
                productCategoryRecListView?.setToDisplayText(productCategoryName, requireContext())
            }
        })
    }

    private fun subscribeToProductPriceInputStatus() {
        viewModel.isProductPriceInputError.observe(viewLifecycleOwner, {
            if (priceSuggestionBottomSheet?.isAdded == true) {
                priceSuggestionBottomSheet?.setPriceValidationResult(
                        isError = it,
                        message = viewModel.productPriceMessage
                )
            } else {
                productPriceField?.isInputError = it
                productPriceField?.setHtmlMessage(viewModel.productPriceMessage)
            }
        })
    }

    private fun subscribeToWholeSaleSwitchStatus() {
        viewModel.isWholeSalePriceActivated.observe(viewLifecycleOwner, {
            if (it) productWholeSaleInputLayout?.visible()
            else productWholeSaleInputLayout?.hide()
        })
    }

    private fun subscribeToProductStockInputStatus() {
        viewModel.isProductStockInputError.observe(viewLifecycleOwner, {
            productStockField?.setError(it)
            productStockField?.setMessage(viewModel.productStockMessage)
        })
    }

    private fun subscribeToOrderQuantityInputStatus() {
        viewModel.isOrderQuantityInputError.observe(viewLifecycleOwner, {
            productMinOrderField?.setError(it)
            productMinOrderField?.setMessage(viewModel.orderQuantityMessage)
        })
    }

    private fun subscribeToPreOrderSwitchStatus() {
        viewModel.isPreOrderActivated.observe(viewLifecycleOwner, {
            isPreOrderFirstTime = false
            if (it) preOrderInputLayout?.visible()
            else preOrderInputLayout?.hide()
        })
    }

    private fun subscribeToPreOrderDurationInputStatus() {
        viewModel.isPreOrderDurationInputError.observe(viewLifecycleOwner, {
            preOrderDurationField?.setError(it)
            preOrderDurationField?.setMessage(viewModel.preOrderDurationMessage)
        })
    }

    private fun subscribeToProductSkuInputStatus() {
        viewModel.isProductSkuInputError.observe(viewLifecycleOwner, {
            productSkuField?.setError(it)
            productSkuField?.setMessage(viewModel.productSkuMessage)
        })
    }

    private fun subscribeToShopShowCases() {
        viewModel.shopShowCases.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    // shop show cases; the source of truth
                    val shopShowCases = result.data
                    // show case ids from getProductV3 response
                    val selectedShowCaseIds = viewModel.productShowCases.map { it.showcaseId }
                    val selectedShowCases = shopShowCases.filter { showCase ->
                        selectedShowCaseIds.contains(showCase.id)
                    }
                    // convert ShowCaseItems to ShowCaseItemPicker collections
                    val selectedProductShowCases = selectedShowCases.map { showCaseItem ->
                        ShowcaseItemPicker(showcaseId = showCaseItem.id, showcaseName = showCaseItem.name)
                    }
                    // update the show case item picker collections in view model
                    viewModel.updateProductShowCases(ArrayList(selectedProductShowCases))
                    if (selectedProductShowCases.isNotEmpty()) {
                        // display the show case names with comma separator
                        displayProductShowCaseNames(selectedProductShowCases.map { it.showcaseName })
                    } else displayProductShowCaseTips()
                    // hide the reload layout only when the view is visible
                    productShowCasesReloadLayout?.run {
                        if (this.isVisible) {
                            productShowCasesReloadLayout?.hide()
                            productShowCasesView?.show()
                            // update the reloading state in view model
                            viewModel.isReloadingShowCase = false
                        }
                    }

                }
                is Fail -> {
                    // hide the tips and show the reload button
                    productShowCasesView?.hide()
                    productShowCasesReloadLayout?.show()
                    // update the reloading state in view model
                    viewModel.isReloadingShowCase = false
                }
            }
        })
    }

    private fun subscribeToShopInfo() {
        viewModel.shopInfo.observe(viewLifecycleOwner, { shopInfoResponse ->
            val shopInfo = shopInfoResponse.shopInfoById.result.firstOrNull()
            shopInfo?.run {
                val totalTxSuccess = shopInfo.shopStats.totalTxSuccess.toIntOrZero()
                viewModel.shopTier = shopInfo.goldOSData.shopTier
                viewModel.isFreeOfServiceFee = viewModel.isFreeOfServiceFee(totalTxSuccess, viewModel.shopTier)
                if (viewModel.isFreeOfServiceFee) {
                    setupCommissionInfoTips(commissionInfoTipsView, viewModel.isFreeOfServiceFee)
                    if (viewModel.shopTier == ShopStatusLevelDef.LEVEL_OFFICIAL_STORE) {
                        commissionInfoTipsView?.hide()
                    } else { commissionInfoTipsView?.show() }
                } else {
                    // display commission info tips when drafting or editing
                    val categoryIdStr = viewModel.productInputModel.detailInputModel.categoryId
                    if (categoryIdStr.isNotBlank()) {
                        val categoryId = categoryIdStr.toIntOrNull()
                        categoryId?.run { viewModel.getCommissionInfo(categoryId) }
                    }
                }
            }
        })
        viewModel.shopInfoError.observe(viewLifecycleOwner, {
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
    }

    private fun subscribeToCommissionInfo() {
        viewModel.commissionInfo.observe(viewLifecycleOwner, { commissionInfo ->
            val categoryRate = commissionInfo.getDefaultCommissionRules.categoryRate.firstOrNull()
            categoryRate?.run {
                if (commissionRules.isNotEmpty()) {
                    // select commission rate based on shop type
                    val commissionRate = viewModel.getCommissionRate(commissionRules, viewModel.shopTier)
                    // setup commission rate tips if rate is not zero
                    if (commissionRate != DOUBLE_ZERO) {
                        val strCommissionRate = commissionRate.toString()
                        setupCommissionInfoTips(commissionInfoTipsView, viewModel.isFreeOfServiceFee, strCommissionRate)
                        commissionInfoTipsView?.show()
                    }
                }
            }
        })
        viewModel.commissionInfoError.observe(viewLifecycleOwner, {
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
    }

    private fun subscribeToAnnotationCategoryData() {
        viewModel.annotationCategoryData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    productSpecificationLayout?.isVisible = result.data.isNotEmpty()
                    productSpecificationTextView?.show()
                    addProductSpecificationButton?.show()
                    productSpecificationReloadLayout?.hide()
                    viewModel.updateSpecificationByAnnotationCategory(result.data)
                    viewModel.updateHasRequiredSpecification(result.data)
                }
                is Fail -> {
                    productSpecificationLayout?.show()
                    productSpecificationTextView?.hide()
                    addProductSpecificationButton?.hide()
                    productSpecificationReloadLayout?.show()
                }
            }
        })
    }

    private fun subscribeToSpecificationText() {
        viewModel.specificationText.observe(viewLifecycleOwner, {
            productSpecificationTextView?.text = it
        })
    }

    private fun subscribeToHasRequiredSpecification() {
        viewModel.hasRequiredSpecification.observe(viewLifecycleOwner, {
            productSpecificationHeaderTextView.displayRequiredAsterisk(it)
            val specificationList = viewModel.selectedSpecificationList.value.orEmpty()
            tooltipSpecificationRequired?.isVisible = it && specificationList.isEmpty()
        })
    }

    private fun subscribeToSelectedSpecificationList() {
        viewModel.selectedSpecificationList.observe(viewLifecycleOwner) {
            addProductSpecificationButton?.text = if (it.isEmpty()) {
                getString(R.string.action_specification_add)
            } else {
                getString(R.string.action_specification_change)
            }
            val hasRequiredSpecification = viewModel.hasRequiredSpecification.value.orFalse()
            tooltipSpecificationRequired?.isVisible = it.isEmpty() && hasRequiredSpecification
        }
    }

    private fun subscribeToInputStatus() {
        viewModel.isInputValid.observe(viewLifecycleOwner, {
            submitButton?.isEnabled = it
        })
    }

    private fun subscribeToCategoryRecommendation() {
        viewModel.productCategoryRecommendationLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onGetCategoryRecommendationSuccess(it)
                is Fail -> {
                    onGetCategoryRecommendationFailed()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun subscribeToAddProductPriceSuggestion() {
        viewModel.addProductPriceSuggestion.observe(viewLifecycleOwner) { priceSuggestion ->
            viewModel.isPriceSuggestionRangeEmpty = viewModel.isPriceSuggestionRangeIsEmpty(
                    minLimit = priceSuggestion?.summary?.suggestedPriceMin.orZero(),
                    maxLimit = priceSuggestion?.summary?.suggestedPriceMax.orZero()
            )
            productPriceSuggestionLayout?.isVisible = isPriceSuggestionVisible()
            if (productPriceSuggestionLayout?.isVisible == true) {
                val productId = viewModel.productInputModel.productId
                ProductEditMainTracking.sendImpressionPriceSuggestionEntryPointEvent(viewModel.isEditing, productId.toString())
            }
            val minLimit = priceSuggestion?.summary?.suggestedPriceMin?.getCurrencyFormatted()
            val maxLimit = priceSuggestion?.summary?.suggestedPriceMax?.getCurrencyFormatted()
            val priceSuggestionText = getString(R.string.price_suggestion_range, minLimit, maxLimit)
            priceSuggestionRangeView?.text = priceSuggestionText

            // display price suggestion feedback on drafting
            val productPriceInput = productPriceField?.editText?.editableText.toString().filterDigit()
            if (productPriceInput.isNotBlank()) {
                val priceSuggestionRange = viewModel.getProductPriceSuggestionRange(viewModel.isEditing)
                val isCompetitive = viewModel.isProductPriceCompetitive(productPriceInput.toDoubleOrZero(), priceSuggestionRange)
                if (isCompetitive) {
                    priceSuggestionStatusView?.setImageUrl(ROUND_GREEN_CHECK_MARK_ICON)
                    priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_is_competitive)
                } else {
                    priceSuggestionStatusView?.setImageUrl(LIGHT_BULB_ICON)
                    priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_price_suggestion_range)
                }
            }
        }
        viewModel.addProductPriceSuggestionError.observe(viewLifecycleOwner) {
            productPriceSuggestionLayout?.isVisible = false
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        }
    }

    private fun subscribeToEditProductPriceSuggestion() {
        viewModel.productPriceRecommendation.observe(viewLifecycleOwner) { priceSuggestion ->
            viewModel.isPriceSuggestionRangeEmpty = viewModel.isPriceSuggestionRangeIsEmpty(
                    minLimit = priceSuggestion.suggestedPriceMin,
                    maxLimit = priceSuggestion.suggestedPriceMax
            )
            productPriceSuggestionLayout?.isVisible = isPriceSuggestionVisible()
            if (productPriceSuggestionLayout?.isVisible == true) {
                val productId = viewModel.productInputModel.productId
                ProductEditMainTracking.sendImpressionPriceSuggestionEntryPointEvent(viewModel.isEditing, productId.toString())
            }
            val minLimit = priceSuggestion.suggestedPriceMin.getCurrencyFormatted()
            val maxLimit = priceSuggestion.suggestedPriceMax.getCurrencyFormatted()
            val priceSuggestionText = getString(R.string.price_suggestion_range, minLimit, maxLimit)
            priceSuggestionRangeView?.text = priceSuggestionText

            // display price suggestion feedback
            val productPriceInput = productPriceField?.editText?.editableText.toString().filterDigit()
            val priceSuggestionRange = viewModel.getProductPriceSuggestionRange(viewModel.isEditing)
            val isCompetitive = viewModel.isProductPriceCompetitive(productPriceInput.toDoubleOrZero(), priceSuggestionRange)
            if (isCompetitive) {
                priceSuggestionStatusView?.setImageUrl(ROUND_GREEN_CHECK_MARK_ICON)
                priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_is_competitive)
            } else {
                priceSuggestionStatusView?.setImageUrl(LIGHT_BULB_ICON)
                priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_price_suggestion_range)
            }
        }
        viewModel.productPriceRecommendationError.observe(viewLifecycleOwner) {
            productPriceSuggestionLayout?.isVisible = false
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        }
    }

    private fun subscribeToProductNameValidationFromNetwork() {
        viewModel.productNameValidationFromNetwork.observe(viewLifecycleOwner, {
            submitButton?.isLoading = false
            when(it) {
                is Success -> {
                    val isError = it.data.isNotBlank()
                    if (isError) {
                        productNameField?.requestFocus()
                        viewModel.productNameMessage = it.data
                        viewModel.setIsProductNameInputError(true)
                    } else {
                        // set live data to null so it cannot commit observing twice when back from previous page
                        viewModel.setProductNameInputFromNetwork(null)
                        viewModel.setIsProductNameInputError(false)
                        submitInputData()
                    }
                }
                is Fail -> {
                    viewModel.productNameMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    viewModel.setIsProductNameInputError(true)
                }
            }
        })
    }

    private fun subscribeToMaxStockThreshold() {
        viewModel.maxStockThreshold.observe(viewLifecycleOwner) {
            val productStockInput = productStockField?.getEditableValue().toString()
            viewModel.validateProductStockInput(productStockInput)
            viewModel.isProductStockInputError.value?.let { isError ->
                if (isError) {
                    productStockField?.requestFocus()
                }
            }
        }
    }

    private fun validateSpecificationList() {
        if (viewModel.validateSelectedSpecificationList()) {
            viewModel.validateProductNameInputFromNetwork(productNameField.getText())
        } else {
            submitButton?.isLoading = false
            view?.post {
                scrollViewParent?.smoothScrollTo(Int.ZERO, productSpecificationLayout?.top.orZero())
                productSpecificationTextView?.text = MethodChecker.fromHtml(
                    getString(R.string.error_specification_signal_status_empty_red))
            }
        }
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {

            val adapter = productPhotoAdapter ?: return@OnClickListener

            // show error message when maximum product image is reached
            val productPhotoCount = adapter.getProductPhotoPaths().size
            if (productPhotoCount == viewModel.getMaxProductPhotos()) showMaxProductImageErrorToast(getString(R.string.error_max_product_photo))
            else {
                val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                    val pictureList = viewModel.productInputModel.detailInputModel.pictureList
                    if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) pictureList.find {
                        it.urlThumbnail == urlOrPath
                    }?.urlOriginal ?: urlOrPath
                    else urlOrPath
                }.orEmpty()
                openImagePickerAddPhoto(imageUrlOrPathList)
            }
        }
    }

    private fun openImagePickerAddPhoto(imageUrlOrPathList: List<String>) {
        val ctx = context ?: return
        val isEditing = viewModel.isEditing
        val isAdding = viewModel.isAdding || !isEditing
        val maxProductPhotoCount = viewModel.getMaxProductPhotos()

        if(Rollence.getImagePickerRollence()) {
            val pageSource = if(!isEditing) PageSource.AddProduct else PageSource.EditProduct
            doTracking(isEditing)
            val intent = ImagePickerAddEditNavigation.getIntentMultiplePicker(
                ctx,
                maxProductPhotoCount,
                pageSource,
                ArrayList(imageUrlOrPathList)
            )
            startActivityForResult(intent, REQUEST_CODE_IMAGE_IMPROVEMENT)
        } else {
            // tracking
            if (isEditing) {
                ProductEditMainTracking.trackAddPhoto(shopId)
            } else {
                ProductAddMainTracking.trackAddPhoto(shopId)
            }

            val intent = ImagePickerAddEditNavigation.getIntent(
                ctx,
                ArrayList(imageUrlOrPathList),
                maxProductPhotoCount,
                isAdding
            )
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun doTracking(isEdit : Boolean){
        val userId = UserSession(context).userId
        val shopId = UserSession(context).shopId
        MediaImprovementTracker.sendProductActionTracker(isEdit, userId, shopId)
    }

    private fun setupProductSubmitButtonViews() {
        submitButton = binding?.btnSubmit
        setupButton()
        // Continue to add product description
        submitButton?.setOnClickListener {
            submitButton?.isLoading = true
            validateInput()
            validateSpecificationList()
        }
    }

    private fun setupServiceFeeBottomSheet() {
        serviceFeeBottomSheet = ServiceFeeBottomSheet.createInstance()
    }

    private fun setupProductShowcaseViews() {
        productShowCasesView = binding?.addEditProductShowcaseLayout?.tvProductShowcases
        addProductShowCaseButton = binding?.addEditProductShowcaseLayout?.tvAddProductShowcases
        productShowCasesReloadLayout = binding?.addEditProductShowcaseLayout?.reloadProductShowcaseLayout
        productShowCasesReloadButton = binding?.addEditProductShowcaseLayout?.tvReloadShowcasesButton

        addProductShowCaseButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER, ShowcasePickerType.CHECKBOX)
            bundle.putParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER, ArrayList(viewModel.productShowCases))
            val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(EXTRA_BUNDLE, bundle)
            startActivityForResult(intent, SHOWCASE_PICKER_RESULT_REQUEST_CODE)
        }

        // product showcase refresh button
        productShowCasesReloadButton?.setOnClickListener {
            // prevent rapid reloading with isReloadingShowCase flag in view model
            if (!viewModel.isReloadingShowCase) {
                viewModel.getShopShowCasesUseCase()
                viewModel.isReloadingShowCase = true
            }
        }
    }

    private fun setupProductSkuViews() {
        productSkuField = binding?.addEditProductSkuLayout?.tfuSku
        // product minimum order text change listener
        productSkuField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            viewModel.validateProductSkuInput(text.toString())
        }
    }

    private fun setupProductConditionViews() {
        productConditionListView = binding?.addEditProductConditionLayout?.lvuProductConditions

        // new condition
        val newCondition = ListItemUnify(getString(R.string.label_new), "")
        newCondition.isBold = false
        newCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        newCondition.run { productConditions.add(NEW_PRODUCT_INDEX, this) }

        // secondhand condition
        val secondHandCondition = ListItemUnify(getString(R.string.label_secondhand), "")
        secondHandCondition.isBold = false
        secondHandCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, getString(R.string.label_secondhand))
        secondHandCondition.run { productConditions.add(USED_PRODUCT_INDEX, this) }

        // add new and secondhand condition to the view
        productConditionListView?.setData(productConditions)
    }

    private fun setupPreOrderViews() {
        preOrderSwitch = binding?.addEditProductPreorderLayout?.switchPreorder
        preOrderInputLayout = binding?.addEditProductPreorderLayout?.preorderInputLayout
        preOrderDurationField = binding?.addEditProductPreorderLayout?.tfuDuration
        preOrderDurationUnitField = binding?.addEditProductPreorderLayout?.tfuDurationUnit

        // set input type no suggestion to prevent red underline on text
        preOrderDurationUnitField?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        // pre order checked change listener
        preOrderSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // tracker
                if (!isPreOrderFirstTime) {
                    if (viewModel.isEditing) {
                        ProductEditMainTracking.clickPreorderButton(shopId)
                    } else {
                        ProductAddMainTracking.clickPreorderButton(shopId)
                    }
                }

                preOrderInputLayout?.visibility = View.VISIBLE
            } else {
                preOrderInputLayout?.visibility = View.GONE
            }

            viewModel.isPreOrderActivated.value = isChecked
        }

        preOrderDurationUnitField?.apply {
            textFieldInput.setText(getDurationUnit(UNIT_DAY))
            textFieldInput.isFocusable = false
            textFieldInput.isActivated = false
            textFieldInput.setOnClickListener {
                showDurationUnitOption()
            }
        }

        // product pre order duration text change listener
        preOrderDurationField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            viewModel.isPreOrderActivated.value?.let {
                if (it) {
                    val preOrderDurationInput = text?.toString()
                    preOrderDurationInput?.let { duration ->
                        viewModel.validatePreOrderDurationInput(selectedDurationPosition, duration)
                    }
                }
            }
        }
    }

    private fun setupStockViews() {
        productStockField = binding?.addEditProductStockLayout?.tfuAvailableStock
        productMinOrderField = binding?.addEditProductStockLayout?.tfuMinimumOrder
        tvProductStockHeader = binding?.addEditProductStockLayout?.tvProductStockHeader

        productStockField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            val productStockInput = text?.toString()
            productStockInput?.let { viewModel.validateProductStockInput(it) }
            val orderQuantityInput = productMinOrderField?.textFieldInput?.editableText.toString()
            orderQuantityInput.let {
                productStockInput?.let { stockInput ->
                    viewModel.validateProductMinOrderInput(stockInput, it)
                }
            }
        }

        // product minimum order text change listener
        productMinOrderField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            val productStockInput = productStockField?.textFieldInput?.editableText.toString()
            val orderQuantityInput = text?.toString()
            orderQuantityInput?.let { viewModel.validateProductMinOrderInput(productStockInput, it) }
            productStockInput.let { viewModel.validateProductStockInput(it) }
        }

        // max stock as threshold when seller inserts stock
        viewModel.getMaxStockThreshold(userSession.shopId)
    }

    private fun setupWholesaleViews() {
        productWholeSaleSwitch = binding?.addEditProductWholesaleLayout?.suWholesale
        productWholeSaleInputLayout = binding?.addEditProductWholesaleLayout?.wholesaleInputLayout
        productWholeSaleInputFormsView = binding?.addEditProductWholesaleLayout?.rvWholesaleInputForms
        addNewWholeSalePriceButton = binding?.addEditProductWholesaleLayout?.tvAddNewWholesalePrice
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter(this, this,
            onDeleteWholesale = {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.clickRemoveWholesale(shopId)
                } else {
                    ProductAddMainTracking.clickRemoveWholesale(shopId)
                }
                addNewWholeSalePriceButton?.visibility = View.VISIBLE
                val deletePosition = wholeSaleInputFormsAdapter?.getDeletePosition()
                wholeSaleInputFormsAdapter?.itemCount?.let {
                    if (it == 1) {
                        productWholeSaleSwitch?.isChecked = false
                    }
                    validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, it - 1, deletePosition
                        ?: -1)
                    // to avoid enable button submit when we delete the last of whole sale
                    if (deletePosition == it - 1) {
                        viewModel.isTheLastOfWholeSale.value = false
                    }
                }
            })

        productWholeSaleInputFormsView?.apply {
            adapter = wholeSaleInputFormsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        addNewWholeSalePriceButton?.setOnClickListener {
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickAddWholesale(shopId)
            } else {
                ProductAddMainTracking.clickAddWholesale(shopId)
            }

            val productPriceInput = productPriceField?.editText?.editableText.toString().filterDigit()
            wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
            wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
            validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount, isAddingWholeSale = true)
            updateAddNewWholeSalePriceButtonVisibility()
        }
        // product whole sale checked change listener
        productWholeSaleSwitch?.setOnClickListener {
            val isChecked = productWholeSaleSwitch?.isChecked.orFalse()
            viewModel.isWholeSalePriceActivated.value = isChecked

            if (isChecked) {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.clickWholesale(shopId)
                } else {
                    ProductAddMainTracking.clickWholesale(shopId)
                }

                if (viewModel.hasVariants) {
                    showVariantWholesalePriceDialog()
                } else {
                    enableWholesale()
                }
            } else {
                viewModel.isAddingValidationWholeSale = false
                viewModel.isAddingWholeSale = false
                viewModel.isTheLastOfWholeSale.value = false
                viewModel.wholeSaleErrorCounter.value = 0
            }
        }
    }

    private fun setupProductPriceViews() {
        productPriceLayout = binding?.addEditProductPriceLayout?.root
        productPriceField = binding?.addEditProductPriceLayout?.tfuProductPrice
        productPriceVariantTicker = binding?.addEditProductPriceVariantTicker
        productNameField.afterTextChanged { editable ->
            // make sure when user is typing the field, the behaviour to get categories is not blocked by this variable
            if (needToSetCategoryName && editable.isNotBlank()) {
                needToSetCategoryName = false
            }
            // no name => no category => no suggestion
            if (editable.isBlank()) productPriceSuggestionLayout?.hide()
            else productPriceSuggestionLayout?.isVisible = isPriceSuggestionVisible()
            viewModel.setProductNameInput(editable)
            showProductNameLoadingIndicator()
        }

        // Set max length to 9 digits price
        productPriceField?.let {
            it.editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_LENGTH_PRICE))
        }

        productPriceField?.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // clean any kind of number formatting here
                val productPriceInput = charSequence?.toString()?.filterDigit()
                productPriceInput?.let {
                    // do the validation first
                    viewModel.validateProductPriceInput(it)
                    productPriceField?.editText?.let { editText ->
                        InputPriceUtil.applyPriceFormatToInputField(editText, it, start,
                            charSequence.length, count, this)
                    }
                    // product wholesale input validation
                    viewModel.isWholeSalePriceActivated.value?.run {
                        if (this) validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
                    }
                    // shimmering only for direct editing
                    if (!viewModel.isSavingPriceAdjustment) {
                        priceSuggestionLayout?.hide()
                        priceSuggestionShimmering?.show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            priceSuggestionLayout?.show()
                            priceSuggestionShimmering?.hide()
                        }, AddEditProductConstants.DELAY_MILLIS)
                    }
                    // display price suggestion feedback
                    val priceSuggestionRange = viewModel.getProductPriceSuggestionRange(viewModel.isEditing)
                    val isError = viewModel.isProductPriceInputError.value.orFalse()
                    val isCompetitive = viewModel.isProductPriceCompetitive(it.toDoubleOrZero(), priceSuggestionRange, isError)
                    if (isCompetitive) {
                        priceSuggestionStatusView?.setImageUrl(ROUND_GREEN_CHECK_MARK_ICON)
                        priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_is_competitive)
                    } else {
                        priceSuggestionStatusView?.setImageUrl(LIGHT_BULB_ICON)
                        priceSuggestionLabelView?.setText(com.tokopedia.product.addedit.R.string.label_price_suggestion_range)
                    }
                }
            }
        })

        productPriceField?.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollViewParent?.smoothScrollTo(Int.ZERO, productPriceLayout?.top.orZero() - TOP_SCROLL_MARGIN)
        }

        if (GlobalConfig.isSellerApp()) {
            productPriceVariantTicker?.setDescriptionClick {
                showVariantDetailActivity()
            }
        }
    }

    private fun setupPriceSuggestionViews() {
        // store view references
        productPriceSuggestionLayout = binding?.addEditPriceSuggestionLayout?.root
        priceSuggestionLayout = binding?.addEditPriceSuggestionLayout?.priceSuggestionLayout
        priceSuggestionShimmering = binding?.addEditPriceSuggestionLayout?.priceSuggestionShimmering
        priceSuggestionStatusView = binding?.addEditPriceSuggestionLayout?.iuPriceSuggestionStatus
        priceSuggestionLabelView = binding?.addEditPriceSuggestionLayout?.tpgPriceSuggestionLabel
        priceSuggestionRangeView = binding?.addEditPriceSuggestionLayout?.tpgPriceSuggestionRange
        priceSuggestionCtaView = binding?.addEditPriceSuggestionLayout?.tpgCtaCheckDetail
        // set default values
        priceSuggestionStatusView?.setImageUrl(LIGHT_BULB_ICON)
        // setup lihat detail cta
        priceSuggestionCtaView?.setOnClickListener {
            val productId = viewModel.productInputModel.productId.toString()
            val priceInput = productPriceField?.editText?.text?.toString().orEmpty()
            val priceSuggestion: PriceSuggestion = if (viewModel.isEditing) {
                val editPriceSuggestion = viewModel.productPriceRecommendation.value?: PriceSuggestionSuggestedPriceGet()
                viewModel.mapEditPriceSuggestionToPriceSuggestionUiModel(editPriceSuggestion)
            } else {
                val addPriceSuggestion = viewModel.addProductPriceSuggestion.value?: PriceSuggestionByKeyword()
                viewModel.mapAddPriceSuggestionToPriceSuggestionUiModel(addPriceSuggestion)
            }
            priceSuggestionBottomSheet = PriceSuggestionBottomSheet.createInstance(
                    viewModel.isEditing,
                    productId,
                    priceInput,
                    priceSuggestion
            )
            priceSuggestionBottomSheet?.setShowListener {
                (priceSuggestionBottomSheet?.dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
                priceSuggestionBottomSheet?.bottomSheetWrapper?.parent?.parent?.requestLayout()
            }
            priceSuggestionBottomSheet?.setCloseClickListener {
                ProductEditMainTracking.sendClickPriceSuggestionPopUpCloseEvent(viewModel.isEditing)
                priceSuggestionBottomSheet?.dismiss()
            }
            priceSuggestionBottomSheet?.setClickListener(this)
            priceSuggestionBottomSheet?.show(childFragmentManager)
            ProductEditMainTracking.sendClickPriceSuggestionEntryPointEvent(viewModel.isEditing, productId)
        }
        // setup pelajari selengkapnya etc bottomsheet
        val productId = viewModel.productInputModel.productId.toString()
        priceSuggestionInfoBottomSheet = PriceSuggestionInfoBottomSheet.createInstance(viewModel.isEditing, productId)
        priceSuggestionInfoBottomSheet?.isKeyboardOverlap = true
        priceSuggestionInfoBottomSheet?.setCloseClickListener {
            priceSuggestionInfoBottomSheet?.dismiss()
            priceSuggestionBottomSheet?.show(childFragmentManager)
        }
    }

    private fun showVariantDetailActivity() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
            }
            val intent = AddEditProductVariantDetailActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DETAIL_DIALOG_EDIT)
        }
    }

    private fun setupSpecificationViews() {
        productSpecificationLayout = binding?.addEditProductSpecificationLayout?.root
        productSpecificationTextView = binding?.addEditProductSpecificationLayout?.tvProductSpecification
        productSpecificationHeaderTextView = binding?.addEditProductSpecificationLayout?.tvProductSpecificationHeader
        addProductSpecificationButton = binding?.addEditProductSpecificationLayout?.tvAddProductSpecification
        productSpecificationReloadLayout = binding?.addEditProductSpecificationLayout?.reloadProductSpecificationLayout
        productSpecificationReloadButton = binding?.addEditProductSpecificationLayout?.tvReloadSpecificationButton
        tooltipSpecificationRequired = binding?.addEditProductSpecificationLayout?.tooltipSpecificationRequired
    }

    private fun setupProductCategoryViews() {
        productCategoryLayout = binding?.addEditProductCategoryLayout?.root
        productCategoryRecListView = binding?.addEditProductCategoryLayout?.lvuProductCategoryRec
        productCategoryPickerButton = binding?.addEditProductCategoryLayout?.tvCategoryPickerButton
        additionalInfoView = binding?.addEditProductCategoryLayout?.tpgAdditionalInfo
        commissionInfoTipsView = binding?.addEditProductCategoryLayout?.tuCommissionInfoTips
        context?.let {
            categoryAlertDialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            categoryAlertDialog?.setTitle(getString(R.string.title_category_dialog))
            categoryAlertDialog?.setDefaultMaxWidth()
            categoryAlertDialog?.setDescription(getString(R.string.immutable_category_message))
            categoryAlertDialog?.setPrimaryCTAText(getString(R.string.action_close_category_dialog))
            categoryAlertDialog?.setPrimaryCTAClickListener {
                // tutup button clicked
                ProductEditMainTracking.clickSaveOtherCategory(shopId)
                categoryAlertDialog?.dismiss()
            }
        }
        productCategoryPickerButton?.setOnClickListener {
            // is editing
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickOtherCategory(shopId)
            }
            // is adding
            else {
                ProductAddMainTracking.clickOtherCategory(shopId)
            }

            if (viewModel.hasVariants) {
                showImmutableCategoryDialog()
            } else {
                if (viewModel.selectedSpecificationList.value.orEmpty().isNotEmpty()) {
                    showChangeCategoryDialog {
                        startCategoryActivity(REQUEST_CODE_CATEGORY)
                    }
                } else {
                    startCategoryActivity(REQUEST_CODE_CATEGORY)
                }
            }
        }
    }

    private fun setupCommissionInfoTips(
        commissionInfoTipsView: TipsUnify?,
        isFreeOfServiceFee: Boolean,
        commissionRate: String = String.EMPTY
    ) {
        commissionInfoTipsView?.run {
            context?.run {
                val description = if (isFreeOfServiceFee) { getString(R.string.label_no_service_fee) }
                else { getString(R.string.text_service_fee_per_sold_product, commissionRate) }
                val htmlDescription = HtmlLinkHelper(this, description)
                htmlDescription.spannedString?.let { commissionInfoTipsView.description = it }
                commissionInfoTipsView.descriptionView.movementMethod = LinkMovementMethod.getInstance()
                htmlDescription.urlList.first().onClick = {
                    serviceFeeBottomSheet?.show(childFragmentManager)
                }
            }
        }
    }

    private fun setupProductNameViews() {
        productNameField = binding?.addEditProductNameLayout?.tfuProductName
        productNameRecView = binding?.addEditProductNameLayout?.rvProductNameRec
        productNameRecShimmering = binding?.addEditProductNameLayout?.productNameRecShimmering
        typoCorrection = binding?.addEditProductNameLayout?.typoCorrection
        productNameRecAdapter = NameRecommendationAdapter(this)
        productNameRecView?.let {
            it.adapter = productNameRecAdapter
            it.layoutManager = LinearLayoutManager(context)
        }
        productNameField?.editText?.apply {
            setHorizontallyScrolling(false)
            isSingleLine = false
            imeOptions = EditorInfo.IME_ACTION_DONE
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
        productNameField?.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) productNameRecView?.hide()
        }
        setupProductNameValidationBottomsheet()
    }

    private fun setupProductPhotoViews() {
        addProductPhotoButton = binding?.addEditProductPhotoLayout?.tvAddProductPhoto
        productPhotosView = binding?.addEditProductPhotoLayout?.rvProductPhotos
        productPhotoAdapter = ProductPhotoAdapter(viewModel.getMaxProductPhotos(), true, viewModel.productPhotoPaths, this)
        productPhotosView?.let {
            it.adapter = productPhotoAdapter
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val photoItemTouchHelperCallback = PhotoItemTouchHelperCallback(it)
            photoItemTouchHelper = ItemTouchHelper(photoItemTouchHelperCallback)
            photoItemTouchHelper?.attachToRecyclerView(it)
        }
        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())
    }

    private fun setupDefaultFieldMessage() {
        // Setup default message for stock if shop admin or owner
        viewModel.setupMultiLocationShopValues()
        productPriceField?.setHtmlMessage(viewModel.productPriceMessage)
        productStockField?.setMessage(viewModel.productStockMessage)
    }

    private fun setupSpecificationField() {
        // get annotation category, if not already obtained from the server (specifications == null)
        val specifications = viewModel.productInputModel.detailInputModel.specifications
        if (specifications != null) {
            viewModel.updateSelectedSpecification(specifications)
        } else {
            getAnnotationCategory()
        }

        addProductSpecificationButton?.setOnClickListener {
            showSpecificationPicker()
        }

        productSpecificationReloadButton?.setOnClickListener {
            getAnnotationCategory()
        }
    }

    private fun initProductShowcaseValue() {
        // execute getShopShowCasesUseCase when the showcases contains nameless show case
        val productShowCases = viewModel.productShowCases.map { it.showcaseName }
        if (productShowCases.contains("")) viewModel.getShopShowCasesUseCase()
    }

    private fun initPriceSuggestion(isEdit: Boolean) {
        if (isEdit) viewModel.getProductPriceRecommendation()
        else {
            val categoryId = viewModel.productInputModel.detailInputModel.categoryId
            val productName = viewModel.productInputModel.detailInputModel.productName
            val isCategoryIdEmpty = categoryId.isBlank()
            val isProductNameEmpty = productName.isBlank()
            if (!isCategoryIdEmpty && !isProductNameEmpty) {
                viewModel.getAddProductPriceSuggestion(
                        keyword = productName,
                        categoryL3 = categoryId
                )
            }
        }
    }

    private fun initCommissionInfo(shopId: String) {
        viewModel.getShopInfo(shopId.toIntOrZero())
    }

    private fun getCategoryRecommendation(productNameInput: String) {
        if (viewModel.isAdding && !viewModel.hasVariants && !needToSetCategoryName) {
            viewModel.getCategoryRecommendation(productNameInput)
        }
    }

    private fun getAnnotationCategory() {
        val productId = if (viewModel.productInputModel.productId > 0) {
            viewModel.productInputModel.productId.toString()
        } else {
            ""
        }

        productSpecificationLayout?.gone()
        if (productCategoryId.isNotEmpty()) {
            viewModel.getAnnotationCategory(productCategoryId, productId)
        }
    }

    private fun showSpecificationPicker(){
        context?.run {
            val productInputModel = viewModel.productInputModel
            productInputModel.detailInputModel.apply {
                if (productCategoryId.isNotBlank()) categoryId = productCategoryId
                if (productCategoryName.isNotBlank()) categoryName = productCategoryName
                specifications = viewModel.selectedSpecificationList.value
            }

            val cacheManager = SaveInstanceCacheManager(this, true)
            cacheManager.put(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val intent = AddEditProductSpecificationActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_SPECIFICATION)
        }
    }

    private fun showMaxProductImageErrorToast(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, type = Toaster.TYPE_ERROR).show()
        }
    }

    private fun showSuccessRemoveBlacklistedWordsToast() {
        view?.let {
            val errorMessage = getString(R.string.label_success_remove_blacklisted_words)
            Toaster.build(it, errorMessage, type = Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showVariantWholesalePriceDialog() {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.message_variant_price_wholesale_title))
            setDescription(getString(R.string.message_variant_price_wholesale))
            setPrimaryCTAText(getString(R.string.action_variant_price_wholesale_negative))
            setPrimaryCTAClickListener {
                dialog.dismiss()
                disableWholesale()
            }
            setSecondaryCTAText(getString(R.string.action_variant_price_wholesale_positive))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                enableWholesale()
            }
        }
        dialog.show()
    }

    private fun showChangeCategoryDialog(onAccepted: () -> Unit) {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.message_change_category_title))
            setDescription(getString(R.string.message_change_category))
            setSecondaryCTAText(getString(R.string.action_change_category_positive))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                onAccepted()
            }
            setPrimaryCTAText(getString(R.string.action_change_category_negative))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun setupProductNameValidationBottomsheet() {
        productNameField?.icon2?.setOnClickListener {
            val titleValidationModel = viewModel.productNameValidationResult
            val bottomSheet = TitleValidationBottomSheet(titleValidationModel)

            bottomSheet.setOnDeleteActionClicked {
                val cleanedProductName = viewModel.removeKeywords(productNameField.getText(), it)
                productNameField.setText(cleanedProductName)
                productNameField?.editText?.setSelection(cleanedProductName.length)
                showSuccessRemoveBlacklistedWordsToast()
            }

            bottomSheet.show(childFragmentManager)
        }
    }

    private fun showProductPriceRecommendationTips() {
        val tooltipBottomSheet = TooltipBottomSheet()
        val tips: ArrayList<NumericWithDescriptionTooltipModel> = ArrayList()
        val tooltipTitle = getString(R.string.title_price_recommendation_bottom_sheet)
        val contentTitles = context?.resources?.getStringArray(R.array.array_price_recommendation_content_titles).orEmpty()
        val contentDescriptions = context?.resources?.getStringArray(R.array.array_price_recommendation_content_descriptions).orEmpty()

        contentTitles.forEachIndexed { index, title ->
            val description = contentDescriptions.getOrNull(index).orEmpty()
            tips.add(NumericWithDescriptionTooltipModel(title, description))
        }

        tooltipBottomSheet.apply {
            setTitle(tooltipTitle)
            setItemMenuList(tips)
            setDividerVisible(false)
            setBannerImage(PRICE_RECOMMENDATION_BANNER_URL)
        }
        tooltipBottomSheet.show(childFragmentManager, null)
    }

    private fun enableWholesale() {
        val productPriceInput = productPriceField?.editText?.editableText
                .toString().replace(".", "")
        wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
        val wholesalePriceExist = wholeSaleInputFormsAdapter?.itemCount != 0
        if (!wholesalePriceExist) wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
        validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
    }

    private fun disableWholesale() {
        productWholeSaleSwitch?.isChecked = false
    }

    private fun showDurationUnitOption() {
        val optionPicker = OptionPicker()
        optionPicker.setCloseClickListener {
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickCancelPreOrderDuration(shopId)
            } else {
                ProductAddMainTracking.clickCancelPreOrderDuration(shopId)
            }
            optionPicker.dismiss()
        }
        val title = getString(R.string.label_duration)
        val options: ArrayList<String> = ArrayList()
        options.add(getString(getDurationUnit(UNIT_DAY)))
        options.add(getString(getDurationUnit(UNIT_WEEK)))

        optionPicker.apply {
            setSelectedPosition(selectedDurationPosition)
            setDividerVisible(true)
            setTitle(title)
            setItemMenuList(options)
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickPreorderDropDownMenu(shopId)
            } else {
                ProductAddMainTracking.clickPreorderDropDownMenu(shopId)
            }
        }
        optionPicker.show(childFragmentManager, null)

        optionPicker.setOnItemClickListener { selectedText: String, selectedPosition: Int ->
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickPreOrderDuration(shopId, selectedPosition == 0)
            } else {
                ProductAddMainTracking.clickPreOrderDuration(shopId, selectedPosition == 0)
            }
            preOrderDurationUnitField?.textFieldInput?.setText(selectedText)
            selectedDurationPosition = selectedPosition
            val preOrderDurationInput = preOrderDurationField?.textFieldInput?.editableText.toString()
            viewModel.validatePreOrderDurationInput(selectedDurationPosition, preOrderDurationInput)
        }
    }

    private fun moveToDescriptionActivity() {
        if (viewModel.isAdding) {
            ProductAddMainTracking.clickContinue(shopId)
        }
        inputAllDataInProductInputModel()
        arguments?.let {
            val cacheManagerId = AddEditProductDetailFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
                put(EXTRA_IS_EDITING_PRODUCT, viewModel.isEditing)
                put(EXTRA_IS_ADDING_PRODUCT, viewModel.isAdding)
                put(EXTRA_IS_DRAFTING_PRODUCT, viewModel.isDrafting)
                put(EXTRA_IS_FIRST_MOVED, viewModel.isFirstMoved)
            }
            val destination = AddEditProductDetailFragmentDirections.actionAddEditProductDetailFragmentToAddEditProductDescriptionFragment()
            destination.cacheManagerId = cacheManagerId
            NavigationController.navigate(this@AddEditProductDetailFragment, destination)
        }
    }

    private fun subscribeToProductNameRecommendation() {
        observe(viewModel.productNameRecommendations) {
            when (it) {
                is Success -> {
                    val inputError = viewModel.isProductNameInputError.value.orFalse()
                    productNameRecView?.isVisible = !inputError
                    productNameRecAdapter?.setProductNameRecommendations(it.data)
                }
                is Fail -> {
                    productNameRecView?.hide()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
            hideProductNameLoadingIndicator()
        }
    }

    private fun showProductNameLoadingIndicator() {
        productNameField?.isLoading = true
        productNameRecShimmering?.visible()
    }

    private fun hideProductNameLoadingIndicator() {
        productNameField?.isLoading = false
        productNameRecShimmering?.hide()
    }

    private fun showProductNameIconSuccess() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_G500, IconUnify.CHECK_CIRCLE)
        productNameField?.isInputError = false
    }

    private fun showProductNameIconTypo() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_N700, IconUnify.INFORMATION)
        productNameField?.isInputError = false
    }

    private fun showProductNameIconNegative() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_Y300, IconUnify.INFORMATION)
        productNameField?.isInputError = false
    }

    private fun showProductNameIconError() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_R500, IconUnify.INFORMATION)
        productNameField?.isInputError = true
    }

    private fun showProductNameIconIndicator(colorResource: Int, iconResource: Int) {
        val color = ContextCompat.getColor(requireContext(), colorResource)
        val iconDrawable = getIconUnifyDrawable(requireContext(), iconResource, color)
        productNameField?.icon2?.loadImage(iconDrawable)
        productNameField?.icon2?.show()
    }

    private fun showImmutableCategoryDialog() {
        categoryAlertDialog?.show()
    }

    private fun displayProductShowCaseNames(productShowCaseNames: List<String>) {
        productShowCasesView?.text = productShowCaseNames.joinToString()
    }

    private fun displayProductShowCaseTips() {
        productShowCasesView?.text = getString(R.string.label_product_showcase_tips)
    }

    private fun onGetCategoryRecommendationSuccess(result: Success<List<ListItemUnify>>) {
        hasCategoryFromPicker = false
        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(AMOUNT_CATEGORY_RECOM_DEFAULT))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            selectFirstCategoryRecommendation(items)

            productCategoryRecListView?.setOnItemClickListener { _, _, position, _ ->
                if (viewModel.isAdding) {
                    ProductAddMainTracking.clickProductCategoryRecom(shopId)
                }

                // display confirmation if product has a specs
                if (viewModel.selectedSpecificationList.value.orEmpty().isEmpty()) {
                    selectCategoryRecommendation(items, position)
                } else {
                    showChangeCategoryDialog {
                        selectCategoryRecommendation(items, position)
                    }
                }
            }

            items.forEachIndexed { position, listItemUnify ->
                listItemUnify.setTextColorToUnify(requireContext())
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    if (viewModel.isAdding) {
                        ProductAddMainTracking.clickProductCategoryRecom(shopId)
                    }
                    selectCategoryRecommendation(items, position)
                }
            }
        }

        // in edit case, product change occurred therefore new price suggestion is needed
        val keyword = productNameField.getText()
        val selectedCategoryId = items.firstOrNull()?.getCategoryId().toString()
        getAddProductPriceSuggestion(keyword = keyword, categoryL3 = selectedCategoryId)

        // display commission rate based on category
        if (!viewModel.isFreeOfServiceFee) {
            val categoryId = productCategoryId.toIntOrNull()
            categoryId?.run { viewModel.getCommissionInfo(categoryId) }
        }
    }

    private fun getAddProductPriceSuggestion(keyword: String, categoryL3: String) {
        viewModel.getAddProductPriceSuggestion(keyword, categoryL3)
    }

    private fun onGetCategoryRecommendationFailed() {
        productCategoryLayout?.show()
        productCategoryRecListView?.hide()
    }

    private fun selectFirstCategoryRecommendation(items: List<ListItemUnify>) {
        productCategoryRecListView?.count?.let { itemSize ->
            if (itemSize > 0) {
                selectCategoryRecommendation(items, FIRST_CATEGORY_SELECTED)
            }
        }
    }

    private fun selectCategoryRecommendation(items: List<ListItemUnify>, position: Int) = productCategoryRecListView?.run {
        if (!hasCategoryFromPicker) {
            setSelected(items, position) {
                val categoryId = it.getCategoryId().toString()
                val categoryName = it.getCategoryName()
                productCategoryId = categoryId
                productCategoryName = categoryName
                getAnnotationCategory() // update annotation specification
                true
            }
        }
    }

    private fun submitInput() {
        inputAllDataInProductInputModel()
        setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE)
    }

    private fun submitInputEdit() {
        inputAllDataInProductInputModel()
        setFragmentResultWithBundle(REQUEST_KEY_DETAIL)
        if (viewModel.isEditing) {
            ProductEditMainTracking.clickContinue(shopId)
        }
    }

    private fun setFragmentResultWithBundle(requestKey: String, dataBackPressed: Int = DETAIL_DATA) {
        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
            val bundle = Bundle().apply {
                putString(BUNDLE_CACHE_MANAGER_ID, cacheManagerId)
                putInt(BUNDLE_BACK_PRESSED, dataBackPressed)
            }
            setNavigationResult(bundle, requestKey)
            findNavController().navigateUp()
        }
    }

    private fun startCategoryActivity(requestCodeCategory: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
        intent.putExtra(AddEditProductConstants.EXTRA_IS_EDIT_MODE, (viewModel.isEditing))
        startActivityForResult(intent, requestCodeCategory)
    }

    private fun enableProductNameField() {
        productNameField?.isEnabled = !viewModel.hasTransaction
    }

    override fun getValidationCurrentWholeSaleQuantity(quantity: String, position: Int): String {
        val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
        val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position - 1) ?: ""
        val validation = viewModel.validateProductWholeSaleQuantityInput(quantity, minOrderInput, previousQuantity)
        viewModel.isTheLastOfWholeSale.value = validation.isNotEmpty()
        return validation
    }

    override fun getValidationCurrentWholeSalePrice(price: String, position: Int): String {
        val productPriceInput = productPriceField?.editText?.editableText.toString().filterDigit()
        val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position - 1)?.filterDigit()
                ?: ""
        val validation = viewModel.validateProductWholeSalePriceInput(price, productPriceInput, previousPrice)
        viewModel.isTheLastOfWholeSale.value = validation.isNotEmpty()
        return validation
    }

    override fun onPriceSuggestionInfoCtaClick() {
        priceSuggestionBottomSheet?.dismiss()
        priceSuggestionInfoBottomSheet?.show(childFragmentManager)
    }

    override fun onPriceTextInputChanged(priceInput: String) {
        viewModel.validateProductPriceInput(priceInput)
    }

    override fun onSaveButtonClick(priceInput: String) {
        viewModel.isSavingPriceAdjustment = true
        productPriceField?.setText(priceInput)
        productPriceField?.isInputError = viewModel.isProductPriceInputError.value ?: false
        productPriceField?.setHtmlMessage(viewModel.productPriceMessage)
        priceSuggestionBottomSheet?.dismiss()
        viewModel.isSavingPriceAdjustment = false
    }

    private fun isPriceSuggestionVisible(): Boolean {
        val productStatus = viewModel.productInputModel.detailInputModel.status
        return viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = viewModel.isPriceSuggestionRangeEmpty,
                productStatus = productStatus,
                isProductConditionNew,
                viewModel.hasVariants
        )
    }

    private fun updateImageListFromIntentData(data: Intent){
        val result = MediaPicker.result(data)
        val newUpdatedPhotos = viewModel.updateProductPhotos(result.editedImages.toMutableList(),
            result.originalPaths.toMutableList())
        productPictureList = newUpdatedPhotos.pictureList
        productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)
        productPhotoAdapter?.let {
            viewModel.validateProductPhotoInput(it.itemCount)
        }
    }
}
