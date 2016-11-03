package com.tokopedia.tkpd.contactus.listener;

import android.app.Activity;

import com.tokopedia.tkpd.contactus.activity.ContactUsActivity;
import com.tokopedia.tkpd.contactus.model.contactuscategory.ContactUsCategory;

/**
 * Created by nisie on 8/12/16.
 */
public interface ContactUsCategoryFragmentView {
    void showProgressDialog();

    Activity getActivity();

    void finishLoading();

    void setCategory(ContactUsCategory result);

    ContactUsActivity.BackButtonListener getBackButtonListener();

    String getString(int resId);

    void showError(String error);
}
