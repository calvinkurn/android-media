package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.model.ProductCatalog
import kotlinx.android.synthetic.main.fragment_product_edit_catalog_picker.*

class ProductEditCatalogPickerFragment : Fragment() {

    private lateinit var productCatalogAdapter: ProductCatalogAdapter
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }
    private var productCatalog = ProductCatalog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_CATALOG)) {
            productCatalog = activity!!.intent.getParcelableExtra(EXTRA_CATALOG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_catalog_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataDummy = ArrayList<ProductCatalog>()
        dataDummy.add(ProductCatalog().apply {
            catalogId = 1
            catalogName = "aw"
            catalogImage = "url" })
        dataDummy.add(ProductCatalog().apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        dataDummy.add(ProductCatalog().apply {
            catalogId = 3
            catalogName = "qw"
            catalogImage = "url" })
        dataDummy.add(ProductCatalog().apply {
            catalogId = 4
            catalogName = "er"
            catalogImage = "url" })
        dataDummy.add(ProductCatalog().apply {
            catalogId = 5
            catalogName = "ty"
            catalogImage = "url" })
        dataDummy.add(ProductCatalog().apply {
            catalogId = 6
            catalogName = "po"
            catalogImage = "url" })
        productCatalogAdapter = ProductCatalogAdapter(dataDummy)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = productCatalogAdapter
        recyclerView.setHasFixedSize(true)
        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
        productCatalogAdapter.setSelectedCategory(productCatalog)
    }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_CATALOG, productCatalogAdapter.getSelectedCatalog())
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
    }

    companion object {
        fun createInstance() = ProductEditCatalogPickerFragment()
    }
}
