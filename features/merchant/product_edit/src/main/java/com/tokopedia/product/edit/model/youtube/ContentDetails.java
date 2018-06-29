package com.tokopedia.product.edit.model.youtube;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentDetails implements Parcelable {

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("licensedContent")
    @Expose
    private boolean licensedContent;
    @SerializedName("projection")
    @Expose
    private String projection;
    @SerializedName("contentRating")
    @Expose
    private ContentRating contentRating;

    public ContentRating getContentRating() {
        return contentRating;
    }

    public void setContentRating(ContentRating contentRating) {
        this.contentRating = contentRating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isLicensedContent() {
        return licensedContent;
    }

    public void setLicensedContent(boolean licensedContent) {
        this.licensedContent = licensedContent;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }


    protected ContentDetails(Parcel in) {
        duration = in.readString();
        dimension = in.readString();
        definition = in.readString();
        caption = in.readString();
        licensedContent = in.readByte() != 0x00;
        projection = in.readString();
        contentRating = (ContentRating) in.readValue(ContentRating.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(dimension);
        dest.writeString(definition);
        dest.writeString(caption);
        dest.writeByte((byte) (licensedContent ? 0x01 : 0x00));
        dest.writeString(projection);
        dest.writeValue(contentRating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContentDetails> CREATOR = new Parcelable.Creator<ContentDetails>() {
        @Override
        public ContentDetails createFromParcel(Parcel in) {
            return new ContentDetails(in);
        }

        @Override
        public ContentDetails[] newArray(int size) {
            return new ContentDetails[size];
        }
    };
}