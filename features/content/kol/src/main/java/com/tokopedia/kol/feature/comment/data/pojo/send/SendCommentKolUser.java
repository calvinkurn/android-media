package com.tokopedia.kol.feature.comment.data.pojo.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class SendCommentKolUser {
    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("iskol")
    @Expose
    private Boolean iskol;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Boolean getIskol() {
        return iskol;
    }

    public void setIskol(Boolean iskol) {
        this.iskol = iskol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
