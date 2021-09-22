package com.tokopedia.createpost.view.plist
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

 class ShopProductListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(PRODUCT_PAGE_TITLE)
    }

    override fun getNewFragment(): Fragment? {
        return ShopProductListFragment.newInstance(intent.getStringExtra("shopid") ?: "",
            intent.getStringExtra("source") ?: "",
            intent.getStringExtra(
                PARAM_SHOP_NAME) ?: "")
    }

    companion object {
        const val PRODUCT_PAGE_TITLE = "Tag Produk"
        private const val PARAM_SHOP_NAME = "shop_name"
        private const val REQUEST_CODE_LOGIN = 1
    }
}
