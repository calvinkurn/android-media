package com.tokopedia.design.text;

import android.app.Activity;

import com.tokopedia.design.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class TkpdHintTextInputLayoutTest2 {
    private TkpdHintTextInputLayout tkpdHintTextInputLayout;

    @Before
    public void setUp() throws Exception {
        ActivityController<Activity> activityController = Robolectric.buildActivity(Activity.class);
        tkpdHintTextInputLayout = spy(new TkpdHintTextInputLayout(activityController.get()));
    }

    @Test
    public void test() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);
        tkpdHintTextInputLayout.setCounterEnabled(true);
        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUICounter");
    }

}
