package com.tokopedia.filter.newdynamicfilter.adapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<BottomSheetExpandableItemSelectedListAdapter.ViewHolder> {

    private List<Option> selectedOptionsList = new ArrayList<>();
    private BottomSheetDynamicFilterView filterView;
    private String filterTitle;

    public BottomSheetExpandableItemSelectedListAdapter(BottomSheetDynamicFilterView filterView, String filterTitle) {
        this.filterView = filterView;
        this.filterTitle = filterTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.bottom_sheet_selected_filter_item, parent, false);
        return new ViewHolder(view, filterView, this, filterTitle);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private ColorSampleView colorIcon;
        private View itemContainer;
        private BottomSheetDynamicFilterView filterView;
        private BottomSheetExpandableItemSelectedListAdapter adapter;
        private String filterTitle;
        private View ratingIcon;
        private View filterNewIcon;

        public ViewHolder(View itemView,
                          BottomSheetDynamicFilterView filterView,
                          BottomSheetExpandableItemSelectedListAdapter adapter,
                          String filterTitle) {
            super(itemView);

            initViewHolderViews(itemView);

            this.adapter = adapter;
            this.filterView = filterView;
            this.filterTitle = filterTitle;
        }

        private void initViewHolderViews(View itemView) {
            itemText = itemView.findViewById(R.id.filter_item_text);
            ratingIcon = itemView.findViewById(R.id.rating_icon);
            colorIcon = itemView.findViewById(R.id.color_icon);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
            filterNewIcon = itemView.findViewById(R.id.filter_new_icon);
        }

        public void bind(final Option option, final int position) {
            bindFilterNewIcon(option);

            bindRatingOption(option);

            bindColorOption(option);

            itemText.setText(option.getName());

            if (option.isCategoryOption()) {
                bindCategoryOption(option);
            } else {
                bindGeneralOption(option, position);
            }
        }

        private void bindFilterNewIcon(Option option) {
            if (option.isNew()) {
                filterNewIcon.setVisibility(View.VISIBLE);
            } else {
                filterNewIcon.setVisibility(View.GONE);
            }
        }

        private void bindRatingOption(Option option) {
            if (Option.KEY_RATING.equals(option.getKey())) {
                ratingIcon.setVisibility(View.VISIBLE);
            } else {
                ratingIcon.setVisibility(View.GONE);
            }
        }

        private void bindColorOption(Option option) {
            if (!TextUtils.isEmpty(option.getHexColor())) {
                colorIcon.setVisibility(View.VISIBLE);
                colorIcon.setColor(Color.parseColor(option.getHexColor()));
            } else {
                colorIcon.setVisibility(View.GONE);
            }
        }

        private void bindCategoryOption(final Option option) {
            final boolean isOptionSelected = filterView.isSelectedCategory(option);
            setItemContainerBackgroundResource(isOptionSelected);

            itemContainer.setOnClickListener(view -> {
                if (isOptionSelected) {
                    filterView.removeSelectedOption(option, filterTitle);
                } else {
                    filterView.selectCategory(option, filterTitle);
                }
                adapter.notifyDataSetChanged();
            });
        }

        private void bindGeneralOption(final Option option, final int position) {
            final boolean isOptionSelected = filterView.getFilterViewState(option.getUniqueId());
            setItemContainerBackgroundResource(isOptionSelected);

            itemContainer.setOnClickListener(view -> {
                boolean newCheckedState = !isOptionSelected;
                filterView.saveCheckedState(option, newCheckedState, filterTitle);
                adapter.notifyItemChanged(position);
            });
        }

        private void setItemContainerBackgroundResource(boolean isSelected) {
            if (isSelected) {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
        }
    }
}
