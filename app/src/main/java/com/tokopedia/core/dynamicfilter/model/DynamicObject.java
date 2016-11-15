package com.tokopedia.core.dynamicfilter.model;

import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.dynamicfilter.adapter.MultiLevelExpIndListAdapter;
import com.tokopedia.core.dynamicfilter.facade.models.HadesV1Model;

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
    private int mGroupSize;
    private String depId;
    public String key;

    public DynamicObject() {
    }

    public DynamicObject(HadesV1Model.Child model, int size) {
        if (size > 0) {
            mParentText = model.getName();
        } else {
            mParentText = model.getName();
        }
        this.mGroupSize = size;
        this.depId = model.getId();
        mChildren = new ArrayList<DynamicObject>();
        initKey(model.getParent(), model.getId());
    }

    public DynamicObject(HadesV1Model.Child_ model, int size) {
        if (size > 0) {
            mParentText = model.getName();
        } else {
            mParentText = model.getName();
        }
        this.mGroupSize = size;
        this.depId = model.getId();
        mChildren = new ArrayList<>();
        initKey(model.getParent(), model.getId());
    }

    public DynamicObject(HadesV1Model.Category model, int size) {
        if (size > 0) {
            mParentText = model.getName();
        } else {
            mParentText = model.getName();
        }
        this.mGroupSize = size;
        this.depId = model.getId();
        mChildren = new ArrayList<>();
        initKey(model.getParent(), model.getId());
    }

    public DynamicObject(CategoryDB model, int size) {
        if (size > 0) {
            mParentText = model.getNameCategory();
        } else {
            mParentText = model.getNameCategory();
        }
        this.mGroupSize = size;
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
        mGroupSize = groupSize;
    }

    public void addChild(List<DynamicObject> childs, int increment) {
        mChildren.addAll(childs);
        for (DynamicObject c : childs) {
            c.setIndentation(getIndentation() + increment);
        }
    }

    public int getIndentation() {
        return mIndentation;
    }

    private void setIndentation(int indentation) {
        mIndentation = indentation;
    }


    public static DynamicObject createOptionForAll(CategoryDB model) {
        DynamicObject object = new DynamicObject();
        object.mParentText = "Semua " + model.getNameCategory();

        object.depId = String.valueOf(model.getDepartmentId());
        object.mChildren = new ArrayList<>();
        object.initKey(model.getParentId(), String.valueOf(model.getDepartmentId()));
        return object;
    }
}
