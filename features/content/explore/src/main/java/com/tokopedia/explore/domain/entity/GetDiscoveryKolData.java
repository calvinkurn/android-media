
package com.tokopedia.explore.domain.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDiscoveryKolData {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("postKol")
    @Expose
    private List<PostKol> postKol = null;
    @SerializedName("lastCursor")
    @Expose
    private String lastCursor;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<PostKol> getPostKol() {
        return postKol;
    }

    public void setPostKol(List<PostKol> postKol) {
        this.postKol = postKol;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

}
