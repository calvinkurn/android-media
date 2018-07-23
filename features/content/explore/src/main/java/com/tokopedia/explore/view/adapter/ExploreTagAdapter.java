package com.tokopedia.explore.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreTagViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreTagAdapter extends RecyclerView.Adapter<ExploreTagAdapter.ViewHolder> {

    private List<ExploreTagViewModel> list;
    private ContentExploreContract listener;

    @Inject
    public ExploreTagAdapter() {
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
        holder.tag.setText(list.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ExploreTagViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(ContentExploreContract listener) {
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
