package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

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

    protected StepperModel stepperModel;
    private RoundCornerProgressBar progressStepper;
    int currentPosition = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressStepper = (RoundCornerProgressBar) findViewById(R.id.stepper_progress);
        progressStepper.setMax(getListFragment().size());
        progressStepper.setProgress(currentPosition);
        updateToolbarTitle();
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        if (getListFragment().size() >= currentPosition) {
            Fragment fragment = getListFragment().get(currentPosition - 1);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.parent_view, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_stepper_toolbar;
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
        currentPosition--;
        progressStepper.setProgress(currentPosition);
        setupFragment(null);
        updateToolbarTitle();
    }

    @Override
    public void onBackPressed() {
        onBackEvent();
    }

    private void onBackEvent() {
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
}
