package com.tokopedia.core.shopinfo.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shopinfo.interactor.ShopFavoritedCacheInteractor;
import com.tokopedia.core.shopinfo.interactor.ShopFavoritedCacheInteractorImpl;
import com.tokopedia.core.shopinfo.interactor.ShopFavoritedRetrofitInteractor;
import com.tokopedia.core.shopinfo.interactor.ShopFavoritedRetrofitInteractorImpl;
import com.tokopedia.core.shopinfo.fragment.ShopFavoritedFragment;
import com.tokopedia.core.shopinfo.listener.ShopFavoritedFragmentView;
import com.tokopedia.core.shopinfo.models.shopfavoritedmodel.ShopFavoritedResponse;
import com.tokopedia.core.util.PagingHandler;

/**
 * Created by USER on 10/5/2016.
 */
public class ShopFavoritedFragmentPresenterImpl implements ShopFavoritedFragmentPresenter {

    public static final int REQUEST_FAVORITEE_CODE = 1;
    ShopFavoritedFragmentView viewListener;
    ShopFavoritedRetrofitInteractor networkInteractor;
    ShopFavoritedCacheInteractor cacheInteractor;
    PagingHandler paging;
    String shopId;


    public ShopFavoritedFragmentPresenterImpl(ShopFavoritedFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ShopFavoritedRetrofitInteractorImpl();
        this.paging = new PagingHandler();
        this.cacheInteractor = new ShopFavoritedCacheInteractorImpl();
    }

    private void showLoading() {
        if (viewListener.isEmpty()) {
            viewListener.setLoading();
        } else if(paging.getPage() == 1){
            viewListener.showRefreshing();
        }else{
            viewListener.setLoading();
        }
    }

    private TKPDMapParam<String, String> paramGetPeopleFavorite() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("shop_id", shopId);
        params.put("page", Integer.toString(paging.getPage()));
        params.put("per_page","10");
        return params;
    }

    public void getShopFavoritee() {
        showLoading();
        viewListener.setActionsEnabled(false);
        viewListener.removeError();
        networkInteractor.getFavoritees(viewListener.getActivity(), paramGetPeopleFavorite(), new ShopFavoritedRetrofitInteractor.ShopFavoritedListener() {
            @Override
            public void onSuccess(@NonNull ShopFavoritedResponse data) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);

                if (paging.getPage() == 1) {
                    viewListener.getAdapter().getList().clear();
                    cacheInteractor.setShopFavoritedCache(data);
                }
                paging.setHasNext(data.getPage()<data.getTotalPage());
                showData(data);

            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                if (viewListener.isEmpty()) {
                    viewListener.showEmptyState();
                } else {
                    viewListener.setRetry();
                }
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);

                if (viewListener.isEmpty()) {
                    viewListener.showEmptyState(error);
                } else {
                    viewListener.setRetry(error);
                }
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.getAdapter().showEmpty(true);
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                if (viewListener.isEmpty()) {
                    viewListener.showEmptyState();
                } else {
                    viewListener.setRetry();
                }
            }

        });
    }

    public void showData(ShopFavoritedResponse data) {
        viewListener.getAdapter().addList(data.getList());
        if (viewListener.isEmpty()) {
            viewListener.getAdapter().showEmpty(true);
        }
        if (paging.CheckNextPage()) {
            viewListener.setLoading();
        }else{
            viewListener.finishLoading();
        }
    }


    @Override
    public void onRefresh() {
        paging.resetPage();
        getShopFavoritee();
    }


    @Override
    public void setCache(String shopId) {
        if (this.shopId==null)
            this.shopId=shopId;
        cacheInteractor.getShopFavoritedCache(new ShopFavoritedCacheInteractor.GetShopFavoritedCacheListener() {
            @Override
            public void onSuccess(ShopFavoritedResponse cache) {
                showData(cache);
                getShopFavoritee();

            }

            @Override
            public void onError(Throwable e) {
                getShopFavoritee();
            }
        });
    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem) {
       if (paging.CheckNextPage()) {
            paging.nextPage();
            getShopFavoritee();
        }
    }

    @Override
    public RetryDataBinder.OnRetryListener onRetry() {
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                getShopFavoritee();
            }
        };
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }
}
