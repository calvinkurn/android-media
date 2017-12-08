package com.tokopedia.tkpd.thankyou.data.source;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.Attributes;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalRequestPayload;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/5/17.
 */

public class DigitalTrackerCloudSource extends ThanksTrackerCloudSource {
    private static final String KEY_TRACK_THANKYOU = "track_thankyou";
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
    public Observable<String> sendAnalytics() {
        JsonObject payload = new JsonParser().parse(gson.toJson(getPayload())).getAsJsonObject();

        return digitalTrackerApi.getTrackingData(payload).map(digitalTrackerMapper);
    }

    private DigitalRequestPayload getPayload() {
        DigitalRequestPayload digitalRequestPayload = new DigitalRequestPayload();
        digitalRequestPayload.setAttributes(getAttributes());
        digitalRequestPayload.setType(KEY_TRACK_THANKYOU);

        return digitalRequestPayload;
    }

    private Attributes getAttributes() {
        Attributes attributes = new Attributes();
        attributes.setOrderId(requestParams.getString(ThanksTrackerConst.Key.ID, ""));
        attributes.setIdentifier(getIdentifier());
        return attributes;
    }

    private RequestBodyIdentifier getIdentifier() {
        RequestBodyIdentifier identifier = new RequestBodyIdentifier();
        identifier.setDeviceToken(gcmHandler.getRegistrationId());
        identifier.setOsType("1");
        identifier.setUserId(sessionHandler.getLoginID());
        return identifier;
    }
}
