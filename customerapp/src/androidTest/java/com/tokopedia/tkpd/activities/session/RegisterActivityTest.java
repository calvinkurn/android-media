package com.tokopedia.tkpd.activities.session;

import androidx.test.runner.AndroidJUnit4;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by normansyahputa on 4/6/18.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    @Rule
    public GuessTokopediaTestRule<RegisterInitialActivity> mIntentsRule = new GuessTokopediaTestRule<RegisterInitialActivity>(
            RegisterInitialActivity.class, true, false, 3
    );

    @Test
    public void testAfterHitDiscovery() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startEmptyActivity();

        ScreenShotter.takeScreenshot("1", mIntentsRule.getActivity());
    }

    private void startEmptyActivity() {
        mIntentsRule.launchActivity(null);
    }
}
