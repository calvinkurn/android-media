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
        return ShopProductSearchPageListParentFragment.newInstance(intent.getStringExtra(PARAM_SHOP_ID) ?: "",
            intent.getStringExtra(PARAM_SOURCE) ?: "",
            intent.getStringExtra(
                PARAM_SHOP_NAME) ?: "",
            intent.getStringExtra(
                PARAM_SHOP_BADGE) ?: "")
    }

    companion object {
        const val PRODUCT_PAGE_TITLE = "Tag Produk"
        const val PARAM_SHOP_NAME = "shop_name"
        const val PARAM_SHOP_BADGE = "shop_badge"
        const val PARAM_SHOP_ID = "shopid"
        const val PARAM_SOURCE = "source"
    }
}
