package com.tokopedia.home_wishlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home_wishlist.component.HasComponent
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment

@SuppressLint("GoogleAppIndexingApiWarning")
class WishlistActivity : BaseSimpleActivity() {

    companion object{
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, WishlistActivity::class.java)
    }

    override fun getNewFragment(): Fragment? {
        return WishlistFragment.newInstance()
    }
}
