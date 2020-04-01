package com.tokopedia.product.addedit.preview.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.description.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment.Companion.REQUEST_CODE_DETAIL
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductUploadService
import com.tokopedia.product.addedit.preview.presentation.viewmodel.AddEditProductPreviewViewModel
import com.tokopedia.product.addedit.shipment.presentation.activity.AddEditProductShipmentActivity
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddEditProductPreviewFragment : BaseDaggerFragment() {

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

    // shipment
    private var addEditProductShipmentTitle: Typography? = null
    private var addEditProductShipmentButton: Typography? = null

    // promotion
    private var editProductPromotionLayout: ViewGroup? = null
    private var editProductPromotionButton: Typography? = null

    // product status
    private var editProductStatusLayout: ViewGroup? = null
    private var productStatusSwitch: SwitchUnify? = null

    @Inject
    lateinit var viewModel: AddEditProductPreviewViewModel

    companion object {
        fun createInstance(productId: String): Fragment {
            return AddEditProductPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_ID, productId)
                }
            }
        }

        private const val MAX_PRODUCT_PHOTOS = 5
        private const val REQUEST_CODE_IMAGE = 0x01

        const val EXTRA_PRODUCT_ID = "PRODUCT_ID"

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            viewModel.setProductId(getString(EXTRA_PRODUCT_ID) ?: "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // photos
        productPhotosView = view.findViewById(R.id.rv_product_photos)
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
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder())
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }

        addProductPhotoTipsLayout?.setOnClickListener {
            showPhotoTips()
        }

        addEditProductDetailButton?.setOnClickListener {
            moveToAddEditProductActivity(arrayListOf())
        }

        addEditProductDescriptionButton?.setOnClickListener {
            moveToDescriptionActivity()
        }

        addEditProductVariantButton?.setOnClickListener {
            showVariantDialog()
        }

        addProductVariantTipsLayout?.setOnClickListener {
            showVariantTips()
        }

        addEditProductShipmentButton?.setOnClickListener {
            startAddEditProductShipmentActivity()
        }

        observeIsEditModeState()
        observeGetProductResult()
        observeProductVariant()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    moveToAddEditProductActivity(imageUrlOrPathList)
                }
            } else if (requestCode == REQUEST_CODE_DETAIL) {
                val shipmentInputModel =
                        data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                val descriptionInputModel =
                        data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                val detailInputModel =
                        data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                val variantInputModel =
                        data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                context?.let { AddEditProductUploadService.startService(it, detailInputModel,
                        descriptionInputModel, shipmentInputModel, variantInputModel) }
            }
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

    private fun displayEditMode(isEditMode: Boolean) {
        if (isEditMode) {
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

    private fun observeIsEditModeState() {
        viewModel.isEditMode.observe(this, Observer {
            displayEditMode(it)
        })
    }

    private fun observeGetProductResult() {
        viewModel.getProductResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    showProductDetailPreview(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun observeProductVariant() {
        viewModel.isVariantEmpty.observe(this, Observer {
            showEmptyVariantState(it)
        })
    }

    private fun showProductDetailPreview(product: Product) {
        productNameView?.text = product.productName
        productPriceView?.text = product.price.toString()
        productStockView?.text = product.stock.toString()
        productDetailPreviewLayout?.show()
    }

    private fun showEmptyVariantState(isVariantEmpty: Boolean) {
        if (isVariantEmpty) {
            addEditProductVariantButton?.text = getString(R.string.label_add)
            addProductVariantTipsLayout?.show()
        }
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

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(): ImagePickerBuilder {

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
                null,
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

    private fun moveToAddEditProductActivity(imageUrlOrPathList: ArrayList<String>) {
        val addEditProductDetailIntent = Intent(context, AddEditProductDetailActivity::class.java)
        addEditProductDetailIntent.putStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS, imageUrlOrPathList)
        startActivityForResult(addEditProductDetailIntent, REQUEST_CODE_DETAIL)
    }

    private fun moveToDescriptionActivity() {
        startActivity(AddEditProductDescriptionActivity.createInstance(context))
    }

    private fun startAddEditProductShipmentActivity() {
        val intent = Intent(context, AddEditProductShipmentActivity::class.java)
        startActivityForResult(intent, AddEditProductShipmentFragment.REQUEST_CODE_SHIPMENT)
    }

    private fun showVariantDialog() {
        activity?.let {
            val productVariantByCatModelList: ArrayList<String> = ArrayList()
            productVariantByCatModelList.add(AddEditProductDescriptionFragment.TEST_VARIANT)
            val cacheManager = SaveInstanceCacheManager(it, true).apply {
                put(AddEditProductUploadConstant.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList) // must using json
                put(AddEditProductUploadConstant.EXTRA_PRODUCT_VARIANT_SELECTION, "") // must using json
                put(AddEditProductUploadConstant.EXTRA_CURRENCY_TYPE, AddEditProductDescriptionFragment.TYPE_IDR)
                put(AddEditProductUploadConstant.EXTRA_DEFAULT_PRICE, 0.0)
                put(AddEditProductUploadConstant.EXTRA_STOCK_TYPE, "")
                put(AddEditProductUploadConstant.EXTRA_IS_OFFICIAL_STORE, false)
                put(AddEditProductUploadConstant.EXTRA_DEFAULT_SKU, "")
                put(AddEditProductUploadConstant.EXTRA_NEED_RETAIN_IMAGE, false)
                put(AddEditProductUploadConstant.EXTRA_PRODUCT_SIZECHART, null) // must using json
                put(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV1, true)
                put(AddEditProductUploadConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV2, false)
                put(AddEditProductUploadConstant.EXTRA_HAS_WHOLESALE, false)
                put(AddEditProductUploadConstant.EXTRA_IS_ADD, false)
            }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
            intent?.run {
                putExtra(AddEditProductUploadConstant.EXTRA_VARIANT_CACHE_ID, cacheManager.id)
                putExtra(AddEditProductUploadConstant.EXTRA_IS_USING_CACHE_MANAGER, true)
                startActivityForResult(this, AddEditProductDescriptionFragment.REQUEST_CODE_VARIANT)
            }
        }
    }
}
