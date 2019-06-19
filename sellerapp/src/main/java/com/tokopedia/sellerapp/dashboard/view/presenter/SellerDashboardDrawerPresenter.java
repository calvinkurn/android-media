package com.tokopedia.sellerapp.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.flashsale.management.common.domain.interactor.FlashsaleGetSellerStatusUseCase;
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel;
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

import com.tokopedia.sellerapp.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by hendry on 21/11/18.
 */
public class SellerDashboardDrawerPresenter {

    private FlashsaleGetSellerStatusUseCase flashsaleGetSellerStatusUseCase;
    private GetShopStatusUseCase getShopStatusUseCase;
    private UserSessionInterface userSession;

    private SellerDashboardView listener;
    public interface SellerDashboardView {
        void onSuccessGetFlashsaleSellerStatus(Boolean isVisible);
        void onSuccessGetShopInfo(ShopStatusModel shopStatusModel);
        Context getContext();
    }

    public void attachView(SellerDashboardView sellerDashboardView){
        this.listener = sellerDashboardView;
    }

    public SellerDashboardDrawerPresenter(GetShopStatusUseCase getShopStatusUseCase,
                                          UserSessionInterface userSession){
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        flashsaleGetSellerStatusUseCase = new FlashsaleGetSellerStatusUseCase(graphqlUseCase);
        flashsaleGetSellerStatusUseCase.setCached(true);

        this.getShopStatusUseCase = getShopStatusUseCase;
        this.userSession = userSession;

    }

    public void getFlashsaleSellerStatus() {
        String rawQuery = GraphqlHelper.loadRawString(listener.getContext().getResources(), R.raw.gql_get_seller_status);
        RequestParams params = FlashsaleGetSellerStatusUseCase.createRequestParams(rawQuery, userSession.getShopId());
        flashsaleGetSellerStatusUseCase.execute(params, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(); }

            @Override
            public void onNext(Boolean isVisible) {
                listener.onSuccessGetFlashsaleSellerStatus(isVisible);
            }
        });
    }

    public void isGoldMerchantAsync() {
        getShopStatusUseCase.execute(GetShopStatusUseCase.Companion.createRequestParams(userSession.getShopId()),
                new Subscriber<ShopStatusModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopStatusModel shopStatusModel) {
                listener.onSuccessGetShopInfo(shopStatusModel);
            }
        });
    }

    public void unsubscribe(){
        flashsaleGetSellerStatusUseCase.unsubscribe();
        getShopStatusUseCase.unsubscribe();
        this.listener = null;
    }
}
