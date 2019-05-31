package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapper;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

final class ShopListPresenter
        extends SearchSectionPresenter<ShopListSectionContract.View>
        implements ShopListSectionContract.Presenter {
    @Inject
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_USE_CASE)
    UseCase<SearchShopModel> searchShopUseCase;
    @Inject
    ShopViewModelMapper shopViewModelMapper;
    @Inject
    @Named(SearchConstant.SearchShop.TOGGLE_FAVORITE_SHOP_USE_CASE)
    UseCase<Boolean> toggleFavouriteShopUseCase;
    @Inject
    UserSessionInterface userSession;

    private boolean isSearchShopReturnedNull = false;

    @Override
    public void initInjector(ShopListSectionContract.View view) {
        ShopListPresenterComponent shopListPresenterComponent = DaggerShopListPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        shopListPresenterComponent.inject(this);
    }

    @Override
    public void handleFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition) {
        if (getView().isUserHasLogin()) {
            getView().disableFavoriteButton(adapterPosition);

            getView().logDebug(this.toString(),
                    "Toggle favorite " + shopItem.getShopId() + " " + Boolean.toString(!shopItem.isFavorited()));

            RequestParams requestParams = createToggleFavoriteShopRequestParam(shopItem.getShopId());

            toggleFavouriteShopUseCase.execute(
                    requestParams,
                    getToggleFavoriteShopSubscriber(adapterPosition, !shopItem.isFavorited())
            );
        } else {
            getView().launchLoginActivity(shopItem.getShopId());
        }
    }

    private Subscriber<Boolean> getToggleFavoriteShopSubscriber(final int adapterPosition, final boolean targetFavoritedStatus) {
        return new Subscriber<Boolean>() {
            @Override
            public void onNext(Boolean isSuccess) {
                toggleFavoriteActionSubscriberOnNext(isSuccess, adapterPosition, targetFavoritedStatus);
            }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                toggleFavoriteActionSubscriberOnError(e, adapterPosition);
            }
        };
    }

    private void toggleFavoriteActionSubscriberOnNext(boolean isSuccess, int adapterPosition, boolean targetFavoritedStatus) {
        if (isSuccess)
            getView().onSuccessToggleFavorite(adapterPosition, targetFavoritedStatus);
        else
            getView().onErrorToggleFavorite(adapterPosition);
    }

    private void toggleFavoriteActionSubscriberOnError(Throwable e, int adapterPosition) {
        getView().onErrorToggleFavorite(e, adapterPosition);
    }

    private RequestParams createToggleFavoriteShopRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SearchConstant.SearchShop.TOGGLE_FAVORITE_SHOP_ID, shopId);
        return requestParams;
    }

    @Override
    public void loadShop(Map<String, Object> searchParameter) {
        loadShopCheckForNulls();
        unsubscribeUseCases();

        RequestParams requestParams = createSearchShopParam(searchParameter);
        searchShopUseCase.execute(requestParams, getSearchShopSubscriber(searchParameter));
    }

    private void loadShopCheckForNulls() {
        checkViewAttached();
        if(searchShopUseCase == null) throw new RuntimeException("UseCase<SearchShopModel> is not injected.");
        if(shopViewModelMapper == null) throw new RuntimeException("ShopViewModelMapper is not injected.");
        if(getDynamicFilterUseCase == null) throw new RuntimeException("UseCase<DynamicFilterModel> is not injected.");
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

    private void unsubscribeUseCases() {
        searchShopUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }

    private Subscriber<SearchShopModel> getSearchShopSubscriber(final Map<String, Object> searchParameter) {
        return new Subscriber<SearchShopModel>() {
            @Override
            public void onNext(SearchShopModel searchShopModel) {
                searchShopSubscriberOnNext(searchShopModel);
            }

            @Override
            public void onCompleted() {
                searchShopSubscriberOnCompleted(searchParameter);
            }

            @Override
            public void onError(Throwable e) {
                searchShopSubscriberOnError(e);
            }
        };
    }

    private void searchShopSubscriberOnNext(SearchShopModel searchShopModel) {
        if(searchShopModel == null) {
            getView().onSearchShopFailed();
            isSearchShopReturnedNull = true;
            return;
        }

        isSearchShopReturnedNull = false;
        ShopViewModel shopViewModel = shopViewModelMapper.convertToShopViewModel(searchShopModel);
        getView().onSearchShopSuccess(shopViewModel.getShopItemList(), shopViewModel.isHasNextPage());
    }

    private void searchShopSubscriberOnCompleted(Map<String, Object> searchParameter) {
        if (!isSearchShopReturnedNull) {
            requestDynamicFilter(searchParameter);
        }
    }

    private void searchShopSubscriberOnError(Throwable e) {
        if(e != null) {
            e.printStackTrace();
        }

        getView().onSearchShopFailed();
    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameter) {
        requestDynamicFilterCheckForNulls();

        RequestParams requestParams = createRequestDynamicFilterParams(searchParameter);
        getDynamicFilterUseCase.execute(requestParams, getDynamicFilterSubscriber(true));
    }

    private void requestDynamicFilterCheckForNulls() {
        checkViewAttached();
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
