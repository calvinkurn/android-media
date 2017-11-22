package com.tokopedia.design.label.selection;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.label.LabelView;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public abstract class SelectionLabelView<T extends SelectionItem> extends BaseCustomView {

    public interface OnDeleteListener<T> {
        void onDelete(T t);
    }

    private SelectionListAdapter<T> adapter;

    private String titleText;
    LabelView labelView;

    public void setOnDeleteListener(final OnDeleteListener<T> onDeleteListener) {
        adapter.setOnDeleteListener(new SelectionListAdapter.OnDeleteListener<T>() {
            @Override
            public void onDelete(T t) {
                onDeleteListener.onDelete(t);
            }
        });
    }

    protected abstract SelectionListAdapter<T> getSelectionListAdapter();

    public SelectionLabelView(Context context) {
        super(context);
        init();
    }

    public SelectionLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SelectionLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SelectionLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.SelectionLabelView_slv_title);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_selection_label_view, this);
        labelView = (LabelView) view.findViewById(R.id.label_view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = getSelectionListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        if (!TextUtils.isEmpty(titleText)) {
            labelView.setTitle(titleText);
        }
    }

    public void setItemList(List<T> itemList) {
        adapter.setItemList(itemList);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public void setTitle(String title) {
        titleText = title;
        labelView.setTitle(titleText);
    }

    public void setContentText(String contentText) {
        labelView.setContent(contentText);
    }

    public void setArrow(boolean isShow) {
        labelView.setVisibleArrow(isShow);
    }
}