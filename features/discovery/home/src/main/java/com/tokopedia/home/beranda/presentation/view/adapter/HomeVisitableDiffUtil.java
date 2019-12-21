package com.tokopedia.home.beranda.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class HomeVisitableDiffUtil extends DiffUtil.ItemCallback<HomeVisitable> {
    @Override
    public boolean areItemsTheSame(@NonNull HomeVisitable oldItem, @NonNull HomeVisitable newItem) {
        return oldItem.visitableId().equals(oldItem.visitableId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull HomeVisitable oldItem, @NonNull HomeVisitable newItem) {
        return oldItem.visitableId().equals(oldItem.visitableId());
    }
}
