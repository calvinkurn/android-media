package com.tokopedia.core.manage.people.address.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

/**
 * Created on 5/18/16.
 */
public interface ManagePeopleAddressPresenter {

    void filterAddress(@NonNull Context context);

    void initFragment(Uri uriData, Bundle bundleData);

    void setOnSubmitFilterDialog(int spinnerPosition, String query);

    void setOnReceiveResult(int resultCode, Bundle resultData);
}
