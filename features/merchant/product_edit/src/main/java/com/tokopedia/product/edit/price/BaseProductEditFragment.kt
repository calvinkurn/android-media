package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.view.activity.*
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment.Companion.EXTRA_KEYWORD
import kotlinx.android.synthetic.main.fragment_base_product_edit.*

abstract class BaseProductEditFragment : Fragment() {

    private var productImages = ArrayList<String>()
    private var productName = ProductName()
    private var productCategory = ProductCategory()
    private var productCatalog = ProductCatalog()
    private var productPrice = ProductPrice()
    private var productDescription = ProductDescription()
    private var productStock = ProductStock()
    private var productLogistic = ProductLogistic()
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

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
        setPriceSectionData(productPrice)
        setDescriptionSectionData(productDescription)
        setLogisticSectionData(productLogistic)
        setStockSectionData(productStock)

        llCategoryCatalog.setOnClickListener {
            startActivityForResult(ProductEditCategoryActivity.createIntent(activity!!, productCategory, productCatalog), REQUEST_CODE_GET_CATALOG_CATEGORY)
        }
        labelViewNameProduct.setOnClickListener {
            startActivityForResult(ProductEditNameActivity.createIntent(activity!!, productName), REQUEST_CODE_GET_NAME)
        }
        labelViewPriceProduct.setOnClickListener {
            startActivityForResult(ProductEditPriceActivity.createIntent(activity!!, productPrice, false, false, true), REQUEST_CODE_GET_PRICE)
        }
        labelViewDescriptionProduct.setOnClickListener {
            startActivityForResult(ProductEditDescriptionActivity.createIntent(activity!!, productDescription, productName.name!!), REQUEST_CODE_GET_DESCRIPTION) }
        labelViewStockProduct.setOnClickListener {
            startActivityForResult(ProductEditStockActivity.createIntent(activity!!, productStock), REQUEST_CODE_GET_STOCK)
        }
        labelViewWeightLogisticProduct.setOnClickListener {
            startActivityForResult(ProductEditWeightLogisticActivity.createIntent(activity!!, productLogistic), REQUEST_CODE_GET_LOGISTIC)
        }

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {  }
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
                REQUEST_CODE_GET_DESCRIPTION -> {
                    productDescription = data!!.getParcelableExtra(EXTRA_DESCRIPTION)
                    setDescriptionSectionData(productDescription)
                }
                REQUEST_CODE_GET_PRICE -> {
                    productPrice = data!!.getParcelableExtra(EXTRA_PRICE)
                    setPriceSectionData(productPrice)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun setPriceSectionData(productPrice: ProductPrice){
        if(productPrice.price>0) {
            labelViewPriceProduct.setContent(CurrencyFormatUtil.convertPriceValueToIdrFormat(productPrice.price, true))
        }
    }

    private fun setDescriptionSectionData(productDescription: ProductDescription){
        if(productDescription.description!=null) {
            labelViewDescriptionProduct.setContent(productDescription.description)
            labelViewDescriptionProduct.setSubTitle(productDescription.videoIDs.size.toString())
        }
    }

    private fun setStockSectionData(productStock: ProductStock){
        if(productStock.isActive) {
            if(productStock.stockCount > 0)
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_limited))
            else
                labelViewStockProduct.setContent(getString(R.string.product_label_stock_always_available))
        } else
            labelViewStockProduct.setContent(getString(R.string.product_label_stock_empty))
    }

    private fun setLogisticSectionData(productLogistic: ProductLogistic){
        if(productLogistic.weight > 0) {
            labelViewWeightLogisticProduct.setContent("${productLogistic.weight} ${getString(ProductEditWeightLogisticFragment.getWeightTypeTitle(productLogistic.weightType))}")
            labelViewWeightLogisticProduct.setSubTitle("")
        }
    }

    companion object {
        const val REQUEST_CODE_GET_IMAGES = 1
        const val REQUEST_CODE_GET_CATALOG_CATEGORY = 2
        const val REQUEST_CODE_GET_NAME = 3
        const val REQUEST_CODE_GET_PRICE = 4
        const val REQUEST_CODE_GET_DESCRIPTION = 5
        const val REQUEST_CODE_GET_STOCK = 6
        const val REQUEST_CODE_GET_LOGISTIC = 7

        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CATALOG = "EXTRA_CATALOG"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_IMAGES = "EXTRA_IMAGES"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_LOGISTIC = "EXTRA_LOGISTIC"

        const val EXTRA_IS_OFFICIAL_STORE = "EXTRA_OFFICIAL_STORE"
        const val EXTRA_HAS_VARIANT = "EXTRA_HAS_VARIANT"
        const val EXTRA_IS_GOLD_MERCHANT = "EXTRA_GOLD_MERCHANT"

//        fun createInstance() = BaseProductEditFragment()
    }
}
