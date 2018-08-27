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

    private static final String PARAM_SHOP_ID = "shop_id";

    private String shopId;

    public static KolPostShopFragment newInstance(String shopId) {
        KolPostShopFragment fragment = new KolPostShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    KolPostShopContract.Presenter presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar();
        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initVar() {
        shopId = getArguments().getString(PARAM_SHOP_ID);
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
