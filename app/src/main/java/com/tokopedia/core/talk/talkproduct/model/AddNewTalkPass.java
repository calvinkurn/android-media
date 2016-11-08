package com.tokopedia.core.talk.talkproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenfredian on 8/3/16.
 */
public class AddNewTalkPass implements Parcelable{

    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_TEXT_MESSAGE = "text_comment";
    
    
    String textMessage;
    String productID;

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.textMessage);
        dest.writeString(this.productID);
    }

    public AddNewTalkPass() {
    }

    protected AddNewTalkPass(Parcel in) {
        this.textMessage = in.readString();
        this.productID = in.readString();
    }

    public static final Creator<AddNewTalkPass> CREATOR = new Creator<AddNewTalkPass>() {
        @Override
        public AddNewTalkPass createFromParcel(Parcel source) {
            return new AddNewTalkPass(source);
        }

        @Override
        public AddNewTalkPass[] newArray(int size) {
            return new AddNewTalkPass[size];
        }
    };
    
    public Map<String,String> getAddTalkNewPass(){
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put(PARAM_PRODUCT_ID,getProductID());
        hashMap.put(PARAM_TEXT_MESSAGE,getTextMessage());
        return hashMap;
    };
}
