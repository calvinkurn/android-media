package com.tokopedia.product.manage.item.price

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.constant.ProductExtraConstant
import com.tokopedia.product.manage.item.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.manage.item.price.model.ProductCatalog
import com.tokopedia.product.manage.item.price.model.ProductCategory
import com.tokopedia.product.manage.item.util.ProductEditModuleRouter
import com.tokopedia.product.manage.item.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.manage.item.view.listener.ProductEditCategoryView
import com.tokopedia.product.manage.item.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.manage.item.view.presenter.ProductEditCategoryPresenter
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
            productCatalog = getParcelable(EXTRA_CATALOG)?:ProductCatalog()
            name = getString(EXTRA_NAME, "")
            isCategoryLocked = getBoolean(EXTRA_CATEGORY_LOCKED)
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
            setCategoryChosen(productCategory)
        }else{
            titleCategoryRecommendation.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            labelNotFindCategory.visibility = View.VISIBLE
            presenter.getCategoryRecommendation(name)
            if(productCategory.categoryId > 0) {
                presenter.fetchCatalogData(name, productCategory.categoryId.toLong(), 0, 1)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_NAME, name)
        outState.putParcelable(EXTRA_CATEGORY, productCategory)
        outState.putParcelable(EXTRA_CATALOG, productCatalog)
    }

    private fun onLabelCategoryClicked() {
        if (isCategoryLocked) {
            val builder = AlertDialog.Builder(activity!!,
                    R.style.AppCompatAlertDialogStyle)
            builder.setTitle(R.string.product_category_locked)
            builder.setMessage(R.string.product_category_locked_description)
            builder.setCancelable(true)
            builder.setNegativeButton(R.string.close) { dialog, _ -> dialog.cancel() }

            val alert = builder.create()
            alert.show()
        } else {
            if (appRouter != null && appRouter is ProductEditModuleRouter){
                startActivityForResult((appRouter as ProductEditModuleRouter)
                        .getCategoryPickerIntent(activity, productCategory.categoryId)
                        , REQUEST_CODE_GET_CATEGORY)
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
        }
        this.productCategory = productCategory
        presenter.categoryId = productCategory.categoryId.toLong()
        setCategoryChosen(productCategory)
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

    companion object {
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
        if (catalogs.isEmpty()) {
            setVisibilityCatalog(false)
        } else {
            setVisibilityCatalog(true)
        }
    }

    override fun onErrorLoadCatalog(errorMessage: String?) {
        productCatalog.catalogId = -1
    }

    override fun populateCategory(strings: List<String>) {
        val category = strings.filter { !TextUtils.isEmpty(it) }.joinToString(separator = " / ")
        setCategoryChosen(ProductCategory(categoryName = category))
    }

    private fun setCategoryChosen(productCategory: ProductCategory){
        if (!TextUtils.isEmpty(productCategory.categoryName)) {
            labelCategory.setContent(productCategory.categoryName)
        }
        productCategoryRecommendationAdapter.setSelectedCategory(productCategory)
    }

    fun setCatalogChosen(productCatalog: ProductCatalog){
        if(!TextUtils.isEmpty(productCatalog.catalogName)) {
            labelCatalog.setContent(productCatalog.catalogName)
        } else {
            labelCatalog.setContent(getString(R.string.label_choose))
        }
    }

    private fun renderRecommendation(categories: List<ProductCategoryPredictionViewModel>){
        resetCategoryCatalog()
        productCategoryRecommendationAdapter.replaceData(categories.map {ProductCategory().apply {
            categoryId = it.lastCategoryId
            categoryName = it.printedString
        }})
        setCatalogChosen(productCatalog)
        setCategoryChosen(productCategory)
    }

    private fun resetCategoryCatalog(){
        productCategoryRecommendationAdapter.selectedPosition = -1
        productCatalog = ProductCatalog()
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
