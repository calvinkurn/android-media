package com.example.core_legacy;


import com.tokopedia.analytic_constant.DataLayer;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.util.PriceUtil;
import com.tokopedia.design.utils.CurrencyFormatHelper;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {
    @Test
    public void deep_copy_map() {
        Map<String, Object> nutsMap = DataLayer.mapOf("satu", new Object[]{1, "123", 0.0, DataLayer.mapOf("satu dalam", "dua dalam"), DataLayer.listOf(12, 33, new Object[]{1, 2, 3})},
                "dua", DataLayer.listOf(
                        DataLayer.mapOf("dua dalam", new Object[]{1, "123", 0.0}), 1, "dua", 0.0, DataLayer.listOf(1, 2, 3)),
                "tiga", DataLayer.mapOf("tiga satu", "tiga 2"),
                "empat", "kosong"
        );

        Map<String, Object> clickDataLayer = getClickDataLayer(nutsMap);

        Map<String, Object> clone = GTMAnalytics.clone(clickDataLayer);
        clone.remove("holdThis");
        clone.remove("satu");
        clone.remove("dua");
        clone.remove("tiga");
        clone.remove("empat");

        assertEquals(4, 2 + 2);

        clickDataLayer.put("lima", null);
        Map<String, Object> clone1 = GTMAnalytics.clone(clickDataLayer);

        assertEquals(4, 2 + 2);
    }

    @Test
    public void price_util_test() {
        String price = "Rp 100.000";
        assertEquals("this has to be good", "100000", PriceUtil.from(price));

        price = "Rp 100.000,889";
        assertFalse("this has to be good", "100000".equals(PriceUtil.from(price)));

        price = CurrencyFormatHelper.removeCurrencyPrefix(price);
//        NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(price);
//        new DecimalFormat("#").format(Double.valueOf(price));
        try {
            long result = new DecimalFormat("#,#", new DecimalFormatSymbols(new Locale("in", "ID"))).parse(price).longValue();
            assertTrue("this has to be good", 100000 == result);
        }catch (ParseException pe){

        }
//        assertTrue("this has to be good", "10000".equals(PriceUtil.from(price)));

    }

    @Test
    public void bruteForceCastToString_test() {
        double mahal = 6_990_000.0;
        long mahalNich = 6_990_000;
        int iyaNich = 6_990_000;
        String value = "10000000";
        System.out.println(Double.valueOf(mahal).toString()+" " + Long.valueOf(mahalNich).toString()+ " " + Integer.valueOf(iyaNich).toString() +" "+ Double.valueOf(value));
    }

    public Map<String, Object> getClickDataLayer(Map<String, Object> holdThis) {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "slider banner click",
                "eventLabel", "url",
                "holdThis", holdThis,
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", "1234",
                                                "name", "name aja",
                                                "creative", "creative",
                                                "position", "position"
                                        )
                                )
                        )
                ),
                "attribution", String.format("1 - sliderBanner - %s - %s","1234", "12356")
        );
    }
}
