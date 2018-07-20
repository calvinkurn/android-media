package com.tokopedia.explore.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.explore.view.listener.ExploreFragmentListener;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreImageAdapter extends RecyclerView.Adapter<ExploreImageAdapter.ViewHolder> {

    private List<ExploreImageViewModel> list;
    private ExploreFragmentListener listener;

    @Inject
    public ExploreImageAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ExploreImageViewModel> getList() {
        return list;
    }

    public void setList(List<ExploreImageViewModel> list) {
        this.list = list;
    }

    public void setListener(ExploreFragmentListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
