package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.util.AnalyticsUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

public class FragmentErrorKyc extends BaseDaggerFragment implements
        View.OnClickListener{
    private ActivityListener activityListener;
    private TextView errorDescription;
    private TextView tryAgain;
    private String TAG = "error";

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.try_again){
            executeTryAgain();
        }
        else if(i == R.id.cancel_btn){
            executeBackToApp();
        }
    }

    private void executeTryAgain(){
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack(FragmentUpgradeToOvo.TAG, 0);
        }
        UserSessionInterface userSession = new UserSession(getContext());
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_CBLG_STLGGL);
    }


    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.KYC_ERROR_SCR;
    }

    public static FragmentErrorKyc newInstance(){
        FragmentErrorKyc fragmentErrorUpload = new FragmentErrorKyc();
        return fragmentErrorUpload;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(activityListener != null) {
            activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
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
        View view = inflater.inflate(R.layout.error_kyc, container, false);
        errorDescription = view.findViewById(R.id.error_desc);
        tryAgain = view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(this::onClick);
        return view;
    }

    private void executeBackToApp(){
        getActivity().finish();
        UserSessionInterface userSession = new UserSession(getContext());
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_BATALKAN_TKPD);
    }

}
