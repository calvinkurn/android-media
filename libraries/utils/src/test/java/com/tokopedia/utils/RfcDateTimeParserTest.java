package com.tokopedia.utils;

import com.tokopedia.utils.time.RfcDateTimeParser;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class RfcDateTimeParserTest {

    @Test
    public void parseRfc3339ReturnAsExpected() {
        String time = "2020-03-25T17:58:49+07:00";
        Date dateActual = RfcDateTimeParser.parseDateString(time, RfcDateTimeParser.RFC_3339);
        Calendar actual = Calendar.getInstance();
        actual.setTime(dateActual);

        assertEquals(2020, actual.get(Calendar.YEAR));
        assertEquals(2, actual.get(Calendar.MONTH)); // starts from 0
        assertEquals(25, actual.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void parseDateStringUnitTestAndProdReturnsSame() {
        String timeUnitTest = "2020-03-25T17:58:49+0700";
        String timeProd = "2020-03-25T17:58:49+07:00";

        Date actual = RfcDateTimeParser.parseDateString(timeUnitTest, RfcDateTimeParser.RFC_3339);
        Date actual2 = RfcDateTimeParser.parseDateString(timeProd, RfcDateTimeParser.RFC_3339);
        assertEquals(actual, actual2);
    }
}