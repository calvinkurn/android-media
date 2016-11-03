package com.tokopedia.tkpd.talkview.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenfredian on 4/12/16.
 */
public class TalkPass implements Parcelable {

    private static final String PARAM_TALK_ID = "talk_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_TEXT_MESSAGE = "text_message";
    private static final String PARAM_TALK_COMMENT_ID = "talk_comment_id";
    private static final String PARAM_COMMENT_ID = "comment_id";

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTalkCommentId() {
        return talkCommentId;
    }

    public void setTalkCommentId(String talkCommentId) {
        this.talkCommentId = talkCommentId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    int position;
    String talkCommentId;
    String commentId;
    String talkId;
    String shopId;
    String productId;
    String textMessage;

    public TalkPass(){
    }

    protected TalkPass(Parcel in) {
        position = in.readInt();
        talkCommentId = in.readString();
        commentId = in.readString();
        talkId = in.readString();
        shopId = in.readString();
        productId = in.readString();
        textMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(talkCommentId);
        dest.writeString(commentId);
        dest.writeString(talkId);
        dest.writeString(shopId);
        dest.writeString(productId);
        dest.writeString(textMessage);
    }

    @SuppressWarnings("unused")
    public static final Creator<TalkPass> CREATOR = new Creator<TalkPass>() {
        @Override
        public TalkPass createFromParcel(Parcel in) {
            return new TalkPass(in);
        }

        @Override
        public TalkPass[] newArray(int size) {
            return new TalkPass[size];
        }
    };

    public Map<String,String> getParamFollow() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PRODUCT_ID,getProductId());
        hashMap.put(PARAM_TALK_ID,getTalkId());
        hashMap.put(PARAM_SHOP_ID,getShopId());
        return hashMap;
    }

    public Map<String,String> getParamReport() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PRODUCT_ID,getProductId());
        hashMap.put(PARAM_TALK_ID,getTalkId());
        hashMap.put(PARAM_SHOP_ID,getShopId());
        hashMap.put(PARAM_TEXT_MESSAGE,getTextMessage());
        return hashMap;
    }

    public Map<String,String> getParamDelete() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_TALK_ID,getTalkId());
        hashMap.put(PARAM_SHOP_ID,getShopId());
        return hashMap;
    }

    public Map<String,String> getParamDeleteComment() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_TALK_ID,getTalkId());
        hashMap.put(PARAM_COMMENT_ID,getCommentId());
        hashMap.put(PARAM_SHOP_ID,getShopId());
        return hashMap;
    }

    public Map<String, String> getParamReportComment() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_SHOP_ID,getShopId());
        hashMap.put(PARAM_TALK_COMMENT_ID,getTalkCommentId());
        hashMap.put(PARAM_TALK_ID,getTalkId());
        hashMap.put(PARAM_TEXT_MESSAGE,getTextMessage());
        return hashMap;
    }
}