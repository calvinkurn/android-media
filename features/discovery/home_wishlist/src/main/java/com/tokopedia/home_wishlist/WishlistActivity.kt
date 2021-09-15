package com.tokopedia.home_wishlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment
import kotlinx.android.synthetic.main.activity_wishlist.*

@SuppressLint("GoogleAppIndexingApiWarning")
class WishlistActivity : BaseSimpleActivity() {

    companion object{
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, WishlistActivity::class.java)
    }

    override fun getLayoutRes() = R.layout.activity_wishlist

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): Fragment? {
        return WishlistFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}
