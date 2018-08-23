package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.kol.feature.post.domain.usecase.GetKolPostShopUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopPresenter implements KolPostShopContract.Presenter {

    private final GetKolPostShopUseCase getKolPostShopUseCase;
    private String lastCursor;


    @Inject
    public KolPostShopPresenter(GetKolPostShopUseCase getKolPostShopUseCase) {
        this.getKolPostShopUseCase = getKolPostShopUseCase;
    }

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
