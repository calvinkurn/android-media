package com.tokopedia.logisticaddaddress.manageaddress;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 5/18/16.
 */
public interface MPAddressView {

    void prepareRecyclerView();

    String getQuerySearch();

    void setQuerySearch(String querySearch);

    int getSortID();

    void setSortID(int sortID);

    void setRefreshEnable(boolean isRefreshAble);

    void setRefreshView(boolean isAble);

    void setFilterView(boolean isAble);

    void setLoadingView(boolean isAble);

    void setNoResultView(boolean isAble);

    void showCache(List<AddressModel> list);

    void clearCurrentList();

    void replaceCache(List<AddressModel> list);

    void showRetryView(NetworkErrorHelper.RetryClickedListener clickedListener);

    ArrayList<AddressModel> getList();

    void showErrorMessageEmptyState(String message, NetworkErrorHelper.RetryClickedListener clickedListener);

    void showErrorMessageSnackBar(String message);

    void addAddressItemList(List<AddressModel> list);

    void setOnGetFilterActivated(int sortID, String query);

    void setOnActionReceiveResult(int resultCode, Bundle resultData);

    void showTimeOutMessage(String message, View.OnClickListener listener);

    void showDialogConfirmation(String message, DialogInterface.OnClickListener onPositiveClickListener);

    void openFormAddressView(AddressModel data);

    void setToken(Token token);

}
