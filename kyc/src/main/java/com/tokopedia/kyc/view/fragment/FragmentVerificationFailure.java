package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentVerificationFailure extends BaseDaggerFragment implements
        View.OnClickListener{

    private ActivityListener activityListener;
    private TextView upgradeNow;
    private TextView returnToMainPage;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.upgrade_now){

        }
        else if(i == R.id.returnto_main_page){

        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.VERFICATION_FAILURE_SCR;
    }

    public static FragmentVerificationFailure newInstance(){
        FragmentVerificationFailure fragmentVerificationFailure = new FragmentVerificationFailure();
        return fragmentVerificationFailure;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityListener.setHeaderTitle(Constants.Values.OVO);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        activityListener = (ActivityListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.failed_verification, container, false);
        upgradeNow = view.findViewById(R.id.upgrade_now);
        returnToMainPage = view.findViewById(R.id.returnto_main_page);
        return view;
    }
}
