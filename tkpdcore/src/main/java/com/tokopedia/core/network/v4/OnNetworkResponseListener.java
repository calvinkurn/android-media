package com.tokopedia.core.network.v4;

import org.json.JSONObject;

import java.util.ArrayList;

import com.tkpd.library.kirisame.network.entity.NetError;

/**
 * Created by ricoharisin on 8/26/15.
 */
@Deprecated
public interface OnNetworkResponseListener {

    public void onResponse(JSONObject Response);
    public void onMessageError(ArrayList<String> MessageError);
    public void onNetworkError(NetError error, int errorCode);
}
