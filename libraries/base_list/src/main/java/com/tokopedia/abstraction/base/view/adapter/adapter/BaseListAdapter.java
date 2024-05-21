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

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        if (onAdapterInteractionListener != null && isItemClickableByDefault()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAdapterInteractionListener != null) {
                        try {
                            T item = (T) visitables.get(holder.getAdapterPosition());
                            onAdapterInteractionListener.onItemClicked(item);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        } catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }

    protected boolean isItemClickableByDefault(){
        return true;
    }

    /**
     * this to remove loading/empty/error (all non T data) in adapter
     */
    public void clearAllNonDataElement() {
        if (hasNonDataElementAtLastIndex()) {
            visitables.remove(getLastIndex());
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getData() {
        boolean hasNonDataElement = hasNonDataElementAtLastIndex();
        if (hasNonDataElement) {
            return new ArrayList((visitables.subList(0, visitables.size() - 1)));
        } else {
            return (ArrayList<T>) visitables;
        }
    }

    private boolean hasNonDataElementAtLastIndex() {
        if (visitables.size() > 0) {
            Visitable visitable = visitables.get(getLastIndex());
            if (REGISTERED_NOT_DATA_CLASSES.contains(visitable.getClass())) {
                return true;
            }
        }
        return false;
    }

    public int getDataSize() {
        if (hasNonDataElementAtLastIndex()) {
            return visitables.size() - 1;
        } else {
            return visitables.size();
        }
    }

    public boolean isContainData() {
        return visitables.size() > 0 && !hasNonDataElementAtLastIndex();
    }

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);
    }
}