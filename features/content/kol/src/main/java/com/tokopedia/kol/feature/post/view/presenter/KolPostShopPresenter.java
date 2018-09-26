package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.usecase.GetKolPostShopUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.subscriber.GetKolPostShopSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopPresenter extends BaseDaggerPresenter<KolPostShopContract.View>
        implements KolPostShopContract.Presenter {

    private final GetKolPostShopUseCase getKolPostShopUseCase;
    private String lastCursor;

    @Inject
    public KolPostShopPresenter(GetKolPostShopUseCase getKolPostShopUseCase) {
        this.getKolPostShopUseCase = getKolPostShopUseCase;
    }

    @Override
    public void initView(String shopId) {
        lastCursor = "";
        getKolPostShop(shopId);
    }

    @Override
    public void getKolPostShop(String shopId) {
        getView().showLoading();
        getKolPostShopUseCase.execute(
                GetKolPostShopUseCase.getParams(shopId, lastCursor),
                new GetKolPostShopSubscriber(getView())
        );
    }

    @Override
    public void updateCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolPostShopUseCase.unsubscribe();
    }
}
