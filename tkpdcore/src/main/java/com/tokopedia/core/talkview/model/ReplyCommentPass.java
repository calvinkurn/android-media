package com.tokopedia.core.talkview.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenfredian on 4/29/16.
 */
public class ReplyCommentPass implements Parcelable {

    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_TALK_ID = "talk_id";
    private static final String PARAM_TEXT_COMMENT = "text_comment";

    public ReplyCommentPass() {

    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getTalkID() {
        return talkID;
    }

    public void setTalkID(String talkID) {
        this.talkID = talkID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    String productID;
    String talkID;
    String commentContent;


    public ReplyCommentPass(Parcel in) {
        productID = in.readString();
        talkID = in.readString();
        commentContent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productID);
        dest.writeString(talkID);
        dest.writeString(commentContent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReplyCommentPass> CREATOR = new Parcelable.Creator<ReplyCommentPass>() {
        @Override
        public ReplyCommentPass createFromParcel(Parcel in) {
            return new ReplyCommentPass(in);
        }

        @Override
        public ReplyCommentPass[] newArray(int size) {
            return new ReplyCommentPass[size];
        }
    };

    public Map<String,String> getReplyParam() {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PRODUCT_ID,getProductID());
        hashMap.put(PARAM_TALK_ID,getTalkID());
        hashMap.put(PARAM_TEXT_COMMENT,getCommentContent());
        return hashMap;
    }

}