package com.tokopedia.home_wishlist.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.home_wishlist.component.HasComponent
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.test.R
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment

class InstrumentationWishlistTestActivity : AppCompatActivity(), HasComponent<WishlistComponent> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist_test)

        val fragment: Fragment = WishlistFragment()

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun getComponent(): WishlistComponent {
        return DaggerWishlistComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }
}