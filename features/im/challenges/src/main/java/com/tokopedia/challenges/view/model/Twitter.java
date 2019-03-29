
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Twitter implements Parcelable
{

    @SerializedName("Instructions")
    @Expose
    private String instructions;
    @SerializedName("RequiredUrl")
    @Expose
    private String requiredUrl;
    public final static Creator<Twitter> CREATOR = new Creator<Twitter>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Twitter createFromParcel(Parcel in) {
            return new Twitter(in);
        }

        public Twitter[] newArray(int size) {
            return (new Twitter[size]);
        }

    }
    ;

    protected Twitter(Parcel in) {
        this.instructions = ((String) in.readValue((String.class.getClassLoader())));
        this.requiredUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Twitter() {
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRequiredUrl() {
        return requiredUrl;
    }

    public void setRequiredUrl(String requiredUrl) {
        this.requiredUrl = requiredUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(instructions);
        dest.writeValue(requiredUrl);
    }

    public int describeContents() {
        return  0;
    }

}
