
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.challenges.view.model.challengesubmission.Sharing;

public class Result implements Parcelable {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("SubmissionCount")
    @Expose
    private Integer submissionCount;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("ThumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("HashTag")
    @Expose
    private String hashTag;
    @SerializedName("Prizes")
    @Expose
    private List<Prize> prizes = null;
    @SerializedName("Sharing")
    @Expose
    private Sharing sharing;
    @SerializedName("Channel")
    @Expose
    private Channel channel;
    @SerializedName("Me")
    @Expose
    private Me me;

    protected Result(Parcel in) {
        id = in.readString();
        type = in.readString();
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            submissionCount = null;
        } else {
            submissionCount = in.readInt();
        }
        endDate = in.readString();
        thumbnailUrl = in.readString();
        hashTag = in.readString();
        prizes = in.createTypedArrayList(Prize.CREATOR);
        sharing = in.readParcelable(Sharing.class.getClassLoader());
        channel = in.readParcelable(Channel.class.getClassLoader());
        me = in.readParcelable((Me.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(description);
        if (submissionCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(submissionCount);
        }
        dest.writeString(endDate);
        dest.writeString(thumbnailUrl);
        dest.writeString(hashTag);
        dest.writeTypedList(prizes);
        dest.writeParcelable(sharing, flags);
        dest.writeParcelable(channel, flags);
        dest.writeParcelable(me, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(Integer submissionCount) {
        this.submissionCount = submissionCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public Sharing getSharing() {
        return sharing;
    }

    public void setSharing(Sharing sharing) {
        this.sharing = sharing;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Me getMe() {
        return me;
    }

    public void setMe(Me me) {
        this.me = me;
    }
}
