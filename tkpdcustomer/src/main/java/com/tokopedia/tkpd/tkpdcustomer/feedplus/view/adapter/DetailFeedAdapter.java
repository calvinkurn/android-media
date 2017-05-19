package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.FeedDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/18/17.
 */

public class DetailFeedAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final FeedPlusDetailTypeFactory typeFactory;

    public DetailFeedAdapter(FeedPlusDetailTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);

        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    public void addList(ArrayList<FeedDetailViewModel> listProduct) {
        this.list.addAll(listProduct);
        notifyDataSetChanged();
    }
}
