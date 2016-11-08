package com.tokopedia.core.msisdn.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.facade.FacadePhoneVerification;
import com.tokopedia.core.fragment.VerificationDialog;
import com.tokopedia.core.interfaces.PhoneVerificationInterfaces;
import com.tokopedia.core.msisdn.presenter.MsisdnView;
import com.tokopedia.core.service.CheckVerification;
import com.tokopedia.core.service.model.GetVerificationNumberForm;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 23/11/2015.
 */
public class PhoneVerificationDialogv4 extends DialogFragment {
    public static final String FRAGMENT_TAG = "PhoneVerificationDialog";
    public static final String USER_PHONE_NUMBER = "user_phone_number";
    public static final String VERIFICATION_NUMBER = "VERIFICATION_NUMBER";

    @Bind(R2.id.phone_verification_instruction)
    TextView phoneVerificationInstruction;
    @Bind(R2.id.refresh_number)
    TextView refreshLink;
    @Bind(R2.id.phone_number)
    TextView PhoneNumber;
    @Bind(R2.id.loading)
    ProgressBar circle;
    @Bind(R2.id.link_to_change_phone)
    TextView changePhoneNumberLink;
    @Bind(R2.id.normal_verification_footer)
    LinearLayout normalVerificationFooter;
    @Bind(R2.id.autoverify_abort_button)
    TextView abortButton;
    @Bind(R2.id.verify_button)
    TextView confirmButton;
    @Bind(R2.id.reminder_footer_layout)
    LinearLayout phoneReminderFooter;
    @Bind(R2.id.reminder_change_number)
    TextView reminderChangeNumberButton;
    @Bind(R2.id.reminder_same_number_button)
    TextView reminderSameNumberButton;

    FacadePhoneVerification facadePhoneVerification;
    LocalCacheHandler cache;

    private static final String PARAM1 = "auto_verify";
    private static final String PARAM2 = "PARAM2";

    private String phoneNumber = "";

    public static final Fragment newInstance(int param1, String param2) {
        PhoneVerificationDialogv4 dialog = new PhoneVerificationDialogv4();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM1, param1);
        bundle.putString(PARAM2, param2);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static final Fragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("auto_verify", type);
        PhoneVerificationDialogv4 phoneVerificationDialog = new PhoneVerificationDialogv4();
        phoneVerificationDialog.setArguments(bundle);
        return phoneVerificationDialog;
    }

    public PhoneVerificationDialogv4() {
        // TODO
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        int verificationType = getArguments().getInt("auto_verify");
        phoneNumber = getArguments().getString(PARAM2);
        if (verificationType == VerificationDialog.PopUpVerificationMode) {
            Log.d("MNORMANSYAH", "show pop verification !!!");
        }

        // init Facade
        facadePhoneVerification = FacadePhoneVerification.createInstance(getActivity());

        // currently disable
//        String reminderText = getString(R.string.phone_verification_reminder);
//        phoneVerificationInstruction.setText(reminderText);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.phone_verification_form, container, false);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cache = new LocalCacheHandler(getActivity(), VERIFICATION_NUMBER);
        circle.setVisibility(View.VISIBLE);

        if (cache.getString(USER_PHONE_NUMBER) != null && !TextUtils.isEmpty(cache.getString(USER_PHONE_NUMBER))) {
            phoneNumber = cache.getString(USER_PHONE_NUMBER);
        }

        if (!TextUtils.isEmpty(phoneNumber) &&
                getArguments().getInt(PARAM1) == 0) {
            phoneNumber = getArguments().getString(PARAM2);
        }

        if (!TextUtils.isEmpty(phoneNumber)) {
            PhoneNumber.setText(phoneNumber);
            circle.setVisibility(View.GONE);
        } else {
            facadePhoneVerification.getVerificationNumber(getVerificationListener());
        }
    }

    private void putToCache(int condition,  String cacheKey){
        cache.putBoolean(cacheKey, condition == 1 ? true : false);
        cache.applyEditor();
    }

    private PhoneVerificationInterfaces.getVerificationResponseListener getVerificationListener() {
        return new PhoneVerificationInterfaces.getVerificationResponseListener() {
            @Override
            public void onPhoneNumberResult(GetVerificationNumberForm dataKampret) {
                phoneNumber = dataKampret.getUserPhone();
                // [START] save number to preference
                cache.putString(USER_PHONE_NUMBER, dataKampret.getUserPhone());
                cache.applyEditor();
                // [END] save number to preference

                if(PhoneNumber != null && dataKampret.getUserPhone() != null){
                    PhoneNumber.setText(dataKampret.getUserPhone());// set number at textview
                }
                if(circle!=null)
                    circle.setVisibility(View.GONE);// dismiss dialog
                putToCache(dataKampret.getShowDialog(), CheckVerification.ALLOW_SHOP);
                putToCache(dataKampret.getIsVerified(), CheckVerification.VERIFIED);
                LocalCacheHandler verificationCache = new LocalCacheHandler(getActivity(), CheckVerification.VERIFICATION_STATUS);
                Boolean phoneVerified  = verificationCache.getBoolean(CheckVerification.VERIFIED, false);;//verificationCache.getBoolean(CheckVerification.VERIFIED, false);
                if (phoneVerified && getActivity()!=null&&getActivity() instanceof VerificationDialog.VerificationUpdate){
                    ((VerificationDialog.VerificationUpdate)getActivity()).onVerified(phoneNumber);
                }else{

                }
            }

            @Override
            public void onProcessFinished() {
                if(circle!=null)
                    circle.setVisibility(View.GONE);// dismiss dialog
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R2.id.autoverify_abort_button)
    public void onCancel() {
        dismiss();
    }

    @OnClick(R2.id.verify_button)
    public void onVerifyButton() {
        if (phoneNumber == null && TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        if (getActivity() != null && getActivity() instanceof MsisdnView) {
            ((MsisdnView) getActivity()).moveToSmsIncoming(
                    VerificationDialog.RequestChangeNumberFromProfile,
                    phoneNumber);

            PhoneVerificationDialogv4.this.dismiss();
        }
    }

    @OnClick(R2.id.link_to_change_phone)
    public void changeToManual() {
        if (getActivity() != null && getActivity() instanceof MsisdnView) {
            dismiss();// dismiss this current dialog
            ((MsisdnView) getActivity()).moveToManualPhoneVerification(VerificationDialog.RequestChangeNumberFromProfile, PhoneNumber.getText().toString());
        }
    }
}
