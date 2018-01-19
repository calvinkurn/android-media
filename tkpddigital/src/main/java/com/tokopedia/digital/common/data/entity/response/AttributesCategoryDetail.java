package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class AttributesCategoryDetail {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("teaser")
    @Expose
    private Teaser teaser;
    @SerializedName("is_new")
    @Expose
    private boolean isNew;
    @SerializedName("instant_checkout")
    @Expose
    private boolean instantCheckout;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("default_operator_id")
    @Expose
    private String defaultOperatorId;
    @SerializedName("operator_style")
    @Expose
    private String operatorStyle;
    @SerializedName("operator_label")
    @Expose
    private String operatorLabel;
    @SerializedName("fields")
    @Expose
    private List<Field> fields = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Teaser getTeaser() {
        return teaser;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public String getSlug() {
        return slug;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public List<Field> getFields() {
        return fields;
    }
}
