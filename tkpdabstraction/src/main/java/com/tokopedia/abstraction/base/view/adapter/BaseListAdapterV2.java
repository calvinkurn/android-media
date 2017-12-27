package com.tokopedia.abstraction.base.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alvarisi
 */
public class BaseListAdapterV2<T extends Visitable, F extends AdapterTypeFactory> extends BaseAdapter {

    private OnAdapterInteractionListener onAdapterInteractionListener;
    private F baseListAdapterTypeFactory;

    public BaseListAdapterV2(F baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, new ArrayList<Visitable>());
        this.baseListAdapterTypeFactory = baseListAdapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return baseListAdapterTypeFactory.createViewHolder(view, viewType);
    }

    public void setOnAdapterInteractionListener(OnAdapterInteractionListener onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterInteractionListener != null) {
                    try {
                        onAdapterInteractionListener.onItemClicked(visitables.get(holder.getAdapterPosition()));
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.bind(visitables.get(position));
    }

    public void addData(List<T> visitables) {
        this.visitables.addAll(visitables);
        notifyDataSetChanged();
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

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);
    }
}