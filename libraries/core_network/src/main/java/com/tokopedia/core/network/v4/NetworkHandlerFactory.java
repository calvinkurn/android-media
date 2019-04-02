package com.tokopedia.core.network.v4;

import android.content.Context;

import com.tkpd.library.kirisame.network.entity.VolleyNetwork;

/**
 * Created by ricoharisin on 8/25/15.
 */
@Deprecated
public class NetworkHandlerFactory {

    public static NetworkHandler createNetworkHandlerAuth(Context context, String url) {
        return new NetworkHandlerAuth(context, url);
    }

    public static NetworkHandler createBasicPostNetworkHandler(Context context, String url) {
        NetworkHandler network = new NetworkHandler(context, url);
        network.setMethod(VolleyNetwork.METHOD_POST);
        return network;
    }

    public static NetworkHandler createBasicGetNetworkHandler(Context context, String url) {
        NetworkHandler network = new NetworkHandler(context, url);
        network.setMethod(VolleyNetwork.METHOD_GET);
        return network;
    }
}
