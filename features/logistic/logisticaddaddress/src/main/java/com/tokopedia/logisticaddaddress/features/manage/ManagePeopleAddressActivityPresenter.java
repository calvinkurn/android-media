package com.tokopedia.logisticaddaddress.features.manage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created on 5/18/16.
 */
public interface ManagePeopleAddressActivityPresenter {

    void filterAddress(@NonNull Context context);

    void setOnSubmitFilterDialog(int spinnerPosition, String query);

    void setOnReceiveResult(int resultCode, Bundle resultData);
}
