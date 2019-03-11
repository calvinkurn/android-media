package com.tokopedia.kyc.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.util.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.view.interfaces.GenericOperationsView;
import com.tokopedia.kyc.view.interfaces.LoaderUiListener;
import com.tokopedia.kyc.view.interfaces.UpgradeToOvoContract;
import com.tokopedia.kyc.view.presenter.EligibilityCheckPresenter;

import javax.inject.Inject;

public class FragmentUpgradeToOvo extends BaseDaggerFragment
        implements UpgradeToOvoContract.View, View.OnClickListener, GenericOperationsView<EligibilityBase> {

    private ActivityListener activityListener;
    private LoaderUiListener loaderUiListener;
    private Button proceedWithUpgrade;
    private Button upgradeLater;
    public static String TAG = "start_upgrade";
    private Snackbar errorSnackbar;

    @Inject
    EligibilityCheckPresenter eligibilityCheckPresenter;

    @Override
    protected void initInjector() {
        getComponent(KYCComponent.class).inject(this);
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

    public static FragmentUpgradeToOvo newInstance() {
        FragmentUpgradeToOvo fragmentUpgradeToOvo = new FragmentUpgradeToOvo();
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
        eligibilityCheckPresenter.attachView(this);
    }


    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        activityListener = (ActivityListener)context;
        loaderUiListener = (LoaderUiListener)context;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.upgrade_btn) {
            makeEligibilityRequest();
        } else if (i == R.id.later_btn) {
            getActivity().finish();
        } else if(i == R.id.btn_ok){
            makeEligibilityRequest();
            if(errorSnackbar.isShownOrQueued()) errorSnackbar.dismiss();
        }
    }

    private void makeEligibilityRequest(){
        eligibilityCheckPresenter.makeEligibilityRequest();
    }

    @Override
    public void success(EligibilityBase data) {
        FragmentIntroToOvoUpgradeSteps fragmentIntroToOvoUpgradeSteps =
                FragmentIntroToOvoUpgradeSteps.newInstance();
        activityListener.getDataContatainer().setKycReqId(data.getGoalKYCRequest().getKycRequestId());
        activityListener.addReplaceFragment(fragmentIntroToOvoUpgradeSteps,
                true, FragmentIntroToOvoUpgradeSteps.TAG);
    }

    @Override
    public void failure(EligibilityBase data) {
        if(activityListener.isRetryValid()) {
            String errorMessage = data.
                    getGoalKYCRequest().getErrors().get(0).get(Constants.Keys.MESSAGE);
            if(TextUtils.isEmpty(errorMessage)){
                errorMessage = Constants.ErrorMsg.SOMETHING_WENT_WRONG;
            }
            errorSnackbar = KycUtil.createErrorSnackBar(getActivity(), FragmentUpgradeToOvo.this::onClick, errorMessage);
            errorSnackbar.show();
        }
        else {
            getActivity().finish();
        }
    }

    @Override
    public void showHideProgressBar(boolean showProgressBar) {
        if(showProgressBar){
            loaderUiListener.showProgressDialog();
        }
        else {
            loaderUiListener.hideProgressDialog();
        }
    }

    @Override
    public void onDestroy() {
        eligibilityCheckPresenter.detachView();
        super.onDestroy();
    }
}
