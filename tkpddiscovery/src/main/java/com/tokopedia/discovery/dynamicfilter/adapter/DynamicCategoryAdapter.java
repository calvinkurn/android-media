package com.tokopedia.discovery.dynamicfilter.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tokopedia.core.R;
import com.tokopedia.core.discovery.dynamicfilter.adapter.MultiLevelExpIndListAdapter;
import com.tokopedia.core.discovery.model.DynamicObject;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tokopedia on 9/5/2016.
 * Modified by erry
 */
public class DynamicCategoryAdapter extends MultiLevelExpIndListAdapter {

    private static final String TAG = "DynamicCategoryAdapter";
    /**
     * This is called when the user click on an item or group.
     */
    private final View.OnClickListener mListener;
    private ArrayList<String> listIds;
    private final DynamicFilterView dynamicFilterView;

    public DynamicCategoryAdapter(DynamicFilterView dynamicFilterView, View.OnClickListener mListener) {
        this.dynamicFilterView = dynamicFilterView;
        this.mListener = mListener;
        this.listIds = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.dynamic_parent_view_holder_layout;
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new DynamicViewHolder(view);
        view.setOnClickListener(mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final DynamicViewHolder parentViewHolder = (DynamicViewHolder) viewHolder;
        final DynamicObject dynamicObject = (DynamicObject) getItemAt(position);
        parentViewHolder.setDynamicObject(dynamicObject);
        parentViewHolder.dynamicParentViewHolderText.setText(dynamicObject.getParentText());
        parentViewHolder.dynamicParentViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                setCheckedView(parentViewHolder, dynamicObject, isChecked);
            }
        });
        if (dynamicFilterView != null) {
            Boolean isChecked = dynamicFilterView.getCheckedPosition(dynamicObject.getKey());
            if (isChecked != null && isChecked) {
                parentViewHolder.dynamicParentViewHolder.setChecked(true);
                if(listIds.contains(dynamicObject.getDepId()))
                    listIds.remove(dynamicObject.getDepId());
                listIds.add(dynamicObject.getDepId());
            } else {
                parentViewHolder.dynamicParentViewHolder.setChecked(false);
            }
        }
        if (dynamicObject.getIndentation() == 0) {
            parentViewHolder.setPaddingLeft(0);
            parentViewHolder.setCheckboxVisibility(View.GONE);
            parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {toggleGroup(position);
                }
            });
        } else if (dynamicObject.getIndentation() == 1) {
            parentViewHolder.setPaddingLeft(0);
            if (dynamicObject.getChildren().size() > 0) {
                parentViewHolder.setCheckboxVisibility(View.INVISIBLE);
                parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleGroup(position);
                    }
                });
            } else {
                parentViewHolder.setCheckboxVisibility(View.VISIBLE);
                parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isChecked = !parentViewHolder.dynamicParentViewHolder.isChecked();
                        setCheckedView(parentViewHolder, dynamicObject, isChecked);
                    }
                });
            }
        } else {
            parentViewHolder.setCheckboxVisibility(View.VISIBLE);
            parentViewHolder.setPaddingLeft(25);
            parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = !parentViewHolder.dynamicParentViewHolder.isChecked();
                    setCheckedView(parentViewHolder, dynamicObject, isChecked);
                }
            });
        }
    }

    private void setCheckedView(DynamicViewHolder parentViewHolder, DynamicObject dynamicObject, boolean isChecked){
        parentViewHolder.dynamicParentViewHolder.setChecked(isChecked);
        dynamicObject.setChecked(isChecked);
        Context context = parentViewHolder.itemView.getContext();
        if (context != null && context instanceof DynamicFilterView) {
            checkFilter(parentViewHolder.dynamicParentViewHolder.isChecked(), ((DynamicFilterView) context), dynamicObject);
        }
    }

    public void clearSelectedIds(){
        listIds.clear();
    }

    private void checkFilter(boolean checked, DynamicFilterView dynamicFilterView, DynamicObject dynamicObject) {
        if (checked) {
            dynamicFilterView.saveCheckedPosition(dynamicObject.getKey(), true);
            if(listIds.contains(dynamicObject.getDepId())){
                listIds.remove(dynamicObject.getDepId());
            }
            listIds.add(dynamicObject.getDepId());
        } else {
            dynamicFilterView.removeCheckedPosition(dynamicObject.getKey());
            listIds.remove(dynamicObject.getDepId());
        }
        String selectedIds = generateSelectedDepIds(listIds);
        if (TextUtils.isEmpty(selectedIds)) {
            dynamicFilterView.removeSelecfedFilter(BrowseApi.SC);
        } else {
            dynamicFilterView.putSelectedFilter(BrowseApi.SC, selectedIds);
        }
    }

    private String generateSelectedDepIds(ArrayList<String> selectedIds) {
        StringBuffer buffer = new StringBuffer();
        for (String s : selectedIds){
            buffer.append(s).append(",");
        }
        if (buffer.length() > 0) {
            return buffer.substring(0, buffer.length() - 1);
        } else {
            return buffer.toString();
        }
    }

    public void reset() {
        notifyItemRangeChanged(0, getItemCount());
        clearSelectedIds();
    }

    /**
     * Expand all checked category
     */
    public void expandCheckedCategory() {
        Map<String, Boolean> selectedPositionMap = new ArrayMap<>();
        selectedPositionMap.putAll(dynamicFilterView.getSelectedPositions());
        // Find group set of category key
        Set<Integer> groupKeySet = new HashSet<>();
        findCheckedParentList(getData(), selectedPositionMap, groupKeySet);
        // Expand and collapse the adapter to find selected category
        List<Integer> groupsNum = saveGroups();
        collapseUncheckedGroup(groupsNum, groupKeySet);

        notifyDataSetChanged();
    }

    /**
     * List all selected category key
     *
     * @param expIndDataList
     * @param selectedPositionMap
     * @param groupKeySet
     * @return
     */
    private boolean findCheckedParentList(List<ExpIndData> expIndDataList, Map<String, Boolean> selectedPositionMap,
                                          Set<Integer> groupKeySet) {
        boolean groupChecked = false;
        for (ExpIndData expIndData : expIndDataList) {
            // Exit if selected group is empty
            if (selectedPositionMap.size() < 1) {
                break;
            }
            DynamicObject dynamicObject = (DynamicObject) expIndData;
            // Expand the item itself in case there's duplicate key, eg "Mainan & Hobi Lainnya" and "Mainan & Hobi"
            Boolean checked = dynamicFilterView.getCheckedPosition(dynamicObject.getKey());
            if (checked != null && checked) {
                selectedPositionMap.remove(dynamicObject.getKey());
                groupChecked = true;
                groupKeySet.add(Integer.parseInt(dynamicObject.getKey()));
            }

            boolean childChecked = findCheckedParentList((List<ExpIndData>) dynamicObject.getChildren(), selectedPositionMap, groupKeySet);
            // Expand parent if the child selected
            if (childChecked) {
                groupKeySet.add(Integer.parseInt(dynamicObject.getKey()));
                groupChecked = true;
            }
        }
        return groupChecked;
    }

    /**
     * Collapse all group except checked category
     *
     * @param groupsNum
     * @param parentExpandKeyList
     */
    private void collapseUncheckedGroup(List<Integer> groupsNum, Set<Integer> parentExpandKeyList) {
        boolean notify = isNotifyOnChange();
        setNotifyOnChange(false);
        for (int i = groupsNum.size() - 1; i >= 0; i--) {
            DynamicObject dynamicObject = (DynamicObject) getItemAt(groupsNum.get(i));
            if (!parentExpandKeyList.contains(Integer.valueOf(dynamicObject.getKey()))) {
                collapseGroup(groupsNum.get(i));
            }
        }
        setNotifyOnChange(notify);
    }
}

