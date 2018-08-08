package com.tokopedia.core.discovery.model;

import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.discovery.dynamicfilter.adapter.MultiLevelExpIndListAdapter;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 7/13/16.
 * Modified by erry
 */
public class DynamicObject implements MultiLevelExpIndListAdapter.ExpIndData {

    private int mIndentation;
    private String mParentText;
    private boolean isChecked;
    private List<DynamicObject> mChildren;
    private boolean mIsGroup;
    private String depId;
    public String key;

    public DynamicObject() {
    }


    public DynamicObject(HadesV1Model.Category model) {
        mParentText = model.getName();
        this.depId = model.getId();
        mChildren = new ArrayList<>();
        initKey(model.getParent(), model.getId());
    }

    public DynamicObject(CategoryDB model) {
        mParentText = model.getNameCategory();
        this.depId = String.valueOf(model.getDepartmentId());
        mChildren = new ArrayList<>();
        initKey(model.getParentId(), String.valueOf(model.getDepartmentId()));
    }

    public String getKey() {
        return key;
    }

    private void initKey(int parentId, String depId) {
        this.key = depId;
    }

    public String getParentText() {
        return mParentText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDepId() {
        return depId;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    @Override
    public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
        return mChildren;
    }

    @Override
    public boolean isGroup() {
        return mIsGroup;
    }

    @Override
    public void setIsGroup(boolean value) {
        mIsGroup = value;
    }

    @Override
    public void setGroupSize(int groupSize) {


    }


    public int getIndentation() {
        return mIndentation;
    }

    private void setIndentation(int indentation) {
        mIndentation = indentation;
    }



}
