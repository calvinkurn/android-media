package com.tokopedia.favorite.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * @author okasurya on 7/26/18.
 */
class FavoriteShopsActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return FragmentFavoriteUsingViewModel.newInstance()
//        return FragmentFavorite.newInstance();
    }

    override fun getTagFragment(): String {
        return FragmentFavorite.TAG
    }
}
