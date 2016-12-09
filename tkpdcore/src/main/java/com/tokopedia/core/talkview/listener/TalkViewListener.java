package com.tokopedia.core.talkview.listener;

import com.tokopedia.core.talkview.model.TalkBaseModel;

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
