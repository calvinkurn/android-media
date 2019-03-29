package com.tokopedia.common.travel.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.dialog.SearchSortModel;

import java.util.List;

/**
 * Created by nabillasabbaha on 21/08/18.
 */
public class TravelSearchSortAdapter extends RecyclerView.Adapter {

    private List<SearchSortModel> searchSortList;
    private ActionListener listener;

    public TravelSearchSortAdapter(List<SearchSortModel> searchSortList) {
        this.searchSortList = searchSortList;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_sort_search, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        SearchSortModel searchSortModel = searchSortList.get(position);

        itemViewHolder.sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SearchSortModel sortModel : searchSortList) {
                    if (sortModel.isSelected()) {
                        sortModel.setSelected(false);
                    }
                }
                searchSortModel.setSelected(true);
                notifyDataSetChanged();

                listener.onClickSortLabel(searchSortModel);
            }
        });
        itemViewHolder.sortLabel.setText(searchSortModel.getSortName());
        setViewSearchSort(searchSortModel, itemViewHolder);
    }

    private void setViewSearchSort(SearchSortModel searchSort, ItemViewHolder itemViewHolder) {
        itemViewHolder.checkboxImg.setVisibility(searchSort.isSelected() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return searchSortList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView sortLabel;
        private AppCompatImageView checkboxImg;
        private RelativeLayout sortLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            sortLabel = itemView.findViewById(R.id.sort_label);
            checkboxImg = itemView.findViewById(R.id.img_checked);
            sortLayout = itemView.findViewById(R.id.layout_sort);
        }
    }

    public interface ActionListener {
        void onClickSortLabel(SearchSortModel searchSortModel);
    }
}
