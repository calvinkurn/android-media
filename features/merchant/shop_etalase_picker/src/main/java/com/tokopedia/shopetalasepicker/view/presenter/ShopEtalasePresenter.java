package com.tokopedia.shopetalasepicker.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase;
import com.tokopedia.shopetalasepicker.view.listener.ShopEtalaseView;
import com.tokopedia.shopetalasepicker.view.model.ShopEtalaseViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalasePresenter extends BaseDaggerPresenter<ShopEtalaseView> {

    private final GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase;
    private final UserSession userSession;

    @Inject
    public ShopEtalasePresenter(GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase, UserSession userSession) {
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
                    getView().renderList(map(shopEtalaseModels), false);
                }
            }

            private List<ShopEtalaseViewModel> map(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                if (shopEtalaseModels.size() == 0) {
                    return new ArrayList<>();
                }
                ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
                // loop to convert to view model, only get until limit.
                for (ShopEtalaseModel etalaseModel : shopEtalaseModels) {
                    shopEtalaseViewModels.add(new ShopEtalaseViewModel(etalaseModel));
                }
                return shopEtalaseViewModels;
            }
        });
    }

    public void clearEtalaseCache(){
        getShopEtalaseByShopUseCase.clearCache();
    }

}
