package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.challenges.view.model.Channel;
import com.tokopedia.challenges.view.model.Collection;
import com.tokopedia.challenges.view.model.Me;
import com.tokopedia.challenges.view.model.User;

import java.util.List;

public class SubmissionResult implements Parcelable {

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
    @SerializedName("Points")
    @Expose
    private int points;
    @SerializedName("Views")
    @Expose
    private int views;
    @SerializedName("Likes")
    @Expose
    private int likes;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("ThumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Media")
    @Expose
    private List<Media> media = null;
    @SerializedName("Channel")
    @Expose
    private Channel channel;
    @SerializedName("Collection")
    @Expose
    private Collection collection;
    @SerializedName("User")
    @Expose
    private User user;
    @SerializedName("Me")
    @Expose
    private Me me;
    @SerializedName("Sharing")
    @Expose
    private Sharing sharing;
    @SerializedName("Awards")
    @Expose
    private List<Awards> awards;

    @SerializedName("StatusMessage")
    @Expose
    private String statusMessage;

    @Expose(deserialize = false)
    private boolean isExpanded;


    public SubmissionResult() {
    }

    protected SubmissionResult(Parcel in) {
        id = in.readString();
        type = in.readString();
        title = in.readString();
        description = in.readString();
        points = in.readInt();
        views = in.readInt();
        likes = in.readInt();
        createDate = in.readString();
        thumbnailUrl = in.readString();
        status = in.readString();
        media = in.createTypedArrayList(Media.CREATOR);
        channel = in.readParcelable(Channel.class.getClassLoader());
        collection = in.readParcelable(Collection.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        me = in.readParcelable(Me.class.getClassLoader());
        sharing = in.readParcelable(Sharing.class.getClassLoader());
        awards = in.createTypedArrayList(Awards.CREATOR);
        statusMessage = in.readString();
        isExpanded = in.readByte() != 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(points);
        dest.writeInt(views);
        dest.writeInt(likes);
        dest.writeString(createDate);
        dest.writeString(thumbnailUrl);
        dest.writeString(status);
        dest.writeTypedList(media);
        dest.writeParcelable(channel, flags);
        dest.writeParcelable(collection, flags);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(me, flags);
        dest.writeParcelable(sharing, flags);
        dest.writeTypedList(awards);
        dest.writeString(statusMessage);
        dest.writeByte((byte) (isExpanded ? 1 : 0));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubmissionResult> CREATOR = new Creator<SubmissionResult>() {
        @Override
        public SubmissionResult createFromParcel(Parcel in) {
            return new SubmissionResult(in);
        }

        @Override
        public SubmissionResult[] newArray(int size) {
            return new SubmissionResult[size];
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Me getMe() {
        return me;
    }

    public void setMe(Me me) {
        this.me = me;
    }

    public Sharing getSharing() {
        return sharing;
    }

    public void setSharing(Sharing sharing) {
        this.sharing = sharing;
    }

    public List<Awards> getAwards() {
        return awards;
    }

    public void setAwards(List<Awards> awards) {
        this.awards = awards;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}