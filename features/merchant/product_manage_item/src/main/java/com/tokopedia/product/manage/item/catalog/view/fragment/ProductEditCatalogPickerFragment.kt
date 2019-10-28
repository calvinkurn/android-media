package com.tokopedia.product.manage.item.catalog.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.category.di.ProductEditCategoryCatalogModule
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.catalog.view.adapter.ProductCatalogTypeFactory
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.manage.item.catalog.view.listener.ProductEditCatalogPickerView
import com.tokopedia.product.manage.item.catalog.view.presenter.ProductEditCatalogPickerPresenter
import com.tokopedia.product.manage.item.category.di.DaggerProductEditCategoryCatalogComponent
import kotlinx.android.synthetic.main.fragment_product_edit_catalog_picker.*
import javax.inject.Inject

class ProductEditCatalogPickerFragment : BaseListFragment<ProductCatalog, ProductCatalogTypeFactory>(),
        ProductEditCatalogPickerView, ProductCatalogTypeFactory.OnCatalogClickedListener {

    override fun isItemSelected(position: Int) = selectedPosCatalog == position

    override fun onSelected(catalog: ProductCatalog, position: Int) {
        selectedPosCatalog = position
        choosenCatalog = catalog
        adapter.notifyDataSetChanged()
    }

    override fun getAdapterTypeFactory() = ProductCatalogTypeFactory().also { it.setCatalogClickedListener(this) }

    override fun onItemClicked(t: ProductCatalog?) {}

    override fun getScreenName(): String? = null

    override fun loadData(page: Int) {
        presenter.getCatalog(productName, categoryId, page)
    }

    @Inject lateinit var presenter: ProductEditCatalogPickerPresenter
    private var productName = ""
    private var categoryId = -1L
    private var choosenCatalog: ProductCatalog = ProductCatalog()
    private var selectedPosCatalog = -1

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.run {
            productName = getString(EXTRA_NAME, "")
            categoryId = getLong(EXTRA_CATEGORY, -1L)
            choosenCatalog = getParcelable(EXTRA_CATALOG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_catalog_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.setHasFixedSize(true)
        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener { setResult() }
        }

        add_no_catalog.setOnClickListener {
            choosenCatalog = ProductCatalog()
            setResult()
        }
    }

    override fun getEndlessLayoutManagerListener() = EndlessLayoutManagerListener { recycler_view.layoutManager }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_CATALOG, choosenCatalog)
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onSuccessLoadCatalog(catalogs: List<ProductCatalog>, hasNextData: Boolean) {
        catalogs.forEachIndexed { index, productCatalog ->
            if (productCatalog.catalogName.toLowerCase() == choosenCatalog.catalogName.toLowerCase()){
                selectedPosCatalog = index
                return@forEachIndexed
            }
        }
        super.renderList(catalogs, hasNextData)
    }

    override fun onErrorLoadCatalog(throwable: Throwable?) {

    }

    companion object {

        fun createInstance(productName: String, categoryId: Long, choosenCatalog: ProductCatalog) =
                ProductEditCatalogPickerFragment().apply {
                    arguments = Bundle().apply { putString(EXTRA_NAME, productName)
                                                putLong(EXTRA_CATEGORY, categoryId)
                                                putParcelable(EXTRA_CATALOG, choosenCatalog)}
        }
    }
}
