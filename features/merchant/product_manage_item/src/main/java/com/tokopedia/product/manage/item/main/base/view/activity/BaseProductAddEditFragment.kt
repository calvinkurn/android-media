package com.tokopedia.product.manage.item.main.base.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.analytics.nishikino.model.EventTracking
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_IS_EDITTED
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.activity.ProductEditCategoryActivity
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.common.util.ProductVariantConstant
import com.tokopedia.product.manage.item.common.util.StockTypeDef
import com.tokopedia.product.manage.item.description.view.activity.ProductEditDescriptionActivity
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.manage.item.logistic.view.activity.ProductEditWeightLogisticActivity
import com.tokopedia.product.manage.item.logistic.view.fragment.ProductEditWeightLogisticFragment
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView
import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenterImpl
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel
import com.tokopedia.product.manage.item.main.base.view.listener.ListenerOnErrorAddProduct
import com.tokopedia.product.manage.item.main.base.view.model.ProductAddViewModel
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService
import com.tokopedia.product.manage.item.name.view.activity.ProductEditNameActivity
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.price.model.ProductPrice
import com.tokopedia.product.manage.item.price.view.activity.ProductEditPriceActivity
import com.tokopedia.product.manage.item.stock.view.activity.ProductEditStockActivity
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.utils.*
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel
import kotlinx.android.synthetic.main.fragment_base_product_edit.*
import javax.inject.Inject
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

abstract class BaseProductAddEditFragment<T : ProductAddPresenterImpl<P>, P : ProductAddView> : BaseDaggerFragment(),
        ProductAddView, ListenerOnErrorAddProduct {

    @Inject
    lateinit var presenter: T

    @ProductStatus
    protected abstract var statusUpload: Int

    private var isHasLoadShopInfo: Boolean = false
    lateinit var cacheManager: SaveInstanceCacheManager
    private var officialStore: Boolean = false
    private var isFreeReturn: Boolean = false
    private var isGoldMerchant: Boolean = false
    val appRouter: Context? by lazy { activity?.application as? Context }
    protected var currentProductAddViewModel: ProductAddViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cacheManager = SaveInstanceCacheManager(context!!, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        presenter.getShopInfo()

        savedInstanceState?.run {
            if (containsKey(EXTRA_IS_OFFICIAL_STORE)) {
                officialStore = getBoolean(EXTRA_IS_OFFICIAL_STORE)
            }
            if (containsKey(EXTRA_IS_FREE_RETURN)) {
                isFreeReturn = getBoolean(EXTRA_IS_FREE_RETURN)
            }
            if (containsKey(EXTRA_IS_GOLD_MERCHANT)) {
                isGoldMerchant = getBoolean(EXTRA_IS_GOLD_MERCHANT)
            }
            currentProductAddViewModel = cacheManager.get(SAVED_PRODUCT_VIEW_MODEL, ProductAddViewModel::class.java)
        }
        if (currentProductAddViewModel == null) {
            currentProductAddViewModel = ProductAddViewModel()
        }
        return inflater.inflate(R.layout.fragment_base_product_edit, container, false)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentProductAddViewModel?.productCategory?.categoryId?.let { if (it > 0) presenter.fetchProductVariantByCat(it.toLong()) }

        populateView(currentProductAddViewModel)

        llCategoryCatalog.setOnClickListener { startCatalogActivity() }
        labelViewNameProduct.setOnClickListener { startNameActivity() }
        labelViewPriceProduct.setOnClickListener { startPriceActivity() }
        labelViewDescriptionProduct.setOnClickListener { startDescriptionActivity() }
        labelViewStockProduct.setOnClickListener { startStockActivity() }
        labelViewWeightLogisticProduct.setOnClickListener { startLogisticActivity() }
        labelViewEtalaseProduct.setOnClickListener { startProductEtalaseActivity() }
        labelViewVariantProduct.setOnClickListener { startProductVariantActivity() }
        containerImageProduct.setOnClickListener { onAddImagePickerClicked() }
        button_save.setOnClickListener {
            if (currentProductAddViewModel?.isDataValid(this) == true) {
                saveDraft(true)
            }
        }
    }

    private fun startCatalogActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditCategoryActivity.createIntent(this,
                    currentProductAddViewModel?.productName?.name, currentProductAddViewModel?.productCategory,
                    currentProductAddViewModel?.productCatalog, currentProductAddViewModel?.productVariantViewModel?.hasSelectedVariant()
                    ?: false), REQUEST_CODE_GET_CATALOG_CATEGORY)
        }
    }

    private fun startNameActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditNameActivity.createIntent(this,
                    currentProductAddViewModel?.productName, currentProductAddViewModel?.isProductNameEditable
                    ?: true), REQUEST_CODE_GET_NAME)
        }
    }

    private fun startPriceActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditPriceActivity.createIntent(this, currentProductAddViewModel?.productPrice, officialStore,
                    currentProductAddViewModel?.productVariantViewModel?.hasSelectedVariant()
                            ?: false, isGoldMerchant), REQUEST_CODE_GET_PRICE)
        }
    }

    private fun startDescriptionActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditDescriptionActivity.createIntent(this, currentProductAddViewModel?.productDescription,
                    currentProductAddViewModel?.productName?.name), REQUEST_CODE_GET_DESCRIPTION)
        }
    }

    private fun startStockActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditStockActivity.createIntent(this, currentProductAddViewModel?.productStock,
                    currentProductAddViewModel?.productVariantViewModel?.hasSelectedVariant()
                            ?: false, isAddStatus()), REQUEST_CODE_GET_STOCK)
        }
    }

    private fun startLogisticActivity() {
        activity?.run {
            this@BaseProductAddEditFragment.startActivityForResult(ProductEditWeightLogisticActivity.createIntent(this,
                    currentProductAddViewModel?.productLogistic, isFreeReturn), REQUEST_CODE_GET_LOGISTIC)
        }
    }

    private fun startProductEtalaseActivity() {
        if (appRouter != null && appRouter is ProductEditModuleRouter) {
            activity?.run {
                this@BaseProductAddEditFragment.startActivityForResult((appRouter as ProductEditModuleRouter).createIntentProductEtalase(activity, currentProductAddViewModel?.etalaseId
                        ?: -1),
                        REQUEST_CODE_GET_ETALASE)
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager.onSave(outState)
        cacheManager.put(SAVED_PRODUCT_VIEW_MODEL, currentProductAddViewModel)
        outState.putBoolean(EXTRA_IS_GOLD_MERCHANT, isGoldMerchant)
        outState.putBoolean(EXTRA_IS_OFFICIAL_STORE, officialStore)
        outState.putBoolean(EXTRA_IS_FREE_RETURN, isFreeReturn)
    }

    override fun getScreenName(): String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG_CATEGORY -> {
                    val productCatalog: ProductCatalog = data.getParcelableExtra(EXTRA_CATALOG)
                    val productCategory: ProductCategory = data.getParcelableExtra(EXTRA_CATEGORY)
                    onCategoryChanged(productCategory)
                    currentProductAddViewModel?.productCatalog = productCatalog
                    currentProductAddViewModel?.productCategory = productCategory
                }
                REQUEST_CODE_GET_NAME -> {
                    val productName: ProductName = data.getParcelableExtra(EXTRA_NAME)
                    if (productName.name != currentProductAddViewModel?.productName?.name) {
                        currentProductAddViewModel?.resetCatalog()
                    }
                    currentProductAddViewModel?.productName = productName
                }
                REQUEST_CODE_GET_LOGISTIC -> {
                    val productLogistic: ProductLogistic = data.getParcelableExtra(EXTRA_LOGISTIC)
                    currentProductAddViewModel?.productLogistic = productLogistic
                }
                REQUEST_CODE_GET_STOCK -> {
                    val productStock: ProductStock = data.getParcelableExtra(EXTRA_STOCK)
                    if (currentProductAddViewModel?.productStock?.stockCount ?: 0 != productStock.stockCount) {
                        currentProductAddViewModel?.productVariantViewModel?.changeStockTo(getStatusStockViewVariant(productStock))
                    }
                    currentProductAddViewModel?.productStock = productStock
                }
                REQUEST_CODE_GET_PRICE -> {
                    val productPrice: ProductPrice = data.getParcelableExtra(EXTRA_PRICE)
                    if (productPrice.price != currentProductAddViewModel?.productPrice?.price) {
                        currentProductAddViewModel?.changePriceTo(productPrice.price)
                    }
                    val isMoveToGm: Boolean = data.getBooleanExtra(EXTRA_IS_MOVE_TO_GM, false)
                    if (isMoveToGm) {
                        saveDraft(false)
                        eventClickYesGoldMerchantAddProduct()
                        goToGoldMerchantPage()
                        activity?.finish()
                    }
                    currentProductAddViewModel?.productPrice = productPrice
                }
                REQUEST_CODE_GET_DESCRIPTION -> {
                    val productDescription: ProductDescription = data.getParcelableExtra(EXTRA_DESCRIPTION)
                    currentProductAddViewModel?.productDescription = productDescription
                }
                REQUEST_CODE_GET_ETALASE -> {
                    val etalaseId = data.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1)
                    val etalaseNameString = data.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME)
                    currentProductAddViewModel?.etalaseId = etalaseId
                    currentProductAddViewModel?.etalaseName = etalaseNameString
                }
                REQUEST_CODE_GET_IMAGES -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    currentProductAddViewModel?.productPictureList = currentProductAddViewModel?.let {
                        imageUrlOrPathList?.convertImageListResult(it,
                                data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE),
                                data.getSerializableExtra(RESULT_IS_EDITTED) as ArrayList<Boolean>?)
                    }
                }
                REQUEST_CODE_VARIANT -> {
                    if (data.hasExtra(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        val productVariantViewModel = data.getParcelableExtra<ProductVariantViewModel>(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION)
                        setVariantModel(productVariantViewModel)
                    }
                    if (data.hasExtra(ProductExtraConstant.EXTRA_PRODUCT_SIZECHART)) {
                        val productPictureViewModel = data.getParcelableExtra<ProductPictureViewModel>(ProductExtraConstant.EXTRA_PRODUCT_SIZECHART)
                        currentProductAddViewModel?.productSizeChart = productPictureViewModel
                    }
                }
            }
            populateView(currentProductAddViewModel)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun eventClickYesGoldMerchantAddProduct() {
        TrackApp.getInstance()!!.gtm.sendGeneralEvent(
            AppEventTracking.Event.CLICK_GOLD_MERCHANT,
            AppEventTracking.Category.GOLD_MERCHANT,
            AppEventTracking.Action.CLICK,
            AppEventTracking.EventLabel.BUY_GM_ADD_PRODUCT)
    }

    private fun onCategoryChanged(productCategory: ProductCategory) {
        if (currentProductAddViewModel?.productCategory?.categoryId != productCategory.categoryId) {
            currentProductAddViewModel?.productVariantViewModel = null
            currentProductAddViewModel?.productVariantByCatModelList = ArrayList()
            presenter.fetchProductVariantByCat(productCategory.categoryId.toLong())
        }
    }

    private fun goToGoldMerchantPage() {
        if (appRouter != null && appRouter is ProductEditModuleRouter) {
            (appRouter as ProductEditModuleRouter).goToGMSubscribe(activity)
        }
    }

    override fun onSuccessStoreProductToDraft(productId: Long, isUploading: Boolean) {
        if (isUploading) {
            CommonUtils.UniversalToast(activity, getString(R.string.upload_product_waiting))
            startUploadProduct(productId)
        } else {
            CommonUtils.UniversalToast(activity, getString(R.string.product_draft_product_has_been_saved_as_draft))
            activity?.finish()
        }
    }

    override fun onErrorStoreProductToDraftWhenUpload(errorMessage: String?) {
        NetworkErrorHelper.createSnackbarWithAction(activity, getString(R.string.title_try_again)) {
            saveDraft(currentProductAddViewModel?.isDataValid(this) == true)
        }.showRetrySnackbar()
    }

    override fun onErrorStoreProductToDraftWhenBackPressed(errorMessage: String?) {
        CommonUtils.UniversalToast(activity, errorMessage)
        activity?.finish()
    }

    override fun onSuccessLoadShopInfo(goldMerchant: Boolean, freeReturn: Boolean, officialStore: Boolean) {
        this.isGoldMerchant = goldMerchant
        this.isFreeReturn = freeReturn
        this.isHasLoadShopInfo = true
        this.officialStore = officialStore
    }

    override fun onErrorLoadShopInfo(errorMessage: String?) {}

    private fun startUploadProduct(productId: Long) {
        startUploadProductService(productId)
        activity?.finish()
    }

    override fun getProductDraftId() = 0L

    private fun startUploadProductService(productId: Long) {
        activity?.startService(UploadProductService.getIntent(activity, productId, isAddStatus()))
    }

    protected fun populateView(currentProductViewModel: ProductAddViewModel?) {
        ImageHandler.loadImageRounded2(context, imageOne, R.drawable.product_add_image_default, 15.0f)
        ImageHandler.loadImageRounded2(context, imageTwo, R.drawable.product_add_image_default, 15.0f)

        textViewCategory.text = currentProductViewModel?.productCategory?.categoryName
        if (currentProductViewModel?.productCatalog?.catalogId ?: 0 > 0) {
            textViewCatalog.run {
                visibility = View.VISIBLE
                text = currentProductViewModel?.productCatalog?.catalogName
            }
        } else {
            textViewCatalog.run {
                visibility = View.GONE
                text = ""
            }
        }

        if (currentProductViewModel == null) return
        if ((currentProductViewModel.productPictureList?.size ?: 0) > 0) {
            textEditImage.text = getString(R.string.label_edit)
            ImageHandler.loadImageRounded2(context, imageOne, currentProductViewModel.productPictureList?.firstOrNull()?.uriOrPath, 20.0f)
            if ((currentProductViewModel.productPictureList?.size ?: 0) > 1)
                ImageHandler.loadImageRounded2(context, imageTwo, currentProductViewModel.productPictureList?.get(1)?.uriOrPath, 20.0f)
        }
        labelViewNameProduct.setContent(currentProductViewModel.productName?.name)
        if (!TextUtils.isEmpty(currentProductViewModel.productDescription?.description)) {
            labelViewDescriptionProduct.setContent(currentProductViewModel.productDescription?.description)
            labelViewDescriptionProduct.setSubTitle("")
        } else {
            labelViewDescriptionProduct.setContent("")
            labelViewDescriptionProduct.setSubTitle(getString(R.string.product_subtitle_product_description))
        }
        if ((currentProductViewModel.productPrice?.price ?: 0.0) > 0) {
            labelViewPriceProduct.setSubTitle("")
            val currencyString = CurrencyFormatUtil.convertPriceValue(currentProductViewModel.productPrice?.price
                    ?: 0.0, true)
            when (currentProductViewModel.productPrice?.currencyType) {
                CurrencyTypeDef.TYPE_USD -> labelViewPriceProduct.setContent(getString(R.string.usd_format, currencyString))
                CurrencyTypeDef.TYPE_IDR -> labelViewPriceProduct.setContent(getString(R.string.rupiah_format, currencyString))
                else -> labelViewPriceProduct.setContent(getString(R.string.rupiah_format, currencyString))
            }
        }
        if (currentProductViewModel.productLogistic?.weight ?: 0 > 0) {
            labelViewWeightLogisticProduct.setContent("${currentProductViewModel.productLogistic?.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(currentProductViewModel.productLogistic?.weightType!!))}")
            labelViewWeightLogisticProduct.setSubTitle("")
        }
        if (currentProductViewModel.productStock?.isActive == true) {
            if ((currentProductViewModel.productStock?.stockCount ?: 0) > 0) {
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            } else {
                labelViewStockProduct.setContent(getString(R.string.label_always_available))
            }
        } else {
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_empty))
        }

        if (currentProductViewModel.productVariantByCatModelList.size > 0 && currentProductViewModel.productVariantViewModel?.hasSelectedVariant() == true) {
            labelViewVariantProduct.visibility = View.VISIBLE
            val productVariantOptionParentLv1 = currentProductViewModel.productVariantViewModel?.getVariantOptionParent(0)
            val productVariantOptionParentLv2 = currentProductViewModel.productVariantViewModel?.getVariantOptionParent(1)
            var selectedVariantString = "${productVariantOptionParentLv1?.productVariantOptionChild?.size} ${productVariantOptionParentLv1?.name}"
            if (productVariantOptionParentLv2 != null && productVariantOptionParentLv2.hasProductVariantOptionChild()) {
                selectedVariantString = "$selectedVariantString \n ${productVariantOptionParentLv2.productVariantOptionChild.size} ${productVariantOptionParentLv2.name}"
            }
            labelViewVariantProduct.setContent(selectedVariantString)
            labelViewVariantProduct.setSubTitle("")
        } else if (currentProductViewModel.productVariantByCatModelList.size > 0) {
            labelViewVariantProduct.visibility = View.VISIBLE
            labelViewVariantProduct.resetContentText()
            labelViewVariantProduct.setSubTitle(getString(R.string.product_subtitle_product_variant))
        } else {
            labelViewVariantProduct.visibility = View.GONE
        }

        if (currentProductViewModel.etalaseId ?: 0 > 0) {
            labelViewEtalaseProduct.setContent(currentProductViewModel.etalaseName)
            labelViewEtalaseProduct.setSubTitle("")
        }
    }

    fun saveDraft(isUploading: Boolean) {
        if (isUploading) {
            sendAnalyticsAdd(currentProductAddViewModel?.convertToProductViewModel())
        }
        if (currentProductAddViewModel?.productVariantViewModel != null) {
            currentProductAddViewModel?.productVariantViewModel?.generateTid()
        }
        presenter.saveDraft(currentProductAddViewModel?.convertToProductViewModel(), isUploading)
    }

    private fun setVariantModel(productVariantViewModel: ProductVariantViewModel?) {

        if (productVariantViewModel != null && productVariantViewModel.hasSelectedVariant()) {
            @StockTypeDef val stockType = productVariantViewModel.calculateProductStatus
            if (stockType == StockTypeDef.TYPE_ACTIVE_LIMITED) {
                currentProductAddViewModel?.productStock?.isActive = true
                currentProductAddViewModel?.productStock?.stockCount = DEFAULT_PARENT_STOCK_IF_VARIANT
            } else {
                currentProductAddViewModel?.productStock?.isActive = (stockType == StockTypeDef.TYPE_ACTIVE)
                currentProductAddViewModel?.productStock?.stockCount = 0
            }
        }
        currentProductAddViewModel?.productVariantViewModel = productVariantViewModel
        currentProductAddViewModel?.productPrice?.price = currentProductAddViewModel?.getPrdPriceOrMinVariantProductPrice() ?: 0.00
    }

    private fun onAddImagePickerClicked() {
        var catalogId = ""
        if (currentProductAddViewModel?.productCatalog?.catalogId!! > 0) {
            catalogId = currentProductAddViewModel!!.productCatalog?.catalogId.toString()
        }
        val intent = AddProductImagePickerBuilder.createPickerIntentWithCatalog(context,
                currentProductAddViewModel?.productPictureList?.convertToListImageString(), catalogId)
        startActivityForResult(intent, REQUEST_CODE_GET_IMAGES)
    }

    override fun onSuccessGetProductVariantCat(productVariantByCatModelList: MutableList<ProductVariantByCatModel>?) {
        if (productVariantByCatModelList != null) {
            currentProductAddViewModel?.productVariantByCatModelList = productVariantByCatModelList as ArrayList<ProductVariantByCatModel>
        } else {
            currentProductAddViewModel?.productVariantByCatModelList = ArrayList()
        }
        populateView(currentProductAddViewModel)
    }

    override fun onErrorGetProductVariantByCat(throwable: Throwable?) {
        onSuccessGetProductVariantCat(null)
    }

    private fun startProductVariantActivity() {
        context?.let {
            val app = it.applicationContext
            if (app is AbstractionRouter){
                TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(ProductVariantConstant.TRACKING_EVENT,
                        ProductVariantConstant.TRACKING_EVENT_CATEGORY,
                        ProductVariantConstant.TRACKING_EVENT_ACTION, null))
            }
        }
        currentProductAddViewModel?.run {
            if (productVariantByCatModelList.size == 0) {
                NetworkErrorHelper.createSnackbarWithAction(activity) {
                    presenter.fetchProductVariantByCat(currentProductAddViewModel?.productCategory?.categoryId?.toLong()
                            ?: 0L)
                }.showRetrySnackbar()
                return
            }

            val hasWholesale = productPrice?.wholesalePrice?.let { it.size > 0 } == true
            if (appRouter is ProductEditModuleRouter) {
                val intent = (appRouter as ProductEditModuleRouter).createIntentProductVariant(activity,
                        productVariantByCatModelList, productVariantViewModel,
                        productPrice?.currencyType ?: CurrencyTypeDef.TYPE_IDR,
                        productPrice?.price ?: 0.0,
                        getStatusStockViewVariant(productStock ?: ProductStock()),
                        officialStore, productStock?.sku, isEdittingDraft(),
                        productSizeChart, hasOriginalVariantLevel1 == true,
                        hasOriginalVariantLevel2 == true,
                        hasWholesale)
                startActivityForResult(intent, REQUEST_CODE_VARIANT)
            }
        }
    }

    private fun getStatusStockViewVariant(productStock: ProductStock) =
            if (!productStock.isActive) {
                StockTypeDef.TYPE_WAREHOUSE
            } else if (productStock.isActive && productStock.stockCount > 0) {
                StockTypeDef.TYPE_ACTIVE_LIMITED
            } else {
                StockTypeDef.TYPE_ACTIVE
            }


    private fun sendAnalyticsAdd(viewModel: ProductViewModel?) {
        val listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                false
        )
        for (labelAnalytics in listLabelAnalytics) {
            if (isAddStatus()) {
                eventAddProductAdd(labelAnalytics)
            } else if (isEditStatus()) {
                eventAddProductEdit(labelAnalytics)
            }
        }
    }

    private fun eventAddProductAdd(label: String) {
        TrackApp.getInstance()!!.gtm.sendGeneralEvent(
            AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
            AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
            AppEventTracking.AddProduct.EVENT_ACTION_ADD,
            label)
    }

    private fun eventAddProductEdit(label: String) {
        TrackApp.getInstance()!!.gtm.sendGeneralEvent(
            AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
            AppEventTracking.AddProduct.CATEGORY_EDIT_PRODUCT,
            AppEventTracking.AddProduct.EVENT_ACTION_EDIT,
            label)
    }

    private fun isEdittingDraft() = isEditStatus() && productDraftId > 0

    open fun isEditStatus() = statusUpload == ProductStatus.EDIT

    private fun isAddStatus() = statusUpload == ProductStatus.ADD

    override fun onErrorName() {
        showWarning(getString(R.string.product_error_product_name_empty), View.OnClickListener {
            startNameActivity()
        })
    }

    override fun onErrorCategoryEmpty() {
        showWarning(getString(R.string.product_error_product_category_empty), View.OnClickListener {
            startCatalogActivity()
        })
        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_CATEGORY)
    }

    override fun onErrorEtalase() {
        showWarning(getString(R.string.product_error_product_etalase_empty), View.OnClickListener {
            startProductEtalaseActivity()
        })
        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_SHOWCASE)
    }

    override fun onErrorPrice() {
        showWarning(getString(R.string.error_empty_price), View.OnClickListener {
            startPriceActivity()
        })
        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE)
    }

    override fun onErrorWeight() {
        showWarning(getString(R.string.error_empty_weight), View.OnClickListener {
            startLogisticActivity()
        })
        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT)
    }

    override fun onErrorImage() {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(R.string.product_error_product_picture_empty))
        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE)
    }

    @SuppressLint("Range")
    private fun showWarning(message: String, action: View.OnClickListener) {
        SnackbarManager.makeRed(activity, message, Snackbar.LENGTH_LONG).setAction(getString(R.string.product_action_view_error), action).show()
    }

    open fun showDialogSaveDraftOnBack() = true

    fun deleteNotUsedTkpdCacheImage() {
        if (currentProductAddViewModel?.productPictureList?.size == 0) {
            return
        }
        ImageUtils.deleteFilesInTokopediaFolder(currentProductAddViewModel?.productPictureList?.convertToListImageString())
    }

    companion object {

        const val DEFAULT_PARENT_STOCK_IF_VARIANT = 1

        const val REQUEST_CODE_GET_IMAGES = 1
        const val REQUEST_CODE_GET_CATALOG_CATEGORY = 2
        const val REQUEST_CODE_GET_NAME = 3
        const val REQUEST_CODE_GET_PRICE = 4
        const val REQUEST_CODE_GET_DESCRIPTION = 5
        const val REQUEST_CODE_GET_STOCK = 6
        const val REQUEST_CODE_GET_LOGISTIC = 7
        const val REQUEST_CODE_VARIANT = 9
        const val REQUEST_CODE_GET_ETALASE = 10

        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CATALOG = "EXTRA_CATALOG"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_CATEGORY_LOCKED = "EXTRA_CATEGORY_LOCKED"
        const val EXTRA_IMAGES = "EXTRA_IMAGES"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_LOGISTIC = "EXTRA_LOGISTIC"

        const val EXTRA_IS_EDITABLE_NAME = "EXTRA_IS_EDITABLE_NAME"
        const val EXTRA_IS_OFFICIAL_STORE = "EXTRA_OFFICIAL_STORE"
        const val EXTRA_HAS_VARIANT = "EXTRA_HAS_VARIANT"
        const val EXTRA_IS_GOLD_MERCHANT = "EXTRA_GOLD_MERCHANT"
        const val EXTRA_IS_MOVE_TO_GM = "EXTRA_IS_MOVE_TO_GM"
        const val EXTRA_IS_STATUS_ADD = "EXTRA_IS_STATUS_ADD"
        const val EXTRA_IS_FREE_RETURN = "EXTRA_IS_FREE_RETURN"

        const val SAVED_PRODUCT_VIEW_MODEL = "svd_prd_model"
    }
}
