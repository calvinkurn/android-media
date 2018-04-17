package com.tokopedia.topads.keyword.view.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 11/20/17.
 */

public class TopAdsKeywordStepperModel implements StepperModel {
    public static final Creator<TopAdsKeywordStepperModel> CREATOR = new Creator<TopAdsKeywordStepperModel>() {
        @Override
        public TopAdsKeywordStepperModel createFromParcel(Parcel source) {
            return new TopAdsKeywordStepperModel(source);
        }

        @Override
        public TopAdsKeywordStepperModel[] newArray(int size) {
            return new TopAdsKeywordStepperModel[size];
        }
    };
    private boolean isPositive;
    private String groupId;
    private String choosenId;
    private String groupName;
    @KeywordTypeDef
    private int keywordType;
    private int serverCount, maxWords;
    @NonNull
    private ArrayList<String> localWords;

    public TopAdsKeywordStepperModel() {
    }

    protected TopAdsKeywordStepperModel(Parcel in) {
        this.isPositive = in.readByte() != 0;
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.keywordType = in.readInt();
        this.serverCount = in.readInt();
        this.maxWords = in.readInt();
        this.localWords = in.createStringArrayList();
    }

    public String getChoosenId() {
        return choosenId;
    }

    public void setChoosenId(String choosenId) {
        this.choosenId = choosenId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(int keywordType) {
        this.keywordType = keywordType;
    }

    public int getServerCount() {
        return serverCount;
    }

    public void setServerCount(int serverCount) {
        this.serverCount = serverCount;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    @NonNull
    public ArrayList<String> getLocalWords() {
        return localWords;
    }

    public void setLocalWords(@NonNull ArrayList<String> localWords) {
        this.localWords = localWords;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPositive ? (byte) 1 : (byte) 0);
        dest.writeString(this.groupId);
        dest.writeString(this.groupName);
        dest.writeInt(this.keywordType);
        dest.writeInt(this.serverCount);
        dest.writeInt(this.maxWords);
        dest.writeStringList(this.localWords);
    }
}
