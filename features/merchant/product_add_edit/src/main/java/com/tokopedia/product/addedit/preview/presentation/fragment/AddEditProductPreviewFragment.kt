package com.tokopedia.product.addedit.preview.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_PREVIEW_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_PREVIEW_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_PREVIEW_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_PREVIEW_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_PREVIEW
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_1
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_2
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_3
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.EXTRA_CASHBACK_IS_DRAFTING
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.EXTRA_RESULT_STATUS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_PRODUCT_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SHOP_LOCATION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DESCRIPTION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_SHIPMENT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_REQUEST_CODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_RESULT
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.imagepicker.ImagePickerAddEditNavigation
import com.tokopedia.product.addedit.preview.data.source.api.response.Cashback
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.ADDRESS_STREET
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.COURIER_ORIGIN
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DELAY_CLOSE_ACTIVITY
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DESCRIPTION_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_ADDRESS_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FIRST_MOVED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FULL_FLOW
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_LOGISTIC_LABEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.LATITUDE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.LONGITUDE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.POSTAL_CODE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.PRODUCT_STATUS_ACTIVE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.SHIPMENT_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.SHOP_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TIMBER_PREFIX_LOCATION_VALIDATION
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TIMBER_PREFIX_PRODUCT_NAME_VALIDATION
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.preview.presentation.model.SetCashbackResult
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductAddService
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductEditService
import com.tokopedia.product.addedit.preview.presentation.viewmodel.AddEditProductPreviewViewModel
import com.tokopedia.product.addedit.productlimitation.domain.mapper.ProductLimitationMapper
import com.tokopedia.product.addedit.productlimitation.presentation.dialog.ProductLimitationBottomSheet
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking
import com.tokopedia.product.addedit.tracking.ProductEditStepperTracking
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantActivity
import com.tokopedia.product.addedit.variant.presentation.model.ValidationResultModel
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.shop.common.constant.SellerHomePermissionGroup
import com.tokopedia.shop.common.constant.admin_roles.AdminPermissionUrl
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddEditProductPreviewFragment :
        BaseDaggerFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        AddEditProductPerformanceMonitoringListener {

    private var countTouchPhoto = 0
    private var dataBackPressed: Int? = null
    private var hasLocation: Boolean = false
    private var isStartButtonClicked: Boolean = false
    private var latitude: String = ""
    private var longitude: String = ""
    private var postalCode: String = ""
    private var districtId: Int = 0
    private var formattedAddress: String = ""
    private var productInputModel: ProductInputModel? = null
    private var isFragmentVisible = false
    private var isFragmentFirstTimeLoaded = true
    private var isAdminEligible = true
    private var isProductLimitEligible: Boolean = true

    private var toolbar: Toolbar? = null

    // action button
    private var doneButton: AppCompatTextView? = null

    // photo
    private var addEditProductPhotoButton: Typography? = null
    private var productPhotosView: RecyclerView? = null
    private var productPhotoAdapter: ProductPhotoAdapter? = null
    private var photoItemTouchHelper: ItemTouchHelper? = null
    private var addProductPhotoTipsLayout: ViewGroup? = null

    // detail
    private var addEditProductDetailTitle: Typography? = null
    private var addEditProductDetailButton: Typography? = null
    private var productDetailPreviewLayout: ViewGroup? = null
    private var productNameView: Typography? = null
    private var productPriceView: Typography? = null
    private var productStockView: Typography? = null
    private var dividerDetail: DividerUnify? = null

    // description
    private var addEditProductDescriptionTitle: Typography? = null
    private var addEditProductDescriptionButton: Typography? = null

    //variant
    private var addEditProductVariantLayout: ViewGroup? = null
    private var addEditProductVariantButton: Typography? = null
    private var addProductVariantTipsLayout: ViewGroup? = null
    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

    // shipment
    private var addEditProductShipmentTitle: Typography? = null
    private var addEditProductShipmentButton: Typography? = null

    // promotion
    private var editProductPromotionLayout: ViewGroup? = null
    private var editProductPromotionButton: Typography? = null

    // product status
    private var editProductStatusLayout: ViewGroup? = null
    private var productStatusSwitch: SwitchUnify? = null

    // loading
    private var loadingLayout: MotionLayout? = null

    // admin revamp
    private var multiLocationTicker: Ticker? = null
    private var adminRevampErrorLayout: FrameLayout? = null
    private var adminRevampGlobalError: GlobalError? = null

    // product limitation
    private var productLimitationTicker: Ticker? = null
    private var productLimitationBottomSheet: ProductLimitationBottomSheet? = null

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    @Inject
    lateinit var viewModel: AddEditProductPreviewViewModel

    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // start PLT monitoring
        startPerformanceMonitoring()

        userSession = UserSession(requireContext())
        shopId = userSession.shopId

        super.onCreate(savedInstanceState)

        arguments?.let {
            val previewFragmentArgs = AddEditProductPreviewFragmentArgs.fromBundle(it)
            val draftId = previewFragmentArgs.draftId
            val productId = previewFragmentArgs.productId
            val isDuplicate = previewFragmentArgs.isProductDuplicate

            viewModel.setProductId(productId)
            viewModel.setIsDuplicate(isDuplicate)
            if (draftId.isNotBlank()) {
                viewModel.setDraftId(draftId)
                viewModel.getProductDraft(draftId.toLongOrZero())
            }
            if (viewModel.getProductId().isNotEmpty()) {
                //TODO is goldmerchant and isregular
                ProductEditStepperTracking.trackScreen(shopId, false, false)
            } else {
                ProductAddStepperTracking.trackScreen()
            }
        }
    }

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        activity?.finish()
    }

    private fun saveProductToDraft() {
        viewModel.productInputModel.value?.let {
            viewModel.saveProductDraft(AddEditProductMapper.mapProductInputModelDetailToDraft(it), it.draftId, false)
        }
        Toast.makeText(context, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show()
    }

    private fun isEditing(): Boolean {
        return viewModel.isEditing.value ?: false
    }

    private fun isDrafting(): Boolean {
        return viewModel.getDraftId() > 0L
    }

    private fun isAdding(): Boolean {
        return viewModel.isAdding
    }

    private fun dataBackPressedLoss(): Boolean {
        // when stepper page has no data, dataBackPressed is null but if stepper page has data, dataBackPressed has data too
        // dataBackPressed is a sign of activity where data is obtained
        if (dataBackPressed == null) {
            return true
        }
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        context?.let { activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)) }

        // activity toolbar
        toolbar = activity?.findViewById(R.id.toolbar)
        toolbar?.title = getString(com.tokopedia.product.addedit.R.string.label_title_add_product)

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        // action button
        doneButton = activity?.findViewById(R.id.tv_done)

        // photos
        productPhotosView = view.findViewById(R.id.rv_product_photos)
        productPhotoAdapter = ProductPhotoAdapter(MAX_PRODUCT_PHOTOS, true, mutableListOf(), this)
        productPhotosView?.let {
            it.adapter = productPhotoAdapter
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val photoItemTouchHelperCallback = PhotoItemTouchHelperCallback(it)
            photoItemTouchHelper = ItemTouchHelper(photoItemTouchHelperCallback)
            photoItemTouchHelper?.attachToRecyclerView(it)
        }
        addProductPhotoTipsLayout = view.findViewById(R.id.add_product_photo_tips_layout)
        addEditProductPhotoButton = view.findViewById(R.id.tv_start_add_edit_product_photo)

        // detail
        addEditProductDetailTitle = view.findViewById(R.id.tv_product_detail)
        addEditProductDetailButton = view.findViewById(R.id.tv_start_add_edit_product_detail)
        productDetailPreviewLayout = view.findViewById(R.id.product_detail_preview_layout)
        productNameView = view.findViewById(R.id.tv_product_name)
        productPriceView = view.findViewById(R.id.tv_product_price)
        productStockView = view.findViewById(R.id.tv_product_stock)
        dividerDetail = view.findViewById(R.id.divider_detail)

        // description
        addEditProductDescriptionTitle = view.findViewById(R.id.tv_product_description)
        addEditProductDescriptionButton = view.findViewById(R.id.tv_start_add_edit_product_description)

        // variant
        addEditProductVariantLayout = view.findViewById(R.id.add_product_variant_step_layout)
        addEditProductVariantButton = view.findViewById(R.id.tv_start_add_edit_product_variant)
        addProductVariantTipsLayout = view.findViewById(R.id.add_product_variant_tips_layout)
        sellerFeatureCarousel = view.findViewById(R.id.sellerFeatureCarousel)

        // shipment
        addEditProductShipmentTitle = view.findViewById(R.id.tv_product_shipment)
        addEditProductShipmentButton = view.findViewById(R.id.tv_start_add_edit_product_shipment)

        // promotion
        editProductPromotionLayout = view.findViewById(R.id.edit_product_promotion_step_layout)
        editProductPromotionButton = view.findViewById(R.id.tv_edit_product_promotion)

        // status
        editProductStatusLayout = view.findViewById(R.id.edit_product_status_layout)
        productStatusSwitch = view.findViewById(R.id.su_product_status)

        // loading
        loadingLayout = view.findViewById(R.id.loading_layout)

        // admin revamp
        multiLocationTicker = view.findViewById(R.id.ticker_add_edit_multi_location)
        adminRevampErrorLayout = view.findViewById(R.id.add_edit_error_layout)
        adminRevampGlobalError = view.findViewById(R.id.add_edit_admin_global_error)

        // product limitation
        productLimitationTicker = view.findViewById(R.id.ticker_add_edit_product_limitation)

        addEditProductPhotoButton?.setOnClickListener {
            val ctx = context ?: return@setOnClickListener
            // tracking
            val buttonTextStart: String = getString(R.string.action_start)
            if (isEditing()) {
                ProductEditStepperTracking.trackClickChangeProductPic(shopId)
                moveToImagePicker()
            } else if (addEditProductPhotoButton?.text == buttonTextStart) {
                ProductAddStepperTracking.trackStart(shopId)
                // validate whether shop has location
                isStartButtonClicked = true
                if (hasLocation) {
                    moveToImagePicker()
                } else {
                    validateShopLocation()
                }
            } else {
                moveToImagePicker()
            }
        }

        productStatusSwitch?.setOnClickListener {
            val isChecked = productStatusSwitch?.isChecked ?: false
            viewModel.updateProductStatus(isChecked)
            viewModel.setIsDataChanged(true)
            // track switch status on click
            if (isChecked && isEditing()) {
                ProductEditStepperTracking.trackChangeProductStatus(shopId)
            }
        }

        doneButton?.setOnClickListener {
            updateImageList()
            if (isEditing()) {
                ProductEditStepperTracking.trackFinishButton(shopId)
            }

            val validateMessage = viewModel.validateProductInput(viewModel.productInputModel.value?.detailInputModel
                    ?: DetailInputModel())
            val isAddingOrDuplicating = isAdding() || viewModel.isDuplicate

            if (validateMessage.isNotEmpty()) {
                Toaster.make(view, validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            } else if (isAddingOrDuplicating && !isProductLimitEligible) {
                productLimitationBottomSheet?.setSubmitButtonText(getString(R.string.label_product_limitation_bottomsheet_button_draft))
                productLimitationBottomSheet?.setIsSavingToDraft(true)
                productLimitationBottomSheet?.show(childFragmentManager)
            } else {
                viewModel.productInputModel.value?.detailInputModel?.productName?.let {
                    viewModel.validateProductNameInput(it)
                }
            }
        }

        addProductPhotoTipsLayout?.setOnClickListener {
            if (!isEditing()) {
                ProductAddStepperTracking.trackHelpProductQuality(shopId)
            }
            showPhotoTips()
        }

        addEditProductDetailButton?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackChangeProductDetail(shopId)
            }
            val productInputModel = viewModel.productInputModel.value ?: ProductInputModel()
            // there is 3 case where we come here
            // 1. edit product
            // 2. open draft (edit product or add product mode)
            // 3. add product then press back
            // to prevent edit tracker always fired when we came here from add product mode
            // we need a flag isAdding which will be true only if we come to this fragment from product manage
            moveToDetailFragment(productInputModel, false)
        }

        addEditProductDescriptionButton?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackChangeProductDesc(shopId)
            }
            moveToDescriptionFragment()
        }

        addEditProductVariantButton?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackAddProductVariant(shopId)
            }
            showVariantDialog()
        }

        addProductVariantTipsLayout?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackClickHelpPriceVariant(shopId)
            }
            showVariantTips()
        }

        addEditProductShipmentButton?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackChangeShipping(shopId)
            }
            moveToShipmentFragment()
        }

        editProductPromotionButton?.setOnClickListener {
            if (isEditing()) {
                ProductEditStepperTracking.trackChangePromotion(shopId)
            }
            setCashback()
        }

        if (!GlobalConfig.isSellerApp()) {
            sellerFeatureCarousel?.apply {
                setListener(object : SellerFeatureCarousel.SellerFeatureClickListener {
                    override fun onSellerFeatureClicked(item: SellerFeatureUiModel) {
                        if (!isDrafting()) {
                            when (item) {
                                is SellerFeatureUiModel.AddEditSetVariantFeatureWithDataUiModel -> goToSellerAppEditProduct(viewModel.getProductId())
                                is SellerFeatureUiModel.SetCashbackFeatureWithDataUiModel -> goToSellerAppProductManageThenSetCashback()
                            }
                        }
                    }
                })
                addItemDecoration()
                setItems(listOf(
                        SellerFeatureUiModel.AddEditSetVariantFeatureWithDataUiModel(Any()),
                        SellerFeatureUiModel.SetCashbackFeatureWithDataUiModel(Any())
                ))
            }
        }

        onFragmentResult()
        setupBackPressed()

        // check it has ever backed from detail for the first time
        if (viewModel.productInputModel.value?.requestCode != null) {
            viewModel.productInputModel.value?.let { displayAddModeDetail(it) }
            checkEnableOrNot()
        }

        multiLocationTicker?.showWithCondition(viewModel.shouldShowMultiLocationTicker)

        context?.let { UpdateShopActiveService.startService(it) }

        setupProductLimitationViews()

        //If you add another observe, don't forget to remove observers at removeObservers()
        observeIsEditingStatus()
        observeProductData()
        observeProductInputModel()
        observeProductVariant()
        observeImageUrlOrPathList()
        observeIsLoading()
        observeValidationMessage()
        observeSaveProductDraft()
        observeGetShopInfoLocation()
        observeSaveShipmentLocationData()
        observeAdminPermission()
        observeProductLimitationData()

        // validate whether shop has location
        validateShopLocationWhenPageOpened()
        // stop prepare page PLT monitoring
        stopPreparePagePerformanceMonitoring()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val productInputModelJson = savedInstanceState.getString(KEY_SAVE_INSTANCE_PREVIEW)
            if (!productInputModelJson.isNullOrBlank()) {
                //set product input model
                mapJsonToObject(productInputModelJson, ProductInputModel::class.java).apply {
                    productInputModel = this
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            outState.putString(KEY_SAVE_INSTANCE_PREVIEW, mapObjectToJson(viewModel.productInputModel.value))
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentVisible = false
        isFragmentFirstTimeLoaded = false
        removeObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val result = ImagePickerResultExtractor.extract(data)
                    val imagePickerResult = result.imageUrlOrPathList as ArrayList
                    val originalImageUrl = result.originalImageUrl as ArrayList
                    val isEditted = result.isEditted as ArrayList
                    if (imagePickerResult.size > 0) {
                        val shouldUpdatePhotosInsteadMoveToDetail = isEditing() ||
                                viewModel.isDuplicate ||
                                viewModel.productInputModel.value != null
                        // update the product pictures in the preview page
                        // this should be executed when the user press "Ubah" on stepper in add or edit or duplicate product
                        if (shouldUpdatePhotosInsteadMoveToDetail) {
                            viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, isEditted)
                        } else {
                            // this only executed when we came from empty stepper page (add product)
                            val newProductInputModel = viewModel.getNewProductInputModel(imagePickerResult)
                            moveToDetailFragment(newProductInputModel, true)
                        }
                    }
                    if (isEditted.any { true }) {
                        viewModel.setIsDataChanged(true)
                    }
                }
                REQUEST_CODE_VARIANT_DIALOG_EDIT -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        viewModel.productInputModel.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                        viewModel.productInputModel.value?.let {
                            updateProductStatusSwitch(it)
                        }
                        viewModel.setIsDataChanged(true)
                    }
                }
                SET_CASHBACK_REQUEST_CODE -> {
                    val cacheManagerId = data.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY) ?: ""
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    onSetCashbackResult(cacheManager)
                }
                REQUEST_CODE_SHOP_LOCATION -> {
                    showLoading()
                    data.let { intent ->
                        val saveAddressDataModel = intent.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_MODEL)

                        saveAddressDataModel?.let { model ->
                            latitude = model.latitude
                            longitude = model.longitude
                            postalCode = model.postalCode
                            districtId = model.districtId
                            formattedAddress = model.formattedAddress
                        }

                        saveShippingLocation()
                    }
                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
        //countTouchPhoto is used for count how many times images hit
        //we use this because startDrag(viewHolder) can hit tracker two times
        countTouchPhoto += 1
        //countTouchPhoto can increment 1 every time we come or back to this page
        //if we back from ActivityOnResult countTouchPhoto still increment
        //to avoid that we have to make sure the value of countTouchPhoto must be 1
        if (countTouchPhoto > 2) {
            countTouchPhoto = 1
        }
        // tracker only hit when there are two images of product
        if (productPhotoAdapter?.itemCount ?: 0 > 1) {
            // to avoid double hit tracker when dragging or touching image product, we have to put if here
            if (countTouchPhoto == 2) {
                if (isAdding()) {
                    ProductAddStepperTracking.trackDragPhoto(shopId)
                } else {
                    ProductEditStepperTracking.trackDragPhoto(shopId)
                }
            }
        }
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        viewModel.setIsDataChanged(true)
        if (isAdding()) {
            ProductAddStepperTracking.trackRemoveProductImage(shopId)
        } else {
            ProductEditStepperTracking.trackRemoveProductImage(shopId)
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_PREVIEW_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_PREVIEW_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_PREVIEW_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_PREVIEW_TRACE)
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

    private fun onSuccessSetCashback() {
        view?.let {
            Toaster.make(it, getString(
                    R.string.product_edit_set_cashback_success),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
    }

    private fun onFailedSetCashback() {
        view?.let {
            Toaster.make(it, getString(
                    R.string.product_edit_set_cashback_error),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val baseMainApplication = requireContext().applicationContext as BaseMainApplication
        DaggerAddEditProductPreviewComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(baseMainApplication))
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
                .inject(this)
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // send tracker
                if (isEditing()) {
                    ProductEditStepperTracking.trackBack(shopId)
                } else {
                    ProductAddStepperTracking.trackBack(shopId)
                }

                if (!viewModel.getIsDataChanged()) {
                    // finish activity if there is no data changes
                    activity?.finish()
                } else {
                    // show dialog
                    DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(R.string.label_title_on_dialog))
                        setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog))
                        setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog))
                        if ((isEditing() || dataBackPressedLoss()) && !isDrafting()) {
                            setDescription(getString(R.string.label_description_on_dialog_edit))
                            setSecondaryCTAClickListener {
                                activity?.finish()
                            }
                            setPrimaryCTAClickListener {
                                this.dismiss()
                            }
                        } else {
                            setDescription(getString(R.string.label_description_on_dialog))
                            setSecondaryCTAClickListener {
                                saveProductToDraft()
                                moveToManageProduct()
                                ProductAddStepperTracking.trackDraftYes(shopId)
                            }
                            setPrimaryCTAClickListener {
                                this.dismiss()
                                ProductAddStepperTracking.trackDraftCancel(shopId)
                            }
                        }
                    }.show()
                }
            }
        })
    }

    private fun onFragmentResult() {
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.observe(viewLifecycleOwner, Observer { bundle ->
            bundle?.let { data ->
                dataBackPressed = data.getInt(BUNDLE_BACK_PRESSED, 0)
                updateProductInputModelOfCacheManagerId(data)
                //only update data on preview page
                when (dataBackPressed) {
                    DETAIL_DATA -> {
                        viewModel.productInputModel.value?.let { displayAddModeDetail(it) }
                        viewModel.productInputModel.value?.requestCode?.set(0, DETAIL_DATA)
                        checkEnableOrNot()
                        return@Observer
                    }
                    DESCRIPTION_DATA -> {
                        viewModel.productInputModel.value?.let { displayAddModeDetail(it) }
                        viewModel.productInputModel.value?.requestCode?.set(1, DESCRIPTION_DATA)
                        checkEnableOrNot()
                        return@Observer
                    }
                    SHIPMENT_DATA -> {
                        viewModel.productInputModel.value?.let { displayAddModeDetail(it) }
                        viewModel.productInputModel.value?.requestCode?.set(2, SHIPMENT_DATA)
                        checkEnableOrNot()
                        return@Observer
                    }
                    NO_DATA -> {
                        viewModel.productInputModel.value?.let { displayAddModeDetail(it) }
                        checkEnableOrNot()
                        return@Observer
                    }
                }
                //upload the product to the server
                val productInputModel = viewModel.productInputModel.value ?: ProductInputModel()
                context?.let {
                    val validateMessage = viewModel.validateProductInput(productInputModel.detailInputModel)
                    if (validateMessage.isEmpty()) {
                        startProductAddService(productInputModel)
                        Handler().postDelayed({ activity?.finish() }, DELAY_CLOSE_ACTIVITY)
                    } else {
                        Toaster.build(requireView(), validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                }
            }
            removeNavigationResult(REQUEST_KEY_ADD_MODE)
        })
        getNavigationResult(REQUEST_KEY_DETAIL)?.observe(viewLifecycleOwner, Observer { bundle ->
            bundle?.let {
                updateProductInputModelOfCacheManagerId(it)
            }
            removeNavigationResult(REQUEST_KEY_DETAIL)
        })
        getNavigationResult(REQUEST_KEY_DESCRIPTION)?.observe(viewLifecycleOwner, Observer { bundle ->
            bundle?.let {
                updateProductInputModelOfCacheManagerId(it)
            }
            removeNavigationResult(REQUEST_KEY_DESCRIPTION)
        })
        getNavigationResult(REQUEST_KEY_SHIPMENT)?.observe(viewLifecycleOwner, Observer { bundle ->
            bundle?.let {
                updateProductInputModelOfCacheManagerId(it)
            }
            removeNavigationResult(REQUEST_KEY_SHIPMENT)
        })
    }

    private fun checkEnableOrNot() {
        viewModel.productInputModel.value?.requestCode?.apply {
            val isDetailData = this[0] == DETAIL_DATA
            val isDescriptionData = this[1] == DESCRIPTION_DATA
            val isShipmentData = this[2] == SHIPMENT_DATA
            if (isDetailData && isDescriptionData && isShipmentData) {
                enableDescriptionEdit()
                enableShipmentEdit()
            } else if (isDetailData && isShipmentData) {
                disableDescriptionEdit()
                enableShipmentEdit()
            } else if (isDetailData && isDescriptionData) {
                enableDescriptionEdit()
                disableShipmentEdit()
            } else {
                disableDescriptionEdit()
                disableShipmentEdit()
            }
        }
    }

    private fun updateProductInputModelOfCacheManagerId(bundle: Bundle) {
        val cacheManagerId = bundle.getString(BUNDLE_CACHE_MANAGER_ID) ?: ""
        SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
            viewModel.productInputModel.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    ?: ProductInputModel()
            // set data only in draft or edit mode to make dynamic ui preview
            if (isDrafting() || viewModel.productInputModel.value?.productId != 0L) {
                productInputModel = viewModel.productInputModel.value
            }
        }
    }

    private fun displayAddModeDetail(productInputModel: ProductInputModel) {
        doneButton?.show()
        enablePhotoEdit()
        enableDetailEdit()
        showProductPhotoPreview(productInputModel)
        showProductDetailPreview(productInputModel)
    }

    private fun displayEditMode() {
        toolbar?.title = getString(R.string.label_title_edit_product)
        doneButton?.show()

        enablePhotoEdit()
        enableDetailEdit()
        enableDescriptionEdit()
        enableVariantEdit()
        enableShipmentEdit()
        enablePromotionEdit()
        enableStatusEdit()
    }

    private fun enablePhotoEdit() {
        addEditProductPhotoButton?.text = getString(R.string.action_add_product_photo)
        addProductPhotoTipsLayout?.hide()
        productPhotosView?.show()
    }

    private fun enableDetailEdit() {
        context?.let {
            addEditProductDetailTitle?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            addEditProductDetailButton?.text = getString(R.string.action_change)
            addEditProductDetailButton?.show()
            dividerDetail?.hide()
        }
    }

    private fun enableDescriptionEdit() {
        context?.let {
            addEditProductDescriptionTitle?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            addEditProductDescriptionButton?.text = getString(R.string.action_change)
            addEditProductDescriptionButton?.show()
        }
    }

    private fun enableVariantEdit() {
        addEditProductVariantLayout?.showWithCondition(GlobalConfig.isSellerApp())
        sellerFeatureCarousel?.showWithCondition(!GlobalConfig.isSellerApp())
    }

    private fun enableShipmentEdit() {
        context?.let {
            addEditProductShipmentTitle?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            addEditProductShipmentButton?.text = getString(R.string.action_change)
            addEditProductShipmentButton?.show()
        }
    }

    private fun enablePromotionEdit() {
        editProductPromotionLayout?.showWithCondition(GlobalConfig.isSellerApp())
    }

    private fun enableStatusEdit() {
        editProductStatusLayout?.show()
    }

    private fun disableShipmentEdit() {
        context?.let {
            if (addEditProductShipmentButton?.text != getString(R.string.action_change)) {
                addEditProductShipmentButton?.text = getString(R.string.action_add)
                addEditProductShipmentButton?.show()
            }
        }
    }

    private fun disableDescriptionEdit() {
        context?.let {
            if (addEditProductDescriptionButton?.text != getString(R.string.action_change)) {
                addEditProductDescriptionButton?.text = getString(R.string.action_add)
                addEditProductDescriptionButton?.show()
            }
        }
    }

    private fun observeIsEditingStatus() {
        viewModel.isEditing.observe(viewLifecycleOwner, Observer {
            if (it) {
                displayEditMode()
            } else {
                stopPerformanceMonitoring()
            }
        })
    }

    private fun observeProductData() {
        // start PLT monitoring network
        startNetworkRequestPerformanceMonitoring()
        viewModel.getProductResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val isVariantEmpty = result.data.variant.products.isEmpty()
                    showEmptyVariantState(isVariantEmpty)
                    showProductStatus(result.data)
                    handleSetCashBackResult()

                    // set temporary price when first loaded
                    SharedPreferencesUtil.setPriceWhenLoaded(requireActivity(), result.data.price)

                    // continue to PLT monitoring render
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                }
                is Fail -> {
                    // stop PLT if failed getting result
                    stopPerformanceMonitoring()
                    context?.let {
                        val isEditing = viewModel.isEditing.value ?: false
                        val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                        val errorName = AddEditProductUploadErrorHandler.getErrorName(result.throwable)
                        if (isEditing) {
                            ProductEditStepperTracking.oopsConnectionPageScreen(
                                    userSession.userId,
                                    errorMessage,
                                    errorName)
                        }
                        showGetProductErrorToast(ErrorHandler.getErrorMessage(it, result.throwable))
                        AddEditProductErrorHandler.logExceptionToCrashlytics(result.throwable)
                    }
                }
            }
        })
    }

    private fun handleSetCashBackResult() {
        activity?.intent?.data?.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID)?.let {
            if (it.isNotBlank()) {
                val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, it) }
                val resultStatus = cacheManager?.get(EXTRA_RESULT_STATUS, Int::class.java) ?: 0
                if (resultStatus == RESULT_OK) {
                    onSetCashbackResult(cacheManager)
                }
            }
        }
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner, Observer {
            showProductPhotoPreview(it)
            showProductDetailPreview(it)
            updateProductStatusSwitch(it)
            showEmptyVariantState(viewModel.productInputModel.value?.variantInputModel?.products?.size == 0)
            if (viewModel.getDraftId() != 0L || it.productId != 0L || viewModel.getProductId().isNotBlank()) {
                displayEditMode()
            }
            stopRenderPerformanceMonitoring()
            stopPerformanceMonitoring()
            //check whether productInputModel has value from savedInstanceState
            if (productInputModel != null) {
                viewModel.productInputModel.value = productInputModel
                checkEnableOrNot()
                productInputModel = null
            }
        })
    }

    private fun updateProductStatusSwitch(productInputModel: ProductInputModel) {
        productStatusSwitch?.isChecked = (productInputModel.detailInputModel.status == STATUS_ACTIVE)
    }

    private fun observeProductVariant() {
        viewModel.isVariantEmpty.observe(viewLifecycleOwner, Observer {
            if ((isEditing() || isDrafting()) && it) {
                showEmptyVariantState(viewModel.productInputModel.value?.variantInputModel?.products?.size == 0)
            }
        })
    }

    private fun observeImageUrlOrPathList() {
        viewModel.imageUrlOrPathList.observe(viewLifecycleOwner, Observer {
            productPhotoAdapter?.setProductPhotoPaths(it)
        })
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun observeValidationMessage() {
        viewModel.resetValidateResult() // reset old result when re-observe
        viewModel.validationResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.result) {
                // when we perform add product, the productId will be 0
                // when we perform edit product, the productId will be provided from the getProductV3 response
                // when we perform open draft, previous state before we save the product to draft will be the same
                ValidationResultModel.Result.VALIDATION_SUCCESS -> {
                    if (viewModel.productInputModel.value?.productId.orZero() != 0L) {
                        viewModel.productInputModel.value?.apply {
                            startProductEditService(this)
                        }
                    } else {
                        viewModel.productInputModel.value?.let { productInputModel ->
                            startProductAddService(productInputModel)
                            activity?.setResult(RESULT_OK)
                        }
                    }
                    showLoading()
                    Handler().postDelayed( { activity?.finish() }, DELAY_CLOSE_ACTIVITY)
                }
                ValidationResultModel.Result.VALIDATION_ERROR -> {
                    showToasterFailed(result.exception)
                    // log error
                    AddEditProductErrorHandler.logMessage(TIMBER_PREFIX_PRODUCT_NAME_VALIDATION + " : " + result.serviceResponse)
                    AddEditProductErrorHandler.logExceptionToCrashlytics(result.exception)
                }
                else -> {
                    // no-op
                }
            }
        })
    }

    private fun observeSaveProductDraft() {
        viewModel.saveProductDraftResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> Toast.makeText(context, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show()
                is Fail -> {
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun observeGetShopInfoLocation() {
        viewModel.locationValidation.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (!it.data && dataBackPressedLoss()) {
                        showDialogLocationValidation()
                    }
                    hasLocation = it.data
                    if (isStartButtonClicked && hasLocation) {
                        moveToImagePicker()
                    }
                }
                is Fail -> {
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                    AddEditProductErrorHandler.logMessage("$TIMBER_PREFIX_LOCATION_VALIDATION: ${it.throwable.message}")
                    if (isStartButtonClicked) {
                        showToasterFailed(it.throwable)
                    }
                }
            }
        }
    }

    private fun observeSaveShipmentLocationData() {
        viewModel.saveShopShipmentLocationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val isSuccess = it.data.ongkirOpenShopShipmentLocation.dataSuccessResponse.success
                    if (isSuccess && dataBackPressedLoss()) {
                        showToasterSuccessSetLocation()
                    } else if (dataBackPressedLoss()) {
                        moveToLocationPicker()
                    }
                    hasLocation = isSuccess
                }
                is Fail -> {
                    saveShippingLocation()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                    AddEditProductErrorHandler.logMessage("$TIMBER_PREFIX_LOCATION_VALIDATION: ${it.throwable.message}")
                }
            }
        }
    }

    private fun observeAdminPermission() {
        viewModel.isProductManageAuthorized.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    result.data.let { isEligible ->
                        isAdminEligible = isEligible
                        doneButton?.showWithCondition(isAdminEligible)
                        if (isEligible) {
                            adminRevampErrorLayout?.hide()
                        } else {
                            showAdminNotEligibleView()
                        }
                    }
                }
                is Fail -> {
                    showGetProductErrorToast(viewModel.getProductId())
                }
            }

        }
    }

    private fun observeProductLimitationData() {
        if (!RollenceUtil.getProductLimitationRollence()) return
        if (isAdding() || viewModel.isDuplicate) {
            if (isFragmentFirstTimeLoaded && !isDrafting()) viewModel.getProductLimitation()
            viewModel.productLimitationData.observe(viewLifecycleOwner) {
                when(it) {
                    is Success -> {
                        val productLimitationModel = ProductLimitationMapper.mapToProductLimitationModel(requireContext(), it.data)
                        setupBottomSheetProductLimitation(productLimitationModel)

                        // store to shared preferences, to reuse at another fragment
                        SharedPreferencesUtil.setProductLimitationModel(requireActivity(), productLimitationModel)
                    }
                    is Fail -> {
                        AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                    }
                }
            }
        }
    }

    private fun removeObservers() {
        viewModel.isEditing.removeObservers(this)
        viewModel.getProductResult.removeObservers(this)
        viewModel.productInputModel.removeObservers(this)
        viewModel.isVariantEmpty.removeObservers(this)
        viewModel.imageUrlOrPathList.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        viewModel.saveProductDraftResultLiveData.removeObservers(this)
        viewModel.validationResult.removeObservers(this)
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.removeObservers(this)
        getNavigationResult(REQUEST_KEY_DETAIL)?.removeObservers(this)
        getNavigationResult(REQUEST_KEY_DESCRIPTION)?.removeObservers(this)
        getNavigationResult(REQUEST_KEY_SHIPMENT)?.removeObservers(this)
    }

    private fun setCashback() {
        viewModel.productInputModel.value?.let { productInputModel ->
            val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.SET_CASHBACK, viewModel.getProductId())
            val uri = Uri.parse(newUri)
                    .buildUpon()
                    .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_NAME, productInputModel.detailInputModel.productName)
                    .appendQueryParameter(PARAM_SET_CASHBACK_VALUE, viewModel.productDomain.cashback.percentage.toString())
                    .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE, productInputModel.detailInputModel.price.toString())
                    .build()
                    .toString()
            val intent = RouteManager.getIntent(context, uri)
            intent.putExtra(EXTRA_CASHBACK_SHOP_ID, shopId)
            intent.putExtra(EXTRA_CASHBACK_IS_DRAFTING, isDrafting())
            startActivityForResult(intent, SET_CASHBACK_REQUEST_CODE)
        }
    }

    private fun showProductPhotoPreview(productInputModel: ProductInputModel) {
        var pictureIndex = 0
        val imageUrlOrPathList = productInputModel.detailInputModel.imageUrlOrPathList.map { urlOrPath ->
            if (urlOrPath.startsWith(HTTP_PREFIX)) productInputModel.detailInputModel.pictureList[pictureIndex++].urlThumbnail
            else urlOrPath
        }
        productPhotoAdapter?.setProductPhotoPaths(imageUrlOrPathList.toMutableList())
    }

    private fun showProductDetailPreview(productInputModel: ProductInputModel) {
        val detailInputModel = productInputModel.detailInputModel
        productNameView?.text = detailInputModel.productName
        productPriceView?.text = "Rp " + InputPriceUtil.formatProductPriceInput(detailInputModel.price.toString())
        productStockView?.text = detailInputModel.stock.toString()
        productDetailPreviewLayout?.show()
    }

    private fun showEmptyVariantState(isVariantEmpty: Boolean) {
        if (isVariantEmpty) {
            addEditProductVariantButton?.text = getString(R.string.action_add)
            addProductVariantTipsLayout?.show()
        } else {
            addEditProductVariantButton?.text = getString(R.string.action_change)
            addProductVariantTipsLayout?.hide()
        }
    }

    private fun showProductStatus(productData: Product) {
        val productStatus = productData.status
        if (productStatus == PRODUCT_STATUS_ACTIVE) productStatusSwitch?.isChecked = true
    }

    private fun showPhotoTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<ImageTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_photo_tips)
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_1), PHOTO_TIPS_URL_1))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_2), PHOTO_TIPS_URL_2))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_3), PHOTO_TIPS_URL_3))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                setDividerVisible(true)
                show(it, null)
            }
        }
    }

    private fun showVariantTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<NumericTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_variant_tips)
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_1)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_2)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_3)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_4)))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                setDividerVisible(false)
                show(it, null)
            }
        }
    }

    private fun showGetProductErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        viewModel.getProductData(viewModel.getProductId())
                    })
        }
    }

    private fun showMaxProductImageErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, type = Toaster.TYPE_ERROR)
        }
    }

    private fun moveToImagePicker() {
        val adapter = productPhotoAdapter ?: return
        // show error message when maximum product image is reached
        val productPhotoSize = adapter.getProductPhotoPaths().size
        if (productPhotoSize == MAX_PRODUCT_PHOTOS) showMaxProductImageErrorToast(getString(R.string.error_max_product_photo))
        else {
            val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                if (urlOrPath.startsWith(HTTP_PREFIX)) viewModel.productInputModel.value?.detailInputModel?.pictureList?.find { it.urlThumbnail == urlOrPath }?.urlOriginal
                        ?: urlOrPath
                else urlOrPath
            }.orEmpty()
            val intent = ImagePickerAddEditNavigation.getIntent(requireContext(), ArrayList(imageUrlOrPathList), viewModel.isAdding || !isEditing())
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun moveToDetailFragment(productInputModel: ProductInputModel, isFirstMoved: Boolean) {
        context?.run {
            updateImageList()
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                put(EXTRA_IS_EDITING_PRODUCT, isEditing())
                put(EXTRA_IS_ADDING_PRODUCT, isAdding())
                put(EXTRA_IS_DRAFTING_PRODUCT, isDrafting())
                put(EXTRA_IS_FIRST_MOVED, isFirstMoved)
            }
            val destination = AddEditProductPreviewFragmentDirections.actionAddEditProductPreviewFragmentToAddEditProductDetailFragment()
            destination.cacheManagerId = cacheManager.id ?: "0"
            NavigationController.navigate(this@AddEditProductPreviewFragment, destination)
            doneButton?.hide()
        }
    }

    private fun moveToDescriptionFragment() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
                put(EXTRA_IS_EDITING_PRODUCT, isEditing())
                put(EXTRA_IS_ADDING_PRODUCT, isAdding())
                put(EXTRA_IS_DRAFTING_PRODUCT, isDrafting())
            }
            val destination = AddEditProductPreviewFragmentDirections.actionAddEditProductPreviewFragmentToAddEditProductDescriptionFragment()
            destination.cacheManagerId = cacheManager.id ?: "0"
            NavigationController.navigate(this@AddEditProductPreviewFragment, destination)
            doneButton?.hide()
        }
    }

    private fun moveToShipmentFragment() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
                put(EXTRA_IS_EDITING_PRODUCT, isEditing())
                put(EXTRA_IS_ADDING_PRODUCT, isAdding())
                put(EXTRA_IS_DRAFTING_PRODUCT, isDrafting())
            }
            val destination = AddEditProductPreviewFragmentDirections.actionAddEditProductPreviewFragmentToAddEditProductShipmentFragment()
            destination.cacheManagerId = cacheManager.id ?: "0"
            NavigationController.navigate(this@AddEditProductPreviewFragment, destination)
            doneButton?.hide()
        }
    }

    private fun showVariantDialog() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
            }
            val intent = AddEditProductVariantActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DIALOG_EDIT)
        }
    }

    private fun updateImageList() {
        // fillter product pictureList, so that edited image will be reuploaded and changed (removed from pictureList) and than reorder the picture if necessary
        val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths().orEmpty()
        val pictureList = viewModel.productInputModel.value?.detailInputModel?.pictureList?.filter {
            imageUrlOrPathList.contains(it.urlThumbnail)
        }.orEmpty()
        val newPictureList = mutableListOf<PictureInputModel>()
        imageUrlOrPathList.forEach { urlOrPath ->
            pictureList.find { it.urlThumbnail == urlOrPath }?.run {
                newPictureList.add(this)
            }
        }
        viewModel.productInputModel.value?.detailInputModel?.pictureList = newPictureList
        viewModel.productInputModel.value?.detailInputModel?.imageUrlOrPathList = imageUrlOrPathList
    }

    private fun startProductAddService(productInputModel: ProductInputModel) {
        // increment wholesale min order by one because of > symbol
        productInputModel.run {
            this.detailInputModel.wholesaleList = viewModel.incrementWholeSaleMinOrder(this.detailInputModel.wholesaleList)
        }

        context?.let {
            val saveInstanceCacheManager = SaveInstanceCacheManager(it, true)
            saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
            AddEditProductAddService.startService(
                    context = it,
                    cacheId = saveInstanceCacheManager.id.orEmpty()
            )
        }
    }

    private fun startProductEditService(productInputModel: ProductInputModel) {
        // increment wholesale min order by one because of > symbol
        productInputModel.run {
            this.detailInputModel.wholesaleList = viewModel.incrementWholeSaleMinOrder(this.detailInputModel.wholesaleList)
        }

        context?.let {
            val saveInstanceCacheManager = SaveInstanceCacheManager(it, true)
            saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
            AddEditProductEditService.startService(
                    context = it,
                    cacheManagerId = saveInstanceCacheManager.id.orEmpty()
            )
        }
    }

    private fun showLoading() {
        loadingLayout?.progress = 0.0f
        loadingLayout?.show()
        doneButton?.hide()
    }

    private fun hideLoading() {
        doneButton?.showWithCondition(isAdminEligible)
        loadingLayout?.transitionToEnd()
        loadingLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                // no-op
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                // no-op
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                // no-op
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                loadingLayout?.hide()
                loadingLayout?.progress = 0.0f
            }
        })
    }

    private fun goToSellerAppProductManageThenSetCashback() {
        viewModel.productInputModel.value?.let { productInputModel ->
            val cacheManagerId = UUID.randomUUID().toString()
            val secondApplink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                    .buildUpon()
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, viewModel.getProductId())
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                    .appendQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID, cacheManagerId)
                    .build()
                    .toString()
            val thirdAppLink = Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.SET_CASHBACK, viewModel.getProductId()))
                    .buildUpon()
                    .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_NAME, productInputModel.detailInputModel.productName)
                    .appendQueryParameter(PARAM_SET_CASHBACK_VALUE, viewModel.productDomain.cashback.percentage.toString())
                    .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE, productInputModel.detailInputModel.price.toString())
                    .appendQueryParameter(EXTRA_CASHBACK_SHOP_ID, userSession.shopId)
                    .appendQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID, cacheManagerId)
                    .build()
                    .toString()
            goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_EDIT_PRODUCT_CASHBACK, arrayListOf(ApplinkConst.PRODUCT_MANAGE, secondApplink, thirdAppLink))
        }
    }

    private fun goToSellerAppEditProduct(productId: String) {
        val secondAppLink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                .build()
                .toString()
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_SET_VARIANT, arrayListOf(ApplinkConst.PRODUCT_MANAGE, secondAppLink))
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        context?.run {
            val intent = SellerMigrationActivity.createIntent(this, featureName, screenName, appLinks)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun showDialogLocationValidation() {
        DialogUnify(
                requireContext(),
                DialogUnify.SINGLE_ACTION,
                DialogUnify.NO_IMAGE
        ).apply {
            setTitle(getString(R.string.label_for_dialog_title_that_shop_has_no_location))
            setDescription(getString(R.string.label_for_dialog_desc_that_shop_has_no_location))
            setPrimaryCTAText(getString(R.string.label_for_dialog_primary_cta_that_shop_has_no_location))
            setPrimaryCTAClickListener {
                moveToLocationPicker()
                dismiss()
            }
        }.show()
    }

    private fun moveToLocationPicker() {
        RouteManager.getIntent(activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
            putExtra(EXTRA_IS_FULL_FLOW, false)
            putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
            startActivityForResult(this, REQUEST_CODE_SHOP_LOCATION)
        }
    }

    private fun showToasterSuccessSetLocation() {
        view?.let {
            Toaster.build(
                    it,
                    getString(R.string.label_for_toaster_success_set_shop_location),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.label_for_action_text_toaster_success_set_shop_location)
            ).show()
        }
    }

    private fun showToasterFailed(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.build(
                requireView(),
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR
        ).show()
    }

    private fun getSaveShopShippingLocationData(
            shopId: Int,
            postCode: String,
            courierOrigin: Int,
            addrStreet: String,
            lat: String,
            long: String
    ): MutableMap<String, Any> {
        val shipmentPayload = mutableMapOf<String, Any>()
        shipmentPayload[SHOP_ID] = shopId
        shipmentPayload[POSTAL_CODE] = postCode
        shipmentPayload[COURIER_ORIGIN] = courierOrigin
        shipmentPayload[ADDRESS_STREET] = addrStreet
        shipmentPayload[LATITUDE] = lat
        shipmentPayload[LONGITUDE] = long
        return shipmentPayload
    }

    private fun validateShopLocationWhenPageOpened() {
        if (!isStartButtonClicked && !isDrafting()) {
            validateShopLocation()
        } else {
            // reset the value when on view created
            isStartButtonClicked = false
        }
    }

    private fun validateShopLocation() {
        if (isAdding()) {
            viewModel.validateShopLocation(userSession.shopId.toIntOrZero())
        }
    }

    private fun saveShippingLocation() {
        val shopId = userSession.shopId.toIntOrZero()
        if (shopId != 0 &&
                postalCode.isNotBlank() &&
                latitude.isNotBlank() &&
                longitude.isNotBlank() &&
                districtId != 0 &&
                formattedAddress.isNotBlank()) {

            val params = getSaveShopShippingLocationData(
                    shopId = shopId,
                    postCode = postalCode,
                    courierOrigin = districtId,
                    addrStreet = formattedAddress,
                    lat = latitude,
                    long = longitude
            )
            viewModel.saveShippingLocation(params)
        }
    }

    private fun onSetCashbackResult(cacheManager: SaveInstanceCacheManager?) {
        val setCashbackResult: SetCashbackResult? = cacheManager?.get(SET_CASHBACK_RESULT, SetCashbackResult::class.java)
        if (setCashbackResult == null) {
            onFailedSetCashback()
            return
        }
        setCashbackResult.let { cashbackResult ->
            if (!cashbackResult.limitExceeded) {
                val cashbackProduct = Cashback(cashbackResult.cashback)
                viewModel.productDomain = viewModel.productDomain.copy(cashback = cashbackProduct)
                onSuccessSetCashback()
            } else {
                onFailedSetCashback()
            }
        }
    }

    private fun showAdminNotEligibleView() {
        adminRevampGlobalError?.run {
            val permissionGroup = SellerHomePermissionGroup.PRODUCT
            ImageHandler.loadImageAndCache(errorIllustration, AdminPermissionUrl.ERROR_ILLUSTRATION)
            errorTitle.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_title, permissionGroup)
            errorDescription.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_desc, permissionGroup)
            errorAction.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_action)
            setButtonFull(true)

            setActionClickListener {
                activity?.finish()
                if (GlobalConfig.isSellerApp()) {
                    RouteManager.route(context, ApplinkConstInternalSellerapp.SELLER_HOME)
                }
            }
        }
        adminRevampErrorLayout?.show()
    }

    private fun setupProductLimitationViews() {
        if (!RollenceUtil.getProductLimitationRollence()) return
        val productLimitStartDate = getString(R.string.label_product_limitation_start_date)
        val htmlDescription = getString(R.string.label_product_limitation_ticker, productLimitStartDate)
        productLimitationTicker?.apply {
            setHtmlDescription(htmlDescription)
            showWithCondition((isAdding() && !isDrafting()) || viewModel.isDuplicate)
        }
    }

    private fun setupBottomSheetProductLimitation(productLimitationModel: ProductLimitationModel) {
        if (productLimitationModel.isUnlimited) {
            productLimitationTicker?.gone()
            return
        }

        productLimitationModel.apply {
            productLimitationBottomSheet = ProductLimitationBottomSheet(actionItems, isEligible, limitAmount)
            isProductLimitEligible = isEligible
        }

        productLimitationBottomSheet?.setOnBottomSheetResult { urlResult ->
            when {
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_FINISH_ACTIVITY) -> {
                    activity?.finish()
                }
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_SAVING_DRAFT) -> {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT)
                    startActivity(intent)
                    saveProductToDraft()
                    activity?.finish()
                }
                urlResult.startsWith(HTTP_PREFIX) -> {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, urlResult))
                }
                else -> {
                    val intent = RouteManager.getIntent(context, urlResult)
                    startActivity(intent)
                }
            }
        }

        productLimitationTicker?.setOnClickListener {
            productLimitationBottomSheet?.setSubmitButtonText(getString(R.string.label_product_limitation_bottomsheet_button))
            productLimitationBottomSheet?.setIsSavingToDraft(false)
            productLimitationBottomSheet?.show(childFragmentManager)
        }

        // launch bottomsheet automatically when fragment loaded
        if (!isProductLimitEligible && isFragmentFirstTimeLoaded) {
            productLimitationTicker?.performClick()
        }
    }
}
