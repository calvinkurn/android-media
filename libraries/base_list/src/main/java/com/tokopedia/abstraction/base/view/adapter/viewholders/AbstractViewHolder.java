package com.tokopedia.abstraction.base.view.adapter.viewholders;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener;

import java.util.List;


/**
 * @author kulomady on 1/24/17.
 */
public abstract class AbstractViewHolder<T extends Visitable> extends RecyclerView.ViewHolder implements IAdsViewHolderTrackListener {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);
    public int visibilityPercentage = 0;

    public RecyclerView rvHolder;

    /*
    This method is used to bind the view holder when only some parts of the model is changed.
    This way, the view holder won't be fully bind, you can control which part of it to re-bind.
    You can refer to https://medium.com/livefront/recyclerview-trick-selectively-bind-viewholders-with-payloads-4b28e3d2cce8
    on how to use it.

    Override this method to do the partial bind
     */
    public void bind(T element, @NonNull List<Object> payloads) {
    }


    /*
    Override this to do recycle and clear image resources
     */
    public void onViewRecycled() {
    }

    protected String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    protected String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }

    @Override
    public void onViewAttachedToWindow() {}

    public void onViewAttachedToWindow(T element) {}

    @Override
    public void onViewDetachedFromWindow(int visiblePercentage) {}

    public void setRecyclerView(RecyclerView recyclerView) {
        this.rvHolder = recyclerView;
    }

    public void onViewDetachedFromWindow(T element, int visiblePercentage) {}

    @Override
    public void setVisiblePercentage(int visiblePercentage) {
        this.visibilityPercentage = visiblePercentage;
    }


    public int getVisiblePercentage() {
        return visibilityPercentage;
    }
}
