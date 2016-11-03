package com.tokopedia.tkpd.manage.people.address.presenter;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.tokopedia.tkpd.customadapter.RetryDataBinder;
import com.tokopedia.tkpd.manage.people.address.fragment.ChooseAddressFragment;
import com.tokopedia.tkpd.manage.people.address.interactor.ChooseAddressCacheInteractor;
import com.tokopedia.tkpd.manage.people.address.interactor.ChooseAddressCacheInteractorImpl;
import com.tokopedia.tkpd.manage.people.address.interactor.ChooseAddressRetrofitInteractor;
import com.tokopedia.tkpd.manage.people.address.interactor.ChooseAddressRetrofitInteractorImpl;
import com.tokopedia.tkpd.manage.people.address.listener.ChooseAddressFragmentView;
import com.tokopedia.tkpd.manage.people.address.model.ChooseAddress.ChooseAddressResponse;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressFragmentPresenterImpl implements  ChooseAddressFragmentPresenter {

    public static final int REQUEST_CHOOSE_ADDRESS_CODE = 1;
    ChooseAddressFragmentView viewListener;
    ChooseAddressRetrofitInteractor networkInteractor;
    ChooseAddressCacheInteractor cacheInteractor;
    PagingHandler paging;
    String query  = "";

    public ChooseAddressFragmentPresenterImpl(ChooseAddressFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ChooseAddressRetrofitInteractorImpl();
        this.cacheInteractor = new ChooseAddressCacheInteractorImpl();
        this.paging = new PagingHandler();
    }

    private void showLoading() {
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.setLoading();
        } else if(paging.getPage() == 1){
            viewListener.showRefreshing();
        }else{
            viewListener.getAdapter().showLoading(true);
        }
    }

    private TKPDMapParam<String, String> paramGetAddresses() {
        TKPDMapParam<String, String> paramTest = new TKPDMapParam<>();
        paramTest.put("page", Integer.toString(paging.getPage()));
        paramTest.put("query", query);
        return paramTest;
    }

    public void getAddresses() {
        showLoading();
        viewListener.setActionsEnabled(false);
        viewListener.removeError();
        networkInteractor.getAddresses(viewListener.getActivity(), paramGetAddresses(), new ChooseAddressRetrofitInteractor.ChooseAddressListener() {
            @Override
            public void onSuccess(@NonNull ChooseAddressResponse data) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);

                if (paging.getPage() == 1) {
                    viewListener.getAdapter().getList().clear();
                    cacheInteractor.setAddressesCache(data);
                }
                paging.setHasNext(PagingHandler.CheckHasNext(data.getPaging()));
                showData(data);

            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                if (viewListener.getAdapter().getList().size() == 0) {
                    viewListener.showEmptyState();
                } else {
                    viewListener.setRetry();
                }
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);

                if (viewListener.getAdapter().getList().size() == 0) {
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
                if (viewListener.getAdapter().getList().size() == 0) {
                    viewListener.showEmptyState();
                } else {
                    viewListener.setRetry();
                }
            }

        });
    }

    public void showData(ChooseAddressResponse data) {
        viewListener.getAdapter().addList(data.getList());
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showEmpty(true);
        }
        if (paging.CheckNextPage()) {
            viewListener.getAdapter().showLoading(true);
        }else{
            viewListener.getAdapter().showLoading(false);
        }
    }

    @Override
    public void onRefresh(String q) {
        this.query=q;
        paging.resetPage();
        getAddresses();
    }

    @Override
    public void setCache(String q) {
        this.query=q;
        cacheInteractor.getAddressesCache(new ChooseAddressCacheInteractor.GetAddressesCacheListener() {
            @Override
            public void onSuccess(ChooseAddressResponse cache) {
                showData(cache);
                getAddresses();

            }

            @Override
            public void onError(Throwable e) {
                getAddresses();
            }
        });
    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem,String q) {
        this.query=q;
        if (paging.CheckNextPage()) {
            paging.nextPage();
            getAddresses();
        }
    }

    @Override
    public RetryDataBinder.OnRetryListener onRetry(String q) {
        this.query=q;
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                getAddresses();
            }
        };
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }


}
