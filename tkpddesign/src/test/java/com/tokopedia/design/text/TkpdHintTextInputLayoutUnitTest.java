package com.tokopedia.design.text;

import android.app.Activity;

import com.tokopedia.design.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * this class demo powermock + robolectric.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config( sdk = 21)
@PrepareForTest(TkpdHintTextInputLayout.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class TkpdHintTextInputLayoutUnitTest {
    private TkpdHintTextInputLayout tkpdHintTextInputLayout;
    private ActivityController<Activity> activityController;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(Activity.class);
        tkpdHintTextInputLayout = spy(new TkpdHintTextInputLayout(activityController.get()));
    }

    @Test
    public void test5() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setCounterEnabled(true);


        boolean mCounterEnabled = Whitebox.getInternalState(tkpdHintTextInputLayout, "mCounterEnabled");
        assertTrue(mCounterEnabled);

        tkpdHintTextInputLayout.setLabel("kaakakaka");
        CharSequence mHint = Whitebox.getInternalState(tkpdHintTextInputLayout, "mHint");
        assertThat(mHint, equalTo("kaakakaka"));
    }

    @Test
    public void test2() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setCounterEnabled(true);

        boolean mCounterEnabled = Whitebox.getInternalState(tkpdHintTextInputLayout, "mCounterEnabled");
        assertTrue(mCounterEnabled);

    }

    /*
    showcase wrong powermock
     */
    @Test
    public void test1() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setCounterEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUICounter");
    }

    /*
    showcase simplify powermock
     */
    @Test
    public void test7() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setCounterEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUICounter");
    }

    /*
    even though success enabled has been set, but still the variable get reset
     */
    @Test
    public void test8() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setSuccessEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUISuccess");

        tkpdHintTextInputLayout.setSuccessEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(2))
                .invoke("setUISuccess");
    }


    @Test
    public void test3() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        Method methodToExpect = method(TkpdHintTextInputLayout.class, "setUICounter");
        doNothing().when(tkpdHintTextInputLayout, methodToExpect).withNoArguments();

        tkpdHintTextInputLayout.setCounterEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUICounter");
    }

    @Test
    public void test() throws Exception{
        assertNotNull(tkpdHintTextInputLayout);

        tkpdHintTextInputLayout.setCounterEnabled(true);

        verifyPrivate(tkpdHintTextInputLayout, times(1))
                .invoke("setUICounter");

        boolean mCounterEnabled = Whitebox.getInternalState(tkpdHintTextInputLayout, "mCounterEnabled");
        assertTrue(mCounterEnabled);

        tkpdHintTextInputLayout.setCounterEnabled(false);

        verifyPrivate(tkpdHintTextInputLayout, times(2))
                .invoke("setUICounter");

        mCounterEnabled = Whitebox.getInternalState(tkpdHintTextInputLayout, "mCounterEnabled");
        assertFalse(mCounterEnabled);

    }

}
