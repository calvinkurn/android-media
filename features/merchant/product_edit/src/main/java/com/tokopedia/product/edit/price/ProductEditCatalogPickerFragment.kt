package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel
import com.tokopedia.product.edit.di.component.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.view.listener.ProductEditCatalogPickerView
import com.tokopedia.product.edit.view.presenter.ProductEditCatalogPickerPresenter
import kotlinx.android.synthetic.main.fragment_product_edit_catalog_picker.*
import javax.inject.Inject

class ProductEditCatalogPickerFragment : BaseDaggerFragment(), ProductEditCatalogPickerView {

    @Inject lateinit var presenter: ProductEditCatalogPickerPresenter
    private var productName = ""
    private var categoryId = -1L
    private var choosenCatalog: ProductCatalog = ProductCatalog()

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    val productCatalogAdapter =  ProductCatalogAdapter(mutableListOf())
    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

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
        presenter.getCatalog(productName, categoryId)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = productCatalogAdapter
        recyclerView.setHasFixedSize(true)
        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener { setResult() }
        }

        productCatalogAdapter.setSelectedCategory(choosenCatalog)
    }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_CATALOG, productCatalogAdapter.getSelectedCatalog())
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onSuccessLoadCatalog(catalogs: List<ProductCatalog>, totalRecord: Int) {
        productCatalogAdapter.replaceData(catalogs)
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
