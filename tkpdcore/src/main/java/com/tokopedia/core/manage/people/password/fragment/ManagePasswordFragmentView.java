package com.tokopedia.core.manage.people.password.fragment;

import android.app.Activity;

/**
 * Created by stevenfredian on 9/28/16.
 */
public interface ManagePasswordFragmentView {
    void showProgress();

    void dismissProgress();

    Activity getActivity();

    void dismissKeyboard();
}
