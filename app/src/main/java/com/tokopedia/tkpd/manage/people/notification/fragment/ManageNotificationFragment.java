package com.tokopedia.tkpd.manage.people.notification.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.manage.people.notification.listener.ManageNotificationFragmentView;
import com.tokopedia.tkpd.manage.people.notification.model.SettingNotification;
import com.tokopedia.tkpd.manage.people.notification.presenter.ManageNotificationPresenter;
import com.tokopedia.tkpd.manage.people.notification.presenter.ManageNotificationPresenterImpl;
import com.tokopedia.tkpd.network.NetworkErrorHelper;

import butterknife.Bind;

/**
 * Created by Nisie on 6/22/16.
 */
public class ManageNotificationFragment extends BasePresenterFragment<ManageNotificationPresenter>
        implements ManageNotificationFragmentView {

    @Bind(R.id.newsletter)
    CheckBox newsletterCheckBox;

    @Bind(R.id.reviews)
    CheckBox reviewsCheckBox;

    @Bind(R.id.talkabout)
    CheckBox talkCheckBox;

    @Bind(R.id.message)
    CheckBox messageCheckBox;

    @Bind(R.id.admin_message)
    CheckBox adminCheckBox;

    @Bind(R.id.save_but)
    TextView saveButton;

    @Bind(R.id.main_view)
    View mainView;

    @Bind(R.id.set_ring)
    TextView setRingButton;

    TkpdProgressDialog mProgressDialog;

    public static ManageNotificationFragment createInstance(Bundle extras) {
        ManageNotificationFragment fragment = new ManageNotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        mainView.setVisibility(View.GONE);
        setRingButton.setVisibility(View.GONE);
        presenter.checkCache();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManageNotificationPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_notification;
    }

    @Override
    protected void initView(View view) {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS, view);
        mProgressDialog.setLoadingViewId(R.id.include_loading);
    }

    @Override
    protected void setViewListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveSetting();
            }
        });
        setRingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGoToSetRing();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        mainView.setVisibility(View.VISIBLE);
        mProgressDialog.dismiss();
    }

    @Override
    public void setToUI(SettingNotification setting) {
        if (setting.getNotification().isReviewChecked())
            reviewsCheckBox.setChecked(true);
        if (setting.getNotification().isMessageChecked())
            messageCheckBox.setChecked(true);
        if (setting.getNotification().isAdminMessageChecked())
            adminCheckBox.setChecked(true);
        if (setting.getNotification().isNewsLetterChecked())
            newsletterCheckBox.setChecked(true);
        if (setting.getNotification().isTalkProductChecked())
            talkCheckBox.setChecked(true);
    }

    @Override
    public void showLoading() {
        mProgressDialog.showDialog();
    }

    @Override
    public int getFlagTalkProduct() {
        return talkCheckBox.isChecked() ? 1 : 0;
    }

    @Override
    public int getFlagReview() {
        return reviewsCheckBox.isChecked() ? 1 : 0;
    }

    @Override
    public int getFlagNewsletter() {
        return newsletterCheckBox.isChecked() ? 1 : 0;
    }

    @Override
    public int getFlagMessage() {
        return messageCheckBox.isChecked() ? 1 : 0;
    }

    @Override
    public int getFlagAdminMessage() {
        return adminCheckBox.isChecked() ? 1 : 0;
    }

    @Override
    public void showSnackbar(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void showSnackbar() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
