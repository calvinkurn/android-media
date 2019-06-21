package com.tokopedia.feedplus.domain.model.feed;

import com.tokopedia.kolcommon.data.pojo.Author;

import java.util.ArrayList;

/**
 * @author by yfsx on 20/06/18.
 */
public class WhitelistDomain {

    private String error;
    private boolean isWhitelist;
    private String url;
    private String title;
    private String titleIdentifier;
    private String postSuccessMessage;
    private String desc;
    private String image;
    private ArrayList<Author> authors;

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public void setWhitelist(boolean whitelist) {
        isWhitelist = whitelist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostSuccessMessage() {
        return postSuccessMessage;
    }

    public void setPostSuccessMessage(String postSuccessMessage) {
        this.postSuccessMessage = postSuccessMessage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    public void setTitleIdentifier(String titleIdentifier) {
        this.titleIdentifier = titleIdentifier;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }
}
