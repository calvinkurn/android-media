package com.tokopedia.product.addedit.preview.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.BuildConfig
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_1
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_2
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_3
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_SIZECHART
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_VARIANT_SELECTION
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_PICKER_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.PictureViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_PRODUCT_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DESCRIPTION_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SHIPMENT_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_REQUEST_CODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.SET_CASHBACK_RESULT
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.preview.data.source.api.response.Cashback
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DESCRIPTION_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DUPLICATING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.PRODUCT_STATUS_ACTIVE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.SHIPMENT_DATA
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.preview.presentation.model.SetCashbackResult
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductAddService
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductEditService
import com.tokopedia.product.addedit.preview.presentation.viewmodel.AddEditProductPreviewViewModel
import com.tokopedia.product.addedit.shipment.presentation.activity.AddEditProductShipmentActivity
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.product.addedit.tracking.ProductEditStepperTracking
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEditProductPreviewFragment : BaseDaggerFragment(), ProductPhotoViewHolder.OnPhotoChangeListener {

    private var countTouchPhoto = 0
    private var dataBackPressed: Int? = null

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

    // shipment
    private var addEditProductShipmentTitle: Typography? = null
    private var addEditProductShipmentButton: Typography? = null

    // promotion
    private var editProductPromotionLayout: ViewGroup? = null
    private var editProductPromotionButton: Typography? = null

    // product status
    private var editProductStatusLayout: ViewGroup? = null
    private var productStatusSwitch: SwitchUnify? = null

    //loading
    private var loadingLayout: View? = null

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    @Inject
    lateinit var viewModel: AddEditProductPreviewViewModel

    companion object {
        fun createInstance(productId: String, draftId: String, isDuplicate: Boolean): Fragment {
            return AddEditProductPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_ID, productId)
                    putString(EXTRA_DRAFT_ID, draftId)
                    putBoolean(EXTRA_IS_DUPLICATING_PRODUCT, isDuplicate)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        super.onCreate(savedInstanceState)
        arguments?.run {
            val draftId = getString(EXTRA_DRAFT_ID).orEmpty()
            viewModel.setProductId(getString(EXTRA_PRODUCT_ID) ?: "")
            viewModel.setIsDuplicate(getBoolean(EXTRA_IS_DUPLICATING_PRODUCT, false))
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

    fun onCtaYesPressed() {
        ProductAddStepperTracking.trackDraftYes(shopId)
    }

    fun onCtaNoPressed() {
        ProductAddStepperTracking.trackDraftCancel(shopId)
    }

    fun onBackPressed() {
        if (viewModel.isEditing.value == true) {
            ProductEditStepperTracking.trackBack(shopId)
        } else {
            ProductAddStepperTracking.trackBack(shopId)
        }
    }

    fun saveProductDraft() {
        viewModel.productInputModel.value?.let {
            viewModel.saveProductDraft(AddEditProductMapper.mapProductInputModelDetailToDraft(it), it.draftId, false)
        }
    }

    fun isEditing(): Boolean {
        return viewModel.isEditing.value ?: false
    }

    fun isDrafting(): Boolean {
        return viewModel.getDraftId() > 0L
    }

    fun dataBackPressedLoss(): Boolean {
        // when stepper page has no data, dataBackPressed is null but if stepper page has data, dataBackPressed has data too
        // dataBackPressed is a sign of activity where data is obtained
        if(dataBackPressed == null) {
            return true
        }
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // activity toolbar
        toolbar = activity?.findViewById(R.id.toolbar)
        toolbar?.title = getString(R.string.label_title_add_product)

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

        // shipment
        addEditProductShipmentTitle = view.findViewById(R.id.tv_product_shipment)
        addEditProductShipmentButton = view.findViewById(R.id.tv_start_add_edit_product_shipment)

        // promotion
        editProductPromotionLayout = view.findViewById(R.id.edit_product_promotion_step_layout)
        editProductPromotionButton = view.findViewById(R.id.tv_edit_product_promotion)

        // status
        editProductStatusLayout = view.findViewById(R.id.edit_product_status_layout)
        productStatusSwitch = view.findViewById(R.id.su_product_status)

        //loading
        loadingLayout = view.findViewById(R.id.loading_layout)

        addEditProductPhotoButton?.setOnClickListener {
            // tracking
            val buttonTextStart: String = getString(R.string.action_start)
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackClickChangeProductPic(shopId)
            } else if (addEditProductPhotoButton?.text == buttonTextStart){
                ProductAddStepperTracking.trackStart(shopId)
            }

            productPhotoAdapter?.run {
                // show error message when maximum product image is reached
                val productPhotoSize = this.getProductPhotoPaths().size
                if (productPhotoSize == MAX_PRODUCT_PHOTOS) showMaxProductImageErrorToast(getString(R.string.error_max_product_photo))
                else {
                    val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                        if (urlOrPath.startsWith(HTTP_PREFIX)) viewModel.productInputModel.value?.detailInputModel?.pictureList?.find { it.urlThumbnail == urlOrPath }?.urlOriginal
                                ?: urlOrPath
                        else urlOrPath
                    }.orEmpty()
                    val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(imageUrlOrPathList)), viewModel.isEditing.value
                            ?: false, viewModel.isAdding)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE)
                }
            }
        }

        productStatusSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateProductStatus(isChecked)
        }

        // track switch status on click
        productStatusSwitch?.setOnClickListener {
            val isChecked = productStatusSwitch?.isChecked
            if (isChecked == true && viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeProductStatus(shopId)
            }
        }

        doneButton?.setOnClickListener {
            updateImageList()
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackFinishButton(shopId)
            }
            val validateMessage = viewModel.validateProductInput(viewModel.productInputModel.value?.detailInputModel ?: DetailInputModel())
            if (validateMessage.isNotEmpty()) {
                Toaster.make(view, validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            } else {

                // increment wholesale min order by one because of > symbol
                viewModel.productInputModel.value?.run {
                    this.detailInputModel.wholesaleList = viewModel.incrementWholeSaleMinOrder(this.detailInputModel.wholesaleList)
                }

                // when we perform add product, the productId will be 0
                // when we perform edit product, the productId will be provided from the getProductV3 response
                // when we perform open draft, previous state before we save the product to draft will be the same
                if (viewModel.productInputModel.value?.productId.orZero() != 0L) {
                    context?.let {
                        viewModel.productInputModel.value?.run {
                            val saveInstanceCacheManager = SaveInstanceCacheManager(it, true)
                            saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, this)
                            AddEditProductEditService.startService(it, saveInstanceCacheManager.id ?: "")
                            activity?.setResult(RESULT_OK)
                            activity?.finish()
                        }
                    }
                } else {
                    viewModel.productInputModel.value?.let { productInputModel ->
                        startProductAddService(productInputModel)
                        activity?.setResult(RESULT_OK)
                        activity?.finish()
                    }
                }
            }

        }

        addProductPhotoTipsLayout?.setOnClickListener {
            if (viewModel.isEditing.value == false) {
                ProductAddStepperTracking.trackHelpProductQuality(shopId)
            }
            showPhotoTips()
        }

        addEditProductDetailButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeProductDetail(shopId)
            }
            val productInputModel = viewModel.productInputModel.value ?: ProductInputModel()
            // there is 3 case where we come here
            // 1. edit product
            // 2. open draft (edit product or add product mode)
            // 3. add product then press back
            // to prevent edit tracker always fired when we came here from add product mode
            // we need a flag isAdding which will be true only if we come to this fragment from product manage
            val isAdding = viewModel.isAdding
            startAddEditProductDetailActivity(productInputModel, true, isAdding)
        }

        addEditProductDescriptionButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeProductDesc(shopId)
            }
            moveToDescriptionActivity()
        }

        addEditProductVariantButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackAddProductVariant(shopId)
            }
            viewModel.productVariantListData?.apply {
                showVariantDialog(this) }
        }

        addProductVariantTipsLayout?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackClickHelpPriceVariant(shopId)
            }
            showVariantTips()
        }

        addEditProductShipmentButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeShipping(shopId)
            }
            moveToShipmentActivity()
        }

        editProductPromotionButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangePromotion(shopId)
            }
            setCashback()
        }

        context?.let { UpdateShopActiveService.startService(it) }
        //If you add another observe, don't forget to remove observers at removeObservers()
        observeIsEditingStatus()
        observeProductData()
        observeProductInputModel()
        observeProductVariant()
        observeImageUrlOrPathList()
        observeVariantList()
        observeIsLoading()
        observeSaveProductDraft()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imagePickerResult = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    val originalImageUrl = data.getStringArrayListExtra(ImageEditorActivity.RESULT_PREVIOUS_IMAGE)
                    val isEditted = data.getSerializableExtra(ImageEditorActivity.RESULT_IS_EDITTED) as ArrayList<Boolean>
                    if (imagePickerResult != null && imagePickerResult.size > 0) {
                        val shouldUpdatePhotosInsteadMoveToDetail = viewModel.isEditing.value ?: false ||
                                viewModel.isDuplicate ||
                                viewModel.productInputModel.value != null
                        // update the product pictures in the preview page
                        // this should be executed when the user press "Ubah" on stepper in add or edit or duplicate product
                        if (shouldUpdatePhotosInsteadMoveToDetail) {
                            viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, isEditted)
                        } else {
                            // this only executed when we came from empty stepper page (add product)
                            val newProductInputModel = viewModel.getNewProductInputModel(imagePickerResult)
                            startAddEditProductDetailActivity(newProductInputModel, isEditing = false, isAdding = true)
                        }
                    }
                }
                REQUEST_CODE_DETAIL -> {
                    dataBackPressed = data.getIntExtra(EXTRA_BACK_PRESSED, 0)
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        viewModel.productAddResult.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
                    }
                    when (dataBackPressed) {
                        DETAIL_DATA -> {
                            viewModel.productAddResult.value?.let { displayAddModeDetail(it) }
                            disableDescriptionEdit()
                            disableShipmentEdit()
                            return
                        }
                        DESCRIPTION_DATA -> {
                            viewModel.productAddResult.value?.let { displayAddModeDetail(it) }
                            enableDescriptionEdit()
                            disableShipmentEdit()
                            return
                        }
                        SHIPMENT_DATA -> {
                            viewModel.productAddResult.value?.let { displayAddModeDetail(it) }
                            enableDescriptionEdit()
                            enableShipmentEdit()
                            return
                        }
                    }
                    val productInputModel = viewModel.productAddResult.value ?: ProductInputModel()
                    context?.let {
                        val validateMessage = viewModel.validateProductInput(productInputModel.detailInputModel)
                        if (validateMessage.isEmpty()) {
                            context?.apply {
                                val saveInstanceCacheManager = SaveInstanceCacheManager(this, true)
                                saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                                AddEditProductAddService.startService(this, saveInstanceCacheManager.id ?: "")
                            }
                            activity?.setResult(RESULT_OK)
                            activity?.finish()
                        } else {
                            view?.let { view ->
                                Toaster.make(view, validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                            }
                        }
                    }
                }
                REQUEST_CODE_DETAIL_EDIT -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        viewModel.productInputModel.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    }
                }
                REQUEST_CODE_DESCRIPTION_EDIT -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        viewModel.productInputModel.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    }
                    enableDescriptionEdit()
                }
                REQUEST_CODE_SHIPMENT_EDIT -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        viewModel.productInputModel.value = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    }
                    enableShipmentEdit()
                }
                REQUEST_CODE_VARIANT_DIALOG_EDIT -> {
                    viewModel.productInputModel.value?.let { productInputModel ->
                        val variantCacheId = data.getStringExtra(EXTRA_VARIANT_PICKER_RESULT_CACHE_ID) ?: ""
                        val cacheManager = SaveInstanceCacheManager(requireContext(), variantCacheId)
                        if (data.hasExtra(EXTRA_PRODUCT_VARIANT_SELECTION)) {
                            val productVariantViewModel = cacheManager.get(EXTRA_PRODUCT_VARIANT_SELECTION,
                                    object : TypeToken<ProductVariantInputModel>() {}.type) ?: ProductVariantInputModel()
                            viewModel.updateVariantAndOption(productVariantViewModel.productVariant,
                                    productVariantViewModel.variantOptionParent)
                        }
                        if (data.hasExtra(EXTRA_PRODUCT_SIZECHART)) {
                            val productPictureViewModel = cacheManager.get(EXTRA_PRODUCT_SIZECHART,
                                    object : TypeToken<PictureViewModel>() {}.type, PictureViewModel())
                            viewModel.updateSizeChart(productPictureViewModel)
                        }
                        showEmptyVariantState(productInputModel.variantInputModel.productSizeChart == null)
                    }
                }
                SET_CASHBACK_REQUEST_CODE -> {
                    val cacheManagerId = data.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY) ?: ""
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    val setCashbackResult: SetCashbackResult? = cacheManager?.get(SET_CASHBACK_RESULT, SetCashbackResult::class.java)
                    if(setCashbackResult == null) {
                        onFailedSetCashback()
                        return
                    }
                    setCashbackResult.let { cashbackResult ->
                        if(!cashbackResult.limitExceeded) {
                            val cashbackProduct = Cashback(cashbackResult.cashback)
                            viewModel.productDomain = viewModel.productDomain.copy(cashback = cashbackProduct)
                            onSuccessSetCashback()
                        } else {
                            onFailedSetCashback()
                        }
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
        if(countTouchPhoto > 2) {
            countTouchPhoto = 1
        }
        // tracker only hit when there are two images of product
        if(productPhotoAdapter?.itemCount ?: 0 > 1) {
            // to avoid double hit tracker when dragging or touching image product, we have to put if here
            if(countTouchPhoto == 2) {
                if (viewModel.isEditing.value == true && !viewModel.isAdding) {
                    ProductEditMainTracking.trackDragPhoto(shopId)
                } else {
                    ProductAddMainTracking.trackDragPhoto(shopId)
                }
            }
        }
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        if (viewModel.isEditing.value == true) {
            ProductEditStepperTracking.trackRemoveProductImage(shopId)
        }
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
            addEditProductDetailTitle?.setTextColor(ContextCompat.getColor(it, android.R.color.black))
            addEditProductDetailButton?.text = getString(R.string.label_change)
            addEditProductDetailButton?.show()
            dividerDetail?.hide()
        }
    }

    private fun enableDescriptionEdit() {
        context?.let {
            addEditProductDescriptionTitle?.setTextColor(ContextCompat.getColor(it, android.R.color.black))
            addEditProductDescriptionButton?.text = getString(R.string.label_change)
            addEditProductDescriptionButton?.show()
        }
    }

    private fun enableVariantEdit() {
        addEditProductVariantLayout?.show()
    }

    private fun enableShipmentEdit() {
        context?.let {
            addEditProductShipmentTitle?.setTextColor(ContextCompat.getColor(it, android.R.color.black))
            addEditProductShipmentButton?.text = getString(R.string.label_change)
            addEditProductShipmentButton?.show()
        }
    }

    private fun enablePromotionEdit() {
        editProductPromotionLayout?.show()
    }

    private fun enableStatusEdit() {
        editProductStatusLayout?.show()
    }

    private fun disableShipmentEdit() {
        context?.let {
            addEditProductShipmentButton?.text = getString(R.string.action_add)
            addEditProductShipmentButton?.show()
        }
    }

    private fun disableDescriptionEdit() {
        context?.let {
            addEditProductDescriptionButton?.text = getString(R.string.action_add)
            addEditProductDescriptionButton?.show()
        }
    }

    private fun observeIsEditingStatus() {
        viewModel.isEditing.observe(this, Observer {
            if (it) displayEditMode()
        })
    }

    private fun observeProductData() {
        viewModel.getProductResult.observe(this, Observer { result ->
            when (result) {
                is Success -> {
                    val isVariantEmpty = result.data.variant.products.isEmpty()
                    showEmptyVariantState(isVariantEmpty)
                    showProductStatus(result.data)
                }
                is Fail -> {
                    context?.let {
                        showGetProductErrorToast(ErrorHandler.getErrorMessage(it, result.throwable))
                        AddEditProductErrorHandler.logExceptionToCrashlytics(result.throwable)
                    }
                }
            }
        })
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(this, Observer {
            showProductPhotoPreview(it)
            showProductDetailPreview(it)
            showEmptyVariantState(viewModel.productInputModel.value?.variantInputModel?.productSizeChart == null)
            if (viewModel.getDraftId() != 0L || it.productId != 0L || viewModel.getProductId().isNotBlank()) {
                displayEditMode()
            }
        })
    }

    private fun observeProductVariant() {
        viewModel.isVariantEmpty.observe(this, Observer {
            showEmptyVariantState(it)
        })
    }

    private fun observeImageUrlOrPathList() {
        viewModel.imageUrlOrPathList.observe(this, Observer {
            productPhotoAdapter?.setProductPhotoPaths(it)
        })
    }

    private fun observeVariantList() {
        addEditProductVariantButton?.alpha = 0.5F
        addEditProductVariantButton?.isEnabled = false
        viewModel.productVariantList.observe(this, Observer { result ->
            when (result) {
                is Success -> {
                    addEditProductVariantButton?.isEnabled = true
                    addEditProductVariantButton?.alpha = 1F
                }
                is Fail -> {
                    showVariantErrorToast(getString(R.string.error_cannot_get_variants))
                    AddEditProductErrorHandler.logExceptionToCrashlytics(result.throwable)
                }
            }
        })
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun observeSaveProductDraft() {
        viewModel.saveProductDraftResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> Toast.makeText(context, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show()
                is Fail -> {
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun removeObservers() {
        viewModel.isEditing.removeObservers(this)
        viewModel.getProductResult.removeObservers(this)
        viewModel.productInputModel.removeObservers(this)
        viewModel.isVariantEmpty.removeObservers(this)
        viewModel.imageUrlOrPathList.removeObservers(this)
        viewModel.productVariantList.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
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
            addEditProductVariantButton?.text = getString(R.string.label_add)
            addProductVariantTipsLayout?.show()
        } else {
            addEditProductVariantButton?.text = getString(R.string.label_change)
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
                    actionText = getString(R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        viewModel.getProductData(viewModel.getProductId())
                    })
        }
    }

    private fun showVariantErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        val categoryId: String = viewModel.productInputModel.value?.detailInputModel?.categoryId.orEmpty()
                        viewModel.getVariantList(categoryId)
                    })
        }
    }

    private fun showMaxProductImageErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, type = Toaster.TYPE_ERROR)
        }
    }

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(selectedImagePathList: ArrayList<String>?): ImagePickerBuilder {

        val title = getString(R.string.action_pick_photo)

        val placeholderDrawableRes = arrayListOf(
                R.drawable.ic_utama,
                R.drawable.ic_depan,
                R.drawable.ic_samping,
                R.drawable.ic_atas,
                R.drawable.ic_detail
        )

        val imagePickerPickerTabTypeDef = intArrayOf(
                ImagePickerTabTypeDef.TYPE_GALLERY,
                ImagePickerTabTypeDef.TYPE_CAMERA,
                ImagePickerTabTypeDef.TYPE_INSTAGRAM
        )

        val imagePickerEditorBuilder = ImagePickerEditorBuilder.getDefaultBuilder().apply {
            this.belowMinResolutionErrorMessage = getString(R.string.error_image_under_x_resolution, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION)
            this.imageTooLargeErrorMessage = getString(R.string.error_image_too_large, (AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB / 1024))
            this.isRecheckSizeAfterResize = false
        }

        val imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                selectedImagePathList,
                placeholderDrawableRes,
                R.string.label_primary,
                MAX_PRODUCT_PHOTOS, false)

        return ImagePickerBuilder(
                title,
                imagePickerPickerTabTypeDef,
                GalleryType.IMAGE_ONLY,
                AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                imagePickerEditorBuilder,
                imagePickerMultipleSelectionBuilder)
    }

    private fun startAddEditProductDetailActivity(productInputModel: ProductInputModel, isEditing: Boolean, isAdding: Boolean) {
        context?.run {
            updateImageList()
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                put(EXTRA_IS_EDITING_PRODUCT, isEditing)
                put(EXTRA_IS_ADDING_PRODUCT, isAdding)
            }
            val intent = Intent(this, AddEditProductDetailActivity::class.java).apply { putExtra(EXTRA_CACHE_MANAGER_ID, cacheManager.id) }
            if (isEditing) {
                startActivityForResult(intent, REQUEST_CODE_DETAIL_EDIT)
            } else {
                startActivityForResult(intent, REQUEST_CODE_DETAIL)
            }
        }
    }

    private fun moveToDescriptionActivity() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
                put(EXTRA_IS_EDITING_PRODUCT, true)
                put(EXTRA_IS_ADDING_PRODUCT, viewModel.isAdding)
            }
            val intent = AddEditProductDescriptionActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_DESCRIPTION_EDIT)
        }
    }

    private fun moveToShipmentActivity() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
                put(EXTRA_IS_EDITING_PRODUCT, true)
                put(EXTRA_IS_ADDING_PRODUCT, viewModel.isAdding)
            }
            val intent = AddEditProductShipmentActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_SHIPMENT_EDIT)
        }
    }

    private fun showVariantDialog(variantList: List<ProductVariantByCatModel>) {
        activity?.let {
            viewModel.productInputModel.value?.let { productInputModel ->
                val cacheManager = SaveInstanceCacheManager(it, true).apply {
                    put(AddEditProductUploadConstant.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, variantList)
                    put(AddEditProductUploadConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productInputModel.variantInputModel)
                    put(AddEditProductUploadConstant.EXTRA_PRODUCT_SIZECHART, productInputModel.variantInputModel.productSizeChart)
                    put(AddEditProductUploadConstant.EXTRA_CURRENCY_TYPE, AddEditProductDescriptionFragment.TYPE_IDR)
                    put(AddEditProductUploadConstant.EXTRA_DEFAULT_PRICE, productInputModel.detailInputModel.price)
                    put(AddEditProductUploadConstant.EXTRA_STOCK_TYPE, viewModel.getStatusStockViewVariant())
                    put(AddEditProductUploadConstant.EXTRA_IS_OFFICIAL_STORE, false)
                    put(AddEditProductUploadConstant.EXTRA_DEFAULT_SKU, "")
                    put(AddEditProductUploadConstant.EXTRA_NEED_RETAIN_IMAGE, false)
                    put(AddEditProductUploadConstant.EXTRA_HAS_WHOLESALE, viewModel.hasWholesale)
                    put(AddEditProductUploadConstant.EXTRA_IS_ADD, viewModel.isAdding)
                }
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
                intent?.run {
                    putExtra(EXTRA_VARIANT_RESULT_CACHE_ID, cacheManager.id)
                    putExtra(AddEditProductUploadConstant.EXTRA_IS_USING_CACHE_MANAGER, true)
                    putExtra(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV1,
                            viewModel.hasOriginalVariantLevel)
                    putExtra(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV2,
                            viewModel.hasOriginalVariantLevel)
                    startActivityForResult(this, REQUEST_CODE_VARIANT_DIALOG_EDIT)
                }
            }
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
        context?.let {
            val saveInstanceCacheManager = SaveInstanceCacheManager(it, true)
            saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
            AddEditProductAddService.startService(
                    context = it,
                    cacheId = saveInstanceCacheManager.id ?: ""
            )
        }
    }

    private fun showLoading() {
        loadingLayout?.show()
    }

    private fun hideLoading() {
        loadingLayout?.hide()
    }
}
