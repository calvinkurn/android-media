package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.util.AnalyticsUtil;
import com.tokopedia.kyc.util.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentIntroToOvoUpgradeSteps extends BaseDaggerFragment implements View.OnClickListener {
    private ActivityListener activityListener;
    private Button startUpgradeProcess;
    private TextView ovoTncLink;
    public static String TAG = "intro_to_ovo_upgrade_steps";

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.OVOUPGRADE_STEP_2_SCR;
    }

    public static FragmentIntroToOvoUpgradeSteps newInstance() {
        FragmentIntroToOvoUpgradeSteps fragmentStartUpgradeToOvo = new FragmentIntroToOvoUpgradeSteps();
        return fragmentStartUpgradeToOvo;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upgrade_ovo_process_intro, container, false);
        startUpgradeProcess = view.findViewById(R.id.start_upgrade_process);
        startUpgradeProcess.setOnClickListener(this);
        ovoTncLink = view.findViewById(R.id.ovo_tncpage_link);
        ovoTncLink.setText(MethodChecker.fromHtml(getResources().getString(R.string.ovo_tnc_text)));
        ovoTncLink.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(activityListener != null) {
            activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_upgrade_process){
            executeStartUpgrade();
        }
        else if(v.getId() == R.id.ovo_tncpage_link){
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
            String ovotncUrl = remoteConfig.getString(RemoteConfigKey.OVO_TNC_LINK);
            if(TextUtils.isEmpty(ovotncUrl)){
                ovotncUrl = Constants.URLs.OVO_TNC_PAGE;
            }
            ((KYCRouter)getContext().getApplicationContext()).actionOpenGeneralWebView(getActivity(),
                    ovotncUrl);
        }
    }

    private void executeStartUpgrade(){
        ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
            @Override
            public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                Bundle bundle = new Bundle();
                ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                if(activityListener != null) {
                    if(activityListener.getDataContatainer() != null) {
                        activityListener.getDataContatainer().setFlipCardIdImg((Boolean) dataObj.get(keysList.get(1)));
                        activityListener.getDataContatainer().setCardIdImage((String) dataObj.get(keysList.get(0)));
                    }
                    activityListener.addReplaceFragment(FragmentCardIDUpload.newInstance(bundle), true,
                            FragmentCardIDUpload.TAG);
                    activityListener.showHideActionbar(true);
                }
                UserSessionInterface userSession = new UserSession(getContext());
                AnalyticsUtil.sendEvent(getContext(),
                        AnalyticsUtil.EventName.CLICK_OVO,
                        AnalyticsUtil.EventCategory.OVO_KYC,
                        "",
                        userSession.getUserId(),
                        AnalyticsUtil.EventAction.CLK_CPTR_PIC_STP2);
            }

            @Override
            public void actionError(int actionId, Integer dataObj) {

            }
        };
        KycUtil.createKYCIdCameraFragment(getContext(),
                activityListener,
                actionCreator,
                Constants.Keys.KYC_CARDID_CAMERA,
                true);
        UserSessionInterface userSession = new UserSession(getContext());
        AnalyticsUtil.sendEvent(getContext(),
                AnalyticsUtil.EventName.CLICK_OVO,
                AnalyticsUtil.EventCategory.OVO_KYC,
                "",
                userSession.getUserId(),
                AnalyticsUtil.EventAction.CLK_MUL_STP1);
    }
}
