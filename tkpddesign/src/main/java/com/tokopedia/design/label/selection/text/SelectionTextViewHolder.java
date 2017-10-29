package com.tokopedia.design.label.selection.text;

import android.view.View;

import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.design.label.selection.SelectionViewHolder;

/**
 * Created by nathan on 10/29/17.
 */

public class SelectionTextViewHolder extends SelectionViewHolder<String> {

    public SelectionTextViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(String title, int position, DeletableItemView.OnDeleteListener deleteListener) {
        super.bind(title, position, deleteListener);
        selectedItem.setItemName(title);
    }
}