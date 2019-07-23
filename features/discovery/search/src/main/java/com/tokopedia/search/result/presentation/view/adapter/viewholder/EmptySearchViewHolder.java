package com.tokopedia.search.result.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by henrypriyono on 10/31/17.
 */

public class EmptySearchViewHolder extends AbstractViewHolder<EmptySearchViewModel> implements TopAdsItemClickListener {

    public static final String SEARCH_NF_VALUE = "1";
    private final int MAX_TOPADS = 4;
    private TopAdsView topAdsView;
    private TopAdsParams topAdsParams;
    private Context context;
    private ImageView noResultImage;
    private TextView emptyTitleTextView;
    private TextView emptyContentTextView;
    private Button emptyButtonItemButton;
    private final EmptyStateListener emptyStateListener;
    private final BannerAdsListener bannerAdsListener;
    private TopAdsBannerView topAdsBannerView;
    private RecyclerView selectedFilterRecyclerView;
    private SelectedFilterAdapter selectedFilterAdapter;
    private EmptySearchViewModel boundedEmptySearchModel;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_product;

    public EmptySearchViewHolder(View view, EmptyStateListener emptyStateListener,
                                 BannerAdsListener bannerAdsListener, Config topAdsConfig) {
        super(view);
        noResultImage = (ImageView) view.findViewById(R.id.no_result_image);
        emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) view.findViewById(R.id.button_add_promo);
        this.emptyStateListener = emptyStateListener;
        this.bannerAdsListener = bannerAdsListener;
        context = itemView.getContext();
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.banner_ads);
        selectedFilterRecyclerView = itemView.findViewById(R.id.selectedFilterRecyclerView);

        if (topAdsConfig != null) {
            topAdsParams = topAdsConfig.getTopAdsParams();
            topAdsParams.getParam().put(TopAdsParams.KEY_SEARCH_NF, SEARCH_NF_VALUE);
        }
        initSelectedFilterRecyclerView();
    }

    private void initSelectedFilterRecyclerView() {
        selectedFilterAdapter = new SelectedFilterAdapter(emptyStateListener);
        selectedFilterRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        selectedFilterRecyclerView.setAdapter(selectedFilterAdapter);
        selectedFilterRecyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    private void loadProductAds() {
        Config productAdsConfig = new Config.Builder()
                .setSessionId(emptyStateListener.getRegistrationId())
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(topAdsParams)
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        topAdsView.setConfig(productAdsConfig);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(MAX_TOPADS);
        topAdsView.setAdsItemClickListener(this);
        topAdsView.loadTopAds();
    }

    private void loadBannerAds() {
        Config bannerAdsConfig = new Config.Builder()
                .setSessionId(emptyStateListener.getRegistrationId())
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(topAdsParams)
                .setEndpoint(Endpoint.CPM)
                .build();
        topAdsBannerView.setConfig(bannerAdsConfig);
        topAdsBannerView.setTopAdsBannerClickListener((position, appLink, data) -> {
            if(bannerAdsListener != null) {
                bannerAdsListener.onBannerAdsClicked(position, appLink, data);
            }
        });
        topAdsBannerView.setAdsListener(new TopAdsListener() {
            @Override
            public void onTopAdsLoaded(List<Item> list) {
                loadProductAds();
            }

            @Override
            public void onTopAdsFailToLoad(int errorCode, String message) {
                loadProductAds();
            }
        });
        topAdsBannerView.loadTopAds();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        context.startActivity(intent);
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        //Not implemented just leave empty
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        //Not implemented just leave empty
    }

    @Override
    public void bind(EmptySearchViewModel model) {
        boundedEmptySearchModel = model;

        bindNoResultImage();
        bindTitleTextView();
        bindContentTextView();
        bindNewSearchButton();
        bindRecylerView();
        loadBannerAdsIfNotNull();
    }

    private void bindNoResultImage() {
        noResultImage.setImageResource(boundedEmptySearchModel.getImageRes());
    }

    private void bindTitleTextView() {
        emptyTitleTextView.setText(boundedEmptySearchModel.getTitle());
    }

    private void bindContentTextView() {
        if (!TextUtils.isEmpty(boundedEmptySearchModel.getContent())) {
            emptyContentTextView.setText(boldTextBetweenQuotes(boundedEmptySearchModel.getContent()));
            emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            emptyContentTextView.setVisibility(View.GONE);
        }
    }

    private void bindNewSearchButton() {
        if (TextUtils.isEmpty(boundedEmptySearchModel.getButtonText())) {
            emptyButtonItemButton.setVisibility(View.GONE);
        } else {
            emptyButtonItemButton.setText(boundedEmptySearchModel.getButtonText());
            emptyButtonItemButton.setOnClickListener(this::newSearchButtonOnClick);
            emptyButtonItemButton.setVisibility(View.VISIBLE);
        }
    }

    private void newSearchButtonOnClick(View view) {
        if (emptyStateListener != null) {
            emptyStateListener.onEmptyButtonClicked();
        }
    }

    private void bindRecylerView() {
        List<Option> selectedFilterFromEmptyStateListener = emptyStateListener.getSelectedFilterAsOptionList();

        if(selectedFilterFromEmptyStateListener != null && !selectedFilterFromEmptyStateListener.isEmpty()) {
            populateSelectedFilterToRecylerView(selectedFilterFromEmptyStateListener);
        } else {
            selectedFilterRecyclerView.setVisibility(View.GONE);
        }
    }

    private void populateSelectedFilterToRecylerView(List<Option> selectedFilterOptionList) {
        selectedFilterRecyclerView.setVisibility(View.VISIBLE);
        selectedFilterAdapter.setOptionList(selectedFilterOptionList);
    }

    private void loadBannerAdsIfNotNull() {
        if (topAdsParams != null) {
            loadBannerAds();
        }
    }

    private CharSequence boldTextBetweenQuotes(String text) {
        String quoteSymbol = "\"";
        int firstQuotePos = text.indexOf(quoteSymbol);
        int lastQuotePos = text.lastIndexOf(quoteSymbol);

        SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new StyleSpan(Typeface.BOLD), firstQuotePos, lastQuotePos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    private static class SelectedFilterAdapter extends RecyclerView.Adapter<SelectedFilterItemViewHolder> {

        private List<Option> optionList = new ArrayList<>();
        private EmptyStateListener clickListener;

        public SelectedFilterAdapter(EmptyStateListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        @Override
        public SelectedFilterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_state_selected_filter_item, parent, false);
            return new SelectedFilterItemViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(SelectedFilterItemViewHolder holder, int position) {
            holder.bind(optionList.get(position));
        }

        @Override
        public int getItemCount() {
            return optionList.size();
        }
    }

    private static class SelectedFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView filterText;
        private final EmptyStateListener clickListener;
        private View deleteButton;

        public SelectedFilterItemViewHolder(View itemView, EmptyStateListener clickListener) {
            super(itemView);
            filterText = itemView.findViewById(R.id.filter_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            this.clickListener = clickListener;
        }

        public void bind(final Option option) {
            filterText.setText(option.getName());
            deleteButton.setOnClickListener(view -> clickListener.onSelectedFilterRemoved(option.getUniqueId()));
        }
    }
}
