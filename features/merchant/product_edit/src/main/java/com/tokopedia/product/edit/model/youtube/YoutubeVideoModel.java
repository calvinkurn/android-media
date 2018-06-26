package com.tokopedia.product.edit.model.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;

import java.util.List;

public class YoutubeVideoModel {

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
    private List<Item> items = null;

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

}