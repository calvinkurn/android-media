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
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.interfaces.UpgradeToOvoContract;

import rx.Subscriber;

public class FragmentUpgradeToOvo extends BaseDaggerFragment
        implements UpgradeToOvoContract.View, View.OnClickListener{

    private ActivityListener activityListener;
    private Button proceedWithUpgrade;
    private Button upgradeLater;
    private String TAG = "start_upgrade";

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
            KycUtil.executeEligibilityCheck(getContext(), getEligibilityCheckSubscriber());
        } else if (i == R.id.later_btn) {
            getActivity().finish();
        }
    }

    private Subscriber getEligibilityCheckSubscriber(){
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                EligibilityBase eligibilityBase = graphqlResponse.getData(EligibilityBase.class);
                if(eligibilityBase != null && eligibilityBase.getGoalKYCRequest().getKycRequestId() > 0){
                    KycUtil.saveDataToPersistentStore(getContext(), Constants.Keys.KYC_REQ_ID,
                            eligibilityBase.getGoalKYCRequest().getKycRequestId());

                    FragmentIntroToOvoUpgradeSteps fragmentIntroToOvoUpgradeSteps =
                            FragmentIntroToOvoUpgradeSteps.newInstance();
                    ConfirmRequestDataContainer confirmRequestDataContainer = new ConfirmRequestDataContainer();
                    confirmRequestDataContainer.setKycReqId(eligibilityBase.getGoalKYCRequest().getKycRequestId());
                    fragmentIntroToOvoUpgradeSteps.setArguments(KycUtil.getConfirmReqDataContainerBundle(confirmRequestDataContainer));
                    activityListener.addReplaceFragment(fragmentIntroToOvoUpgradeSteps,
                            true, FragmentIntroToOvoUpgradeSteps.TAG);
                }
            }
        };
    }
}
