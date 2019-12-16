package com.tokopedia.usecase;

import com.google.gson.Gson;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.util.EncoderDecoder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void encryptKeys(){
        Gson gson = new Gson();

        KEY_TAMPUNG key_tampung = new KEY_TAMPUNG();


        for(String key :UserSession.KEYS){
            String result = EncoderDecoder.Encrypt(key, KEY_TAMPUNG.KEY_PEMBUKA);



            assertEquals(EncoderDecoder.Decrypt(result, KEY_TAMPUNG.KEY_PEMBUKA), key);

            key_tampung.keyList.add(new KEY_TAMPUNG.KEY(key, result));
        }

        System.out.println(gson.toJson(key_tampung));
        System.out.println(gson.toJson(key_tampung));

    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public static class KEY_TAMPUNG {
        public static final String KEY_PEMBUKA = "tokopedia1234567";
        public List<KEY> keyList = new ArrayList<>();

        public static class KEY{
            String originalKey;
            String encryptKey;

            public KEY(String originalKey, String encryptKey) {
                this.originalKey = originalKey;
                this.encryptKey = encryptKey;
            }
        }
    }
}