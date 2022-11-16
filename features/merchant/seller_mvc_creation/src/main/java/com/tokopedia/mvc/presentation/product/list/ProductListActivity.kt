package com.tokopedia.mvc.presentation.product.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.util.constant.BundleConstant

class ProductListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, selectedProductIds: List<Long>) {
            val bundle = Bundle().apply {
                putLongArray(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    selectedProductIds.toLongArray()
                )
            }
            val starter = Intent(context, ProductListActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }
    }

    private val selectedProductIds by lazy {
        intent?.extras?.getLongArray(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS)?.asList().orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_product_list)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProductListFragment.newInstance(selectedProductIds))
            .commit()
    }

}
