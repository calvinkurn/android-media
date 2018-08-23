package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopPresenter implements KolPostShopContract.Presenter {

    private String lastCursor;

    @Override
    public void initView(String userId) {
        lastCursor = "";
        getKolPostShop(userId);
    }

    @Override
    public void getKolPostShop(String userId) {

    }

    @Override
    public void updateCursor(String lastCursor) {

    }

    @Override
    public void attachView(KolPostShopContract.View view) {

    }

    @Override
    public void detachView() {

    }
}
