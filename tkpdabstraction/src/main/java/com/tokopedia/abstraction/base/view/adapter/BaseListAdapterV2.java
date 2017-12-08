package com.tokopedia.abstraction.base.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;

/**
 * @author alvarisi
 */
public abstract class BaseListAdapterV2<T extends Visitable> extends BaseAdapter {
    private static int DEFAULT_ROW_PER_PAGE = 10;

    public interface OnAdapterInteractionListener<T> {
        void onItemClicked(T t);

        void loadData(int page, int currentDataSize, int rowPerPage);
    }

    private OnAdapterInteractionListener onAdapterInteractionListener;

    public BaseListAdapterV2(BaseAdapterTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory, new ArrayList<Visitable>());
    }

    public void setOnAdapterInteractionListener(OnAdapterInteractionListener onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    public void loadStartPage() {
        super.clearData();
        if (onAdapterInteractionListener != null) {
            onAdapterInteractionListener.loadData(1, visitables.size(), DEFAULT_ROW_PER_PAGE);
        }
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

}