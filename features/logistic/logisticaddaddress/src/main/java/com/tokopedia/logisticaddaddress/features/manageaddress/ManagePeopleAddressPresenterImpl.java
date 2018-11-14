package com.tokopedia.logisticaddaddress.features.manageaddress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticaddaddress.di.AddressScope;
import com.tokopedia.logisticaddaddress.domain.AddressViewModelMapper;
import com.tokopedia.logisticaddaddress.data.DataManager;
import com.tokopedia.logisticaddaddress.data.DataManagerImpl;
import com.tokopedia.logisticaddaddress.utils.NetworkParam;
import com.tokopedia.logisticaddaddress.service.ManagePeopleAddressService;
import com.tokopedia.logisticdata.data.entity.address.GetAddressDataPass;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.logisticdata.data.entity.address.Paging;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;


import java.util.Map;

import javax.inject.Inject;

/**
 * Created on 5/18/16.
 */
@AddressScope
public class ManagePeopleAddressPresenterImpl implements ManagePeopleAddressPresenter {

    private final MPAddressActivityListener activityListener;
    private ManagePeopleAddressView fragmentListener;
    private final DataManager dataManager;
    private final PagingHandler pagingHandler;
    private boolean allowConnection;

    @Inject
    public ManagePeopleAddressPresenterImpl(MPAddressActivityListener listener, DataManagerImpl dataManager) {
        this.activityListener = listener;
        this.pagingHandler = new PagingHandler();
        this.dataManager = dataManager;
        // this is a workaround for circular dependency issue when using dagger
        // todo: try a better approach, rx? listener?
        ((DataManagerImpl) this.dataManager).setPresenter(this);
        this.setAllowConnection(true);
    }

    @Override
    public void setView(ManagePeopleAddressView view) {
        this.fragmentListener = view;
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
        UserSession userSession = new UserSession(context);
        return AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(),
                NetworkParam.paramGetAddress(pass));
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
        fragmentListener.setToken(getPeopleAddress.getToken());
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
        fragmentListener.setToken(data.getToken());
    }

    @Override
    public void setOnEmptyAddressList(GetPeopleAddress data) {
        fragmentListener.clearCurrentList();
        fragmentListener.setNoResultView(true);
        fragmentListener.setToken(data.getToken());
    }

    @Override
    public void setOnSuccessLoadMoreData(GetPeopleAddress data) {
        this.setNextPageStatus(data.getPaging());
        fragmentListener.addAddressItemList(data.getList());
        fragmentListener.setToken(data.getToken());
    }

    @Override
    public void setActionDefaultButtonClicked(final AddressViewModel data) {
        fragmentListener.showDialogConfirmation(
                "Apakah Anda yakin ingin menggunakan alamat:" +
                        "<br/><br/><b>" + data.getAddressName() + "</b><br/>" +
                        "<br/>" + data.getAddressFull() + "<br/>" +
                        "<br/><br/> sebagai alamat utama Anda?",
                (dialogInterface, i) -> setOnStartActionSetDefaultAddress(data.getAddressId()));
    }

    @Override
    public void setOnStartActionSetDefaultAddress(String addressID) {
        this.setAllowConnection(false);
        fragmentListener.setFilterView(false);
        fragmentListener.setRefreshView(true);
        activityListener.startServiceSetDefaultAddress(addressID);
    }

    @Override
    public void setActionDeleteButton(final AddressViewModel data) {
        fragmentListener.showDialogConfirmation(
                "Apakah Anda yakin ingin menghapus alamat: " +
                        "<b>" + data.getAddressName() + "</b>?",
                (dialogInterface, i) -> setOnStartActionDeleteAddress(data.getAddressId()));
    }

    @Override
    public void setOnStartActionDeleteAddress(String addressId) {
        this.setAllowConnection(false);
        fragmentListener.setFilterView(false);
        fragmentListener.setRefreshView(true);
        activityListener.startServiceDeleteAddress(addressId);
    }

    @Override
    public void setActionEditButton(AddressViewModel data) {
        fragmentListener.openFormAddressView(
                AddressViewModelMapper.convertFromViewModel(data)
        );
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
            case ManagePeopleAddressService.STATUS_TIME_OUT:
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
