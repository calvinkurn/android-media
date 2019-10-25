package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_header_layout;
    private Ticker tickerView;
    private LinearLayout suggestionContainer;
    private RecyclerView quickFilterListView;
    private TopAdsBannerView adsBannerView;
    private Context context;
    private TickerListener tickerListener;
    private SuggestionListener suggestionListener;
    private QuickFilterListener quickFilterListener;
    private QuickFilterAdapter quickFilterAdapter;

    public HeaderViewHolder(View itemView,
                            TickerListener tickerListener,
                            SuggestionListener suggestionListener,
                            QuickFilterListener quickFilterListener,
                            BannerAdsListener bannerAdsListener) {
        super(itemView);
        context = itemView.getContext();
        this.tickerListener = tickerListener;
        this.suggestionListener = suggestionListener;
        this.quickFilterListener = quickFilterListener;
        tickerView = itemView.findViewById(R.id.tickerView);
        suggestionContainer = itemView.findViewById(R.id.suggestion_container);
        adsBannerView = itemView.findViewById(R.id.ads_banner);
        quickFilterListView = itemView.findViewById(R.id.quickFilterListView);
        initQuickFilterRecyclerView();
        adsBannerView.setTopAdsBannerClickListener((position, applink, data) -> {
            if (bannerAdsListener != null) {
                bannerAdsListener.onBannerAdsClicked(position, applink, data);
            }
        });
        adsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                if(bannerAdsListener != null) {
                    bannerAdsListener.onBannerAdsImpressionListener(position, data);
                }
            }
        });
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
    public void bind(final HeaderViewModel element) {
        bindAdsBannerView(element);

        bindTickerView(element);

        bindSuggestionView(element);

        bindQuickFilterView(element);
    }

    private void bindAdsBannerView(final HeaderViewModel element) {
        adsBannerView.displayAds(element.getCpmModel());
    }

    private void bindTickerView(final HeaderViewModel element) {
        if (tickerListener == null || tickerListener.isTickerHasDismissed() ||
                element.getTickerViewModel() == null || TextUtils.isEmpty(element.getTickerViewModel().getText())) {
            tickerView.setVisibility(View.GONE);
            return;
        }

        tickerView.setHtmlDescription(element.getTickerViewModel().getText());
        tickerView.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(@NotNull CharSequence charSequence) {
                if (tickerListener != null && !TextUtils.isEmpty(element.getTickerViewModel().getQuery())) {
                    tickerListener.onTickerClicked(element.getTickerViewModel().getQuery());
                }
            }

            @Override
            public void onDismiss() {
                if (tickerListener != null) {
                    tickerListener.onTickerDismissed();
                }
            }
        });
        tickerView.setVisibility(View.VISIBLE);
    }

    private void bindSuggestionView(final HeaderViewModel element) {
        if (element.getSuggestionViewModel() != null) {
            suggestionContainer.removeAllViews();
            View suggestionView = LayoutInflater.from(context).inflate(R.layout.suggestion_layout, null);
            TextView suggestionText = suggestionView.findViewById(R.id.suggestion_text_view);
            if (!TextUtils.isEmpty(element.getSuggestionViewModel().getSuggestionText())) {
                suggestionText.setText(Html.fromHtml(element.getSuggestionViewModel().getSuggestionText()));
                suggestionText.setOnClickListener(v -> {
                    if (suggestionListener != null && !TextUtils.isEmpty(element.getSuggestionViewModel().getSuggestedQuery())) {
                        suggestionListener.onSuggestionClicked(element.getSuggestionViewModel().getSuggestedQuery());
                    }
                });
                suggestionText.setVisibility(View.VISIBLE);
            } else {
                suggestionText.setVisibility(View.GONE);
            }
            suggestionContainer.addView(suggestionView);
        }
    }

    private void bindQuickFilterView(final HeaderViewModel element) {
        if (!TextUtils.isEmpty(element.getSuggestionViewModel().getFormattedResultCount())) {
            quickFilterAdapter.setFormattedResultCount(String.format(context.getString(R.string.result_count_template_text), element.getSuggestionViewModel().getFormattedResultCount()));
        } else {
            quickFilterAdapter.setFormattedResultCount("");
        }

        quickFilterAdapter.setOptionList(element.getQuickFilterList());
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
