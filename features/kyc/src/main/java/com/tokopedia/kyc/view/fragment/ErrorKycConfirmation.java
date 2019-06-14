package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class ErrorKycConfirmation extends BaseDaggerFragment
        implements View.OnClickListener {
    private ActivityListener activityListener;
    public static String TAG = "error_kyc_confirm";
    private Button tryAgain;
    private Button cancelBtn;
    private TextView errorDesc;

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {

        return Constants.Values.ERROR_KYC_CONFIRM;
    }

    public static ErrorKycConfirmation newInstance() {
        ErrorKycConfirmation errorKycConfirmation = new ErrorKycConfirmation();
        return errorKycConfirmation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activityListener != null) {
            activityListener.showHideActionbar(false);
        }
        setErrorDesc();
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActivityListener) {
            activityListener = (ActivityListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.try_again) {
            if (activityListener != null) {
                activityListener.addReplaceFragment(FragmentUpgradeToOvo.newInstance(), true, FragmentUpgradeToOvo.TAG);
            }
        } else if (i == R.id.cancel_btn) {
            getActivity().finish();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_kyc, container, false);
        tryAgain = view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(this::onClick);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this::onClick);
        errorDesc = view.findViewById(R.id.error_desc);
        return view;
    }

    private void setErrorDesc() {
        if (getArguments() != null) {
            String msg = getArguments().getString(Constants.Keys.MESSAGE, "");
            if (!TextUtils.isEmpty(msg)) {
                errorDesc.setText(msg);
            }
        }
    }
}
