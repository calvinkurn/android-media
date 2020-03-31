package com.tokopedia.product.addedit.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.getTextFloatOrZero
import com.tokopedia.product.addedit.common.util.getTextIntOrZero
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment.Companion.REQUEST_CODE_DESCRIPTION
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
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
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.ProductCatalog
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddEditProductDetailFragment(private val initialSelectedImagePathList: ArrayList<String>?)
    : BaseDaggerFragment(), ProductPhotoViewHolder.OnStartDragListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener {

    companion object {

        private const val EXTRA_PRODUCT_NAME = "product_name"
        private const val EXTRA_CATEGORY_ID = "category_id"
        private const val EXTRA_JSON_CATALOG = "json_catalog"

        fun createInstance(initialSelectedImagePathList: ArrayList<String>?): Fragment {
            return AddEditProductDetailFragment(initialSelectedImagePathList)
        }

        fun getDurationUnit(type: Int) =
                when (type) {
                    UNIT_DAY -> R.string.label_day
                    UNIT_WEEK -> R.string.label_week
                    else -> -1
                }

        const val REQUEST_CODE_DETAIL = 0x02
    }

    //Last typing product name
    private val delayLastTextEdit = 1000L
    private var lastTextEdit = 0L
    private val handlerTypingProductName = Handler()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AddEditProductDetailViewModel

    private var productPhotoPaths = mutableListOf<String>()

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
    private var isProductConditionNew = true

    // product sku
    private var productSkuField: TextFieldUnify? = null

    // button lanjut
    private var continueButton: Button? = null

    override fun getScreenName(): String {
        return getString(R.string.product_add_edit_detail)
    }

    override fun initInjector() {
        getComponent(AddEditProductDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AddEditProductDetailViewModel::class.java)

        // store the selected image paths from previous activity
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

        // add edit product category views
        productCategoryLayout = view.findViewById(R.id.add_edit_product_category_layout)
        productCategoryRecListView = view.findViewById(R.id.lvu_product_category_rec)
        productCategoryPickerButton = view.findViewById(R.id.tv_category_picker_button)
        productCategoryPickerButton?.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
            startActivityForResult(intent, REQUEST_CODE_GET_CATEGORY)
        }

        // add edit product catalog views
        productCatalogLayout = view.findViewById(R.id.add_edit_product_catalog_layout)
        productCatalogPickerButton = view.findViewById(R.id.tv_add_product_picker_button)
        productCatalogPickerButton?.setOnClickListener {

            // intent for catalog picker
            val openCatalogPickerIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_OPEN_CATALOG_PICKER)

            // json selected catalog
            var jsonSelectedCatalog = Gson().toJson(ProductCatalog())
            val selectedCatalogObj = productCategoryPickerButton?.getTag(R.id.selected_catalog)
            selectedCatalogObj?.let { jsonSelectedCatalog = Gson().toJson(it as ProductCatalog) }
            openCatalogPickerIntent.putExtra(EXTRA_JSON_CATALOG, jsonSelectedCatalog)

            // product name
            val productName = productNameField?.textFieldInput?.editableText.toString()
            openCatalogPickerIntent.putExtra(EXTRA_PRODUCT_NAME, productName)

            // category id
            var categoryId = -1L
            val categoryIdObj = productCategoryPickerButton?.getTag(R.id.category_id)
            categoryIdObj?.let { categoryId = it.toString().toLong() }
            openCatalogPickerIntent.putExtra(EXTRA_CATEGORY_ID, categoryId)

            startActivityForResult(openCatalogPickerIntent, REQUEST_CODE_GET_CATALOG)
        }

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)

        // add edit product wholesale views
        productWholeSaleSwitch = view.findViewById(R.id.su_wholesale)
        productWholeSaleInputLayout = view.findViewById(R.id.wholesale_input_layout)
        productWholeSaleInputFormsView = view.findViewById(R.id.rv_wholesale_input_forms)
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter(this)
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
        // set input type no suggestion to prevent red underline on text
        preOrderDurationUnitField?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

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
                isProductConditionNew = true
            }
            secondHandCondition.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) newCondition.listRightRadiobtn?.isChecked = false
                isProductConditionNew = false
            }
        }

        // add edit product sku views
        productSkuField = view.findViewById(R.id.tfu_sku)

        // button 'lanjut'
        continueButton = view.findViewById(R.id.btn_continue)

        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())

        productNameField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            //
//            val productNameInput = productNameField?.getEditableValue().toString()
//
//            if (!hasFocus) {
//                // TODO: refactor this code once the integration code is ready
//                onLoadingNameSuggestion()
//                productNameRecAdapter?.setProductNameInput(productNameInput)
////                productNameRecAdapter?.setProductNameRecommendations(dummyProductNameRecs)
//                viewModel.getSearchNameSuggestion(query = productNameInput)
//
////                Handler().postDelayed({
////
////                    productCategoryRecListView?.setData(productCategoryRecs)
////                }, 1000)
//            }
//        }

//            if (!hasFocus) {
//                productNameRecAdapter?.setProductNameInput(productNameInput)
//                productNameRecAdapter?.setProductNameRecommendations(dummyProductNameRecs)
//                productNameRecLoader?.visibility = View.VISIBLE
//                productNameRecShimmering?.visibility = View.VISIBLE
//                viewModel.getCategoryRecommendation(productNameInput)
//            }
        }


        // product name text change listener
        productNameField?.textFieldInput?.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(editable: Editable) {
                        viewModel.validateProductNameInput(editable.toString())
                        lastTextEdit = System.currentTimeMillis()
                    }

                    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

                        //TODO requires a refactor without delay and investigation without delay can be forced close due to ListUnify
                        handlerTypingProductName.removeCallbacks(productNameFieldFinishChecker())

                        // hide recommendations if the text input is changed
                        val isTextChanged = start != before
                        if (isTextChanged) hideRecommendations()
                    }
                })

        // product price text change listener
        productPriceField?.textFieldInput?.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        // clean any kind of number formatting here
                        val productPriceInput = charSequence?.toString()?.replace(".", "")
                        productPriceInput?.let {
                            // do the validation first
                            viewModel.validateProductPriceInput(it)
                            // format the number
                            productPriceField?.textFieldInput?.removeTextChangedListener(this)
                            val formattedText: String = NumberFormat.getNumberInstance(Locale.US).format(it.toLong()).toString().replace(",", ".")
                            productPriceField?.textFieldInput?.setText(formattedText)
                            productPriceField?.textFieldInput?.setSelection(formattedText.length)
                            productPriceField?.textFieldInput?.addTextChangedListener(this)
                        }
                    }
                })

        // product whole sale checked change listener
        productWholeSaleSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isWholeSalePriceActivated.value = isChecked
        }

        // product stock text change listener
        productStockField?.textFieldInput?.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        val productStockInput = charSequence?.toString()
                        productStockInput?.let { viewModel.validateProductStockInput(it) }
                        val orderQuantityInput = productMinOrderField?.textFieldInput?.editableText.toString()
                        orderQuantityInput.let { productStockInput?.let { stockInput -> viewModel.validateProductMinOrderInput(stockInput, it) } }
                    }
                })

        // product minimum order text change listener
        productMinOrderField?.textFieldInput?.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        val productStockInput = productStockField?.textFieldInput?.editableText.toString()
                        val orderQuantityInput = charSequence?.toString()
                        orderQuantityInput?.let { viewModel.validateProductMinOrderInput(productStockInput, it) }
                        productStockInput.let { viewModel.validateProductStockInput(it) }
                    }
                })

        // pre order checked change listener
        preOrderSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isPreOrderActivated.value = isChecked
        }

        // product pre order duration text change listener
        preOrderDurationField?.textFieldInput?.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        viewModel.isPreOrderActivated.value?.let {
                            if (it) {
                                val preOrderDurationInput = charSequence?.toString()
                                preOrderDurationInput?.let { duration -> viewModel.validatePreOrderDurationInput(selectedDurationPosition, duration) }
                            }
                        }
                    }
                })

        continueButton?.setOnClickListener {

            // input re-validation process in case the user click the button without entering the input

            // product photo validation
            productPhotoAdapter?.let { viewModel.validateProductPhotoInput(it.itemCount) }

            // product name validation
            val productNameInput = productNameField?.getEditableValue().toString()
            viewModel.validateProductNameInput(productNameInput)

            // product price validation
            val productPriceInput = productPriceField?.getEditableValue().toString().replace(".", "")
            viewModel.validateProductPriceInput(productPriceInput)

            // product stock validation
            val productStockInput = productStockField?.getEditableValue().toString()
            viewModel.validateProductStockInput(productStockInput)

            // product minimum order validation
            val orderQuantityInput = productMinOrderField?.getEditableValue().toString()
            viewModel.validateProductMinOrderInput(productStockInput, orderQuantityInput)

            // pre order duration validation
            val preOrderDurationInput = preOrderDurationField?.getEditableValue().toString()
            viewModel.validatePreOrderDurationInput(selectedDurationPosition, preOrderDurationInput)

            val isInputValid = viewModel.isInputValid.value

            isInputValid?.let {
                if (it) {
                    moveToDescriptionActivity()
                }
            }
        }

        subscribeToProductNameInputStatus()
        subscribeToProductPriceInputStatus()
        subscribeToWholeSaleSwitchStatus()
        subscribeToProductStockInputStatus()
        subscribeToOrderQuantityInputStatus()
        subscribeToPreOrderSwitchStatus()
        subscribeToPreOrderDurationInputStatus()
        subscribeToInputStatus()
        subscribeNameSuggestion()
        subscribeToCategoryRecomendation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    productPhotoAdapter?.setProductPhotoPaths(imageUrlOrPathList)
                    productPhotoAdapter?.let { viewModel.validateProductPhotoInput(it.itemCount) }
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_NAME)
                    productCategoryPickerButton?.text = categoryName
                    viewModel.selectedCategoryId = categoryId
                    productCategoryRecListView?.getSelectedCategory()?.listRightRadiobtn?.isChecked = false
                }
                REQUEST_CODE_GET_CATALOG -> {
                    val jsonSelectedCatalog: String? = data.getStringExtra(EXTRA_JSON_CATALOG)
                    jsonSelectedCatalog?.let {
                        val selectedCatalog: ProductCatalog = Gson().fromJson(jsonSelectedCatalog, ProductCatalog::class.java)
                        val selectedCatalogName = selectedCatalog.catalogName
                        viewModel.selectedCatalogId = selectedCatalog.catalogId.toString()
                        if (!TextUtils.isEmpty(selectedCatalogName)) {
                            productCategoryPickerButton?.text = selectedCatalogName
                            productCategoryPickerButton?.setTag(R.id.selected_catalog, selectedCatalog)
                        }
                    }
                }
                REQUEST_CODE_DESCRIPTION -> {
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    val descriptionInputModel =
                            data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                    val variantInputModel =
                            data.getParcelableExtra<ProductVariantInputModel>(EXTRA_VARIANT_INPUT)
                    submitInput(shipmentInputModel, descriptionInputModel, variantInputModel)
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

    override fun onWholeSaleQuantityItemTextChanged(position: Int, input: String) {
        val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
        val quantityField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_quantity)
        val errorMessage = viewModel.validateProductWholeSaleQuantityInput(input)
        quantityField?.setError(errorMessage.isNotEmpty())
        quantityField?.setMessage(errorMessage)
        updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
    }

    override fun onWholeSalePriceItemTextChanged(position: Int, input: String) {
        val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
        val priceField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_price)
        val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
        val errorMessage = viewModel.validateProductWholeSalePriceInput(input, productPriceInput)
        priceField?.setError(errorMessage.isNotEmpty())
        priceField?.setMessage(errorMessage)
        updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
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
        productWholeSaleInputFormsView?.childCount?.let {
            for (index in 0 until it) {
                val productWholeSaleFormView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(index)
                val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)
                val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                val item = WholeSaleInputModel(
                        productWholeSalePriceField.getText(),
                        productWholeSaleQuantityField.getText()
                )
                inputResult.add(item)
            }
        }
        return inputResult
    }

    private fun subscribeToProductNameInputStatus() {
        observe(viewModel.isProductNameInputError) {
            if (it) {
                productNameField?.setError(it)
                productNameField?.setMessage(viewModel.productNameMessage)
            } else {
                //TODO requires a refactor without delay and investigation without delay can be forced close due to ListUnify
                handlerTypingProductName.postDelayed(productNameFieldFinishChecker(productNameField?.getEditableValue().toString()), delayLastTextEdit)
            }
        }
    }

    private fun subscribeToProductPriceInputStatus() {
        viewModel.isProductPriceInputError.observe(this, Observer {
            productPriceField?.setError(it)
            productPriceField?.setMessage(viewModel.productPriceMessage)
        })
    }

    private fun subscribeToWholeSaleSwitchStatus() {
        viewModel.isWholeSalePriceActivated.observe(this, Observer {
            if (it) productWholeSaleInputLayout?.visible()
            else productWholeSaleInputLayout?.hide()
        })
    }

    private fun subscribeToProductStockInputStatus() {
        viewModel.isProductStockInputError.observe(this, Observer {
            productStockField?.setError(it)
            productStockField?.setMessage(viewModel.productStockMessage)
        })
    }

    private fun subscribeToOrderQuantityInputStatus() {
        viewModel.isOrderQuantityInputError.observe(this, Observer {
            productMinOrderField?.setError(it)
            productMinOrderField?.setMessage(viewModel.orderQuantityMessage)
        })
    }

    private fun subscribeToPreOrderSwitchStatus() {
        viewModel.isPreOrderActivated.observe(this, Observer {
            if (it) preOrderInputLayout?.visible()
            else preOrderInputLayout?.hide()
        })
    }

    private fun subscribeToPreOrderDurationInputStatus() {
        viewModel.isPreOrderDurationInputError.observe(this, Observer {
            preOrderDurationField?.setError(it)
            preOrderDurationField?.setMessage(viewModel.preOrderDurationMessage)
        })
    }

    private fun subscribeToInputStatus() {
        viewModel.isInputValid.observe(this, Observer {
            if (it) enableContinueButton()
            else disableContinueButton()
        })
    }

    private fun subscribeToCategoryRecomendation() {
        viewModel.productCategoryRecommendationLiveData.observe(this, Observer {
            when (it) {
                is Success -> onGetCategoryRecommendationSuccess(it)
                is Fail -> onGetCategoryRecommendationFailed()
            }
        })
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            productPhotoAdapter?.let { productPhotoPaths = it.getProductPhotoPaths() }
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(productPhotoPaths)))
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
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

    private fun enableContinueButton() {
        continueButton?.isEnabled = true
        continueButton?.setBackgroundResource(R.drawable.rect_green_solid)
        context?.let { continueButton?.setTextColor(ContextCompat.getColor(it, android.R.color.white)) }
    }

    private fun disableContinueButton() {
        continueButton?.isEnabled = false
        continueButton?.setBackgroundResource(R.drawable.rect_grey_solid)
        context?.let { continueButton?.setTextColor(ContextCompat.getColor(it, R.color.Neutral_N700_32)) }
    }

    private fun hideRecommendations() {
        productNameRecView?.hide()
        productCategoryLayout?.hide()
        productCatalogLayout?.hide()
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
                val preOrderDurationInput = preOrderDurationField?.textFieldInput?.editableText.toString()
                viewModel.validatePreOrderDurationInput(selectedDurationPosition, preOrderDurationInput)
            }
        }
    }

    private fun moveToDescriptionActivity() {
        val categoryId = viewModel.selectedCategoryId
        val intent = AddEditProductDescriptionActivity.createInstance(context)
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId.toString())
        startActivityForResult(intent, REQUEST_CODE_DESCRIPTION)
    }

    private fun subscribeNameSuggestion() {
        observe(viewModel.searchProductSuggestionName) {
            when (it) {
                is Success -> {
                    productNameRecAdapter?.setProductNameRecommendations(it.data)
                    onNameSuggestionSuccess()
                }
                is Fail -> {
                    onNameSuggestionFailed()
                }
            }
        }
    }

    private fun onLoadingNameSuggestion() {
        productNameRecLoader?.visible()
        productNameRecShimmering?.visible()
    }

    private fun onNameSuggestionSuccess() {
        productNameRecLoader?.hide()
        productNameRecShimmering?.hide()
        productNameRecView?.visible()
        productCategoryLayout?.visible()
        productCatalogLayout?.visible()
    }

    private fun onNameSuggestionFailed() {
        productNameRecLoader?.hide()
        productNameRecShimmering?.hide()
        productNameRecView?.hide()
        productCategoryLayout?.hide()
        productCatalogLayout?.hide()
    }

    //TODO requires a refactor without delay and investigation without delay can be forced close due to ListUnify
    fun productNameFieldFinishChecker(productNameInput: String = "") = Runnable {
        if (System.currentTimeMillis() > (lastTextEdit + delayLastTextEdit - 500)) {
            onLoadingNameSuggestion()
            productNameRecAdapter?.setProductNameInput(productNameInput)
            viewModel.getSearchNameSuggestion(query = productNameInput)
            viewModel.getCategoryRecommendation(productNameInput)
        }
    }

    fun isNotAllowingCharacter(productName: String): Boolean {
        val notAllowingChar = charArrayOf('[', '{', '}', '<', '>', '}', ']')
        return productName.indexOfAny(notAllowingChar) >= 0
    }

    private fun onGetCategoryRecommendationSuccess(result: Success<List<ListItemUnify>>) {
        // TODO: remove this 4 lines from here
        productNameRecLoader?.hide()
        productNameRecShimmering?.hide()
        productNameRecView?.show()
        productCatalogLayout?.show()

        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(3))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            (productCategoryRecListView?.adapter?.getItem(0) as? ListItemUnify)?.let {
                it.listRightRadiobtn?.isChecked = true
                viewModel.selectedCategoryId = it.categoryId
            }
            productCategoryRecListView?.setOnItemClickListener { _, _, position, _ ->
                val clickedItem = productCategoryRecListView?.getItemAtPosition(position) as ListItemUnify
                items.filter { it.listRightRadiobtn?.isChecked ?: false }
                        .filterNot { it == clickedItem }
                        .onEach { it.listRightRadiobtn?.isChecked = false }
                clickedItem.listRightRadiobtn?.isChecked = true
                viewModel.selectedCategoryId = clickedItem.categoryId
            }
        }
    }

    private fun onGetCategoryRecommendationFailed() {
        // TODO: remove this 4 lines from here
        productNameRecLoader?.hide()
        productNameRecShimmering?.hide()
        productNameRecView?.show()
        productCatalogLayout?.show()

        productCategoryLayout?.show()
        productCategoryRecListView?.hide()
    }

    private fun submitInput(shipmentInputModel: ShipmentInputModel,
                            descriptionInputModel: DescriptionInputModel,
                            variantInputModel: ProductVariantInputModel) {
        val detailInputModel = DetailInputModel(
                productNameField.getText(),
                viewModel.selectedCategoryId.toString(),
                viewModel.selectedCatalogId,
                productPriceField.getTextFloatOrZero(),
                productStockField.getTextIntOrZero(),
                productMinOrderField.getTextIntOrZero(),
                if (isProductConditionNew) "NEW" else "USED",
                productSkuField.getText(),
                productPhotoPaths,
                PreorderInputModel(
                        preOrderDurationField.getTextIntOrZero(),
                        selectedDurationPosition,
                        preOrderSwitch?.isChecked ?: false
                ),
                getWholesaleInput()
        )

        val intent = Intent()
        intent.putExtra(EXTRA_DETAIL_INPUT, detailInputModel)
        intent.putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
        intent.putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
        intent.putExtra(EXTRA_VARIANT_INPUT, variantInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    // category id is saved on listActionText property
    private val ListItemUnify.categoryId: Long
        get() = listActionText?.toLong().orZero()

    @Suppress("UNCHECKED_CAST")
    private fun ListUnify.getSelectedCategory(): ListItemUnify? {
        ListUnify::class.java.getDeclaredField("array").let {
            it.isAccessible = true
            return (it.get(productCategoryRecListView) as ArrayList<ListItemUnify>).find {
                it.listRightRadiobtn?.isChecked ?: false
            }
        }
    }
}
