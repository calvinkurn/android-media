package com.tokopedia.topchat.chatroom.domain;

import android.os.CountDownTimer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.attachproduct.resultmodel.ResultProduct;
import com.tokopedia.topchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkAttributePojo;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkPojo;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.common.InboxChatConstant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author by StevenFredian on 24/04/18.
 */

public class WebSocketUseCase {

    private ChatWebSocketListenerImpl listener;
    private CountDownTimer countDownTimer;
    private OkHttpClient client;
    private String url;
    private WebSocket ws;
    private UserSession userSession;


    public WebSocketUseCase(String magicString, UserSession session, ChatWebSocketListenerImpl
            listener) {
        this.client = new OkHttpClient();
        this.url = magicString;
        this.userSession = session;
        this.listener = listener;
        this.countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };
        createWebSocket();
    }

    private void createWebSocket() {
        Request request = new Request.Builder().url(url)
                .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                .header("Accounts-Authorization",
                        "Bearer " + userSession.getAccessToken())
                .header("x-app-version",String.valueOf(GlobalConfig.VERSION_CODE))
                .header("x-device", "android-" + GlobalConfig.VERSION_NAME)
                .header("x-tkpd-app-version","android-" + GlobalConfig.VERSION_NAME)
                .header("x-tkpd-app-name", GlobalConfig.getPackageApplicationName())
                .build();
        ws = client.newWebSocket(request, listener);
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }

    public void closeConnection() {
        try {
            client.dispatcher().executorService().shutdown();
            ws.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe() {
        countDownTimer.cancel();
    }

    public void execute(JsonObject json) {
        ws.send(json.toString());
    }

    public JsonObject getParamSendProductAttachment(String messageId, ResultProduct product, String startTime) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", product.getProductUrl());

        data.addProperty("start_time", startTime);
        data.addProperty("attachment_type", Integer.parseInt(WebSocketMapper
                .TYPE_PRODUCT_ATTACHMENT));
        data.addProperty("product_id", product.getProductId());

        JsonObject productProfile = new JsonObject();
        productProfile.addProperty("name", product.getName());
        productProfile.addProperty("price", product.getPrice());
        productProfile.addProperty("image_url", product.getProductImageThumbnail());
        productProfile.addProperty("url", product.getProductUrl());
        data.add("product_profile", productProfile);
        json.add("data", data);
        return json;
    }

    public JsonObject getParamSendReply(String messageId, String reply, String startTime) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", reply);
        data.addProperty("start_time", startTime);
        json.add("data", data);
        return json;
    }

    public JsonObject getParamSendImage(String messageId, String path, String startTime) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", InboxChatConstant.UPLOADING);
        data.addProperty("start_time", startTime);
        data.addProperty("file_path", path);
        data.addProperty("attachment_type", Integer.parseInt(WebSocketMapper.TYPE_IMAGE_UPLOAD));
        json.add("data", data);
        return json;
    }

    public JsonObject getReadMessage(String messageId) {

        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamStartTyping(String messageId) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamStopTyping(String messageId) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

}
