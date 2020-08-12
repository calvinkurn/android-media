package com.tokopedia.shop.score.view.presenter;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.score.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop.score.view.fragment.ShopScoreDetailView;
import com.tokopedia.shop.score.view.mapper.ShopScoreDetailItemsViewModelMapper;
import com.tokopedia.shop.score.view.mapper.ShopScoreDetailStateMapper;
import com.tokopedia.shop.score.view.mapper.ShopScoreDetailSummaryViewModelMapper;
import com.tokopedia.shop.score.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.shop.score.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.shop.score.view.model.ShopScoreDetailSummaryViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailPresenterImpl extends BaseDaggerPresenter<ShopScoreDetailView> implements ShopScoreDetailPresenter {
    private final GetShopScoreDetailUseCase getShopScoreDetailUseCase;

    public ShopScoreDetailPresenterImpl(GetShopScoreDetailUseCase getShopScoreDetailUseCase) {
        this.getShopScoreDetailUseCase = getShopScoreDetailUseCase;
    }

    @Override
    public void getShopScoreDetail() {
        getView().showLoading();
        getShopScoreDetailUseCase.execute(
                RequestParams.EMPTY,
                new GetShopScoreDetailSubscriber()
        );
    }

    public void unsubscribe() {
        getShopScoreDetailUseCase.unsubscribe();
    }

    private class GetShopScoreDetailSubscriber extends Subscriber<ShopScoreDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissLoading();
            getView().emptyState();
        }

        @Override
        public void onNext(ShopScoreDetailDomainModel domainModels) {
            getView().dismissLoading();
            renderItemsDetail(domainModels);
            renderSummary(domainModels);
            renderState(domainModels);
        }
    }

    private void renderState(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailStateEnum shopScoreDetailStateEnum = ShopScoreDetailStateMapper.map(domainModels);
        getView().renderShopScoreState(shopScoreDetailStateEnum);
    }

    private void renderSummary(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailSummaryViewModel viewModel = ShopScoreDetailSummaryViewModelMapper.map(domainModels);
        getView().renderShopScoreSummary(viewModel);
    }

    private void renderItemsDetail(ShopScoreDetailDomainModel domainModels) {
        List<ShopScoreDetailItemViewModel> viewModel = ShopScoreDetailItemsViewModelMapper.map(domainModels);
        getView().renderShopScoreDetail(viewModel);
    }
}
