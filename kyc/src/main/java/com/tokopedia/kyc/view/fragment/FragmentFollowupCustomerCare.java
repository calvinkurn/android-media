package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentFollowupCustomerCare extends BaseDaggerFragment implements
        View.OnClickListener{
    private ActivityListener activityListener;
    private ImageView callAction;
    private ImageView emailAction;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.call){

        }
        else if(i == R.id.email){

        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_CC_FOLLOWUP_SCR;
    }

    public static FragmentFollowupCustomerCare newInstance(){
        FragmentFollowupCustomerCare fragmentFollowupCustomerCare = new FragmentFollowupCustomerCare();
        return fragmentFollowupCustomerCare;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
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
        View view = inflater.inflate(R.layout.application_inprocess_notifier, container, false);
        callAction = view.findViewById(R.id.call);
        callAction.setOnClickListener(this::onClick);
        emailAction = view.findViewById(R.id.email);
        emailAction.setOnClickListener(this::onClick);
        return view;
    }
}
