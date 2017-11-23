package com.tokopedia.abstraction.base.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;

/**
 * Created by User on 11/2/2017.
 */

public class VerticalRecyclerView extends RecyclerView {

    protected LinearLayoutManager linearLayoutManager;

    public VerticalRecyclerView(Context context) {
        super(context);
        init();
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.setLayoutManager(linearLayoutManager);
        ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            this.addItemDecoration(itemDecoration);
        }
    }

    protected ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext());
    }

}
