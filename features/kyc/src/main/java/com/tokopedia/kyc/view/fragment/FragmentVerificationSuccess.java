package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class FragmentVerificationSuccess extends BaseDaggerFragment implements
        View.OnClickListener{

    private ActivityListener activityListener;
    private Button continueShopping;
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.continue_shopping){
            getActivity().finish();
        }
    }

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.VERFICATION_SUCCESS_SCR;
    }

    public static FragmentVerificationSuccess newInstance(){
        FragmentVerificationSuccess fragmentVerificationSuccess = new FragmentVerificationSuccess();
        return fragmentVerificationSuccess;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.successful_verification, container, false);
        continueShopping = view.findViewById(R.id.continue_shopping);
        continueShopping.setOnClickListener(this::onClick);
        return view;
    }
}
