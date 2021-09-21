package com.tokopedia.shop.score.detail_old.view.activity

import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.score.detail_old.view.fragment.ShopScoreDetailFragment

class ShopScoreDetailActivity : BaseSimpleActivity() {

    companion object {
        private const val SCREEN_SELLER_SHOP_SCORE = "Shop Score"
    }

    override fun getScreenName() = SCREEN_SELLER_SHOP_SCORE

    override fun getNewFragment(): Fragment = ShopScoreDetailFragment.createFragment()

    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }
}