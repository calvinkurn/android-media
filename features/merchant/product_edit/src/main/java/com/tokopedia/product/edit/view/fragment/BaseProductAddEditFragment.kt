package com.tokopedia.product.edit.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.util.ProductEditModuleRouter
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
import javax.inject.Inject

abstract class BaseProductAddEditFragment<T : ProductAddPresenterImpl<P>, P : ProductAddView> : BaseDaggerFragment(), ProductAddView {

    @Inject
    lateinit var presenter: T
    val view : P? = null

    @ProductStatus
    protected abstract var statusUpload: Int
    protected var productDraftId: Int = 0

    var isHasLoadShopInfo: Boolean = false
    private var officialStore: Boolean = false
    val appRouter = activity?.application
    protected var currentProductAddViewModel: ProductAddViewModel? = null
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(view)
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

        llCategoryCatalog.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditCategoryActivity::class.java)
                    .putExtra(EXTRA_CATALOG, currentProductAddViewModel?.productCatalog)
                    .putExtra(EXTRA_CATEGORY, currentProductAddViewModel?.productCategory), REQUEST_CODE_GET_CATALOG_CATEGORY)
        }
        labelViewNameProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditNameActivity::class.java)
                    .putExtra(EXTRA_NAME, currentProductAddViewModel?.productName), REQUEST_CODE_GET_NAME)
        }
        labelViewPriceProduct.setOnClickListener { startActivity(Intent(activity, ProductEditPriceActivity::class.java)) }
        labelViewDescriptionProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditDescriptionActivity::class.java)
                    .putExtra(EXTRA_DESCRIPTION, currentProductAddViewModel?.productDescription)
                    .putExtra(EXTRA_KEYWORD, currentProductAddViewModel?.productName?.name), REQUEST_CODE_GET_DESCRIPTION)
        }
        labelViewStockProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditStockActivity::class.java)
                    .putExtra(EXTRA_STOCK, currentProductAddViewModel?.productStock), REQUEST_CODE_GET_STOCK)
        }
        labelViewWeightLogisticProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditWeightLogisticActivity::class.java)
                    .putExtra(EXTRA_LOGISTIC, currentProductAddViewModel?.productLogistic), REQUEST_CODE_GET_LOGISTIC)
        }
        labelViewEtalaseProduct.setOnClickListener{

        }
        labelViewVariantProduct.setOnClickListener {
            startProductVariantActivity()
        }
        containerImageProduct.setOnClickListener{
            onAddImagePickerClicked()
        }

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener { saveDraft(true)}
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
                REQUEST_CODE_GET_DESCRIPTION -> {
                    val productDescription: ProductDescription = data!!.getParcelableExtra(EXTRA_DESCRIPTION)
                    currentProductAddViewModel?.productDescription = productDescription
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
                text = currentProductViewModel.productCatalog.catalogName
            }

        }
        if (currentProductViewModel?.productPictureList?.size!! > 0) {
            ImageHandler.loadImageRounded2(context, imageOne, currentProductViewModel.productPictureList!![0])
            if (currentProductViewModel.productPictureList?.size!! > 1)
                ImageHandler.loadImageRounded2(context, imageOne, currentProductViewModel.productPictureList!![0])
        }
        labelViewNameProduct.setContent(currentProductViewModel.productName.name)
        labelViewDescriptionProduct.setContent(currentProductViewModel.productDescription.description)
        if (currentProductViewModel.productLogistic?.weight!! > 0) {
            labelViewWeightLogisticProduct.setContent("${currentProductViewModel.productLogistic?.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(currentProductViewModel.productLogistic?.weightType!!))}")
            labelViewWeightLogisticProduct.setSubTitle("")
        }
        if (currentProductViewModel.productStock?.isActive!!)
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
        else
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
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
        val intent = AddProductImagePickerBuilder.createPickerIntentWithCatalog(context,
                currentProductAddViewModel?.productPictureList, currentProductAddViewModel?.productCatalog?.catalogId.toString())
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT_IMAGE)
    }

    fun goToProductDescriptionPicker(description: String) {
        startActivity(ProductEditDescriptionActivity.createIntent(activity, description))
    }

    fun startProductVariantActivity() {

        val hasWholesale = true /* Todo currentProductAddViewModel!!.getProductWholesale() != null && currentProductAddViewModel!!.getProductWholesale().size > 0*/
        if (appRouter is ProductEditModuleRouter) {
            val intent = appRouter.createIntentProductVariant(activity,
                    null,
                    currentProductAddViewModel?.productVariantViewModel,
                    1/*todo*/,
                    123.0 /*todo*/,
                    currentProductAddViewModel?.productStock?.stockCount!!,
                    officialStore,
                    ""/*todo*/,
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
        const val REQUEST_CODE_GET_DESCRIPTION = 5
        const val REQUEST_CODE_GET_STOCK = 6
        const val REQUEST_CODE_GET_LOGISTIC = 7
        const val REQUEST_CODE_VARIANT = 9

        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CATALOG = "EXTRA_CATALOG"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_IMAGES = "EXTRA_IMAGES"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_LOGISTIC = "EXTRA_LOGISTIC"

        val DEFAULT_PARENT_STOCK_IF_VARIANT = 1
        val REQUEST_CODE_ADD_PRODUCT_IMAGE = 3912
        val REQUEST_CODE_EDIT_IMAGE = 3412

        val SAVED_PRODUCT_VIEW_MODEL = "svd_prd_model"
    }
}
