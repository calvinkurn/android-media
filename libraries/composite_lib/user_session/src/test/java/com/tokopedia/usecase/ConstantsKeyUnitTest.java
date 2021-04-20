package com.tokopedia.usecase;

import com.tokopedia.user.session.Constants;
import com.tokopedia.user.session.util.EncoderDecoder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConstantsKeyUnitTest {

    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String GTM_LOGIN_ID = "GTM_LOGIN_ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    private static final String EMAIL = "EMAIL";
    private static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    private static final String IS_AFFILIATE = "is_affiliate";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String GC_TOKEN = "GC_TOKEN";

    private static final String TEMP_USER_ID = "temp_login_id";
    private static final String TEMP_EMAIL = "TEMP_EMAIL";
    private static final String TEMP_PHONE_NUMBER = "TEMP_PHONE_NUMBER";
    private static final String TEMP_NAME = "TEMP_NAME";

    private static final String GCM_STORAGE = "GCM_STORAGE";
    private static final String GCM_ID = "gcm_id";

    private static final String LOGIN_UUID_KEY = "LOGIN_UUID";
    private static final String UUID_KEY = "uuid";

    private static final String SHOP_ID = "SHOP_ID";
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String SHOP_AVATAR = "SHOP_AVATAR";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    private static final String IS_POWER_MERCHANT_IDLE = "IS_POWER_MERCHANT_IDLE";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String KEY_IV = "tokopedia1234567";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String IS_FIRST_TIME_USER = "IS_FIRST_TIME";
    private static final String IS_FIRST_TIME_USER_NEW_ONBOARDING = "IS_FIRST_TIME_NEW_ONBOARDING";
    private static final String HAS_PASSWORD = "HAS_PASSWORD";
    private static final String HAS_SHOWN_SALDO_WARNING = "HAS_SHOWN_SALDO_WARNING";
    private static final String HAS_SHOWN_SALDO_INTRO_PAGE = "HAS_SHOWN_SALDO_INTRO_PAGE";
    private static final String AUTOFILL_USER_DATA = "AUTOFILL_USER_DATA";
    private static final String LOGIN_METHOD = "LOGIN_METHOD";
    public static final String GCM_ID_TIMESTAMP = "gcm_id_timestamp";
    public static final String IS_SHOP_OFFICIAL_STORE = "IS_SHOP_OFFICIAL_STORE";

    public static final String ANDROID_ID = "ANDROID_ID";
    public static final String KEY_ANDROID_ID = "KEY_ANDROID_ID";

    public static final String ADVERTISINGID = "ADVERTISINGID";
    public static final String KEY_ADVERTISINGID = "KEY_ADVERTISINGID";

    private static final String IS_SHOP_OWNER = "IS_SHOP_OWNER";
    private static final String IS_SHOP_ADMIN = "IS_SHOP_ADMIN";
    private static final String IS_LOCATION_ADMIN = "IS_LOCATION_ADMIN";
    private static final String IS_MULTI_LOCATION_SHOP = "IS_MULTI_LOCATION_SHOP";

    /**
     * Twitter Prefs
     */
    private static final String TWITTER_ACCESS_TOKEN = "TWITTER_ACCESS_TOKEN";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "TWITTER_ACCESS_TOKEN_SECRET";
    private static final String TWITTER_SHOULD_POST = "TWITTER_SHOULD_POST";

    public static final String[] KEYS = {
            ACCESS_TOKEN, TOKEN_TYPE, REFRESH_TOKEN, LOGIN_ID, IS_LOGIN,
            LOGIN_SESSION, SHOP_ID, GC_TOKEN, IS_GOLD_MERCHANT, FULL_NAME,
            PROFILE_PICTURE, TEMP_EMAIL, TEMP_PHONE_NUMBER,
            IS_MSISDN_VERIFIED, HAS_SHOWN_SALDO_WARNING, HAS_SHOWN_SALDO_INTRO_PAGE,
            PHONE_NUMBER, EMAIL, HAS_PASSWORD,
            SHOP_AVATAR, IS_POWER_MERCHANT_IDLE, AUTOFILL_USER_DATA, LOGIN_METHOD,
            TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET, TWITTER_SHOULD_POST, GTM_LOGIN_ID,
            TEMP_USER_ID, GCM_ID, IS_AFFILIATE, IS_FIRST_TIME_USER_NEW_ONBOARDING,
            UUID_KEY, LOGIN_UUID_KEY, GCM_ID_TIMESTAMP, IS_SHOP_OFFICIAL_STORE,
            ANDROID_ID, KEY_ANDROID_ID, ADVERTISINGID, KEY_ADVERTISINGID, IS_SHOP_OWNER,
            IS_SHOP_ADMIN, IS_LOCATION_ADMIN, IS_MULTI_LOCATION_SHOP
    };

    @Test
    public void compareAndDecryptKeys() {

        final String KEY_PEMBUKA = "tokopedia1234567";

        for(int i=0; i< KEYS.length; i++){
            String result = EncoderDecoder.Decrypt(Constants.ENCRYPTED_KEYS[i], KEY_PEMBUKA);
            String expectedResult = KEYS[i];

            assertEquals(result, expectedResult);
        }
    }

    @Test
    public void encryptKeys() {
        final String KEY_PEMBUKA = "tokopedia1234567";

        for(int i=0;i<KEYS.length;i++){
            String result = EncoderDecoder.Encrypt(KEYS[i], KEY_PEMBUKA);
            System.out.println("public static final String  "+KEYS[i]+ " = \""+result+"\";");
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}