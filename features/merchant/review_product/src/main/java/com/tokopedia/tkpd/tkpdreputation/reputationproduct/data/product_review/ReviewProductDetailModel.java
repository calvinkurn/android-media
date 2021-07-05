package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stevenfredian on 2/16/16.
 */
public class ReviewProductDetailModel implements Parcelable{
    public boolean isGetLikeDislike;
    public int statusLikeDislike;
    public boolean isHelpful;
    public int smiley;
    public int counterLike;
    public int counterDislike;
    public int counterResponse;

    public ReviewProductDetailModel(){
    }

    protected ReviewProductDetailModel(Parcel in) {
        isGetLikeDislike = in.readByte() != 0;
        statusLikeDislike = in.readInt();
        isHelpful = in.readByte() != 0;
        smiley = in.readInt();
        counterLike = in.readInt();
        counterDislike = in.readInt();
        counterResponse = in.readInt();
    }

    public static final Creator<ReviewProductDetailModel> CREATOR = new Creator<ReviewProductDetailModel>() {
        @Override
        public ReviewProductDetailModel createFromParcel(Parcel in) {
            return new ReviewProductDetailModel(in);
        }

        @Override
        public ReviewProductDetailModel[] newArray(int size) {
            return new ReviewProductDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isGetLikeDislike ? 1 : 0));
        dest.writeInt(statusLikeDislike);
        dest.writeByte((byte) (isHelpful ? 1 : 0));
        dest.writeInt(smiley);
        dest.writeInt(counterLike);
        dest.writeInt(counterDislike);
        dest.writeInt(counterResponse);
    }
}
