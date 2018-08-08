package com.tokopedia.challenges.view.activity;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.ChallengesComponentInstance;

public abstract class BaseActivity extends BaseSimpleActivity implements HasComponent<ChallengesComponent>{

    @Override
    public ChallengesComponent getComponent() {
        return ChallengesComponentInstance.getChallengesComponent(getApplication());
    }
}
