package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class MenuPromoResponse {

    @SerializedName("id_menu")
    @Expose
    private int idMenu;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("childrens")
    @Expose
    private List<Children> childrenList = new ArrayList<>();
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_off")
    @Expose
    private String iconOff;
    @SerializedName("slug")
    @Expose
    private String slug;

    public int getIdMenu() {
        return idMenu;
    }

    public String getTitle() {
        return title;
    }

    public List<Children> getChildrenList() {
        return childrenList;
    }

    public String getIcon() {
        return icon;
    }

    public String getIconOff() {
        return iconOff;
    }

    public String getSlug() {
        return slug;
    }
}
