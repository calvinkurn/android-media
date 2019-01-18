package com.tokopedia.affiliate.feature.explore.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 16/01/19.
 */
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.Holder> {

    public interface OnSortItemClicked {
        void onItemClicked(SortViewModel sort);
    }

    private List<SortViewModel> sortList = new ArrayList<>();
    private OnSortItemClicked listener;

    public SortAdapter(OnSortItemClicked listener, List<SortViewModel> sortList, SortViewModel selectedSort) {
        this.sortList = sortList;
        this.listener = listener;
        setCurrentItemSelected(selectedSort);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_sort, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SortViewModel sort = sortList.get(position);
        initView(holder, sort);
        initViewListener(holder, sort);
    }

    private void initView(Holder holder, SortViewModel sort) {
        holder.tvText.setText(MethodChecker.fromHtml(sort.getText()));
        holder.ivCheck.setVisibility(sort.isSelected() ? View.VISIBLE : View.GONE);
    }

    private void initViewListener(Holder holder, SortViewModel sort) {
        holder.layout.setOnClickListener(view ->{
            setCurrentItemSelectedAndReload(sort);
            listener.onItemClicked(sort);
        });
    }

    @Override
    public int getItemCount() {
        return sortList.size();
    }

    private void setCurrentItemSelected(SortViewModel sort) {
        for (SortViewModel item: sortList) {
            item.setSelected(item.getText().equals(sort.getText()));
        }
    }

    private void setCurrentItemSelectedAndReload(SortViewModel sort) {
        setCurrentItemSelected(sort);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvText;
        ImageView ivCheck;
        ConstraintLayout layout;

        public Holder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_sort);
            ivCheck = itemView.findViewById(R.id.iv_check);
            layout = itemView.findViewById(R.id.item_sort);
        }
    }
}
