package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;

import com.tokopedia.home.HomeApp;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.user.session.UserSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.controller.ComponentController;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Config(application = HomeApp.class, manifest = "AndroidTestManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MainParentActivityLocalTest {

    private MainParentActivity mActivity;
    private ActivityController<MainParentActivity> mActivityController;

    @Before
    public void setup() throws Exception{

        mActivity = (mActivityController = Robolectric.buildActivity(MainParentActivity.class)).get();
    }

    @Test
    public void skip_onboarding() throws Exception{
        assert mActivity != null;

        mActivityController.create();

        MainParentPresenter mActivityPresenter = null;
        mActivityController.get().setPresenter(mActivityPresenter = spy(mActivityController.get().getPresenter()));

        when(mActivityPresenter.isFirstTimeUser()).thenReturn(false);

        mActivityController.start().resume();

        assertEquals(mActivityController.get().isFinishing(), false);
    }

    @Test
    public void show_onboarding() throws Exception{
        assert mActivity != null;

        mActivityController.create();

        MainParentPresenter mActivityPresenter = null;
        mActivityController.get().setPresenter(mActivityPresenter = spy(mActivityController.get().getPresenter()));

        when(mActivityPresenter.isFirstTimeUser()).thenReturn(true);

        mActivityController.start();

        assertEquals(mActivityController.get().isFinishing(), true);
    }
}
