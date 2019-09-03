package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class TrendingDealsAdapter extends BaseAdapter<ProductItem> {

    private Context context;
    private List<ProductItem> categoryItems;
    private GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase;
    private DealsAnalytics dealsAnalytics;
    private INavigateToActivityRequest toActivityRequest;
    @Inject
    DealCategoryAdapterPresenter mPresenter;
    private String dealType;
    private String url;
    private String title;
    private boolean brandPageCard;
    private boolean isDealsHomeLayout;
    private int homePosition;
    private Brand brand;

    public TrendingDealsAdapter(Context context, List<ProductItem> categoryItems, AdapterCallback callback, INavigateToActivityRequest toActivityRequest, String url, String title, boolean brandPageCard, boolean isDealsHomeLayout, int homePosition) {
        super(callback);
        this.context = context;
        this.categoryItems = categoryItems;
        this.toActivityRequest = toActivityRequest;
        this.url = url;
        this.title = title;
        this.brandPageCard = brandPageCard;
        this.isDealsHomeLayout = isDealsHomeLayout;
        this.homePosition = homePosition;
        dealsAnalytics = new DealsAnalytics();
        getCategoryDetailRequestUseCase = new GetCategoryDetailRequestUseCase();

        if (!title.equalsIgnoreCase(context.getResources().getString(R.string.trending_deals))) {
            dealType = DealsAnalytics.CURATED_DEALS;
        } else {
            dealType = DealsAnalytics.TRENDING_DEALS;
        }
    }

    @Override
    public void loadData(int currentPageIndex) {
        super.loadData(currentPageIndex);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealsHomePresenter.TAG, url);
        getCategoryDetailRequestUseCase.setRequestParams(url);
        getCategoryDetailRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                loadCompletedWithError();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<CategoryDetailsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                CategoryDetailsResponse dealEntity = (CategoryDetailsResponse) dataResponse.getData();
                if (dealEntity != null && dealEntity.getDealItems() != null) {
                    loadCompleted(dealEntity.getDealItems(), dealEntity);
                }
                if (dealEntity.getPage() == null || !URLUtil.isValidUrl(dealEntity.getPage().getUriNext())) {
                    setLastPage(true);
                } else {
                    url = dealEntity.getPage().getUriNext();
                }
            }
        });
    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_item_card, parent, false);
        return new TrendingDealsAdapter.ItemViewHolderNormal(itemView);
    }


    public class ItemViewHolderNormal extends BaseVH implements View.OnClickListener {
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
        }

        @Override
        public void bindView(ProductItem item, int position) {
            bindData(item, position);
        }

        void bindData(final ProductItem productItem, int itemPosition) {
            this.index = itemPosition;
            dealsDetails.setText(productItem.getDisplayName());
            ImageHandler.loadImage(context, dealImage, productItem.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            if (!brandPageCard) {
                ImageHandler.loadImage(context, brandImage, productItem.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
                brandName.setText(productItem.getBrand().getTitle());
                if (productItem.getBrand().getUrl() != null) {
                    cvBrand.setOnClickListener(this);
                }
            } else {
//                productItem.setBrand(brand);
                cvBrand.setVisibility(View.GONE);
                brandName.setVisibility(View.GONE);
                Drawable img = MethodChecker.getDrawable(context, R.drawable.ic_location);
                dealavailableLocations.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                dealavailableLocations.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.dp_8));

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
                ivFavourite.setImageDrawable(MethodChecker.getDrawable(ivFavourite.getContext(), R.drawable.ic_wishlist_filled));
            } else {
                ivFavourite.setImageDrawable(MethodChecker.getDrawable(ivFavourite.getContext(), R.drawable.ic_wishlist_unfilled));
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
                if (dealType.equalsIgnoreCase(DealsAnalytics.TRENDING_DEALS)) {
                    dealsAnalytics.sendTrendingDealClickEvent(categoryItems.get(getIndex()), DealsAnalytics.EVENT_CLICK_TRENDING_DEALS, position, 0);
                } else if (dealType.equalsIgnoreCase(DealsAnalytics.CURATED_DEALS)) {
                    dealsAnalytics.sendTrendingDealClickEvent(categoryItems.get(getIndex()), DealsAnalytics.EVENT_CLICK_CURATED_DEALS, position, homePosition);
                } else if (dealType.equalsIgnoreCase(DealsAnalytics.CATEGORY_DEALS)) {
                    dealsAnalytics.sendCategoryDealClickEvent(categoryItems.get(getIndex()), position, DealsAnalytics.EVENT_CLICK_CATEGORY_DEALS);
                } else {
                    dealsAnalytics.sendDealClickEvent(categoryItems.get(getIndex()), position, DealsAnalytics.EVENT_CLICK_PRODUCT_BRAND);
                }

            }
        }
    }


    public interface INavigateToActivityRequest {
        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);
    }

}
