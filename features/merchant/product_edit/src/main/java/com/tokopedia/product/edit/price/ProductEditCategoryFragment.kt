package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder

class ProductEditCategoryFragment : Fragment(), ProductEditCategoryCatalogViewHolder.Listener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditCategoryCatalogViewHolder(view, this, context)
    }

    override fun onLabelCategoryClicked() {

    }

    override fun onLabelCatalogClicked() {

    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {

    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditCategoryFragment()
        }
    }
}
