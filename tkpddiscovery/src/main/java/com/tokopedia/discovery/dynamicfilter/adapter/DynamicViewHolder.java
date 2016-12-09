package com.tokopedia.discovery.dynamicfilter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.discovery.model.DynamicObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noiz354 on 7/13/16.
 */
public class DynamicViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.dynamic_parent_view_holder_text)
    TextView dynamicParentViewHolderText;
    @BindView(R2.id.dynamic_parent_view_holder_checkbox)
    CheckBox dynamicParentViewHolder;
    @BindView(R2.id.container)
    LinearLayout containerLayout;
    DynamicObject dynamicObject;

    /**
     * Public constructor that takes in an ItemView along with an implementation of
     * ParentItemClickListener to handle the clicks of either the Parent item or the custom defined
     * view.
     *
     * @param itemView
     */
    public DynamicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public DynamicObject getDynamicObject() {
        return dynamicObject;
    }

    public void setDynamicObject(DynamicObject dynamicObject) {
        this.dynamicObject = dynamicObject;
    }

    public void setCheckboxVisibility(int visibility){
        dynamicParentViewHolder.setVisibility(visibility);
    }

    public void setPaddingLeft(int paddingLeft) {
        int pad = getPaddingPixels(10);
        containerLayout.setPadding(getPaddingPixels(paddingLeft), pad, pad, pad);
    }

    public int getPaddingPixels(int dpValue) {
        // Get the screen's density scale
        final float scale = containerLayout.getContext().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dpValue * scale + 0.5f);
    }
}