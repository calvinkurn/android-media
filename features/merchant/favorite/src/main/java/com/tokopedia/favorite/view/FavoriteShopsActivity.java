package com.tokopedia.favorite.view;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * @author okasurya on 7/26/18.
 */
public class FavoriteShopsActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return FragmentFavorite.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return FragmentFavorite.TAG;
    }
}
