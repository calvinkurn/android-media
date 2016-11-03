package com.tokopedia.tkpd.dynamicfilter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.dynamicfilter.model.DynamicObject;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.tkpd.network.apiservices.ace.apis.BrowseApi;

/**
 * Created by Tokopedia on 9/5/2016.
 * Modified by erry
 */
public class DynamicCategoryAdapter extends MultiLevelExpIndListAdapter {

    /**
     * This is called when the user click on an item or group.
     */
    private final View.OnClickListener mListener;

    private final Context mContext;

    public DynamicCategoryAdapter(Context mContext, View.OnClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.dynamic_parent_view_holder_layout;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new DynamicViewHolder(v);
        v.setOnClickListener(mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final DynamicViewHolder parentViewHolder = (DynamicViewHolder) viewHolder;
        final DynamicObject dynamicObject = (DynamicObject) getItemAt(position);
        parentViewHolder.setDynamicObject(dynamicObject);
        parentViewHolder.dynamicParentViewHolderText.setText(dynamicObject.getParentText());
        parentViewHolder.dynamicParentViewHolder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                parentViewHolder.getDynamicObject().setChecked(isChecked);
                Context context = parentViewHolder.itemView.getContext();
                if (context != null && context instanceof DynamicFilterView) {
                    if (isChecked) {
                        ((DynamicFilterView) context).putSelectedFilter(BrowseApi.SC, getSelectedIds());
                        ((DynamicFilterView) context).saveCheckedPosition(dynamicObject.getKey(), true);
                    } else {
                        ((DynamicFilterView) context).removeCheckedPosition(dynamicObject.getKey());
                        if (getSelectedIds().isEmpty()) {
                            ((DynamicFilterView) context).removeSelecfedFilter(BrowseApi.SC);
                        } else {
                            ((DynamicFilterView) context).putSelectedFilter(BrowseApi.SC, getSelectedIds());
                        }
                    }
                }
            }
        });
        if (mContext != null && mContext instanceof DynamicFilterView) {
            Boolean isChecked = ((DynamicFilterView) mContext).getCheckedPosition(dynamicObject.getKey());
            if (isChecked != null && isChecked) {
                parentViewHolder.dynamicParentViewHolder.setChecked(true);
            } else {
                parentViewHolder.dynamicParentViewHolder.setChecked(false);
            }
        }
        if (dynamicObject.getIndentation() == 0) {
            parentViewHolder.setPaddingLeft(0);
            parentViewHolder.setCheckboxVisibility(View.GONE);
            parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleGroup(position);
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
                        parentViewHolder.dynamicParentViewHolder.setChecked(!parentViewHolder.dynamicParentViewHolder.isChecked());
                    }
                });
            }
        } else {
            parentViewHolder.setCheckboxVisibility(View.VISIBLE);
            parentViewHolder.setPaddingLeft(25);
            parentViewHolder.dynamicParentViewHolderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentViewHolder.dynamicParentViewHolder.setChecked(!parentViewHolder.dynamicParentViewHolder.isChecked());
                }
            });
        }
    }

    private String getSelectedIds() {
        StringBuffer buffer = new StringBuffer();
        for (ExpIndData data : getData()) {
            DynamicObject object = (DynamicObject) data;
            if (object.isChecked()) {
                buffer.append(object.getDepId()).append(",");
            }
        }
        if (buffer.length() > 0) {
            return buffer.substring(0, buffer.length() - 1);
        } else {
            return buffer.toString();
        }
    }

    public void reset() {
        notifyItemRangeChanged(0, getItemCount());
    }
}
