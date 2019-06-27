package com.tokopedia.tkpd.rule;

import android.app.Activity;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.CacheGetFingerprintUseCase;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.di.SessionModule;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.tkpd.ConsumerMainApplication;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by normansyahputa on 4/6/18.
 */

public class GuessTokopediaTestRule<T extends Activity> extends BaseTokopediaTestRule<T> {

    public static final int SESSION_URL_ACCOUNTS_DOMAIN = 0;
    public static final int SESSION_URL_BASE_DOMAIN = 1;
    public static final int TKPD_BASE_URL_ACCOUNTS_DOMAIN = 2;
    private UiDevice device;

    public GuessTokopediaTestRule(Class<T> activityClass, int numOfServer) {
        super(activityClass, numOfServer);
    }

    public GuessTokopediaTestRule(Class<T> activityClass, boolean initialTouchMode, int numOfServer) {
        super(activityClass, initialTouchMode, numOfServer);
    }

    public GuessTokopediaTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity, int numOfServer) {
        super(activityClass, initialTouchMode, launchActivity, numOfServer);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new GuessTokopediaStatement(super.apply(base, description), mockWebServers);
    }

    public UiDevice getDevice() {
        return device;
    }

    private class GuessTokopediaStatement extends TokopediaStatement {

        public GuessTokopediaStatement(Statement base, MockWebServer[] mockWebServers) {
            super(base, mockWebServers);
        }

        /**
         * never call super.evaluate().
         *
         * @throws Throwable
         */
        @Override
        public void evaluate() throws Throwable {

            device = UiDevice.getInstance(getInstrumentation());

            SessionUrl.ACCOUNTS_DOMAIN = getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).url("/").toString();
            SessionUrl.BASE_DOMAIN = getIndexMockWebServer(SESSION_URL_BASE_DOMAIN).url("/").toString();
            TkpdBaseURL.ACCOUNTS_DOMAIN = getIndexMockWebServer(TKPD_BASE_URL_ACCOUNTS_DOMAIN).url("/").toString();

            ConsumerMainApplication application = (ConsumerMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();

            new GlobalCacheManager().deleteAll();

            SessionHandler.clearUserData(application);

            // prevent auto complete textview in here
            new LocalCacheHandler(application, SessionModule.LOGIN_CACHE).clearCache(SessionModule.LOGIN_CACHE);


            /**
             * This cause bug "Terjadi kesalahan koneksi, because
             * Terjadi kesalahan koneksi : different base64 implementation.
             * EncoderDecoder.Decrypt(SessionHandler.getRefreshToken(context), SessionHandler.getRefreshTokenIV(context))
             */
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                SessionHandler sessionHandler = new SessionHandler(application);
                sessionHandler.setToken("lalala",
                        "zzzzz",
                        EncoderDecoder.Encrypt("xxxx",
                                SessionHandler.getRefreshTokenIV(application)));
            }

            // fingerprint problem's
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(application, CacheGetFingerprintUseCase.FINGERPRINT_KEY_NAME);
            localCacheHandler.putString(CacheGetFingerprintUseCase.FINGERPRINT_KEY_NAME, getBaseJsonFactory().convertFromAndroidResource("fingerprint.json"));
            localCacheHandler.applyEditor();

            mBase.evaluate();

            new GlobalCacheManager().deleteAll();

            SessionHandler.clearUserData(application);
        }
    }
}