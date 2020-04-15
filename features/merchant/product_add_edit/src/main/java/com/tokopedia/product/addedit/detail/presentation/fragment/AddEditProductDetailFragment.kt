package com.tokopedia.product.addedit.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment.Companion.REQUEST_CODE_DESCRIPTION
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import com.tokopedia.product.addedit.detail.presentation.adapter.WholeSalePriceInputAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_USED
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.DEBOUNCE_DELAY_MILLIS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.NEW_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.USED_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PreorderInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.detail.presentation.widget.ProductBulkPriceEditBottomSheetContent
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.add_edit_product_bulk_price_edit_bottom_sheet_content.view.*
import javax.inject.Inject

class AddEditProductDetailFragment : BaseDaggerFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener {

    companion object {
        private const val TAG_BULK_EDIT_PRICE: String = "TAG_BULK_EDIT_PRICE"

        fun createInstance(productInputModel: ProductInputModel, isEditing: Boolean, isDrafting: Boolean, isAdding: Boolean): Fragment {
            return AddEditProductDetailFragment().apply {
                val args = Bundle()
                args.putParcelable(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                args.putBoolean(EXTRA_IS_EDITING_PRODUCT, isEditing)
                args.putBoolean(EXTRA_IS_DRAFTING_PRODUCT, isDrafting)
                args.putBoolean(EXTRA_IS_ADDING_PRODUCT, isAdding)
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
    private var categoryAlertDialog: DialogUnify? = null

    // product price
    private var productPriceField: TextFieldUnify? = null
    private var productPriceEditIcon: ImageView? = null
    private val productPriceBulkEditBottomSheet by lazy { BottomSheetUnify() }
    private val productPriceBulkPriceEditBottomSheetContent by lazy { ProductBulkPriceEditBottomSheetContent(requireContext()) }

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

    // button continue
    private var submitButton: ViewGroup? = null
    private var submitTextView: AppCompatTextView? = null
    private var submitLoadingIndicator: LoaderUnify? = null

    override fun getScreenName(): String {
        return getString(R.string.product_add_edit_detail)
    }

    override fun initInjector() {
        getComponent(AddEditProductDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set detail and variant input model
        arguments?.getParcelable<ProductInputModel>(EXTRA_PRODUCT_INPUT_MODEL)?.run {
            viewModel.productInputModel = this
            viewModel.detailInputModel = this.detailInputModel
            viewModel.hasVariants = this.variantInputModel.productVariant.isNotEmpty()
            var pictureIndex = 0
            viewModel.productPhotoPaths = this.detailInputModel.imageUrlOrPathList.map { urlOrPath ->
                if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) this.detailInputModel.pictureList[pictureIndex++].urlThumbnail
                else urlOrPath
            }.toMutableList()
        }
        // set isEditing status
        arguments?.getBoolean(EXTRA_IS_EDITING_PRODUCT)?.run {
            viewModel.isEditing = this
        }
        // set isDrafting status
        arguments?.getBoolean(EXTRA_IS_DRAFTING_PRODUCT)?.run {
            viewModel.isDrafting = this
        }
        // set isAdding status
        arguments?.getBoolean(EXTRA_IS_ADDING_PRODUCT)?.run {
            viewModel.isAdding = this
        }

        userSession = UserSession(requireContext())
        shopId = userSession.shopId

        if (viewModel.isEditing) {
            ProductEditMainTracking.trackScreen()
        } else {
            ProductAddMainTracking.trackScreen()
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
        productPhotoAdapter = ProductPhotoAdapter(MAX_PRODUCT_PHOTOS, true, viewModel.productPhotoPaths, this)
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
        context?.let {
            categoryAlertDialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            categoryAlertDialog?.setTitle(getString(R.string.title_category_dialog))
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
            // if has variant
            if (viewModel.hasVariants) {
                showImmutableCategoryDialog()
            } else {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
                startActivityForResult(intent, REQUEST_CODE_CATEGORY)
            }
        }

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)
        productPriceEditIcon = view.findViewById(R.id.ic_edit_price)

        // add edit product wholesale views
        productWholeSaleSwitch = view.findViewById(R.id.su_wholesale)
        productWholeSaleInputLayout = view.findViewById(R.id.wholesale_input_layout)
        productWholeSaleInputFormsView = view.findViewById(R.id.rv_wholesale_input_forms)
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter(this,

                onDeleteWholesale = {
                    if (viewModel.isEditing) {
                        ProductEditMainTracking.clickRemoveWholesale(shopId)
                    } else {
                        ProductAddMainTracking.clickRemoveWholesale(shopId)
                    }
                    addNewWholeSalePriceButton?.visibility = View.VISIBLE
                },

                onAddWholesale = {
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
        addNewWholeSalePriceButton = view.findViewById(R.id.tv_add_new_wholesale_price)
        addNewWholeSalePriceButton?.setOnClickListener {
            wholeSaleInputFormsAdapter?.itemCount?.let {
                if (it >= AddEditProductDetailConstants.MAX_WHOLESALE_PRICES - 1) {
                    addNewWholeSalePriceButton?.visibility = View.GONE
                }
            }
            val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
            wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
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

        // submit button
        submitButton = view.findViewById(R.id.btn_submit)
        submitTextView = view.findViewById(R.id.tv_submit_text)
        submitLoadingIndicator = view.findViewById(R.id.lu_submit_loading_indicator)
        if (viewModel.isEditing || viewModel.isDrafting || viewModel.isAdding) submitTextView?.text = getString(R.string.action_save)
        else submitTextView?.text = getString(R.string.action_continue)

        // fill the form with detail input model
        fillProductDetailForm(viewModel.detailInputModel)

        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())

        productNameField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) productNameRecView?.hide()
        }

        // product name text change listener
        productNameField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                viewModel.isProductNameChanged = true
                Handler().postDelayed({ viewModel.validateProductNameInput(editable.toString()) }, DEBOUNCE_DELAY_MILLIS)
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // product price text change listener
        if (viewModel.productInputModel.variantInputModel.variantOptionParent.isNotEmpty() &&
                viewModel.productInputModel.variantInputModel.productVariant.isNotEmpty()) {
            productPriceField?.textFieldInput?.isEnabled = false
            productPriceEditIcon?.visible()
            productPriceEditIcon?.setOnClickListener { showEditAllVariantPriceBottomSheet() }
        } else {
            productPriceEditIcon?.hide()
            productPriceField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                    // clean any kind of number formatting here
                    val productPriceInput = charSequence?.toString()?.replace(".", "")
                    productPriceInput?.let {
                        // do the validation first
                        viewModel.validateProductPriceInput(it)
                        productPriceField?.textFieldInput?.let { editText ->
                            InputPriceUtil.applyPriceFormatToInputField(editText, it, this)
                        }
                    }
                }
            })
        }

        // product whole sale checked change listener
        productWholeSaleSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isWholeSalePriceActivated.value = isChecked
            if (isChecked) {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.clickWholesale(shopId)
                } else {
                    ProductAddMainTracking.clickWholesale(shopId)
                }

                val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
                wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
                val wholesalePriceExist = wholeSaleInputFormsAdapter?.itemCount != 0
                if (!wholesalePriceExist) wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
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

        // Continue to add product description
        submitButton?.setOnClickListener {
            submitTextView?.hide()
            submitLoadingIndicator?.show()
            validateInput()
            val isInputValid = viewModel.isInputValid.value
            isInputValid?.let {
                if (it) {
                    val isEditing = viewModel.isEditing
                    val isDrafting = viewModel.isDrafting
                    val isAdding = viewModel.isAdding

                    // navigate to preview page
                    if (isEditing || isDrafting || isAdding) submitInputEdit()
                    // navigate to description page
                    else moveToDescriptionActivity()
                }
            }
            submitTextView?.show()
            submitLoadingIndicator?.hide()
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

        var requestedFocus = false

        // product photo validation
        productPhotoAdapter?.let { viewModel.validateProductPhotoInput(it.itemCount) }

        // product name validation
        val productNameInput = productNameField?.getEditableValue().toString()
        // prevent name recommendation from being showed
        viewModel.isProductNameChanged = false
        viewModel.validateProductNameInput(productNameInput)
        viewModel.isProductNameInputError.value?.run {
            if (this && !requestedFocus) {
                productNameField?.requestFocus()
                requestedFocus = true
            }
        }

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
            if (this) validateWholeSaleInput(viewModel, productWholeSaleInputFormsView)
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
    }

    @Suppress("UNCHECKED_CAST")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                    val originalImageUrl = data.getStringArrayListExtra(ImageEditorActivity.RESULT_PREVIOUS_IMAGE)
                    val isEditted = data.getSerializableExtra(ImageEditorActivity.RESULT_IS_EDITTED) as ArrayList<Boolean>
                    viewModel.updateProductPhotos(imageUrlOrPathList, originalImageUrl, isEditted)
                    productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)
                    productPhotoAdapter?.let {
                        viewModel.validateProductPhotoInput(it.itemCount)
                    }
                }
                REQUEST_CODE_CATEGORY -> {

                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_NAME)

                    viewModel.productInputModel.detailInputModel.categoryId = categoryId.toString()
                    viewModel.productInputModel.detailInputModel.categoryName = categoryName

                    val categoryList = ListUnifyUtil.getSelected(productCategoryRecListView)
                    if (categoryList != null) {
                        ListUnifyUtil.getShownRadioButton(categoryList)?.isChecked = false
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickSaveOtherCategory(shopId)
                        }
                    } else {
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickBackOtherCategory(shopId)
                        }
                    }
                    productCategoryLayout?.show()
                    productCategoryRecListView?.show()
                    // TODO remove the onchange listener in productCategoryRecListView, because it overwrite viewModel.productInputModel.detailInputModel.categoryId to "0"
                    val selectedCategory = ArrayList<ListItemUnify>()
                    selectedCategory.add(ListItemUnify(categoryName, ""))
                    productCategoryRecListView?.setData(selectedCategory)
                }
                REQUEST_CODE_DESCRIPTION -> {
                    if(data.getIntExtra(EXTRA_BACK_PRESSED, 0) != 0) {
                        activity?.setResult(Activity.RESULT_OK, data)
                        activity?.finish()
                        return
                    }
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

        productNameRecView?.hide()

        viewModel.isNameRecommendationSelected = true

        productNameField?.textFieldInput?.setText(productName)

        if (!viewModel.isEditing) {
            ProductAddMainTracking.clickProductNameRecom(shopId, productName)
        }
    }

    override fun onWholeSaleQuantityItemTextChanged(position: Int, input: String) {
        val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
        val quantityField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_quantity)
        val minOrderInput = productMinOrderField?.textFieldInput?.editableText.toString()
        val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position) ?: ""
        val errorMessage = viewModel.validateProductWholeSaleQuantityInput(input, minOrderInput, previousQuantity)
        quantityField?.setError(errorMessage.isNotEmpty())
        quantityField?.setMessage(errorMessage)
        updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
        wholeSaleInputFormsAdapter?.run {
            if (input.isNotBlank()) this.updateWholeSaleQuantityInputModel(position, input)
        }
    }

    override fun onWholeSalePriceItemTextChanged(position: Int, input: String) {
        val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
        val priceField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_price)
        val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
        val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position)?.replace(".", "")
                ?: ""
        val errorMessage = viewModel.validateProductWholeSalePriceInput(input, productPriceInput, previousPrice)
        priceField?.setError(errorMessage.isNotEmpty())
        priceField?.setMessage(errorMessage)
        updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
        wholeSaleInputFormsAdapter?.run {
            if (input.isNotBlank()) this.updateWholeSalePriceInputModel(position, input)
        }
    }

    fun onCtaYesPressed() {
        ProductAddStepperTracking.trackDraftYes(shopId)
    }

    fun onCtaNoPressed() {
        ProductAddStepperTracking.trackDraftCancel(shopId)
    }

    fun sendDataBack() {
        if(!viewModel.isDrafting && !viewModel.isEditing && !viewModel.isAdding) {
            inputAllDataInProductInputModel()
            val intent = Intent()
            intent.putExtra(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
            intent.putExtra(EXTRA_BACK_PRESSED, 1)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            activity?.finish()
        }
    }

    fun onBackPressed() {
        if (viewModel.isEditMode) {
            ProductEditMainTracking.trackBack(shopId)
        } else {
            ProductAddMainTracking.trackBack(shopId)
        }
    }

    fun saveProductDraft(isUploading: Boolean) {
        inputAllDataInProductInputModel()
        viewModel.saveProductDraft(mapProductInputModelDetailToDraft(viewModel.productInputModel), viewModel.productInputModel.draftId, isUploading)
        Toast.makeText(context, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show()
    }

    private fun inputAllDataInProductInputModel() {
        viewModel.productInputModel.detailInputModel.productName = productNameField.getText()
        viewModel.productInputModel.detailInputModel.categoryId = viewModel.productInputModel.detailInputModel.categoryId
        viewModel.productInputModel.detailInputModel.price = productPriceField.getTextBigIntegerOrZero()
        viewModel.productInputModel.detailInputModel.stock = productStockField.getTextIntOrZero()
        viewModel.productInputModel.detailInputModel.minOrder = productMinOrderField.getTextIntOrZero()
        viewModel.productInputModel.detailInputModel.condition = if (isProductConditionNew) CONDITION_NEW else CONDITION_USED
        viewModel.productInputModel.detailInputModel.sku = productSkuField.getText()
        viewModel.productInputModel.detailInputModel.imageUrlOrPathList = viewModel.productPhotoPaths
        viewModel.productInputModel.detailInputModel.preorder.apply {
            duration = preOrderDurationField.getTextIntOrZero()
            timeUnit = selectedDurationPosition
            isActive = preOrderSwitch?.isChecked ?: false
        }
        viewModel.productInputModel.detailInputModel.wholesaleList = getWholesaleInput()
    }

    private fun validateWholeSaleInput(viewModel: AddEditProductDetailViewModel, wholesaleInputForms: RecyclerView?) {
        wholesaleInputForms?.childCount?.let {
            var wholeSaleErrorCounter = 0
            for (index in 0 until it) {
                val productWholeSaleFormView = wholesaleInputForms.layoutManager?.getChildAt(index)
                // Minimum amount
                val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                productWholeSaleQuantityField?.textFieldInput?.editableText?.run {
                    val minOrderInput = productMinOrderField?.textFieldInput?.editableText.toString()
                    val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(index)
                            ?: ""
                    val errorMessage = viewModel.validateProductWholeSaleQuantityInput(this.toString(), minOrderInput, previousQuantity)
                    productWholeSaleQuantityField.setError(errorMessage.isNotEmpty())
                    productWholeSaleQuantityField.setMessage(errorMessage)
                }
                val isQuantityError = productWholeSaleQuantityField?.isTextFieldError
                isQuantityError?.let { isError -> if (isError) wholeSaleErrorCounter++ }

                // Product price
                val productWholeSalePriceField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_price)
                productWholeSalePriceField?.textFieldInput?.editableText?.run {
                    val wholeSalePriceInput = this.toString().replace(".", "")
                    val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
                    val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(index)?.replace(".", "")
                            ?: ""
                    val errorMessage = viewModel.validateProductWholeSalePriceInput(wholeSalePriceInput, productPriceInput, previousPrice)
                    productWholeSalePriceField.setError(errorMessage.isNotEmpty())
                    productWholeSalePriceField.setMessage(errorMessage)
                }
                val isPriceError = productWholeSalePriceField?.isTextFieldError
                isPriceError?.let { isError -> if (isError) wholeSaleErrorCounter++ }
            }
            viewModel.wholeSaleErrorCounter.value = wholeSaleErrorCounter
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
                                productWholeSalePriceField.getText(),
                                productWholeSaleQuantityField.getText()
                        )
                        inputResult.add(item)
                    }
                }

            }
        }
        return inputResult
    }

    private fun fillProductDetailForm(detailInputModel: DetailInputModel) {

        // product photo
        productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)

        // product name
        productNameField?.textFieldInput?.setText(detailInputModel.productName)

        // product price
        val productPrice = detailInputModel.price
        if (productPrice != 0.toBigInteger()) {
            productPriceField?.textFieldInput?.setText(InputPriceUtil.formatProductPriceInput(detailInputModel.price.toString()))
        }
        if (viewModel.hasVariants) productPriceField?.textFieldInput?.isEnabled = false

        // product category
        if (detailInputModel.categoryName.isNotBlank()) {
            productCategoryLayout?.show()
            productCategoryRecListView?.show()
            val selectedCategory = ArrayList<ListItemUnify>()
            selectedCategory.add(ListItemUnify(detailInputModel.categoryName, ""))
            productCategoryRecListView?.setData(selectedCategory)
        }

        // product wholesale
        val wholesalePriceExist = detailInputModel.wholesaleList.isNotEmpty()
        if (wholesalePriceExist) {
            productWholeSaleSwitch?.isChecked = true
            wholeSaleInputFormsAdapter?.setWholeSaleInputModels(detailInputModel.wholesaleList)
            viewModel.isWholeSalePriceActivated.value = true
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
        val isProductConditionNew = detailInputModel.condition == CONDITION_NEW
        if (isProductConditionNew) productConditions[NEW_PRODUCT_INDEX].listRightRadiobtn?.isChecked = true
        else productConditions[USED_PRODUCT_INDEX].listRightRadiobtn?.isChecked = true

        // product sku
        productSkuField?.textFieldInput?.setText(detailInputModel.sku)

    }

    private fun subscribeToProductNameInputStatus() {
        viewModel.isProductNameInputError.observe(this, Observer {
            productNameField?.setError(it)
            productNameField?.setMessage(viewModel.productNameMessage)
            // if product name input has no issue
            if (!it) {
                val productNameInput = productNameField?.getEditableValue().toString()
                // prevent queries getting called from recursive name selection and clicked submit button
                if (!viewModel.isNameRecommendationSelected && viewModel.isProductNameChanged) {
                    // show product name recommendations
                    showProductNameLoadingIndicator()
                    productNameRecAdapter?.setProductNameInput(productNameInput)
                    viewModel.getProductNameRecommendation(query = productNameInput)
                }
                // show category recommendations to the product has no variants
                if (!viewModel.hasVariants) viewModel.getCategoryRecommendation(productNameInput)
            } else {
                // show empty recommendations for input with error
                productNameRecAdapter?.setProductNameRecommendations(emptyList())
                // keep the category if the product has variants
                if (!viewModel.hasVariants) productCategoryRecListView?.setData(ArrayList(emptyList()))
            }
            // reset name selection status
            viewModel.isNameRecommendationSelected = false
        })
    }

    private fun subscribeToProductPriceInputStatus() {
        viewModel.isProductPriceInputError.observe(this, Observer {
            if (productPriceBulkEditBottomSheet.isVisible) {
                productPriceBulkPriceEditBottomSheetContent.setError(it, viewModel.productPriceMessage)
            } else {
                productPriceField?.setError(it)
                productPriceField?.setMessage(viewModel.productPriceMessage)
            }
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

            if (viewModel.isEditing) {
                ProductEditMainTracking.clickPreorderButton(shopId)
            } else {
                ProductAddMainTracking.clickPreorderButton(shopId)
            }
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
            if (it) enableSubmitButton()
            else disableSubmitButton()
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
            val isDrafting = viewModel.isDrafting
            val isAdding = viewModel.isAdding
            val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) viewModel.detailInputModel.pictureList.find { it.urlThumbnail == urlOrPath }?.urlOriginal
                        ?: urlOrPath
                else urlOrPath
            }.orEmpty()
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(imageUrlOrPathList)), isEditing)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
            if (isEditing || isDrafting || isAdding) {
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

    private fun enableSubmitButton() {
        submitButton?.isClickable = true
        submitButton?.setBackgroundResource(R.drawable.rect_green_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, android.R.color.white)) }
    }

    private fun disableSubmitButton() {
        submitButton?.isClickable = false
        submitButton?.setBackgroundResource(R.drawable.rect_grey_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, R.color.Neutral_N700_32)) }
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
        val categoryId = viewModel.productInputModel.detailInputModel.categoryId
        inputAllDataInProductInputModel()
        val intent = AddEditProductDescriptionActivity.createInstance(context, categoryId, viewModel.productInputModel)
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

    private fun showImmutableCategoryDialog() {
        categoryAlertDialog?.show()
    }

    private fun onGetCategoryRecommendationSuccess(result: Success<List<ListItemUnify>>) {
        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(3))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            selectFirstCategoryRecommendation()
            createCategoryRecommendationItemClickListener(items)
            productCategoryRecListView?.onLoadFinish {}
        }
    }

    private fun onGetCategoryRecommendationFailed() {
        productCategoryLayout?.show()
        productCategoryRecListView?.hide()
    }

    private fun selectFirstCategoryRecommendation() = productCategoryRecListView?.run {
        productCategoryRecListView?.adapter?.count?.let {
            if (it > 0) {
                ListUnifyUtil.setSelected(this, 0) {
                    val categoryId = ListUnifyUtil.getCategoryId(it).toString()
                    viewModel.productInputModel.detailInputModel.categoryId = categoryId
                    ProductAddMainTracking.clickProductCategoryRecom(shopId)
                }
            }
        }
    }

    private fun createCategoryRecommendationItemClickListener(items: List<ListItemUnify>) = productCategoryRecListView?.run {
        productCategoryRecListView?.setOnItemClickListener { _, _, position, _ ->
            ListUnifyUtil.setSelected(this, position) {
                onCategoryRecommendationSelected(ListUnifyUtil.getCategoryId(it).toString())
            }
        }

        items.forEachIndexed { index, item ->
            item.listRightRadiobtn?.setOnClickListener {
                ListUnifyUtil.setSelected(this, index) {
                    onCategoryRecommendationSelected(ListUnifyUtil.getCategoryId(it).toString())
                }
            }
        }
    }

    private fun onCategoryRecommendationSelected(categoryId: String) {
        productNameRecView?.hide()
        viewModel.productInputModel.detailInputModel.categoryId = categoryId
        ProductAddMainTracking.clickProductCategoryRecom(shopId)
    }

    private fun showEditAllVariantPriceBottomSheet() {
        with(productPriceBulkEditBottomSheet) {
            setTitle(this@AddEditProductDetailFragment.getString(R.string.product_price_edit_bottom_sheet_title))
            showCloseIcon = true
            setCloseClickListener { this.dismiss() }
            productPriceBulkPriceEditBottomSheetContent.setPrice(viewModel.detailInputModel.price.toString())
            productPriceBulkPriceEditBottomSheetContent.setPriceTextWatcher(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                    // clean any kind of number formatting here
                    val productPriceInput = charSequence?.toString()?.replace(".", "")
                    productPriceInput?.let {
                        // do the validation first
                        viewModel.validateProductPriceInput(it)
                        productPriceBulkPriceEditBottomSheetContent.tfu_product_price.textFieldInput.let { editText ->
                            InputPriceUtil.applyPriceFormatToInputField(editText, it, this)
                        }
                    }
                }
            })
            productPriceBulkPriceEditBottomSheetContent.setPriceInputListener(object : ProductBulkPriceEditBottomSheetContent.PriceInputListener {
                override fun onPriceChangeRequested(price: Long) {
                    productPriceField?.textFieldInput?.setText(InputPriceUtil.formatProductPriceInput(price.toString()))
                    productPriceBulkEditBottomSheet.dismiss()
                }
            })
            setChild(productPriceBulkPriceEditBottomSheetContent)
            show(this@AddEditProductDetailFragment.childFragmentManager, TAG_BULK_EDIT_PRICE)
        }
    }

    private fun submitInput(shipmentInputModel: ShipmentInputModel,
                            descriptionInputModel: DescriptionInputModel,
                            variantInputModel: ProductVariantInputModel) {
        val detailInputModel = DetailInputModel(
                productNameField.getText(),
                viewModel.productInputModel.detailInputModel.categoryName,
                viewModel.productInputModel.detailInputModel.categoryId,
                "",
                productPriceField.getTextBigIntegerOrZero(),
                productStockField.getTextIntOrZero(),
                productMinOrderField.getTextIntOrZero(),
                if (isProductConditionNew) CONDITION_NEW else CONDITION_USED,
                productSkuField.getText(),
                viewModel.productInputModel.detailInputModel.status,
                viewModel.productPhotoPaths,
                PreorderInputModel(
                        preOrderDurationField.getTextIntOrZero(),
                        selectedDurationPosition,
                        preOrderSwitch?.isChecked ?: false
                ),
                getWholesaleInput()
        )

        updateImageList(detailInputModel)
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
            categoryId = viewModel.productInputModel.detailInputModel.categoryId
            price = productPriceField.getTextBigIntegerOrZero()
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

        updateImageList(detailInputModel)
        val intent = Intent()
        intent.putExtra(EXTRA_DETAIL_INPUT, detailInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun updateImageList(detailInputModel: DetailInputModel) {
        // fillter product pictureList, so that edited image will be reuploaded and changed (removed from pictureList) and than reorder the picture if necessary
        val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths().orEmpty()
        val pictureList = detailInputModel.pictureList.filter {
            imageUrlOrPathList.contains(it.urlThumbnail)
        }
        val newPictureList = mutableListOf<PictureInputModel>()
        imageUrlOrPathList.forEach { urlOrPath ->
            pictureList.find { it.urlThumbnail == urlOrPath }?.run {
                newPictureList.add(this)
            }
        }
        detailInputModel.pictureList = newPictureList
        detailInputModel.imageUrlOrPathList = imageUrlOrPathList
    }
}
