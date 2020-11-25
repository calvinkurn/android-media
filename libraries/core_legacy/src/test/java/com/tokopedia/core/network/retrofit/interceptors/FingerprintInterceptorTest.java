package com.tokopedia.core.network.retrofit.interceptors;

import org.junit.Test;

import static com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor.trimGoogleAdId;
import static org.junit.Assert.assertEquals;

public class FingerprintInterceptorTest {
    @Test
    public void trimGoogleAdId_isCorrect() {
        String input = "00000000-0000-0000-0000-000000000000";
        String result = trimGoogleAdId(input);
        assertEquals("", input, result);

        for(int cc=2013;cc<=2015;cc++) {
            char ccc = (char) Integer.parseInt(String.valueOf(cc), 16);
            String text = String.valueOf(ccc);
            assertEquals("","-", trimGoogleAdId(text));
        }
    }
}
