package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> elements;
    private final FavoriteTypeFactory mFavoriteTypeFactory;

    public FavoriteAdapter(FavoriteTypeFactory favoriteTypeFactory) {
        this.mFavoriteTypeFactory = favoriteTypeFactory;
        this.elements = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return mFavoriteTypeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(elements.get(position));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return elements.get(position).type(mFavoriteTypeFactory);
    }

    public void setElements(List<Visitable> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
        this.notifyDataSetChanged();
    }

    public void addElement(int position, Visitable element) {
        this.elements.add(position, element);
        this.notifyDataSetChanged();
    }

}
