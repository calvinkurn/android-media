package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;

import java.util.ArrayList;
import java.util.List;

public class QuickFilterViewHolder extends AbstractViewHolder<QuickFilterViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_quick_filter_layout;
    private QuickFilterListener quickFilterListener;
    private QuickFilterAdapter quickFilterAdapter;
    private RecyclerView quickFilterListView;
    private Context context;

    public QuickFilterViewHolder(View itemView,
                                 QuickFilterListener quickFilterListener) {
        super(itemView);
        context = itemView.getContext();
        this.quickFilterListener = quickFilterListener;
        quickFilterListView = itemView.findViewById(R.id.quickFilterListView);
        initQuickFilterRecyclerView();
    }

    private void initQuickFilterRecyclerView() {
        quickFilterAdapter = new QuickFilterAdapter(quickFilterListener);
        quickFilterListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        quickFilterListView.setAdapter(quickFilterAdapter);
        quickFilterListView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
        ));
    }

    @Override
    public void bind(final QuickFilterViewModel element) {
        bindQuickFilterView(element);
    }

    private void bindQuickFilterView(final QuickFilterViewModel element) {
        if (!TextUtils.isEmpty(element.getFormattedResultCount())) {
            quickFilterAdapter.setFormattedResultCount(String.format(context.getString(R.string.result_count_template_text), element.getFormattedResultCount()));
        } else {
            quickFilterAdapter.setFormattedResultCount("");
        }

        quickFilterAdapter.setOptionList(element.getQuickFilterOptions());
    }

    private static class QuickFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER_PRODUCT_COUNT = 0;
        private static final int TYPE_ITEM_QUICK_FILTER = 1;
        private static final int HEADER_COUNT = 1;

        private List<Option> optionList = new ArrayList<>();
        private QuickFilterListener quickFilterListener;
        private String formattedResultCount;

        QuickFilterAdapter(QuickFilterListener quickFilterListener) {
            this.quickFilterListener = quickFilterListener;
        }

        void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        void setFormattedResultCount(String formattedResultCount) {
            this.formattedResultCount = formattedResultCount;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_QUICK_FILTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_quick_filter_item, parent, false);
                return new QuickFilterItemViewHolder(view, quickFilterListener);
            } else if (viewType == TYPE_HEADER_PRODUCT_COUNT) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_count_item, parent, false);
                return new ProductCountViewHolder(view);
            }
            throw new RuntimeException("Unknown view type " + viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProductCountViewHolder) {
                ((ProductCountViewHolder) holder).bind(formattedResultCount);
            } else if (holder instanceof QuickFilterItemViewHolder) {
                ((QuickFilterItemViewHolder) holder).bind(optionList.get(position - HEADER_COUNT));
            }
        }

        @Override
        public int getItemCount() {
            return optionList.size() + HEADER_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderPosition(position)) {
                return TYPE_HEADER_PRODUCT_COUNT;
            } else {
                return TYPE_ITEM_QUICK_FILTER;
            }
        }

        private boolean isHeaderPosition(int position) {
            return position < HEADER_COUNT;
        }
    }

    private static class QuickFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView quickFilterText;
        private View itemContainer;
        private View filterNewIcon;
        private final QuickFilterListener quickFilterListener;

        QuickFilterItemViewHolder(View itemView, QuickFilterListener quickFilterListener) {
            super(itemView);
            quickFilterText = itemView.findViewById(R.id.quick_filter_text);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
            filterNewIcon = itemView.findViewById(R.id.filter_new_icon);
            this.quickFilterListener = quickFilterListener;
        }

        public void bind(final Option option) {
            bindFilterNewIcon(option);

            quickFilterText.setText(option.getName());

            setBackgroundResource(option);

            quickFilterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quickFilterListener != null) {
                        quickFilterListener.onQuickFilterSelected(option);
                    }
                }
            });
        }

        private void bindFilterNewIcon(Option option) {
            if (option.isNew()) {
                filterNewIcon.setVisibility(View.VISIBLE);
            } else {
                filterNewIcon.setVisibility(View.GONE);
            }
        }

        private void setBackgroundResource(Option option) {
            if (quickFilterListener != null && quickFilterListener.isQuickFilterSelected(option)) {
                itemContainer.setBackgroundResource(R.drawable.search_quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.search_quick_filter_item_background_neutral);
            }
        }
    }

    private static class ProductCountViewHolder extends RecyclerView.ViewHolder {
        private TextView resultCountText;

        ProductCountViewHolder(View itemView) {
            super(itemView);
            resultCountText = itemView.findViewById(R.id.result_count_text_view);
        }

        public void bind(String formattedResultCount) {
            if (!TextUtils.isEmpty(formattedResultCount)) {
                resultCountText.setText(formattedResultCount);
                resultCountText.setVisibility(View.VISIBLE);
            } else {
                resultCountText.setVisibility(View.GONE);
            }
        }
    }
}
