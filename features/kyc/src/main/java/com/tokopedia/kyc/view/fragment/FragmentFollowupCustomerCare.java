package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.util.AnalyticsUtil;
import com.tokopedia.kyc.util.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

public class FragmentFollowupCustomerCare extends BaseDaggerFragment implements
        View.OnClickListener{
    private ActivityListener activityListener;
    private TextView callAction;
    private ImageView emailAction;
    private Button backToApp;
    public static String TAG = "cc_follow_up";
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.call){
            executeCall();
        }
        else if(i == R.id.email){
            executeEmail();
        }
        else if(i == R.id.back_to_app){
            executeBackToApp();
        }
    }

    private void executeCall(){
        KycUtil.makeCall(getContext());
        UserSessionInterface userSession = new UserSession(getContext());
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_PHN);
    }

    private void executeEmail(){
        KycUtil.sendEmail(getContext());
        UserSessionInterface userSession = new UserSession(getContext());

        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_EML);
    }

    private void executeBackToApp(){
        getActivity().finish();
        PersistentCacheManager.instance.put("reload_webview", 1);
        UserSessionInterface userSession = new UserSession(getContext());
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_KMBL_TKPD);
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
        View view = inflater.inflate(R.layout.application_inprocess_notifier, container, false);
        callAction = view.findViewById(R.id.call);
        callAction.setOnClickListener(this::onClick);
        emailAction = view.findViewById(R.id.email);
        emailAction.setOnClickListener(this::onClick);
        backToApp = view.findViewById(R.id.back_to_app);
        backToApp.setOnClickListener(this::onClick);
        return view;
    }
}
