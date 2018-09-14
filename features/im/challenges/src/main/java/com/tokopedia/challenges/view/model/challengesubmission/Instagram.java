
package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instagram implements Parcelable
{

    @SerializedName("Instructions")
    @Expose
    private String instructions;
    @SerializedName("RequiredText")
    @Expose
    private String requiredText;
    @SerializedName("VideoAssetUrl")
    @Expose
    private String videoAssetUrl;
    public final static Creator<Instagram> CREATOR = new Creator<Instagram>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Instagram createFromParcel(Parcel in) {
            return new Instagram(in);
        }

        public Instagram[] newArray(int size) {
            return (new Instagram[size]);
        }

    }
    ;

    protected Instagram(Parcel in) {
        this.instructions = ((String) in.readValue((String.class.getClassLoader())));
        this.requiredText = ((String) in.readValue((String.class.getClassLoader())));
        this.videoAssetUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Instagram() {
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRequiredText() {
        return requiredText;
    }

    public void setRequiredText(String requiredText) {
        this.requiredText = requiredText;
    }

    public String getVideoAssetUrl() {
        return videoAssetUrl;
    }

    public void setVideoAssetUrl(String videoAssetUrl) {
        this.videoAssetUrl = videoAssetUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(instructions);
        dest.writeValue(requiredText);
        dest.writeValue(videoAssetUrl);
    }

    public int describeContents() {
        return  0;
    }

}
