package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.BaseAdapter;
import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteAdapter extends BaseAdapter {
    private final FavoriteTypeFactory favoriteTypeFactory;

    public FavoriteAdapter(BaseAdapterTypeFactory adapterTypeFactory, List<Visitable> data) {
        super(adapterTypeFactory, data);
        this.favoriteTypeFactory = (FavoriteTypeFactory) adapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return favoriteTypeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(favoriteTypeFactory);
    }

    public void setElement(int position, Visitable element) {
        visitables.set(position, element);
        notifyDataSetChanged();
//        notifyItemInserted(position);
    }

    public void setElement(List<Visitable> data) {
        visitables.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        visitables.clear();
    }

    public void addMoreData(List<Visitable> data) {
        final int positionStart = visitables.size() + 1;
        visitables.addAll(data);
        notifyItemRangeInserted(positionStart, data.size());
    }

}
