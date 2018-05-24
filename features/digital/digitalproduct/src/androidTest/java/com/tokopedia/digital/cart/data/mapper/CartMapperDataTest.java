package com.tokopedia.digital.cart.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author anggaprasetiyo on 3/29/17.
 */
public class CartMapperDataTest extends TestCase {

    private CartMapperData cartMapperData;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        cartMapperData = new CartMapperData();
    }


    public void testTransformCartInfoData() throws Exception {
        ResponseCartData responseCartData = initialFakeResponseCartData();
        CartDigitalInfoData cartDigitalInfoData =
                cartMapperData.transformCartInfoData(responseCartData);
        assertThat(cartDigitalInfoData, is(instanceOf(CartDigitalInfoData.class)));

    }

    private ResponseCartData initialFakeResponseCartData() {
        return new Gson().fromJson("{\n" +
                "  \"type\": \"cart\",\n" +
                "  \"id\": \"3045173-1-52ea79bc8eb2cf0021fbc007e4376380\",\n" +
                "  \"attributes\": {\n" +
                "    \"user_id\": 3045173,\n" +
                "    \"client_number\": \"08121781611111\",\n" +
                "    \"title\": \"Detail Pembelian\",\n" +
                "    \"category_name\": \"Pulsa\",\n" +
                "    \"operator_name\": \"Telkomsel - Simpati\",\n" +
                "    \"icon\": \"https://cdn-img.easyicon.net/png/5792/579263.gif\",\n" +
                "    \"price\": \"Rp 100.000\",\n" +
                "    \"price_plain\": 100000,\n" +
                "    \"instant_checkout\": false,\n" +
                "    \"need_otp\": true,\n" +
                "    \"sms_state\": \"recharge_blacklist\",\n" +
                "    \"user_input_price\": null,\n" +
                "    \"main_info\": [\n" +
                "      {\n" +
                "        \"label\": \"\",\n" +
                "        \"value\": \"08121781611111\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"label\": \"\",\n" +
                "        \"value\": \"Telkomsel - Simpati 100.000\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"label\": \"\",\n" +
                "        \"value\": \"Rp 100.000\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"additional_info\": []\n" +
                "  },\n" +
                "  \"relationships\": {\n" +
                "    \"category\": {\n" +
                "      \"data\": {\n" +
                "        \"type\": \"category\",\n" +
                "        \"id\": \"1\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"operator\": {\n" +
                "      \"data\": {\n" +
                "        \"type\": \"operator\",\n" +
                "        \"id\": \"12\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"product\": {\n" +
                "      \"data\": {\n" +
                "        \"type\": \"product\",\n" +
                "        \"id\": \"70\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", ResponseCartData.class);
    }

}