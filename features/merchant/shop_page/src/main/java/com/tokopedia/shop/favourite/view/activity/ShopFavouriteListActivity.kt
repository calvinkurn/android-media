package com.tokopedia.shop.favourite.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.favourite.view.fragment.ShopFavouriteListFragment

/**
 * Created by normansyahputa on 2/8/18.
 */
class ShopFavouriteListActivity : BaseSimpleActivity(), HasComponent<ShopComponent?> {
    private var shopId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(ShopParamConstant.EXTRA_SHOP_ID)
        if (shopId == null) {
            val pathSegments = intent.data!!.pathSegments
            if (pathSegments.size > 1) {
                shopId = pathSegments[1]
            }
        }
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        toolbar.background = ColorDrawable(
                MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

    override fun getNewFragment(): Fragment? {
        return ShopFavouriteListFragment.createInstance(shopId)
    }

    override fun getComponent(): ShopComponent {
        return ShopComponentHelper().getComponent(application, this)
    }

    companion object {
        fun createIntent(context: Context?, shopId: String?): Intent {
            val intent = Intent(context, ShopFavouriteListActivity::class.java)
            intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId)
            return intent
        }
    }
}