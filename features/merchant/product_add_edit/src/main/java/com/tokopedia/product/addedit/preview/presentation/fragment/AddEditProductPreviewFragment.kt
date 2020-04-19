package com.tokopedia.product.addedit.preview.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
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
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_1
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_2
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TIPS_URL_3
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.photoTipsUrl
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_SIZECHART
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_VARIANT_SELECTION
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_PICKER_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.PictureViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DESCRIPTION_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SHIPMENT_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_EDIT
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
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DUPLICATING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.PRODUCT_STATUS_ACTIVE
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.preview.presentation.model.SetCashbackResult
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductAddService
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductEditService
import com.tokopedia.product.addedit.preview.presentation.viewmodel.AddEditProductPreviewViewModel
import com.tokopedia.product.addedit.shipment.presentation.activity.AddEditProductShipmentActivity
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking
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

    // description
    private var addEditProductDescriptionTitle: Typography? = null
    private var addEditProductDescriptionButton: Typography? = null

    //variant
    private var addEditProductVariantLayout: ViewGroup? = null
    private var addEditProductVariantButton: Typography? = null
    private var addProductVariantTipsLayout: ViewGroup? = null
    private var variantDivider: DividerUnify? = null

    // shipment
    private var addEditProductShipmentTitle: Typography? = null
    private var addEditProductShipmentButton: Typography? = null

    // promotion
    private var editProductPromotionLayout: ViewGroup? = null
    private var editProductPromotionButton: Typography? = null

    // product status
    private var editProductStatusLayout: ViewGroup? = null
    private var productStatusSwitch: SwitchUnify? = null

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
        Toast.makeText(context, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show()
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

        // description
        addEditProductDescriptionTitle = view.findViewById(R.id.tv_product_description)
        addEditProductDescriptionButton = view.findViewById(R.id.tv_start_add_edit_product_description)

        // variant
        addEditProductVariantLayout = view.findViewById(R.id.add_product_variant_step_layout)
        addEditProductVariantButton = view.findViewById(R.id.tv_start_add_edit_product_variant)
        addProductVariantTipsLayout = view.findViewById(R.id.add_product_variant_tips_layout)
        variantDivider = view.findViewById(R.id.du_fourth)

        // shipment
        addEditProductShipmentTitle = view.findViewById(R.id.tv_product_shipment)
        addEditProductShipmentButton = view.findViewById(R.id.tv_start_add_edit_product_shipment)

        // promotion
        editProductPromotionLayout = view.findViewById(R.id.edit_product_promotion_step_layout)
        editProductPromotionButton = view.findViewById(R.id.tv_edit_product_promotion)

        // status
        editProductStatusLayout = view.findViewById(R.id.edit_product_status_layout)
        productStatusSwitch = view.findViewById(R.id.su_product_status)

        addEditProductPhotoButton?.setOnClickListener {

            // tracking
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackClickChangeProductPic(shopId)
            } else {
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
                            ?: false)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE)
                }
            }
        }

        productStatusSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeProductStatus(shopId)
            }
            viewModel.updateProductStatus(isChecked)
        }

        doneButton?.setOnClickListener {
            updateImageList()
            ProductEditStepperTracking.trackFinishButton(shopId)
            val validateMessage = viewModel.validateProductInput()
            if (validateMessage.isNotEmpty()) {
                Toaster.make(view, validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            } else {
                // when we perform add product, the productId will be 0
                // when we perform edit product, the productId will be provided from the getProductV3 response
                // when we perform open draft, previous state before we save the product to draft will be the same
                if (viewModel.productInputModel.value?.productId.orZero() != 0L) {
                    context?.let {
                        viewModel.productInputModel.value?.run {
                            AddEditProductEditService.startService(it, this)
                            moveToManageProduct()
                        }
                    }
                } else {
                    context?.apply {
                        viewModel.productInputModel.value?.let { productInputModel ->
                            AddEditProductAddService.startService(
                                    context = this,
                                    detailInputModel = productInputModel.detailInputModel,
                                    descriptionInputModel = productInputModel.descriptionInputModel,
                                    shipmentInputModel = productInputModel.shipmentInputModel,
                                    variantInputModel = productInputModel.variantInputModel,
                                    draftId = viewModel.getDraftId()
                            )
                            moveToManageProduct()
                        }
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
            val categoryId: String = viewModel.productInputModel.value?.detailInputModel?.categoryId
                    ?: ""
            viewModel.getVariantList(categoryId)
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

        observeIsEditingStatus()
        observeProductData()
        observeProductInputModel()
        observeProductVariant()
        observeImageUrlOrPathList()
        observeProductVariantList()
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
                    val dataBackPressed = data.getIntExtra(EXTRA_BACK_PRESSED, 0)
                    val productInputModel = data.getParcelableExtra<ProductInputModel>(EXTRA_PRODUCT_INPUT_MODEL)
                    viewModel.productAddResult.value = productInputModel
                    when (dataBackPressed) {
                        1 -> {
                            displayAddModeDetail(productInputModel)
                            disableDescriptionEdit()
                            disableShipmentEdit()
                            return
                        }
                        2 -> {
                            displayAddModeDetail(productInputModel)
                            enableDescriptionEdit()
                            disableShipmentEdit()
                            return
                        }
                        3 -> {
                            displayAddModeDetail(productInputModel)
                            enableDescriptionEdit()
                            enableShipmentEdit()
                            return
                        }
                    }

                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    val descriptionInputModel =
                            data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                    val detailInputModel =
                            data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    context?.let {
                        val validateMessage = viewModel.validateProductInput(detailInputModel)
                        if (validateMessage.isEmpty()) {
                            AddEditProductAddService.startService(it, detailInputModel,
                                descriptionInputModel, shipmentInputModel, variantInputModel,
                                    viewModel.getDraftId())
                            activity?.finish()
                        } else {
                            view?.let { view ->
                                Toaster.make(view, validateMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                            }
                        }
                    }
                }

                REQUEST_CODE_DETAIL_EDIT -> {
                    val detailInputModel =
                            data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    viewModel.updateDetailInputModel(detailInputModel)
                    viewModel.updateVariantInputModel(variantInputModel)
                }
                REQUEST_CODE_DESCRIPTION_EDIT -> {
                    val descriptionInputModel =
                            data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    viewModel.updateDescriptionInputModel(descriptionInputModel)
                    viewModel.updateVariantInputModel(variantInputModel)
                    enableDescriptionEdit()
                }
                REQUEST_CODE_SHIPMENT_EDIT -> {
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    viewModel.updateShipmentInputModel(shipmentInputModel)
                    enableShipmentEdit()
                }
                REQUEST_CODE_VARIANT_EDIT -> {
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    viewModel.updateVariantInputModel(variantInputModel)
                }
                REQUEST_CODE_VARIANT_DIALOG_EDIT -> {
                    viewModel.productInputModel.value?.let { productInputModel ->
                        val variantCacheId = data.getStringExtra(EXTRA_VARIANT_PICKER_RESULT_CACHE_ID)
                        val cacheManager = SaveInstanceCacheManager(context!!, variantCacheId)
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
                    }
                }
                SET_CASHBACK_REQUEST_CODE -> {
                    val cacheManagerId = data.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY)
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    val setCashbackResult: SetCashbackResult? = cacheManager?.get(SET_CASHBACK_RESULT, SetCashbackResult::class.java)
                    setCashbackResult?.let { cashbackResult ->
                        if(!cashbackResult.limitExceeded) {
                            val cashbackProduct = Cashback(cashbackResult.cashback)
                            viewModel.productDomain = viewModel.productDomain.copy(cashback = cashbackProduct)
                            onSuccessSetCashback(cashbackResult)
                        }
                    }
                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        if (viewModel.isEditing.value == true) {
            ProductEditStepperTracking.trackRemoveProductImage(shopId)
        }
    }

    private fun onSuccessSetCashback(setCashbackResult: SetCashbackResult) {
        view?.let {
            Toaster.make(it, getString(
                R.string.product_manage_set_cashback_success, setCashbackResult.productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
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
        addEditProductPhotoButton?.text = getString(R.string.label_change)
        addProductPhotoTipsLayout?.hide()
        productPhotosView?.show()
    }

    private fun enableDetailEdit() {
        context?.let {
            addEditProductDetailTitle?.setTextColor(ContextCompat.getColor(it, android.R.color.black))
            addEditProductDetailButton?.text = getString(R.string.label_change)
            addEditProductDetailButton?.show()
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
        variantDivider?.show()
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
        viewModel.getProductResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    val isVariantEmpty = it.data.variant.products.isEmpty()
                    showEmptyVariantState(isVariantEmpty)
                    showProductStatus(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(this, Observer {
            showProductPhotoPreview(it)
            showProductDetailPreview(it)
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

    private fun observeProductVariantList() {
        viewModel.productVariantList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> showVariantDialog(result.data)
                is Fail -> showVariantErrorToast(getString(R.string.error_cannot_get_variants))
            }
        })
    }

    private fun setCashback() {
        viewModel.productInputModel.value?.let { productInputModel ->
            val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.SET_CASHBACK, viewModel.getProductId(), productInputModel.detailInputModel.productName)
            val uri = Uri.parse(newUri)
                    .buildUpon()
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
            if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) productInputModel.detailInputModel.pictureList[pictureIndex++].urlThumbnail
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

    private fun showVariantErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    clickListener = View.OnClickListener {
                        val categoryId: String = viewModel.productInputModel.value?.detailInputModel?.categoryId
                                ?: ""
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

        val imagePickerEditorBuilder = ImagePickerEditorBuilder.getDefaultBuilder()

        val imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                selectedImagePathList,
                placeholderDrawableRes,
                R.string.label_primary,
                MAX_PRODUCT_PHOTOS, false)

        return ImagePickerBuilder(
                title,
                imagePickerPickerTabTypeDef,
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
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
        viewModel.productInputModel.value?.let {
            val intent = AddEditProductDescriptionActivity.createInstanceEditMode(context,
                    it.detailInputModel.categoryId, it.descriptionInputModel, it.variantInputModel, viewModel.isAdding)
            startActivityForResult(intent, REQUEST_CODE_DESCRIPTION_EDIT)
        }
    }

    private fun moveToShipmentActivity() {
        viewModel.productInputModel.let {
            val shipmentInputModel = it.value?.shipmentInputModel ?: ShipmentInputModel()
            val intent = AddEditProductShipmentActivity.createInstanceEditMode(context, shipmentInputModel, viewModel.isAdding)
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
                    put(AddEditProductUploadConstant.EXTRA_DEFAULT_PRICE, 0.0)
                    put(AddEditProductUploadConstant.EXTRA_STOCK_TYPE, "")
                    put(AddEditProductUploadConstant.EXTRA_IS_OFFICIAL_STORE, false)
                    put(AddEditProductUploadConstant.EXTRA_DEFAULT_SKU, "")
                    put(AddEditProductUploadConstant.EXTRA_NEED_RETAIN_IMAGE, false)
                    put(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV1, true)
                    put(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV2, false)
                    put(AddEditProductUploadConstant.EXTRA_HAS_WHOLESALE, false)
                    put(AddEditProductUploadConstant.EXTRA_IS_ADD, 1    )
                }
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
                intent?.run {
                    putExtra(EXTRA_VARIANT_RESULT_CACHE_ID, cacheManager.id)
                    putExtra(AddEditProductUploadConstant.EXTRA_IS_USING_CACHE_MANAGER, true)
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

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
    }
}
