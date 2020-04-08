package com.tokopedia.navigation.presentation.activity;

import com.tokopedia.home.HomeApp;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.powermock.api.mockito.PowerMockito.when;

@Config(application = HomeApp.class, manifest = "AndroidTestManifest.xml")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class MainParentActivityLocalTestPowerMock {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private MainParentActivity mActivity;
    private ActivityController<MainParentActivity> mActivityController;

    @Before
    public void setup() throws Exception{

        /**
         * powermockito can only test "class within this module" not outside.
         */
        UserSessionInterface userSession = PowerMockito.mock(UserSessionInterface.class);
        when(userSession.isFirstTimeUser()).thenReturn(false);

        mActivity = (mActivityController = Robolectric.buildActivity(MainParentActivity.class)).get();
        mActivity.setUserSession(userSession);
    }


//    @Test
//    public void testAja() throws Exception{
//        assert mActivity != null;
//
//        /**
//         * this shows that HomeApp cannot be casted.
//         */
//        mActivityController.create().start().resume();
//    }
}
