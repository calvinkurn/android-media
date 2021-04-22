package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.listener.StepperListener;
import com.tokopedia.abstraction.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/27/17.
 */

public abstract class BaseStepperActivity extends BaseToolbarActivity implements StepperListener {
    public static final String STEPPER_MODEL_EXTRA = "STEPPER_MODEL_EXTRA";
    private static final String CURRENT_POSITION_EXTRA = "current_position";

    protected StepperModel stepperModel;
    private RoundCornerProgressBar progressStepper;
    protected int currentPosition = 1;
    protected int oldcurrentPosition = -1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION_EXTRA);
        }
        super.onCreate(savedInstanceState);
        progressStepper = (RoundCornerProgressBar) findViewById(getProgressBar());
        progressStepper.setMax(getListFragment().size());
        progressStepper.setProgress(currentPosition);
        updateToolbarTitle();
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        if (getListFragment().size() >= currentPosition) {
            Fragment fragment = getListFragment().get(currentPosition - 1);
            Bundle fragmentArguments = fragment.getArguments();
            Bundle bundle;
            if( null == fragmentArguments){
                bundle = new Bundle();
            }else {
                bundle = fragmentArguments;
            }
            bundle.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(getParentView(), fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION_EXTRA, currentPosition);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_stepper;
    }

    @Override
    public void goToNextPage(StepperModel stepperModel) {
        incrementPage();
        this.stepperModel = stepperModel;
        setupFragment(null);
    }

    private void incrementPage() {
        currentPosition++;
        progressStepper.setProgress(currentPosition);
        updateToolbarTitle();
    }

    @Override
    public void finishPage() {
        finish();
    }

    private void decrementPage() {
        if(oldcurrentPosition>0) {
            currentPosition = oldcurrentPosition;
            oldcurrentPosition = -1;
        } else {
            currentPosition--;
        }
        progressStepper.setProgress(currentPosition);
        setupFragment(null);
        updateToolbarTitle();
    }

    public void getToFragment(int pos, StepperModel stepperModel) {
        this.stepperModel = stepperModel;
        oldcurrentPosition = currentPosition;
        currentPosition = pos;
        progressStepper.setProgress(currentPosition);
        setupFragment(null);
        updateToolbarTitle();
    }

    @Override
    public void onBackPressed() {
//        getSupportFragmentManager().popBackStackImmediate();
        onBackEvent();
    }

    protected void onBackEvent() {
        if (currentPosition > 1) {
            decrementPage();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackEvent();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    protected abstract List<Fragment> getListFragment();


    public void updateToolbarTitle() {
        getSupportActionBar().setTitle(getString(R.string.top_ads_label_stepper, currentPosition, getListFragment().size()));
    }
    public int getParentView() { return com.tokopedia.abstraction.R.id.parent_view; }

    public int getProgressBar(){
        return R.id.stepper_progress;
    }

    public void updateToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setMaxProgressStepper(float maxProgress) {
        progressStepper.setMax(maxProgress);
    }

    public void refreshCurrentProgressStepper(){
        progressStepper.setProgress(currentPosition);
    }
}
