package com.tokopedia.kol.feature.post.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void fetchDataFirstTime() {
        presenter.initView(shopId);
    }

    @Override
    protected void fetchData() {
        presenter.getKolPostShop(shopId);
    }
}
