package com.tokopedia.tkpd.manage.people.address.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.tkpd.manage.people.address.datamanager.DataManager;
import com.tokopedia.tkpd.manage.people.address.datamanager.DataManagerImpl;
import com.tokopedia.tkpd.manage.people.address.datamanager.NetworkParam;
import com.tokopedia.tkpd.manage.people.address.fragment.ManagePeopleAddressFragment;
import com.tokopedia.tkpd.manage.people.address.listener.MPAddressActivityListener;
import com.tokopedia.tkpd.manage.people.address.listener.MPAddressFragmentListener;
import com.tokopedia.tkpd.manage.people.address.model.AddressModel;
import com.tokopedia.tkpd.manage.people.address.model.GetAddressDataPass;
import com.tokopedia.tkpd.manage.people.address.model.GetPeopleAddress;
import com.tokopedia.tkpd.manage.people.address.model.Paging;
import com.tokopedia.tkpd.manage.people.address.service.ManagePeopleAddressService;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.Map;

/**
 * Created on 5/18/16.
 */
public class ManagePeopleAddressFragmentImpl implements ManagePeopleAddressFragmentPresenter {

    @SuppressWarnings("unused")
    private static final String TAG = ManagePeopleAddressFragmentImpl.class.getSimpleName();

    private final MPAddressActivityListener activityListener;
    private final MPAddressFragmentListener fragmentListener;
    private final DataManager dataManager;
    private final PagingHandler pagingHandler;
    private boolean allowConnection;

    public ManagePeopleAddressFragmentImpl(ManagePeopleAddressFragment mFragment, MPAddressActivityListener listener) {
        this.activityListener = listener;
        this.fragmentListener = mFragment;
        this.pagingHandler = new PagingHandler();
        this.dataManager = new DataManagerImpl(this);
        this.setAllowConnection(true);
    }

    @Override
    public void setActionOnRefreshing(@NonNull Context context) {
        if (isAllowConnection()) {
            resetPage();
            dataManager.initAddressList(context, generateParams(context));
        }
    }

    @Override
    public void resetPage() {
        pagingHandler.resetPage();
    }

    private Map<String, String> generateParams(Context context) {
        GetAddressDataPass pass = new GetAddressDataPass();
        pass.setPage(pagingHandler.getPage());
        pass.setQuery(fragmentListener.getQuerySearch());
        pass.setSortID(fragmentListener.getSortID());
        return AuthUtil.generateParams(context, NetworkParam.paramGetAddress(pass));
    }

    @Override
    public void setActionOnLazyLoad(@NonNull Context context) {
        if (isAllowLazyLoad()) {
            pagingHandler.nextPage();
            dataManager.loadMoreAddressList(context, generateParams(context));
        }
    }

    private boolean isAllowLazyLoad() {
        return pagingHandler.CheckNextPage() && isAllowConnection();
    }

    @Override
    public void setActionOnLaunchFirstTime(@NonNull Context context) {
        this.setActionOnRefreshing(context);
    }

    @Override
    public boolean isAllowConnection() {
        return allowConnection;
    }

    @Override
    public void setAllowConnection(boolean allowConnection) {
        this.allowConnection = allowConnection;
    }

    @Override
    public void setActionOnActivityKilled(@NonNull Context context) {
        dataManager.disconnectNetworkConnection(context);
    }

    @Override
    public void setBeforeInitAddressList() {
        fragmentListener.setNoResultView(false);
        fragmentListener.setLoadingView(true);
        fragmentListener.setFilterView(false);
        fragmentListener.setRefreshEnable(false);
    }

    @Override
    public void setBeforeLoadMoreData() {
        setBeforeInitAddressList();
    }

    @Override
    public void onErrorGetCache() {

    }

    @Override
    public void onSuccessGetCache(GetPeopleAddress getPeopleAddress) {
        fragmentListener.setLoadingView(false);
        fragmentListener.setNoResultView(false);
        this.setNextPageStatus(getPeopleAddress.getPaging());
        fragmentListener.setRefreshView(true);
        fragmentListener.showCache(getPeopleAddress.getList());
    }

    private void setNextPageStatus(Paging paging) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(paging.getUriNext()));
    }

    @Override
    public void finishRequest() {
        fragmentListener.setLoadingView(false);
        fragmentListener.setRefreshView(false);
    }

    @Override
    public void setOnRequestSuccess() {
        fragmentListener.setFilterView(true);
        fragmentListener.setRefreshEnable(true);
    }

    @Override
    public void setOnResponseNull() {
        fragmentListener.clearCurrentList();
        fragmentListener.setNoResultView(true);
    }

    @Override
    public void setOnRequestError(String message, NetworkErrorHelper.RetryClickedListener clickedListener) {
        if (!fragmentListener.getList().isEmpty()) {
            fragmentListener.showErrorMessageSnackBar(message);
        } else {
            fragmentListener.showErrorMessageEmptyState(message, clickedListener);
        }
    }

    @Override
    public void setOnRequestTimeOut(NetworkErrorHelper.RetryClickedListener clickListener) {
        if (!fragmentListener.getList().isEmpty()) {
            setAllowConnection(true);
            setOnRequestSuccess();
        }
        fragmentListener.setLoadingView(false);
        fragmentListener.setRefreshView(false);
        fragmentListener.setFilterView(false);
        fragmentListener.showRetryView(clickListener);
    }

    @Override
    public void setOnSuccessInitAddressList(GetPeopleAddress data) {
        this.setNextPageStatus(data.getPaging());
        fragmentListener.replaceCache(data.getList());
    }

    @Override
    public void setOnSuccessLoadMoreData(GetPeopleAddress data) {
        this.setNextPageStatus(data.getPaging());
        fragmentListener.addAddressItemList(data.getList());
    }

    @Override
    public void setActionDefaultButtonClicked(final AddressModel data) {
        fragmentListener.showDialogConfirmation(
                "Apakah Anda yakin ingin menggunakan alamat:" +
                        "<br/><br/><b>" + data.getAddressName() + "</b><br/>" +
                        "<br/>" + data.getAddressFull() + "<br/>" +
                        "<br/><br/> sebagai alamat utama Anda?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setOnStartActionSetDefaultAddress(data.getAddressId());
                    }
                });
    }

    @Override
    public void setOnStartActionSetDefaultAddress(String addressID) {
        this.setAllowConnection(false);
        fragmentListener.setFilterView(false);
        fragmentListener.setRefreshEnable(false);
        fragmentListener.setRefreshView(true);
        activityListener.startServiceSetDefaultAddress(addressID);
    }

    @Override
    public void setActionDeleteButton(final AddressModel data) {
        fragmentListener.showDialogConfirmation(
                "Apakah Anda yakin ingin menghapus alamat: " +
                        "<b>" + data.getAddressName() + "</b>?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setOnStartActionDeleteAddress(data.getAddressId());
                    }
                });
    }

    @Override
    public void setOnStartActionDeleteAddress(String addressId) {
        this.setAllowConnection(false);
        fragmentListener.setFilterView(false);
        fragmentListener.setRefreshEnable(false);
        fragmentListener.setRefreshView(true);
        activityListener.startServiceDeleteAddress(addressId);
    }

    @Override
    public void setActionEditButton(AddressModel data) {
        fragmentListener.openFormAddressView(data);
    }

    @Override
    public void setOnActionReceiveResult(Context context, int resultCode, Bundle resultData) {
        final String action = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_ACTION_TYPE, "unknown_action");
        final String addressID = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_ADDRESS_ID);

        if (resultCode == ManagePeopleAddressService.STATUS_FINISHED) {
            this.setAllowConnection(true);
            this.setActionOnRefreshing(context);
        } else {
            String errorMessage = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_NETWORK_ERROR_MESSAGE);
            fragmentListener.setRefreshView(false);
            this.setAllowConnection(true);
            this.setOnActionErrorResult(action, resultData, addressID, errorMessage);
        }
    }

    private void setOnActionErrorResult(String action, Bundle resultData, String addressID, String errorMessage) {
        switch (resultData.getInt(ManagePeopleAddressService.EXTRA_PARAM_NETWORK_ERROR_TYPE)) {
            case ManagePeopleAddressService.STATUS_TIME_OUT :
                showRetyConnection(action, addressID, errorMessage);
                break;
            default:
                fragmentListener.showErrorMessageSnackBar(errorMessage);
                break;
        }
    }

    private void showRetyConnection(String action, final String addressID, String errorMessage) {
        switch (action) {
            case ManagePeopleAddressService.ACTION_DELETE_ADDRESS:
                fragmentListener.showTimeOutMessage(errorMessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnStartActionDeleteAddress(addressID);
                    }
                });
                break;
            case ManagePeopleAddressService.ACTION_SET_DEFAULT_ADDRESS:
                fragmentListener.showTimeOutMessage(errorMessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnStartActionSetDefaultAddress(addressID);
                    }
                });
                break;
            default:
                throw new UnsupportedOperationException("unknown operation");
        }
    }
}
