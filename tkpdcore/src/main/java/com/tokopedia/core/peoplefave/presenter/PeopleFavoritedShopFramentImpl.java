package com.tokopedia.core.peoplefave.presenter;

import android.content.Context;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.peoplefave.listener.PeopleFavoritedShopFragmentView;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;
import com.tokopedia.core.peoplefave.retrofit.PeopleFavoritedShopInteractor;
import com.tokopedia.core.peoplefave.retrofit.PeopleFavoritedShopInteractorImpl;
import com.tokopedia.core.util.PagingHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hangnadi on 10/11/16.
 */
public class PeopleFavoritedShopFramentImpl implements PeopleFavoritedShopFragmentPresenter {

    private final PagingHandler pagingHandler;
    private final PeopleFavoritedShopInteractor retrofitInteractor;
    private final PeopleFavoritedShopFragmentView viewListener;

    public PeopleFavoritedShopFramentImpl(PeopleFavoritedShopFragmentView viewListener) {
        this.pagingHandler = new PagingHandler();
        this.retrofitInteractor = new PeopleFavoritedShopInteractorImpl();
        this.viewListener = viewListener;
    }

    @Override
    public void setOnFirstTimeLaunch(Context context) {
        if (isAllowConnection()) {
            requestDataService(context, new PeopleFavoritedShopInteractor.PeopleFavoritedShopListener() {
                @Override
                public void onStart() {
                    if (viewListener.getAdapter().getListSize() == 0) {
                        viewListener.getAdapter().showLoadingFull(true);
                        viewListener.getAdapter().showEmpty(false);
                    } else if (pagingHandler.getPage() == 1) {
                        viewListener.setRefreshing(true);
                    }
                    viewListener.setConnectionStatus(true);
                    viewListener.setPullToRefresh(false);
                }

                @Override
                public void onSuccess(PeopleFavoritedShopData data) {
                    viewListener.getAdapter().showLoading(false);
                    viewListener.setRefreshing(false);
                    viewListener.setConnectionStatus(false);
                    viewListener.setPullToRefresh(true);
                    viewListener.renderData(data);
                    setNextPageStatus(data);
                }

                @Override
                public void onTimeout(NetworkErrorHelper.RetryClickedListener listener) {
                    viewListener.getAdapter().showLoading(false);
                    viewListener.setRefreshing(false);
                    setOnTimeOutConnection(listener);
                }

                @Override
                public void onError(String message) {
                    viewListener.getAdapter().showLoading(false);
                    viewListener.setRefreshing(false);
                    renderErrorMessage(message);
                }

                @Override
                public void onNullData() {
                    viewListener.getAdapter().showLoading(false);
                    viewListener.setRefreshing(false);
                    renderErrorMessage(null);
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private Map<String, String> generateParams() {
        Map<String, String> params = new HashMap<>();
        params.put("profile_user_id", viewListener.getUserID());
        params.put("page", String.valueOf(pagingHandler.getPage()));
        return params;
    }

    private boolean isAllowConnection() {
        return !viewListener.isOnConnection();
    }

    private void requestDataService(Context context, PeopleFavoritedShopInteractor.PeopleFavoritedShopListener listener) {
        retrofitInteractor.requestDataService(context, AuthUtil.generateParams(context, generateParams()), listener);
    }

    @Override
    public void setNextPageStatus(PeopleFavoritedShopData data) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(data.getPaging().getUriNext()));
    }

    @Override
    public void setOnTimeOutConnection(NetworkErrorHelper.RetryClickedListener listener) {
        if (viewListener.getAdapter().getListSize() == 0) {
            viewListener.showEmptyState(listener);
        } else {
            viewListener.setConnectionStatus(false);
            viewListener.setPullToRefresh(true);
            viewListener.showSnackBar();
        }
    }

    @Override
    public void renderErrorMessage(String message) {
        if (viewListener.getAdapter().getListSize() == 0) {
            viewListener.getAdapter().showEmptyFull(true);
        } else {
            showSnackBar(message);
        }
    }

    private void showSnackBar(String message) {
        if (message == null) {
            viewListener.showSnackBar();
        } else {
            viewListener.showSnackBar(message);
        }
    }

    private void showEmptyState(String message) {
        if (message == null) {
            viewListener.showEmptyState();
        } else {
            viewListener.showEmptyState(message);
        }
    }

    @Override
    public void setOnRefreshing(Context context) {
        pagingHandler.resetPage();
        setOnFirstTimeLaunch(context);
    }

    @Override
    public void getNextPage(Context context, int lastItemPosition, int visibleItem) {
        if (hasNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && isAllowConnection()) {
            pagingHandler.nextPage();
            requestDataService(context, new PeopleFavoritedShopInteractor.PeopleFavoritedShopListener() {
                @Override
                public void onStart() {
                    viewListener.getAdapter().showLoading(true);
                    viewListener.setConnectionStatus(true);
                    viewListener.setPullToRefresh(false);
                }

                @Override
                public void onSuccess(PeopleFavoritedShopData data) {
                    renderNextData(data);
                    setNextPageStatus(data);
                }

                @Override
                public void onTimeout(NetworkErrorHelper.RetryClickedListener listener) {
                    setOnTimeOutConnection(listener);
                }

                @Override
                public void onError(String message) {
                    renderErrorMessage(message);
                }

                @Override
                public void onNullData() {
                    renderErrorMessage(null);
                }

                @Override
                public void onComplete() {
                    viewListener.getAdapter().showLoading(false);
                    viewListener.setConnectionStatus(false);
                    viewListener.setPullToRefresh(true);
                }
            });
        }
    }

    private void renderNextData(PeopleFavoritedShopData newData) {
        PeopleFavoritedShopData previousData = viewListener.getData();
        previousData.setPaging(newData.getPaging());
        previousData.getList().addAll(newData.getList());
        viewListener.renderData(previousData);
    }

    private boolean isOnLastPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }


    @Override
    public void postUnfavoriteClick(final Context context, String shopID) {
        retrofitInteractor.updateDataService(context,
                AuthUtil.generateParams(context, generateUpdateFavoriteShopParams(shopID)),
                new PeopleFavoritedShopInteractor.ActionFavoritedShopListener() {
                    @Override
                    public void onStart() {
                        viewListener.showLoadingDialog();
                    }

                    @Override
                    public void onSuccess() {
                        viewListener.dismissLoadingDialog();
                        viewListener.showSnackBar(context.getString(R.string.message_success_people_unfav));
                        resetPage(context);
                    }

                    @Override
                    public void onTimeOut() {
                        viewListener.dismissLoadingDialog();
                        viewListener.showSnackBar();
                    }

                    @Override
                    public void onError(String message) {
                        viewListener.dismissLoadingDialog();
                        viewListener.showSnackBar(message);
                    }

                });
    }

    private void resetPage(Context context) {
        viewListener.resetData();
        setOnRefreshing(context);
    }

    private Map<String, String> generateUpdateFavoriteShopParams(String shopID) {
        Map<String, String> params = new HashMap<>();
        params.put("shop_id", shopID);
        params.put("src", "people_info");
        return params;
    }

    @Override
    public void setOnDestroyView(Context context) {
        retrofitInteractor.unsubscribe();
    }
}
