
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterResponseEntity {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("attribute_name")
    @Expose
    private String attributeName;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("values")
    @Expose
    private List<ValueResponseEntity> values = null;
    @SerializedName("applied")
    @Expose
    private Object applied;
    @SerializedName("applied_range")
    @Expose
    private Object appliedRange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ValueResponseEntity> getValues() {
        return values;
    }

    public void setValues(List<ValueResponseEntity> values) {
        this.values = values;
    }

    public Object getApplied() {
        return applied;
    }

    public void setApplied(Object applied) {
        this.applied = applied;
    }

    public Object getAppliedRange() {
        return appliedRange;
    }

    public void setAppliedRange(Object appliedRange) {
        this.appliedRange = appliedRange;
    }

}
