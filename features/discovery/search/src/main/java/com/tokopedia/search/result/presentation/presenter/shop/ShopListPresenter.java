package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.common.Mapper;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

final class ShopListPresenter
        extends SearchSectionPresenter<ShopListSectionContract.View>
        implements ShopListSectionContract.Presenter {
    @Inject
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_FIRST_PAGE_USE_CASE)
    UseCase<SearchShopModel> searchShopFirstPageUseCase;
    @Inject
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_LOAD_MORE_USE_CASE)
    UseCase<SearchShopModel> searchShopLoadMoreUseCase;
    @Inject
    Mapper<SearchShopModel, ShopViewModel> shopViewModelMapper;
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
    public void loadData(Map<String, Object> searchParameter, int loadShopRow) {
        loadDataCheckForNulls();
        unsubscribeUseCasesBeforeLoadData();

        RequestParams requestParams = createSearchShopParam(searchParameter);
        searchShopFirstPageUseCase.execute(requestParams, getLoadDataSubscriber(searchParameter, loadShopRow));
    }

    private void loadDataCheckForNulls() {
        checkViewAttached();
        if(searchShopFirstPageUseCase == null) throw new RuntimeException("UseCase<SearchShopModel> is not injected.");
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

    private void unsubscribeUseCasesBeforeLoadData() {
        searchShopFirstPageUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }

    private Subscriber<SearchShopModel> getLoadDataSubscriber(final Map<String, Object> searchParameter, final int loadShopRow) {
        return new Subscriber<SearchShopModel>() {
            @Override
            public void onNext(SearchShopModel searchShopModel) {
                loadDataSubscriberOnNext(searchShopModel, loadShopRow);
            }

            @Override
            public void onCompleted() {
                loadDataSubscriberOnComplete(searchParameter);
            }

            @Override
            public void onError(Throwable e) {
                loadDataSubscriberOnError(e);
            }
        };
    }

    private void loadDataSubscriberOnNext(SearchShopModel searchShopModel, int loadShopRow) {
        if(isViewAttached()) {
            if (searchShopModel == null) {
                getViewToShowLoadDataFailed();
            } else {
                getViewToShowLoadDataSuccess(searchShopModel, loadShopRow);
            }
        }
    }

    private void getViewToShowLoadDataFailed() {
        getView().onSearchShopFailed();
        isSearchShopReturnedNull = true;
    }

    private void getViewToShowLoadDataSuccess(SearchShopModel searchShopModel, int loadShopRow) {
        isSearchShopReturnedNull = false;

        ShopViewModel shopViewModel = shopViewModelMapper.convert(searchShopModel);
        enrichPositionData(shopViewModel.getShopItemList(), loadShopRow);

        ShopHeaderViewModel shopHeaderViewModel = new ShopHeaderViewModel(
                shopViewModel.getCpmModel(),
                shopViewModel.getTotalShop()
        );

        List<Visitable> visitableList = createVisitableList(shopHeaderViewModel, shopViewModel.getShopItemList());

        getView().onSearchShopSuccess(visitableList, shopViewModel.getHasNextPage());
    }

    private List<Visitable> createVisitableList(
            ShopHeaderViewModel shopHeaderViewModel,
            List<ShopViewModel.ShopItem> shopItemList
    ) {

        List<Visitable> visitableList = new ArrayList<>();
        visitableList.add(shopHeaderViewModel);
        visitableList.addAll(shopItemList);

        return visitableList;
    }

    private void enrichPositionData(List<ShopViewModel.ShopItem> shopViewItemList, int startRow) {
        int position = startRow;
        for (ShopViewModel.ShopItem shopItem : shopViewItemList) {
            position++;
            shopItem.setPosition(position);
        }
    }

    private void loadDataSubscriberOnComplete(Map<String, Object> searchParameter) {
        if(isViewAttached()) {
            if (!isSearchShopReturnedNull) {
                requestDynamicFilter(searchParameter);
            }
        }
    }

    private void loadDataSubscriberOnError(Throwable e) {
        if (isViewAttached()) {
            if (e != null) {
                e.printStackTrace();
            }

            getView().onSearchShopFailed();
        }
    }

    @Override
    public void loadMoreData(Map<String, Object> searchParameter, int loadShopRow) {
        loadMoreDataCheckForNulls();
        unsubscribeUseCasesBeforeLoadMoreData();

        RequestParams requestParams = createSearchShopParam(searchParameter);
        searchShopLoadMoreUseCase.execute(requestParams, getLoadMoreDataSubscriber(loadShopRow));
    }

    private void loadMoreDataCheckForNulls() {
        checkViewAttached();
        if(searchShopLoadMoreUseCase == null) throw new RuntimeException("UseCase<SearchShopModel> is not injected.");
        if(shopViewModelMapper == null) throw new RuntimeException("ShopViewModelMapper is not injected.");
        if(getDynamicFilterUseCase == null) throw new RuntimeException("UseCase<DynamicFilterModel> is not injected.");
    }

    private void unsubscribeUseCasesBeforeLoadMoreData() {
        searchShopLoadMoreUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }

    private Subscriber<SearchShopModel> getLoadMoreDataSubscriber(final int loadShopRow) {
        return new Subscriber<SearchShopModel>() {
            @Override
            public void onNext(SearchShopModel searchShopModel) {
                loadMoreDataSubscriberOnNext(searchShopModel, loadShopRow);
            }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                loadDataSubscriberOnError(e);
            }
        };
    }

    private void loadMoreDataSubscriberOnNext(SearchShopModel searchShopModel, int loadShopRow) {
        if(isViewAttached()) {
            if (searchShopModel == null) {
                getViewToShowLoadMoreDataFailed();
            } else {
                getViewToShowLoadMoreDataSuccess(searchShopModel, loadShopRow);
            }
        }
    }

    private void getViewToShowLoadMoreDataFailed() {
        getView().onSearchShopFailed();
    }

    private void getViewToShowLoadMoreDataSuccess(SearchShopModel searchShopModel, int loadShopRow) {
        ShopViewModel shopViewModel = shopViewModelMapper.convert(searchShopModel);
        enrichPositionData(shopViewModel.getShopItemList(), loadShopRow);

        List<Visitable> visitableList = new ArrayList<>(shopViewModel.getShopItemList());

        getView().onSearchShopSuccess(visitableList, shopViewModel.getHasNextPage());
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
        super.detachView();
        if(searchShopFirstPageUseCase != null) searchShopFirstPageUseCase.unsubscribe();
        if(searchShopLoadMoreUseCase != null) searchShopLoadMoreUseCase.unsubscribe();
    }
}
