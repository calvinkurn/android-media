package com.tokopedia.digital_deals.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponentInstance;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.customview.ExpandableTextView;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

;

public class DealsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DealCategoryAdapterContract.View {

    private int itemViewType;
    private List<ProductItem> categoryItems;
    private Context context;
    private final int ITEM_PRODUCT_NORMAL = 1;
    private final int ITEM_PRODUCT_SHORT = 2;
    private final int ITEM_PRODUCT_TOP_DEALS = 3;
    private final int ITEM_PRODUCT_HOME = 4;
    private final int HEADER_TRENDING_DEALS = 5;
    private final int HEADER_TRENDING_DEALS_SEARCHED = 6;
    private final int HEADER_BRAND = 7;
    private final int FOOTER = 8;
    private boolean isFooterAdded;
    private boolean isHeaderAdded;
    private boolean shortLayout;
    private boolean brandPageCard;
    private boolean topDealsLayout;
    private String highLightText;
    private String lowerhighlight;
    private String upperhighlight;
    private boolean isBrandHeaderAdded = false;

    private INavigateToActivityRequest toActivityRequest;
    public static final int HOME_PAGE = 1;
    public static final int SEARCH_PAGE = 2;
    public static final int CATEGORY_PAGE = 3;
    public static final int BRAND_PAGE = 4;
    public static final int DETAIL_PAGE = 5;
    private int pageType = 1;
    private String categoryName;

    @Inject
    DealCategoryAdapterPresenter mPresenter;
    DealsAnalytics dealsAnalytics;
    private SpannableString headerText;
    private boolean showHighlightText;
    private String brandHeaderText;
    private int productCount;
    private Brand brand;
    private boolean isDealsHomeLayout;

    public DealsCategoryAdapter(List<ProductItem> categoryItems, int pageType, INavigateToActivityRequest toActivityRequest, Boolean... layoutType) {
        if (categoryItems == null)
            this.categoryItems = new ArrayList<>();
        else
            this.categoryItems = categoryItems;
        if (layoutType.length > 0) {
            if (layoutType[0] != null) {
                this.shortLayout = layoutType[0];
            }
        }
        if (layoutType.length > 1) {
            if (layoutType[1] != null) {
                brandPageCard = layoutType[1];
            }
        }
        this.toActivityRequest = toActivityRequest;
        this.pageType = pageType;

    }

    public DealsCategoryAdapter(List<ProductItem> categoryItems, int pageType, INavigateToActivityRequest toActivityRequest, int itemViewType, Boolean... layoutType) {
        if (categoryItems == null)
            this.categoryItems = new ArrayList<>();
        else
            this.categoryItems = categoryItems;
        if (layoutType.length > 0) {
            if (layoutType[0] != null) {
                this.shortLayout = layoutType[0];
            }
        }
        if (layoutType.length > 1) {
            if (layoutType[1] != null) {
                brandPageCard = layoutType[1];
            }
        }
        this.toActivityRequest = toActivityRequest;
        this.pageType = pageType;
        this.itemViewType = itemViewType;

    }

    public void setTopDealsLayout(boolean isTopDealsLayout) {
        topDealsLayout = isTopDealsLayout;
    }

    public void setDealsHomeLayout(boolean isDealsHomeLayout) {
        this.isDealsHomeLayout = isDealsHomeLayout;
    }

    @Override
    public int getItemCount() {
        return (categoryItems == null) ? 0 : (isDealsHomeLayout) ? 9 : categoryItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        dealsAnalytics = new DealsAnalytics(context.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM_PRODUCT_NORMAL:
                v = inflater.inflate(R.layout.deal_item_card, parent, false);
                holder = new ItemViewHolderNormal(v);
                break;
            case ITEM_PRODUCT_SHORT:
                v = inflater.inflate(R.layout.deal_item_card_short, parent, false);
                holder = new ItemViewHolderShort(v);
                break;
            case ITEM_PRODUCT_HOME:
                v = inflater.inflate(R.layout.deal_item_card_home, parent, false);
                holder = new ItemViewHolderNormal(v);
                break;
            case ITEM_PRODUCT_TOP_DEALS:
                v = inflater.inflate(R.layout.item_top_suggestions, parent, false);
                holder = new TopSuggestionHolder(v);
                break;
            case HEADER_TRENDING_DEALS:
                v = inflater.inflate(R.layout.header_layout, parent, false);
                holder = new HeaderViewHolder(v);
                break;
            case HEADER_TRENDING_DEALS_SEARCHED:
                v = inflater.inflate(R.layout.header_layout_trending_deals, parent, false);
                holder = new HeaderViewHolder(v);
                break;
            case HEADER_BRAND:
                v = inflater.inflate(R.layout.header_layout_brand_page, parent, false);
                holder = new HeaderBrandViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }

        DealsComponentInstance.getDealsComponent(getActivity().getApplication()).inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_PRODUCT_NORMAL:
                ((ItemViewHolderNormal) holder).setIndex(position);
                ((ItemViewHolderNormal) holder).bindData(categoryItems.get(position));
                break;
            case ITEM_PRODUCT_SHORT:
                ((ItemViewHolderShort) holder).setIndex(position);
                ((ItemViewHolderShort) holder).bindData(categoryItems.get(position));
                break;
            case ITEM_PRODUCT_HOME:
                ((ItemViewHolderNormal) holder).setIndex(position);
                ((ItemViewHolderNormal) holder).bindData(categoryItems.get(position));
                break;
            case ITEM_PRODUCT_TOP_DEALS:
                ((TopSuggestionHolder) holder).setIndex(position);
                ((TopSuggestionHolder) holder).setDealTitle(position, categoryItems.get(position));
                break;
            case HEADER_TRENDING_DEALS:
                ((HeaderViewHolder) holder).bindData(headerText);
                break;
            case HEADER_TRENDING_DEALS_SEARCHED:
                break;
            case HEADER_BRAND:
                ((HeaderBrandViewHolder) holder).bindData(brandHeaderText, productCount);
                break;
            case FOOTER:
                break;
            default:
                break;
        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        return (shortLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : (position == 0 && isHeaderAdded)
//                ? HEADER_TRENDING_DEALS : ITEM_PRODUCT_SHORT
//                :
//                (topDealsLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : (position == 0 && isHeaderAdded)
//                        ? HEADER_TRENDING_DEALS_SEARCHED : ITEM_PRODUCT_TOP_DEALS
//                        :
//                        (isLastPosition(position) && isFooterAdded)
//                                ? FOOTER : (position == 0 && isHeaderAdded)
//                                ? HEADER_TRENDING_DEALS : (position == 0 && isBrandHeaderAdded && brandPageCard)
//                                ? HEADER_BRAND : ITEM_PRODUCT_NORMAL));
//    }

    @Override
    public int getItemViewType(int position) {
        int itemType;
        if (shortLayout) {
            if (isLastPosition(position) && isFooterAdded) {
                itemType = FOOTER;
            } else if ((position == 0 && isHeaderAdded)) {
                itemType = HEADER_TRENDING_DEALS;
            } else {
                itemType = ITEM_PRODUCT_SHORT;
            }

        } else if (topDealsLayout) {
            if ((isLastPosition(position) && isFooterAdded)) {
                itemType = FOOTER;
            } else if ((position == 0 && isHeaderAdded)) {
                itemType = HEADER_TRENDING_DEALS_SEARCHED;
            } else {
                itemType = ITEM_PRODUCT_TOP_DEALS;
            }
        } else {
            if ((isLastPosition(position) && isFooterAdded)) {
                itemType = FOOTER;
            } else if ((position == 0 && isHeaderAdded)) {
                itemType = HEADER_TRENDING_DEALS;
            } else if ((position == 0 && isBrandHeaderAdded && brandPageCard)) {
                itemType = HEADER_BRAND;
            } else if (isDealsHomeLayout) {
                itemType = ITEM_PRODUCT_HOME;
            } else {
                itemType = ITEM_PRODUCT_NORMAL;
            }
        }

        return itemType;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new ProductItem(), true);
        }
    }

    public void addHeader(SpannableString text) {
        if (!isHeaderAdded) {
            isHeaderAdded = true;
            headerText = text;
            categoryItems.add(0, new ProductItem());
            notifyItemInserted(0);
        }
    }

    public void addBrandHeader(String brandDetails, String brandName, int count) {
        if (!isBrandHeaderAdded) {
            isBrandHeaderAdded = true;
            highLightText = brandName;  //brand name
            brandHeaderText = brandDetails;
            productCount = count;
            categoryItems.add(0, new ProductItem());
            notifyItemInserted(0);
        }
    }

    public void removeHeaderAndFooter() {
        if (isHeaderAdded)
            categoryItems.remove(0);
        this.isHeaderAdded = false;
        if (isFooterAdded)
            categoryItems.remove(categoryItems.size() - 1);
        this.isFooterAdded = false;
    }

    public void setHighLightText(String text) {
        if (text != null && text.length() > 0) {
            String first = text.substring(0, 1).toUpperCase();
            lowerhighlight = text.toLowerCase();
            upperhighlight = text.toUpperCase();
            highLightText = first + text.substring(1).toLowerCase();
        }
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof ItemViewHolderNormal) {
            ItemViewHolderNormal holder1 = ((ItemViewHolderNormal) holder);
            if (!holder1.isShown()) {
                holder1.setShown(true);
                dealsAnalytics.sendDealImpressionEvent(isHeaderAdded, isBrandHeaderAdded, topDealsLayout,
                        categoryItems.get(holder1.getIndex()), categoryName, pageType, holder1.getIndex());
            }
        } else if (holder instanceof ItemViewHolderShort) {
            ItemViewHolderShort holder1 = ((ItemViewHolderShort) holder);
            if (!holder1.isShown()) {
                holder1.setShown(true);
                dealsAnalytics.sendDealImpressionEvent(isHeaderAdded, isBrandHeaderAdded, topDealsLayout,
                        categoryItems.get(holder1.getIndex()), categoryName, pageType, holder1.getIndex());
            }
        } else if (holder instanceof TopSuggestionHolder) {

            TopSuggestionHolder holder1 = ((TopSuggestionHolder) holder);
            if (!isHeaderAdded) {
                holder1.setShown(false);
            }
            if (!holder1.isShown()) {
                holder1.setShown(true);
                dealsAnalytics.sendDealImpressionEvent(isHeaderAdded, isBrandHeaderAdded, topDealsLayout,
                        categoryItems.get(holder1.getIndex()), categoryName, pageType, holder1.getIndex());
            }
        }
    }

    public void showHighLightText(boolean value) {
        this.showHighlightText = value;
    }

    public void add(ProductItem item, boolean refreshItem) {
        categoryItems.add(item);
        if (refreshItem)
            notifyItemInserted(categoryItems.size() - 1);
    }

    public void clearList() {
        isHeaderAdded = false;
        isFooterAdded = false;
        if (categoryItems != null)
            categoryItems.clear();
    }

    public void addAll(List<ProductItem> items, Boolean... refreshItems) {
        boolean refreshItem = true;
        if (refreshItems.length > 0)
            refreshItem = refreshItems[0];
        if (items != null) {
            for (ProductItem item : items) {
                add(item, refreshItem);
            }

        }
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            ProductItem item = categoryItems.get(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public Activity getActivity() {
        return (Activity) context;
    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public void notifyDataSetChanged(int position) {

    }


    @Override
    public void showLoginSnackbar(String message, int position) {
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
                getActivity().getResources().getString(R.string.title_activity_login), v -> {
                    Intent intent = ((DealsModuleRouter) getActivity().getApplication()).
                            getLoginIntent(getActivity());
                    toActivityRequest.onNavigateToActivityRequest(intent, DealsHomeActivity.REQUEST_CODE_LOGIN, position);
                }
        ).show();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public class ItemViewHolderNormal extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String LIKE = "LIKE";
        private static final String UNLIKE = "UNLIKE";
        private View itemView;
        private ImageView dealImage;
        private ImageView brandImage;
        private TextView discount;
        private TextView tvLikes;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView dealavailableLocations;
        private TextView dealListPrice;
        private ImageView ivFavourite;
        private ImageView ivShareVia;
        private TextView dealSellingPrice;
        private TextView hotDeal;
        private CardView cvBrand;
        private int index;
        private boolean isShown;

        ItemViewHolderNormal(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.iv_product);
            tvLikes = itemView.findViewById(R.id.tv_wish_list);
            discount = itemView.findViewById(R.id.tv_off);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            ivFavourite = itemView.findViewById(R.id.iv_wish_list);
            dealavailableLocations = itemView.findViewById(R.id.tv_available_locations);
            dealListPrice = itemView.findViewById(R.id.tv_mrp);
            brandImage = itemView.findViewById(R.id.iv_brand);
            ivShareVia = itemView.findViewById(R.id.iv_share);
            dealSellingPrice = itemView.findViewById(R.id.tv_sales_price);
            hotDeal = itemView.findViewById(R.id.tv_hot_deal);
            cvBrand = itemView.findViewById(R.id.cv_brand);
            if (isDealsHomeLayout) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int devicewidth = (int) (displaymetrics.widthPixels / 1.2);
                itemView.getLayoutParams().width = devicewidth;
            }
        }

        void bindData(final ProductItem productItem) {
            dealsDetails.setText(productItem.getDisplayName());
            ImageHandler.loadImage(context, dealImage, productItem.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            if (!brandPageCard) {
                ImageHandler.loadImage(context, brandImage, productItem.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
                brandName.setText(productItem.getBrand().getTitle());
                if (productItem.getBrand().getUrl() != null) {
                    cvBrand.setOnClickListener(this);
                }
            } else {
                productItem.setBrand(brand);
                cvBrand.setVisibility(View.GONE);
                brandName.setVisibility(View.GONE);
                Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_location);
                dealavailableLocations.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                dealavailableLocations.setCompoundDrawablePadding(getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8));

            }
            int likes = Utils.getSingletonInstance().containsLikedEvent(productItem.getId());
            int prevLikes = Utils.getSingletonInstance().containsUnlikedEvent(productItem.getId());
            if (likes > 0) {
                setLikes(likes, true);
            } else if (prevLikes >= 0) {
                setLikes(prevLikes, false);
            } else {
                setLikes(productItem.getLikes(), productItem.isLiked());
            }

            if (productItem.getDisplayTags() != null && !isDealsHomeLayout) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(productItem.getBrand().getCityName())) {
                Location location = Utils.getSingletonInstance().getLocation(context);
                if (location != null) {
                    dealavailableLocations.setText(location.getName());
                }
            } else {
                dealavailableLocations.setText(productItem.getBrand().getCityName());
            }
            if (productItem.getMrp() != 0 && productItem.getMrp() != productItem.getSalesPrice()) {
                dealListPrice.setVisibility(View.VISIBLE);
                dealListPrice.setText(Utils.convertToCurrencyString(productItem.getMrp()));
                dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                dealListPrice.setVisibility(View.INVISIBLE);
            }
            if (TextUtils.isEmpty(productItem.getSavingPercentage())) {
                discount.setVisibility(View.INVISIBLE);
            } else {
                discount.setVisibility(View.VISIBLE);
                discount.setText(productItem.getSavingPercentage());
            }
            dealSellingPrice.setText(Utils.convertToCurrencyString(productItem.getSalesPrice()));
            itemView.setOnClickListener(this);
            ivShareVia.setOnClickListener(this);
            ivFavourite.setOnClickListener(this);
        }

        void setLikes(int likes, boolean isLiked) {
            categoryItems.get(getIndex()).setLikes(likes);
            categoryItems.get(getIndex()).setLiked(isLiked);
            if (likes > 0) {
                tvLikes.setVisibility(View.VISIBLE);
                tvLikes.setText(String.valueOf(likes));
            } else {
                tvLikes.setVisibility(View.GONE);
            }
            if (isLiked) {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_filled);
            } else {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_unfilled);
            }
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        boolean isShown() {
            return isShown;
        }

        void setShown(boolean shown) {
            isShown = shown;
        }

        private String getShareLabel(int position) {
            if (categoryItems.get(getIndex()).getBrand() == null)
                return "";
            return String.format("%s - %s - %s", categoryItems.get(getIndex()).getBrand().getTitle()
                    , categoryItems.get(getIndex()).getDisplayName(),
                    position);
        }

        private String getFavouriteLabel(int position) {
            String str = LIKE;
            if (categoryItems.get(getIndex()).isLiked())
                str = UNLIKE;
            if (categoryItems.get(getIndex()).getBrand() == null)
                return "";
            return String.format("%s - %s - %s - %s",
                    categoryItems.get(getIndex()).getBrand().getTitle()
                    , categoryItems.get(getIndex()).getDisplayName()
                    , position
                    , str);
        }

        @Override
        public void onClick(View v) {
            int position = getIndex();
            if (isHeaderAdded || isBrandHeaderAdded)
                position -= 1;

            if (v.getId() == R.id.iv_share) {
                dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SHARE,
                        getShareLabel(position));

                Utils.getSingletonInstance().shareDeal(categoryItems.get(getIndex()).getSeoUrl(),
                        context, categoryItems.get(getIndex()).getDisplayName(),
                        categoryItems.get(getIndex()).getImageWeb(), categoryItems.get(getIndex()).getDesktopUrl());
            } else if (v.getId() == R.id.iv_wish_list) {
                ProductItem item = categoryItems.get(getIndex());
                boolean isLoggedIn = mPresenter.setDealLike(item.getId(), item.isLiked(), getIndex(), item.getLikes());
                if (isLoggedIn) {
                    dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_LOVE, getFavouriteLabel(position));
                    if (categoryItems.get(getIndex()).isLiked()) {
                        setLikes(categoryItems.get(getIndex()).getLikes() - 1, !categoryItems.get(getIndex()).isLiked());
                    } else {
                        setLikes(categoryItems.get(getIndex()).getLikes() + 1, !categoryItems.get(getIndex()).isLiked());
                    }
                }
            } else if (v.getId() == R.id.cv_brand) {
                Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
                detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, categoryItems.get(getIndex()).getBrand());
                context.startActivity(detailsIntent);
            } else {
                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
                toActivityRequest.onNavigateToActivityRequest(detailsIntent, DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY, getIndex());
                dealsAnalytics.sendDealClickEvent(categoryItems.get(getIndex()), categoryName,
                        pageType, position);

            }


        }
    }

    public void setLike(int position) {
        if (position < categoryItems.size()) {
            ProductItem item = categoryItems.get(position);
            mPresenter.setDealLike(item.getId(), item.isLiked(), position, item.getLikes());
            if (item.isLiked()) {
                categoryItems.get(position).setLikes(item.getLikes() - 1);
                categoryItems.get(position).setLiked(!item.isLiked());
            } else {
                categoryItems.get(position).setLikes(item.getLikes() + 1);
                categoryItems.get(position).setLiked(!item.isLiked());
            }
            notifyItemChanged(position);
        }
    }

    public class ItemViewHolderShort extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View itemView;
        private ImageView dealImage;
        private ImageView brandImage;
        private CardView cvBrand;
        private TextView discount;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView dealListPrice;
        private TextView dealSellingPrice;
        private TextView hotDeal;
        private int index;
        private boolean isShown;

        ItemViewHolderShort(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.iv_product);
            discount = itemView.findViewById(R.id.tv_off);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            dealListPrice = itemView.findViewById(R.id.tv_mrp);
            brandImage = itemView.findViewById(R.id.iv_brand);
            dealSellingPrice = itemView.findViewById(R.id.tv_sales_price);
            hotDeal = itemView.findViewById(R.id.tv_hot_deal);
            cvBrand = itemView.findViewById(R.id.cv_brand);


            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int devicewidth = (int) (displaymetrics.widthPixels / 1.2);
            itemView.getLayoutParams().width = devicewidth;

        }

        void bindData(final ProductItem productItem) {
            dealsDetails.setText(productItem.getDisplayName());
            ImageHandler.loadImage(context, dealImage, productItem.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            ImageHandler.loadImage(context, brandImage, productItem.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            brandName.setText(productItem.getBrand().getTitle());
            if (productItem.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            if (productItem.getBrand().getUrl() != null) {
                cvBrand.setOnClickListener(this);
            }
            if (productItem.getMrp() != 0 && productItem.getMrp() != productItem.getSalesPrice()) {
                dealListPrice.setVisibility(View.VISIBLE);
                dealListPrice.setText(Utils.convertToCurrencyString(productItem.getMrp()));
                dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                dealListPrice.setVisibility(View.INVISIBLE);
            }
            if (TextUtils.isEmpty(productItem.getSavingPercentage())) {
                discount.setVisibility(View.INVISIBLE);
            } else {
                discount.setVisibility(View.VISIBLE);
                discount.setText(productItem.getSavingPercentage());
            }
            dealSellingPrice.setText(Utils.convertToCurrencyString(productItem.getSalesPrice()));
            itemView.setOnClickListener(this);

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        boolean isShown() {
            return isShown;
        }

        void setShown(boolean shown) {
            isShown = shown;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cv_brand) {
                Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
                detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, categoryItems.get(getIndex()).getBrand());
                context.startActivity(detailsIntent);
            } else {
                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
                context.startActivity(detailsIntent);
                dealsAnalytics.sendEcommerceClickEvent(categoryItems.get(getIndex()), getIndex(), DealsAnalytics.EVENT_CLICK_RECOMMENDED_PDT_DETAIL, DealsAnalytics.LIST_DEALS_RECOMMENDED_PDP);
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView dealsInCity;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            dealsInCity = itemView.findViewById(R.id.deals_in_city);
        }

        void bindData(SpannableString headerText) {
            dealsInCity.setText(headerText);
        }
    }

    public class TopSuggestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDealTitle;
        private TextView tvBrandName;
        private View itemView;
        private ProductItem valueItem;
        private int index;
        private boolean isShown;

        private TopSuggestionHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            tvDealTitle = itemView.findViewById(R.id.tv_simple_item);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
        }

        private void setDealTitle(int position, ProductItem value) {
            this.valueItem = value;
            if (showHighlightText) {
                SpannableString spannableString = new SpannableString(valueItem.getDisplayName());
                if (highLightText != null && !highLightText.isEmpty() && Utils.containsIgnoreCase(valueItem.getDisplayName(), highLightText)) {
                    StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                    int fromindex = valueItem.getDisplayName().toLowerCase().indexOf(highLightText.toLowerCase());
                    if (fromindex == -1) {
                        fromindex = valueItem.getDisplayName().toLowerCase().indexOf(lowerhighlight.toLowerCase());
                    }
                    if (fromindex == -1) {
                        fromindex = valueItem.getDisplayName().toLowerCase().indexOf(upperhighlight.toLowerCase());
                    }
                    int toIndex = fromindex + highLightText.length();
                    spannableString.setSpan(styleSpan, fromindex, toIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvDealTitle.setText(spannableString);
            } else {
                tvDealTitle.setText(valueItem.getDisplayName());

            }
            tvBrandName.setText(value.getBrand().getTitle());
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        boolean isShown() {
            return isShown;
        }

        void setShown(boolean shown) {
            isShown = shown;
        }

        @Override
        public void onClick(View v) {
            Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
            detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
            context.startActivity(detailsIntent);
            int position = getIndex();
            String action;
            if (isHeaderAdded) {
                action = DealsAnalytics.EVENT_CLICK_SEARCH_TRENDING;
                position -= 1;
            } else
                action = DealsAnalytics.EVENT_CLICK_SEARCH_RESULT;
            ProductItem productItem = categoryItems.get(getIndex());
            dealsAnalytics.sendEcommerceClickEvent(categoryItems.get(getIndex()), position, action, DealsAnalytics.LIST_DEALS_SEARCH_TRENDING);
        }
    }

    public class HeaderBrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ExpandableTextView tvExpandableDesc;
        private TextView tvSeeMoreBtn;
        private TextView tvDealsCount;
        private TextView tvCityName;
        private View itemView;
        private TextView tvSeeMore;

        private HeaderBrandViewHolder(View view) {
            super(view);
            this.itemView = view;
            tvExpandableDesc = view.findViewById(R.id.tv_expandable_description);
            tvSeeMoreBtn = view.findViewById(R.id.seemorebutton_description);
            tvDealsCount = view.findViewById(R.id.number_of_locations);
            tvCityName = view.findViewById(R.id.tv_popular);
            tvSeeMore = view.findViewById(R.id.seemorebutton_description);
        }

        void bindData(final String headerText, int count) {
            tvExpandableDesc.setText(headerText);
            Location location = Utils.getSingletonInstance().getLocation(getActivity());
            if (location != null) {
                tvCityName.setText(String.format(context.getResources().getString(R.string.deals_brand_detail_location), location.getName()));

            } else {
                tvCityName.setText(context.getResources().getString(R.string.text_deals));
            }
            tvDealsCount.setText(String.format(context.getResources().getString(R.string.number_of_items), count));
            tvSeeMoreBtn.setOnClickListener(this);
            tvExpandableDesc.setInterpolator(new OvershootInterpolator());

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.seemorebutton_description) {
                dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SEE_MORE_BRAND_DETAIL, highLightText);
                if (tvExpandableDesc.isExpanded()) {
                    tvSeeMore.setText(R.string.expand);

                } else {
                    tvSeeMore.setText(R.string.collapse);

                }
                tvExpandableDesc.toggle();
            }
        }
    }

    public void unsubscribeUseCase() {
        mPresenter.onDestroy();
    }

    public interface INavigateToActivityRequest {
        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);
    }
}
