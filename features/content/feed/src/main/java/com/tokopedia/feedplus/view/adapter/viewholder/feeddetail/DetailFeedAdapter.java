package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/18/17.
 */

public class DetailFeedAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private LoadingMoreModel loadingMoreModel;
    private final FeedPlusDetailTypeFactory typeFactory;

    public DetailFeedAdapter(FeedPlusDetailTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
        this.loadingMoreModel = new LoadingMoreModel();
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

    public void addList(ArrayList<Visitable> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Visitable item) {
        int position = getItemCount();
        if (this.list.add(item)) {
            notifyItemInserted(position);
        }
    }

    public void remove(Visitable item) {
        int position = this.list.indexOf(item);
        if (this.list.remove(item)) {
            notifyItemRemoved(position);
        }
    }

    public void showEmpty() {
        add(emptyModel);
    }

    public void dismissEmpty() {
        remove(emptyModel);
    }

    public void showLoading() {
        add(loadingModel);
    }

    public void dismissLoading() {
        remove(loadingModel);
    }

    public void showLoadingMore() {
        add(loadingMoreModel);
    }

    public void dismissLoadingMore() {
        remove(loadingMoreModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public List<Visitable> getList() {
        return list;
    }
}
