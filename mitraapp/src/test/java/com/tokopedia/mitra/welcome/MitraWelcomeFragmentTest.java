package com.tokopedia.mitra.welcome;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;

import com.tokopedia.mitra.BaseFragmentTest;
import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class MitraWelcomeFragmentTest extends BaseFragmentTest {
    private ActivityController<MitraWelcomeActivity> activityController;
    private MitraWelcomeActivity activity;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraWelcomeActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
    }

    @After
    public void tearDown() throws Exception {
        activityController.pause().stop().destroy();
    }

    @Test
    public void shouldSetTheSubmitButtonFragment() {
        MitraWelcomeFragment fragment = MitraWelcomeFragment.newInstance();
        startFragment(activity, fragment);

        AppCompatButton continueTextView = (AppCompatButton) fragment.getView().findViewById(R.id.btn_continue);
        assertEquals(activity.getString(R.string.mitra_welcome_button_text), continueTextView.getText());
    }

    @Test
    public void shouldNavigateToHomePageActivityWhenContinueButtonClicked() {
        //given
        MitraWelcomeFragment fragment = MitraWelcomeFragment.newInstance();
        startFragment(activity, fragment);
        AppCompatButton continueTextView = (AppCompatButton) fragment.getView().findViewById(R.id.btn_continue);
        //when
        continueTextView.performClick();
        //then
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent expectedIntent = new Intent(activity, MitraParentHomepageActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
//        ShadowIntent shadowIntent = Shadows.shadowOf(actualIntent);
//        assertEquals(MitraHomepageActivity.class, shadowIntent.getIntentClass());

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}