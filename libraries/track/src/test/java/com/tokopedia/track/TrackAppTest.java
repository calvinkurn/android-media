package com.tokopedia.track;


import android.app.Application;
import android.content.Context;

import com.tokopedia.track.interfaces.ContextAnalytics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class TrackAppTest  {

    @Test
    public void testAja(){
        TrackApp.initTrackApp((Application) RuntimeEnvironment.application.getApplicationContext());
        TrackApp trackApp = TrackApp.getInstance();
        trackApp.registerImplementation("GTM", TestAnalytics.class);
        trackApp.registerImplementation("TEST2", TestAnalytics2.class);

        MockAja mock = mock(MockAja.class);

        ((TestAnalytics2)trackApp.getValue("TEST2")).mockAja = mock;

        ((TestAnalytics)trackApp.getValue("TEST")).test1 = 2;

        Assert.assertEquals(2,  ((TestAnalytics)trackApp.getValue("TEST")).test1);

        assert trackApp.context==RuntimeEnvironment.application.getApplicationContext();

        trackApp.initializeAllApis();

        trackApp.getValue("TEST2").sendGeneralEvent(new HashMap<>());

        verify(mock).lalala();
    }

    class MockAja{
        void lalala(){}
    }

    static class TestAnalytics2 extends ContextAnalytics{

        private TrackAppTest.MockAja mockAja;

        public TestAnalytics2(Context context) {
            super(context);
        }

        public void setMockAja(MockAja mockAja) {
            this.mockAja = mockAja;
        }

        @Override
        public void sendGeneralEvent(Map<String, Object> value) {
            mockAja.lalala();
        }

        @Override
        public void sendGeneralEvent(String event, String category, String action, String label) {

        }

        @Override
        public void sendEnhanceEcommerceEvent(Map<String, Object> value) {

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

        @Override
        public void sendEvent(String eventName, Map<String, Object> eventValue) {

        }
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
        public void sendGeneralEvent(String event, String category, String action, String label) {

        }

        @Override
        public void sendEnhanceEcommerceEvent(Map<String, Object> value) {

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

        @Override
        public void sendEvent(String eventName, Map<String, Object> eventValue) {

        }
    }
}
