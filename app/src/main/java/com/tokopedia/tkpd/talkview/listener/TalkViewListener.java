package com.tokopedia.tkpd.talkview.listener;

import android.os.Bundle;

import com.tokopedia.tkpd.talkview.model.TalkBaseModel;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by stevenfredian on 4/28/16.
 */
public interface TalkViewListener {
    void successGet(JSONObject result);

    void showError(String error);

    void successReply(String string);

    void errorReply(String error);

    String getTalkID();

    String getProductID();

    String getCommentContent();

    String getShopID();

    String getTextMessage();

    void onStateResponse(List<TalkBaseModel> list, int position, int page, boolean hasNext);
}
