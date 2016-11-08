package com.tokopedia.core.peoplefave.presenter;

import android.content.Context;

import com.tokopedia.core.peoplefave.listener.PeopleFavoritedShopView;

/**
 * Created by hangnadi on 10/11/16.
 */
public class PeopleFavoritedShopImpl implements PeopleFavoritedShopPresenter {

    private final PeopleFavoritedShopView viewListener;

    public PeopleFavoritedShopImpl(PeopleFavoritedShopView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void setOnInitView(Context context) {
        viewListener.inflateFragment();
    }
}
