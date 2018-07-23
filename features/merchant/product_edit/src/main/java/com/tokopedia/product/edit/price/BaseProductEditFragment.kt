package com.tokopedia.product.edit.price

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.activity.*
import kotlinx.android.synthetic.main.fragment_base_product_edit.*

class BaseProductEditFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_base_product_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llCategoryCatalog.setOnClickListener { startActivity(Intent(activity, ProductEditCategoryActivity::class.java)) }
        labelViewNameProduct.setOnClickListener { startActivity(Intent(activity, ProductEditNameActivity::class.java)) }
        labelViewPriceProduct.setOnClickListener { startActivity(Intent(activity, ProductEditPriceActivity::class.java)) }
        labelViewDescriptionProduct.setOnClickListener { startActivity(Intent(activity, ProductEditDescriptionActivity::class.java)) }
        labelViewStockProduct.setOnClickListener { startActivity(Intent(activity, ProductEditStockActivity::class.java)) }
        labelViewWeightLogisticProduct.setOnClickListener { startActivity(Intent(activity, ProductEditWeightLogisticActivity::class.java)) }
    }

    companion object {

        fun createInstance(): Fragment {
            return BaseProductEditFragment()
        }
    }
}
