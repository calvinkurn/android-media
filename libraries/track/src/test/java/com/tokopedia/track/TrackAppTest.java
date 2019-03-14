package com.tokopedia.track;


import android.app.Application;
import android.content.Context;

import com.tokopedia.track.interfaces.ContextAnalytics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class TrackAppTest  {

    @Test
    public void testAja(){
        TrackApp.initTrackApp((Application) RuntimeEnvironment.application.getApplicationContext());
        TrackApp trackApp = TrackApp.getInstance();
        trackApp.registerImplementation("TEST", TestAnalytics.class);

        ((TestAnalytics)trackApp.getValue("TEST")).test1 = 2;

        Assert.assertEquals(2,  ((TestAnalytics)trackApp.getValue("TEST")).test1);

        assert trackApp.context==RuntimeEnvironment.application.getApplicationContext();

        trackApp.initializeAllApis();
    }

    static class TestAnalytics extends ContextAnalytics {
        public int test1 = 1;

        public TestAnalytics(Context context) {
            super(context);
        }

        @Override
        public void initialize() {
            test1 = 1;
        }

        @Override
        public void sendGeneralEvent(Map<String, Object> value) {

        }

        @Override
        public void sendEnhanceECommerceEvent(Map<String, Object> value) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {

        }

        @Override
        public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {

        }
    }
}
