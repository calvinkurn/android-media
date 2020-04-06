package com.tokopedia.product.addedit.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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
import com.tokopedia.product.addedit.common.util.getTextIntOrZero
import com.tokopedia.product.addedit.common.util.getTextLongOrZero
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment.Companion.REQUEST_CODE_DESCRIPTION
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import com.tokopedia.product.addedit.detail.presentation.adapter.WholeSalePriceInputAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_USED
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.NEW_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.USED_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddEditProductDetailFragment : BaseDaggerFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener {

    companion object {
        fun createInstance(productInputModel: ProductInputModel, isEditing: Boolean, isDrafting: Boolean): Fragment {
            return AddEditProductDetailFragment().apply {
                val args = Bundle()
                args.putParcelable(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                args.putBoolean(EXTRA_IS_EDITING_PRODUCT, isEditing)
                args.putBoolean(EXTRA_IS_DRAFTING_PRODUCT, isDrafting)
                arguments = args
            }
        }

        private fun getDurationUnit(type: Int) =
                when (type) {
                    UNIT_DAY -> R.string.label_day
                    UNIT_WEEK -> R.string.label_week
                    else -> -1
                }
    }

    @Inject
    lateinit var viewModel: AddEditProductDetailViewModel

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
    private val productConditions = ArrayList<ListItemUnify>()
    private var isProductConditionNew = true

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    // product sku
    private var productSkuField: TextFieldUnify? = null

    // button lanjut
    private var continueButton: Button? = null

    // button save
    private var saveButton: Button? = null

    override fun getScreenName(): String {
        return getString(R.string.product_add_edit_detail)
    }

    override fun initInjector() {
        getComponent(AddEditProductDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set detail input model
        arguments?.getParcelable<ProductInputModel>(EXTRA_PRODUCT_INPUT_MODEL)?.run {
            viewModel.detailInputModel = this.detailInputModel
            viewModel.selectedCategoryId = this.detailInputModel.categoryId
            viewModel.productPhotoPaths = this.detailInputModel.imageUrlOrPathList.toMutableList()
        }
        // set isEditing status
        arguments?.getBoolean(EXTRA_IS_EDITING_PRODUCT)?.run {
            viewModel.isEditing = this
        }
        // set isDrafting status
        arguments?.getBoolean(EXTRA_IS_DRAFTING_PRODUCT)?.run {
            viewModel.isDrafting = this

            userSession = UserSession(requireContext())
            shopId = userSession.shopId

            if (viewModel.isEditing) {
                ProductEditMainTracking.trackScreen()
            } else {
                ProductAddMainTracking.trackScreen()
            }
        }
    }

    fun onBackPressed() {
        if (viewModel.isEditing) {
            ProductEditMainTracking.trackBack(shopId)
        } else {
            ProductAddMainTracking.trackBack(shopId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add edit product photo views
        addProductPhotoButton = view.findViewById(R.id.tv_add_product_photo)
        productPhotosView = view.findViewById(R.id.rv_product_photos)
        productPhotoAdapter = ProductPhotoAdapter(MAX_PRODUCT_PHOTOS, mutableListOf(), this)
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
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickOtherCategory(shopId)
            } else {
                ProductAddMainTracking.clickOtherCategory(shopId)
            }
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
            startActivityForResult(intent, REQUEST_CODE_CATEGORY)
        }

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)

        // add edit product wholesale views
        productWholeSaleSwitch = view.findViewById(R.id.su_wholesale)
        productWholeSaleInputLayout = view.findViewById(R.id.wholesale_input_layout)
        productWholeSaleInputFormsView = view.findViewById(R.id.rv_wholesale_input_forms)
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter(this, onDeleteWholesale = {
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickRemoveWholesale(shopId)
            } else {
                ProductAddMainTracking.clickRemoveWholesale(shopId)
            }
        }, onAddWholesale = {
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickAddWholesale(shopId)
            } else {
                ProductAddMainTracking.clickAddWholesale(shopId)
            }
        })
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
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickPreorderButton(shopId)
            } else {
                ProductAddMainTracking.clickPreorderButton(shopId)
            }
            if (isChecked) {
                preOrderInputLayout?.visibility = View.VISIBLE
            } else {
                preOrderInputLayout?.visibility = View.GONE
            }
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
        // new condition
        val newCondition = ListItemUnify(getString(R.string.label_new), "")
        newCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        productConditions.add(NEW_PRODUCT_INDEX, newCondition)

        // secondhand condition
        val secondHandCondition = ListItemUnify(getString(R.string.label_secondhand), "")
        secondHandCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, getString(R.string.label_secondhand))
        productConditions.add(USED_PRODUCT_INDEX, secondHandCondition)

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

        // button 'simpan'
        saveButton = view.findViewById(R.id.btn_save)

        // fill the form with detail input model
        fillProductDetailForm(viewModel.detailInputModel)

        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())

        productNameField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val productNameInput = productNameField?.getEditableValue().toString()
                if (productNameInput.isNotBlank()) {
                    productNameRecView?.hide()
                    viewModel.getCategoryRecommendation(productNameInput)
                }
            }
        }

        // product name text change listener
        productNameField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                viewModel.isProductNameChanged = true
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

                val productNameInput = charSequence?.toString()
                productNameInput?.let {
                    viewModel.validateProductNameInput(it)
                }

                // hide category recommendations
                productCategoryRecListView?.hide()

                // hide recommendations if the text input is changed
                val isTextChanged = start != before
                if (isTextChanged) hideRecommendations()
            }
        })

        // product price text change listener
        productPriceField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // clean any kind of number formatting here
                val productPriceInput = charSequence?.toString()?.replace(".", "")
                productPriceInput?.let {
                    // do the validation first
                    viewModel.validateProductPriceInput(it)
                    // format the number
                    productPriceField?.textFieldInput?.removeTextChangedListener(this)
                    val formattedText = formatProductPriceInput(it)
                    productPriceField?.textFieldInput?.setText(formattedText)
                    productPriceField?.textFieldInput?.setSelection(formattedText.length)
                    productPriceField?.textFieldInput?.addTextChangedListener(this)
                }
            }
        })

        // product whole sale checked change listener
        productWholeSaleSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isWholeSalePriceActivated.value = isChecked
            if (isChecked) {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.clickWholesale(shopId)
                } else {
                    ProductAddMainTracking.clickWholesale(shopId)
                }
            }
        }

        // product stock text change listener
        productStockField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val productStockInput = charSequence?.toString()
                productStockInput?.let { viewModel.validateProductStockInput(it) }
                val orderQuantityInput = productMinOrderField?.textFieldInput?.editableText.toString()
                orderQuantityInput.let { productStockInput?.let { stockInput -> viewModel.validateProductMinOrderInput(stockInput, it) } }
            }
        })

        // product minimum order text change listener
        productMinOrderField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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
        preOrderDurationField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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
            validateInput()

            val isInputValid = viewModel.isInputValid.value
            isInputValid?.let {
                if (it) {
                    moveToDescriptionActivity()
                }
            }
        }

        saveButton?.setOnClickListener {
            validateInput()

            val isInputValid = viewModel.isInputValid.value
            isInputValid?.let {
                if (it) {
                    submitInputEdit()
                }
            }
        }

        // switch continue button to save button
        if (viewModel.isEditing) {
            saveButton?.visibility = View.VISIBLE
            continueButton?.visibility = View.GONE
        }

        subscribeToProductNameInputStatus()
        subscribeToProductNameRecommendation()
        subscribeToCategoryRecommendation()
        subscribeToProductPriceInputStatus()
        subscribeToWholeSaleSwitchStatus()
        subscribeToProductStockInputStatus()
        subscribeToOrderQuantityInputStatus()
        subscribeToPreOrderSwitchStatus()
        subscribeToPreOrderDurationInputStatus()
        subscribeToInputStatus()
    }

    private fun validateInput() {
        // input re-validation process in case the user click the button without entering the input

        // product photo validation
        productPhotoAdapter?.let { viewModel.validateProductPhotoInput(it.itemCount) }

        // product name validation
        val productNameInput = productNameField?.getEditableValue().toString()
        // prevent name recommendation from being showed
        viewModel.isProductNameChanged = false
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    productPhotoAdapter?.setProductPhotoPaths(imageUrlOrPathList)
                    productPhotoAdapter?.let {
                        viewModel.validateProductPhotoInput(it.itemCount)
                        viewModel.productPhotoPaths = it.getProductPhotoPaths()
                    }
                }
                REQUEST_CODE_CATEGORY -> {
                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_NAME)
                    productCategoryPickerButton?.text = categoryName
                    viewModel.selectedCategoryId = categoryId.toString()
                    val categoryList = getSelectedCategory()
                    if (categoryList != null) {
                        categoryList.listRightRadiobtn?.isChecked = false
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickSaveOtherCategory(shopId)
                        }
                    } else {
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickBackOtherCategory(shopId)
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
        if (viewModel.isEditing) {
            ProductEditMainTracking.trackDragPhoto(shopId)
        } else {
            ProductAddMainTracking.trackDragPhoto(shopId)
        }
        photoItemTouchHelper?.startDrag(viewHolder)
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        if (viewModel.isEditing) {
            ProductEditMainTracking.trackRemovePhoto(shopId)
        } else {
            ProductAddMainTracking.trackRemovePhoto(shopId)
        }
    }

    override fun onNameItemClicked(productName: String) {
        viewModel.isProductRecommendationSelected = true
        productNameField?.textFieldInput?.setText(productName)
        viewModel.getCategoryRecommendation(productName)
        viewModel.isProductRecommendationSelected = false

        if (!viewModel.isEditing) {
            ProductAddMainTracking.clickProductNameRecom(shopId, productName)
        }
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

    private fun fillProductDetailForm(detailInputModel: DetailInputModel) {

        // product photo
        productPhotoAdapter?.setProductPhotoPaths(detailInputModel.imageUrlOrPathList.toMutableList())

        // product name
        productNameField?.textFieldInput?.setText(detailInputModel.productName)

        // product price
        productPriceField?.textFieldInput?.setText(formatProductPriceInput(detailInputModel.price.toString()))

        // product category
        if (detailInputModel.categoryName.isNotBlank()) {
            productCategoryPickerButton?.text = detailInputModel.categoryName
            productCategoryRecListView?.hide()
            productCategoryPickerButton?.show()
            productCategoryLayout?.show()
        }

        // product wholesale
        val wholesalePriceExist = detailInputModel.wholesaleList.isNotEmpty()
        if (wholesalePriceExist) {
            productWholeSaleSwitch?.isChecked = true
            addNewWholeSalePrice(wholesaleInputs = detailInputModel.wholesaleList)
            productWholeSaleInputLayout?.show()
        }

        // product pre order
        val isPreOrder = detailInputModel.preorder.isActive
        if (isPreOrder) {
            preOrderSwitch?.isChecked = true
            preOrderDurationUnitField?.textFieldInput?.setText(getString(getDurationUnit(detailInputModel.preorder.timeUnit)))
            preOrderDurationField?.textFieldInput?.setText(detailInputModel.preorder.duration)
            preOrderInputLayout?.show()
        }

        // product stock
        productStockField?.textFieldInput?.setText(detailInputModel.stock.toString())

        // product min order
        productMinOrderField?.textFieldInput?.setText(detailInputModel.minOrder.toString())

        // product condition
        val isProductConditionNew = detailInputModel.condition == CONDITION_NEW
        if (isProductConditionNew) productConditions[NEW_PRODUCT_INDEX].listRightRadiobtn?.isChecked = true
        else productConditions[USED_PRODUCT_INDEX].listRightRadiobtn?.isChecked = true

        // product sku
        productSkuField?.textFieldInput?.setText(detailInputModel.sku)

    }

    private fun addNewWholeSalePrice(wholesaleInputs: List<WholeSaleInputModel>) {
        wholeSaleInputFormsAdapter?.addNewWholeSalePrice(wholesaleInputs)
    }

    private fun formatProductPriceInput(productPriceInput: String): String {
        return if (productPriceInput.isNotBlank()) NumberFormat.getNumberInstance(Locale.US).format(productPriceInput.toLong()).replace(",", ".")
        else productPriceInput
    }

    private fun subscribeToProductNameInputStatus() {
        viewModel.isProductNameInputError.observe(this, Observer {
            productNameField?.setError(it)
            productNameField?.setMessage(viewModel.productNameMessage)
            if (!viewModel.isProductRecommendationSelected && viewModel.isProductNameChanged) {
                showProductNameLoadingIndicator()
                val productNameInput = productNameField?.getEditableValue().toString()
                productNameRecAdapter?.setProductNameInput(productNameInput)
                viewModel.getProductNameRecommendation(query = productNameInput)
            }
        })
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

    private fun subscribeToCategoryRecommendation() {
        viewModel.productCategoryRecommendationLiveData.observe(this, Observer {
            when (it) {
                is Success -> onGetCategoryRecommendationSuccess(it)
                is Fail -> onGetCategoryRecommendationFailed()
            }
        })
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val isEditing = viewModel.isEditing
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(viewModel.productPhotoPaths)), isEditing)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
            if (isEditing) {
                ProductEditMainTracking.trackAddPhoto(shopId)
            } else {
                ProductAddMainTracking.trackAddPhoto(shopId)
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
        productCategoryRecListView?.hide()
    }

    private fun showDurationUnitOption() {
        fragmentManager?.let {
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
                show(it, null)

                if (viewModel.isEditing) {
                    ProductEditMainTracking.clickPreorderDropDownMenu(shopId)
                } else {
                    ProductAddMainTracking.clickPreorderDropDownMenu(shopId)
                }
            }

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
    }

    private fun moveToDescriptionActivity() {
        if (viewModel.isEditing) {
            ProductEditMainTracking.clickContinue(shopId)
        } else {
            ProductAddMainTracking.clickContinue(shopId)
        }
        val categoryId = viewModel.selectedCategoryId
        val intent = AddEditProductDescriptionActivity.createInstance(context, categoryId)
        startActivityForResult(intent, REQUEST_CODE_DESCRIPTION)
    }

    private fun subscribeToProductNameRecommendation() {
        observe(viewModel.productNameRecommendations) {
            when (it) {
                is Success -> {
                    productNameRecAdapter?.setProductNameRecommendations(it.data)
                    productNameRecLoader?.hide()
                    productNameRecShimmering?.hide()
                    productNameRecView?.visible()
                }
                is Fail -> {
                    productNameRecLoader?.hide()
                    productNameRecShimmering?.hide()
                    productNameRecView?.hide()
                }
            }
        }
    }

    private fun showProductNameLoadingIndicator() {
        productNameRecLoader?.visible()
        productNameRecShimmering?.visible()
    }

    private fun onGetCategoryRecommendationSuccess(result: Success<List<ListItemUnify>>) {
        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(3))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            (productCategoryRecListView?.adapter?.getItem(0) as? ListItemUnify)?.let {
                it.listRightRadiobtn?.isChecked = true
                viewModel.selectedCategoryId = it.categoryId.toString()
            }
            productCategoryRecListView?.setOnItemClickListener { _, _, position, _ ->
                val clickedItem = productCategoryRecListView?.getItemAtPosition(position) as ListItemUnify
                items.filter { it.listRightRadiobtn?.isChecked ?: false }
                        .filterNot { it == clickedItem }
                        .onEach { it.listRightRadiobtn?.isChecked = false }
                clickedItem.listRightRadiobtn?.isChecked = true
                viewModel.selectedCategoryId = clickedItem.categoryId.toString()

                if (!viewModel.isEditing) {
                    ProductAddMainTracking.clickProductCategoryRecom(shopId)
                }
            }
        }
    }

    private fun onGetCategoryRecommendationFailed() {
        productCategoryLayout?.show()
        productCategoryRecListView?.hide()
    }

    private fun submitInput(shipmentInputModel: ShipmentInputModel,
                            descriptionInputModel: DescriptionInputModel,
                            variantInputModel: ProductVariantInputModel) {
        val detailInputModel = DetailInputModel(
                productNameField.getText(),
                productCategoryPickerButton?.text?.toString() ?: "",
                viewModel.selectedCategoryId,
                "",
                productPriceField.getTextLongOrZero(),
                productStockField.getTextIntOrZero(),
                productMinOrderField.getTextIntOrZero(),
                if (isProductConditionNew) CONDITION_NEW else CONDITION_USED,
                productSkuField.getText(),
                viewModel.productPhotoPaths,
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

    private fun submitInputEdit() {
        val detailInputModel = viewModel.detailInputModel
        detailInputModel.apply {
            productName = productNameField.getText()
            price = productPriceField.getTextLongOrZero()
            stock = productStockField.getTextIntOrZero()
            minOrder = productMinOrderField.getTextIntOrZero()
            condition = if (isProductConditionNew) CONDITION_NEW else CONDITION_USED
            sku = productSkuField.getText()
            imageUrlOrPathList = viewModel.productPhotoPaths
            preorder = PreorderInputModel(
                    preOrderDurationField.getTextIntOrZero(),
                    selectedDurationPosition,
                    preOrderSwitch?.isChecked ?: false)
            wholesaleList = getWholesaleInput()
        }

        val intent = Intent()
        intent.putExtra(EXTRA_DETAIL_INPUT, detailInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    // category id is saved on listActionText property
    private val ListItemUnify.categoryId: Long
        get() = listActionText?.toLong().orZero()

    @Suppress("UNCHECKED_CAST")
    private fun getSelectedCategory(): ListItemUnify? {
        ListUnify::class.java.getDeclaredField("array").let {
            it.isAccessible = true
            return (it.get(productCategoryRecListView) as ArrayList<ListItemUnify>).find { listItem ->
                listItem.listRightRadiobtn?.isChecked ?: false
            }
        }
    }
}
