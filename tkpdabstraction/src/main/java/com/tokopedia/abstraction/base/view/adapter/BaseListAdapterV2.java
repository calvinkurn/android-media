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
public class BaseListAdapterV2<T extends Visitable> extends BaseAdapter {

    private BaseListAdapterTypeFactory baseListAdapterTypeFactory;
    private OnAdapterInteractionListener onAdapterInteractionListener;

    public BaseListAdapterV2(BaseListAdapterTypeFactory baseListAdapterTypeFactory) {
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
        holder.bind(visitables.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterInteractionListener != null) {
                    onAdapterInteractionListener.onItemClicked(visitables.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public void addData(List<T> visitables) {
        this.visitables.addAll(visitables);
        notifyDataSetChanged();
    }

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);
    }
}