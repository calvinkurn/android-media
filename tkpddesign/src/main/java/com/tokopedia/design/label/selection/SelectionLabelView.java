package com.tokopedia.design.label.selection;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.label.LabelView;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public abstract class SelectionLabelView<T> extends BaseCustomView {

    private LabelView labelView;
    private RecyclerView recyclerView;

    private SelectionListAdapter<T> adapter;

    private String titleText;

    protected abstract SelectionListAdapter getSelectionListAdapter();

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = getSelectionListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(labelView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(titleText)) {
            labelView.setTitle(titleText);
        }
        invalidate();
        requestLayout();
    }

    public void setSelectionDataList(List<T> selectionDataList) {
        adapter.setSelectionDataList(selectionDataList);
    }
}