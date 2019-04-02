package com.tokopedia.loginphone.verifyotptokocash.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.loginphone.verifyotptokocash.di.DaggerOtpTokoCashComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.activity.TokoCashOtpActivity;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseTokocashVerificationMethodFragment extends ChooseVerificationMethodFragment {

    @Inject
    LoginPhoneNumberAnalytics loginPhoneNumberAnalytics;

    public static Fragment createInstance(VerificationPassModel passModel) {
        Fragment fragment = new ChooseTokocashVerificationMethodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerOtpTokoCashComponent daggerOtpTokoCashComponent = (DaggerOtpTokoCashComponent)
                    DaggerOtpTokoCashComponent.builder()
                            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                            .build();

            daggerOtpTokoCashComponent.inject(this);

        }
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        changePhoneNumberButton.setVisibility(View.GONE);
    }

    @Override
    protected void initList() {
        dismissLoading();
        adapter.setList(getList());
    }

    private List<MethodItem> getList() {
        List<MethodItem> list = new ArrayList<>();

        if (getContext() != null) {
            list.add(new MethodItem(
                    RequestOtpUseCase.MODE_SMS,
                    R.drawable.ic_verification_sms,
                    MethodItem.getSmsMethodText(passModel.getPhoneNumber(), getContext()),
                    createSmsMessage(passModel.getPhoneNumber())
            ));
            list.add(new MethodItem(
                    RequestOtpUseCase.MODE_CALL,
                    R.drawable.ic_verification_call,
                    MethodItem.getCallMethodText(passModel.getPhoneNumber(), getContext()),
                    createCallMessage(passModel.getPhoneNumber())
            ));
        }
        return list;
    }

    private String createSmsMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sms_sent_to) + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    private String getMaskedPhone(String phoneNumber) {
        String masked = String.valueOf(phoneNumber).replaceFirst("(\\d{4})(\\d{4})(\\d+)",
                "$1-$2-$3");
        return String.format(
                ("<b>%s</b>"), masked);
    }

    private String createCallMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sent_to_call)
                    + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    @Override
    public void onMethodSelected(MethodItem methodItem) {
        if (getActivity() instanceof TokoCashOtpActivity) {
            loginPhoneNumberAnalytics.eventChooseVerificationMethodTracking(methodItem
                    .getModeName());
            ((TokoCashOtpActivity) getActivity()).goToVerificationPage(methodItem);
        }
    }


}
