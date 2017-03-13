package com.tokopedia.sellerapp.home.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreMainDataUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.sellerapp.home.view.SellerHomeView;
import com.tokopedia.sellerapp.home.view.mapper.ShopScoreMapper;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 2/24/17.
 */

public class SellerHomePresenterImpl
        extends BaseDaggerPresenter<SellerHomeView>
        implements SellerHomePresenter {

    private final GetShopScoreMainDataUseCase getShopScoreMainData;

    public SellerHomePresenterImpl(GetShopScoreMainDataUseCase getShopScoreMainData) {
        this.getShopScoreMainData = getShopScoreMainData;
    }

    @Override
    public void getShopScoreMainData() {
        getShopScoreMainData.execute(
                RequestParams.EMPTY,
                new GetShopScoreMainDataSubscriber()
        );
    }

    private class GetShopScoreMainDataSubscriber extends Subscriber<ShopScoreMainDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().onErrorShopScore();
        }

        @Override
        public void onNext(ShopScoreMainDomainModel shopScoreMainDomainModel) {
            ShopScoreViewModel shopScoreViewModel = ShopScoreMapper.map(shopScoreMainDomainModel);
            getView().renderShopScore(shopScoreViewModel);
        }
    }
}
