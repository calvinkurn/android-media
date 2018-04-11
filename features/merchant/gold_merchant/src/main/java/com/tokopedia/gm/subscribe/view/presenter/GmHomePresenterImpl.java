package com.tokopedia.gm.subscribe.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.gm.subscribe.domain.product.interactor.ClearGmSubscribeProductCacheUseCase;
import com.tokopedia.gm.subscribe.view.fragment.GmHomeView;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 2/9/17.
 */
public class GmHomePresenterImpl extends BaseDaggerPresenter<GmHomeView> implements GmHomePresenter {
    private static final String TAG = "GMHomePresenter";
    private final ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache;

    @Inject
    public GmHomePresenterImpl(ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache) {
        this.clearGMSubscribeProductCache = clearGMSubscribeProductCache;
    }

    @Override
    public void clearGMProductCache() {
        checkViewAttached();
        getView().showProgressDialog();
        clearGMSubscribeProductCache.execute(RequestParams.EMPTY, new ClearGMSubscribeProductCacheSubscriber());
    }

    private class ClearGMSubscribeProductCacheSubscriber extends rx.Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            getView().dismissProgressDialog();
        }
    }
}
