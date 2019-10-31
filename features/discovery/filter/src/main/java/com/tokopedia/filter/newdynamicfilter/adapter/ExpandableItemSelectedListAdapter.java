package com.tokopedia.filter.newdynamicfilter.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.helper.RatingHelper;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class ExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<ExpandableItemSelectedListAdapter.ViewHolder> {

    private List<Option> selectedOptionsList = new ArrayList<>();
    private DynamicFilterView filterView;

    public ExpandableItemSelectedListAdapter(DynamicFilterView filterView) {
        this.filterView = filterView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.selected_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(selectedOptionsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return selectedOptionsList.size();
    }

    public void setSelectedOptionsList(List<Option> selectedOptionsList) {
        this.selectedOptionsList = selectedOptionsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DeletableItemView selectedItem;

        public ViewHolder(View itemView) {
            super(itemView);
            selectedItem = itemView.findViewById(R.id.selected_item);
        }

        public void bind(final Option option, final int position) {
            if (Option.KEY_RATING.equals(option.getKey())) {
                int ratingCount = Integer.parseInt(option.getName());
                selectedItem.setItemDrawable(RatingHelper.getRatingDrawable(ratingCount));
            } else {
                selectedItem.setItemName(option.getName());
            }

            selectedItem.setOnDeleteListener(() -> {
                filterView.removeSelectedOption(option);
                selectedOptionsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            });
        }
    }
}
