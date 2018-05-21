package com.tokopedia.events.data.entity.response.searchresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FiltersItem {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("applied_range")
    private Object appliedRange;

    @SerializedName("applied")
    private List<AppliedItem> applied;

    @SerializedName("kind")
    private String kind;

    @SerializedName("values")
    private List<ValuesItem> values;

    @SerializedName("name")
    private String name;

    @SerializedName("attribute_name")
    private String attributeName;

    @SerializedName("id")
    private int id;

    @SerializedName("label")
    private String label;

    @SerializedName("priority")
    private int priority;

    @SerializedName("status")
    private int status;

    public List<ValuesItem> getValues() {
        return values;
    }

    public void setValues(List<ValuesItem> values) {
        this.values = values;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setAppliedRange(Object appliedRange) {
        this.appliedRange = appliedRange;
    }

    public Object getAppliedRange() {
        return appliedRange;
    }

    public void setApplied(List<AppliedItem> applied) {
        this.applied = applied;
    }

    public List<AppliedItem> getApplied() {
        return applied;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "FiltersItem{" +
                        "category_id = '" + categoryId + '\'' +
                        ",applied_range = '" + appliedRange + '\'' +
                        ",applied = '" + applied + '\'' +
                        ",kind = '" + kind + '\'' +
                        ",values = '" + values + '\'' +
                        ",name = '" + name + '\'' +
                        ",attribute_name = '" + attributeName + '\'' +
                        ",id = '" + id + '\'' +
                        ",label = '" + label + '\'' +
                        ",priority = '" + priority + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}