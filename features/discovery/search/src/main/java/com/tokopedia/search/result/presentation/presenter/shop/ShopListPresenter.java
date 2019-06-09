package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.search.result.presentation.presenter.subscriber.RequestDynamicFilterSubscriber;
import com.tokopedia.search.result.presentation.presenter.subscriber.SearchShopSubscriber;
import com.tokopedia.search.result.presentation.presenter.subscriber.ToggleFavoriteActionSubscriber;
import com.tokopedia.search.result.presentation.view.listener.FavoriteActionListener;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SearchShopListener;
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
    @Inject
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_USE_CASE)
    UseCase<SearchShopModel> searchShopUseCase;
    @Inject
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> getDynamicFilterUseCase;
    @Inject
    @Named(SearchConstant.SearchShop.TOGGLE_FAVORITE_SHOP_USE_CASE)
    UseCase<Boolean> toggleFavouriteShopUseCase;
    @Inject
    UserSessionInterface userSession;

    private SearchShopListener searchShopListener;
    private FavoriteActionListener favoriteActionListener;

    @Override
    public void initInjector(ShopListSectionContract.View view) {
        ShopListPresenterComponent shopListPresenterComponent = DaggerShopListPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        shopListPresenterComponent.inject(this);
    }

    @Override
    public void setSearchShopListener(SearchShopListener searchShopListener) {
        this.searchShopListener = searchShopListener;
    }

    @Override
    public void setFavoriteActionListener(FavoriteActionListener favoriteActionListener) {
        this.favoriteActionListener = favoriteActionListener;
    }

    @Override
    public void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition) {
        if (getView().isUserHasLogin()) {
            getView().disableFavoriteButton(adapterPosition);

            getView().logDebug(this.toString(),
                    "Toggle favorite " + shopItem.getShopId() + " " + Boolean.toString(!shopItem.isFavorited()));

            RequestParams requestParams = createToggleFavoriteShopRequestParam(shopItem.getShopId());

            toggleFavouriteShopUseCase.execute(requestParams,
                    new ToggleFavoriteActionSubscriber(favoriteActionListener, adapterPosition, !shopItem.isFavorited())
            );
        } else {
            getView().launchLoginActivity(shopItem.getShopId());
        }
    }

    private RequestParams createToggleFavoriteShopRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SearchConstant.SearchShop.TOGGLE_FAVORITE_SHOP_ID, shopId);
        return requestParams;
    }

    @Override
    public void loadShop(Map<String, Object> searchParameter) {
        loadShopCheckForNulls();

        RequestParams requestParams = createSearchShopParam(searchParameter);

        searchShopUseCase.unsubscribe();

        searchShopUseCase.execute(requestParams, new SearchShopSubscriber(searchShopListener));
    }

    private void loadShopCheckForNulls() {
        if(searchShopListener == null) throw new RuntimeException("SearchShopListener is not set.");
        if(searchShopUseCase == null) throw new RuntimeException("UseCase<SearchShopModel> is not injected.");
    }

    private RequestParams createSearchShopParam(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();
        putRequestParamsOtherParameters(requestParams);
        requestParams.putAll(searchParameter);

        return requestParams;
    }

    private void putRequestParamsOtherParameters(RequestParams requestParams) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.OB, requestParams.getString(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT));
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameter) {
        requestDynamicFilterCheckForNulls();

        RequestParams requestParams = createRequestDynamicFilterParams(searchParameter);

        getDynamicFilterUseCase.execute(requestParams, new RequestDynamicFilterSubscriber(requestDynamicFilterListener));
    }

    private void requestDynamicFilterCheckForNulls() {
        checkViewAttached();
        if(requestDynamicFilterListener == null) throw new RuntimeException("RequestDynamicFilterListener is not set.");
        if(getDynamicFilterUseCase == null) throw new RuntimeException("UseCase<DynamicFilterModel> is not injected.");
        if(userSession == null) throw new RuntimeException("UserSession is not injected.");
    }

    private RequestParams createRequestDynamicFilterParams(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(searchParameter);
        requestParams.putAllString(generateParamsNetwork(requestParams));
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SHOP);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

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

    @Override
    public void detachView() {
        searchShopUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }
}
