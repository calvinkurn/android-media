package com.tokopedia.product.edit.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.model.edit.ProductPictureViewModel
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.edit.common.model.variantbyprd.ProductVariantViewModel
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.utils.convertImageListResult
import com.tokopedia.product.edit.utils.convertToListImageString
import com.tokopedia.product.edit.utils.convertToProductViewModel
import com.tokopedia.product.edit.utils.isDataValid
import com.tokopedia.product.edit.view.activity.*
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment.Companion.EXTRA_KEYWORD
import com.tokopedia.product.edit.view.listener.ProductAddView
import com.tokopedia.product.edit.view.mapper.AnalyticsMapper
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import com.tokopedia.product.edit.view.presenter.ProductAddPresenterImpl
import com.tokopedia.product.edit.view.service.UploadProductService
import kotlinx.android.synthetic.main.fragment_base_product_edit.*
import java.util.ArrayList
import javax.inject.Inject

abstract class BaseProductAddEditFragment<T : ProductAddPresenterImpl<P>, P : ProductAddView> : BaseDaggerFragment(), ProductAddView {

    @Inject
    lateinit var presenter: T

    @ProductStatus
    protected abstract var statusUpload: Int
    protected var productDraftId: Int = 0

    var isHasLoadShopInfo: Boolean = false
    private var officialStore: Boolean = false
    val appRouter : Context by lazy { activity?.application as Context}
    protected var currentProductAddViewModel: ProductAddViewModel? = null
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        presenter.getShopInfo()

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

        populateView(currentProductAddViewModel)

        llCategoryCatalog.setOnClickListener { startCatalogActivity() }
        labelViewNameProduct.setOnClickListener { startNameActivity() }
        labelViewPriceProduct.setOnClickListener { startPriceActivity() }
        labelViewDescriptionProduct.setOnClickListener { startDescriptionActivity() }
        labelViewStockProduct.setOnClickListener { startStockActivity() }
        labelViewWeightLogisticProduct.setOnClickListener { startLogisticActivity() }
        labelViewEtalaseProduct.setOnClickListener{ startProductEtalaseActivity() }
        labelViewVariantProduct.setOnClickListener { startProductVariantActivity() }
        containerImageProduct.setOnClickListener{ onAddImagePickerClicked() }

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener { saveDraft(true)}
    }

    private fun startCatalogActivity() {
        startActivityForResult(ProductEditCategoryActivity.createIntent(activity!!, currentProductAddViewModel?.productCategory!!, currentProductAddViewModel?.productCatalog!!), REQUEST_CODE_GET_CATALOG_CATEGORY)
    }

    private fun startNameActivity() {
        startActivityForResult(ProductEditNameActivity.createIntent(activity!!, currentProductAddViewModel?.productName!!, true), REQUEST_CODE_GET_NAME)
    }

    private fun startPriceActivity() {
        startActivityForResult(ProductEditPriceActivity.createIntent(activity!!, currentProductAddViewModel?.productPrice!!, false, false, false), REQUEST_CODE_GET_PRICE)
    }

    private fun startDescriptionActivity() {
        startActivityForResult(ProductEditDescriptionActivity.createIntent(activity!!, currentProductAddViewModel?.productDescription!!, currentProductAddViewModel?.productName?.name!!), REQUEST_CODE_GET_DESCRIPTION)
    }

    private fun startStockActivity() {
        startActivityForResult(ProductEditStockActivity.createIntent(activity!!, currentProductAddViewModel?.productStock!!, false, false), REQUEST_CODE_GET_STOCK)
    }

    private fun startLogisticActivity() {
        startActivityForResult(ProductEditWeightLogisticActivity.createIntent(activity!!, currentProductAddViewModel?.productLogistic!!), REQUEST_CODE_GET_LOGISTIC)
    }

    private fun startProductEtalaseActivity() {
        if(appRouter is ProductEditModuleRouter){
            startActivityForResult((appRouter as ProductEditModuleRouter).createIntentProductEtalase(activity, currentProductAddViewModel?.etalaseId!!), REQUEST_CODE_GET_ETALASE);

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_VIEW_MODEL, currentProductAddViewModel)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG_CATEGORY -> {
                    val productCatalog: ProductCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    val productCategory: ProductCategory = data.getParcelableExtra(EXTRA_CATEGORY)
                    currentProductAddViewModel?.productCatalog = productCatalog
                    currentProductAddViewModel?.productCategory = productCategory
                }
                REQUEST_CODE_GET_NAME -> {
                    val productName: ProductName = data!!.getParcelableExtra(EXTRA_NAME)
                    currentProductAddViewModel?.productName = productName
                }
                REQUEST_CODE_GET_LOGISTIC -> {
                    val productLogistic: ProductLogistic = data!!.getParcelableExtra(EXTRA_LOGISTIC)
                    currentProductAddViewModel?.productLogistic = productLogistic
                }
                REQUEST_CODE_GET_STOCK -> {
                    val productStock: ProductStock = data!!.getParcelableExtra(EXTRA_STOCK)
                    currentProductAddViewModel?.productStock = productStock
                }
                REQUEST_CODE_GET_PRICE -> {
                    val productPrice: ProductPrice = data!!.getParcelableExtra(EXTRA_PRICE)
                    currentProductAddViewModel?.productPrice = productPrice
                }
                REQUEST_CODE_GET_DESCRIPTION -> {
                    val productDescription: ProductDescription = data!!.getParcelableExtra(EXTRA_DESCRIPTION)
                    currentProductAddViewModel?.productDescription = productDescription
                }
                REQUEST_CODE_GET_ETALASE ->{
                    val etalaseId = data!!.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1)
                    val etalaseNameString = data.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME)
                    currentProductAddViewModel?.etalaseId = etalaseId
                    currentProductAddViewModel?.etalaseName = etalaseNameString
                }
                REQUEST_CODE_GET_IMAGES ->{
                    val imageUrlOrPathList = data?.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    currentProductAddViewModel?.productPictureList = currentProductAddViewModel?.let {
                        imageUrlOrPathList?.convertImageListResult(it,
                                data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE),
                                data.getSerializableExtra(RESULT_IS_EDITTED) as ArrayList<Boolean>?)
                    }
                }
                REQUEST_CODE_VARIANT ->{
                    if (data!!.hasExtra(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        val productVariantViewModel = data.getParcelableExtra<ProductVariantViewModel>(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION)
                        currentProductAddViewModel?.productVariantViewModel = productVariantViewModel
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
            if (currentProductAddViewModel?.isDataValid()!!) {
                saveDraft(true)
            }
        }.showRetrySnackbar()
    }

    override fun onErrorStoreProductToDraftWhenBackPressed(errorMessage: String?) {
        CommonUtils.UniversalToast(activity, errorMessage)
        activity?.finish()
    }

    override fun onSuccessLoadShopInfo(goldMerchant: Boolean, freeReturn: Boolean, officialStore: Boolean) {
        this.isHasLoadShopInfo = true
        this.officialStore = officialStore
    }

    override fun onErrorLoadShopInfo(errorMessage: String?) {
        //do nothing
    }

    fun startUploadProduct(productId: Long) {
        startUploadProductService(productId)
        activity?.finish()
    }

    override fun getProductDraftId(): Long {
        return 0
    }

    private fun startUploadProductService(productId: Long) {
        activity?.startService(UploadProductService.getIntent(activity, productId, isAddStatus()))
    }

    protected fun populateView(currentProductViewModel: ProductAddViewModel?) {
        textViewCategory.text = currentProductViewModel?.productCategory?.categoryName
        if (currentProductViewModel?.productCatalog?.catalogName != null) {
            textViewCatalog.run {
                visibility = View.VISIBLE
                text = currentProductViewModel.productCatalog?.catalogName
            }

        }
        if (currentProductViewModel?.productPictureList?.size!! > 0) {
            ImageHandler.loadImageRounded2(context, imageOne, currentProductViewModel.productPictureList!![0].uriOrPath)
            if (currentProductViewModel.productPictureList?.size!! > 1)
                ImageHandler.loadImageRounded2(context, imageTwo, currentProductViewModel.productPictureList!![1].uriOrPath)
        }
        labelViewNameProduct.setContent(currentProductViewModel.productName?.name)
        if(!TextUtils.isEmpty(currentProductViewModel.productDescription?.description)) {
            labelViewDescriptionProduct.setContent(currentProductViewModel.productDescription?.description)
            labelViewDescriptionProduct.setSubTitle("")
        }
        labelViewNameProduct.setContent(currentProductViewModel.productName.name)
        if(currentProductViewModel.productPrice.price>0) {
            labelViewPriceProduct.setContent(CurrencyFormatUtil.convertPriceValueToIdrFormat(currentProductViewModel.productPrice.price, true))
            labelViewPriceProduct.setSubTitle("")
        }
        labelViewDescriptionProduct.setContent(currentProductViewModel.productDescription.description)
        if (currentProductViewModel.productLogistic?.weight!! > 0) {
            labelViewWeightLogisticProduct.setContent("${currentProductViewModel.productLogistic?.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(currentProductViewModel.productLogistic?.weightType!!))}")
            labelViewWeightLogisticProduct.setSubTitle("")
        }
        if (currentProductViewModel.productStock?.isActive!!) {
            if(currentProductViewModel.productStock?.stockCount!! > 0) {
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
            }else {
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            }
        }else{
            labelViewStockProduct.setContent("Stok Kosong")
        }

        if(currentProductViewModel.productVariantByCatModelList.size > 0){
            labelViewVariantProduct.visibility = View.VISIBLE
            val productVariantOptionParentLv1 = currentProductViewModel.productVariantViewModel?.getVariantOptionParent(0)
            val productVariantOptionParentLv2 = currentProductViewModel.productVariantViewModel?.getVariantOptionParent(1)
            var selectedVariantString = "${productVariantOptionParentLv1?.getProductVariantOptionChild()?.size} ${productVariantOptionParentLv1?.name}"
            if (productVariantOptionParentLv2 != null && productVariantOptionParentLv2.hasProductVariantOptionChild()) {
                selectedVariantString = "$selectedVariantString \n ${productVariantOptionParentLv2.getProductVariantOptionChild().size} ${productVariantOptionParentLv2.getName()}"
            }
            labelViewVariantProduct.setContent(selectedVariantString)
        }else{
            labelViewVariantProduct.visibility = View.GONE
        }



        if (currentProductViewModel.productStock?.isActive!!){
            if(currentProductViewModel.productStock?.stockCount!! > 0)
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            else
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
        } else
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_empty))
    }

    fun saveDraft(isUploading: Boolean) {
        if (isUploading) {
            sendAnalyticsAdd(currentProductAddViewModel?.convertToProductViewModel())
        }
        if (currentProductAddViewModel?.productVariantViewModel != null) {
            currentProductAddViewModel?.productVariantViewModel?.generateTid()
        }
        presenter!!.saveDraft(currentProductAddViewModel?.convertToProductViewModel(), isUploading)
    }

    fun onAddImagePickerClicked() {
        var catalogId = ""
        if(currentProductAddViewModel?.productCatalog?.catalogId!! > 0){
            catalogId = currentProductAddViewModel!!.productCatalog?.catalogId.toString()
        }
        val intent = AddProductImagePickerBuilder.createPickerIntentWithCatalog(context,
                currentProductAddViewModel?.productPictureList?.convertToListImageString(), catalogId  )
        startActivityForResult(intent, REQUEST_CODE_GET_IMAGES)
    }

    override fun onSuccessGetProductVariantCat(productVariantByCatModelList: MutableList<ProductVariantByCatModel>?) {
        currentProductAddViewModel?.productVariantByCatModelList = productVariantByCatModelList as ArrayList<ProductVariantByCatModel>
        populateView(currentProductAddViewModel)
    }

    override fun onErrorGetProductVariantByCat(throwable: Throwable?) {
        onSuccessGetProductVariantCat(null)
    }

    fun goToProductDescriptionPicker(description: String) {
        startActivity(ProductEditDescriptionActivity.createIntent(activity, description))
    }

    fun startProductVariantActivity() {
        if (currentProductAddViewModel?.productVariantByCatModelList == null || currentProductAddViewModel?.productVariantByCatModelList!!.size == 0) {
            NetworkErrorHelper.createSnackbarWithAction(activity) {
                presenter.fetchProductVariantByCat(currentProductAddViewModel?.productCategory?.categoryId!!.toLong())
            }.showRetrySnackbar()
            return
        }

        val hasWholesale = false /* Todo currentProductAddViewModel!!.getProductWholesale() != null && currentProductAddViewModel!!.getProductWholesale().size > 0*/
        if (appRouter is ProductEditModuleRouter) {
            val intent = (appRouter as ProductEditModuleRouter).createIntentProductVariant(activity,
                    currentProductAddViewModel?.productVariantByCatModelList,
                    currentProductAddViewModel?.productVariantViewModel,
                    1/*todo*/,
                    123.0 /*todo*/,
                    currentProductAddViewModel?.productStock?.stockCount!!,
                    officialStore,
                    currentProductAddViewModel?.productStock?.sku,
                    isEdittingDraft(),
                    currentProductAddViewModel?.productSizeChart,
                    currentProductAddViewModel?.hasOriginalVariantLevel1!!,
                    currentProductAddViewModel?.hasOriginalVariantLevel2!!,
                    hasWholesale)
            startActivityForResult(intent, REQUEST_CODE_VARIANT)
        }

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

    private fun isEdittingDraft(): Boolean {
        return isEditStatus() && productDraftId > 0
    }

    fun isEditStatus(): Boolean {
        return statusUpload == ProductStatus.EDIT
    }

    fun isAddStatus(): Boolean {
        return statusUpload == ProductStatus.ADD
    }

    companion object {

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

        val SAVED_PRODUCT_VIEW_MODEL = "svd_prd_model"
    }
}
