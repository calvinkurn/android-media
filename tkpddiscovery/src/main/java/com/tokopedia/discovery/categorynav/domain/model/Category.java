package com.tokopedia.discovery.categorynav.domain.model;


import com.tokopedia.core.discovery.dynamicfilter.adapter.MultiLevelExpIndListAdapter;
import com.tokopedia.core.discovery.model.DynamicObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class Category implements MultiLevelExpIndListAdapter.ExpIndData {

    private List<Category> children = new ArrayList<>();
    private String id;
    private String name;
    private String iconImageUrl;
    private Boolean hasChild;
    private int indentation;


    @Override
    public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
        return children;
    }

    @Override
    public boolean isGroup() {
        return hasChild;
    }

    @Override
    public void setIsGroup(boolean value) {
        hasChild = value;
    }

    @Override
    public void setGroupSize(int groupSize) {

    }

    @Override
    public String toString() {
        return id;
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

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public void addChildren(List<Category> children, int indentation) {
        this.children.addAll(children);
        for (Category category : children) {
            category.setIndentation(indentation);
        }
    }
}
