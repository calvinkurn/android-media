package com.tokopedia.challenges.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.ChallengesComponentInstance;

public abstract class BaseActivity extends BaseSimpleActivity implements HasComponent<ChallengesComponent> {

    @Override
    public ChallengesComponent getComponent() {
        return ChallengesComponentInstance.getChallengesComponent(getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserSession userSession = ((AbstractionRouter) this.getApplication()).getSession();
        if (!userSession.isLoggedIn()) {
            navigateToLoginPage();
        }
    }

    private void navigateToLoginPage() {
        Intent intent = ((ChallengesModuleRouter) (this.getApplication())).getLoginIntent(this);
        startActivity(intent);
        finish();
    }
}
