package com.tokopedia.topchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.topchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatroom.domain.pojo.common.WebSocketResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.common.WebSocketResponseData;
import com.tokopedia.topchat.chatroom.domain.pojo.imageupload.ImageUploadAttributes;
import com.tokopedia.topchat.chatroom.domain.pojo.productattachment.ProductAttachmentAttributes;
import com.tokopedia.topchat.chatroom.domain.pojo.quickreply.QuickReplyAttachmentAttributes;
import com.tokopedia.topchat.chatroom.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;

import java.util.ArrayList;
import java.util.List;

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
            case TYPE_PRODUCT_ATTACHMENT:
                return convertToProductAttachment(pojo.getData(), jsonAttributes);
            case TYPE_IMAGE_UPLOAD:
                return convertToImageUpload(pojo.getData(), jsonAttributes);
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

    private BaseChatViewModel convertToImageUpload(WebSocketResponseData pojo, JsonObject
            jsonAttribute) {
        ImageUploadAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                ImageUploadAttributes.class);

        return new ImageUploadViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                !pojo.getIsOpposite(),
                pojoAttribute.getImageUrl(),
                pojoAttribute.getThumbnail(),
                pojo.getStartTime(),
                pojo.getMessage().getCensoredReply()
        );
    }

    private BaseChatViewModel convertToProductAttachment(WebSocketResponseData pojo, JsonObject jsonAttribute) {
        ProductAttachmentAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                ProductAttachmentAttributes.class);

        return new ProductAttachmentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojoAttribute.getProductId(),
                pojoAttribute.getProductProfile().getName(),
                pojoAttribute.getProductProfile().getPrice(),
                pojoAttribute.getProductProfile().getUrl(),
                pojoAttribute.getProductProfile().getImageUrl(),
                !pojo.getIsOpposite(),
                pojo.getMessage().getCensoredReply(),
                pojo.getStartTime()
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

    private QuickReplyListViewModel convertToQuickReplyModel(WebSocketResponseData pojo, JsonObject
            jsonAttribute) {
        QuickReplyAttachmentAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                QuickReplyAttachmentAttributes.class);
        return new QuickReplyListViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getMessage().getCensoredReply(),
                pojo.getAttachment().getId(),
                TYPE_QUICK_REPLY,
                pojo.getMessage().getTimeStampUnix(),
                convertToQuickReplyList(pojoAttribute)
        );
    }

    private List<QuickReplyViewModel> convertToQuickReplyList(QuickReplyAttachmentAttributes quickReplyListPojo) {
        List<QuickReplyViewModel> list = new ArrayList<>();
        if (quickReplyListPojo != null
                && !quickReplyListPojo.getQuickReplies().isEmpty()) {
            for (QuickReplyPojo pojo : quickReplyListPojo.getQuickReplies()) {
                if (pojo.getText() != null && !TextUtils.isEmpty(pojo.getText())) {
                    list.add(new QuickReplyViewModel(pojo.getText(), pojo.getValue(), pojo.getAction()));
                }
            }
        }
        return list;
    }
}
