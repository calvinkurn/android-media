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
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
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
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.FIRST_CATEGORY_SELECTED
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailModule
import com.tokopedia.product.addedit.detail.di.DaggerAddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.adapter.NameRecommendationAdapter
import com.tokopedia.product.addedit.detail.presentation.adapter.WholeSalePriceInputAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_FULL_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_USED
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.DEBOUNCE_DELAY_MILLIS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.NEW_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.USED_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FIRST_MOVED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragmentArgs
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEditProductDetailFragment : BaseDaggerFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener,
        WholeSaleInputViewHolder.OnAddButtonClickListener,
        AddEditProductPerformanceMonitoringListener
{

    companion object {
        private fun getDurationUnit(type: Int) =
                when (type) {
                    UNIT_DAY -> com.tokopedia.product.addedit.R.string.label_day
                    UNIT_WEEK -> com.tokopedia.product.addedit.R.string.label_week
                    else -> -1
                }
    }

    @Inject
    lateinit var viewModel: AddEditProductDetailViewModel

    private var selectedDurationPosition: Int = UNIT_DAY
    private var isPreOrderFirstTime = true
    private var countTouchPhoto = 0
    private var hasCategoryFromPicker = false

    // product photo
    private var addProductPhotoButton: AppCompatTextView? = null
    private var productPhotosView: RecyclerView? = null
    private var productPhotoAdapter: ProductPhotoAdapter? = null
    private var photoItemTouchHelper: ItemTouchHelper? = null
    private var productPictureList: List<PictureInputModel>? = null

    // product name
    private var productNameField: TextFieldUnify? = null
    private var productNameRecView: RecyclerView? = null
    private var productNameRecLoader: LoaderUnify? = null
    private var productNameRecShimmering: View? = null
    private var productNameRecAdapter: NameRecommendationAdapter? = null

    // product category
    private var productCategoryId: String = ""
    private var productCategoryName: String = ""
    private var productCategoryLayout: ViewGroup? = null
    private var productCategoryRecListView: ListUnify? = null
    private var productCategoryPickerButton: AppCompatTextView? = null
    private var categoryAlertDialog: DialogUnify? = null

    // product price
    private var productPriceField: TextFieldUnify? = null
    private var productPriceEditIcon: ImageView? = null
    private var productPriceBulkEditDialog: DialogUnify? = null

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

    // PLT monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun getScreenName(): String {
        return getString(R.string.product_add_edit_detail)
    }

    override fun initInjector() {
        DaggerAddEditProductDetailComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .addEditProductDetailModule(AddEditProductDetailModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cacheManagerId = AddEditProductDetailFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

            // set detail and variant input model
            cacheManagerId.run {
                viewModel.productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
                var pictureIndex = 0
                viewModel.productPhotoPaths = viewModel.productInputModel.detailInputModel.imageUrlOrPathList.map { urlOrPath ->
                    if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) viewModel.productInputModel.detailInputModel.pictureList[pictureIndex++].urlThumbnail
                    else urlOrPath
                }.toMutableList()
                viewModel.isEditing = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java) ?: false
                viewModel.isAdding = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java) ?: false
                viewModel.isDrafting = saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java) ?: false
                viewModel.isFirstMoved = saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java) ?: false
            }
        }
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        selectedDurationPosition = viewModel.productInputModel.detailInputModel.preorder.timeUnit

        if (viewModel.isAdding) {
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
            if (viewModel.hasVariants) {
                showImmutableCategoryDialog()
            } else {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
                intent.putExtra(AddEditProductConstants.EXTRA_IS_EDIT_MODE, (viewModel.isEditing))
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
        wholeSaleInputFormsAdapter = WholeSalePriceInputAdapter(this, this,
                onDeleteWholesale = {
                    if (viewModel.isEditing) {
                        ProductEditMainTracking.clickRemoveWholesale(shopId)
                    } else {
                        ProductAddMainTracking.clickRemoveWholesale(shopId)
                    }
                    addNewWholeSalePriceButton?.visibility = View.VISIBLE
                    val deletePosition = wholeSaleInputFormsAdapter?.getDeletePosition()
                    wholeSaleInputFormsAdapter?.itemCount?.let {
                        if (it == 1) {
                            productWholeSaleSwitch?.isChecked = false
                        }
                        validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, it - 1,  deletePosition ?: -1)
                        // to avoid enable button submit when we delete the last of whole sale
                        if(deletePosition == it - 1) {
                            viewModel.isTheLastOfWholeSale.value = false
                        }
                    }
                })

        productWholeSaleInputFormsView?.apply {
            adapter = wholeSaleInputFormsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        addNewWholeSalePriceButton = view.findViewById(R.id.tv_add_new_wholesale_price)
        addNewWholeSalePriceButton?.setOnClickListener {
            if (viewModel.isEditing) {
                ProductEditMainTracking.clickAddWholesale(shopId)
            } else {
                ProductAddMainTracking.clickAddWholesale(shopId)
            }

            val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
            wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
            wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
            validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount, isAddingWholeSale = true)
            updateAddNewWholeSalePriceButtonVisibility()
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

        // pre order checked change listener
        preOrderSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // tracker
                if (!isPreOrderFirstTime) {
                    if (viewModel.isEditing) {
                        ProductEditMainTracking.clickPreorderButton(shopId)
                    } else {
                        ProductAddMainTracking.clickPreorderButton(shopId)
                    }
                }

                preOrderInputLayout?.visibility = View.VISIBLE
            } else {
                preOrderInputLayout?.visibility = View.GONE
            }

            viewModel.isPreOrderActivated.value = isChecked
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
        newCondition.isBold = false
        newCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, null)
        newCondition.run { productConditions.add(NEW_PRODUCT_INDEX, this) }

        // secondhand condition
        val secondHandCondition = ListItemUnify(getString(R.string.label_secondhand), "")
        secondHandCondition.isBold = false
        secondHandCondition.setVariant(null, ListItemUnify.RADIO_BUTTON, getString(R.string.label_secondhand))
        secondHandCondition.run { productConditions.add(USED_PRODUCT_INDEX, this) }

        // add new and secondhand condition to the view
        productConditionListView?.setData(productConditions)

        // add edit product sku views
        productSkuField = view.findViewById(R.id.tfu_sku)

        // submit button
        submitButton = view.findViewById(R.id.btn_submit)
        submitTextView = view.findViewById(R.id.tv_submit_text)
        submitLoadingIndicator = view.findViewById(R.id.lu_submit_loading_indicator)
        if (viewModel.isAdding && viewModel.isFirstMoved) {
            submitTextView?.text = getString(R.string.action_continue)
        } else {
            submitTextView?.text = getString(R.string.action_save)
        }

        // fill the form with detail input model
        fillProductDetailForm(viewModel.productInputModel.detailInputModel)

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
        if (viewModel.productInputModel.variantInputModel.selections.isNotEmpty() &&
                viewModel.productInputModel.variantInputModel.products.isNotEmpty()) {
            productPriceField?.textFieldInput?.isEnabled = false
            productPriceEditIcon?.visible()
            productPriceEditIcon?.setOnClickListener { showEditAllVariantPriceDialog() }
        } else {
            productPriceEditIcon?.hide()
        }

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
                        InputPriceUtil.applyPriceFormatToInputField(editText, it, start,
                                charSequence.length, count, this)
                    }
                    // product wholesale input validation
                    viewModel.isWholeSalePriceActivated.value?.run {
                        if (this) validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
                    }
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

                if (viewModel.hasVariants) {
                    showVariantWholesalePriceDialog()
                } else {
                    enableWholesale()
                }
            } else {
                viewModel.isAddingValidationWholeSale = false
                viewModel.isAddingWholeSale = false
                viewModel.isTheLastOfWholeSale.value = false
                viewModel.wholeSaleErrorCounter.value = 0
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
                    val isAdding = viewModel.isAdding
                    val isDrafting = viewModel.isDrafting
                    val isFirstMoved = viewModel.isFirstMoved
                    if (isAdding && isFirstMoved) moveToDescriptionActivity()
                    else if (isAdding && !isDrafting) submitInput()
                    else submitInputEdit()
                }
            }
            submitTextView?.show()
            submitLoadingIndicator?.hide()
        }

        onFragmentResult()
        setupBackPressed()

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

        stopPreparePagePerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_DETAIL_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_DETAIL_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_DETAIL_TRACE)
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

    private fun removeObservers() {
        viewModel.isProductNameInputError.removeObservers(this)
        viewModel.productNameRecommendations.removeObservers(this)
        viewModel.productCategoryRecommendationLiveData.removeObservers(this)
        viewModel.isProductPriceInputError.removeObservers(this)
        viewModel.isWholeSalePriceActivated.removeObservers(this)
        viewModel.isProductStockInputError.removeObservers(this)
        viewModel.isOrderQuantityInputError.removeObservers(this)
        viewModel.isPreOrderActivated.removeObservers(this)
        viewModel.isPreOrderDurationInputError.removeObservers(this)
        viewModel.isInputValid.removeObservers(this)
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.removeObservers(this)
    }

    private fun updateAddNewWholeSalePriceButtonVisibility() {
        wholeSaleInputFormsAdapter?.itemCount?.let {
            if (it >= AddEditProductDetailConstants.MAX_WHOLESALE_PRICES) {
                addNewWholeSalePriceButton?.visibility = View.GONE
            }
        }
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
            if (this) validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
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
                    val newUpdatedPhotos = viewModel.updateProductPhotos(imageUrlOrPathList, originalImageUrl, isEditted)
                    productPictureList = newUpdatedPhotos.pictureList
                    productPhotoAdapter?.setProductPhotoPaths(viewModel.productPhotoPaths)
                    productPhotoAdapter?.let {
                        viewModel.validateProductPhotoInput(it.itemCount)
                    }
                }
                REQUEST_CODE_CATEGORY -> {
                    hasCategoryFromPicker = true
                    val categoryId = data.getLongExtra(CATEGORY_RESULT_ID, 0)
                    val categoryName = data.getStringExtra(CATEGORY_RESULT_FULL_NAME)

                    productCategoryId = categoryId.toString()
                    productCategoryName = categoryName ?: ""

                    val categoryRecommendationResult = viewModel.productCategoryRecommendationLiveData.value
                    val categoryList = if (categoryRecommendationResult != null && categoryRecommendationResult is Success) {
                        productCategoryRecListView?.getSelected(categoryRecommendationResult.data)
                    } else {
                        null
                    }
                    if (categoryList != null) {
                        categoryList.getShownRadioButton()?.isChecked = false
                        if (viewModel.isEditing) {
                            ProductEditMainTracking.clickSaveOtherCategory(shopId)
                        }
                    }
                    productCategoryLayout?.show()
                    productCategoryRecListView?.show()
                    val selectedCategory = ArrayList<ListItemUnify>()
                    val selectedCategoryItemUnify = ListItemUnify(categoryName, "")
                    selectedCategoryItemUnify.isBold = false
                    selectedCategory.add(selectedCategoryItemUnify)
                    productCategoryRecListView?.setData(selectedCategory)
                }
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        photoItemTouchHelper?.startDrag(viewHolder)
        //countTouchPhoto is used for count how many times images hit
        // we use this because startDrag(viewHolder) can hit tracker two times
        // we use this because startDrag(viewHolder) can hit tracker two times
        countTouchPhoto += 1
        //countTouchPhoto can increment 1 every time we come or back to this page
        //if we back from ActivityOnResult countTouchPhoto still increment,
        //to avoid that we have to make sure the value of countTouchPhoto must be 1
        if (countTouchPhoto > 2) {
            countTouchPhoto = 1
        }
        // tracker only hit when there are two images of product
        if (productPhotoAdapter?.itemCount ?: 0 > 1) {
            // to avoid double hit tracker when dragging or touching image product, we have to put if here
            if (countTouchPhoto == 2) {
                if (viewModel.isEditing) {
                    ProductEditMainTracking.trackDragPhoto(shopId)
                } else {
                    ProductAddMainTracking.trackDragPhoto(shopId)
                }
            }
        }
    }

    override fun onRemovePhoto(viewHolder: RecyclerView.ViewHolder) {
        // validate when 1 photo item is removed
        val photoCount = productPhotoAdapter?.itemCount ?: 0
        viewModel.validateProductPhotoInput(photoCount - 1)

        // tracking
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
        productNameField?.textFieldInput?.setSelection(productName.length)

        if (viewModel.isAdding) {
            ProductAddMainTracking.clickProductNameRecom(shopId, productName)
        }
    }

    override fun onWholeSaleQuantityItemTextChanged(position: Int, input: String) {
        if ( productWholeSaleInputFormsView?.layoutManager?.itemCount == wholeSaleInputFormsAdapter?.itemCount) {
            val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
            val quantityField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_quantity)
            val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
            val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position) ?: ""
            val errorMessage = viewModel.validateProductWholeSaleQuantityInput(input, minOrderInput, previousQuantity)
            // to avoid enable button submit when we edit the last of whole sale
            if(position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
                viewModel.isTheLastOfWholeSale.value = errorMessage.isNotEmpty()
            }
            quantityField?.setError(errorMessage.isNotEmpty())
            quantityField?.setMessage(errorMessage)
            updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
            wholeSaleInputFormsAdapter?.run {
                if (input.isNotBlank()) this.updateWholeSaleQuantityInputModel(position, input)
            }
        }
    }

    override fun onWholeSalePriceItemTextChanged(position: Int, input: String) {
       if (productWholeSaleInputFormsView?.layoutManager?.itemCount == wholeSaleInputFormsAdapter?.itemCount) {
           val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
           val priceField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_price)
           val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
           val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position)?.replace(".", "")
                   ?: ""
           val errorMessage = viewModel.validateProductWholeSalePriceInput(input, productPriceInput, previousPrice)
           // to avoid enable button submit when we edit the last of whole sale
           if(position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
               viewModel.isTheLastOfWholeSale.value = errorMessage.isNotEmpty()
           }
           priceField?.setError(errorMessage.isNotEmpty())
           priceField?.setMessage(errorMessage)
           updateWholeSaleErrorCounter(viewModel, productWholeSaleInputFormsView)
           wholeSaleInputFormsAdapter?.run {
               if (input.isNotBlank()) this.updateWholeSalePriceInputModel(position, input)
           }
       }
    }

    private fun onFragmentResult() {
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.observe(viewLifecycleOwner, Observer { bundle ->
            setNavigationResult(bundle, REQUEST_KEY_ADD_MODE)
            removeNavigationResult(REQUEST_KEY_ADD_MODE)
            findNavController().navigateUp()
        })
    }

    private fun sendDataBack() {
        if (viewModel.isAdding && !viewModel.isDrafting) {
            var dataBackPressed = NO_DATA
            if (viewModel.isFirstMoved) {
                inputAllDataInProductInputModel()
                dataBackPressed = DETAIL_DATA
                viewModel.productInputModel.requestCode = arrayOf(DETAIL_DATA, NO_DATA, NO_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_DETAIL)
        }
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendDataBack()

                if (viewModel.isEditing) {
                    ProductEditMainTracking.trackBack(shopId)
                } else {
                    ProductAddMainTracking.trackBack(shopId)
                }
            }
        })
    }

    private fun inputAllDataInProductInputModel() {
        viewModel.productInputModel.detailInputModel.apply {
            productName = productNameField.getText()
            price = productPriceField.getTextBigIntegerOrZero()
            stock = productStockField.getTextIntOrZero()
            condition = if (isProductConditionNew)  CONDITION_NEW else CONDITION_USED
            minOrder = productMinOrderField.getTextIntOrZero()
            sku = productSkuField.getText()
            imageUrlOrPathList = viewModel.productPhotoPaths
            if (!productPictureList.isNullOrEmpty()) pictureList = productPictureList ?: listOf()
            if (productCategoryId.isNotBlank()) categoryId = productCategoryId
            if (categoryName.isNotBlank()) categoryName = productCategoryName
            preorder.apply {
                duration = preOrderDurationField.getTextIntOrZero()
                timeUnit = selectedDurationPosition
                isActive = preOrderSwitch?.isChecked ?: false
            }
            wholesaleList = getWholesaleInput()
        }
    }

    private fun validateWholeSaleInput(viewModel: AddEditProductDetailViewModel, wholesaleInputForms: RecyclerView?, itemCount: Int?, specialIndex: Int = -1, isAddingWholeSale : Boolean = false) {
        itemCount?.let {
            var wholeSaleErrorCounter = 0
            for (index in 0 until it) {
                // to avoid counting error of whole sale that we removed
                if (specialIndex == index) {
                    continue
                }
                val productWholeSaleFormView = wholesaleInputForms?.layoutManager?.getChildAt(index)
                // Minimum amount
                val productWholeSaleQuantityField: TextFieldUnify? = productWholeSaleFormView?.findViewById(R.id.tfu_wholesale_quantity)
                productWholeSaleQuantityField?.textFieldInput?.editableText?.run {
                    val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
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
            viewModel.isAddingWholeSale = isAddingWholeSale
            viewModel.isAddingValidationWholeSale = wholeSaleErrorCounter > 0
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

    @Suppress("RedundantIf", "LiftReturnOrAssignment")
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
            updateAddNewWholeSalePriceButtonVisibility()
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
        productConditionListView?.onLoadFinish {

            if (detailInputModel.condition == CONDITION_NEW) {
                isProductConditionNew = true
                productConditionListView?.setSelected(productConditions, NEW_PRODUCT_INDEX) {}
            } else {
                isProductConditionNew = false
                productConditionListView?.setSelected(productConditions, USED_PRODUCT_INDEX) {}
            }

            // list item click listener
            productConditionListView?.run {
                this.setOnItemClickListener { _, _, position, _ ->
                    setSelected(productConditions, position) {
                        if (position == NEW_PRODUCT_INDEX) isProductConditionNew = true
                        else isProductConditionNew = false
                    }
                }
            }

            productConditions.forEachIndexed { index, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    productConditionListView?.setSelected(productConditions, index) {
                        if (index == NEW_PRODUCT_INDEX) isProductConditionNew = true
                        else isProductConditionNew = false
                    }
                }
            }
        }

        // product sku
        productSkuField?.textFieldInput?.setText(detailInputModel.sku)

    }

    private fun subscribeToProductNameInputStatus() {
        viewModel.isProductNameInputError.observe(viewLifecycleOwner, Observer {
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
                // show category recommendations to the product that has no variants
                if (viewModel.isAdding && !viewModel.hasVariants) viewModel.getCategoryRecommendation(productNameInput)
            } else {
                // show empty recommendations for input with error
                productNameRecAdapter?.setProductNameRecommendations(emptyList())
                // keep the category if the product has variants
                if (viewModel.isAdding && !viewModel.hasVariants) productCategoryRecListView?.setData(ArrayList(emptyList()))
            }
            // reset name selection status
            viewModel.isNameRecommendationSelected = false
        })
    }

    private fun subscribeToProductPriceInputStatus() {
        viewModel.isProductPriceInputError.observe(viewLifecycleOwner, Observer {
            productPriceField?.setError(it)
            productPriceField?.setMessage(viewModel.productPriceMessage)
        })
    }

    private fun subscribeToWholeSaleSwitchStatus() {
        viewModel.isWholeSalePriceActivated.observe(viewLifecycleOwner, Observer {
            if (it) productWholeSaleInputLayout?.visible()
            else productWholeSaleInputLayout?.hide()
        })
    }

    private fun subscribeToProductStockInputStatus() {
        viewModel.isProductStockInputError.observe(viewLifecycleOwner, Observer {
            productStockField?.setError(it)
            productStockField?.setMessage(viewModel.productStockMessage)
        })
    }

    private fun subscribeToOrderQuantityInputStatus() {
        viewModel.isOrderQuantityInputError.observe(viewLifecycleOwner, Observer {
            productMinOrderField?.setError(it)
            productMinOrderField?.setMessage(viewModel.orderQuantityMessage)
        })
    }

    private fun subscribeToPreOrderSwitchStatus() {
        viewModel.isPreOrderActivated.observe(viewLifecycleOwner, Observer {
            isPreOrderFirstTime = false
            if (it) preOrderInputLayout?.visible()
            else preOrderInputLayout?.hide()
        })
    }

    private fun subscribeToPreOrderDurationInputStatus() {
        viewModel.isPreOrderDurationInputError.observe(viewLifecycleOwner, Observer {
            preOrderDurationField?.setError(it)
            preOrderDurationField?.setMessage(viewModel.preOrderDurationMessage)
        })
    }

    private fun subscribeToInputStatus() {
        viewModel.isInputValid.observe(viewLifecycleOwner, Observer {
            if (it) enableSubmitButton()
            else disableSubmitButton()
        })
    }

    private fun subscribeToCategoryRecommendation() {
        viewModel.productCategoryRecommendationLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onGetCategoryRecommendationSuccess(it)
                is Fail -> {
                    onGetCategoryRecommendationFailed()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {

            val isEditing = viewModel.isEditing
            val isAdding = viewModel.isAdding

            // tracking
            if (isEditing) {
                ProductEditMainTracking.trackAddPhoto(shopId)
            } else {
                ProductAddMainTracking.trackAddPhoto(shopId)
            }

            productPhotoAdapter?.run {
                // show error message when maximum product image is reached
                val productPhotoSize = this.getProductPhotoPaths().size
                if (productPhotoSize == MAX_PRODUCT_PHOTOS) showMaxProductImageErrorToast(getString(R.string.error_max_product_photo))
                else {
                    val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                        val pictureList = viewModel.productInputModel.detailInputModel.pictureList
                        if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) pictureList.find {
                            it.urlThumbnail == urlOrPath
                        }?.urlOriginal ?: urlOrPath
                        else urlOrPath
                    }.orEmpty()
                    val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder(ArrayList(imageUrlOrPathList)), isEditing, isAdding)
                    startActivityForResult(intent, REQUEST_CODE_IMAGE)
                }
            }
        }
    }

    private fun showMaxProductImageErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, type = Toaster.TYPE_ERROR)
        }
    }

    private fun showVariantWholesalePriceDialog() {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.message_variant_price_wholesale_title))
            setDescription(getString(R.string.message_variant_price_wholesale))
            setPrimaryCTAText(getString(R.string.action_variant_price_wholesale_negative))
            setPrimaryCTAClickListener {
                dialog.dismiss()
                disableWholesale()
            }
            setSecondaryCTAText(getString(R.string.action_variant_price_wholesale_positive))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                enableWholesale()
            }
        }
        dialog.show()
    }

    private fun enableWholesale() {
        val productPriceInput = productPriceField?.textFieldInput?.editableText
                .toString().replace(".", "")
        wholeSaleInputFormsAdapter?.setProductPrice(productPriceInput)
        val wholesalePriceExist = wholeSaleInputFormsAdapter?.itemCount != 0
        if (!wholesalePriceExist) wholeSaleInputFormsAdapter?.addNewWholeSalePriceForm()
        validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, productWholeSaleInputFormsView?.childCount)
        viewModel.shouldUpdateVariant = true
    }

    private fun disableWholesale() {
        productWholeSaleSwitch?.isChecked = false
        viewModel.shouldUpdateVariant = false
    }

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(selectedImagePathList: ArrayList<String>?): ImagePickerBuilder {

        val title = getString(R.string.action_pick_photo)

        val placeholderDrawableRes = arrayListOf(
                com.tokopedia.product.addedit.R.drawable.ic_utama,
                com.tokopedia.product.addedit.R.drawable.ic_depan,
                com.tokopedia.product.addedit.R.drawable.ic_samping,
                com.tokopedia.product.addedit.R.drawable.ic_atas,
                com.tokopedia.product.addedit.R.drawable.ic_detail
        )

        val imagePickerPickerTabTypeDef = intArrayOf(
                ImagePickerTabTypeDef.TYPE_GALLERY,
                ImagePickerTabTypeDef.TYPE_CAMERA
        )

        val imagePickerEditorBuilder = ImagePickerEditorBuilder.getDefaultBuilder().apply {
            this.belowMinResolutionErrorMessage = getString(R.string.error_image_under_x_resolution, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION)
            this.imageTooLargeErrorMessage = getString(R.string.error_image_too_large, (AddEditProductConstants.MAX_PRODUCT_IMAGE_SIZE_IN_KB / 1024))
            this.isRecheckSizeAfterResize = false
        }

        val imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                selectedImagePathList,
                placeholderDrawableRes,
                com.tokopedia.product.addedit.R.string.label_primary,
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

    private fun enableSubmitButton() {
        submitButton?.isClickable = true
        submitButton?.setBackgroundResource(com.tokopedia.product.addedit.R.drawable.product_add_edit_rect_green_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, android.R.color.white)) }
    }

    private fun disableSubmitButton() {
        submitButton?.isClickable = false
        submitButton?.setBackgroundResource(com.tokopedia.product.addedit.R.drawable.rect_grey_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N700_32)) }
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
        if (viewModel.isAdding) {
            ProductAddMainTracking.clickContinue(shopId)
        }
        inputAllDataInProductInputModel()
        arguments?.let {
            val cacheManagerId = AddEditProductDetailFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
                put(EXTRA_IS_EDITING_PRODUCT, viewModel.isEditing)
                put(EXTRA_IS_ADDING_PRODUCT, viewModel.isAdding)
                put(EXTRA_IS_DRAFTING_PRODUCT, viewModel.isDrafting)
                put(EXTRA_IS_FIRST_MOVED, viewModel.isFirstMoved)
            }
            val destination = AddEditProductDetailFragmentDirections.actionAddEditProductDetailFragmentToAddEditProductDescriptionFragment()
            destination.cacheManagerId = cacheManagerId
            findNavController().navigate(destination)
        }
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
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
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
        hasCategoryFromPicker = false
        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(3))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            selectFirstCategoryRecommendation(items)

            productCategoryRecListView?.run {
                this.setOnItemClickListener { _, _, position, _ ->
                    if (viewModel.isAdding) {
                        ProductAddMainTracking.clickProductCategoryRecom(shopId)
                    }
                    selectCategoryRecommendation(items, position)
                }
            }

            items.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    if (viewModel.isAdding) {
                        ProductAddMainTracking.clickProductCategoryRecom(shopId)
                    }
                    selectCategoryRecommendation(items, position)
                }
            }
        }
    }

    private fun onGetCategoryRecommendationFailed() {
        productCategoryLayout?.show()
        productCategoryRecListView?.hide()
    }

    private fun selectFirstCategoryRecommendation(items: List<ListItemUnify>) {
        productCategoryRecListView?.count?.let { itemSize ->
            if (itemSize > 0) {
                selectCategoryRecommendation(items, FIRST_CATEGORY_SELECTED)
            }
        }
    }

    private fun selectCategoryRecommendation(items: List<ListItemUnify>, position: Int) = productCategoryRecListView?.run {
        if (!hasCategoryFromPicker) {
            setSelected(items, position) {
                val categoryId = it.getCategoryId().toString()
                val categoryName = it.getCategoryName()
                productCategoryId = categoryId
                productCategoryName = categoryName
                true
            }
        }
    }

    private fun showEditAllVariantPriceDialog() {
        if (productPriceBulkEditDialog == null) {
            setupEditAllVariantDialog()
        }
        productPriceBulkEditDialog?.show()
    }

    private fun setupEditAllVariantDialog() {
        context?.run {
            productPriceBulkEditDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            productPriceBulkEditDialog?.setTitle(getString(R.string.label_change_price_dialog_title))
            productPriceBulkEditDialog?.setDescription(getString(R.string.label_change_price_dialog_message))
            productPriceBulkEditDialog?.setPrimaryCTAText(getString(R.string.action_cancel))
            productPriceBulkEditDialog?.setSecondaryCTAText(getString(R.string.action_change_price))

            productPriceBulkEditDialog?.setPrimaryCTAClickListener {
                productPriceBulkEditDialog?.dismiss()
            }
            productPriceBulkEditDialog?.setSecondaryCTAClickListener {
                productPriceField?.textFieldInput?.isEnabled = true
                viewModel.shouldUpdateVariant = true
                productPriceBulkEditDialog?.dismiss()
            }
        }
    }

    private fun submitInput() {
        inputAllDataInProductInputModel()
        setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE)
    }

    private fun submitInputEdit() {
        inputAllDataInProductInputModel()
        setFragmentResultWithBundle(REQUEST_KEY_DETAIL)
        if (viewModel.isEditing) {
            ProductEditMainTracking.clickContinue(shopId)
        }
    }

    private fun setFragmentResultWithBundle(requestKey: String, dataBackPressed: Int = DETAIL_DATA) {
        val variantInputModel = viewModel.productInputModel.variantInputModel
        if (viewModel.shouldUpdateVariant) {
            variantInputModel.products.forEach {
                it.price = productPriceField.getTextBigIntegerOrZero()
            }
            viewModel.shouldUpdateVariant = false
        }
        viewModel.productInputModel.variantInputModel = variantInputModel
        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel)
            val bundle = Bundle().apply {
                putString(BUNDLE_CACHE_MANAGER_ID, cacheManagerId)
                putInt(BUNDLE_BACK_PRESSED, dataBackPressed)
            }
            setNavigationResult(bundle, requestKey)
            findNavController().navigateUp()
        }
    }

    override fun getValidationCurrentWholeSaleQuantity(quantity: String, position: Int): String {
        val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
        val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position - 1) ?: ""
        val validation = viewModel.validateProductWholeSaleQuantityInput(quantity, minOrderInput, previousQuantity)
        viewModel.isTheLastOfWholeSale.value = validation.isNotEmpty()
        return validation
    }

    override fun getValidationCurrentWholeSalePrice(price: String, position: Int): String {
        val productPriceInput = productPriceField?.textFieldInput?.editableText.toString().replace(".", "")
        val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position - 1)?.replace(".", "") ?: ""
        val validation = viewModel.validateProductWholeSalePriceInput(price, productPriceInput, previousPrice)
        viewModel.isTheLastOfWholeSale.value = validation.isNotEmpty()
        return validation
    }
}
