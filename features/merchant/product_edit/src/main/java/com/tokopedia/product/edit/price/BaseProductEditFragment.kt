package com.tokopedia.product.edit.price

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.view.activity.*
import kotlinx.android.synthetic.main.fragment_base_product_edit.*

class BaseProductEditFragment : Fragment() {

    private lateinit var productCatalog: ProductCatalog
    private lateinit var productName: ProductName
    private lateinit var productCategory: ProductCategory
    private lateinit var productImages: ArrayList<String>

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

        textViewCategory.text = productCategory.categoryName
        textViewCatalog.text = productCatalog.catalogName
        if (productImages.size > 0) {
            ImageHandler.loadImageRounded2(context, imageOne, productImages[0])
            if (productImages.size > 1)
                ImageHandler.loadImageRounded2(context, imageOne, productImages[1])
        }
        labelViewNameProduct.setContent(productName.name)


        llCategoryCatalog.setOnClickListener {
            startActivity(Intent(activity, ProductEditCategoryActivity::class.java)
                    .putExtra(EXTRA_CATALOG, productCatalog)
                    .putExtra(EXTRA_CATEGORY, productCategory)) }
        labelViewNameProduct.setOnClickListener {
            startActivity(Intent(activity, ProductEditNameActivity::class.java)
                    .putExtra(EXTRA_NAME, productName))
        }
        labelViewPriceProduct.setOnClickListener { startActivity(Intent(activity, ProductEditPriceActivity::class.java)) }
        labelViewDescriptionProduct.setOnClickListener { startActivity(Intent(activity, ProductEditDescriptionActivity::class.java)) }
        labelViewStockProduct.setOnClickListener { startActivity(Intent(activity, ProductEditStockActivity::class.java)) }
        labelViewWeightLogisticProduct.setOnClickListener { startActivity(Intent(activity, ProductEditWeightLogisticActivity::class.java)) }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CATALOG = "EXTRA_CATALOG"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_IMAGES = "EXTRA_IMAGES"

        fun createInstance(): Fragment {
            return BaseProductEditFragment()
        }
    }
}
