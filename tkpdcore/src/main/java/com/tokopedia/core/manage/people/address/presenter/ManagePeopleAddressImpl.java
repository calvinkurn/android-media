package com.tokopedia.core.manage.people.address.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.manage.people.address.activity.ManagePeopleAddressActivity;
import com.tokopedia.core.manage.people.address.customdialog.BottomSheetFilterDialog;
import com.tokopedia.core.manage.people.address.fragment.ManagePeopleAddressFragment;
import com.tokopedia.core.manage.people.address.listener.MPAddressActivityListener;

/**
 * Created on 5/18/16.
 */
public class ManagePeopleAddressImpl implements ManagePeopleAddressPresenter {

    private final MPAddressActivityListener listener;
    private final BottomSheetFilterDialog dialog;

    public ManagePeopleAddressImpl(ManagePeopleAddressActivity mActivity) {
        this.listener = mActivity;
        this.dialog =
                BottomSheetFilterDialog.Builder(mActivity)
                        .setView()
                        .setListener(this);
    }

    @Override
    public void filterAddress(@NonNull Context context) {
        dialog.show();
    }

    @Override
    public void initFragment(Uri uriData, Bundle bundleData) {
        listener.inflateFragment(ManagePeopleAddressFragment.newInstance(),
                ManagePeopleAddressFragment.class.getSimpleName());
    }

    @Override
    public void setOnSubmitFilterDialog(int spinnerPosition, String query) {
        try {
            ManagePeopleAddressFragment fragment = (ManagePeopleAddressFragment) listener.getInflatedFragment(ManagePeopleAddressFragment.class.getSimpleName());
            fragment.setOnGetFilterActivated(spinnerPosition, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnReceiveResult(int resultCode, Bundle resultData) {
        try {
            ManagePeopleAddressFragment fragment = (ManagePeopleAddressFragment) listener.getInflatedFragment(ManagePeopleAddressFragment.class.getSimpleName());
            fragment.setOnActionReceiveResult(resultCode, resultData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
