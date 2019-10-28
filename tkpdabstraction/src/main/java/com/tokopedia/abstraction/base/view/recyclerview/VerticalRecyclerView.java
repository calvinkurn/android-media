package com.tokopedia.abstraction.base.view.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;

/**
 * Created by User on 11/2/2017.
 */

public class VerticalRecyclerView extends RecyclerView {

    protected LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private boolean useLeftPadding;

    public VerticalRecyclerView(Context context) {
        super(context);
        init();
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(attrs);
        init();
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttrs(attrs);
        init();
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalRecyclerView);
        try {
            useLeftPadding = styledAttributes.getBoolean(R.styleable.VerticalRecyclerView_has_divider_left_padding, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        dividerItemDecoration = new DividerItemDecoration(getContext());
        dividerItemDecoration.setUsePaddingLeft(useLeftPadding);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.setLayoutManager(linearLayoutManager);
        ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            this.addItemDecoration(itemDecoration);
        }
    }

    @Override
    public void removeItemDecoration(ItemDecoration decor) {
        super.removeItemDecoration(decor);
    }

    protected ItemDecoration getItemDecoration() {
        return dividerItemDecoration;
    }

    public void clearItemDecoration(){
        removeItemDecoration(getItemDecoration());
    }

}
