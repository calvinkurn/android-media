package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsCreditAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int DEFAULT_CHECKED_POSITION = -1;
    private static final int VIEW_DATA = 100;

    private List<DataCredit> data;
    private int checkedPosition;

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public TopAdsCreditAdapter() {
        data = new ArrayList<>();
        checkedPosition = DEFAULT_CHECKED_POSITION;
    }

    public boolean isChecked() {
        return checkedPosition >= 0;
    }

    public void setData(List<DataCredit> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_ads_credit, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_DATA:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    private void bindProduct(final ViewHolder holder, int position) {
        DataCredit dataCredit = data.get(position);
        holder.radioButton.setChecked(position == checkedPosition);
        holder.radioButton.setText(dataCredit.getProductPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
    }

    private void onSelectItem(int position) {
        checkedPosition = position;
        notifyDataSetChanged();
    }

    public DataCredit getSelectedCredit() {
        return data.get(checkedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;

        public ViewHolder(View view) {
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.radio_button);
        }
    }
}