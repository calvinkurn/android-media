package com.tokopedia.kol.feature.post.view.fragment;

import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopFragment extends KolPostFragment implements KolPostShopContract.View,
        KolPostShopContract.View.Like {

    private String shopId;

    @Inject
    KolPostShopContract.Presenter presenter;

    @Override
    protected void fetchDataFirstTime() {
        presenter.initView(shopId);
    }

    @Override
    protected void fetchData() {
        presenter.getKolPostShop(shopId);
    }
}
