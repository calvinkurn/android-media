package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.GuidedSearchViewModel;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavWidgetListener;
import com.tokopedia.search.result.presentation.view.listener.GuidedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.widget.GlobalNavWidget;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

import java.util.ArrayList;
import java.util.List;

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search__header_layout;
    private static final String SHOP = "shop";
    private LinearLayout suggestionContainer;
    private RecyclerView quickFilterListView;
    private TopAdsBannerView adsBannerView;
    private Context context;
    private SuggestionListener suggestionListener;
    private QuickFilterListener quickFilterListener;
    private GlobalNavWidgetListener globalNavWidgetListener;
    private QuickFilterAdapter quickFilterAdapter;
    private RecyclerView guidedSearchRecyclerView;
    private GuidedSearchAdapter guidedSearchAdapter;
    private GlobalNavWidget globalNavWidget;

    public HeaderViewHolder(View itemView,
                            SuggestionListener suggestionListener,
                            QuickFilterListener quickFilterListener,
                            GuidedSearchListener guidedSearchListener,
                            GlobalNavWidgetListener globalNavWidgetListener,
                            BannerAdsListener bannerAdsListener) {
        super(itemView);
        context = itemView.getContext();
        this.suggestionListener = suggestionListener;
        this.quickFilterListener = quickFilterListener;
        this.globalNavWidgetListener = globalNavWidgetListener;
        suggestionContainer = itemView.findViewById(R.id.suggestion_container);
        adsBannerView = itemView.findViewById(R.id.ads_banner);
        globalNavWidget = itemView.findViewById(R.id.globalNavWidget);
        quickFilterListView = itemView.findViewById(R.id.quickFilterListView);
        guidedSearchRecyclerView = itemView.findViewById(R.id.guidedSearchRecyclerView);
        guidedSearchAdapter = new GuidedSearchAdapter(guidedSearchListener);
        guidedSearchRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        guidedSearchRecyclerView.setAdapter(guidedSearchAdapter);
        guidedSearchRecyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
        initQuickFilterRecyclerView();
        adsBannerView.setTopAdsBannerClickListener((position, applink, data) -> {
            if (bannerAdsListener != null) {
                bannerAdsListener.onBannerAdsClicked(applink);
            }
            if (applink.contains(SHOP)) {
                TopAdsGtmTracker.eventSearchResultPromoShopClick(context, data, position);
            } else {
                TopAdsGtmTracker.eventSearchResultPromoProductClick(context, data, position);
            }
        });
        adsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                TopAdsGtmTracker.eventSearchResultPromoView(context, data, position);
            }
        });
    }

    private void initQuickFilterRecyclerView() {
        quickFilterAdapter = new QuickFilterAdapter(quickFilterListener);
        quickFilterListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        quickFilterListView.setAdapter(quickFilterAdapter);
        quickFilterListView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    @Override
    public void bind(final HeaderViewModel element) {
        bindAdsBannerView(element);

        bindGlobalNavWidget(element);

        bindSuggestionView(element);

        bindQuickFilterView(element);

        bindGuidedSearchView(element);
    }

    private void bindAdsBannerView(final HeaderViewModel element) {
        adsBannerView.displayAds(element.getCpmModel());
    }

    private void bindGlobalNavWidget(final HeaderViewModel element) {
        if (element.getGlobalNavViewModel() != null) {
            globalNavWidget.setData(element.getGlobalNavViewModel(), new GlobalNavWidget.ClickListener() {
                @Override
                public void onClickItem(GlobalNavViewModel.Item item) {
                    if (globalNavWidgetListener != null) {
                        globalNavWidgetListener.onGlobalNavWidgetClicked(item, element.getGlobalNavViewModel().getKeyword());
                    }
                }

                @Override
                public void onclickSeeAllButton(String applink, String url) {
                    if (globalNavWidgetListener != null) {
                        globalNavWidgetListener.onGlobalNavWidgetClickSeeAll(applink, url);
                    }
                }
            });
            globalNavWidget.setVisibility(View.VISIBLE);
        } else {
            globalNavWidget.setVisibility(View.GONE);
        }
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

    private void bindGuidedSearchView(final HeaderViewModel element) {
        if (element.getGuidedSearch() != null
                && element.getGuidedSearch().getItemList() != null
                && !element.getGuidedSearch().getItemList().isEmpty()) {
            guidedSearchRecyclerView.setVisibility(View.VISIBLE);
            guidedSearchAdapter.setItemList(element.getGuidedSearch().getItemList());
        } else {
            guidedSearchRecyclerView.setVisibility(View.GONE);
        }
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
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
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

    private static class GuidedSearchAdapter extends RecyclerView.Adapter<GuidedSearchViewHolder> {

        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();
        GuidedSearchListener guidedSearchListener;

        GuidedSearchAdapter(GuidedSearchListener guidedSearchListener) {
            this.guidedSearchListener = guidedSearchListener;
        }

        void setItemList(List<GuidedSearchViewModel.Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public GuidedSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guided_search_item_with_background, parent, false);
            return new GuidedSearchViewHolder(view, guidedSearchListener);
        }

        @Override
        public void onBindViewHolder(@NonNull GuidedSearchViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private static class GuidedSearchViewHolder extends RecyclerView.ViewHolder {

        private static final int[] BACKGROUND = {
                R.drawable.guided_back_1,
                R.drawable.guided_back_2,
                R.drawable.guided_back_3,
                R.drawable.guided_back_4,
                R.drawable.guided_back_5,
        };

        TextView textView;
        ImageView imageView;
        GuidedSearchListener guidedSearchListener;

        GuidedSearchViewHolder(View itemView, GuidedSearchListener guidedSearchListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.guided_search_text);
            imageView = itemView.findViewById(R.id.guided_search_background);
            this.guidedSearchListener = guidedSearchListener;
        }

        public void bind(final GuidedSearchViewModel.Item item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(view -> {
                if (guidedSearchListener != null) {
                    SearchTracking.eventClickGuidedSearch(item.getPreviousKey(), item.getPosition(), item.getKeyword());
                    guidedSearchListener.onSearchGuideClicked(Uri.parse(item.getUrl()).getEncodedQuery());
                }
            });
            imageView.setImageResource(BACKGROUND[getAdapterPosition() % 5]);
        }
    }
}
