package com.tokopedia.challenges.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.ChallengesComponentInstance;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

public abstract class ChallengesBaseActivity extends BaseSimpleActivity implements HasComponent<ChallengesComponent> {

    protected static final int LOGIN_REQUEST_CODE = 23;

    @Override
    public ChallengesComponent getComponent() {
        return ChallengesComponentInstance.getChallengesComponent(getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserSessionInterface userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            navigateToLoginPage();
        }
        if (!checkFirebaseEnable()) {
            finish();
        }
    }

    private void navigateToLoginPage() {
        Intent intent = ((ChallengesModuleRouter) (this.getApplication())).getLoginIntent(this);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!checkFirebaseEnable()) {
            finish();
        }
        if (requestCode == LOGIN_REQUEST_CODE && resultCode != RESULT_OK) {
            finish();
        }

    }

    private boolean checkFirebaseEnable() {
        return (((ChallengesModuleRouter) getApplicationContext()).getBooleanRemoteConfig("app_enable_indi_challenges", true));
    }
}
