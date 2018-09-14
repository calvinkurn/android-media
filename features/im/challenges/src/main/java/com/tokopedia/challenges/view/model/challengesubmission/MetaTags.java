
package com.tokopedia.challenges.view.model.challengesubmission;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaTags implements Serializable, Parcelable
{

    @SerializedName("og:title")
    @Expose
    private String ogTitle;
    @SerializedName("og:description")
    @Expose
    private String ogDescription;
    @SerializedName("og:image")
    @Expose
    private String ogImage;
    @SerializedName("og:url")
    @Expose
    private String ogUrl;
    public final static Creator<MetaTags> CREATOR = new Creator<MetaTags>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MetaTags createFromParcel(Parcel in) {
            return new MetaTags(in);
        }

        public MetaTags[] newArray(int size) {
            return (new MetaTags[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4774327625422063567L;

    protected MetaTags(Parcel in) {
        this.ogTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.ogDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.ogImage = ((String) in.readValue((String.class.getClassLoader())));
        this.ogUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MetaTags() {
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    public String getOgUrl() {
        return ogUrl;
    }

    public void setOgUrl(String ogUrl) {
        this.ogUrl = ogUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ogTitle);
        dest.writeValue(ogDescription);
        dest.writeValue(ogImage);
        dest.writeValue(ogUrl);
    }

    public int describeContents() {
        return  0;
    }

}
