package com.tokopedia.shop.settings.notes.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopNoteUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopSettingNoteListReorderPresenter extends BaseDaggerPresenter<ShopSettingNoteListReorderPresenter.View> {
    private ReorderShopNoteUseCase reorderShopNoteUseCase;

    public interface View extends CustomerView {
        void onSuccessReorderShopNote(String successMessage);
        void onErrorReorderShopNote(Throwable throwable);
    }

    @Inject
    public ShopSettingNoteListReorderPresenter(ReorderShopNoteUseCase reorderShopNoteUseCase) {
        this.reorderShopNoteUseCase = reorderShopNoteUseCase;
    }

    public void reorderShopNotes(ArrayList<String> noteIdList){
        reorderShopNoteUseCase.unsubscribe();
        reorderShopNoteUseCase.execute(ReorderShopNoteUseCase.createRequestParams(noteIdList),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorReorderShopNote(e);
                        }
                    }

                    @Override
                    public void onNext(String successMessage) {
                        if (isViewAttached()) {
                            getView().onSuccessReorderShopNote(successMessage);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        reorderShopNoteUseCase.unsubscribe();
    }
}
