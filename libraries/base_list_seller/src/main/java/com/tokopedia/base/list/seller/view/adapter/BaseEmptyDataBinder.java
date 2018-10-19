package com.tokopedia.base.list.seller.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.base.list.seller.R;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public abstract class BaseEmptyDataBinder extends NoResultDataBinder {

    protected String emptyTitleText;
    protected int errorDrawableRes;
    private String emptyContentText;
    private String emptyContentItemText;
    private String emptyButtonItemText;
    private Callback callback;

    public BaseEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        this.errorDrawableRes = R.drawable.ic_empty_state;
    }

    public BaseEmptyDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter);
        this.errorDrawableRes = errorDrawableRes;
    }

    public void setEmptyTitleText(String emptyTitleText) {
        this.emptyTitleText = emptyTitleText;
    }

    public void setEmptyContentText(String emptyContentText) {
        this.emptyContentText = emptyContentText;
    }

    public void setEmptyContentItemText(String emptyContentItemText) {
        this.emptyContentItemText = emptyContentItemText;
    }

    public void setEmptyButtonItemText(String emptyButtonItemText) {
        this.emptyButtonItemText = emptyButtonItemText;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_list, parent, false);
        ((ImageView) view.findViewById(R.id.no_result_image)).setImageResource(errorDrawableRes);
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
        if (!TextUtils.isEmpty(emptyTitleText)) {
            emptyViewHolder.emptyTitleTextView.setText(emptyTitleText);
        }
        if (!TextUtils.isEmpty(emptyContentText)) {
            emptyViewHolder.emptyContentTextView.setText(emptyContentText);
            emptyViewHolder.emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            emptyViewHolder.emptyContentTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(emptyContentItemText)) {
            emptyViewHolder.emptyContentItemTextView.setVisibility(View.VISIBLE);
            emptyViewHolder.emptyContentItemTextView.setText(emptyContentItemText);
            emptyViewHolder.emptyContentItemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onEmptyContentItemTextClicked();
                    }
                }
            });
        } else {
            emptyViewHolder.emptyContentItemTextView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(emptyButtonItemText)) {
            emptyViewHolder.emptyButtonItemButton.setVisibility(View.GONE);
        } else {
            emptyViewHolder.emptyButtonItemButton.setText(emptyButtonItemText);
            emptyViewHolder.emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onEmptyButtonClicked();
                    }
                }
            });
            emptyViewHolder.emptyButtonItemButton.setVisibility(View.VISIBLE);
        }
    }

    public interface Callback {

        void onEmptyContentItemTextClicked();

        void onEmptyButtonClicked();
    }

    public static class EmptyViewHolder extends ViewHolder {
        public TextView emptyTitleTextView;
        private TextView emptyContentTextView;
        private TextView emptyContentItemTextView;
        private Button emptyButtonItemButton;

        public EmptyViewHolder(View view) {
            super(view);
            emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
            emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
            emptyContentItemTextView = (TextView) view.findViewById(R.id.text_view_empty_content_item_text);
            emptyButtonItemButton = (Button) view.findViewById(R.id.button_add_promo);
        }
    }
}