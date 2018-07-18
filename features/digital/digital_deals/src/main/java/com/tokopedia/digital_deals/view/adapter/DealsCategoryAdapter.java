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
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DealCategoryAdapterContract.View {

    private List<ProductItem> categoryItems;
    private Context context;
    private final int ITEM = 1;
    private final int FOOTER = 2;
    private final int ITEM2 = 3;
    private final int HEADER = 4;
    private final int ITEM3 = 5;
    private final int HEADER2 = 6;
    private boolean isFooterAdded;
    private boolean isHeaderAdded;
    private boolean shortLayout;
    private boolean brandPageCard;
    private boolean topDealsLayout;
    private String highLightText;
    private String lowerhighlight;
    private String upperhighlight;
    private INavigateToActivityRequest toActivityRequest;
    @Inject
    DealCategoryAdapterPresenter mPresenter;
    private SpannableString headerText;

    public DealsCategoryAdapter(List<ProductItem> categoryItems, INavigateToActivityRequest toActivityRequest, Boolean... layoutType) {
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

    }

    public void setTopDealsLayout(boolean isTopDealsLayout) {
        topDealsLayout = isTopDealsLayout;
    }

    @Override
    public int getItemCount() {
        return (categoryItems == null) ? 0 : categoryItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.deal_item_card, parent, false);
                holder = new ItemViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            case ITEM2:
                v = inflater.inflate(R.layout.deal_item_card_short, parent, false);
                holder = new ItemViewHolder2(v);
                break;
            case HEADER:
                v = inflater.inflate(R.layout.header_layout, parent, false);
                holder = new HeaderViewHolder(v);
                break;
            case ITEM3:
                v = inflater.inflate(R.layout.item_top_suggestions, parent, false);
                holder = new TopSuggestionHolder(v);
                break;
            case HEADER2:
                v = inflater.inflate(R.layout.header_layout_trending_deals, parent, false);
                holder = new HeaderViewHolder(v);
                break;
            default:
                break;
        }

        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build().inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((ItemViewHolder) holder).setIndex(position);
                ((ItemViewHolder) holder).bindData(categoryItems.get(position));
                break;
            case FOOTER:
                break;
            case ITEM2:
                ((ItemViewHolder2) holder).setIndex(position);
                ((ItemViewHolder2) holder).bindData(categoryItems.get(position));
                break;
            case HEADER:
                ((HeaderViewHolder) holder).bindData(headerText);
                break;
            case ITEM3:
                ((TopSuggestionHolder) holder).setIndex(position);
                ((TopSuggestionHolder) holder).setDealTitle(position, categoryItems.get(position));
                break;
            case HEADER2:
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (shortLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : (position == 0 && isHeaderAdded)
                ? HEADER : ITEM2
                :
                (topDealsLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : (position == 0 && isHeaderAdded)
                        ? HEADER2 : ITEM3
                        :
                        (isLastPosition(position) && isFooterAdded)
                                ? FOOTER : (position == 0 && isHeaderAdded)
                                ? HEADER : ITEM));
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


    public void add(ProductItem item, boolean refreshItem) {
        categoryItems.add(item);
        if (refreshItem)
            notifyItemInserted(categoryItems.size() - 1);
    }

    public void clearList() {
        isHeaderAdded = false;
        isFooterAdded = false;
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
    public void showLoginSnackbar(String message) {
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
                getActivity().getResources().getString(R.string.title_activity_login), (View.OnClickListener) v -> {
                    Intent intent = ((DealsModuleRouter) getActivity().getApplication()).
                            getLoginIntent(getActivity());
                    toActivityRequest.onNavigateToActivityRequest(intent, DealsHomeActivity.REQUEST_CODE_LOGIN);
                }
        ).show();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ItemViewHolder(View itemView) {
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
        }

        public void bindData(final ProductItem productItem) {
            dealsDetails.setText(productItem.getDisplayName());
            ImageHandler.loadImage(context, dealImage, productItem.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            if (!brandPageCard) {
                ImageHandler.loadImage(context, brandImage, productItem.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
                brandName.setText(productItem.getBrand().getTitle());
                if (productItem.getBrand().getUrl() != null) {
                    cvBrand.setOnClickListener(this);
                }

            } else {
                cvBrand.setVisibility(View.GONE);
                brandName.setVisibility(View.GONE);
                Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_location);
                dealavailableLocations.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                dealavailableLocations.setCompoundDrawablePadding(getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8));

            }
            setLikes(productItem.getLikes(), productItem.isLiked());

            if (productItem.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(productItem.getCityName())) {
                Location location = Utils.getSingletonInstance().getLocation(context);
                if (location != null) {
                    dealavailableLocations.setText(location.getName());
                }
            } else {
                dealavailableLocations.setText(productItem.getCityName());
            }
            if (productItem.getMrp() != 0) {
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

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_share) {
                Utils.getSingletonInstance().shareDeal(categoryItems.get(getIndex()).getSeoUrl(),
                        context, categoryItems.get(getIndex()).getDisplayName(),
                        categoryItems.get(getIndex()).getImageWeb());
            } else if (v.getId() == R.id.iv_wish_list) {

                boolean isLoggedIn = mPresenter.setDealLike(categoryItems.get(getIndex()), getIndex());
                if (isLoggedIn) {
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
                toActivityRequest.onNavigateToActivityRequest(detailsIntent, DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY);
            }
        }
    }

    public class ItemViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        public ItemViewHolder2(View itemView) {
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

        public void bindData(final ProductItem productItem) {
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
            if (productItem.getMrp() != 0) {
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

        public void bindData(SpannableString headerText) {
            dealsInCity.setText(headerText);
        }
    }

    public class TopSuggestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDealTitle;
        private TextView tvBrandName;
        private View itemView;
        private ProductItem valueItem;
        private int index;

        private TopSuggestionHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            tvDealTitle = itemView.findViewById(R.id.tv_simple_item);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
        }

        private void setDealTitle(int position, ProductItem value) {
            this.valueItem = value;
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
            tvBrandName.setText(value.getBrand().getTitle());
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
            Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
            detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
            context.startActivity(detailsIntent);
        }
    }

    public void unsubscribeUseCase() {
        mPresenter.onDestroy();
    }

    public interface INavigateToActivityRequest {
        void onNavigateToActivityRequest(Intent intent, int requestCode);
    }
}
