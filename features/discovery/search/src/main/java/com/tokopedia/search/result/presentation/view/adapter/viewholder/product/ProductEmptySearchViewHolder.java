package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import androidx.annotation.LayoutRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.EmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.topads.sdk.base.Config;

import java.util.ArrayList;
import java.util.List;

public class ProductEmptySearchViewHolder extends EmptySearchViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_empty_state_product;

    private ProductSelectedFilterAdapter productSelectedFilterAdapter;

    public ProductEmptySearchViewHolder(View view, EmptyStateListener emptyStateListener, BannerAdsListener bannerAdsListener, Config topAdsConfig) {
        super(view, emptyStateListener, bannerAdsListener, topAdsConfig);
    }

    @Override
    protected void initSelectedFilterRecyclerView() {
        ChipsLayoutManager layoutManager = ChipsLayoutManager.newBuilder(itemView.getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        int staticDimen8dp = itemView.getContext().getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8);
        selectedFilterRecyclerView.addItemDecoration(new SpacingItemDecoration(staticDimen8dp));
        selectedFilterRecyclerView.setLayoutManager(layoutManager);
        ViewCompat.setLayoutDirection(selectedFilterRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
        productSelectedFilterAdapter = new ProductSelectedFilterAdapter(emptyStateListener);
        selectedFilterRecyclerView.setAdapter(productSelectedFilterAdapter);
    }

    @Override
    protected void populateSelectedFilterToRecylerView(List<Option> selectedFilterOptionList) {
        productSelectedFilterAdapter.setOptionList(selectedFilterOptionList);
    }

    private static class ProductSelectedFilterAdapter extends RecyclerView.Adapter<ProductSelectedFilterItemViewHolder> {

        private List<Option> optionList = new ArrayList<>();
        private EmptyStateListener clickListener;

        public ProductSelectedFilterAdapter(EmptyStateListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        @Override
        public ProductSelectedFilterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_product_empty_state_selected_filter_item, parent, false);
            return new ProductSelectedFilterItemViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(ProductSelectedFilterItemViewHolder holder, int position) {
            holder.bind(optionList.get(position));
        }

        @Override
        public int getItemCount() {
            return optionList.size();
        }
    }

    private static class ProductSelectedFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView filterText;
        private final EmptyStateListener clickListener;

        public ProductSelectedFilterItemViewHolder(View itemView, EmptyStateListener clickListener) {
            super(itemView);
            filterText = itemView.findViewById(R.id.filter_text);
            this.clickListener = clickListener;
        }

        public void bind(final Option option) {
            filterText.setText(option.getName());
            filterText.setOnClickListener(view -> clickListener.onSelectedFilterRemoved(option.getUniqueId()));
        }
    }
}
