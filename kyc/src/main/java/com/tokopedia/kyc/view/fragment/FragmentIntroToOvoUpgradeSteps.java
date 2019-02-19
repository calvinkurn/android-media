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
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;

public class FragmentIntroToOvoUpgradeSteps extends BaseDaggerFragment implements View.OnClickListener {
    private ActivityListener activityListener;
    private Button startUpgradeProcess;

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
            //launch camera data collection
        }
    }
}
