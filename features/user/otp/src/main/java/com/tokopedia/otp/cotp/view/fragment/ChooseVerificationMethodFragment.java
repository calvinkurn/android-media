package com.tokopedia.otp.cotp.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.common.di.DaggerOtpComponent;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.adapter.VerificationMethodAdapter;
import com.tokopedia.otp.cotp.view.presenter.ChooseVerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseVerificationMethodFragment extends BaseDaggerFragment implements
        SelectVerification.View {

    protected static final String PASS_MODEL = "pass_model";

    private static final int TYPE_HIDE_LINK = 0;
    private static final int TYPE_CHANGE_PHONE_UPLOAD_KTP = 1;
    private static final int TYPE_PROFILE_SETTING = 2;

    protected RecyclerView methodListRecyclerView;
    protected TextView changePhoneNumberButton;
    protected VerificationMethodAdapter adapter;
    protected VerificationPassModel passModel;
    protected View mainView;
    protected ProgressBar loadingView;
    @Inject
    ChooseVerificationPresenter presenter;

    @Inject
    OTPAnalytics analytics;

    @Inject
    UserSessionInterface userSession;

    @Override
    protected String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD;
    }

    @Override
    protected void initInjector() {

        OtpComponent otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication())
                        .getBaseAppComponent()).build();

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this);

        presenter.attachView(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());

    }

    public static Fragment createInstance(VerificationPassModel passModel) {
        Fragment fragment = new ChooseVerificationMethodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getParcelable(PASS_MODEL) != null) {
            passModel = getArguments().getParcelable(PASS_MODEL);
        } else if (getActivity() != null) {
            Log.d(ChooseVerificationMethodFragment.class.getSimpleName(), "Missing Passmodel");
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotp_choose_method, parent, false);
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.progress_bar);
        methodListRecyclerView = view.findViewById(R.id.method_list);
        changePhoneNumberButton = view.findViewById(R.id.phone_inactive);
        prepareView();
        return view;
    }

    protected void prepareView() {
        adapter = VerificationMethodAdapter.createInstance(this);
        methodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        methodListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
    }

    protected void initList() {
        presenter.getMethodList(passModel.getPhoneNumber(),
                passModel.getOtpType());
    }

    @Override
    public void onMethodSelected(MethodItem methodItem) {

        if (analytics != null
                && methodItem != null
                && passModel != null) {
            analytics.eventClickMethodOtp(passModel.getOtpType(), methodItem.getModeName());
        }
        if (methodItem != null
                && methodItem.isUsingPopUp()
                && !TextUtils.isEmpty(methodItem.getPopUpHeader())
                && !TextUtils.isEmpty(methodItem.getPopUpBody())) {
            showInterruptDialog(methodItem);
        } else if (getActivity()!= null && getActivity() instanceof VerificationActivity) {
            ((VerificationActivity) getActivity()).goToVerificationPage(methodItem);
        }
    }

    private void showInterruptDialog(final MethodItem methodItem) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);

        dialog.setTitle(methodItem.getPopUpHeader());
        dialog.setDesc(methodItem.getPopUpBody());
        dialog.setBtnOk(getString(R.string.btn_continue));
        dialog.setOnOkClickListener(v -> {
            dialog.dismiss();
            if (getActivity() instanceof VerificationActivity) {
                ((VerificationActivity) getActivity()).goToVerificationPage(methodItem);
            }
        });
        dialog.setBtnCancel(getString(R.string.btn_cancel));
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetList(ListVerificationMethod listVerificationMethod) {
        adapter.setList(listVerificationMethod.getList());

        switch (listVerificationMethod.getFooterLinkType()) {
            case TYPE_CHANGE_PHONE_UPLOAD_KTP:
                changePhoneNumberButton.setVisibility(View.VISIBLE);
                changePhoneNumberButton.setTypeface(Typeface.create("sans-serif-medium", Typeface
                        .NORMAL));
                changePhoneNumberButton.setText(getString(R.string.my_phone_number_is_inactive));
                changePhoneNumberButton.setTextColor(MethodChecker.getColor(getContext(), R.color
                        .tkpd_main_green));
                changePhoneNumberButton.setOnClickListener(v -> goToRequestChangePhoneNumberUploadKTP());
                break;
            case TYPE_PROFILE_SETTING:
                changePhoneNumberButton.setVisibility(View.VISIBLE);
                changePhoneNumberButton.setTextColor(MethodChecker.getColor(getContext(), R.color
                        .black_38));
                String changeInactiveString = getString(R.string
                        .my_phone_inactive_change_at_setting);
                SpannableString changeInactiveSpan = new SpannableString(changeInactiveString);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        goToProfileSetting();

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                        ds.setUnderlineText(false);
                    }
                };

                changeInactiveSpan.setSpan(clickableSpan,
                        changeInactiveString.indexOf(getString(R.string.setting)),
                        changeInactiveString.indexOf(getString(R.string.setting)) + getString(R
                                .string.setting).length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                changePhoneNumberButton.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
                changePhoneNumberButton.setText(changeInactiveSpan);
                changePhoneNumberButton.setMovementMethod(LinkMovementMethod.getInstance());
                changePhoneNumberButton.setHighlightColor(Color.TRANSPARENT);
                break;
            case TYPE_HIDE_LINK:
            default:
                changePhoneNumberButton.setVisibility(View.GONE);
                break;
        }

    }

    private void goToProfileSetting() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SETTING_PROFILE);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void goToRequestChangePhoneNumberUploadKTP() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(),
                    ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE_FORM);
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_CIPF_USER_ID,
                    userSession.getTemporaryUserId());
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_CIPF_OLD_PHONE,
                    userSession.getTempPhoneNumber());
            startActivity(intent);
        }
    }

    @Override
    public void onErrorGetList(String errorMessage) {
        if (!isAdded()) {
            return;
        }
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView,
                    () -> presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType()));
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, errorMessage,
                    () -> presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType()));
        }
    }

    @Override
    public void logUnknownError(Throwable message) {
        try {
            Crashlytics.logException(message);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
