package com.tokopedia.favorite.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteAdapter extends BaseAdapter<FavoriteTypeFactory> {
    private final FavoriteTypeFactory favoriteTypeFactory;

    public FavoriteAdapter(FavoriteTypeFactory adapterTypeFactory, List<Visitable> data) {
        super(adapterTypeFactory, data);
        this.favoriteTypeFactory = adapterTypeFactory;
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


    public void clearData() {
        visitables.clear();
    }
}
