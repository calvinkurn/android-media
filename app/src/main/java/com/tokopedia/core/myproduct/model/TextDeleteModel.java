package com.tokopedia.core.myproduct.model;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 08/12/2015.
 */
@Parcel
public class TextDeleteModel {
    String customText;
    String text;
    boolean isDeleted;
    boolean isDefault;
    int dataPosition;
    int departmentId;
    long etalaseId;

    /**
     * this is for parcelable
     */
    public TextDeleteModel(){}

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }


    public TextDeleteModel(String text){ this.text = text;}

    public TextDeleteModel(boolean isDefault){
        this.isDefault = isDefault;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    @Override
    public String toString() {
        return "TextDeleteModel{" +
                "text='" + text + '\'' +
                '}';
    }
}
