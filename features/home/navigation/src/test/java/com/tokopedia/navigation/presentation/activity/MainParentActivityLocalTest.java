package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.DeepLinkActivity;
import com.tokopedia.ShadowTaskStackBuilder;
import com.tokopedia.applink.ApplinkConst;
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
import org.robolectric.shadows.ShadowActivity;

import java.lang.reflect.Field;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(application = HomeApp.class, manifest = "AndroidTestManifest.xml", shadows = {ShadowTaskStackBuilder.class})
@RunWith(RobolectricTestRunner.class)
public class MainParentActivityLocalTest {

    private MainParentActivity mActivity;
    private ActivityController<MainParentActivity> mActivityController;

    @Before
    public void setup() throws Exception{

        mActivity = (mActivityController = Robolectric.buildActivity(MainParentActivity.class)).get();
    }

    @Test
    public void testIntent(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApplinkConst.HOME));

        DeepLinkActivity deepLinkActivity = Robolectric.buildActivity(DeepLinkActivity.class, intent)
                .create().get();

        ShadowActivity shadowActivity = shadowOf(deepLinkActivity);
        Intent launchedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
        assertThat(launchedIntent.getComponent(),
                equalTo(new ComponentName(deepLinkActivity, MainParentActivity.class)));
    }

    @Test
    public void skip_onboarding() throws Exception{
        assert mActivity != null;

        mActivityController.create();

        MainParentPresenter mActivityPresenter = null;
        mActivityController.get().setPresenter(mActivityPresenter = spy(mActivityController.get().getPresenter()));

        when(mActivityPresenter.isFirstTimeUser()).thenReturn(false);

        mActivityController.start();

        verify(mActivityPresenter, times(1)).isFirstTimeUser();

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

        verify(mActivityPresenter, times(1)).isFirstTimeUser();

        assertEquals(mActivityController.get().isFinishing(), true);
    }
}
