package com.tokopedia.shop.note.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.mapper.ShopNoteViewModelMapper;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopNoteListPresenter extends BaseDaggerPresenter<ShopNoteListView> {

    private final GetShopNoteListUseCase getShopNoteListUseCase;
    private final ShopNoteViewModelMapper shopNoteViewModelMapper;
    private final UserSessionInterface userSession;

    @Inject
    public ShopNoteListPresenter(GetShopNoteListUseCase getShopNoteListUseCase, ShopNoteViewModelMapper shopNoteViewModelMapper, UserSessionInterface userSession) {
        this.getShopNoteListUseCase = getShopNoteListUseCase;
        this.shopNoteViewModelMapper = shopNoteViewModelMapper;
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public void getShopNoteList(String shopId) {
        getShopNoteListUseCase.execute(GetShopNoteListUseCase.createRequestParam(shopId), new Subscriber<List<ShopNote>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<ShopNote> shopNoteList) {
                getView().renderList(shopNoteViewModelMapper.transform(shopNoteList), false);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopNoteListUseCase.unsubscribe();
    }
}