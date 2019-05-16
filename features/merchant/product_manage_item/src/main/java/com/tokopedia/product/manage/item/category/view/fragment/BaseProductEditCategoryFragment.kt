package com.tokopedia.product.manage.item.category.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.manage.item.catalog.view.listener.ProductEditCategoryView
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.adapter.ProductCategoryRecommendationAdapter
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.category.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.manage.item.category.view.presenter.ProductEditCategoryPresenter
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import kotlinx.android.synthetic.main.fragment_product_edit_category.*
import javax.inject.Inject

abstract class BaseProductEditCategoryFragment : BaseDaggerFragment(),
        ProductEditCategoryView, ProductCategoryRecommendationAdapter.Listener{

    @Inject
    lateinit var presenter: ProductEditCategoryPresenter

    private val appRouter : Context? by lazy { activity?.application as? Context }

    private lateinit var productCategoryRecommendationAdapter: ProductCategoryRecommendationAdapter

    override fun getScreenName(): String? = null

    private var isCategoryLocked: Boolean = false

    var name: String = ""
    var productCatalog = ProductCatalog()
    var productCategory = ProductCategory()

    val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.run {
            productCategory = getParcelable(EXTRA_CATEGORY)?: ProductCategory()
            productCatalog = getParcelable(EXTRA_CATALOG)?: ProductCatalog()
            name = getString(EXTRA_NAME, "")
            isCategoryLocked = getBoolean(EXTRA_CATEGORY_LOCKED, false)
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_CATEGORY)) {
                productCategory = savedInstanceState.getParcelable(SAVED_PRODUCT_CATEGORY)
            }
            if (savedInstanceState.containsKey(SAVED_PRODUCT_CATALOG)) {
                productCatalog = savedInstanceState.getParcelable(SAVED_PRODUCT_CATALOG)
            }
            if (savedInstanceState.containsKey(SAVED_NAME)) {
                name = savedInstanceState.getString(SAVED_NAME)
            }
            if (savedInstanceState.containsKey(SAVED_CATEGORY_ID)) {
                presenter.categoryId = savedInstanceState.getLong(SAVED_CATEGORY_ID)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        restoreSaveInstance(savedInstanceState)
        return inflater.inflate(R.layout.fragment_product_edit_category, container, false)
    }

    open fun restoreSaveInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            if(containsKey(EXTRA_NAME)){
                name = getString(EXTRA_NAME)
            }
            if(containsKey(EXTRA_CATALOG)){
                productCatalog = getParcelable(EXTRA_CATALOG)
            }
            if(containsKey(EXTRA_CATEGORY)){
                productCategory = getParcelable(EXTRA_CATEGORY)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productCategoryRecommendationAdapter = ProductCategoryRecommendationAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = productCategoryRecommendationAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        labelCatalog.setOnClickListener { onLabelCatalogClicked() }
        labelCategory.setOnClickListener { onLabelCategoryClicked() }
        texViewMenu?.run {  text = getString(R.string.label_save)
            setOnClickListener {
                setResult()
            }}
        if(isCategoryLocked){
            titleCategoryRecommendation.visibility = View.GONE
            recyclerView.visibility = View.GONE
            labelNotFindCategory.visibility = View.GONE
            setCatalogChosen(productCatalog)
        }else{
            titleCategoryRecommendation.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            labelNotFindCategory.visibility = View.VISIBLE
            presenter.getCategoryRecommendation(name)
        }
        if(productCategory.categoryId > 0) {
            presenter.fetchCatalogData(name, productCategory.categoryId.toLong(), 0, 1)
        }

        if(productCategory.categoryList != null && productCategory.categoryList?.size?:0 >= 1){
            setCategoryChosen(productCategory)
        }else{
            presenter.fetchCategory(productCategory.categoryId.toLong())
        }
    }

    private fun onLabelCategoryClicked() {
        if (isCategoryLocked) {
            activity?.run {
                val builder = AlertDialog.Builder(this,
                        R.style.AppCompatAlertDialogStyle)
                builder.setTitle(R.string.product_category_locked)
                builder.setMessage(R.string.product_category_locked_description)
                builder.setCancelable(true)
                builder.setNegativeButton(R.string.close) { dialog, _ -> dialog.cancel() }

                val alert = builder.create()
                alert.show()
            }

        } else {
            activity?.let {
                val intent = RouteManager.getIntent(it,
                        ApplinkConstInternalMarketplace.PRODUCT_CATEGORY_PICKER,
                        productCategory.categoryId.toString())
                intent?.run { startActivityForResult(this, REQUEST_CODE_GET_CATEGORY) }
            }
        }
    }

    private fun onLabelCatalogClicked() {
        context?.run {
            startActivityForResult(ProductEditCatalogPickerActivity
                    .createIntent(this, name, productCategory.categoryId.toLong(), productCatalog),
                    REQUEST_CODE_GET_CATALOG)
        }
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        if(this.productCategory.categoryId != productCategory.categoryId) {
            presenter.fetchCatalogData(name, productCategory.categoryId.toLong(), 0, 1)
            resetCategoryCatalog()
        }
        this.productCategory = productCategory.copy()
        presenter.categoryId = productCategory.categoryId.toLong()
        setCategoryChosen(this.productCategory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data.getParcelableExtra(EXTRA_CATALOG)
                    setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val newCategoryId = data.getLongExtra(ProductExtraConstant.CATEGORY_RESULT_ID, -1).toInt()
                    if(productCategory.categoryId != newCategoryId){
                        presenter.fetchCatalogData(name, newCategoryId.toLong(), 0, 1)
                    }
                    productCategory.categoryId = newCategoryId
                    presenter.categoryId = newCategoryId.toLong()
                    presenter.fetchCategory(productCategory.categoryId.toLong())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setResult(){
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_CATALOG, productCatalog)
                putExtra(EXTRA_CATEGORY, productCategory)
            })
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_CATEGORY, productCategory)
        outState.putParcelable(SAVED_PRODUCT_CATALOG, productCatalog)
        outState.putString(SAVED_NAME, name)
        outState.putLong(SAVED_CATEGORY_ID, presenter.categoryId)
    }

    companion object {
        const val SAVED_PRODUCT_CATEGORY = "SAVED_PRODUCT_CATEGORY"
        const val SAVED_PRODUCT_CATALOG = "SAVED_PRODUCT_CATALOG"
        const val SAVED_NAME = "SAVED_NAME"
        const val SAVED_CATEGORY_ID = "SAVED_CATEGORY_ID"
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onSuccessLoadRecommendationCategory(categories: List<ProductCategoryPredictionViewModel>) {
        renderRecommendation(categories)
    }

    override fun onErrorLoadRecommendationCategory(throwable: Throwable?) {
        setCatalogChosen(productCatalog)
        setCategoryChosen(productCategory)
    }

    override fun onSuccessLoadCatalog(keyword: String, departmentId: Long, catalogs: List<Catalog>) {
        setVisibilityCatalog(!catalogs.isEmpty())
    }

    override fun onErrorLoadCatalog(errorMessage: String?) {
        productCatalog.catalogId = -1
    }

    override fun populateCategory(strings: List<String>, categoryId: Long) {
        val category = strings.filter { !TextUtils.isEmpty(it) }.joinToString(separator = " / ")
        setCategoryChosen(ProductCategory(categoryId = categoryId.toInt(), categoryName = category, categoryList =strings.toTypedArray()))
    }

    private fun setCategoryChosen(productCategory: ProductCategory){
        if (!TextUtils.isEmpty(productCategory.getCategoryLastName())) {
            labelCategory.setContent(productCategory.getCategoryLastName())
        }
        this.productCategory = productCategory.copy()
        productCategoryRecommendationAdapter.setSelectedCategory(productCategory)
    }

    private fun setCatalogChosen(productCatalog: ProductCatalog){
        if(!TextUtils.isEmpty(productCatalog.catalogName)) {
            labelCatalog.setContent(productCatalog.catalogName)
        } else {
            labelCatalog.setContent(getString(R.string.label_choose))
        }
    }

    private fun renderRecommendation(categories: List<ProductCategoryPredictionViewModel>){
        productCategoryRecommendationAdapter.replaceData(categories.map {ProductCategory().apply {
            categoryId = it.lastCategoryId
            categoryName = it.printedString
            categoryList = it.categoryName
        }})
        setCatalogChosen(productCatalog)
        setCategoryChosen(productCategory)
    }

    fun resetCategoryCatalog(){
        productCategoryRecommendationAdapter.selectedPosition = -1
        productCatalog = ProductCatalog()
        setCatalogChosen(productCatalog)
        setVisibilityCatalog(false)
    }

    private fun setVisibilityCatalog(isVisible: Boolean) {
        if(isVisible){
            titleCatalog.visibility = View.VISIBLE
            labelCatalog.visibility = View.VISIBLE
        }else{
            titleCatalog.visibility = View.GONE
            labelCatalog.visibility = View.GONE
        }
    }
}
