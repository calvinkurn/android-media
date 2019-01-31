package com.tokopedia.sellerapp.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.flashsale.management.common.domain.interactor.FlashsaleGetSellerStatusUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

import com.tokopedia.sellerapp.R;

/**
 * Created by hendry on 21/11/18.
 */
public class SellerDashboardDrawerPresenter {

    private FlashsaleGetSellerStatusUseCase flashsaleGetSellerStatusUseCase;
    private GetShopInfoUseCase getShopInfoUseCase;

    private SellerDashboardView listener;
    public interface SellerDashboardView {
        void onSuccessGetFlashsaleSellerStatus(Boolean isVisible);
        void onSuccessGetShopInfo(ShopModel shopModel);
        Context getContext();
    }

    public SellerDashboardDrawerPresenter(SellerDashboardView sellerDashboardView,
                                          GetShopInfoUseCase getShopInfoUseCase){
        this.listener = sellerDashboardView;

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        flashsaleGetSellerStatusUseCase = new FlashsaleGetSellerStatusUseCase(graphqlUseCase);
        flashsaleGetSellerStatusUseCase.setCached(true);

        this.getShopInfoUseCase = getShopInfoUseCase;

    }

    public void getFlashsaleSellerStatus( String shopId) {
        /*String rawQuery = GraphqlHelper.loadRawString(listener.getContext().getResources(), R.raw.gql_get_seller_status);
        RequestParams params = FlashsaleGetSellerStatusUseCase.createRequestParams(rawQuery, shopId);
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
        });*/
    }

    public void isGoldMerchantAsync() {
        getShopInfoUseCase.execute(com.tokopedia.core.base.domain.RequestParams.EMPTY, new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopModel shopModel) {
                listener.onSuccessGetShopInfo(shopModel);
            }
        });
    }

    public void unsubscribe(){
        flashsaleGetSellerStatusUseCase.unsubscribe();
        getShopInfoUseCase.unsubscribe();
    }
}
