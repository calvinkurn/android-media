package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private final InboxReputationDetailTypeFactory typeFactory;

    public InboxReputationDetailAdapter(InboxReputationDetailTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
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
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<Visitable> list) {
        this.list.addAll(list);
    }

    public void showEmpty() {
        this.list.add(emptyModel);
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
    }

    public void showLoading() {
        this.list.add(loadingModel);
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public void addHeader(InboxReputationDetailHeaderUiModel model) {
        this.list.add(model);
    }

    public void clearList() {
        this.list.clear();
    }

    public InboxReputationDetailHeaderUiModel getHeader() {
        if (list.get(0) instanceof InboxReputationDetailHeaderUiModel)
            return (InboxReputationDetailHeaderUiModel) list.get(0);
        else
            return null;
    }

    public List<Visitable> getList() {
        return list;
    }
}
