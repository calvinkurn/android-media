package com.tokopedia.train.search.presentation.adapter;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.tkpdtrain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterAdapter extends RecyclerView.Adapter {

    private List<String> listObjectFilter;
    private ActionListener listener;
    private List<String> selectedFilter;

    public TrainFilterAdapter() {
        this.listObjectFilter = new ArrayList<>();
        this.selectedFilter = new ArrayList<>();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_train_filter, null);
        return new ItemViewFilter(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewFilter itemViewFilter = (ItemViewFilter) holder;
        itemViewFilter.itemName.setText(listObjectFilter.get(position));

        if (!selectedFilter.isEmpty()) {
            for (String selectFilter : selectedFilter) {
                if (itemViewFilter.itemName.getText().toString().equals(selectFilter))
                    itemViewFilter.checkBoxFilter.setChecked(true);
            }
        } else {
            itemViewFilter.checkBoxFilter.setChecked(false);
        }

        itemViewFilter.checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (itemViewFilter.checkBoxFilter.isChecked()) {
                    selectedFilter.add(listObjectFilter.get(position));
                } else {
                    selectedFilter.remove(listObjectFilter.get(position));
                }
                listener.onCheckChanged(selectedFilter);
            }
        });
        itemViewFilter.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemViewFilter.checkBoxFilter.isChecked()) {
                    itemViewFilter.checkBoxFilter.setChecked(false);
                } else {
                    itemViewFilter.checkBoxFilter.setChecked(true);
                }
                listener.onCheckChanged(selectedFilter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listObjectFilter.size();
    }

    static class ItemViewFilter extends RecyclerView.ViewHolder {

        private TextView itemName;
        private AppCompatCheckBox checkBoxFilter;
        private RelativeLayout itemContainer;

        public ItemViewFilter(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_title);
            checkBoxFilter = itemView.findViewById(R.id.checkbox);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }

    public void addList(List<String> listObjectFilter, List<String> selectedFilter) {
        if (selectedFilter == null) {
            selectedFilter = new ArrayList<>();
        }
        this.listObjectFilter.clear();
        this.listObjectFilter = listObjectFilter;
        this.selectedFilter = selectedFilter;
        notifyDataSetChanged();
    }

    public void removeListSelected() {
        this.selectedFilter.clear();
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onCheckChanged(List<String> listObjectFilter);
    }
}
