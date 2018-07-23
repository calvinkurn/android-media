package com.tokopedia.explore.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreImageAdapter extends RecyclerView.Adapter<ExploreImageAdapter.ViewHolder> {

    private List<ExploreImageViewModel> list;
    private ContentExploreContract listener;

    @Inject
    public ExploreImageAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_explore_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.image, list.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ExploreImageViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(ContentExploreContract listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
