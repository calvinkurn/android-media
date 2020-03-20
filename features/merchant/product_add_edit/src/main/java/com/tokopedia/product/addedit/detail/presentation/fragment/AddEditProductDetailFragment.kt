package com.tokopedia.product.addedit.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
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
import com.google.gson.Gson
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import com.tokopedia.product.addedit.detail.presentation.adapter.WholeSalePriceInputAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_GET_CATALOG
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_GET_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.model.ProductCatalog
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

class AddEditProductDetailFragment(private val initialSelectedImagePathList: ArrayList<String>?)
    : Fragment(), ProductPhotoViewHolder.OnStartDragListener, NameRecommendationAdapter.ProductNameItemClickListener {

    companion object {
        private const val EXTRA_JSON_CATALOG = "EXTRA_JSON_CATALOG"
        private const val EXTRA_PRODUCT_NAME = "product_name"
        private const val EXTRA_CATEGORY_ID = "category_id"

        fun createInstance(initialSelectedImagePathList: ArrayList<String>?): Fragment {
            return AddEditProductDetailFragment(initialSelectedImagePathList)
        }

        fun getDurationUnit(type: Int) =
                when (type) {
                    UNIT_DAY -> R.string.label_day
                    UNIT_WEEK -> R.string.label_week
                    else -> -1
                }
    }

    private val productPhotoPaths = mutableListOf<String>()

    // TODO: remove dummy once the data layer is ready
    private val dummyProductNameRecs: List<String> = listOf(
            "Batik Keris",
            "Batik Mega Mendung",
            "Batik Solo",
            "Batik Bandung",
            "Batik Jawa"
    )

    private var selectedDurationPosition: Int = UNIT_DAY

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
    private var productNameRecAdapter: NameRecommendationAdapter? = null

    // product category
    private var productCategoryLayout: ViewGroup? = null
    private var productCategoryRecListView: ListUnify? = null
    private var productCategoryPickerButton: AppCompatTextView? = null

    // product catalog
    private var productCatalogLayout: ViewGroup? = null
    private var productCatalogPickerButton: AppCompatTextView? = null

    // product price
    private var productPriceField: TextFieldUnify? = null

    // product wholesale price
    private var productWholeSaleSwitch: SwitchUnify? = null
    private var productWholeSaleInputLayout: ViewGroup? = null
    private var productWholeSaleInputFormsView: RecyclerView? = null
    private var wholeSaleInputFormsAdapter: WholeSalePriceInputAdapter? = null
    private var addNewWholeSalePriceButton: AppCompatTextView? = null
    private var wholeSaleErrorCounter = 0

    // product stock
    private var productStockField: TextFieldUnify? = null
    private var productMinOrderField: TextFieldUnify? = null

    // product pre order
    private var preOrderSwitch: SwitchUnify? = null
    private var preOrderInputLayout: ViewGroup? = null
    private var preOrderDurationField: TextFieldUnify? = null
    private var preOrderDurationUnitField: TextFieldUnify? = null

    // product conditions
    private var productConditionListView: ListUnify? = null

    // product sku
    private var productSkuField: TextFieldUnify? = null

    private var continueButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSelectedImagePathList?.let { productPhotoPaths.addAll(initialSelectedImagePathList) }

        // add edit product photo views
        addProductPhotoButton = view.findViewById(R.id.tv_add_product_photo)
        productPhotosView = view.findViewById(R.id.rv_product_photos)
        productPhotoAdapter = ProductPhotoAdapter(MAX_PRODUCT_PHOTOS, productPhotoPaths, this)
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
        productNameRecAdapter = NameRecommendationAdapter(this)
        productNameRecView?.let {
            it.adapter = productNameRecAdapter
            it.layoutManager = LinearLayoutManager(context)
        }

        // TODO: need to integrate the category picker from manage product item
        // add edit product category views
        productCategoryLayout = view.findViewById(R.id.add_edit_product_category_layout)
        productCategoryRecListView = view.findViewById(R.id.lvu_product_category_rec)
        productCategoryPickerButton = view.findViewById(R.id.tv_category_picker_button)
        productCategoryPickerButton?.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
            startActivityForResult(intent, REQUEST_CODE_GET_CATEGORY)
        }
        val productCategoryRecs = ArrayList<ListItemUnify>()
        // first category recommendation
        val firstCategoryRec = ListItemUnify("Fashion Wanita/Batik Wanita/Dress", "")
        firstCategoryRec.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        productCategoryRecs.add(firstCategoryRec)
        // second category recommendation
        val secondCategoryRec = ListItemUnify("Fashion Wanita/Batik Wanita/Dress", "")
        secondCategoryRec.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        productCategoryRecs.add(secondCategoryRec)
        // third category recommendation
        val thirdCategoryRec = ListItemUnify("Fashion Wanita/Batik Wanita/Dress", "")
        thirdCategoryRec.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        productCategoryRecs.add(thirdCategoryRec)

        productCategoryRecListView?.onLoadFinish {
            firstCategoryRec.listRightRadiobtn?.isChecked = true
            firstCategoryRec.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    secondCategoryRec.listRightRadiobtn?.isChecked = false
                    thirdCategoryRec.listRightRadiobtn?.isChecked = false
                }
            }
            secondCategoryRec.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    firstCategoryRec.listRightRadiobtn?.isChecked = false
                    thirdCategoryRec.listRightRadiobtn?.isChecked = false
                }
            }
            thirdCategoryRec.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    firstCategoryRec.listRightRadiobtn?.isChecked = false
                    secondCategoryRec.listRightRadiobtn?.isChecked = false
                }
            }
        }

        // TODO: need to integrate the catalog picker from manage product item
        // add edit product catalog views
        productCatalogLayout = view.findViewById(R.id.add_edit_product_catalog_layout)
        productCatalogPickerButton = view.findViewById(R.id.tv_add_product_picker_button)
        productCatalogPickerButton?.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_OPEN_CATALOG_PICKER)
            val productName = productNameField?.textFieldInput?.editableText.toString()
            val categoryIdObj = productCategoryPickerButton?.getTag(R.id.category_id)
            categoryIdObj?.let { intent.putExtra(EXTRA_CATEGORY_ID, it.toString().toLong()) }
            intent.putExtra(EXTRA_PRODUCT_NAME, productName)
            startActivityForResult(intent, REQUEST_CODE_GET_CATALOG)
        }

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)

        // add edit product wholesale views
        productWholeSaleSwitch = view.findViewById(R.id.su_wholesale)
        productWholeSaleInputLayout = view.findViewById(R.id.wholesale_input_layout)
        productWholeSaleInputFormsView = view.findViewById(R.id.rv_wholesale_input_forms)
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter()
        productWholeSaleInputFormsView?.apply {
            adapter = wholeSaleInputFormsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
        addNewWholeSalePriceButton = view.findViewById(R.id.tv_add_new_wholesale_price)
        addNewWholeSalePriceButton?.setOnClickListener {
            wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
        }

        // add edit product stock views
        productStockField = view.findViewById(R.id.tfu_available_stock)
        productMinOrderField = view.findViewById(R.id.tfu_minimum_order)

        // add edit product pre order state views
        preOrderSwitch = view.findViewById(R.id.switch_preorder)
        preOrderInputLayout = view.findViewById(R.id.preorder_input_layout)
        preOrderDurationField = view.findViewById(R.id.tfu_duration)
        preOrderDurationUnitField = view.findViewById(R.id.tfu_duration_unit)

        preOrderSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) preOrderInputLayout?.visibility = View.VISIBLE
            else preOrderInputLayout?.visibility = View.GONE
        }

        preOrderDurationUnitField?.apply {
            textFieldInput.setText(getDurationUnit(UNIT_DAY))
            textFieldInput.isFocusable = false
            textFieldInput.isActivated = false
            textFieldInput.setOnClickListener {
                showDurationUnitOption()
            }
        }

        // add edit product conditions views
        productConditionListView = view.findViewById(R.id.lvu_product_conditions)
        val productConditions = ArrayList<ListItemUnify>()
        // new condition
        val newCondition = ListItemUnify(getString(R.string.label_new), "")
        newCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        productConditions.add(newCondition)
        // secondhand condition
        val secondHandCondition = ListItemUnify(getString(R.string.label_secondhand), "")
        secondHandCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, getString(R.string.label_secondhand))
        productConditions.add(secondHandCondition)
        // add new and secondhand condition to the view
        productConditionListView?.setData(productConditions)
        productConditionListView?.onLoadFinish {
            newCondition.listRightRadiobtn?.isChecked = true
            newCondition.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) secondHandCondition.listRightRadiobtn?.isChecked = false
            }
            secondHandCondition.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) newCondition.listRightRadiobtn?.isChecked = false
            }
        }

        // add edit product sku views
        productSkuField = view.findViewById(R.id.tfu_sku)

        // button 'lanjut'
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
                    productCategoryLayout?.visibility = View.VISIBLE
                    productCatalogLayout?.visibility = View.VISIBLE

                    productCategoryRecListView?.setData(productCategoryRecs)
                }, 1000)
            }
        }

        productNameField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO: move all the view state to view model - UT
                val isTextChanged = start != before
                val isRecommendationsVisible = productNameRecView?.visibility == View.VISIBLE
                if (isTextChanged && isRecommendationsVisible) {
                    productNameRecView?.visibility = View.GONE
                    productCategoryLayout?.visibility = View.GONE
                    productCatalogLayout?.visibility = View.GONE
                }
            }
        })

        productWholeSaleSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) productWholeSaleInputLayout?.visible()
            else productWholeSaleInputLayout?.hide()
        }

        continueButton?.setOnClickListener {

            var isProductPhotoValid = false
            var isProductNameValid = false
            var isProductPriceValid = false
            var isProductWholeSaleActivated = false
            var isProductStockValid = false
            var isProductMinimumOrderValid = false
            var isPreOrderActivated = false
            var isPreOrderDurationValid = false

            productWholeSaleSwitch?.let { isProductWholeSaleActivated = it.isChecked }

            preOrderSwitch?.let { isPreOrderActivated = it.isChecked }

            // TODO: move product photo validation to view model - UT
            // product photo validation
            isProductPhotoValid = productPhotoAdapter?.itemCount != 0

            // TODO: move product name validation to view model - UT
            // product name validation
            val productNameEditable = productNameField?.getEditableValue()
            productNameEditable?.let {
                if (it.isNotEmpty()) {
                    when (productNameField?.getEditableValue().toString()) {
                        "Addidas" -> {
                            isProductNameValid = false
                            productNameField?.textFieldInput?.error = getString(R.string.error_product_name_exist)
                        }
                        "Shopee" -> {
                            isProductNameValid = false
                            productNameField?.textFieldInput?.error = getString(R.string.error_restricted_product_name)
                        }
                        else -> isProductNameValid = true
                    }
                } else {
                    isProductNameValid = false
                    productNameField?.textFieldInput?.error = getString(R.string.error_empty_product_name)
                }
            }

            // TODO: move product price validation to view model - UT
            // product price validation
            val productPriceEditable = productPriceField?.getEditableValue()
            productPriceEditable?.let {
                if (it.isNotEmpty()) {
                    // TODO: move all the limit to int resources
                    val productPrice = productPriceField?.getEditableValue().toString().toInt()
                    when {
                        productPrice > 500000000 -> {
                            isProductPriceValid = false
                            productPriceField?.textFieldInput?.error = getString(R.string.error_product_price_exceeding_max_limit)
                        }
                        productPrice < 100 -> {
                            isProductPriceValid = false
                            productPriceField?.textFieldInput?.error = getString(R.string.error_product_price_less_than_min_limit)
                        }
                        else -> {
                            isProductPriceValid = true
                        }
                    }
                } else {
                    isProductPriceValid = false
                    productPriceField?.textFieldInput?.error = getString(R.string.error_empty_product_price)
                }
            }

            // TODO: move product wholesale validation to view model - UT
            productWholeSaleInputFormsView?.childCount?.let {

                wholeSaleErrorCounter = 0

                for (index in 0 until it) {

                    val productWholeSaleFormView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(index)
                    val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                    val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)

                    // TODO: move product wholesale price validation to view model - UT
                    val quantityEditable = productWholeSaleQuantityField?.getEditableValue()
                    quantityEditable?.let { editable ->
                        if (isProductWholeSaleActivated) {
                            if (editable.isNotEmpty()) {
                                val productWholeSaleQuantity = editable.toString().toInt()
                                if (productWholeSaleQuantity <= 1) {
                                    productWholeSaleQuantityField.textFieldInput.error = getString(R.string.error_wholesale_quantity_less_than_two)
                                    wholeSaleErrorCounter++
                                }
                            } else {
                                productWholeSaleQuantityField.textFieldInput.error = getString(R.string.error_empty_wholesale_quantity)
                                wholeSaleErrorCounter++
                            }
                        }
                    }

                    // TODO: move product wholesale price validation to view model - UT
                    // product wholesale price validation
                    val productWholeSalePriceEditable = productWholeSalePriceField?.getEditableValue()
                    productWholeSalePriceEditable?.let { editable ->
                        if (isProductWholeSaleActivated) {
                            if (editable.isNotEmpty()) {
                                val productPrice = productPriceField?.getEditableValue().toString().toInt()
                                val productWholesalePrice = editable.toString().toInt()
                                when {
                                    productWholesalePrice == 0 -> {
                                        productWholeSalePriceField.textFieldInput.error = getString(R.string.error_wholesale_price_cant_be_zero)
                                        wholeSaleErrorCounter++
                                    }
                                    productWholesalePrice > productPrice -> {
                                        productWholeSalePriceField.textFieldInput.error = getString(R.string.error_wholesale_price_too_expensive)
                                        wholeSaleErrorCounter++
                                    }
                                    productWholesalePrice > 500000000 -> {
                                        productWholeSalePriceField.textFieldInput.error = getString(R.string.error_wholesale_price_exceeding_max_limit)
                                        wholeSaleErrorCounter++
                                    }
                                }
                            } else {
                                productWholeSalePriceField.textFieldInput.error = getString(R.string.error_empty_wholesale_price)
                                wholeSaleErrorCounter++
                            }
                        }
                    }
                }
            }

            // TODO: move product stock validation to view model - UT
            // product stock validation
            val productStockEditable = productStockField?.getEditableValue()
            productStockEditable?.let {
                if (it.isNotEmpty()) {
                    // TODO: move all the limit to int resources
                    val productStock = productStockField?.getEditableValue().toString().toInt()
                    when {
                        productStock > 999999 -> {
                            isProductStockValid = false
                            productStockField?.textFieldInput?.error = getString(R.string.error_available_stock_quantity_exceeding_max_limit)
                        }
                        productStock == 0 -> {
                            isProductStockValid = false
                            productStockField?.textFieldInput?.error = getString(R.string.error_minimum_stock_quantity_is_one)
                        }
                        else -> isProductStockValid = true
                    }
                } else {
                    isProductStockValid = false
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
                    when {
                        productMinOrder > productStock -> {
                            isProductMinimumOrderValid = false
                            productMinOrderField?.textFieldInput?.error = getString(R.string.error_minimum_order_cant_exceed_available_stock)
                        }
                        productMinOrder == 0 -> {
                            isProductMinimumOrderValid = false
                            productMinOrderField?.textFieldInput?.error = getString(R.string.error_minimum_order_cant_be_zero)
                        }
                        else -> isProductMinimumOrderValid = true
                    }
                } else {
                    isProductMinimumOrderValid = false
                    productMinOrderField?.textFieldInput?.error = getString(R.string.error_empty_minimum_order)
                }
            }

            // TODO: move pre order duration validation to view model - UT
            val preOrderDurationEditable = preOrderDurationField?.getEditableValue()
            preOrderDurationEditable?.let {
                if (isPreOrderActivated) {
                    if (it.isNotEmpty()) {
                        val isDayUnit = selectedDurationPosition == UNIT_DAY
                        val isWeekUnit = selectedDurationPosition == UNIT_WEEK
                        val duration = preOrderDurationField?.getEditableValue().toString().toInt()
                        if (duration < 1) {
                            isPreOrderDurationValid = false
                            preOrderDurationField?.textFieldInput?.error = getString(R.string.error_preorder_duration_minimum_is_one)
                        } else if (isDayUnit && duration > 90) {
                            isPreOrderDurationValid = false
                            preOrderDurationField?.textFieldInput?.error = getString(R.string.error_preorder_duration_cant_exceed_ninety_days)
                        } else if (isWeekUnit && duration > 13) {
                            isPreOrderDurationValid = false
                            preOrderDurationField?.textFieldInput?.error = getString(R.string.error_preorder_duration_cant_exceed_thirteen_weeks)
                        } else {
                            isPreOrderDurationValid = true
                        }
                    } else {
                        isPreOrderDurationValid = false
                        preOrderDurationField?.textFieldInput?.error = getString(R.string.error_preorder_duration_cant_be_empty)
                    }
                }
            }

            var isInputValid = isProductPhotoValid && isProductNameValid && isProductPriceValid && isProductStockValid && isProductMinimumOrderValid

            if (isPreOrderActivated) {
                if (!isPreOrderDurationValid) {
                    isInputValid = false
                }
            }

            if (isProductWholeSaleActivated) {
                if (wholeSaleErrorCounter > 0) {
                    isInputValid = false
                }
            }

            if (isInputValid) {
                val intent = Intent(context, AddEditProductDescriptionActivity::class.java)
                startActivity((intent))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {

            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                        imageUrlOrPathList.forEach {
                            productPhotoAdapter?.addItem(it)
                        }
                    }
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_NAME)
                    productCategoryPickerButton?.text = categoryName
                    productCategoryPickerButton?.setTag(R.id.category_id, categoryId)

                    productCategoryRecListView?.let {
                    }
                }

                // TODO: need adjustment / validation when the catalog picker return appropriate result
                REQUEST_CODE_GET_CATALOG -> {
                    val jsonCatalog = data.getStringExtra(EXTRA_JSON_CATALOG)
                    val catalog: ProductCatalog = Gson().fromJson(jsonCatalog, ProductCatalog::class.java)
                    val catalogName = catalog.catalogName
                    if (!TextUtils.isEmpty(catalogName)) productCategoryPickerButton?.text = catalogName
                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
    }

    override fun onNameItemClicked(productName: String) {
        productNameField?.textFieldInput?.setText(productName)
        productCategoryLayout?.visible()
        productCatalogLayout?.visible()
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            // TODO: move all validation to view model - UT
            if (productPhotoAdapter?.itemCount != MAX_PRODUCT_PHOTOS) {
                val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(productPhotoPaths)))
                startActivityForResult(intent, REQUEST_CODE_IMAGE)
            }
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

    private fun showDurationUnitOption() {

        fragmentManager?.let {
            val optionPicker = OptionPicker()
            val title = getString(R.string.label_duration)
            val options: ArrayList<String> = ArrayList()
            options.add(getString(getDurationUnit(UNIT_DAY)))
            options.add(getString(getDurationUnit(UNIT_WEEK)))

            optionPicker.apply {
                setSelectedPosition(selectedDurationPosition)
                setDividerVisible(true)
                setTitle(title)
                setItemMenuList(options)
                show(it, null)
            }

            optionPicker.setOnItemClickListener { selectedText: String, selectedPosition: Int ->
                preOrderDurationUnitField?.textFieldInput?.setText(selectedText)
                selectedDurationPosition = selectedPosition
                resetPreOrderDurationField()
            }
        }
    }

    private fun resetPreOrderDurationField() {
        preOrderDurationField?.apply {
            setError(false)
        }
    }
}
