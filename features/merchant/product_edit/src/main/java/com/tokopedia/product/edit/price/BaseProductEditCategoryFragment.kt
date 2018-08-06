package com.tokopedia.product.edit.price

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
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.edit.view.presenter.ProductEditCategoryPresenter
import kotlinx.android.synthetic.main.fragment_product_edit_category.*
import javax.inject.Inject

abstract class BaseProductEditCategoryFragment : BaseDaggerFragment(),
        ProductEditCategoryView, ProductCategoryRecommendationAdapter.Listener{

    @Inject
    lateinit var presenter: ProductEditCategoryPresenter

    private val appRouter : Context by lazy { activity?.application as Context }

    private lateinit var productCategoryRecommendationAdapter: ProductCategoryRecommendationAdapter

    override fun getScreenName(): String? = null

    private var productName: String = ""
    private var isCategoryLocked: Boolean = false

    var productCatalog = ProductCatalog()
    var productCategory = ProductCategory()

    val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.run {
            productCategory = getParcelable(EXTRA_CATEGORY)
            productCatalog = getParcelable(EXTRA_CATALOG)
            productName = getString(EXTRA_NAME, "")
            isCategoryLocked = getBoolean(EXTRA_CATEGORY_LOCKED)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_category, container, false)
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
        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
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
            presenter.getCategoryRecommendation(productName)
            if(productCategory.categoryId > 0) {
                presenter.fetchCatalogData(productName, productCategory.categoryId.toLong(), 0, 1)
            }
        }
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
            if (appRouter is ProductEditModuleRouter){
                startActivityForResult((appRouter as ProductEditModuleRouter)
                        .getCategoryPickerIntent(activity, productCategory.categoryId)
                        , REQUEST_CODE_GET_CATEGORY)
            }
        }
    }

    private fun onLabelCatalogClicked() {
        context?.run {
            startActivityForResult(ProductEditCatalogPickerActivity
                    .createIntent(this, productName, productCategory.categoryId.toLong(), productCatalog),
                    REQUEST_CODE_GET_CATALOG)
        }
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        if(this.productCategory.categoryId != productCategory.categoryId) {
            presenter.fetchCatalogData(productName, productCategory.categoryId.toLong(), 0, 1)
        }
        this.productCategory = productCategory
        setCategoryChosen(productCategory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val newCategoryId = data!!.getLongExtra(ProductExtraConstant.CATEGORY_RESULT_ID, -1).toInt()
                    if(productCategory.categoryId != newCategoryId){
                        presenter.fetchCatalogData(productName, newCategoryId.toLong(), 0, 1)
                    }
                    productCategory.categoryId = newCategoryId
                    presenter.fetchCategory(productCategory.categoryId.toLong())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_CATALOG, productCatalog)
        intent.putExtra(EXTRA_CATEGORY, productCategory)
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
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
        var category = ""
        var i = 0
        val sizei = strings.size
        while (i < sizei) {
            val categoryName = strings.get(i)
            if (TextUtils.isEmpty(categoryName)) {
                i++
                continue
            }
            category += categoryName
            if (i < sizei - 1) {
                category += "/"
            }
            i++
        }
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
        }
    }

    private fun renderRecommendation(categories: List<ProductCategoryPredictionViewModel>){
        productCategoryRecommendationAdapter.replaceData(categories.map {ProductCategory().apply {
            categoryId = it.lastCategoryId
            categoryName = it.printedString
        }})
        setCatalogChosen(productCatalog)
        setCategoryChosen(productCategory)
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
