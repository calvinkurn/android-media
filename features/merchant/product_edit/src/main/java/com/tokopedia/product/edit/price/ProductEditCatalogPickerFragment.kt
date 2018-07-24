package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.model.ProductCatalog
import kotlinx.android.synthetic.main.fragment_product_edit_catalog_picker.*

class ProductEditCatalogPickerFragment : Fragment() {

    private lateinit var productCatalogAdapter: ProductCatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_catalog_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataDummy = ArrayList<ProductCatalog>()
        val productCatalog = ProductCatalog()
        dataDummy.add(productCatalog.apply {
            catalogId = 1
            catalogName = "aw"
            catalogImage = "url" })
        dataDummy.add(productCatalog.apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        dataDummy.add(productCatalog.apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        dataDummy.add(productCatalog.apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        dataDummy.add(productCatalog.apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        dataDummy.add(productCatalog.apply {
            catalogId = 2
            catalogName = "wo"
            catalogImage = "url" })
        productCatalogAdapter = ProductCatalogAdapter(dataDummy)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = productCatalogAdapter
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_next, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_next) {
            val intent = Intent()
            intent.putExtra(EXTRA_CATALOG, productCatalogAdapter.getSelectedCatalog())
            activity!!.setResult(Activity.RESULT_OK, intent)
            activity!!.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun createInstance(): Fragment {
            return ProductEditCatalogPickerFragment()
        }
    }
}
