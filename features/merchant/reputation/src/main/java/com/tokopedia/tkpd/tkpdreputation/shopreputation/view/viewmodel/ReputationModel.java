package com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 10/21/2015.
 */
public class ReputationModel implements Parcelable {

    public String reviewId;
    public Boolean Editable;
    public String username;
    public String userId;
    public String avatarUrl;
    public String date;
    public String comment;
    public String userLabel;
    public int starQuality;
    public int starAccuracy;
    public int smiley;
    public String counterSmiley;
    public int counterLike;
    public int counterDislike;
    public int counterResponse;
    public String shopName;
    public String shopId;
    public String shopAvatarUrl;
    public String responseMessage;
    public String responseDate;
    public String userNameResponder;
    public String avatarUrlResponder;
    public String labelIdResponder;
    public String userLabelResponder;
    public String userIdResponder;
    public String productId;
    public String productName;
    public String shopReputation;
    public int typeMedal;
    public int levelMedal;
    public boolean isGetLikeDislike;
    public int statusLikeDislike;
    public int positive;
    public int negative;
    public int netral;
    public int noReputationUserScore;
    public String productAvatar;
    public String reputationId;
    public List<ImageUpload> reviewImageList = new ArrayList<>();

    public ReputationModel() {
    }


    protected ReputationModel(Parcel in) {
        reviewId = in.readString();
        username = in.readString();
        userId = in.readString();
        avatarUrl = in.readString();
        date = in.readString();
        comment = in.readString();
        userLabel = in.readString();
        starQuality = in.readInt();
        starAccuracy = in.readInt();
        smiley = in.readInt();
        counterSmiley = in.readString();
        counterLike = in.readInt();
        counterDislike = in.readInt();
        counterResponse = in.readInt();
        shopName = in.readString();
        shopId = in.readString();
        shopAvatarUrl = in.readString();
        responseMessage = in.readString();
        responseDate = in.readString();
        userNameResponder = in.readString();
        avatarUrlResponder = in.readString();
        labelIdResponder = in.readString();
        userLabelResponder = in.readString();
        userIdResponder = in.readString();
        productId = in.readString();
        productName = in.readString();
        shopReputation = in.readString();
        typeMedal = in.readInt();
        levelMedal = in.readInt();
        isGetLikeDislike = in.readByte() != 0;
        statusLikeDislike = in.readInt();
        positive = in.readInt();
        negative = in.readInt();
        netral = in.readInt();
        noReputationUserScore = in.readInt();
        productAvatar = in.readString();
        reputationId = in.readString();
        reviewImageList = in.createTypedArrayList(ImageUpload.CREATOR);
    }

    public static final Creator<ReputationModel> CREATOR = new Creator<ReputationModel>() {
        @Override
        public ReputationModel createFromParcel(Parcel in) {
            return new ReputationModel(in);
        }

        @Override
        public ReputationModel[] newArray(int size) {
            return new ReputationModel[size];
        }
    };

    public ArrayList<ImageUpload> getImages() {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ImageUpload reviewImage : reviewImageList) {
            list.add(reviewImage);
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(username);
        dest.writeString(userId);
        dest.writeString(avatarUrl);
        dest.writeString(date);
        dest.writeString(comment);
        dest.writeString(userLabel);
        dest.writeInt(starQuality);
        dest.writeInt(starAccuracy);
        dest.writeInt(smiley);
        dest.writeString(counterSmiley);
        dest.writeInt(counterLike);
        dest.writeInt(counterDislike);
        dest.writeInt(counterResponse);
        dest.writeString(shopName);
        dest.writeString(shopId);
        dest.writeString(shopAvatarUrl);
        dest.writeString(responseMessage);
        dest.writeString(responseDate);
        dest.writeString(userNameResponder);
        dest.writeString(avatarUrlResponder);
        dest.writeString(labelIdResponder);
        dest.writeString(userLabelResponder);
        dest.writeString(userIdResponder);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(shopReputation);
        dest.writeInt(typeMedal);
        dest.writeInt(levelMedal);
        dest.writeByte((byte) (isGetLikeDislike ? 1 : 0));
        dest.writeInt(statusLikeDislike);
        dest.writeInt(positive);
        dest.writeInt(negative);
        dest.writeInt(netral);
        dest.writeInt(noReputationUserScore);
        dest.writeString(productAvatar);
        dest.writeString(reputationId);
        dest.writeTypedList(reviewImageList);
    }
}
