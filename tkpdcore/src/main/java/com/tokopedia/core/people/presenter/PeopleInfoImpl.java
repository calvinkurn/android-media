package com.tokopedia.core.people.presenter;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.people.fragment.PeopleInfoFragment;
import com.tokopedia.core.people.listener.PeopleInfoView;

import java.util.List;

/**
 * Created on 5/30/16.
 */
public class PeopleInfoImpl implements PeopleInfoPresenter {

    private final PeopleInfoView listener;

    public PeopleInfoImpl(PeopleInfoView view) {
        this.listener = view;
    }

    @Override
    public void initFragment(Uri uriData, Bundle bundleData) {
        listener.inflateFragment(
                PeopleInfoFragment.newInstance(generateUserID(uriData, bundleData)),
                PeopleInfoFragment.TAG);
    }

    private String generateUserID(Uri uriData, Bundle bundleData) {
        String userID = "";
        if (bundleData != null) {
            userID = bundleData.getString("user_id", "");
        } else {
            List<String> uriSegments = uriData.getPathSegments();
            if (uriSegments.size() >= 2) {
                userID = uriSegments.get(1);
            }
        }
        return userID;
    }
}
