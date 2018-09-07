
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewerData implements Parcelable{

    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("user_id")
    @Expose
    private String userId;

    protected ReviewerData(Parcel in) {
        userLabelId = in.readInt();
        fullName = in.readString();
        imageUri = in.readString();
        userLabel = in.readString();
        userUrl = in.readString();
        userId = in.readString();
        userReputation = (UserReputation) in.readValue(UserReputation.class.getClassLoader());
    }

    public static final Creator<ReviewerData> CREATOR = new Creator<ReviewerData>() {
        @Override
        public ReviewerData createFromParcel(Parcel in) {
            return new ReviewerData(in);
        }

        @Override
        public ReviewerData[] newArray(int size) {
            return new ReviewerData[size];
        }
    };

    /**
     * 
     * @return
     *     The userLabelId
     */
    public int getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The userReputation
     */
    public UserReputation getUserReputation() {
        return userReputation;
    }

    /**
     * 
     * @param userReputation
     *     The user_reputation
     */
    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    /**
     * 
     * @return
     *     The imageUri
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * 
     * @param imageUri
     *     The image_uri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * 
     * @return
     *     The userUrl
     */
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * 
     * @param userUrl
     *     The user_url
     */
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userLabelId);
        dest.writeString(fullName);
        dest.writeString(imageUri);
        dest.writeString(userLabel);
        dest.writeString(userUrl);
        dest.writeString(userId);
        dest.writeValue(userReputation);
    }
}
