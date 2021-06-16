package com.tokopedia.kol.feature.comment.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final KolCommentTypeFactory typeFactory;
    private LoadingModel loadingModel;

    @Inject
    public KolCommentAdapter(KolCommentTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
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


    public void showLoading() {
        list.add(loadingModel);
        notifyItemInserted(list.size());
    }

    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    public void removeLoading() {
        list.remove(loadingModel);
        notifyItemRemoved(list.size());
    }

    public void setList(ArrayList<Visitable> list) {
        this.list.addAll(list);
    }

    public KolCommentHeaderViewModel getHeader() {
        if (list.size() == 0) return null;
        else if (list.get(0) instanceof KolCommentHeaderViewModel)
            return (KolCommentHeaderViewModel) list.get(0);
        else
            return null;
    }

    public void addList(ArrayList<Visitable> list) {
        this.list.addAll(1, list);
        notifyItemRangeInserted(1, list.size());
    }

    public KolCommentProductViewModel getFooter() {
        if (list.get(list.size() - 1) instanceof KolCommentHeaderViewModel)
            return (KolCommentProductViewModel) list.get(list.size() - 1);
        else
            return null;
    }

    public void addHeader(KolCommentHeaderViewModel header) {
        this.list.add(0, header);
        notifyItemInserted(0);
    }

    public void addHeaderNew(KolCommentHeaderNewModel header) {
        this.list.add(0, header);
        notifyItemInserted(0);
    }

    public void addItem(Visitable visitable) {
        this.list.add(visitable);
        notifyItemInserted(this.list.size() - 1);
    }

    public void deleteItem(int adapterPosition) {
        this.list.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
