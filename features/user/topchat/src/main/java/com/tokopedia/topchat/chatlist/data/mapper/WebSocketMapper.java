package com.tokopedia.topchat.chatlist.data.mapper;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.topchat.chatlist.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData;
import com.tokopedia.topchat.chatlist.viewmodel.BaseChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.message.MessageViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 5/8/18.
 */
public class WebSocketMapper {

    public static final String TYPE_CHAT_RATING = "-1";
    public static final String TYPE_IMAGE_ANNOUNCEMENT = "1";
    public static final String TYPE_IMAGE_UPLOAD = "2";
    public static final String TYPE_PRODUCT_ATTACHMENT = "3";
    public static final String TYPE_IMAGE_DUAL_ANNOUNCEMENT = "4";
    public static final String TYPE_INVOICES_SELECTION = "6";
    public static final String TYPE_INVOICE_SEND = "7";
    public static final String TYPE_QUICK_REPLY = "8";

    @Inject
    public WebSocketMapper() {

    }

    public BaseChatViewModel map(String json) {
        try {
            WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

            if (pojo.getCode() == ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE) {
                return mapReplyMessage(pojo);
            } else {
                return null;
            }

        } catch (JsonSyntaxException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private boolean hasAttachment(WebSocketResponse pojo) {
        return pojo.getData() != null
                && pojo.getData().getAttachment() != null
                && pojo.getData().getAttachment().getAttributes() != null;
    }

    private BaseChatViewModel mapReplyMessage(WebSocketResponse pojo) {
        if (hasAttachment(pojo)) {
            JsonObject jsonAttributes = pojo.getData().getAttachment().getAttributes();
            return mapAttachmentMessage(pojo, jsonAttributes);
        } else {
            return convertToMessageViewModel(pojo.getData());
        }
    }

    private BaseChatViewModel mapAttachmentMessage(WebSocketResponse pojo, JsonObject jsonAttributes) {
        switch (pojo.getData().getAttachment().getType()) {
            default:
                return convertToFallBackModel(pojo.getData());
        }
    }

    private BaseChatViewModel convertToMessageViewModel(WebSocketResponseData pojo) {
        return new MessageViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                "",
                "",
                pojo.getMessage().getTimeStampUnix(),
                pojo.getStartTime(),
                pojo.getMessage().getCensoredReply(),
                false,
                false,
                !pojo.getIsOpposite()
        );
    }

    private BaseChatViewModel convertToFallBackModel(WebSocketResponseData pojo) {
        return new FallbackAttachmentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojo.getAttachment().getFallbackAttachment().getMessage(),
                pojo.getAttachment().getFallbackAttachment().getUrl(),
                pojo.getAttachment().getFallbackAttachment().getSpan(),
                pojo.getAttachment().getFallbackAttachment().getHtml()
        );
    }
}
