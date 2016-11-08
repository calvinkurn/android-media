package com.tokopedia.core.peoplefave.listener;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.peoplefave.customadapter.PeopleFavoritedShopAdapter;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;

/**
 * Created by hangnadi on 10/11/16.
 */
public interface PeopleFavoritedShopFragmentView {
    boolean isOnConnection();

    void setConnectionStatus(boolean connectionStatus);

    PeopleFavoritedShopData getData();

    void renderData(PeopleFavoritedShopData data);

    boolean isOwner();

    void openShopPage(String shopID);

    void onActionUnfavoriteClick(PeopleFavoritedShopData.ShopFavorited shopFavorited);

    void setPullToRefresh(boolean able);

    void showEmptyState(NetworkErrorHelper.RetryClickedListener listener);

    void showEmptyState(String message);

    void showEmptyState();

    void showSnackBar();

    void showSnackBar(String message);

    void setRefreshing(boolean refreshing);

    String getUserID();

    PeopleFavoritedShopAdapter getAdapter();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void resetData();
}
