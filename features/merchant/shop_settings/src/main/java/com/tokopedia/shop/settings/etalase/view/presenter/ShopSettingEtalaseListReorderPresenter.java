package com.tokopedia.shop.settings.etalase.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopEtalaseUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopSettingEtalaseListReorderPresenter extends BaseDaggerPresenter<ShopSettingEtalaseListReorderPresenter.View> {
    private ReorderShopEtalaseUseCase reorderShopEtalaseUseCase;

    public interface View extends CustomerView {
        void onSuccessReorderShopEtalase(String successMessage);
        void onErrorReorderShopEtalase(Throwable throwable);
    }

    @Inject
    public ShopSettingEtalaseListReorderPresenter(ReorderShopEtalaseUseCase reorderShopEtalaseUseCase) {
        this.reorderShopEtalaseUseCase = reorderShopEtalaseUseCase;
    }

    public void reorderShopNotes(ArrayList<String> etalaseIdList){
        reorderShopEtalaseUseCase.unsubscribe();
        reorderShopEtalaseUseCase.execute(ReorderShopEtalaseUseCase.createRequestParams(etalaseIdList),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorReorderShopEtalase(e);
                        }
                    }

                    @Override
                    public void onNext(String successMessage) {
                        if (isViewAttached()) {
                            getView().onSuccessReorderShopEtalase(successMessage);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        reorderShopEtalaseUseCase.unsubscribe();
    }
}
