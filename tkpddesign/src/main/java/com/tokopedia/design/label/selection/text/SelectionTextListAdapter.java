package com.tokopedia.design.label.selection.text;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.design.label.selection.SelectionListAdapter;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class SelectionTextListAdapter extends SelectionListAdapter<String> {

    @Override
    public SelectionTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deletable_selection, parent, false);
        return new SelectionTextViewHolder(view);
    }
}