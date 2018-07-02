package com.tokopedia.product.edit.domain.model.youtube;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YoutubeVideoModel implements Parcelable {

    private static final String YT_AGE_RESTRICTED = "ytAgeRestricted";

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;

    private Item item;

    public boolean isVideoAgeRestricted(){
        if (!hasVideo()) {
            return false;
        }
        ContentRating contentRating = items.get(0).getContentDetails().getContentRating();
        return contentRating != null && contentRating.getYtRating().equals(YT_AGE_RESTRICTED);
    }

    public boolean hasVideo(){
        return items!= null && items.size() > 0;
    }

    public String getId(){
        return items.get(0).getId();
    }

    public String getTitle(){
        return items.get(0).getSnippet().getTitle();
    }

    public String getDescription(){
        return items.get(0).getSnippet().getDescription();
    }

    public String getChannel(){
        return items.get(0).getSnippet().getChannelTitle();
    }

    public int getHeight(){
        return items.get(0).getSnippet().getThumbnails().getDefault().getHeight();
    }

    public int getWidth(){
        return items.get(0).getSnippet().getThumbnails().getDefault().getWidth();
    }

    public String getThumbnailUrl(){
        return items.get(0).getSnippet().getThumbnails().getDefault().getUrl();
    }

    public String getDuration(){
        return items.get(0).getContentDetails().getDuration();
    }


    protected YoutubeVideoModel(Parcel in) {
        kind = in.readString();
        etag = in.readString();
        pageInfo = (PageInfo) in.readValue(PageInfo.class.getClassLoader());
        if (in.readByte() == 0x01) {
            items = new ArrayList<>();
            in.readList(items, Item.class.getClassLoader());
        } else {
            items = null;
        }
        item = (Item) in.readValue(Item.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(etag);
        dest.writeValue(pageInfo);
        if (items == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(items);
        }
        dest.writeValue(item);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<YoutubeVideoModel> CREATOR = new Parcelable.Creator<YoutubeVideoModel>() {
        @Override
        public YoutubeVideoModel createFromParcel(Parcel in) {
            return new YoutubeVideoModel(in);
        }

        @Override
        public YoutubeVideoModel[] newArray(int size) {
            return new YoutubeVideoModel[size];
        }
    };
}