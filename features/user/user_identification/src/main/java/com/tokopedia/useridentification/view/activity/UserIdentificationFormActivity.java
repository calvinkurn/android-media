package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.abstraction.base.view.model.StepperModel;
import com.tokopedia.useridentification.view.fragment.UserIdentificationFormFaceFragment;
import com.tokopedia.useridentification.view.fragment.UserIdentificationFormKtpFragment;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormActivity extends BaseStepperActivity {

    private List<Fragment> fragmentList;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserIdentificationFormActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA);
        } else {
            createNewStepperModel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
    }

    private StepperModel createNewStepperModel(){
        return new UserIdentificationStepperModel();
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(UserIdentificationFormKtpFragment.createInstance());
            fragmentList.add(UserIdentificationFormFaceFragment.createInstance());
            return fragmentList;
        } else {
            return fragmentList;
        }
    }
}
