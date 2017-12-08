package com.tokopedia.tkpd.thankyou.data.source;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.thankyou.data.mapper.MarketplaceTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.source.api.MarketplaceTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    private MarketplaceTrackerApi marketplaceTrackerApi;
    private MarketplaceTrackerMapper mapper;
    private Gson gson;

    public MarketplaceTrackerCloudSource(RequestParams requestParams,
                                         MarketplaceTrackerApi marketplaceTrackerApi,
                                         MarketplaceTrackerMapper mapper,
                                         Gson gson) {
        super(requestParams);
        this.marketplaceTrackerApi = marketplaceTrackerApi;
        this.mapper = mapper;
        this.gson = gson;
    }

    @Override
    public Observable<String> sendAnalytics() {
        return marketplaceTrackerApi.getTrackingData(getRequestPayload()).map(mapper);
    }

    private String getRequestPayload() {
        return "{\n" +
                "  payment(payment_id:"+requestParams.getString(ThanksTrackerConst.Key.ID, "0")+"){\n" +
                "    payment_id\n" +
                "    payment_ref_num\n" +
                "    orders{\n" +
                "    \torder_id\n" +
                "    \tphone\n" +
                "    }\n" +
                "    partial {\n" +
                "    \tamount\n" +
                "    \tgateway{\n" +
                "      \t  gateway_name\n" +
                "          gateway_img_url\n" +
                "          gateway_id\n" +
                "      \t}\n" +
                "    }\n" +
                "    payment_method{\n" +
                "      method\n" +
                "      instant{\n" +
                "        gateway{\n" +
                "      \t  gateway_name\n" +
                "          gateway_img_url\n" +
                "          gateway_id\n" +
                "      \t}\n" +
                "      }\n" +
                "      transfer{\n" +
                "      \tdestination_name\n" +
                "      \tdestination_account\n" +
                "      \tsource_account\n" +
                "      \tsource_name\n" +
                "      \tgateway{\n" +
                "      \t  gateway_name\n" +
                "          gateway_img_url\n" +
                "          gateway_id\n" +
                "      \t}\n" +
                "      }\n" +
                "      defer{\n" +
                "      \tgateway{\n" +
                "      \t  gateway_name\n" +
                "          gateway_img_url\n" +
                "          gateway_id\n" +
                "      \t}\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
//        return "{payment(payment_id:"
//                + requestParams.getString(ThanksTrackerConst.Key.ID, "0")
//                + "){"
//                + gson.toJsonTree(new PaymentData()).toString()
//                + "}}";
    }
}
