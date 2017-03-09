package com.tokopedia.discovery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.InfoTopAds;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.discovery.old.BucketListImageScroll;
import com.tokopedia.core.discovery.old.HeaderHotAdapter;
import com.tokopedia.core.home.adapter.ProductFeedAdapter;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.home.model.ViewHolderProductTopAds;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.network.entity.categoriesHades.Data;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.custom.TopAdsListRecyclerViewAdapter;
import com.tokopedia.discovery.adapter.custom.TopAdsRecyclerViewAdapter;
import com.tokopedia.discovery.presenter.DiscoveryActivityPresenter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.GridType.GRID_1;


/**
 * Created by m.normansyah on 6/27/16.
 */
public class ProductAdapter extends BaseRecyclerViewAdapter {
    public static final String DATA_LIST = "DATA_LIST";
    public static final String TOPADS_COUNTER = "TOPADS_COUNTER";
    public static final String ADAPTER_PAGING = "ADAPTER_PAGING";
    private static final String TAG = ProductAdapter.class.getSimpleName();
    public static final int ROWS_OF_PRODUCT = 12;
    //    private static final int PRODUCT_GRIDVIEW = 151_458;
    PagingHandler.PagingHandlerModel pagingHandlerModel;
    int page = 1;
    private int topAddsCounter = 0;
    private String source = "search";

    public int getTopAddsCounter() {
        return topAddsCounter + 1; // + 1 because it will indexed as 0
    }

    public ProductAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        Log.d(TAG, "ProductAdapter data " + data.size());
        if (context instanceof BrowseProductActivity) {
            BrowseProductActivity activity = (BrowseProductActivity) context;
            switch (activity.getBrowseProductActivityModel().getSource()) {
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                    source = "hotlist";
                    break;
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY:
                    source = "directory";
                    break;
                default:
                    source = "search";
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return new ViewHolderProductitem(context, LayoutInflater.from(context).inflate(R.layout.listview_product_item_list, parent, false));
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return new ViewHolderProductitem(context, LayoutInflater.from(context).inflate(R.layout.listview_product_item_grid, parent, false));
            case TkpdState.RecyclerView.VIEW_TOP_ADS_LIST:
            case TkpdState.RecyclerView.VIEW_TOP_ADS:
                return ProductFeedAdapter.createViewTopAds(parent);
            case TkpdState.RecyclerView.VIEW_TOP_ADS_4:
                return onCreateTopAds4ViewHolder(parent);
            case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:
                return onCreateBannerHotList(parent);
            case TkpdState.RecyclerView.VIEW_CATEGORY_HEADER:
                return onCreateDefaultCategoryHeader(parent);
            case TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER:
                return onCreateRevampCategoryHeader(parent);
            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
                return createEmptySearch(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!checkAvailableData(position)) {
            return;
        }

        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                ViewHolderProductitem itemHolder = (ViewHolderProductitem) holder;
                itemHolder.bindData((ProductItem) data.get(position), itemHolder);
                break;
            case TkpdState.RecyclerView.VIEW_TOP_ADS_LIST:
                bindTopAdsListViewHolder((ViewHolderProductTopAds) holder, position);
                break;
            case TkpdState.RecyclerView.VIEW_TOP_ADS:
                bindTopAdsViewHolder((ViewHolderProductTopAds) holder, position);
                break;
            case TkpdState.RecyclerView.VIEW_TOP_ADS_4:
                ((TopAds4ViewHolder) holder).bind((HorizontalProductList) data.get(position));
                break;
            case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:
                ((BannerHotListViewHolder) holder).bind((HotListBannerModel) data.get(position));
                break;
            case TkpdState.RecyclerView.VIEW_CATEGORY_HEADER:
                ((DefaultCategoryHeaderViewHolder) holder).bind((CategoryHeaderModel) data.get(position));
                break;
            case TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER:
                ((RevampCategoryHeaderViewHolder) holder).bind((CategoryHeaderRevampModel) data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public void setgridView(BrowseProductRouter.GridType gridType) {
        Log.d(TAG, "GridType " + gridType.name());
        for (RecyclerViewItem item : data) {
            if (item.getType() == TkpdState.RecyclerView.VIEW_PRODUCT
                    || item.getType() == TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1
                    || item.getType() == TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2) {

                if (gridType.equals(GRID_1)) {
                    item.setType(TkpdState.RecyclerView.VIEW_PRODUCT);
                } else if (gridType.equals(BrowseProductRouter.GridType.GRID_2)) {
                    item.setType(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2);
                } else {
                    item.setType(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1);
                }
            }

            if (item.getType() == TkpdState.RecyclerView.VIEW_TOP_ADS_LIST
                    || item.getType() == TkpdState.RecyclerView.VIEW_TOP_ADS) {
                if (gridType.equals(GRID_1)) {
                    item.setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
                } else {
                    item.setType(TkpdState.RecyclerView.VIEW_TOP_ADS);
                }
            }
        }
    }

    public void resetPaging() {
        page = 1;
        topAddsCounter = 0;
    }


    //[START] This is banner HotList


    public RecyclerView.ViewHolder createEmptySearch(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_hotlist, parent, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.include_no_result);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(lp);
        ImageHandler.loadImageWithId(((ImageView)view.findViewById(R.id.no_result_image)), R.drawable.status_no_result);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    private BannerHotListViewHolder onCreateBannerHotList(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_list_banner, parent, false);
        return new BannerHotListViewHolder(inflate);
    }

    public static class BannerHotListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.hot_list_banner_indicator)
        CirclePageIndicator hotListBannerIndicator;

        @BindView(R2.id.hot_list_banner_view_pager)
        ViewPager hotListBannerViewPager;

        @BindView(R2.id.hot_list_banner_hashtags)
        LinearLayout hotListBannerHashTags;

        HeaderHotAdapter headerHotAdapter;
        private int counterError;

        public BannerHotListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(HotListBannerModel hotListBannerModel) {
            headerHotAdapter = new HeaderHotAdapter(itemView.getContext(), hotListBannerModel.hotList.info.hotlistDescription);
            final String coverImg = hotListBannerModel.hotList.info.coverImg;
            ImageHandler.loadImageBitmap2(itemView.getContext(),
                    coverImg, new SimpleTarget<Bitmap>() {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            counterError++;
                            if (counterError < 5) {
                                ImageHandler.loadImageBitmap2(itemView.getContext(), coverImg, this);
                            }
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            counterError = 0;
                            Drawable drawable;
                            drawable = new BitmapDrawable(itemView.getResources(), resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                hotListBannerViewPager.setBackground(drawable);
                            } else {
                                hotListBannerViewPager.setBackgroundDrawable(drawable);
                            }
                        }


                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            counterError = 0;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                hotListBannerViewPager.setBackground(placeholder);
                            } else {
                                hotListBannerViewPager.setBackgroundDrawable(placeholder);
                            }
                        }
                    });
            hotListBannerViewPager.setAdapter(headerHotAdapter);

            hotListBannerIndicator.setViewPager(hotListBannerViewPager);
            hotListBannerIndicator.setFillColor(itemView.getResources().getColor(R.color.tkpd_dark_green));
            hotListBannerIndicator.setStrokeColor(itemView.getResources().getColor(R.color.white));
            if (hotListBannerModel.hashtags != null) {
                CreateHashtags(hotListBannerModel.hashtags);
            }
        }

        private void CreateHashtags(final List<BrowseProductModel.Hashtag> hashtags) {
            LayoutInflater vi = (LayoutInflater) itemView.getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            hotListBannerHashTags.removeAllViews();
            for (int i = 0; i < hashtags.size(); i++) {
                final int pos = i;
                View view = vi.inflate(R.layout.hashtags_view, null);
                TextView texthash = (TextView) view.findViewById(R.id.hashtags_txt);
                final String hastagName = hashtags.get(i).getName();
                texthash.setText("#" + hastagName);
                texthash.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        URLParser urlp = new URLParser(hashtags.get(pos).getUrl());
                        BrowseProductActivity.moveTo(itemView.getContext(), urlp.getDepIDfromURI(itemView.getContext()), TopAdsApi.SRC_DIRECTORY, BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY, hastagName);
                    }
                });
                hotListBannerHashTags.addView(view);
            }
        }
    }
    //[END] This is banner HotList


    //[START] This is for top ads
    private void bindTopAdsListViewHolder(ViewHolderProductTopAds holder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.listTopAdProduct.setLayoutManager(manager);

        HorizontalProductList horizontalProductList = (HorizontalProductList) data.get(position);
        TopAdsListRecyclerViewAdapter topAdsList = new TopAdsListRecyclerViewAdapter(horizontalProductList.getListProduct(), context, source);
        holder.listTopAdProduct.setAdapter(topAdsList);
        topAdsList.notifyDataSetChanged();
    }

    private void bindTopAdsViewHolder(ViewHolderProductTopAds holder, int position) {
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        holder.listTopAdProduct.setLayoutManager(manager);
        HorizontalProductList horizontalProductList = (HorizontalProductList) data.get(position);
        TopAdsRecyclerViewAdapter adapter = new TopAdsRecyclerViewAdapter(context, horizontalProductList.getListProduct(), source);
        holder.listTopAdProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private TopAds4ViewHolder onCreateTopAds4ViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_ads_four_layout, parent, false);
        return new TopAds4ViewHolder(inflate);
    }

    public static class TopAds4ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.main_content)
        LinearLayout mainContent;

        @BindView(R2.id.titlePromote)
        TextView titlePromote;

        @BindView(R2.id.info_topads)
        ImageView infoTopAds;

        @BindView(R2.id.top_ads_linearlayout)
        LinearLayout topAdsRecyclerView;

        public TopAds4ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final HorizontalProductList horizontalProductList) {
            final Context context = itemView.getContext();
            BucketListImageScroll bucketListImageScroll = new BucketListImageScroll(context);
            bucketListImageScroll.setContain(horizontalProductList);
            if (context != null && context instanceof DiscoveryActivityPresenter) {
                String adSrc = ((DiscoveryActivityPresenter) context).getBrowseProductActivityModel().getAdSrc();
                bucketListImageScroll.setAdSrc(adSrc);
            } else {
                bucketListImageScroll.setAdSrc(TopAdsApi.SRC_BROWSE_PRODUCT);
            }
            bucketListImageScroll.setAdapter(topAdsRecyclerView);
            infoTopAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoTopAds infoTopAds = new InfoTopAds();
                    Activity activity = (Activity) context;
                    infoTopAds.show(activity.getFragmentManager(), "INFO_TOPADS");
                }
            });
        }
    }

    private DefaultCategoryHeaderViewHolder onCreateDefaultCategoryHeader(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_category_header, parent, false);
        return new DefaultCategoryHeaderViewHolder(inflate);
    }

    public static class DefaultCategoryHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.recycler_view_default_categories)
        RecyclerView defaultCategoriesRecyclerView;

        @BindView(R2.id.expand_layout)
        LinearLayout expandLayout;

        @BindView(R2.id.hide_layout)
        LinearLayout hideLayout;

        @BindView(R2.id.card_category)
        CardView cardViewCategory;

        private DefaultCategoryAdapter categoryAdapter;

        public DefaultCategoryHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final CategoryHeaderModel categoryHeaderModel) {
            defaultCategoriesRecyclerView.setVisibility(View.VISIBLE);
            defaultCategoriesRecyclerView.setHasFixedSize(true);
            defaultCategoriesRecyclerView.setLayoutManager(
                    new NonScrollGridLayoutManager(categoryHeaderModel.context, 2,
                            GridLayoutManager.VERTICAL, false));
            defaultCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(categoryHeaderModel.context));
            categoryAdapter = new DefaultCategoryAdapter(categoryHeaderModel.categoryWidth,categoryHeaderModel.activeChildren,categoryHeaderModel.listener);
            defaultCategoriesRecyclerView.setAdapter(categoryAdapter);
            if (categoryHeaderModel.isUsedUnactiveChildren) {
                expandLayout.setVisibility(View.VISIBLE);
                expandLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryAdapter.addDataChild(categoryHeaderModel.categoryHeader.getChild()
                                .subList(6,categoryHeaderModel.categoryHeader.getChild().size()));
                        expandLayout.setVisibility(View.GONE);
                        categoryHeaderModel.isUsedUnactiveChildren = false;
                        hideLayout.setVisibility(View.VISIBLE);
                    }
                });
                hideLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryAdapter.hideExpandable();
                        expandLayout.setVisibility(View.VISIBLE);
                        categoryHeaderModel.isUsedUnactiveChildren = true;
                        hideLayout.setVisibility(View.GONE);
                    }
                });
            }
            if (categoryHeaderModel.categoryHeader.getChild() == null || categoryHeaderModel.categoryHeader.getChild().isEmpty()) {
                cardViewCategory.setVisibility(View.GONE);
            }

        }

        public interface CategoryHeaderListener {
            void onExpandClick();
        }
    }

    private RevampCategoryHeaderViewHolder onCreateRevampCategoryHeader(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.revamp_category_header, parent, false);
        return new RevampCategoryHeaderViewHolder(inflate);
    }

    public static class RevampCategoryHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_header)
        ImageView imageHeader;

        @BindView(R2.id.title_header)
        TextView titleHeader;

        @BindView(R2.id.expand_layout)
        LinearLayout expandLayout;

        @BindView(R2.id.hide_layout)
        LinearLayout hideLayout;

        @BindView(R2.id.recycler_view_revamp_categories)
        RecyclerView revampCategoriesRecyclerView;

        private RevampCategoryAdapter categoryAdapter;

        public RevampCategoryHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final CategoryHeaderRevampModel categoryHeaderModel) {
            revampCategoriesRecyclerView.setVisibility(View.VISIBLE);
            revampCategoriesRecyclerView.setHasFixedSize(true);
            revampCategoriesRecyclerView.setLayoutManager(
                    new NonScrollGridLayoutManager(categoryHeaderModel.context, 3,
                            GridLayoutManager.VERTICAL, false));
            categoryAdapter = new RevampCategoryAdapter(categoryHeaderModel.categoryWidth,categoryHeaderModel.activeChildren,categoryHeaderModel.listener);
            revampCategoriesRecyclerView.setAdapter(categoryAdapter);
            ImageHandler.loadImageFit2(imageHeader.getContext(),imageHeader,categoryHeaderModel.categoryHeader.getHeaderImage());
            titleHeader.setText(categoryHeaderModel.categoryHeader.getName().toUpperCase());
            if (categoryHeaderModel.isUsedUnactiveChildren) {
                expandLayout.setVisibility(View.VISIBLE);
                expandLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryAdapter.addDataChild(categoryHeaderModel.categoryHeader.getChild()
                                .subList(9,categoryHeaderModel.categoryHeader.getChild().size()));
                        expandLayout.setVisibility(View.GONE);
                        categoryHeaderModel.isUsedUnactiveChildren = false;
                        hideLayout.setVisibility(View.VISIBLE);

                    }
                });
                hideLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryAdapter.hideExpandable();
                        expandLayout.setVisibility(View.VISIBLE);
                        categoryHeaderModel.isUsedUnactiveChildren = true;
                        hideLayout.setVisibility(View.GONE);
                    }
                });
            }
        }

        public interface CategoryHeaderListener {
            void onExpandClick();
        }



    }

    /**
     * Much better if we check
     * is data zize is less than index position
     *
     * @param position
     * @return
     */
    private boolean checkAvailableData(int position) {
        return position <= data.size();
    }

    /**
     * check if position is in range with data size
     *
     * @param position
     * @return true whether is in range of data
     */
    private boolean checkDataSize(int position) {
        return data != null && data.size() > 0 && position > -1 && position < data.size();
    }

    /**
     * this is for registered type
     *
     * @param recyclerViewItem
     * @return
     */
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            case TkpdState.RecyclerView.VIEW_TOP_ADS:
            case TkpdState.RecyclerView.VIEW_TOP_ADS_4:
            case TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST:
            case TkpdState.RecyclerView.VIEW_CATEGORY_HEADER:
            case TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER:
            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
            case TkpdState.RecyclerView.VIEW_TOP_ADS_LIST:
                return recyclerViewItem.getType();
            default:
                return -1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (checkDataSize(position)) {
            RecyclerViewItem recyclerViewItem = data.get(position);
            return isInType(recyclerViewItem);
        } else {
            return super.getItemViewType(position);
        }
    }

    public boolean isHotListBanner(int position) {
        return data.get(position).getType() == TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST;
    }

    public boolean isCategoryHeader(int position) {
        return (data.get(position).getType() == TkpdState.RecyclerView.VIEW_CATEGORY_HEADER
                || data.get(position).getType() == TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER);
    }


    public boolean isEmptySearch(int position) {
        return data.get(position).getType() == TkpdState.RecyclerView.VIEW_EMPTY_SEARCH;
    }

    public boolean isTopAds(int position) {
        if (position > data.size())
            return false;
        return ProductFeedAdapter.isTopAds(data.get(position)) || data.get(position).getType() == TkpdState.RecyclerView.VIEW_TOP_ADS_4;
    }

    /**
     * @param reload true if want to notify the adapter
     * @param datas  data from Internet
     */
    public void addAll(boolean reload, List<RecyclerViewItem> datas) {
        addAll(false, reload, datas);

    }

    /**
     * @param withClear true means clear all data
     * @param reload    call notifydataseinsert.
     * @param datas     just another data.
     */
    public void addAll(boolean withClear, boolean reload, List<RecyclerViewItem> datas) {
        if (withClear) {
            this.data.clear();
            resetPaging();
        }
        this.data.addAll(datas);
        if (reload)
            notifyItemInserted(datas.size());
    }

    // SETTER AND GETTER BELOW

    public boolean checkHasNext() {
        return pagingHandlerModel.getStartIndex() != -1;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void incrementPage() {
        this.page++;
    }

    public int addTopAds(List<ProductItem> listProduct, int page) {
        Log.d(TAG, "masuk sini list add size " + listProduct.size());
        boolean productIsEmpty = false;
        if (data != null && data.size() == 0) {
            setIsLoading(false);
            notifyDataSetChanged();
            productIsEmpty = true;
        }
        boolean isHeader = checkIfOffset();
        if (listProduct != null && listProduct.size() > 0) {
            int i = page - 1;
            int posTop = (i * 12) + topAddsCounter + (isHeader ? 1 : 0);
            topAddsCounter++;
            Log.d(TAG, "ukuran data : " + data.size() + " : posTop " + posTop);
            HorizontalProductList horizontalProductListTop = new HorizontalProductList(listProduct);
            if (context != null && context instanceof BrowseProductActivity) {
                BrowseProductRouter.GridType gridType = ((BrowseProductActivity) context).getGridType();
                switch (gridType) {
                    case GRID_1:
                        horizontalProductListTop.setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
                        break;
                    default:
                        horizontalProductListTop.setType(TkpdState.RecyclerView.VIEW_TOP_ADS);
                        break;
                }
            } else {
                horizontalProductListTop.setType(TkpdState.RecyclerView.VIEW_TOP_ADS);
            }

            if (data.size() > 1) {
                try {
                    data.add(posTop, horizontalProductListTop);
                } catch (Exception e) {
                    data.add(data.size(), horizontalProductListTop);
                }
            } else {
                data.add(horizontalProductListTop);
            }
            if (productIsEmpty) {
                setSearchNotFound();
            }
            notifyItemInserted(posTop);
            return posTop;
        }
        return 0;
    }

    protected boolean checkIfOffset() {
        return data != null && data.size() > 1 && (data.get(0).getType() == (TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST)
        || data.get(0).getType() == (TkpdState.RecyclerView.VIEW_CATEGORY_HEADER)
                || data.get(0).getType() == (TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER));
    }

    public void setSearchNotFound() {
        data.add(new EmptySearchItem());
        notifyDataSetChanged();
    }

    public void addHotListHeader(HotListBannerModel hotListBannerModel) {
        data.add(0, hotListBannerModel);
    }

    public void addCategoryHeader(CategoryHeaderModel categoryHeaderModel) {
        data.add(0, categoryHeaderModel);
    }

    public void addCategoryRevampHeader(CategoryHeaderRevampModel categoryHeaderModel) {
        data.add(0, categoryHeaderModel);
    }

    public static class EmptySearchItem extends RecyclerViewItem {
        public EmptySearchItem() {
            setType(TkpdState.RecyclerView.VIEW_EMPTY_SEARCH);
        }
    }

    public static class HotListBannerModel extends RecyclerViewItem {

        private com.tokopedia.core.discovery.model.HotListBannerModel hotList;
        List<BrowseProductModel.Hashtag> hashtags;

        private HotListBannerModel() {
            setType(TkpdState.RecyclerView.VIEW_BANNER_HOT_LIST);
        }

        public HotListBannerModel(com.tokopedia.core.discovery.model.HotListBannerModel hotList, List<BrowseProductModel.Hashtag> hashtags) {
            this();
            this.hotList = hotList;
            this.hashtags = hashtags;
        }
    }


    public static class CategoryHeaderModel extends RecyclerViewItem {

        private Data categoryHeader;
        private ArrayList<Child> activeChildren = new ArrayList<>();
        private boolean isUsedUnactiveChildren = false;
        private Context context;
        private int categoryWidth;
        DefaultCategoryAdapter.CategoryListener listener;

        private CategoryHeaderModel() {
            setType(TkpdState.RecyclerView.VIEW_CATEGORY_HEADER);

        }

        public CategoryHeaderModel(Data categoryHeader, Context context, int categoryWidth, DefaultCategoryAdapter.CategoryListener listener) {
            this();
            this.categoryHeader = categoryHeader;
            this.context = context;
            this.categoryWidth = categoryWidth;
            this.listener = listener;
            if (categoryHeader.getChild()!=null && categoryHeader.getChild().size()>6) {
                activeChildren.addAll(categoryHeader.getChild()
                        .subList(0,6));
                isUsedUnactiveChildren = true;
            } else if (categoryHeader.getChild()!=null) {
                activeChildren.addAll(categoryHeader.getChild());
            }
        }
    }

    public static class CategoryHeaderRevampModel extends RecyclerViewItem {

        private Data categoryHeader;
        private ArrayList<Child> activeChildren = new ArrayList<>();
        private boolean isUsedUnactiveChildren = false;
        private Context context;
        private int categoryWidth;
        RevampCategoryAdapter.CategoryListener listener;

        private CategoryHeaderRevampModel() {
            setType(TkpdState.RecyclerView.VIEW_CATEGORY_REVAMP_HEADER);
        }

        public CategoryHeaderRevampModel(Data categoryHeader, Context context, int categoryWidth, RevampCategoryAdapter.CategoryListener listener) {
            this();
            this.categoryHeader = categoryHeader;
            this.context = context;
            this.categoryWidth = categoryWidth;
            this.listener = listener;
            if (categoryHeader.getChild()!=null && categoryHeader.getChild().size()>9) {
                activeChildren.addAll(categoryHeader.getChild()
                        .subList(0,9));
                isUsedUnactiveChildren = true;
            } else if (categoryHeader.getChild()!=null){
                activeChildren.addAll(categoryHeader.getChild());
            }
        }
    }

    /**
     * save data before rotation
     *
     * @param bundle
     */
    public void saveDataBeforeRotation(Bundle bundle) {
        bundle.putParcelable(DATA_LIST, Parcels.wrap(data));
    }

    public void saveAdapterPaging(Bundle bundle) {
        bundle.putInt(TOPADS_COUNTER, topAddsCounter);
        bundle.putInt(ADAPTER_PAGING, page);
    }

    public void restoreAdapterPaging(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(ADAPTER_PAGING, 1);
            topAddsCounter = savedInstanceState.getInt(TOPADS_COUNTER, 0);
        }

    }

    public static class ViewHolderProductitem extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_image)
        ImageView productImage;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.price)
        TextView price;
        @BindView(R2.id.label_container)
        FlowLayout labelContainer;
        @BindView(R2.id.shop_name)
        TextView shopName;
        @BindView(R2.id.location)
        TextView location;
        @BindView(R2.id.badges_container)
        LinearLayout badgesContainer;

        private Context context;
        private ProductItem data;

        public ViewHolderProductitem(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void bindData(ProductItem data, ViewHolderProductitem viewHolder) {
            this.data = data;
            if (data.getSpannedName() != null)
                title.setText(data.getSpannedName());
            else
                title.setText(MethodChecker.fromHtml(data.name));
            price.setText(data.price);
            if (data.getShopLocation() != null)
                location.setText(MethodChecker.fromHtml(data.getShopLocation()));
            else
                location.setVisibility(View.INVISIBLE);

            if (data.getSpannedShop() != null)
                shopName.setText(data.getSpannedShop());
            else
                shopName.setText(MethodChecker.fromHtml(data.shop));
            ImageHandler.loadImageThumbs(context, productImage, data.imgUri);
            viewHolder.badgesContainer.removeAllViews();
            if (data.getBadges() != null) {
                for (Badge badges : data.getBadges()) {
                    LuckyShopImage.loadImage(context, badges.getImageUrl(), badgesContainer);
                }
            }
            viewHolder.labelContainer.removeAllViews();
            if (data.getLabels() != null) {
                for (Label label : data.getLabels()) {
                    View view = LayoutInflater.from(context).inflate(R.layout.label_layout, null);
                    TextView labelText = (TextView) view.findViewById(R.id.label);
                    labelText.setText(label.getTitle());
                    if (!label.getColor().toLowerCase().equals("#ffffff")) {
                        labelText.setBackgroundResource(R.drawable.bg_label);
                        labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                        ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            labelText.setBackgroundTintList(tint);
                        } else {
                            ViewCompat.setBackgroundTintList(labelText, tint);
                        }
                    }
                    labelContainer.addView(view);
                }
            }
        }

        @OnClick(R2.id.container)
        public void onClick() {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, ProductInfoActivity.class);
            bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }

    public static class ViewHolderProductGrid extends RecyclerView.ViewHolder {

        public ImageView productImage;

        public ViewHolderProductGrid(View itemLayoutView) {
            super(itemLayoutView);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
        }

        public void bindData(ProductItem data) {
            ImageHandler.loadImageFitCenter(itemView.getContext(), productImage, data.imgUri);
        }

        public static RecyclerView.ViewHolder createView(ViewGroup parent) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_product_item_grid, parent, false);
            return new ViewHolderProductGrid(itemLayoutView);
        }

    }
}