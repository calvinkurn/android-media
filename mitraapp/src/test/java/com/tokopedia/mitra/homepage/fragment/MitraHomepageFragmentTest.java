package com.tokopedia.mitra.homepage.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.mitra.BaseFragmentTest;
import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.TestMitraApplication;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestMitraApplication.class, manifest = "TestAndroidManifest.xml")
public class MitraHomepageFragmentTest extends BaseFragmentTest {

    private ActivityController<MitraParentHomepageActivity> activityController;
    private MitraParentHomepageActivity activity;
    MitraHomepageFragment fragment;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraParentHomepageActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
        fragment = MitraHomepageFragment.newInstance();
        startFragment(activity, fragment);
        fragment.presenter = spy(fragment.presenter);
    }

    @After
    public void tearDown() throws Exception {
        activityController.pause().stop().destroy();
    }

    @Test
    public void showLoginContainer() {
        fragment.showLoginContainer();
        LinearLayout loginLayout = fragment.getView().findViewById(R.id.layout_login);
        assertTrue(loginLayout.getVisibility() == View.VISIBLE);
    }

    @Test
    public void hideLoginContainer() {
        fragment.hideLoginContainer();
        LinearLayout loginLayout = fragment.getView().findViewById(R.id.layout_login);
        assertTrue(loginLayout.getVisibility() == View.GONE);
    }

    @Test
    public void loginButtonClick_unloggin_navigateToLoginPage() {
        AppCompatButton loginBtn = fragment.getView().findViewById(R.id.btn_login);
        loginBtn.performClick();
        verify(fragment.presenter).onLoginBtnClicked();
    }

    @Test
    public void navigateToLoginPage_unloggin_navigateToLoginPage() {
        fragment.navigateToLoginPage();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent expectedIntent = new Intent(activity, LoginPhoneNumberActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}