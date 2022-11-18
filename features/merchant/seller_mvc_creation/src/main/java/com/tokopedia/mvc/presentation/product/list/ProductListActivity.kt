package com.tokopedia.mvc.presentation.product.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.BundleConstant

class ProductListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, selectedProducts: List<SelectedProduct>) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
            }
            val starter = Intent(context, ProductListActivity::class.java)
                .putExtras(bundle)
            context.startActivity(starter)
        }

        fun buildEditMode(context: Context, selectedProducts: List<SelectedProduct>): Intent {
            val bundle = Bundle().apply {
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.EDIT)
                putParcelableArrayList(
                    BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS,
                    ArrayList(selectedProducts)
                )
            }

            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

    private val pageMode by lazy { intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val selectedProducts by lazy {
        intent?.extras?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCT_IDS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_product_list)

        val pageMode = pageMode ?: return
        val products = (selectedProducts as? List<SelectedProduct>).orEmpty()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProductListFragment.newInstance(pageMode, products))
            .commit()
    }

}
