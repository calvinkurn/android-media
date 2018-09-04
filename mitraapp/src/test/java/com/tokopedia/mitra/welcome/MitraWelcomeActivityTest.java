package com.tokopedia.mitra.welcome;


import android.support.v4.app.Fragment;

import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.mitra.R;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class MitraWelcomeActivityTest extends TestCase {
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

    @Test
    public void shouldNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveMitraWelcomeFragment() {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.parent_view);
        assertNotNull(fragment);
        assertEquals(MitraWelcomeFragment.class.getSimpleName(), fragment.getClass().getSimpleName());
    }
}