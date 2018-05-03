package com.tokopedia.tokocash.autosweepmf.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.view.fragment.SetAutoSweepLimitFragment;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;

/**
 * This screen will be responsible for setting the limit of auto sweep
 * and upon successful operation it will broadcast the messages
 * It will taking the current tokocash balance as an extras param
 */
public class SetAutoSweepLimitActivity extends BaseSimpleActivity implements HasComponent<TokoCashComponent> {

    private TokoCashComponent tokoCashComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.mf_action_enable_autosweep));
    }

    @Override
    protected Fragment getNewFragment() {
        return SetAutoSweepLimitFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    public static Intent getCallingIntent(Context context, Bundle extras) {
        return new Intent(context, SetAutoSweepLimitActivity.class).putExtras(extras);
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }
}
