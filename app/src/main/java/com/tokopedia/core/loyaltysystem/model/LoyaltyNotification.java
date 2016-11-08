package com.tokopedia.core.loyaltysystem.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ricoharisin on 9/21/15.
 */
public class LoyaltyNotification implements Parcelable{

    public String Id;
    public String Type;
    public Attributes Attr = new Attributes();

    protected LoyaltyNotification(Parcel in) {
        Id = in.readString();
        Type = in.readString();
        Attr = in.readParcelable(Attributes.class.getClassLoader());
    }

    public LoyaltyNotification() {
    }

    public static final Creator<LoyaltyNotification> CREATOR = new Creator<LoyaltyNotification>() {
        @Override
        public LoyaltyNotification createFromParcel(Parcel in) {
            return new LoyaltyNotification(in);
        }

        @Override
        public LoyaltyNotification[] newArray(int size) {
            return new LoyaltyNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Type);
        dest.writeParcelable(Attr, flags);
    }


    public static class Attributes implements Parcelable {
        public Boolean NotifyBuyer;
        public Boolean NotifySeller;
        public String ExpiryTimeLoyalBuyer;
        public String ExpiryTimeLoyalSeller;
        public String contentBuyer3;
        public String contentBuyer2;
        public String contentBuyer1;
        public String url;
        public String contentSeller1;
        public String contentSeller2;
        public String contentSeller3;

        protected Attributes(Parcel in) {
            NotifyBuyer = in.readByte() != 0;
            NotifySeller = in.readByte() != 0;
            ExpiryTimeLoyalBuyer = in.readString();
            ExpiryTimeLoyalSeller = in.readString();
            contentBuyer3 = in.readString();
            contentBuyer2 = in.readString();
            contentBuyer1 = in.readString();
            url = in.readString();
            contentSeller1 = in.readString();
            contentSeller2 = in.readString();
            contentSeller3 = in.readString();
        }

        public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
            @Override
            public Attributes createFromParcel(Parcel in) {
                return new Attributes(in);
            }

            @Override
            public Attributes[] newArray(int size) {
                return new Attributes[size];
            }
        };

        public Attributes() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (NotifyBuyer ? 1 : 0));
            dest.writeByte((byte) (NotifySeller ? 1 : 0));
            dest.writeString(ExpiryTimeLoyalBuyer);
            dest.writeString(ExpiryTimeLoyalSeller);
            dest.writeString(contentBuyer3);
            dest.writeString(contentBuyer2);
            dest.writeString(contentBuyer1);
            dest.writeString(url);
            dest.writeString(contentSeller1);
            dest.writeString(contentSeller2);
            dest.writeString(contentSeller3);
        }
    }

}


