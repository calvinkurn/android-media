package com.tokopedia.core.people.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * Created on 5/31/16.
 */
public abstract class BaseView<Data, Presenter> extends FrameLayout {

    protected Presenter presenter;

    public BaseView(Context context) {
        super(context);
        initView(context);
        parseAttribute(context, null);
        setViewListener();
    }


    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        parseAttribute(context, attrs);
        setViewListener();
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutView();

    protected abstract void parseAttribute(Context context, AttributeSet attrs);

    protected abstract void setViewListener();

    public abstract void renderData(@NonNull Data data);

    public abstract void setPresenter(@NonNull Presenter presenter);
}
