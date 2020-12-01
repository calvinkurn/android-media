package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private EmptySearchModel emptySearchModel;
    private LoadingModel loadingModel;
    private final InboxReputationTypeFactory typeFactory;

    public InboxReputationAdapter(InboxReputationTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.loadingModel = new LoadingModel();
        this.emptySearchModel = new EmptySearchModel();
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

    public void setList(List<InboxReputationItemUiModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(List<InboxReputationItemUiModel> list,
                        SellerMigrationReviewModel sellerMigrationReviewModel) {
        this.list.clear();
        if (sellerMigrationReviewModel != null) {
            this.list.add(sellerMigrationReviewModel);
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<InboxReputationItemUiModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void showEmpty(String title, String buttonText, View.OnClickListener onClickListener) {
        emptySearchModel.setTitle(title);
        emptySearchModel.setButtonText(buttonText);
        emptySearchModel.setButtonListener(onClickListener);
        this.list.add(emptySearchModel);
    }

    public void removeEmpty() {
        this.list.remove(emptySearchModel);
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

    public void showLoadingFull() {
        this.list.add(loadingModel);
    }

    public void removeLoadingFull() {
        this.list.remove(loadingModel);
    }

    public void clearList() {
        this.list.clear();
    }

    public void showEmpty(String title) {
        emptySearchModel.setTitle(title);
        emptySearchModel.setButtonText("");
        emptySearchModel.setButtonListener(null);
        this.list.add(emptySearchModel);
    }

    public List<Visitable> getlist() {
        return list;
    }

    public boolean isEmpty() {
        return list.contains(emptySearchModel);
    }
}
