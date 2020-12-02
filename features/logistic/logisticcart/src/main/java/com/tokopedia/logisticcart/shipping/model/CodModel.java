package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fajarnuha on 26/12/18.
 */
public class CodModel implements Parcelable {

    private boolean isCod;
    private int counterCod;
    private String messageInfo;
    private String messageLink;
    private String messageLogo;

    public CodModel() {
    }

    public CodModel(boolean isCod, int counterCod, String messageInfo, String messageLink, String messageLogo) {
        this.isCod = isCod;
        this.counterCod = counterCod;
        this.messageInfo = messageInfo;
        this.messageLink = messageLink;
        this.messageLogo = messageLogo;
    }

    protected CodModel(Parcel in) {
        isCod = in.readByte() != 0;
        counterCod = in.readInt();
        messageInfo = in.readString();
        messageLink = in.readString();
        messageLogo = in.readString();
    }

    public static final Creator<CodModel> CREATOR = new Creator<CodModel>() {
        @Override
        public CodModel createFromParcel(Parcel in) {
            return new CodModel(in);
        }

        @Override
        public CodModel[] newArray(int size) {
            return new CodModel[size];
        }
    };

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    public int getCounterCod() {
        return counterCod;
    }

    public void setCounterCod(int counterCod) {
        this.counterCod = counterCod;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessageLink() {
        return messageLink;
    }

    public void setMessageLink(String messageLink) {
        this.messageLink = messageLink;
    }

    public String getMessageLogo() {
        return messageLogo;
    }

    public void setMessageLogo(String messageLogo) {
        this.messageLogo = messageLogo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isCod ? 1 : 0));
        dest.writeInt(counterCod);
        dest.writeString(messageInfo);
        dest.writeString(messageLink);
        dest.writeString(messageLogo);
    }
}
