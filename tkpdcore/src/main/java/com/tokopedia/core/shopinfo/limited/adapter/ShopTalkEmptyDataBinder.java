package com.tokopedia.core.shopinfo.limited.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.util.DataBindAdapter;

/**
 * Created by Nisie on 2/26/16.
 */
public class ShopTalkEmptyDataBinder extends NoResultDataBinder {

    public ShopTalkEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_list, parent, false);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new EmptyViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        emptyViewHolder.emptyTitleTextView.setText(emptyViewHolder.emptyTitleTextView.getContext().getString(R.string.discussion_shop_label_empty_list_title));
        emptyViewHolder.emptyContentTextView.setText(emptyViewHolder.emptyContentTextView.getContext().getString(R.string.discussion_shop_label_empty_list_content));
        emptyViewHolder.emptyContentTextView.setVisibility(View.VISIBLE);
    }

    public static class EmptyViewHolder extends ViewHolder {
        private TextView emptyTitleTextView;
        private TextView emptyContentTextView;

        public EmptyViewHolder(View view) {
            super(view);
            emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
            emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        }
    }
}