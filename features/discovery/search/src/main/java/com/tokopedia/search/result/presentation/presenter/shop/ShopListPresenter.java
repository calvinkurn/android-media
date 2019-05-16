package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.search.result.presentation.presenter.subscriber.RequestDynamicFilterSubscriber;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.ShopListLoadMoreListener;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

final class ShopListPresenter
        extends SearchSectionPresenter<ShopListSectionContract.View>
        implements ShopListSectionContract.Presenter {

//    @Inject
//    GetShopUseCase getShopUseCase;
    @Inject
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> getDynamicFilterUseCase;
//    @Inject
//    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
    @Inject
    UserSessionInterface userSession;

    private FavoriteActionListener favoriteActionListener;
    private RequestDynamicFilterListener requestDynamicFilterListener;

    @Override
    public void initInjector(ShopListSectionContract.View view) {
        ShopListPresenterComponent shopListPresenterComponent = DaggerShopListPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        shopListPresenterComponent.inject(this);
    }

    @Override
    public void setFavoriteActionListener(FavoriteActionListener favoriteActionListener) {
        this.favoriteActionListener = favoriteActionListener;
    }

    @Override
    public void setRequestDynamicFilterListener(RequestDynamicFilterListener requestDynamicFilterListener) {
        this.requestDynamicFilterListener = requestDynamicFilterListener;
    }

    @Override
    public void handleFavoriteButtonClicked(ShopViewModel.ShopItem shopItem, int adapterPosition) {
    }

    @Override
    public void loadShop(Map<String, Object> searchParameter, ShopListLoadMoreListener loadMoreListener) {

    }

    @Override
    public void requestDynamicFilter() {
        requestDynamicFilterCheckForNulls();

        RequestParams requestParams = createRequestDynamicFilterParams();

        getDynamicFilterUseCase.execute(requestParams, new RequestDynamicFilterSubscriber(requestDynamicFilterListener));
    }

    private void requestDynamicFilterCheckForNulls() {
        checkViewAttached();
        if(requestDynamicFilterListener == null) throw new RuntimeException("RequestDynamicFilterListener is not set.");
        if(getDynamicFilterUseCase == null) throw new RuntimeException("UseCase<DynamicFilterModel> is not injected.");
        if(userSession == null) throw new RuntimeException("UserSession is not injected.");
    }

    private RequestParams createRequestDynamicFilterParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(generateParamsNetwork(requestParams));
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SHOP);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.Q, getView().getQueryKey());
        requestParams.putString(SearchApiConst.SC, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SC);

        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);

        return requestParams;
    }

    private Map<String, String> generateParamsNetwork(RequestParams requestParams) {
        return new HashMap<>(
                AuthUtil.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        requestParams.getParamsAllValueInString())
        );
    }
}
