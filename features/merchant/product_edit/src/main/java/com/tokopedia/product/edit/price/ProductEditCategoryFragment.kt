package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.edit.di.component.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.edit.view.presenter.ProductEditCategoryPresenter
import kotlinx.android.synthetic.main.partial_product_edit_category.*
import javax.inject.Inject

class ProductEditCategoryFragment : BaseDaggerFragment(), ProductEditCategoryCatalogViewHolder.Listener,
        ProductEditCategoryView{

    @Inject
    lateinit var presenter: ProductEditCategoryPresenter

    val appRouter : Context? by lazy { activity?.application as? Context }

    override fun getScreenName(): String? = null

    private var productName: String = ""
    private var isCategoryLocked: Boolean = false

    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    private lateinit var productEditCategoryCatalogViewHolder: ProductEditCategoryCatalogViewHolder
    private var productCatalog = ProductCatalog()
    private var productCategory = ProductCategory()
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

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
        productEditCategoryCatalogViewHolder = ProductEditCategoryCatalogViewHolder(view, this, context)
        productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog)
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener {setResult() }
        }
        if(isCategoryLocked){
            titleCategoryRecommendation.visibility = View.GONE
            recyclerView.visibility = View.GONE
            labelNotFindCategory.visibility = View.GONE
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

    override fun onLabelCategoryClicked() {
        if (isCategoryLocked) {
            val builder = AlertDialog.Builder(activity!!,
                    R.style.AppCompatAlertDialogStyle)
            builder.setTitle(R.string.product_category_locked)
            builder.setMessage(R.string.product_category_locked_description)
            builder.setCancelable(true)
            builder.setNegativeButton(R.string.close) { dialog, id -> dialog.cancel() }

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

    override fun onLabelCatalogClicked() {
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
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog)
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
        activity?.let {
            val intent = Intent().apply { putExtra(EXTRA_CATALOG, productCatalog)
                putExtra(EXTRA_CATEGORY, productCategory)}

            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2

        fun createInstance(productName: String, category: ProductCategory?, catalog: ProductCatalog?, isCategoryLocked : Boolean) =
                ProductEditCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_NAME, productName)
                        putBoolean(EXTRA_CATEGORY_LOCKED, isCategoryLocked)
                        putParcelable(EXTRA_CATALOG, catalog ?: ProductCatalog())
                        putParcelable(EXTRA_CATEGORY, category ?: ProductCategory())
                }
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onSuccessLoadRecommendationCategory(categories: List<ProductCategoryPredictionViewModel>) {
        productEditCategoryCatalogViewHolder.renderRecommendation(categories)
    }

    override fun onErrorLoadRecommendationCategory(throwable: Throwable?) {}

    override fun populateCategory(strings: List<String>) {
        val category = strings.filter { !TextUtils.isEmpty(it) }.joinToString(separator = " / ")
        productEditCategoryCatalogViewHolder.setCategoryChosen(ProductCategory(categoryName = category))
    }


    override fun onSuccessLoadCatalog(keyword: String, departmentId: Long, catalogs: List<Catalog>) {
        productEditCategoryCatalogViewHolder.setVisiblityCatalog(!catalogs.isEmpty())
    }

    override fun onErrorLoadCatalog(errorMessage: String?) {
        productCatalog.catalogId = -1
    }
}
