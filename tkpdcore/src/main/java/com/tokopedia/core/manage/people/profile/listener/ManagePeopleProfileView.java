package com.tokopedia.core.manage.people.profile.listener;

import android.app.Fragment;

import com.tokopedia.core.manage.people.profile.model.PeopleProfilePass;

/**
 * Created by stevenfredian on 4/26/16.
 */
@Deprecated
public interface ManagePeopleProfileView {
    void inflateFragment(Fragment fragment, String TAG);

    void callServiceToSaveProfile(PeopleProfilePass paramPass);

}
