package com.tokopedia.tkpd.facade;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.loyaltysystem.model.LoyaltyNotification;
import com.tokopedia.tkpd.network.NetworkHandlerJSON;
import com.tokopedia.tkpd.network.v4.OnNetworkResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nope.yuuji.kirisame.network.entity.NetError;
import nope.yuuji.kirisame.network.entity.VolleyNetwork;

/**
 * Created by Nisie on 17/11/15.
 */
public class FacadeLuckyNotification {

    private Context context;

    public FacadeLuckyNotification(Context context) {
        this.context = context;
    }

    public interface OnGetNotification {
        public void onSuccess(LoyaltyNotification notification);

        public void onEmpty();
    }

    public void getLoyaltyNotification(JSONObject loyalty, OnGetNotification listener) throws JSONException {
        String url = loyalty.getString("url");
//        String url = "https://clover.tokopedia.com/badge/member/extend/v1";
//        String url = "http://192.168.60.100:8008/badge/member/extend/v1";
        NetworkHandlerJSON network = new NetworkHandlerJSON(context, url);
        network.setMethodJSON(VolleyNetwork.METHOD_POST);
        network.addParamJSON("data", loyalty.getJSONObject("data"));
        network.setOnNetworkResponseListener(onGetNotification(listener));
        network.commit();
    }

    private OnNetworkResponseListener onGetNotification(final OnGetNotification listener) {
        return new OnNetworkResponseListener() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    listener.onSuccess(parseNotif(Response));
                } catch (JSONException e) {
                    listener.onEmpty();
                }
            }

            @Override
            public void onMessageError(ArrayList<String> MessageError) {
                CommonUtils.dumper("NISTAG : " + MessageError);
                listener.onEmpty();
            }

            @Override
            public void onNetworkError(NetError error, int errorCode) {
                CommonUtils.dumper("NISTAG : " + errorCode);
                listener.onEmpty();
            }
        };
    }

    private LoyaltyNotification parseNotif(JSONObject result) throws JSONException {
        LoyaltyNotification notif = new LoyaltyNotification();
        notif.Id = result.getString("id");
        notif.Type = result.getString("type");
        JSONObject attributes = result.getJSONObject("attributes");
        JSONObject words = attributes.getJSONObject("words");
        notif.Attr.NotifyBuyer = words.optInt("notify_buyer", 0) == 1;
        notif.Attr.NotifySeller = words.optInt("notify_seller", 0) == 1;
        notif.Attr.ExpiryTimeLoyalBuyer = getString(words, "expiry_time_lucky_buyer");
        notif.Attr.ExpiryTimeLoyalSeller = getString(words, "expiry_time_lucky_seller");
        notif.Attr.contentBuyer1 = getString(words,"content_buyer_1");
        notif.Attr.contentBuyer2 = getString(words,"content_buyer_2");
        notif.Attr.contentBuyer3 = getString(words,"content_buyer_3");
        notif.Attr.contentSeller1 = getString(words,"content_merchant_1");
        notif.Attr.contentSeller2 = getString(words,"content_merchant_2");
        notif.Attr.contentSeller3 = getString(words,"content_merchant_3");
        notif.Attr.url = words.getString("link");
        return notif;
    }

    private String getString(JSONObject words, String expiry_time_lucky_buyer) {
        try {
            return words.getString(expiry_time_lucky_buyer);
        }catch (JSONException e){
            return "";
        }
    }

}