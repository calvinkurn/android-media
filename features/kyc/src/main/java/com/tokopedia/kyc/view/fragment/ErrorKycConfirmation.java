package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class ErrorKycConfirmation extends BaseDaggerFragment
        implements View.OnClickListener{
    private ActivityListener activityListener;
    public static String TAG = "error_kyc_confirm";
    private Button tryAgain;

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
        if(activityListener != null) {
            activityListener.setHeaderTitle(Constants.Values.OVO);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if(context instanceof ActivityListener) {
            activityListener = (ActivityListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.try_again){
            if(activityListener != null) {
                activityListener.addReplaceFragment(FragmentUpgradeToOvo.newInstance(), true, FragmentUpgradeToOvo.TAG);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_kyc, container, false);
        tryAgain = view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(this::onClick);
        return view;
    }
}
