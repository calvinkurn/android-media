package com.tokopedia.digital.newcart.presentation.compoundview.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder.DigitalCartDetailTitleViewHolder;
import com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder.DigitalCartDetailViewHolder;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAdditionalInfo;
import com.tokopedia.digital.newcart.presentation.model.cart.CartItemDigital;

import java.util.ArrayList;
import java.util.List;

public class DigitalCartDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Visitable> visitables;

    public DigitalCartDetailAdapter() {
        this.visitables = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DigitalCartDetailTitleViewHolder.LAYOUT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    DigitalCartDetailTitleViewHolder.LAYOUT, parent, false);
            return new DigitalCartDetailTitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    DigitalCartDetailViewHolder.LAYOUT, parent, false);
            return new DigitalCartDetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == DigitalCartDetailTitleViewHolder.LAYOUT) {
            ((DigitalCartDetailTitleViewHolder) holder).bind((CartAdditionalInfo) visitables.get(position));
        } else if (holder.getItemViewType() == DigitalCartDetailViewHolder.LAYOUT) {
            ((DigitalCartDetailViewHolder) holder).bind((CartItemDigital) visitables.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return visitables.size();
    }

    public void addInfos(List<Visitable> visitables) {
        this.visitables.addAll(visitables);
    }

    public void setInfos(List<Visitable> visitables) {
        this.visitables = visitables;
    }

    private void removeInfos() {
        this.visitables.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (visitables.get(position) instanceof CartItemDigital) {
            return DigitalCartDetailViewHolder.LAYOUT;
        } else {
            return DigitalCartDetailTitleViewHolder.LAYOUT;
        }
    }
}
