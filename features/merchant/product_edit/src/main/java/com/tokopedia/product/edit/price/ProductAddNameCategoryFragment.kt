package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.edit.di.component.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.activity.ProductAddActivity
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.edit.view.presenter.ProductEditCategoryPresenter
import kotlinx.android.synthetic.main.fragment_product_add_name_category.*
import javax.inject.Inject

class ProductAddNameCategoryFragment : BaseDaggerFragment(), ProductEditNameViewHolder.Listener, ProductEditCategoryCatalogViewHolder.Listener, ProductEditCategoryView {

    @Inject
    lateinit var presenter: ProductEditCategoryPresenter

    val appRouter : Context? by lazy { activity?.application as? Context }
    private lateinit var productEditCategoryCatalogViewHolder: ProductEditCategoryCatalogViewHolder
    private var productCatalog = ProductCatalog()
    private var productName = ProductName()
    private var productCategory = ProductCategory()
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onSuccessLoadRecommendationCategory(categories: List<ProductCategoryPredictionViewModel>) {
        productEditCategoryCatalogViewHolder.renderRecommendation(categories)
    }

    override fun onErrorLoadRecommendationCategory(throwable: Throwable?) {}

    override fun getScreenName(): String? = null


    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_name_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this)
        productEditCategoryCatalogViewHolder = ProductEditCategoryCatalogViewHolder(view, this, context)
        texViewMenu?.run {
            text = getString(R.string.label_next)
            setOnClickListener { goToNext() }
        }
        validateData()
    }

    override fun onNameChanged(productName: ProductName) {
        categoryCatalogViewHolder.visibility = if (productName.name.isNotEmpty()) View.VISIBLE else View.GONE
        this.productName = productName
        presenter.onProductNameChange(productName.name)
        validateData()
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        if(this.productCategory.categoryId != productCategory.categoryId) {
            presenter.fetchCatalogData(productName.name, productCategory.categoryId.toLong(), 0, 1)
        }
        this.productCategory = productCategory
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
        presenter.categoryId = productCategory.categoryId.toLong()
        validateData()
    }

    override fun onLabelCategoryClicked() {
        if (appRouter != null && appRouter is ProductEditModuleRouter){
            startActivityForResult((appRouter as ProductEditModuleRouter)
                    .getCategoryPickerIntent(activity, productCategory.categoryId)
                    , ProductEditCategoryFragment.REQUEST_CODE_GET_CATEGORY)
        }
    }

    override fun onLabelCatalogClicked() {
        context?.run {
            startActivityForResult(ProductEditCatalogPickerActivity
                    .createIntent(this, productName.name, productCategory.categoryId.toLong(), productCatalog),
                    REQUEST_CODE_GET_CATALOG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data.getParcelableExtra(EXTRA_CATALOG)
                    productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val newCategoryId = data.getLongExtra(ProductExtraConstant.CATEGORY_RESULT_ID, -1).toInt()
                    if(productCategory.categoryId != newCategoryId){
                        presenter.fetchCatalogData(productName.name, newCategoryId.toLong(), 0, 1)
                    }
                    productCategory.categoryId = newCategoryId
                    presenter.categoryId = newCategoryId.toLong()
                    presenter.fetchCategory(productCategory.categoryId.toLong())
                }
                REQUEST_CODE_GET_IMAGES -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    startActivity(ProductAddActivity.createInstance(activity, productCatalog, productCategory, productName, imageUrlOrPathList))
                    activity?.finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun populateCategory(strings: List<String>) {
        val category = strings.filter { !TextUtils.isEmpty(it) }.joinToString(separator = " / ")
        productEditCategoryCatalogViewHolder.setCategoryChosen(ProductCategory(categoryName = category))
    }

    private fun validateData(){
        texViewMenu?.run {
            setTextColor(ContextCompat.getColor(context, if (isDataValid()) R.color.tkpd_main_green else R.color.font_black_secondary_54))
        }
    }

    override fun onSuccessLoadCatalog(keyword: String, departmentId: Long, catalogs: List<Catalog>) {
        productEditCategoryCatalogViewHolder.setVisiblityCatalog(!catalogs.isEmpty())
    }

    override fun onErrorLoadCatalog(errorMessage: String?) {
        productCatalog.catalogId = -1
    }

    private fun isDataValid() = productName.name.isNotEmpty() && productCategory.categoryId > 0

    private fun goToNext(){
        if(isDataValid()){
            startActivityForResult(AddProductImagePickerBuilder.createPickerIntentPrimary(activity, ArrayList()), REQUEST_CODE_GET_IMAGES)
        }
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
        const val REQUEST_CODE_GET_IMAGES = 100
        fun createInstance() = ProductAddNameCategoryFragment()
    }
}
