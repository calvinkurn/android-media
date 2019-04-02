
package com.tokopedia.core.common.category.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("child")
    @Expose
    private List<Category> child = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("parent")
    @Expose
    private Integer parent;
    @SerializedName("tree")
    @Expose
    private Integer tree;
    @SerializedName("hidden")
    @Expose
    private Integer hidden;
    @SerializedName("is_revamp")
    @Expose
    private Boolean isRevamp;
    @SerializedName("is_intermediary")
    @Expose
    private Boolean isIntermediary;

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getTree() {
        return tree;
    }

    public void setTree(Integer tree) {
        this.tree = tree;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Boolean getIsRevamp() {
        return isRevamp;
    }

    public void setIsRevamp(Boolean isRevamp) {
        this.isRevamp = isRevamp;
    }

    public Boolean getIsIntermediary() {
        return isIntermediary;
    }

    public void setIsIntermediary(Boolean isIntermediary) {
        this.isIntermediary = isIntermediary;
    }

}
