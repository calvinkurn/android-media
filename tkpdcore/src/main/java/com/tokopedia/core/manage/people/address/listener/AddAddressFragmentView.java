package com.tokopedia.core.manage.people.address.listener;

import android.content.Context;

import com.tokopedia.core.manage.people.address.model.Destination;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressFragmentView {

    Context context();

    boolean isEdit();

    void showLoading();

    void finishLoading();

    void finishActivity();

    void showErrorSnackbar(String errorMessage);

    void setActionsEnabled(boolean actionsEnabled);

    String getPassword();

    boolean isValidAddress();

    Destination getAddress();

    void setAddress(Destination address);

}
