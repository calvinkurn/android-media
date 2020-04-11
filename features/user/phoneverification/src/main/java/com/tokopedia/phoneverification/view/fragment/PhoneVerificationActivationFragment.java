package com.tokopedia.phoneverification.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.phoneverification.R;

/**
 * Created by nisie on 2/27/17.
 */

public class PhoneVerificationActivationFragment extends BaseDaggerFragment {
    TextView protectAccountText;

    public static PhoneVerificationActivationFragment createInstance() {
        return new PhoneVerificationActivationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verification_activation, parent, false);
        protectAccountText = (TextView) view.findViewById(R.id.protect_account_text);
        prepareView();
        return view;

    }

    @Override
    protected void initInjector() {

    }

    private void prepareView() {
        Spannable spannable = new SpannableString(getString(R.string.protect_your_account_with_phone_verification));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setFakeBoldText(true);
                              }
                          }
                , getString(R.string.protect_your_account_with_phone_verification).indexOf("melakukan verifikasi nomor ponsel")
                , getString(R.string.protect_your_account_with_phone_verification).length()
                , 0);

        protectAccountText.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected String getScreenName() {
        return PhoneVerificationConst.SCREEN_PHONE_VERIFICATION;
    }
}
