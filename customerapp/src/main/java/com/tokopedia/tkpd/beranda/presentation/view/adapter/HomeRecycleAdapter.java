package com.tokopedia.tkpd.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokopedia.core.base.adapter.BaseAdapter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends BaseAdapter {

    protected HomeAdapterFactory typeFactory;

    public HomeRecycleAdapter(HomeAdapterFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return visitables.size();
    }

    public void setItems(List<Visitable> items) {
        this.visitables = items;
        notifyDataSetChanged();
    }

    public Visitable getItem(int pos){
        return visitables.get(pos);
    }

    public List<Visitable> getItems(){
        return visitables;
    }
}
