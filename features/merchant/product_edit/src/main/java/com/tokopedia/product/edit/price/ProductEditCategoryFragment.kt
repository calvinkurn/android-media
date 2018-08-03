package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.di.component.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel
import com.tokopedia.product.edit.view.presenter.ProductEditCategoryPresenter
import javax.inject.Inject

class ProductEditCategoryFragment : BaseDaggerFragment(), ProductEditCategoryCatalogViewHolder.Listener,
        ProductEditCategoryView{

    val appRouter : Context by lazy { activity?.application as Context }

    override fun getScreenName(): String? = null

    @Inject lateinit var presenter: ProductEditCategoryPresenter

    private var productName: String = ""


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
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.run {
            productCategory = getParcelable(EXTRA_CATEGORY)
            productCatalog = getParcelable(EXTRA_CATALOG)
            productName = getString(EXTRA_PRODUCT_NAME, "")
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
        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
        presenter.getCategotyRecommendation(productName)
    }

    override fun onLabelCategoryClicked() {
        if (appRouter is ProductEditModuleRouter){
            startActivityForResult((appRouter as ProductEditModuleRouter)
                    .getCategoryPickerIntent(activity, productCategory.categoryId)
                    , REQUEST_CODE_GET_CATEGORY)
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
                    productCategory = data!!.getParcelableExtra(EXTRA_CATEGORY)
                    productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
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
        private const val EXTRA_PRODUCT_NAME = "product_name"
        private const val EXTRA_CATALOG = "extra_catalog"
        private const val EXTRA_CATEGORY = "extra_category"
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2

        fun createInstance(productName: String, category: ProductCategory?, catalog: ProductCatalog?) =
                ProductEditCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_PRODUCT_NAME, productName)
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

    override fun onErrorLoadRecommendationCategory(throwable: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
