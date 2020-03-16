package com.tokopedia.product.addedit.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.adapter.ProductNameRecAdapter
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify

class AddEditProductDetailFragment(private val initialSelectedImagePathList: ArrayList<String>?)
    : Fragment(), ProductPhotoViewHolder.OnStartDragListener {

    companion object {
        fun createInstance(initialSelectedImagePathList: ArrayList<String>?): Fragment {
            return AddEditProductDetailFragment(initialSelectedImagePathList)
        }

        private const val MAX_PRODUCT_PHOTOS = 5
        private const val REQUEST_CODE_IMAGE = 0x01
    }

    // TODO: remove dummy once the data layer is ready
    private val dummyProductNameRecs: List<String> = listOf(
            "Batik Keris",
            "Batik Mega Mendung",
            "Batik Solo",
            "Batik Bandung",
            "Batik Jawa"
    )

    // product photo
    private var addProductPhotoButton: AppCompatTextView? = null
    private var productPhotosView: RecyclerView? = null
    private var productPhotoAdapter: ProductPhotoAdapter? = null
    private var photoItemTouchHelper: ItemTouchHelper? = null

    // product name
    private var productNameField: TextFieldUnify? = null
    private var productNameRecView: RecyclerView? = null
    private var productNameRecLoader: LoaderUnify? = null
    private var productNameRecShimmering: View? = null
    private var productNameRecAdapter: ProductNameRecAdapter? = null

    // product category
    private var productCategoryLayout: View? = null

    // product catalog
    private var productCatalogLayout: View? = null

    // product price
    private var productPriceField: TextFieldUnify? = null

    // product stock
    private var productStockField: TextFieldUnify? = null
    private var productMinOrderField: TextFieldUnify? = null

    private var continueButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productPhotoPaths = mutableListOf<String>()
        initialSelectedImagePathList?.let { productPhotoPaths.addAll(initialSelectedImagePathList) }

        // add edit product photo views
        addProductPhotoButton = view.findViewById(R.id.tv_add_product_photo)
        productPhotosView = view.findViewById(R.id.rv_product_photos)
        productPhotoAdapter = ProductPhotoAdapter(5, productPhotoPaths, this)
        productPhotosView?.let {
            it.adapter = productPhotoAdapter
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val photoItemTouchHelperCallback = PhotoItemTouchHelperCallback(it)
            photoItemTouchHelper = ItemTouchHelper(photoItemTouchHelperCallback)
            photoItemTouchHelper?.attachToRecyclerView(it)
        }

        // add edit product name views
        productNameField = view.findViewById(R.id.tfu_product_name)
        productNameRecView = view.findViewById(R.id.rv_product_name_rec)
        productNameRecShimmering = view.findViewById(R.id.product_name_rec_shimmering)
        productNameRecLoader = view.findViewById(R.id.lu_product_name)
        productNameRecAdapter = ProductNameRecAdapter()
        productNameRecView?.let {
            it.adapter = productNameRecAdapter
            it.layoutManager = LinearLayoutManager(context)
        }

        // add edit product category views
        productCategoryLayout = view.findViewById(R.id.add_edit_product_category_layout)
        // TODO: refactor the radio buttons to list : unify component
        // TODO: need to integrate the category picker from manage product item

        // add edit product catalog views
        productCatalogLayout = view.findViewById(R.id.add_edit_product_catalog_layout)
        // TODO: need to integrate the catalog picker from manage product item

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)

        // add edit product wholesale views
        // TODO: refactor the wholesale views from rv to Text Fields

        // add edit product stock views
        productStockField = view.findViewById(R.id.tfu_available_stock)
        productMinOrderField = view.findViewById(R.id.tfu_minimum_order)

        // add edit product preOrder state views
        // TODO: add product preOrder validation
        // TODO: bottom sheet for preOrder duration picker

        // add edit product condition views
        // TODO: refactor the radio buttons to list : unify component

        // add edit product sku views
        // TODO: setup the view ref to retrieve the editable value

        continueButton = view.findViewById(R.id.btn_continue)

        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())

        productNameField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {
                // TODO: remove dummy simulation once the real implementation is ready
                productNameRecAdapter?.setProductNameInput(productNameField?.getEditableValue().toString())
                productNameRecAdapter?.setProductNameRecommendations(dummyProductNameRecs)

                // TODO: move all the view state to view model - UT
                productNameRecLoader?.visibility = View.VISIBLE
                productNameRecShimmering?.visibility = View.VISIBLE

                // TODO: remove dummy simulation once the real implementation is ready
                Handler().postDelayed({
                    productNameRecLoader?.visibility = View.GONE
                    productNameRecShimmering?.visibility = View.GONE
                    productNameRecView?.visibility = View.VISIBLE
                }, 1000)
            }
        }

        productNameField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {
                val productNameLength = editable?.length
                productNameLength?.let {

                    // TODO: move all the view state to view model - UT
                    if (it > 0) {
                        productCategoryLayout?.visibility = View.VISIBLE
                        productCatalogLayout?.visibility = View.VISIBLE
                    } else {
                        productCategoryLayout?.visibility = View.GONE
                        productCatalogLayout?.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO: move all the view state to view model - UT
                val isTextChanged = start != before
                val isRecommendationsVisible = productNameRecView?.visibility == View.VISIBLE
                if (isTextChanged && isRecommendationsVisible) productNameRecView?.visibility = View.GONE
            }
        })

        continueButton?.setOnClickListener {

            // TODO: move product name validation to view model - UT
            // product name validation
            val productNameEditable = productNameField?.getEditableValue()
            productNameEditable?.let {
                if (it.isNotEmpty()) {
                    val productName = productNameField?.getEditableValue().toString()
                    if (productName == "Addidas") productNameField?.textFieldInput?.error = getString(R.string.error_product_name_exist)
                    if (productName == "Shopee") productNameField?.textFieldInput?.error = getString(R.string.error_restricted_product_name)
                } else {
                    productNameField?.textFieldInput?.error = getString(R.string.error_empty_product_name)
                }
            }

            // TODO: move product price validation to view model - UT
            // product price validation
            val productPriceEditable = productPriceField?.getEditableValue()
            productPriceEditable?.let {
                if (it.isNotEmpty()) {
                    val productPrice = productPriceField?.getEditableValue().toString().toInt()
                    if (productPrice > 500000000) productPriceField?.textFieldInput?.error = getString(R.string.error_product_price_exceeding_max_limit)
                    if (productPrice < 100) productPriceField?.textFieldInput?.error = getString(R.string.error_product_price_less_than_min_limit)
                } else {
                    productPriceField?.textFieldInput?.error = getString(R.string.error_empty_product_price)
                }
            }

            // TODO: move product stock validation to view model - UT
            // product stock validation
            val productStockEditable = productStockField?.getEditableValue()
            productStockEditable?.let {
                if (it.isNotEmpty()) {
                    val productStock = productStockField?.getEditableValue().toString().toInt()
                    if (productStock > 999999) productStockField?.textFieldInput?.error = getString(R.string.error_available_stock_quantity_exceeding_max_limit)
                    if (productStock == 0) productStockField?.textFieldInput?.error = getString(R.string.error_available_stock_quantity_cant_be_zero)
                } else {
                    productStockField?.textFieldInput?.error = getString(R.string.error_empty_product_price)
                }
            }

            // TODO: move minimum order validation to view model - UT
            // product minimum order validation
            val productMinOrderEditable = productMinOrderField?.getEditableValue()
            productMinOrderEditable?.let {
                if (it.isNotEmpty()) {
                    val productStock = productStockField?.getEditableValue().toString().toInt()
                    val productMinOrder = productMinOrderField?.getEditableValue().toString().toInt()
                    if (productMinOrder > productStock) productMinOrderField?.textFieldInput?.error = getString(R.string.error_minimum_order_cant_exceed_available_stock)
                    if (productMinOrder == 0) productMinOrderField?.textFieldInput?.error = getString(R.string.error_minimum_order_cant_be_zero)
                }
            }

            // TODO: navigate to add product description once the validation is passed
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                // TODO: need a bug fix in the adding image process
                val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    imageUrlOrPathList.forEach {
                        productPhotoAdapter?.addItem(it)
                    }
                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            // TODO: move all validation to view model - UT
            if (productPhotoAdapter?.itemCount != MAX_PRODUCT_PHOTOS) {
                val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder())
                startActivityForResult(intent, REQUEST_CODE_IMAGE)
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
                initialSelectedImagePathList,
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
}
