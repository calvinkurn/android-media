package com.tokopedia.logisticaddaddress.features.manage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.logisticaddaddress.utils.BottomSheetFilterDialog;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.service.ManagePeopleAddressReceiver;
import com.tokopedia.logisticaddaddress.service.ManagePeopleAddressService;

import static com.tokopedia.logisticaddaddress.AddressConstants.SCREEN_MANAGE_ADDRESS;

public class ManagePeopleAddressActivity extends BaseSimpleActivity
        implements MPAddressActivityListener, ManagePeopleAddressReceiver.Receiver, ManagePeopleAddressActivityPresenter {

    FloatingActionButton fab;
    private ManagePeopleAddressReceiver mReceiver;
    private BottomSheetFilterDialog dialog;

    @Override
    public String getScreenName() {
        return SCREEN_MANAGE_ADDRESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::onFabClicked);
        fab.hide();

        mReceiver = new ManagePeopleAddressReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected Fragment getNewFragment() {
        return ManageAddressFragment.newInstance();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.logistic_activity_manage_people_address;
    }

    private void onFabClicked(View view) {
        dialog = BottomSheetFilterDialog.Builder(this).setView()
                .setListener(this);
        dialog.show();
    }

    @Override
    public void startServiceSetDefaultAddress(String addressId) {
        ManagePeopleAddressService.startActionSetDefaultAddress(this, addressId, mReceiver);
    }

    @Override
    public void startServiceDeleteAddress(String addressId) {
        ManagePeopleAddressService.startActionDeleteAddress(this, addressId, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        setOnReceiveResult(resultCode, resultData);
    }

    @Override
    public void setFilterViewVisibility(boolean isAble) {
        if (isAble) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    public void filterAddress(@NonNull Context context) {
        dialog.show();
    }

    @Override
    public void setOnSubmitFilterDialog(int spinnerPosition, String query) {
        ((ManageAddressContract.View) getFragment()).filter(spinnerPosition, query);
    }

    @Override
    public void setOnReceiveResult(int resultCode, Bundle resultData) {
        final String action = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_ACTION_TYPE, "unknown_action");
        final String addressID = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_ADDRESS_ID);

        if (getFragment() != null) {
            ManageAddressContract.View view = (ManageAddressContract.View) getFragment();
            if (resultCode == ManagePeopleAddressService.STATUS_FINISHED) {
                view.refreshView();
            } else {
                String errorMessage = resultData.getString(ManagePeopleAddressService.EXTRA_PARAM_NETWORK_ERROR_MESSAGE);
                view.showErrorSnackbar(errorMessage);
            }
        }
    }
}
