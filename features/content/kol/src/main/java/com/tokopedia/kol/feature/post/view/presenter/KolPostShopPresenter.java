package com.tokopedia.kol.feature.post.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.subscriber.GetKolPostShopSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/08/18.
 */

public class KolPostShopPresenter extends BaseDaggerPresenter<KolPostShopContract.View>
        implements KolPostShopContract.Presenter {

    private final GetContentListUseCase getContentListUseCase;
    private String lastCursor;

    @Inject
    public KolPostShopPresenter(GetContentListUseCase getContentListUseCase) {
        this.getContentListUseCase = getContentListUseCase;
    }

    @Override
    public void initView(String shopId) {
        lastCursor = "";
        getKolPostShop(shopId);
    }

    @Override
    public void getKolPostShop(String shopId) {
        getView().showLoading();
        getContentListUseCase.execute(
                GetContentListUseCase.getShopParams(shopId, lastCursor),
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
        getContentListUseCase.unsubscribe();
    }
}
