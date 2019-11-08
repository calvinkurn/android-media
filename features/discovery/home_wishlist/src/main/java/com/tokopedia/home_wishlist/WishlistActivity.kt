package com.tokopedia.home_wishlist

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent

class WishlistActivity : BaseSimpleActivity(), HasComponent<WishlistComponent> {
    override fun getComponent(): WishlistComponent {
        return DaggerWishlistComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    companion object{


        @JvmStatic
        fun newInstance(context: Context) = Intent(context, WishlistActivity::class.java)

    }

    /**
     * [getNewFragment] is override from [BaseSimpleActivity]
     * @return default fragment it will shown at activity
     */
    override fun getNewFragment(): Fragment {
        return WishlistFragment.newInstance()
    }

}
