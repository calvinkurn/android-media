package com.tokopedia.loginregister.registerinitial.view.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author by nisie on 28/10/18.
 */
public class CustomPhoneNumberUtilTest {

    @Test
    public void transformFromCommonPhoneNumber() {
        String input = "08123456789";
        String expected = "0812-3456-789";
        String output;

        output = CustomPhoneNumberUtil.transform(input);
        assertEquals(expected, output);
    }

    @Test
    public void transformFromCountryCodePhoneNumber() {
        String input = "628123456789";
        String expected = "0812-3456-789";
        String output;

        output = CustomPhoneNumberUtil.transform(input);
        assertEquals(expected, output);

    }

    @Test
    public void transformFromCountryCodePlusPhoneNumber() {
        String input = "+628123456789";
        String expected = "0812-3456-789";
        String output;

        output = CustomPhoneNumberUtil.transform(input);
        assertEquals(expected, output);
    }


}