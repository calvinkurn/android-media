package com.tokopedia.shop.settings.basicinfo.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment

class ShopEditBasicInfoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return ShopEditBasicInfoFragment.newInstance(intent?.extras)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_edit_basic_info
    }

    override fun getParentViewResourceID(): Int {
        return R.id.container
    }

    companion object {
        const val EXTRA_SHOP_MODEL = "shop_model"
        const val EXTRA_MESSAGE = "message"

        @JvmStatic
        fun createIntent(context: Context, shopBasicDataModel: ShopBasicDataModel?): Intent {
            return Intent(context, ShopEditBasicInfoActivity::class.java).apply {
                putExtra(EXTRA_SHOP_MODEL, shopBasicDataModel)
            }
        }
    }
}
