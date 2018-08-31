package com.tokopedia.mitra.welcome;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;

import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.activity.MitraHomepageActivity;

import junit.framework.TestCase;

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
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.FragmentTestUtil;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class MitraWelcomeFragmentTest extends TestCase {
    private ActivityController<MitraWelcomeActivity> activityController;
    private MitraWelcomeActivity activity;

    @Override
    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(MitraWelcomeActivity.class);
        activity = activityController.create().start().postCreate(null).resume().visible().get();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        activityController.pause().stop().destroy();
    }

    public void startFragment(AppCompatActivity parentActivity, Fragment fragment) {
        FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();
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
        Intent expectedIntent = new Intent(activity, MitraHomepageActivity.class);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
//        ShadowIntent shadowIntent = Shadows.shadowOf(actualIntent);
//        assertEquals(MitraHomepageActivity.class, shadowIntent.getIntentClass());

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}