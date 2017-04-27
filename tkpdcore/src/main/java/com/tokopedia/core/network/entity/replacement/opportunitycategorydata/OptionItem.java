package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nisie on 4/26/17.
 */

public class OptionItem {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    @Expose
    private int parent;
    @SerializedName("hidden")
    @Expose
    private int hidden;
    @SerializedName("tree")
    @Expose
    private int tree;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("child")
    @Expose
    private ArrayList<OptionItem> listChild;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getTree() {
        return tree;
    }

    public void setTree(int tree) {
        this.tree = tree;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ArrayList<OptionItem> getListChild() {
        return listChild;
    }

    public void setListChild(ArrayList<OptionItem> listChild) {
        this.listChild = listChild;
    }
}
