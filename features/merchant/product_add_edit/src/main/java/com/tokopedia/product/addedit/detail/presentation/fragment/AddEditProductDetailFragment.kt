package com.tokopedia.product.addedit.detail.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
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
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DETAIL_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.FIRST_CATEGORY_SELECTED
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
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
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.NEW_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.PRICE_RECOMMENDATION_BANNER_URL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CATEGORY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_IMAGE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_SPECIFICATION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DETAIL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.USED_PRODUCT_INDEX
import com.tokopedia.product.addedit.detail.presentation.dialog.TitleValidationBottomSheet
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.TitleValidationModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.detail.presentation.viewholder.WholeSaleInputViewHolder
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import com.tokopedia.product.addedit.imagepicker.ImagePickerAddEditNavigation
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
import com.tokopedia.product.addedit.specification.presentation.activity.AddEditProductSpecificationActivity
import com.tokopedia.product.addedit.tooltip.model.NumericWithDescriptionTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tooltip.presentation.TooltipCardViewSelectable
import com.tokopedia.product.addedit.tracking.ProductAddMainTracking
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.product_photo_adapter.PhotoItemTouchHelperCallback
import com.tokopedia.product_photo_adapter.ProductPhotoAdapter
import com.tokopedia.product_photo_adapter.ProductPhotoViewHolder
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.SHOWCASE_PICKER_RESULT_REQUEST_CODE
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class AddEditProductDetailFragment : BaseDaggerFragment(),
        ProductPhotoViewHolder.OnPhotoChangeListener,
        NameRecommendationAdapter.ProductNameItemClickListener,
        WholeSaleInputViewHolder.TextChangedListener,
        WholeSaleInputViewHolder.OnAddButtonClickListener,
        AddEditProductPerformanceMonitoringListener {

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

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    private var selectedDurationPosition: Int = UNIT_DAY
    private var isPreOrderFirstTime = true
    private var countTouchPhoto = 0
    private var hasCategoryFromPicker = false
    private var isFragmentVisible = false
    private var needToSetCategoryName = false

    // product photo
    private var addProductPhotoButton: AppCompatTextView? = null
    private var productPhotosView: RecyclerView? = null
    private var productPhotoAdapter: ProductPhotoAdapter? = null
    private var photoItemTouchHelper: ItemTouchHelper? = null
    private var productPictureList: List<PictureInputModel>? = null

    // product name
    private var productNameField: TextFieldUnify2? = null
    private var productNameRecView: RecyclerView? = null
    private var productNameRecShimmering: View? = null
    private var productNameRecAdapter: NameRecommendationAdapter? = null

    // product category
    private var productCategoryId: String = ""
    private var productCategoryName: String = ""
    private var productCategoryLayout: ViewGroup? = null
    private var productCategoryRecListView: ListUnify? = null
    private var productCategoryPickerButton: AppCompatTextView? = null
    private var categoryAlertDialog: DialogUnify? = null

    // product specification
    private var productSpecificationLayout: ViewGroup? = null
    private var productSpecificationTextView: Typography? = null
    private var addProductSpecificationButton: Typography? = null
    private var productSpecificationReloadLayout: View? = null
    private var productSpecificationReloadButton: Typography? = null

    // product price
    private var productPriceField: TextFieldUnify? = null
    private var productPriceEditIcon: ImageView? = null
    private var productPriceBulkEditDialog: DialogUnify? = null
    private var productPriceRecommendation: TooltipCardViewSelectable? = null

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

    // product sku
    private var productSkuField: TextFieldUnify? = null

    // product show case
    private var productShowCasesView: Typography? = null
    private var addProductShowCaseButton: Typography? = null
    private var productShowCasesReloadLayout: View? = null
    private var productShowCasesReloadButton: Typography? = null

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
        // start PLT monitoring
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cacheManagerId = AddEditProductDetailFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

            // set detail and variant input model
            cacheManagerId.run {
                viewModel.productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                        ?: ProductInputModel()
                var pictureIndex = 0
                viewModel.productPhotoPaths = viewModel.productInputModel.detailInputModel.imageUrlOrPathList.map { urlOrPath ->
                    if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) viewModel.productInputModel.detailInputModel.pictureList[pictureIndex++].urlThumbnail
                    else urlOrPath
                }.toMutableList()
                viewModel.isEditing = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isAdding = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isDrafting = saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java)
                        ?: false
                viewModel.isFirstMoved = saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java)
                        ?: false
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

        // set bg color programatically, to reduce overdraw
        context?.let { activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)) }

        // to check whether current fragment is visible or not
        isFragmentVisible = true

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
        productNameRecAdapter = NameRecommendationAdapter(this)
        productNameRecView?.let {
            it.adapter = productNameRecAdapter
            it.layoutManager = LinearLayoutManager(context)
        }
        productNameField?.editText?.apply {
            setHorizontallyScrolling(false)
            isSingleLine = false
            imeOptions = EditorInfo.IME_ACTION_DONE
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
        setupProductNameValidationBottomsheet()

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
                if (viewModel.specificationList.isNotEmpty()) {
                    showChangeCategoryDialog {
                        startCategoryActivity(REQUEST_CODE_CATEGORY)
                    }
                } else {
                    startCategoryActivity(REQUEST_CODE_CATEGORY)
                }
            }
        }

        // add product specification button
        productSpecificationLayout = view.findViewById(R.id.add_edit_product_specification_layout)
        productSpecificationTextView = view.findViewById(R.id.tv_product_specification)
        addProductSpecificationButton = view.findViewById(R.id.tv_add_product_specification)
        productSpecificationReloadLayout = view.findViewById(R.id.reload_product_specification_layout)
        productSpecificationReloadButton = view.findViewById(R.id.tv_reload_specification_button)

        // add edit product price views
        productPriceField = view.findViewById(R.id.tfu_product_price)
        productPriceEditIcon = view.findViewById(R.id.ic_edit_price)

        // add edit product price recommendation views
        productPriceRecommendation = view.findViewById(R.id.add_edit_product_product_recommendation_layout)

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
                        validateWholeSaleInput(viewModel, productWholeSaleInputFormsView, it - 1, deletePosition
                                ?: -1)
                        // to avoid enable button submit when we delete the last of whole sale
                        if (deletePosition == it - 1) {
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

        // add product showcase button
        productShowCasesView = view.findViewById(R.id.tv_product_showcases)
        addProductShowCaseButton = view.findViewById(R.id.tv_add_product_showcases)
        productShowCasesReloadLayout = view.findViewById(R.id.reload_product_showcase_layout)
        productShowCasesReloadButton = view.findViewById(R.id.tv_reload_showcases_button)

        // submit button
        submitButton = view.findViewById(R.id.btn_submit)
        submitTextView = view.findViewById(R.id.tv_submit_text)
        submitLoadingIndicator = view.findViewById(R.id.lu_submit_loading_indicator)
        setupButton()

        // fill the form with detail input model
        fillProductDetailForm(viewModel.productInputModel.detailInputModel)

        // execute getShopShowCasesUseCase when the showcases contains nameless show case
        val productShowCases = viewModel.productShowCases.map { it.showcaseName }
        if (productShowCases.contains("")) viewModel.getShopShowCasesUseCase()

        addProductPhotoButton?.setOnClickListener(createAddProductPhotoButtonOnClickListener())

        productNameField?.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) productNameRecView?.hide()
        }

        // product showcase
        addProductShowCaseButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER, ShowcasePickerType.CHECKBOX)
            bundle.putParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER, ArrayList(viewModel.productShowCases))
            val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(EXTRA_BUNDLE, bundle)
            startActivityForResult(intent, SHOWCASE_PICKER_RESULT_REQUEST_CODE)
        }

        // product name text change listener
        productNameField?.editText?.afterTextChanged { editable ->
            // make sure when user is typing the field, the behaviour to get categories is not blocked by this variable
            if (needToSetCategoryName && editable.isNotBlank()) {
                needToSetCategoryName = false
            }

            viewModel.setProductNameInput(editable)
            showProductNameLoadingIndicator()
            showPriceRecommendationShimmer()
        }

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
                    // price recommendation
                    val suggestedPrice  = viewModel.productPriceRecommendation.value?.suggestedPrice.orZero()
                    if (it.toDoubleOrZero() == 0.toDouble() || suggestedPrice != it.toDoubleOrZero()) {
                        productPriceRecommendation?.displaySuggestedPriceDeselected()
                    } else {
                        productPriceRecommendation?.displaySuggestedPriceSelected()
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

        // product minimum order text change listener
        productSkuField?.textFieldInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val skuInput = charSequence?.toString()
                skuInput?.let { viewModel.validateProductSkuInput(it) }
            }
        })

        // product showcase refresh button
        productShowCasesReloadButton?.setOnClickListener {
            // prevent rapid reloading with isReloadingShowCase flag in view model
            if (!viewModel.isReloadingShowCase) {
                viewModel.getShopShowCasesUseCase()
                viewModel.isReloadingShowCase = true
            }
        }

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

        // Setup default message for stock if shop admin or owner
        viewModel.setupMultiLocationShopValues()
        productStockField?.setMessage(viewModel.productStockMessage)

        setupSpecificationField()
        setupProductPriceRecommendationField()
        enableProductNameField()
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
        subscribeToProductSkuInputStatus()
        subscribeToShopShowCases()
        subscribeToSpecificationList()
        subscribeToSpecificationText()
        subscribeToInputStatus()
        subscribeToPriceRecommendation()

        // stop PLT monitoring, because no API hit at load page
        stopPreparePagePerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            inputAllDataInProductInputModel()
            outState.putString(KEY_SAVE_INSTANCE_INPUT_MODEL, mapObjectToJson(viewModel.productInputModel))
            outState.putBoolean(KEY_SAVE_INSTANCE_ISADDING, viewModel.isAdding)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISEDITING, viewModel.isEditing)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISDRAFTING, viewModel.isDrafting)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED, viewModel.isFirstMoved)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val productInputModelJson = savedInstanceState.getString(KEY_SAVE_INSTANCE_INPUT_MODEL)
            viewModel.isAdding = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISADDING)
            viewModel.isEditing = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISEDITING)
            viewModel.isDrafting = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISDRAFTING)
            viewModel.isFirstMoved = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED)

            if (!productInputModelJson.isNullOrBlank()) {
                //set product input model and and ui of the page
                val productInputModel = mapJsonToObject(productInputModelJson, ProductInputModel::class.java)
                viewModel.productInputModel = productInputModel
                if (!productInputModel.detailInputModel.imageUrlOrPathList.isNullOrEmpty()) {
                    viewModel.productPhotoPaths = productInputModel.detailInputModel.imageUrlOrPathList as MutableList<String>
                }
                fillProductDetailForm(productInputModel.detailInputModel)
                setupButton()
            }
            // only need set category, no need to get category list
            needToSetCategoryName = true
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentVisible = false
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

    private fun setupButton() {
        if (viewModel.isAdding && viewModel.isFirstMoved) {
            submitTextView?.text = getString(R.string.action_continue)
        } else {
            submitTextView?.text = getString(R.string.action_save)
        }
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

        // product sku validation
        val productSkuInput = productSkuField?.getEditableValue().toString()
        viewModel.validateProductSkuInput(productSkuInput)
        viewModel.isProductSkuInputError.value?.run {
            if (this && !requestedFocus) {
                productSkuField?.requestFocus()
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
                    val result = ImagePickerResultExtractor.extract(data)
                    val newUpdatedPhotos = viewModel.updateProductPhotos(result.imageUrlOrPathList,
                            result.originalImageUrl, result.isEditted)
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
                    productCategoryRecListView?.setToDisplayText(categoryName.orEmpty(), requireContext())

                    // clear specification, get new annotation spec
                    getAnnotationCategory()

                    // only need set category, no need to get category list
                    needToSetCategoryName = true
                }
                SHOWCASE_PICKER_RESULT_REQUEST_CODE -> {
                    val selectedShowcaseList: ArrayList<ShowcaseItemPicker> = data.getParcelableArrayListExtra(EXTRA_PICKER_SELECTED_SHOWCASE)
                            ?: ArrayList()
                    // update the view model state
                    viewModel.updateProductShowCases(selectedShowcaseList)
                    if (selectedShowcaseList.isNotEmpty()) {
                        // display the show case names with comma separator
                        displayProductShowCaseNames(selectedShowcaseList.map { it.showcaseName })
                    } else displayProductShowCaseTips()
                }
                REQUEST_CODE_SPECIFICATION -> {
                    val cacheManagerId = data.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID).orEmpty()
                    val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

                    saveInstanceCacheManager.get(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL,
                            ProductInputModel::class.java, viewModel.productInputModel)?.apply {
                        viewModel.updateSpecification(detailInputModel.specifications.orEmpty())
                    }
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
        var newProductName = productName
        val maxLengthKeyword = resources.getInteger(R.integer.max_product_name_length)

        if (productName.trim().length > maxLengthKeyword) {
            newProductName = productName.take(maxLengthKeyword)
        }

        productNameRecView?.hide()
        viewModel.isNameRecommendationSelected = true
        productNameField?.editText?.setText(newProductName)
        productNameField?.editText?.setSelection(newProductName.length)

        if (viewModel.isAdding) {
            ProductAddMainTracking.clickProductNameRecom(shopId, productName)
        }
    }

    override fun onWholeSaleQuantityItemTextChanged(position: Int, input: String) {
        if (productWholeSaleInputFormsView?.layoutManager?.itemCount == wholeSaleInputFormsAdapter?.itemCount) {
            val itemView = productWholeSaleInputFormsView?.layoutManager?.getChildAt(position)
            val quantityField: TextFieldUnify? = itemView?.findViewById(R.id.tfu_wholesale_quantity)
            val minOrderInput = productMinOrderField.getTextIntOrZero().toString()
            val previousQuantity = wholeSaleInputFormsAdapter?.getPreviousQuantity(position) ?: ""
            val errorMessage = viewModel.validateProductWholeSaleQuantityInput(input, minOrderInput, previousQuantity)
            // to avoid enable button submit when we edit the last of whole sale
            if (position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
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
            if (position == wholeSaleInputFormsAdapter?.itemCount?.minus(1)) {
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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
        viewModel.productInputModel.isDataChanged = true
        viewModel.productInputModel.detailInputModel.apply {
            productName = productNameField.getText()
            price = productPriceField.getTextBigIntegerOrZero()
            stock = productStockField.getTextIntOrZero()
            condition = if (isProductConditionNew) CONDITION_NEW else CONDITION_USED
            minOrder = productMinOrderField.getTextIntOrZero()
            sku = productSkuField.getText()
            imageUrlOrPathList = viewModel.productPhotoPaths
            if (!productPictureList.isNullOrEmpty()) pictureList = productPictureList ?: listOf()
            if (productCategoryId.isNotBlank()) categoryId = productCategoryId
            if (productCategoryName.isNotBlank()) categoryName = productCategoryName
            preorder.apply {
                duration = preOrderDurationField.getTextIntOrZero()
                timeUnit = selectedDurationPosition
                isActive = preOrderSwitch?.isChecked ?: false
            }
            wholesaleList = getWholesaleInput()
            productShowCases = viewModel.productShowCases
            specifications = viewModel.specificationList
        }
    }

    private fun validateWholeSaleInput(viewModel: AddEditProductDetailViewModel, wholesaleInputForms: RecyclerView?, itemCount: Int?, specialIndex: Int = -1, isAddingWholeSale: Boolean = false) {
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
        productNameField.setText(detailInputModel.productName)

        // product price
        val productPrice = detailInputModel.price
        if (productPrice != 0.toBigInteger()) {
            productPriceField?.textFieldInput?.setText(InputPriceUtil.formatProductPriceInput(detailInputModel.price.toString()))
        }
        if (viewModel.hasVariants) productPriceField?.textFieldInput?.isEnabled = false

        // product category
        if (detailInputModel.categoryName.isNotBlank()) {
            productCategoryLayout?.show()
            productCategoryRecListView?.setToDisplayText(detailInputModel.categoryName, requireContext())
            productCategoryId = detailInputModel.categoryId
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
                listItemUnify.setTextColorToUnify(requireContext())
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

        // product showcases
        viewModel.updateProductShowCases(ArrayList(detailInputModel.productShowCases))
        val productShowCases = viewModel.productShowCases.map { it.showcaseName }
        if (!productShowCases.contains("")) displayProductShowCaseNames(productShowCases)
    }

    private fun subscribeToProductNameInputStatus() {
        viewModel.isProductNameInputError.observe(viewLifecycleOwner, Observer {
            productNameField?.isInputError = it
            productNameField?.setMessage(viewModel.productNameMessage)
            // if product name input has no issue
            if (!it) {
                val productNameInput = productNameField?.getEditableValue().toString()
                // prevent queries getting called from recursive name selection and clicked submit button
                if (!viewModel.isNameRecommendationSelected && viewModel.isProductNameChanged) {
                    // show product name recommendations
                    productNameRecAdapter?.setProductNameInput(productNameInput)
                    viewModel.getProductNameRecommendation(query = productNameInput)
                }
                // show category recommendations to the product that has no variants and no category name before
                if (viewModel.isAdding && !viewModel.hasVariants && !needToSetCategoryName) {
                    viewModel.getCategoryRecommendation(productNameInput)
                }
                showProductNameIconSuccess()
            } else {
                // show empty recommendations for input with error
                productNameRecAdapter?.setProductNameRecommendations(emptyList())
                hideProductNameLoadingIndicator()
                // keep the category
                if (viewModel.isAdding && !viewModel.hasVariants) {
                    productCategoryRecListView?.setData(ArrayList(emptyList()))
                }
                showProductNameIconBlacklist()
            }

            if (needToSetCategoryName) {
                productCategoryLayout?.show()
                if (productCategoryName.isBlank()) {
                    productCategoryName = viewModel.productInputModel.detailInputModel.categoryName
                }
                productCategoryRecListView?.setToDisplayText(productCategoryName, requireContext())
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

    private fun subscribeToProductSkuInputStatus() {
        viewModel.isProductSkuInputError.observe(viewLifecycleOwner, Observer {
            productSkuField?.setError(it)
            productSkuField?.setMessage(viewModel.productSkuMessage)
        })
    }

    private fun subscribeToShopShowCases() {
        viewModel.shopShowCases.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    // shop show cases; the source of truth
                    val shopShowCases = result.data
                    // show case ids from getProductV3 response
                    val selectedShowCaseIds = viewModel.productShowCases.map { it.showcaseId }
                    val selectedShowCases = shopShowCases.filter { showCase ->
                        selectedShowCaseIds.contains(showCase.id)
                    }
                    // convert ShowCaseItems to ShowCaseItemPicker collections
                    val selectedProductShowCases = selectedShowCases.map { showCaseItem ->
                        ShowcaseItemPicker(showcaseId = showCaseItem.id, showcaseName = showCaseItem.name)
                    }
                    // update the show case item picker collections in view model
                    viewModel.updateProductShowCases(ArrayList(selectedProductShowCases))
                    if (selectedProductShowCases.isNotEmpty()) {
                        // display the show case names with comma separator
                        displayProductShowCaseNames(selectedProductShowCases.map { it.showcaseName })
                    } else displayProductShowCaseTips()
                    // hide the reload layout only when the view is visible
                    productShowCasesReloadLayout?.run {
                        if (this.isVisible) {
                            productShowCasesReloadLayout?.hide()
                            productShowCasesView?.show()
                            // update the reloading state in view model
                            viewModel.isReloadingShowCase = false
                        }
                    }

                }
                is Fail -> {
                    // hide the tips and show the reload button
                    productShowCasesView?.hide()
                    productShowCasesReloadLayout?.show()
                    // update the reloading state in view model
                    viewModel.isReloadingShowCase = false
                }
            }
        })
    }

    private fun subscribeToSpecificationList() {
        viewModel.annotationCategoryData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    productSpecificationLayout?.isVisible = result.data.isNotEmpty()
                    productSpecificationTextView?.show()
                    productSpecificationReloadLayout?.hide()
                    viewModel.updateSpecificationByAnnotationCategory(result.data)
                }
                is Fail -> {
                    productSpecificationTextView?.hide()
                    productSpecificationReloadLayout?.show()
                }
            }
        })
    }

    private fun subscribeToSpecificationText() {
        viewModel.specificationText.observe(viewLifecycleOwner, Observer {
            productSpecificationTextView?.text = it
            addProductSpecificationButton?.text = if (viewModel.specificationList.isEmpty()) {
                getString(R.string.action_specification_add)
            } else {
                getString(R.string.action_specification_change)
            }
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

    private fun subscribeToPriceRecommendation() {
        if (viewModel.isAdding) {
            subscribeToAddingPriceRecommendation()
        } else {
            subscribeToEditingPriceRecommendation()
        }
        viewModel.productPriceRecommendationError.observe(viewLifecycleOwner) {
            productPriceRecommendation?.isVisible = false
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        }
    }

    private fun subscribeToAddingPriceRecommendation() {
        viewModel.productPriceRecommendation.observe(viewLifecycleOwner) { priceSuggestion ->
            val inputPrice = productPriceField.getTextBigIntegerOrZero()
            val minText = priceSuggestion.suggestedPriceMin.getCurrencyFormatted()
            val maxText = priceSuggestion.suggestedPriceMax.getCurrencyFormatted()
            val descriptionText = getString(R.string.label_price_recommendation_description, minText, maxText)
            val priceDescriptionText = getString(R.string.label_price_recommendation_price_description, priceSuggestion.title)

            productPriceRecommendation?.apply {
                setShimmerVisibility(false)
                description = descriptionText
                priceDescription = priceDescriptionText
                price = priceSuggestion.suggestedPrice.getCurrencyFormatted()
                isVisible = (priceSuggestion.suggestedPrice != 0.toDouble())
                if (inputPrice == priceSuggestion.suggestedPrice.toBigDecimal().toBigInteger()) {
                    displaySuggestedPriceSelected()
                } else {
                    displaySuggestedPriceDeselected()
                }
            }
        }
    }

    private fun subscribeToEditingPriceRecommendation() {
        //observe only if (1) product has product id, (2) if seller app, (3) has no variant
        if (viewModel.productInputModel.productId == 0L || !GlobalConfig.isSellerApp()
                || viewModel.hasVariants) return

        viewModel.productPriceRecommendation.observe(viewLifecycleOwner) {
            val productPrice = viewModel.productInputModel.detailInputModel.price
            val productSuggestedPrice = it.suggestedPrice.toBigDecimal().toBigInteger()
            val priceWhenLoaded = SharedPreferencesUtil.getPriceWhenLoaded(requireActivity())

            // hide/ show price recommendation
            productPriceRecommendation?.isVisible = it.suggestedPrice > 0.0
                    && priceWhenLoaded > it.suggestedPrice.toBigDecimal().toBigInteger()

            // display suggestion only when price recomendation visible
            if (productPriceRecommendation?.isVisible == true) {
                val minText = it.suggestedPriceMin.getCurrencyFormatted()
                val maxText = it.suggestedPriceMax.getCurrencyFormatted()
                val descriptionText = getString(R.string.label_price_recommendation_description, minText, maxText)

                productPriceRecommendation?.price = it.suggestedPrice.getCurrencyFormatted()
                productPriceRecommendation?.description = descriptionText
            }

            // expand/ collapse price recommendation
            if (productPrice <= productSuggestedPrice) {
                productPriceRecommendation?.setSuggestedPriceSelected()
            }
        }
        viewModel.getProductPriceRecommendation()
    }

    private fun createAddProductPhotoButtonOnClickListener(): View.OnClickListener {
        return View.OnClickListener {

            val adapter = productPhotoAdapter ?: return@OnClickListener

            // show error message when maximum product image is reached
            val productPhotoSize = adapter.getProductPhotoPaths().size
            if (productPhotoSize == MAX_PRODUCT_PHOTOS) showMaxProductImageErrorToast(getString(R.string.error_max_product_photo))
            else {
                val imageUrlOrPathList = productPhotoAdapter?.getProductPhotoPaths()?.map { urlOrPath ->
                    val pictureList = viewModel.productInputModel.detailInputModel.pictureList
                    if (urlOrPath.startsWith(AddEditProductConstants.HTTP_PREFIX)) pictureList.find {
                        it.urlThumbnail == urlOrPath
                    }?.urlOriginal ?: urlOrPath
                    else urlOrPath
                }.orEmpty()
                openImagePickerAddPhoto(imageUrlOrPathList)
            }
        }
    }

    private fun openImagePickerAddPhoto(imageUrlOrPathList: List<String>) {
        val ctx = context ?: return
        val isEditing = viewModel.isEditing
        val isAdding = viewModel.isAdding || !isEditing

        // tracking
        if (isEditing) {
            ProductEditMainTracking.trackAddPhoto(shopId)
        } else {
            ProductAddMainTracking.trackAddPhoto(shopId)
        }
        val intent = ImagePickerAddEditNavigation.getIntent(ctx,ArrayList(imageUrlOrPathList), isAdding)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun setupSpecificationField() {
        // get annotation category, if not already obtained from the server (specifications == null)
        val specifications = viewModel.productInputModel.detailInputModel.specifications
        if (specifications != null) {
            viewModel.updateSpecification(specifications)
        } else {
            getAnnotationCategory()
        }

        addProductSpecificationButton?.setOnClickListener {
            showSpecificationPicker()
        }

        productSpecificationReloadButton?.setOnClickListener {
            getAnnotationCategory()
        }
    }

    private fun setupProductPriceRecommendationField() {
        productPriceRecommendation?.apply {
            if (viewModel.isAdding) hideIconCheck()
            setPriceDescriptionVisibility(viewModel.isAdding)
            setButtonToBlack()
            setOnButtonNextClicked {
                showProductPriceRecommendationTips()
            }
            setOnSuggestedPriceSelected { suggestedPrice ->
                productPriceField.setText(suggestedPrice)
                if (viewModel.isAdding) {
                    displaySuggestedPriceSelected()
                }
            }
        }
    }

    private fun getAnnotationCategory() {
        val productId = viewModel.productInputModel.productId

        productSpecificationLayout?.gone()
        viewModel.getAnnotationCategory(productCategoryId, if (productId > 0) {
            productId.toString()
        } else {
            ""
        })
    }

    private fun showSpecificationPicker(){
        context?.run {
            val productInputModel = viewModel.productInputModel
            productInputModel.detailInputModel.apply {
                if (productCategoryId.isNotBlank()) categoryId = productCategoryId
                if (productCategoryName.isNotBlank()) categoryName = productCategoryName
                specifications = viewModel.specificationList
            }

            val cacheManager = SaveInstanceCacheManager(this, true)
            cacheManager.put(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val intent = AddEditProductSpecificationActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_SPECIFICATION)
        }
    }

    private fun showMaxProductImageErrorToast(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, type = Toaster.TYPE_ERROR).show()
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

    private fun showChangeCategoryDialog(onAccepted: () -> Unit) {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.message_change_category_title))
            setDescription(getString(R.string.message_change_category))
            setSecondaryCTAText(getString(R.string.action_change_category_positive))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                onAccepted()
            }
            setPrimaryCTAText(getString(R.string.action_change_category_negative))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun setupProductNameValidationBottomsheet() {
        productNameField?.icon2?.setOnClickListener {
            val titleValidationModel = viewModel.productNameValidationResult
            TitleValidationBottomSheet(titleValidationModel).show(childFragmentManager)
        }
    }

    private fun showProductPriceRecommendationTips() {
        val tooltipBottomSheet = TooltipBottomSheet()
        val tips: ArrayList<NumericWithDescriptionTooltipModel> = ArrayList()
        val tooltipTitle = getString(R.string.title_price_recommendation_bottom_sheet)
        val contentTitles = resources.getStringArray(R.array.array_price_recommendation_content_titles)
        val contentDescriptions = resources.getStringArray(R.array.array_price_recommendation_content_descriptions)

        contentTitles.forEachIndexed { index, title ->
            val description = contentDescriptions.getOrNull(index).orEmpty()
            tips.add(NumericWithDescriptionTooltipModel(title, description))
        }

        tooltipBottomSheet.apply {
            setTitle(tooltipTitle)
            setItemMenuList(tips)
            setDividerVisible(false)
            setBannerImage(PRICE_RECOMMENDATION_BANNER_URL)
        }
        tooltipBottomSheet.show(childFragmentManager, null)
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

    private fun enableSubmitButton() {
        submitButton?.isClickable = true
        submitButton?.setBackgroundResource(R.drawable.product_add_edit_rect_green_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)) }
    }

    private fun disableSubmitButton() {
        submitButton?.isClickable = false
        submitButton?.setBackgroundResource(R.drawable.rect_grey_solid)
        context?.let { submitTextView?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)) }
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
            NavigationController.navigate(this@AddEditProductDetailFragment, destination)
        }
    }

    private fun subscribeToProductNameRecommendation() {
        observe(viewModel.productNameRecommendations) {
            when (it) {
                is Success -> {
                    productNameRecView?.visible()
                    productNameRecAdapter?.setProductNameRecommendations(it.data)
                }
                is Fail -> {
                    productNameRecView?.hide()
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
            hideProductNameLoadingIndicator()
        }
    }

    private fun showProductNameLoadingIndicator() {
        productNameField?.isLoading = true
        productNameRecShimmering?.visible()
    }

    private fun showPriceRecommendationShimmer() {
        if (viewModel.isAdding) {
            productPriceRecommendation?.show()
            productPriceRecommendation?.displaySuggestedPriceDeselected()
            productPriceRecommendation?.setShimmerVisibility(true)
        }
    }

    private fun hideProductNameLoadingIndicator() {
        productNameField?.isLoading = false
        productNameRecShimmering?.hide()
    }

    private fun showProductNameIconSuccess() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_G500, IconUnify.CHECK_CIRCLE)
    }

    private fun showProductNameIconTypo() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_N700, IconUnify.INFORMATION)
    }

    private fun showProductNameIconNegative() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_Y300, IconUnify.INFORMATION)
    }

    private fun showProductNameIconBlacklist() {
        showProductNameIconIndicator(com.tokopedia.unifyprinciples.R.color.Unify_R500, IconUnify.INFORMATION)
    }

    private fun showProductNameIconIndicator(colorResource: Int, iconResource: Int) {
        val color = ContextCompat.getColor(requireContext(), colorResource)
        val iconDrawable = getIconUnifyDrawable(requireContext(), iconResource, color)
        productNameField?.icon2?.loadImage(iconDrawable)
        productNameField?.icon2?.show()
    }

    private fun showImmutableCategoryDialog() {
        categoryAlertDialog?.show()
    }

    private fun displayProductShowCaseNames(productShowCaseNames: List<String>) {
        productShowCasesView?.text = productShowCaseNames.joinToString()
    }

    private fun displayProductShowCaseTips() {
        productShowCasesView?.text = getString(R.string.label_product_showcase_tips)
    }

    private fun onGetCategoryRecommendationSuccess(result: Success<List<ListItemUnify>>) {
        hasCategoryFromPicker = false
        productCategoryLayout?.show()
        productCategoryRecListView?.show()
        val items = ArrayList(result.data.take(3))
        productCategoryRecListView?.setData(items)
        productCategoryRecListView?.onLoadFinish {
            selectFirstCategoryRecommendation(items)

            productCategoryRecListView?.setOnItemClickListener { _, _, position, _ ->
                if (viewModel.isAdding) {
                    ProductAddMainTracking.clickProductCategoryRecom(shopId)
                }

                // display confirmation if product has a specs
                if (viewModel.specificationList.isEmpty()) {
                    selectCategoryRecommendation(items, position)
                } else {
                    showChangeCategoryDialog {
                        selectCategoryRecommendation(items, position)
                    }
                }
            }

            items.forEachIndexed { position, listItemUnify ->
                listItemUnify.setTextColorToUnify(requireContext())
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
                getAnnotationCategory() // update annotation specification
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

    private fun startCategoryActivity(requestCodeCategory: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER, 0.toString())
        intent.putExtra(AddEditProductConstants.EXTRA_IS_EDIT_MODE, (viewModel.isEditing))
        startActivityForResult(intent, requestCodeCategory)
    }

    private fun enableProductNameField() {
        productNameField?.isEnabled = !viewModel.hasTransaction
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
        val previousPrice = wholeSaleInputFormsAdapter?.getPreviousPrice(position - 1)?.replace(".", "")
                ?: ""
        val validation = viewModel.validateProductWholeSalePriceInput(price, productPriceInput, previousPrice)
        viewModel.isTheLastOfWholeSale.value = validation.isNotEmpty()
        return validation
    }
}
