package com.tokopedia.core.shopinfo.presenter;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.interactor.ShopProductCacheInteractor;
import com.tokopedia.core.shopinfo.interactor.ShopProductCacheInteractorImpl;
import com.tokopedia.core.shopinfo.listener.OsHomeFragmentView;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by brilliant.oka on 26/04/17.
 */

public class OsHomePresenterImpl implements OsHomePresenter {
    OsHomeFragmentView viewListener;
    ShopProductCacheInteractor cacheInteractor;

    public OsHomePresenterImpl(OsHomeFragmentView viewListener) {
        this.viewListener = viewListener;
        cacheInteractor = new ShopProductCacheInteractorImpl();
    }

    @Override
    public void onRefresh() {
        cacheInteractor.getLimitedShopProductCache(10, new ShopProductCacheInteractor.GetShopProductCacheListener() {
            @Override
            public void onSuccess(ProductModel model) {
                viewListener.onGetProduct(model);
            }

            @Override
            public void onError(Throwable e) {
                viewListener.showError(e.getMessage(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {

                    }
                });
            }
        });
    }

    @Override
    public RetryDataBinder.OnRetryListener onRetry() {
        return null;
    }

    @Override
    public void onDestroyView() {
    }
}
