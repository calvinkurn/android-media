
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class ReviewProductOwner implements Parcelable{

    @SerializedName("user_shop_reputation")
    @Expose
    private UserShopReputation userShopReputation;
    @SerializedName("user_shop_name")
    @Expose
    private String userShopName;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("user_shop_image")
    @Expose
    private String userShopImage;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_name")
    @Expose
    private String userName;

    protected ReviewProductOwner(Parcel in) {
        userShopReputation = (UserShopReputation) in.readValue(UserShopReputation.class.getClassLoader());
        userShopName = in.readString();
        userLabelId = in.readInt();
        userLabel = in.readString();
        userShopImage = in.readString();
        userId = in.readInt();
        userImage = in.readString();
        userName = in.readString();
    }

    public static final Creator<ReviewProductOwner> CREATOR = new Creator<ReviewProductOwner>() {
        @Override
        public ReviewProductOwner createFromParcel(Parcel in) {
            return new ReviewProductOwner(in);
        }

        @Override
        public ReviewProductOwner[] newArray(int size) {
            return new ReviewProductOwner[size];
        }
    };

    /**
     * 
     * @return
     *     The userShopReputation
     */
    public UserShopReputation getUserShopReputation() {
        return userShopReputation;
    }

    /**
     * 
     * @param userShopReputation
     *     The user_shop_reputation
     */
    public void setUserShopReputation(UserShopReputation userShopReputation) {
        this.userShopReputation = userShopReputation;
    }

    /**
     * 
     * @return
     *     The userShopName
     */
    public String getUserShopName() {
        return MethodChecker.fromHtml(userShopName).toString();
    }

    /**
     * 
     * @param userShopName
     *     The user_shop_name
     */
    public void setUserShopName(String userShopName) {
        this.userShopName = userShopName;
    }

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
     *     The userShopImage
     */
    public String getUserShopImage() {
        return userShopImage;
    }

    /**
     * 
     * @param userShopImage
     *     The user_shop_image
     */
    public void setUserShopImage(String userShopImage) {
        this.userShopImage = userShopImage;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * 
     * @param userImage
     *     The user_image
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userShopReputation);
        dest.writeString(userShopName);
        dest.writeInt(userLabelId);
        dest.writeString(userLabel);
        dest.writeString(userShopImage);
        dest.writeInt(userId);
        dest.writeString(userImage);
        dest.writeString(userName);
    }
}
