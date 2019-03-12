package com.tokopedia.shop.etalase.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase;
import com.tokopedia.shop.etalase.view.listener.ShopEtalaseView;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalasePresenter extends BaseDaggerPresenter<ShopEtalaseView> {

    private final GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase;
    private final UserSessionInterface userSession;

    @Inject
    public ShopEtalasePresenter(GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase, UserSessionInterface userSession) {
        this.getShopEtalaseByShopUseCase = getShopEtalaseByShopUseCase;
        this.userSession = userSession;
    }

    public void getShopEtalase(String shopId) {
        if (TextUtils.isEmpty(shopId)) {
            return;
        }
        RequestParams params = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId, true, false, shopId.equalsIgnoreCase(userSession.getShopId()));
        getShopEtalaseByShopUseCase.execute(params, new Subscriber<ArrayList<ShopEtalaseModel>>() {
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
            public void onNext(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                if (isViewAttached()) {
                    ArrayList<ShopEtalaseViewModel> shopEtalaseViewModelList = ShopProductMapper.map(shopEtalaseModels);
                    getView().renderList(shopEtalaseViewModelList, false);
                }
            }
        });
    }

    public void clearEtalaseCache(){
        getShopEtalaseByShopUseCase.clearCache();
    }

}
