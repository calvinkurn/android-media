package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tokopedia.base.list.seller.view.old.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsBasicRadioButtonAdapter extends BaseLinearRecyclerViewAdapter {

    public interface Callback {

        void onItemSelected(RadioButtonItem radioButtonItem, int position);

    }

    private static final int VIEW_DATA = 100;

    private List<RadioButtonItem> data;
    private int selectedPosition;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(List<RadioButtonItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public TopAdsBasicRadioButtonAdapter() {
        data = new ArrayList<>();
        selectedPosition = 0;
    }

    @Override
    public int getItemCount() {
        if (data == null) { // data might come empty, because loading from net
            return super.getItemCount();
        }
        return data.size() + super.getItemCount();
    }

    public boolean isEmpty(){
        return (data == null || data.size() == 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_ads_basic_checkbox, viewGroup, false));
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
        if (null== data|| data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    private void bindProduct(final ViewHolder holder, int position) {
        final RadioButtonItem radioButtonItem = data.get(position);
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setText(MethodChecker.fromHtml(radioButtonItem.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(radioButtonItem, holder.getAdapterPosition());
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(radioButtonItem, holder.getAdapterPosition());
            }
        });
    }

    private void onSelectItem(RadioButtonItem radioButtonItem, int position) {
        selectedPosition = position;
        notifyDataSetChanged();
        if (callback != null) {
            callback.onItemSelected(radioButtonItem, position);
        }
    }

    public RadioButtonItem getSelectedItem() {
        return data.get(selectedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;

        public ViewHolder(View view) {
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.radio_button);
        }
    }
}