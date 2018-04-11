package com.tokopedia.abstraction.base.view.adapter.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alvarisi
 */
public class BaseListAdapter<T, F extends AdapterTypeFactory> extends BaseAdapter<F> {

    private static final List<Class> REGISTERED_NOT_DATA_CLASSES = Arrays.asList(new Class[]{
            EmptyModel.class, EmptyResultViewModel.class, ErrorNetworkModel.class,
            LoadingModel.class, LoadingMoreModel.class});

    private OnAdapterInteractionListener<T> onAdapterInteractionListener;
    private boolean nonDataElementOnLastOnly = true;

    public BaseListAdapter(F baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public BaseListAdapter(F baseListAdapterTypeFactory, OnAdapterInteractionListener<T> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory);
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    public void setOnAdapterInteractionListener(OnAdapterInteractionListener<T> onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    public void setNonDataElementOnLastOnly(boolean nonDataElementOnLastOnly) {
        this.nonDataElementOnLastOnly = nonDataElementOnLastOnly;
    }

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        if (onAdapterInteractionListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAdapterInteractionListener != null) {
                        try {
                            T item = (T) visitables.get(holder.getAdapterPosition());
                            onAdapterInteractionListener.onItemClicked(item);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }

    /**
     * this to remove loading/empty/error (all non T data) in adapter
     */
    public void clearAllNonDataElement() {
        for (int i = visitables.size() - 1; i >= 0; i--) {
            try {
                T item = (T) visitables.get(i);
                if (nonDataElementOnLastOnly) {
                    break;
                }
            } catch (ClassCastException cce) {
                visitables.remove(i);
            }
        }
    }

    public List<T> getData() {
        List<T> list = new ArrayList<>();
        for (Visitable visitable : this.visitables) {
            try {
                if (!REGISTERED_NOT_DATA_CLASSES.contains(visitable.getClass())) {
                    T item = (T) visitable;
                    list.add(item);
                }
            } catch (ClassCastException exception) {
                exception.printStackTrace();
            }
        }
        return list;
    }

    public int getDataSize() {
        return getData().size();
    }

    public boolean isContainData() {
        for (Visitable visitable : visitables) {
            if (!REGISTERED_NOT_DATA_CLASSES.contains(visitable.getClass())) {
                return true;
            }
        }
        return false;
    }

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);
    }
}