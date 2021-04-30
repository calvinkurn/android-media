package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.unifycomponents.UnifyButton;

import java.util.ArrayList;
import java.util.List;

public class ProductEmptySearchViewHolder extends AbstractViewHolder<EmptySearchProductDataView> implements TopAdsItemClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_empty_state_product;

    public static final String SEARCH_NF_VALUE = "1";
    private TopAdsParams topAdsParams;
    private Context context;
    private ImageView noResultImage;
    private TextView emptyTitleTextView;
    private TextView emptyContentTextView;
    private TextView emptyButtonItemButton;
    protected final EmptyStateListener emptyStateListener;
    private final BannerAdsListener bannerAdsListener;
    private TopAdsBannerView topAdsBannerView;
    private UnifyButton buttonEmptySearchToGlobalSearch;
    protected RecyclerView selectedFilterRecyclerView;
    private ProductSelectedFilterAdapter productSelectedFilterAdapter;
    private EmptySearchProductDataView boundedEmptySearchModel;

    public ProductEmptySearchViewHolder(View view, EmptyStateListener emptyStateListener, BannerAdsListener bannerAdsListener, Config topAdsConfig) {
        super(view);
        noResultImage = view.findViewById(R.id.no_result_image);
        emptyTitleTextView = view.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = view.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = view.findViewById(R.id.button_add_promo);
        buttonEmptySearchToGlobalSearch = view.findViewById(R.id.buttonEmptySearchToGlobalSearch);
        this.emptyStateListener = emptyStateListener;
        this.bannerAdsListener = bannerAdsListener;
        context = itemView.getContext();
        topAdsBannerView = itemView.findViewById(R.id.banner_ads);
        selectedFilterRecyclerView = itemView.findViewById(R.id.selectedFilterRecyclerView);

        if (topAdsConfig != null) {
            topAdsParams = topAdsConfig.getTopAdsParams();
            topAdsParams.getParam().put(TopAdsParams.KEY_SEARCH_NF, SEARCH_NF_VALUE);
        }
        initSelectedFilterRecyclerView();
    }

    protected void initSelectedFilterRecyclerView() {
        ChipsLayoutManager layoutManager = ChipsLayoutManager.newBuilder(itemView.getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        int staticDimen8dp = itemView.getContext().getResources().getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8);
        selectedFilterRecyclerView.addItemDecoration(new SpacingItemDecoration(staticDimen8dp));
        selectedFilterRecyclerView.setLayoutManager(layoutManager);
        ViewCompat.setLayoutDirection(selectedFilterRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
        productSelectedFilterAdapter = new ProductSelectedFilterAdapter(emptyStateListener);
        selectedFilterRecyclerView.setAdapter(productSelectedFilterAdapter);
    }


    private void loadBannerAds() {
        if (!boundedEmptySearchModel.isBannerAdsAllowed()) {
            return;
        }

        Config bannerAdsConfig = new Config.Builder()
                .setSessionId(emptyStateListener.getRegistrationId())
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(topAdsParams)
                .setEndpoint(Endpoint.CPM)
                .build();
        if(topAdsBannerView != null) {
            topAdsBannerView.setConfig(bannerAdsConfig);
            topAdsBannerView.setTopAdsBannerClickListener((position, appLink, data) -> {
                if (bannerAdsListener != null) {
                    bannerAdsListener.onBannerAdsClicked(position, appLink, data);
                }
            });
            topAdsBannerView.loadTopAds();
        }
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        context.startActivity(intent);
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
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
    public void bind(EmptySearchProductDataView model) {
        boundedEmptySearchModel = model;

        bindNoResultImage();
        bindTitleTextView();
        bindContentTextView();
        bindNewSearchButton();
        bindRecylerView();
        bindGlobalSearchButton();
    }

    private void bindNoResultImage() {
        noResultImage.setImageResource(com.tokopedia.resources.common.R.drawable.ic_product_search_not_found);
    }

    private void bindTitleTextView() {
        if (emptyTitleTextView == null) return;

        if (boundedEmptySearchModel.isLocalSearch())
            emptyTitleTextView.setText(getString(R.string.msg_empty_search_product_title_local));
        else
            emptyTitleTextView.setText(getString(R.string.msg_empty_search_product_title));
    }

    private void bindContentTextView() {
        if (emptyContentTextView == null) return;

        if (boundedEmptySearchModel.isLocalSearch())
            emptyContentTextView.setText(getLocalSearchEmptyMessage());
        else if (boundedEmptySearchModel.getIsFilterActive())
            emptyContentTextView.setText(getString(R.string.msg_empty_search_product_content_with_filter));
        else
            emptyContentTextView.setText(getString(R.string.msg_empty_search_product_content));
    }

    private String getLocalSearchEmptyMessage() {
        if (itemView.getContext() == null) return "";

        return itemView.getContext().getString(
                R.string.msg_empty_search_product_content_local,
                boundedEmptySearchModel.getKeyword(),
                boundedEmptySearchModel.getPageTitle()
        );
    }

    private void bindNewSearchButton() {
        if (boundedEmptySearchModel.getIsFilterActive()) {
            emptyButtonItemButton.setVisibility(View.GONE);
        } else {
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
            selectedFilterRecyclerView.setVisibility(View.VISIBLE);
            populateSelectedFilterToRecylerView(selectedFilterFromEmptyStateListener);
        } else {
            selectedFilterRecyclerView.setVisibility(View.GONE);
        }
    }

    protected void populateSelectedFilterToRecylerView(List<Option> selectedFilterOptionList) {
        productSelectedFilterAdapter.setOptionList(selectedFilterOptionList);
    }

    private void bindBannerAds() {
        if (topAdsParams != null) {
            loadBannerAds();
        }
    }

    private void bindGlobalSearchButton() {
        if (buttonEmptySearchToGlobalSearch == null) return;

        if (boundedEmptySearchModel.isLocalSearch()) {
            buttonEmptySearchToGlobalSearch.setVisibility(View.VISIBLE);
            buttonEmptySearchToGlobalSearch.setOnClickListener(this::onEmptySearchToGlobalSearchClicked);
        }
        else {
            buttonEmptySearchToGlobalSearch.setVisibility(View.GONE);
        }
    }

    private void onEmptySearchToGlobalSearchClicked(View view) {
        if (emptyStateListener == null) return;

        emptyStateListener.onEmptySearchToGlobalSearchClicked(boundedEmptySearchModel.getGlobalSearchApplink());
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
