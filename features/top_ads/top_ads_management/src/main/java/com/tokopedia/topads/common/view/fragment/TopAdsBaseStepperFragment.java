package com.tokopedia.topads.common.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordStepperModel;

/**
 * Created by normansyahputa on 11/20/17.
 */

public abstract class TopAdsBaseStepperFragment<T extends TopAdsKeywordStepperModel> extends BaseDaggerFragment {
    protected T stepperModel;
    protected StepperListener stepperListener;

    protected void onNextClicked() {

        if (stepperModel == null) {
            initiateStepperModel();
        }
        saveStepperModel(stepperModel);
        goToNextPage();
    }

    protected abstract void saveStepperModel(T stepperModel);

    protected abstract void initiateStepperModel();

    protected abstract void goToNextPage();

    protected abstract void populateView(T stepperModel);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null)
            stepperModel = getArguments().getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (stepperModel != null) {
            populateView(stepperModel);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }
}
