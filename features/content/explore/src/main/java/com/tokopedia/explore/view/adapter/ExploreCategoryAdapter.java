package com.tokopedia.explore.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreCategoryAdapter extends RecyclerView.Adapter<ExploreCategoryAdapter.ViewHolder> {

    private List<ExploreCategoryViewModel> list;
    private ContentExploreContract.View listener;

    @Inject
    public ExploreCategoryAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_explore_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.tag.getContext();
        holder.tag.setText(list.get(position).getName());
        holder.tag.setOnClickListener(v -> {
            listener.onCategoryClicked(
                    position,
                    list.get(position).getId(),
                    list.get(position).getName()
            );
        });

        if (list.get(position).isActive()) {
            holder.tag.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.explore_tag_selected));
        } else {
            holder.tag.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.explore_tag_neutral));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ExploreCategoryViewModel> getList() {
        return list;
    }

    public void setList(List<ExploreCategoryViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(ContentExploreContract.View listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag;

        ViewHolder(View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
        }
    }
}
