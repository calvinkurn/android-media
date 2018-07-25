package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.view.activity.*
import kotlinx.android.synthetic.main.fragment_base_product_edit.*

class BaseProductEditFragment : Fragment() {

    private var productCatalog = ProductCatalog()
    private var productName = ProductName()
    private var productCategory = ProductCategory()
    private var productImages = ArrayList<String>()
    private var productLogistic = ProductLogistic()
    private var productStock = ProductStock()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_NAME) && activity!!.intent.hasExtra(EXTRA_CATEGORY) && activity!!.intent.hasExtra(EXTRA_IMAGES)) {
            productName = activity!!.intent.getParcelableExtra(EXTRA_NAME)
            productCatalog = activity!!.intent.getParcelableExtra(EXTRA_CATALOG)
            productCategory = activity!!.intent.getParcelableExtra(EXTRA_CATEGORY)
            productCategory = activity!!.intent.getParcelableExtra(EXTRA_CATEGORY)
            productImages = activity!!.intent.getStringArrayListExtra(EXTRA_IMAGES)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_base_product_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImagesSectionData(productImages)
        setCategoryCatalogSection(productCategory,productCatalog)
        setNameSectionData(productName)
        setLogisticSectionData(productLogistic)
        setStockSectionData(productStock)

        llCategoryCatalog.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditCategoryActivity::class.java)
                    .putExtra(EXTRA_CATALOG, productCatalog)
                    .putExtra(EXTRA_CATEGORY, productCategory), REQUEST_CODE_GET_CATALOG_CATEGORY) }
        labelViewNameProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditNameActivity::class.java)
                    .putExtra(EXTRA_NAME, productName), REQUEST_CODE_GET_NAME)
        }
        labelViewPriceProduct.setOnClickListener { startActivity(Intent(activity, ProductEditPriceActivity::class.java)) }
        labelViewDescriptionProduct.setOnClickListener { startActivity(Intent(activity, ProductEditDescriptionActivity::class.java)) }
        labelViewStockProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditStockActivity::class.java)
                    .putExtra(EXTRA_STOCK, productStock), REQUEST_CODE_GET_STOCK) }
        labelViewWeightLogisticProduct.setOnClickListener {
            startActivityForResult(Intent(activity, ProductEditWeightLogisticActivity::class.java)
                    .putExtra(EXTRA_LOGISTIC, productLogistic), REQUEST_CODE_GET_LOGISTIC) }
    }

    private fun setImagesSectionData(productImages: ArrayList<String>){
        if (productImages.size > 0) {
            ImageHandler.loadImageRounded2(context, imageOne, productImages[0])
            if (productImages.size > 1)
                ImageHandler.loadImageRounded2(context, imageOne, productImages[1])
        }
    }

    private fun setCategoryCatalogSection(productCategory: ProductCategory, productCatalog: ProductCatalog){
        textViewCategory.text = productCategory.categoryName
        if(productCatalog.catalogName!=null) {
            textViewCatalog.run{
                visibility = View.VISIBLE
                text = productCatalog.catalogName
            }

        }
    }

    private fun setNameSectionData(productName: ProductName){
        labelViewNameProduct.setContent(productName.name)
    }

    private fun setStockSectionData(productStock: ProductStock){
        if(productStock.stockLimited)
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
        else
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
    }

    private fun setLogisticSectionData(productLogistic: ProductLogistic){
        if(productLogistic.weight > 0)
            labelViewWeightLogisticProduct.setContent("${productLogistic.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(productLogistic.weightType))}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG_CATEGORY -> {
                    productCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    productCategory = data.getParcelableExtra(EXTRA_CATEGORY)
                    setCategoryCatalogSection(productCategory, productCatalog)
                }
                REQUEST_CODE_GET_NAME -> {
                    productName = data!!.getParcelableExtra(EXTRA_NAME)
                    setNameSectionData(productName)
                }
                REQUEST_CODE_GET_LOGISTIC -> {
                    productLogistic = data!!.getParcelableExtra(EXTRA_LOGISTIC)
                    setLogisticSectionData(productLogistic)
                }
                REQUEST_CODE_GET_STOCK -> {
                    productStock = data!!.getParcelableExtra(EXTRA_STOCK)
                    setStockSectionData(productStock)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_GET_IMAGES = 1
        const val REQUEST_CODE_GET_CATALOG_CATEGORY = 2
        const val REQUEST_CODE_GET_NAME = 3
        const val REQUEST_CODE_GET_STOCK = 6
        const val REQUEST_CODE_GET_LOGISTIC = 7

        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CATALOG = "EXTRA_CATALOG"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_IMAGES = "EXTRA_IMAGES"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_LOGISTIC = "EXTRA_LOGISTIC"

        fun createInstance(): Fragment {
            return BaseProductEditFragment()
        }
    }
}
