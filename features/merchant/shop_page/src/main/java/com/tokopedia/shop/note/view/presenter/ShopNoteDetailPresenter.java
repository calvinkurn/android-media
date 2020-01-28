package com.tokopedia.shop.note.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteDetailUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailPresenter extends BaseDaggerPresenter<ShopNoteDetailView> {

    private final GetShopNoteDetailUseCase getShopNoteDetailUseCase;

    @Inject
    public ShopNoteDetailPresenter(GetShopNoteDetailUseCase getShopNoteDetailUseCase) {
        this.getShopNoteDetailUseCase = getShopNoteDetailUseCase;
    }

    public void getShopNoteList(String shopId, String noteId) {
        getShopNoteDetailUseCase.execute(GetShopNoteDetailUseCase.createRequestParams(
                shopId,
                noteId
        ), new Subscriber<ShopNoteModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopNoteList(e);
                }
            }

            @Override
            public void onNext(ShopNoteModel shopNoteModel) {
                getView().onSuccessGetShopNoteList(shopNoteModel);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopNoteDetailUseCase.unsubscribe();
    }
}