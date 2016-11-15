package com.tokopedia.core.manage.people.password.presenter;

import android.app.Activity;

import com.tokopedia.core.manage.people.password.model.ChangePasswordParam;

/**
 * Created by stevenfredian on 9/28/16.
 */
public interface ManagePasswordFragmentPresenter {

    void changePassword(Activity activity, ChangePasswordParam param);
}
