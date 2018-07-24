package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity

class ProductEditCategoryFragment : Fragment(), ProductEditCategoryCatalogViewHolder.Listener{

    private lateinit var productEditCategoryCatalogViewHolder: ProductEditCategoryCatalogViewHolder
    private lateinit var productCatalog: ProductCatalog
    private lateinit var productCategory: ProductCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_CATALOG)&&activity!!.intent.hasExtra(EXTRA_CATEGORY)) {
            productCatalog = activity!!.intent.getParcelableExtra(EXTRA_CATALOG)
            productCategory = activity!!.intent.getParcelableExtra(EXTRA_CATEGORY)
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
    }

    override fun onLabelCategoryClicked() {

    }

    override fun onLabelCatalogClicked() {
        startActivityForResult(Intent(context, ProductEditCatalogPickerActivity::class.java), REQUEST_CODE_GET_CATALOG)
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        this.productCategory = productCategory
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data.getParcelableExtra(EXTRA_CATALOG)
                    productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    productCategory = data.getParcelableExtra(EXTRA_CATEGORY)
                    productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_next, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_next) {
            val intent = Intent()
            intent.putExtra(EXTRA_CATALOG, productCatalog)
            intent.putExtra(EXTRA_CATEGORY, productCategory)
            activity!!.setResult(Activity.RESULT_OK, intent)
            activity!!.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
        fun createInstance(): Fragment {
            return ProductEditCategoryFragment()
        }
    }
}
