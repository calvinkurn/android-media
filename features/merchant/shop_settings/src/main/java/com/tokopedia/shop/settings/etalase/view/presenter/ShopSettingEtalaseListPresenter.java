package com.tokopedia.shop.settings.etalase.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.DeleteShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.DeleteShopNoteUseCase;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopSettingEtalaseListPresenter extends BaseDaggerPresenter<ShopSettingEtalaseListPresenter.View> {
    private GetShopEtalaseUseCase getShopEtalaseUseCase;
    private DeleteShopEtalaseUseCase deleteShopEtalaseUseCase;
    public static final int PRIMARY_ETALASE_LIMIT = 5;

    public interface View extends CustomerView {
        void onSuccessGetShopEtalase(ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels);
        void onErrorGetShopEtalase(Throwable throwable);
        void onSuccessDeleteShopEtalase(String successMessage);
        void onErrorDeleteShopEtalase(Throwable throwable);
    }

    @Inject
    public ShopSettingEtalaseListPresenter(GetShopEtalaseUseCase getShopEtalaseUseCase,
                                           DeleteShopEtalaseUseCase deleteShopEtalaseUseCase) {
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
        this.deleteShopEtalaseUseCase = deleteShopEtalaseUseCase;
    }

    public void getShopEtalase(){
        getShopEtalaseUseCase.unsubscribe();
        getShopEtalaseUseCase.execute(GetShopEtalaseUseCase.createRequestParams(true),
                new Subscriber<ArrayList<ShopEtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopEtalase(e);
                }
            }

            @Override
            public void onNext(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                if (isViewAttached()) {
                    ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
                    int countPrimaryEtalase = 0;
                    for (ShopEtalaseModel shopEtalaseModel: shopEtalaseModels) {
                        shopEtalaseViewModels.add(new ShopEtalaseViewModel (shopEtalaseModel,
                                countPrimaryEtalase < PRIMARY_ETALASE_LIMIT));
                        countPrimaryEtalase++;
                    }
                    getView().onSuccessGetShopEtalase(shopEtalaseViewModels);
                }
            }
        });
    }

    public void deleteShopEtalase(String etalaseId){
        deleteShopEtalaseUseCase.unsubscribe();
        deleteShopEtalaseUseCase.execute(DeleteShopEtalaseUseCase.createRequestParams(etalaseId),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorDeleteShopEtalase(e);
                        }
                    }

                    @Override
                    public void onNext(String successMessage) {
                        if (isViewAttached()) {
                            getView().onSuccessDeleteShopEtalase(successMessage);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopEtalaseUseCase.unsubscribe();
        deleteShopEtalaseUseCase.unsubscribe();
    }
}
