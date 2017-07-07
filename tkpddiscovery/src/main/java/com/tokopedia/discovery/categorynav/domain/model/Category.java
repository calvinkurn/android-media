package com.tokopedia.discovery.categorynav.domain.model;


import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class Category {

    private List<ChildCategory> children = null;
    private String id;
    private String name;
    private String iconImageUrl;
    private Boolean hasChild;

    public List<ChildCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ChildCategory> children) {
        this.children = children;
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

    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }
}
