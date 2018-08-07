package com.tokopedia.product.edit.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_IS_EDITTED
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.model.edit.ProductPictureViewModel
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.edit.common.model.variantbyprd.ProductVariantViewModel
import com.tokopedia.product.edit.common.util.CurrencyTypeDef
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.common.util.StockTypeDef
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.utils.*
import com.tokopedia.product.edit.view.activity.*
import com.tokopedia.product.edit.view.listener.ListenerOnErrorAddProduct
import com.tokopedia.product.edit.view.listener.ProductAddView
import com.tokopedia.product.edit.view.mapper.AnalyticsMapper
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import com.tokopedia.product.edit.view.presenter.ProductAddPresenterImpl
import com.tokopedia.product.edit.view.service.UploadProductService
import kotlinx.android.synthetic.main.fragment_base_product_edit.*
import javax.inject.Inject

abstract class BaseProductAddEditFragment<T : ProductAddPresenterImpl<P>, P : ProductAddView> : BaseDaggerFragment(),
        ProductAddView, ListenerOnErrorAddProduct {

    @Inject
    lateinit var presenter: T

    @ProductStatus
    protected abstract var statusUpload: Int

    private var isHasLoadShopInfo: Boolean = false
    private var officialStore: Boolean = false
    private var isFreeReturn: Boolean = false
    private var isGoldMerchant: Boolean = false
    val appRouter: Context? by lazy { activity?.application as? Context }
    protected var currentProductAddViewModel: ProductAddViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        presenter.getShopInfo()

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_VIEW_MODEL)) {
                currentProductAddViewModel = savedInstanceState.getParcelable(SAVED_PRODUCT_VIEW_MODEL)
            }
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
        button_save.setOnClickListener{
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
            startActivityForResult((appRouter as ProductEditModuleRouter).createIntentProductEtalase(activity, currentProductAddViewModel?.etalaseId!!),
                    REQUEST_CODE_GET_ETALASE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_VIEW_MODEL, currentProductAddViewModel)
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
                    if(productName.name != currentProductAddViewModel?.productName?.name){
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
                    if(currentProductAddViewModel?.productStock?.stockCount?:0 != productStock.stockCount){
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
                        UnifyTracking.eventClickYesGoldMerchantAddProduct()
                        goToGoldMerchantPage()
                        activity!!.finish()
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
        }
        labelViewNameProduct.setContent(currentProductViewModel.productName?.name)
        if ((currentProductViewModel.productPrice?.price ?: 0.0) > 0) {
            labelViewPriceProduct.setContent(CurrencyFormatUtil.convertPriceValueToIdrFormat(currentProductViewModel.productPrice?.price ?: 0.00, true))
            labelViewPriceProduct.setSubTitle("")
        }
        labelViewDescriptionProduct.setContent(currentProductViewModel.productDescription?.description)
        if (currentProductViewModel.productLogistic?.weight ?: 0 > 0) {
            labelViewWeightLogisticProduct.setContent("${currentProductViewModel.productLogistic?.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(currentProductViewModel.productLogistic?.weightType!!))}")
            labelViewWeightLogisticProduct.setSubTitle("")
        }
        if (currentProductViewModel.productStock?.isActive ?: false) {
            if ((currentProductViewModel.productStock?.stockCount ?: 0) > 0) {
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
            } else {
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            }
        } else {
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_empty))
        }

        if (currentProductViewModel.productVariantByCatModelList.size > 0 && currentProductViewModel.productVariantViewModel?.hasSelectedVariant() ?: false) {
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
        } else {
            labelViewVariantProduct.visibility = View.GONE
        }

        if (currentProductViewModel.productStock?.isActive ?: false) {
            if ((currentProductViewModel.productStock?.stockCount ?: 0) > 0)
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            else
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
        } else
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_empty))

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
            @StockTypeDef val stockType = productVariantViewModel.getCalculateProductStatus()
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
        currentProductAddViewModel?.productVariantByCatModelList = productVariantByCatModelList as ArrayList<ProductVariantByCatModel>
        populateView(currentProductAddViewModel)
    }

    override fun onErrorGetProductVariantByCat(throwable: Throwable?) {
        onSuccessGetProductVariantCat(null)
    }

    private fun startProductVariantActivity() {
        currentProductAddViewModel?.run {
            if (productVariantByCatModelList.size == 0) {
                NetworkErrorHelper.createSnackbarWithAction(activity) {
                    presenter.fetchProductVariantByCat(currentProductAddViewModel?.productCategory?.categoryId?.toLong() ?: 0L)
                }.showRetrySnackbar()
                return
            }

            val hasWholesale = productPrice?.wholesalePrice?.let { it.size > 0 } == true
            if (appRouter is ProductEditModuleRouter) {
                val intent = (appRouter as ProductEditModuleRouter).createIntentProductVariant(activity,
                        productVariantByCatModelList, productVariantViewModel,
                        productPrice?.currencyType ?: CurrencyTypeDef.TYPE_IDR,
                        productPrice?.price ?: 0.0,
                        getStatusStockViewVariant(productStock?:ProductStock()),
                        officialStore, productStock?.sku, isEdittingDraft(),
                        productSizeChart, hasOriginalVariantLevel1 == true,
                        hasOriginalVariantLevel2 == true,
                        hasWholesale)
                startActivityForResult(intent, REQUEST_CODE_VARIANT)
            }
        }
    }

    private fun getStatusStockViewVariant(productStock: ProductStock) =
        if(!productStock.isActive){
            StockTypeDef.TYPE_WAREHOUSE
        }else if(productStock.isActive && productStock.stockCount > 0){
            StockTypeDef.TYPE_ACTIVE_LIMITED
        }else{
            StockTypeDef.TYPE_ACTIVE
        }


    private fun sendAnalyticsAdd(viewModel: ProductViewModel?) {
        val listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                false
        )
        for (labelAnalytics in listLabelAnalytics) {
            if (isAddStatus()) {
                UnifyTracking.eventAddProductAdd(labelAnalytics)
            } else if (isEditStatus()) {
                UnifyTracking.eventAddProductEdit(labelAnalytics)
            }
        }
    }

    private fun isEdittingDraft() = isEditStatus() && getProductDraftId() > 0

    private fun isEditStatus() = statusUpload == ProductStatus.EDIT

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
        UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_CATEGORY)
    }

    override fun onErrorEtalase() {
        showWarning(getString(R.string.product_error_product_etalase_empty), View.OnClickListener {
            startProductEtalaseActivity()
        })
        UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_SHOWCASE)
    }

    override fun onErrorPrice() {
        showWarning(getString(R.string.error_empty_price), View.OnClickListener {
            startPriceActivity()
        })
        UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE)
    }

    override fun onErrorWeight() {
        showWarning(getString(R.string.error_empty_weight), View.OnClickListener {
            startLogisticActivity()
        })
        UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT)
    }

    override fun onErrorImage() {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(R.string.product_error_product_picture_empty))
        UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE)
    }

    @SuppressLint("Range")
    fun showWarning(message: String, action: View.OnClickListener) {
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

        val DEFAULT_PARENT_STOCK_IF_VARIANT = 1

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
