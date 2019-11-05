package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CouponCatalogDetailsActivity;
import com.tokopedia.tokopoints.view.contract.SendGiftContract;
import com.tokopedia.tokopoints.view.presenter.SendGiftPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import javax.inject.Inject;

public class SendGiftFragment extends BottomSheetDialogFragment implements SendGiftContract.View, View.OnClickListener, TextWatcher {
    private static final int CONTAINER_SEND_FORM = 0;
    private static final int CONTAINER_PRE_CONFIRMATION = 1;
    private ViewFlipper mContainerMain;
    private TextInputEditText mEditEmail, mEditNotes;
    private TextView mBtnSendGift, mBtnSendNow;
    private TkpdHintTextInputLayout mWrapperEmail;
    TokoPointComponent tokoPointComponent;

    @Inject
    public SendGiftPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new SendGiftFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, com.tokopedia.design.R.style.TransparentBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tp_fragment_send_gift, container, false);
        initView();
        return view;
    }

    public void initView() {
        tokoPointComponent = ((CouponCatalogDetailsActivity) getActivity()).getComponent();
        tokoPointComponent.inject(this);
        mPresenter.attachView(this);
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

        ImageView ivCancelInitial = getView().findViewById(R.id.iv_cancel);
        ImageView ivCancelPreConfirm = getView().findViewById(R.id.iv_cancel_preconfirmation);
        ivCancelInitial.setOnClickListener(view1 -> {
            dismiss();
        });
        ivCancelPreConfirm.setOnClickListener(view1 -> dismiss());

        mEditEmail.addTextChangedListener(this);
        mEditNotes.addTextChangedListener(this);
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

        TextView textTitle = getView().findViewById(R.id.tv_title_banner);
        TextView textPoint = getView().findViewById(R.id.point);
        TextView textEmail = getView().findViewById(R.id.email);
        TextView textNotes = getView().findViewById(R.id.message);
        ImageView imgBanner = getView().findViewById(R.id.iv_banner);
        ImageHandler.loadImage(this.getContext(), imgBanner, getArguments().getString(CommonConstant.EXTRA_COUPON_BANNER), com.tokopedia.design.R.color.grey_100);


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
        return getActivity();    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void showPopup(String title, String message, int success) {
        ConstraintLayout constraintLayout = getView().findViewById(R.id.pre_confirmation_parent_container);
        constraintLayout.setVisibility(View.GONE);
        View viewSentFail = View.inflate(this.getContext(), R.layout.tp_gift_coupon_sent_fail, null);
        View viewSentSuccess = View.inflate(this.getContext(), R.layout.tp_gift_coupon_sent, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());

        if (success == 1) {
            TextView tvTitle = viewSentSuccess.findViewById(R.id.tv_title);
            TextView tvContent = viewSentSuccess.findViewById(R.id.content);
            TextView btnSuccess = viewSentSuccess.findViewById(R.id.btn_sentSuccess);

            btnSuccess.setOnClickListener(view -> {
                getActivity().finish();
            });

            tvTitle.setText(title);
            tvContent.setText(message);
            adb.setView(viewSentSuccess);
        } else {
            TextView tvTitle = viewSentFail.findViewById(R.id.tv_title);
            TextView tvContent = viewSentFail.findViewById(R.id.content);
            TextView tvRoute = viewSentFail.findViewById(R.id.tv_route);
            TextView btnFailed = viewSentFail.findViewById(R.id.btn_sentFail);

            btnFailed.setOnClickListener(view -> {
                getActivity().finish();
            });

            tvRoute.setOnClickListener(view -> {
                RouteManager.route(this.getContext(), ApplinkConst.TOKOPOINTS);
            });
            tvTitle.setText(title);
            tvContent.setText(message);
            adb.setView(viewSentFail);
        }


        AlertDialog dialog = adb.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        dialog.show();
        decorateDialog(dialog);
    }

    private void decorateDialog(AlertDialog dialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    com.tokopedia.design.R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.length() == 0) {
            mBtnSendGift.setEnabled(false);
            mBtnSendGift.setTextColor(getResources().getColor(R.color.tp_btn_sent_gift_color));
        } else {
            mBtnSendGift.setEnabled(true);
            mBtnSendGift.setTextColor(getResources().getColor(R.color.tp_bg_button_orange_border));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
