package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.SendGiftActivity;
import com.tokopedia.tokopoints.view.contract.SendGiftContract;
import com.tokopedia.tokopoints.view.presenter.SendGiftPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import javax.inject.Inject;

public class SendGiftFragment extends BaseDaggerFragment implements SendGiftContract.View, View.OnClickListener {
    private static final int CONTAINER_SEND_FORM = 0;
    private static final int CONTAINER_PRE_CONFIRMATION = 1;
    private ViewFlipper mContainerMain;
    private TextInputEditText mEditEmail, mEditNotes;
    private Button mBtnSendGift, mBtnSendNow;
    private TkpdHintTextInputLayout mWrapperEmail;

    @Inject
    public SendGiftPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new SendGiftFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_send_gift, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);

        mContainerMain = view.findViewById(R.id.container_main);
        mContainerMain.setDisplayedChild(CONTAINER_SEND_FORM);

        mEditEmail = view.findViewById(R.id.edit_email);
        mWrapperEmail = view.findViewById(R.id.wrapper_email);
        mEditNotes = view.findViewById(R.id.edit_notes);
        mBtnSendGift = view.findViewById(R.id.button_send);
        mBtnSendNow = view.findViewById(R.id.button_send_now);
        mBtnSendGift.setOnClickListener(this::onClick);
        mBtnSendNow.setOnClickListener(this::onClick);
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_send) {
            if (getArguments() == null || getActivity() == null) {
                return;
            }

            KeyboardHandler.hideSoftKeyboard(getActivity());
            mPresenter.preValidateGift(getArguments().getInt(CommonConstant.EXTRA_COUPON_ID), mEditEmail.getText().toString());
        } else if (view.getId() == R.id.button_send_now) {
            if (getArguments() == null || getActivity() == null) {
                return;
            }
            mPresenter.sendGift(getArguments().getInt(CommonConstant.EXTRA_COUPON_ID),
                    mEditEmail.getText().toString(),
                    mEditNotes.getText().toString());
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_KIRIM_SEKARANG,
                    getCouponTitle());

        }
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoading() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send).setVisibility(View.VISIBLE);
        mBtnSendGift.setText("");
    }

    @Override
    public void hideLoading() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send).setVisibility(View.GONE);
        mBtnSendGift.setText(R.string.tp_label_send);
    }

    @Override
    public void showLoadingSendNow() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send_now).setVisibility(View.VISIBLE);
        mBtnSendGift.setText("");
    }

    @Override
    public void hideLoadingSendNow() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.progress_send_now).setVisibility(View.GONE);
        mBtnSendGift.setText(R.string.tp_label_send_now);
    }

    private String getCouponTitle() {
        if (getArguments() != null) {
            String couponTitle = getArguments().getString(CommonConstant.EXTRA_COUPON_TITLE);
            if (couponTitle != null)
                return couponTitle;
        }
        return "";
    }

    private String getCouponPoint() {
        if (getArguments() != null) {
            String couponPoint = getArguments().getString(CommonConstant.EXTRA_COUPON_POINT);
            if (couponPoint != null)
                return couponPoint;
        }
        return "";
    }

    @Override
    public void openPreConfirmationWindow() {
        mContainerMain.setDisplayedChild(CONTAINER_PRE_CONFIRMATION);

        if (getView() == null || getArguments() == null) {
            return;
        }

        TextView textTitle = getView().findViewById(R.id.text_point_label);
        TextView textPoint = getView().findViewById(R.id.text_point_value);
        TextView textEmail = getView().findViewById(R.id.text_for_value);
        TextView textNotes = getView().findViewById(R.id.text_notes_value);

        textTitle.setText(getCouponTitle());
        textPoint.setText(getCouponPoint());
        textEmail.setText(mEditEmail.getText().toString());
        if (!mEditNotes.getText().toString().trim().isEmpty()) {
            textNotes.setText("\"" + mEditNotes.getText().toString().trim() + "\"");
        }
    }

    @Override
    public void onErrorPreValidate(String error) {
        mWrapperEmail.setError(error);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void showPopup(String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());

        adb.setTitle(title);
        adb.setMessage(message);

        adb.setPositiveButton(R.string.tp_label_ok, (dialogInterface, i) -> {
                    if (getActivity() != null) {
                        getActivity().finish();
                        if(title.equalsIgnoreCase(getString(R.string.tp_send_gift_success_title))){
                            AnalyticsTrackerUtil.sendEvent(getContext(),
                                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                                    AnalyticsTrackerUtil.ActionKeys.CLICK_OK_ON_SUCCESS,
                                    getCouponTitle());
                        }else{
                            AnalyticsTrackerUtil.sendEvent(getContext(),
                                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                                    AnalyticsTrackerUtil.ActionKeys.CLICK_OK_ON_FAILED,
                                    "");
                        }
                    }
                }
        );

        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    private void decorateDialog(AlertDialog dialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }
}
