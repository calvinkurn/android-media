package com.tokopedia.kyc;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

public class UpgradeToOvoFragment extends BaseDaggerFragment
        implements UpgradeToOvoContract.View, View.OnClickListener{

    private ActivityListener activityListener;
    private Button proceedWithUpgrade;
    private Button upgradeLater;
    private String TAG_START_UPGRADE = "start_upgrade";

    @Override
    protected void initInjector() {
        getComponent(UpgradeOvoComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return Constants.Values.OVOUPGRADE_STEP_1_SCR;
    }

    @Override
    public void showSnackbarErrorMessage(String message) {

    }

    @Override
    public String getErrorMessage(Throwable e) {
        return null;
    }

    public static UpgradeToOvoFragment newInstance() {
        UpgradeToOvoFragment fragmentUpgradeToOvo = new UpgradeToOvoFragment();
        return fragmentUpgradeToOvo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upgrade_ovo, container, false);
        proceedWithUpgrade = view.findViewById(R.id.upgrade_btn);
        upgradeLater = view.findViewById(R.id.later_btn);
        proceedWithUpgrade.setOnClickListener(this::onClick);
        upgradeLater.setOnClickListener(this::onClick);
        return view;
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.upgrade_btn) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.parent_view,IntroToOvoUpgradeStepsFragment.newInstance(), TAG_START_UPGRADE);
            fragmentTransaction.addToBackStack(TAG_START_UPGRADE);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (i == R.id.later_btn) {
            getActivity().finish();
        }
    }
}
