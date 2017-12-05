package com.tokopedia.tkpd.home.thankyou.data.source;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;
import com.tokopedia.tkpd.home.thankyou.data.pojo.digital.Attributes;
import com.tokopedia.tkpd.home.thankyou.data.pojo.digital.DigitalRequestPayload;
import com.tokopedia.tkpd.home.thankyou.data.source.api.DigitalThanksApi;
import com.tokopedia.tkpd.home.thankyou.domain.model.ThanksAnalyticsConst;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/5/17.
 */

public class DigitalAnalyticsCloudSource extends ThanksAnalyticsCloudSource {
    private static final String KEY_TRACK_THANKYOU = "track_thankyou";
    private DigitalThanksApi digitalThanksApi;
    private SessionHandler sessionHandler;
    private Gson gson;

    public DigitalAnalyticsCloudSource(RequestParams requestParams,
                                       DigitalThanksApi digitalThanksApi,
                                       Gson gson,
                                       SessionHandler sessionHandler) {
        super(requestParams);
        this.digitalThanksApi = digitalThanksApi;
        this.gson = gson;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<String> sendAnalytics() {
        JsonObject payload = new JsonParser().parse(gson.toJson(getPayload())).getAsJsonObject();

        return digitalThanksApi.getPaymentData(payload).map(new Func1<Response<String>, String>() {
            @Override
            public String call(Response<String> response) {
                return "";
            }
        });
    }

    private DigitalRequestPayload getPayload() {
        DigitalRequestPayload digitalRequestPayload = new DigitalRequestPayload();
        digitalRequestPayload.setAttributes(getAttributes());
        digitalRequestPayload.setType(KEY_TRACK_THANKYOU);

        return digitalRequestPayload;
    }

    private Attributes getAttributes() {
        Attributes attributes = new Attributes();
        attributes.setOrderId(requestParams.getString(ThanksAnalyticsConst.Key.ID, ""));
        attributes.setIdentifier(getIdentifier());
        return attributes;
    }

    private RequestBodyIdentifier getIdentifier() {
        RequestBodyIdentifier identifier = new RequestBodyIdentifier();
        identifier.setDeviceToken(sessionHandler.getAccessToken());
        identifier.setOsType("1");
        identifier.setUserId(sessionHandler.getLoginID());
        return identifier;
    }
}
