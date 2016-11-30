package com.tokopedia.core.manage.people.address.listener;

import android.app.Fragment;

/**
 * Created on 5/18/16.
 */
public interface MPAddressActivityListener {
    void inflateFragment(Fragment fragment, String tag);

    Fragment getInflatedFragment(String tag);

    void startServiceSetDefaultAddress(String addressId);

    void startServiceDeleteAddress(String addressId);

    void setFilterViewVisibility(boolean isAble);
}
