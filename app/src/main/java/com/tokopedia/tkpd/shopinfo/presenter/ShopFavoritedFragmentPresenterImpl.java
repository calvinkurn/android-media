package com.tokopedia.tkpd.shopinfo.presenter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.tokopedia.tkpd.customadapter.RetryDataBinder;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.shopinfo.interactor.ShopFavoritedCacheInteractor;
import com.tokopedia.tkpd.shopinfo.interactor.ShopFavoritedCacheInteractorImpl;
import com.tokopedia.tkpd.shopinfo.interactor.ShopFavoritedRetrofitInteractor;
import com.tokopedia.tkpd.shopinfo.interactor.ShopFavoritedRetrofitInteractorImpl;
import com.tokopedia.tkpd.shopinfo.fragment.ShopFavoritedFragment;
import com.tokopedia.tkpd.shopinfo.listener.ShopFavoritedFragmentView;
import com.tokopedia.tkpd.shopinfo.models.shopfavoritedmodel.ShopFavoritedResponse;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.Map;

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
