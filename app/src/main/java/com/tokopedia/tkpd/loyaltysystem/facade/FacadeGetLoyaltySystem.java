package com.tokopedia.tkpd.loyaltysystem.facade;

import android.content.Context;

import com.tokopedia.tkpd.loyaltysystem.model.LoyaltyNotification;
import com.tokopedia.tkpd.network.v4.NetworkHandler;
import com.tokopedia.tkpd.network.v4.NetworkHandlerFactory;
import com.tokopedia.tkpd.network.v4.OnNetworkResponseListener;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.tkpd.library.kirisame.network.entity.NetError;

/**
 * Created by ricoharisin on 9/21/15.
 */
public class FacadeGetLoyaltySystem {

    private Context context;

    public FacadeGetLoyaltySystem(Context context) {
        this.context = context;
    }

    public interface OnGetNotification {
        public void onResult(LoyaltyNotification notification);
        public void onNetworkError(NetError error);
        public void onError(ArrayList<String> MessageError);
    }

    public void getLoyaltyNotification(OnGetNotification listener) {
        NetworkHandler network = NetworkHandlerFactory.createBasicGetNetworkHandler(context, TkpdUrl.LOYALTY_NOTIFY);
        network.addParam("user_id", SessionHandler.getLoginID(context));
        network.addParam("shop_id", SessionHandler.getShopID(context));
        network.setOnNetworkResponseListener(getLoyaltyNotificationResponse(listener));
        network.commit();
    }

    public OnNetworkResponseListener getLoyaltyNotificationResponse(final OnGetNotification listener) {
        return new OnNetworkResponseListener() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    LoyaltyNotification model = new LoyaltyNotification();
                    model.Id = Response.getString("id");
                    model.Type = Response.getString("type");
                    JSONObject Attributes = Response.getJSONObject("attributes");
                    model.Attr.NotifyBuyer = Attributes.optInt("notify_buyer", 0) == 1;
                    model.Attr.NotifySeller = Attributes.optInt("notify_merchant", 0) == 1;
                    model.Attr.ExpiryTimeLoyalBuyer = Attributes.getString("expiry_time_lucky_merchant");
                    model.Attr.ExpiryTimeLoyalSeller = Attributes.getString("expiry_time_lucky_buyer");
                    listener.onResult(model);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessageError(ArrayList<String> MessageError) {
                listener.onError(MessageError);
            }

            @Override
            public void onNetworkError(NetError error, int errorCode) {
                listener.onNetworkError(error);

            }
        };
    }
}
