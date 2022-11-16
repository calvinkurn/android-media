package com.tokopedia.mvc.presentation.product.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.util.constant.BundleConstant

class ProductListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, selectedProducts: List<SelectedProduct>) {
            val bundle = Bundle().apply {
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
            }
            val starter = Intent(context, ProductListActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }
    }

    private val selectedProducts by lazy {
        intent?.extras?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_product_list)

        val products = (selectedProducts as? List<SelectedProduct>).orEmpty()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProductListFragment.newInstance(products))
            .commit()
    }

}
