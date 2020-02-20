package com.tokopedia.home_wishlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment

@SuppressLint("GoogleAppIndexingApiWarning")
class WishlistActivity : AppCompatActivity(), HasComponent<WishlistComponent> {
    override fun getComponent(): WishlistComponent {
        return DaggerWishlistComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    companion object{
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, WishlistActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, WishlistFragment.newInstance(), "wishlist")
                .commit()
    }
}
