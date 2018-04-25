package com.tokopedia.discovery.autocomplete;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

public class DefaultAutoCompleteViewHolder extends AbstractViewHolder<DefaultAutoCompleteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_simple_recyclerview;

    private DefaultAutoCompleteAdapter resultAdapter;

    RecyclerView recyclerView;

    private Context context;
    private final ItemClickListener mClickListener;



    public DefaultAutoCompleteViewHolder(View view, ItemClickListener clickListener) {
        super(view);
        this.context = itemView.getContext();
        this.mClickListener = clickListener;
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        resultAdapter = new DefaultAutoCompleteAdapter(mClickListener);
        recyclerView.setAdapter(resultAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void bind(DefaultAutoCompleteViewModel element) {
        resultAdapter.setViewModel(element);
    }
}
