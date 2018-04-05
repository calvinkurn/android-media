package com.tokopedia.abstraction.base.view.adapter.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alvarisi
 */
public class BaseListAdapter<T extends Visitable, F extends AdapterTypeFactory> extends BaseAdapter<F> {

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
                        } catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        super.onBindViewHolder(holder,position);
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
                T item = (T) visitable;
                list.add(item);
            } catch (ClassCastException exception) {
                exception.printStackTrace();
            }
        }
        return list;
    }

    public int getDataSize(){
        return getData().size();
    }

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);
    }

}