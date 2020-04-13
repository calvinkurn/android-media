package com.tokopedia.product.addedit.preview.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.Companion.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_RESULT_CACHE_ID
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DESCRIPTION_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_DETAIL_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SHIPMENT_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_EDIT
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.PRODUCT_STATUS_ACTIVE
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
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
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddEditProductPreviewFragment : BaseDaggerFragment(), ProductPhotoViewHolder.OnPhotoChangeListener {

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
        fun createInstance(productId: String, draftId: String): Fragment {
            return AddEditProductPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_ID, productId)
                    putString(EXTRA_DRAFT_ID, draftId)
                }
            }
        }

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        super.onCreate(savedInstanceState)
        arguments?.run {
            val draftId = getString(EXTRA_DRAFT_ID)
            viewModel.setProductId(getString(EXTRA_PRODUCT_ID) ?: "")
            draftId?.let { viewModel.setDraftId(it) }
            viewModel.getProductDraft(viewModel.getDraftId())
        }

        if(viewModel.isDrafting.value == true) {
            viewModel.getDraftId().let { viewModel.getProductDraft(it) }
        }

        if (viewModel.isEditing.value == true) {
            //TODO is goldmerchant and isregular
            ProductEditStepperTracking.trackScreen(shopId, false, false)
        } else {
            ProductAddStepperTracking.trackScreen()
        }
    }

    fun onBackPressed() {
        if (viewModel.isEditing.value == true) {
            ProductAddStepperTracking.trackBack(shopId)
        } else {
            ProductEditStepperTracking.trackBack(shopId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // action button
        doneButton = activity?.findViewById(R.id.tv_done)

        // photos
        productPhotosView = view.findViewById(R.id.rv_product_photos)
        productPhotoAdapter = ProductPhotoAdapter(MAX_PRODUCT_PHOTOS, mutableListOf(), this)
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
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackClickChangeProductPic(shopId)
            } else {
                ProductAddStepperTracking.trackStart(shopId)
            }
            val imageUrlOrPathList = viewModel.productInputModel.value?.detailInputModel?.imageUrlOrPathList ?: listOf()
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(imageUrlOrPathList)),
                    viewModel.isEditing.value ?: false)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }

        productStatusSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO function
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangeProductStatus(shopId)
            }
        }

        doneButton?.setOnClickListener {
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackFinishButton(shopId)
                context?.apply {
                    viewModel.productInputModel.value?.let { productInputModel ->
                        AddEditProductEditService.startService(this,
                                viewModel.getProductId(), productInputModel)
                    }
                }
            } else if(viewModel.isDrafting.value == true) {
                context?.let {
                    AddEditProductAddService.startService(it,
                            viewModel.productInputModel.value?.detailInputModel ?: DetailInputModel(),
                            viewModel.productInputModel.value?.descriptionInputModel ?: DescriptionInputModel(),
                            viewModel.productInputModel.value?.shipmentInputModel ?: ShipmentInputModel(),
                            viewModel.productInputModel.value?.variantInputModel ?: ProductVariantInputModel(),
                            viewModel.getDraftId())
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
            val isEditing = viewModel.isEditing.value ?: false
            val isDrafting = viewModel.isDrafting.value ?: false
            startAddEditProductDetailActivity(productInputModel, isEditing, isDrafting)
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
            //TODO functionality
            if (viewModel.isEditing.value == true) {
                ProductEditStepperTracking.trackChangePromotion(shopId)
            }
        }

        observeIsEditingStatus()
        observeIsDraftingStatus()
        observeProductData()
        observeProductInputModel()
        observeProductVariant()
        observeImageUrlOrPathList()
        observeProductVariantList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imagePickerResult = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    if (imagePickerResult != null && imagePickerResult.size > 0) {
                        val isEditMode = viewModel.isEditing.value ?: false
                        val isDraftMode = viewModel.isDrafting.value ?: false
                        // update the product pictures in the preview page
                        if (isEditMode || isDraftMode) {
                            viewModel.updateProductPhotos(imagePickerResult)
                            saveToDraft()
                        } else {
                            // start add product detail
                            val newProductInputModel = viewModel.getNewProductInputModel(imagePickerResult)
                            val isEditing = viewModel.isEditing.value ?: false
                            val isDrafting = viewModel.isDrafting.value ?: false
                            startAddEditProductDetailActivity(newProductInputModel, isEditing = isEditing, isDrafting = isDrafting)
                        }
                    }
                }
                REQUEST_CODE_DETAIL -> {
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    val descriptionInputModel =
                            data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                    val detailInputModel =
                            data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    context?.let {
                        AddEditProductAddService.startService(it, detailInputModel,
                                descriptionInputModel, shipmentInputModel, variantInputModel, viewModel.getDraftId())
                    }
                }
                REQUEST_CODE_DETAIL_EDIT -> {
                    val detailInputModel =
                            data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                    viewModel.updateDetailInputModel(detailInputModel)
                    viewModel.productInputModel.value?.let { it.detailInputModel = detailInputModel }
                    saveToDraft()
                }
                REQUEST_CODE_DESCRIPTION_EDIT -> {
                    val descriptionInputModel =
                            data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    viewModel.updateDescriptionInputModel(descriptionInputModel)
                    viewModel.updateVariantInputModel(variantInputModel)
                    viewModel.productInputModel.value?.let {
                        it.descriptionInputModel = descriptionInputModel
                        it.variantInputModel = variantInputModel
                    }
                    saveToDraft()
                }
                REQUEST_CODE_SHIPMENT_EDIT -> {
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    viewModel.updateShipmentInputModel(shipmentInputModel)
                    viewModel.productInputModel.value?.let { it.shipmentInputModel = shipmentInputModel }
                    saveToDraft()
                }
                REQUEST_CODE_VARIANT_EDIT -> {
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    viewModel.updateVariantInputModel(variantInputModel)
                    viewModel.productInputModel.value?.let { it.variantInputModel = variantInputModel }
                    saveToDraft()
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

    private fun formatProductPriceInput(productPriceInput: String): String {
        return if (productPriceInput.isNotBlank()) NumberFormat.getNumberInstance(Locale.US).format(productPriceInput.toBigInteger()).replace(",", ".")
        else productPriceInput
    }

    private fun displayEditMode(isEditMode: Boolean) {
        if (isEditMode) {
            doneButton?.show()
            enablePhotoEdit()
            enableDetailEdit()
            enableDescriptionEdit()
            enableVariantEdit()
            enableShipmentEdit()
            enablePromotionEdit()
            enableStatusEdit()
        }
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

    private fun observeIsEditingStatus() {
        viewModel.isEditing.observe(this, Observer {
            displayEditMode(it)
        })
    }

    private fun observeIsDraftingStatus() {
        viewModel.isDrafting.observe(this, Observer {
            displayEditMode(it)
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

    private fun saveToDraft() {
        if (viewModel.isDrafting.value == true) {
            viewModel.productInputModel.value?.let {
                viewModel.saveProductDraft(AddEditProductMapper.mapProductInputModelDetailToDraft(it), it.draftId, false)
            }
        }
    }

    private fun showProductPhotoPreview(productInputModel: ProductInputModel) {
        val imageUrlOrPathList = productInputModel.detailInputModel.imageUrlOrPathList
        productPhotoAdapter?.setProductPhotoPaths(imageUrlOrPathList.toMutableList())
    }

    private fun showProductDetailPreview(productInputModel: ProductInputModel) {
        val detailInputModel = productInputModel.detailInputModel
        productNameView?.text = detailInputModel.productName
        productPriceView?.text = "Rp " + formatProductPriceInput(detailInputModel.price.toString())
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
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_1), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_2), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_3), TEST_IMAGE_URL))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
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

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(selectedImagePathList: ArrayList<String>?): ImagePickerBuilder {

        val title = getString(R.string.action_pick_image)

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

    private fun startAddEditProductDetailActivity(productInputModel: ProductInputModel, isEditing: Boolean, isDrafting: Boolean) {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                put(EXTRA_IS_EDITING_PRODUCT, isEditing)
                put(EXTRA_IS_DRAFTING_PRODUCT, isDrafting)
            }
            val intent = Intent(this, AddEditProductDetailActivity::class.java).apply { putExtra(EXTRA_CACHE_MANAGER_ID, cacheManager.id) }
            if (!isEditing && !isDrafting) {
                startActivityForResult(intent, REQUEST_CODE_DETAIL)
            } else {
                startActivityForResult(intent, REQUEST_CODE_DETAIL_EDIT)
            }
        }
    }

    private fun moveToDescriptionActivity() {
        viewModel.productInputModel.value?.let {
            val intent = AddEditProductDescriptionActivity.createInstanceEditMode(context,
                    it.detailInputModel.categoryId, it.descriptionInputModel, it.variantInputModel)
            startActivityForResult(intent, REQUEST_CODE_DESCRIPTION_EDIT)
        }
    }

    private fun moveToShipmentActivity() {
        viewModel.productInputModel.let {
            val shipmentInputModel = it.value?.shipmentInputModel ?: ShipmentInputModel()
            val intent = AddEditProductShipmentActivity.createInstanceEditMode(context, shipmentInputModel)
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
                    put(AddEditProductUploadConstant.EXTRA_IS_ADD, false)
                }
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
                intent?.run {
                    putExtra(EXTRA_VARIANT_RESULT_CACHE_ID, cacheManager.id)
                    putExtra(AddEditProductUploadConstant.EXTRA_IS_USING_CACHE_MANAGER, true)
                    startActivityForResult(this, REQUEST_CODE_VARIANT_EDIT)
                }
                val productVariantByCatModelList: ArrayList<String> = ArrayList()
//            productVariantByCatModelList.add(AddEditProductDescriptionFragment.TEST_VARIANT)
            }
        }
    }
}
