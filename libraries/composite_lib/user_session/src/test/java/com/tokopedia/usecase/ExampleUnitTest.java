package com.tokopedia.usecase;

import com.google.gson.Gson;
import com.tokopedia.user.session.Constants;
import com.tokopedia.user.session.util.EncoderDecoder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void encryptKeys() {

        final String KEY_PEMBUKA = "tokopedia1234567";


        for (String key : Constants.KEYS) {
            String result = EncoderDecoder.Decrypt(key, KEY_PEMBUKA);

            System.out.println(result);
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}