package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.model.CardIdDataKeyProvider;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentIntroToOvoUpgradeSteps extends BaseDaggerFragment implements View.OnClickListener {
    private ActivityListener activityListener;
    private Button startUpgradeProcess;
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
        activityListener = (ActivityListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upgrade_ovo_process_intro, container, false);
        startUpgradeProcess = view.findViewById(R.id.start_upgrade_process);
        startUpgradeProcess.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityListener.setHeaderTitle(Constants.Values.OVOUPGRADE_STEP_2_TITLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_upgrade_process){
            ActionCreator<HashMap<String, Object>, Integer> actionCreator = new ActionCreator<HashMap<String, Object>, Integer>() {
                @Override
                public void actionSuccess(int actionId, HashMap<String, Object> dataObj) {
                    Bundle bundle = new Bundle();
                    ArrayList<String> keysList = (new CardIdDataKeyProvider()).getData(1, null);
                    bundle.putString(FragmentCardIDUpload.CARDID_IMG_PATH, (String) dataObj.get(keysList.get(0)));
                    bundle.putBoolean(FragmentCardIDUpload.FLAG_IMG_FLIP, (Boolean) dataObj.get(keysList.get(1)));
                    bundle.putParcelable(Constants.Keys.CONFIRM_DATA_REQ_CONTAINER,
                            getArguments().getParcelable(Constants.Keys.CONFIRM_DATA_REQ_CONTAINER));
                    activityListener.addReplaceFragment(FragmentCardIDUpload.newInstance(bundle), true,
                            FragmentCardIDUpload.TAG);
                    activityListener.showHideActionbar(true);
                }

                @Override
                public void actionError(int actionId, Integer dataObj) {

                }
            };
            KycUtil.createKYCIdCameraFragment(getContext(), activityListener, actionCreator, Constants.Keys.KYC_CARDID_CAMERA);
        }
    }
}
