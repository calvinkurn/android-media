package com.tokopedia.design.label.selection.text;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionListAdapter;
import com.tokopedia.design.label.selection.SelectionViewHolder;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class SelectionTextListAdapter extends SelectionListAdapter<SelectionItem<String>> {

    @Override
    public SelectionViewHolder<SelectionItem<String>> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_label_view, parent, false);
        return new SelectionTextViewHolder(view);
    }
}