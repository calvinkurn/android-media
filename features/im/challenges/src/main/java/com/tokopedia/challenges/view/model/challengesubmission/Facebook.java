
package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Facebook implements Parcelable
{

    @SerializedName("Instructions")
    @Expose
    private String instructions;
    @SerializedName("RequiredUrl")
    @Expose
    private String requiredUrl;
    public final static Creator<Facebook> CREATOR = new Creator<Facebook>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Facebook createFromParcel(Parcel in) {
            return new Facebook(in);
        }

        public Facebook[] newArray(int size) {
            return (new Facebook[size]);
        }

    }
    ;

    protected Facebook(Parcel in) {
        this.instructions = ((String) in.readValue((String.class.getClassLoader())));
        this.requiredUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Facebook() {
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
