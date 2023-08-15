package com.tokopedia.shop_widget.favourite.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop_widget.favourite.di.component.DaggerShopFavouriteComponent
import com.tokopedia.shop_widget.favourite.di.component.ShopFavouriteComponent
import com.tokopedia.shop_widget.favourite.di.module.ShopFavouriteModule
import com.tokopedia.shop_widget.favourite.view.fragment.ShopFavouriteListFragment

/**
 * Created by normansyahputa on 2/8/18.
 */
class ShopFavouriteListActivity : BaseSimpleActivity(), HasComponent<ShopFavouriteComponent?> {

    private var shopId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = intent.getStringExtra(EXTRA_SHOP_ID)
        if (shopId == null) {
            val pathSegments = intent.data!!.pathSegments
            if (pathSegments.size > 1) {
                shopId = pathSegments[1]
            }
        }
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        toolbar.background = ColorDrawable(
                MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        )
    }

    override fun getNewFragment(): Fragment {
        return ShopFavouriteListFragment.createInstance(shopId)
    }

    override fun getComponent(): ShopFavouriteComponent {
        return DaggerShopFavouriteComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .shopFavouriteModule(ShopFavouriteModule())
                .build()
    }

    companion object {
        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        fun createIntent(context: Context?, shopId: String?): Intent {
            val intent = Intent(context, ShopFavouriteListActivity::class.java)
            intent.putExtra(EXTRA_SHOP_ID, shopId)
            return intent
        }
    }
}