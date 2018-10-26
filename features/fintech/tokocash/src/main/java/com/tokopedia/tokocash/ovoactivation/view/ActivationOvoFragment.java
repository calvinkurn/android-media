package com.tokopedia.tokocash.ovoactivation.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.common.di.TokoCashComponent;
import com.tokopedia.tokocash.tracker.WalletAnalytics;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 20/09/18.
 */
public class ActivationOvoFragment extends BaseDaggerFragment {

    private Button activationNewAccountBtn;
    private Button changeNumberBtn;
    private TextView activationDesc;
    private String registeredApplink;
    private String phoneNumber;
    private String changeMsisdnApplink;

    @Inject
    WalletAnalytics walletAnalytics;

    public static ActivationOvoFragment newInstance(String registeredApplink,
                                                    String phoneNumber, String changeMsisdnApplink) {
        ActivationOvoFragment fragment = new ActivationOvoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivationOvoActivity.REGISTERED_APPLINK, registeredApplink);
        bundle.putString(ActivationOvoActivity.PHONE_NUMBER, phoneNumber);
        bundle.putString(ActivationOvoActivity.CHANGE_MSISDN_APPLINK, changeMsisdnApplink);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation_ovo, container, false);
        activationNewAccountBtn = view.findViewById(R.id.activation_ovo_btn);
        changeNumberBtn = view.findViewById(R.id.change_number_btn);
        activationDesc = view.findViewById(R.id.activation_desc);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredApplink = getArguments().getString(ActivationOvoActivity.REGISTERED_APPLINK);
        phoneNumber = getArguments().getString(ActivationOvoActivity.PHONE_NUMBER);
        changeMsisdnApplink = getArguments().getString(ActivationOvoActivity.CHANGE_MSISDN_APPLINK);

        activationDesc.setText(setContentAndBoldPhoneNumber());
        activationNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletAnalytics.eventClickMakeNewOvoAccount();
                directPageWithApplink(registeredApplink);
            }
        });
        changeNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletAnalytics.eventClickChangePhoneNumber();
                directPageWithApplink(changeMsisdnApplink);
            }
        });
    }

    private SpannableString setContentAndBoldPhoneNumber() {
        String activationDesc = String.format(getString(R.string.activation_ovo_desc), phoneNumber);
        SpannableString spannableString = new SpannableString(activationDesc);
        int endIndex = 22 + phoneNumber.length();
        spannableString.setSpan( new StyleSpan(Typeface.BOLD), 22, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void directPageWithApplink(String ApplinkSchema) {
        if (RouteManager.isSupportApplink(getActivity(), ApplinkSchema)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getActivity(), ApplinkSchema);
            startActivity(intentRegisteredApplink);
            getActivity().finish();
        }
    }
}
