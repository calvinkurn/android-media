package com.tokopedia.core.manage.people.profile.presenter;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.manage.people.profile.fragment.ManagePeopleProfileFragment;
import com.tokopedia.core.manage.people.profile.listener.ManagePeopleProfileView;

/**
 * Created on 6/9/16.
 */
public class ManagePeopleProfileImpl implements ManagePeopleProfilePresenter {

    private final ManagePeopleProfileView listener;

    public ManagePeopleProfileImpl(ManagePeopleProfileView view) {
        this.listener = view;
    }

    @Override
    public void initFragment(Uri uriData, Bundle bundleData) {
        listener.inflateFragment(
                ManagePeopleProfileFragment.newInstance(),
                ManagePeopleProfileFragment.class.getSimpleName()
        );
    }
}
