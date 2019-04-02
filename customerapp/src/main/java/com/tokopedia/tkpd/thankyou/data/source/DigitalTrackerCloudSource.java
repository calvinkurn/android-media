package com.tokopedia.tkpd.thankyou.data.source;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.Attributes;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalDataWrapper;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalRequestPayload;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class DigitalTrackerCloudSource extends ThanksTrackerCloudSource {
    private static final String KEY_TRACK_THANKYOU = "track_thankyou";
    private static final String OS_TYPE_ANDROID = "1";
    private DigitalTrackerApi digitalTrackerApi;
    private DigitalTrackerMapper digitalTrackerMapper;
    private Gson gson;
    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;

    public DigitalTrackerCloudSource(RequestParams requestParams,
                                     DigitalTrackerApi digitalTrackerApi,
                                     DigitalTrackerMapper digitalTrackerMapper,
                                     Gson gson,
                                     SessionHandler sessionHandler,
                                     GCMHandler gcmHandler) {
        super(requestParams);
        this.digitalTrackerApi = digitalTrackerApi;
        this.digitalTrackerMapper = digitalTrackerMapper;
        this.gson = gson;
        this.sessionHandler = sessionHandler;
        this.gcmHandler = gcmHandler;
    }

    @Override
    public Observable<Boolean> sendAnalytics() {
        JsonObject requestBody = new JsonParser().parse(gson.toJson(getPayload())).getAsJsonObject();

        return digitalTrackerApi.getTrackingData(requestBody).map(digitalTrackerMapper);
    }

    private DigitalDataWrapper<DigitalRequestPayload> getPayload() {
        DigitalDataWrapper<DigitalRequestPayload> data = new DigitalDataWrapper<>();
        DigitalRequestPayload payload = new DigitalRequestPayload();
        payload.setAttributes(getAttributes());
        payload.setType(KEY_TRACK_THANKYOU);
        data.setData(payload);

        return data;
    }

    private Attributes getAttributes() {
        Attributes attributes = new Attributes();
        attributes.setOrderId(Integer.parseInt(requestParams.getString(ThanksTrackerConst.Key.ID, "0")));
        attributes.setIdentifier(getIdentifier());
        return attributes;
    }

    private RequestBodyIdentifier getIdentifier() {
        RequestBodyIdentifier identifier = new RequestBodyIdentifier();
        identifier.setDeviceToken(gcmHandler.getRegistrationId());
        identifier.setOsType(OS_TYPE_ANDROID);
        identifier.setUserId(sessionHandler.getLoginID());
        return identifier;
    }
}
