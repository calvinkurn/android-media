package com.tokopedia.digital_deals.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.domain.getusecase.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class TrendingDealsAdapter extends BaseAdapter<ProductItem> implements DealCategoryAdapterContract.View {

    private Context context;
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

    public TrendingDealsAdapter(Context context, AdapterCallback callback, INavigateToActivityRequest toActivityRequest, String url, String title, boolean brandPageCard, boolean isDealsHomeLayout, int homePosition) {
        super(callback);
        this.context = context;
        this.toActivityRequest = toActivityRequest;
        this.url = url;
        this.title = title;
        this.brandPageCard = brandPageCard;
        this.isDealsHomeLayout = isDealsHomeLayout;
        this.homePosition = homePosition;
        dealsAnalytics = new DealsAnalytics();
        getCategoryDetailRequestUseCase = new GetCategoryDetailRequestUseCase();

        if (!title.equalsIgnoreCase(context.getResources().getString(com.tokopedia.digital_deals.R.string.trending_deals))) {
            dealType = DealsAnalytics.CURATED_DEALS;
        } else {
            dealType = DealsAnalytics.TRENDING_DEALS;
        }

        DaggerDealsComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
        ).build().inject(this);
    }


    @Override
    public void loadData(int currentPageIndex) {
        super.loadData(currentPageIndex);
        getCategoryDetailRequestUseCase.setCategoryUrl(url);
        getCategoryDetailRequestUseCase.setRequestParams(getParams());
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
                loadCompleted(dealEntity.getDealItems(), dealEntity);
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
                .inflate(com.tokopedia.digital_deals.R.layout.deal_item_card, parent, false);
        mPresenter.attachView(this);
        mPresenter.initialize();
        return new TrendingDealsAdapter.ItemViewHolderNormal(itemView);
    }

    @Override
    public Activity getActivity() {
        return (Activity) context;
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (!TextUtils.isEmpty(location.getCoordinates())) {
            requestParams.putString(Utils.LOCATION_COORDINATES, location.getCoordinates());
        }
        if (location.getLocType() != null && !TextUtils.isEmpty(location.getLocType().getName())) {
            requestParams.putString(Utils.LOCATION_TYPE, location.getLocType().getName());
        }
        return requestParams;
    }

    @Override
    public void notifyDataSetChanged(int position) {

    }

    @Override
    public void showLoginSnackbar(String message, int position) {
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
                getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.title_activity_login), v -> {
                    Intent intent = RouteManager.getIntent(context, ApplinkConst.LOGIN);
                    toActivityRequest.onNavigateToActivityRequest(intent, DealsHomeActivity.REQUEST_CODE_LOGIN, position);
                }
        ).show();
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
            dealImage = itemView.findViewById(com.tokopedia.digital_deals.R.id.iv_product);
            tvLikes = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_wish_list);
            discount = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_off);
            dealsDetails = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_deal_intro);
            brandName = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_brand_name);
            ivFavourite = itemView.findViewById(com.tokopedia.digital_deals.R.id.iv_wish_list);
            dealavailableLocations = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_available_locations);
            dealListPrice = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_mrp);
            brandImage = itemView.findViewById(com.tokopedia.digital_deals.R.id.iv_brand);
            ivShareVia = itemView.findViewById(com.tokopedia.digital_deals.R.id.iv_share);
            dealSellingPrice = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_sales_price);
            hotDeal = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_hot_deal);
            cvBrand = itemView.findViewById(com.tokopedia.digital_deals.R.id.cv_brand);
        }

        @Override
        public void bindView(ProductItem item, int position) {
            bindData(item, position);
        }

        void bindData(final ProductItem productItem, int itemPosition) {
            this.index = itemPosition;
            dealsDetails.setText(productItem.getDisplayName());
            ImageHandler.loadImage(context, dealImage, productItem.getImageWeb(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
            if (!brandPageCard) {
                ImageHandler.loadImage(context, brandImage, productItem.getBrand().getFeaturedThumbnailImage(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
                brandName.setText(productItem.getBrand().getTitle());
                if (productItem.getBrand().getUrl() != null) {
                    cvBrand.setOnClickListener(this);
                }
            } else {
//                productItem.setBrand(brand);
                cvBrand.setVisibility(View.GONE);
                brandName.setVisibility(View.GONE);
                Drawable img = MethodChecker.getDrawable(context, com.tokopedia.digital_deals.R.drawable.ic_location);
                dealavailableLocations.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                dealavailableLocations.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8));

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
            getItems().get(getIndex()).setLikes(likes);
            getItems().get(getIndex()).setLiked(isLiked);
            if (likes > 0) {
                tvLikes.setVisibility(View.VISIBLE);
                tvLikes.setText(String.valueOf(likes));
            } else {
                tvLikes.setVisibility(View.GONE);
            }
            if (isLiked) {
                ivFavourite.setImageDrawable(MethodChecker.getDrawable(ivFavourite.getContext(), com.tokopedia.digital_deals.R.drawable.ic_wishlist_filled));
            } else {
                ivFavourite.setImageDrawable(MethodChecker.getDrawable(ivFavourite.getContext(), com.tokopedia.digital_deals.R.drawable.ic_wishlist_unfilled));
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
            if (getItems().get(getIndex()).getBrand() == null)
                return "";
            return String.format("%s - %s - %s", getItems().get(getIndex()).getBrand().getTitle()
                    , getItems().get(getIndex()).getDisplayName(),
                    position);
        }

        private String getFavouriteLabel(int position) {
            String str = LIKE;
            if (getItems().get(getIndex()).isLiked())
                str = UNLIKE;
            if (getItems().get(getIndex()).getBrand() == null)
                return "";
            return String.format("%s - %s - %s - %s",
                    getItems().get(getIndex()).getBrand().getTitle()
                    , getItems().get(getIndex()).getDisplayName()
                    , position
                    , str);
        }

        @Override
        public void onClick(View v) {
            int position = getIndex();
            if (v.getId() == com.tokopedia.digital_deals.R.id.iv_share) {
                dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SHARE,
                        getShareLabel(position));

                Utils.getSingletonInstance().shareDeal(getItems().get(getIndex()).getSeoUrl(),
                        context, getItems().get(getIndex()).getDisplayName(),
                        getItems().get(getIndex()).getImageWeb(), getItems().get(getIndex()).getDesktopUrl());
            } else if (v.getId() == com.tokopedia.digital_deals.R.id.iv_wish_list) {
                ProductItem item = getItems().get(getIndex());
                boolean isLoggedIn = mPresenter.setDealLike(item.getId(), item.isLiked(), getIndex(), item.getLikes());
                if (isLoggedIn) {
                    dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_LOVE, getFavouriteLabel(position));
                    if (getItems().get(getIndex()).isLiked()) {
                        setLikes(getItems().get(getIndex()).getLikes() - 1, !getItems().get(getIndex()).isLiked());
                    } else {
                        setLikes(getItems().get(getIndex()).getLikes() + 1, !getItems().get(getIndex()).isLiked());
                    }
                }
            } else if (v.getId() == com.tokopedia.digital_deals.R.id.cv_brand) {
                Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
                detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, getItems().get(getIndex()).getBrand());
                context.startActivity(detailsIntent);
            } else {
                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, getItems().get(getIndex()).getSeoUrl());
                toActivityRequest.onNavigateToActivityRequest(detailsIntent, DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY, getIndex());
                if (dealType.equalsIgnoreCase(DealsAnalytics.TRENDING_DEALS)) {
                    dealsAnalytics.sendTrendingDealClickEvent(getItems().get(getIndex()), DealsAnalytics.EVENT_CLICK_TRENDING_DEALS, position, 0);
                } else if (dealType.equalsIgnoreCase(DealsAnalytics.CURATED_DEALS)) {
                    dealsAnalytics.sendTrendingDealClickEvent(getItems().get(getIndex()), DealsAnalytics.EVENT_CLICK_CURATED_DEALS, position, homePosition);
                } else if (dealType.equalsIgnoreCase(DealsAnalytics.CATEGORY_DEALS)) {
                    dealsAnalytics.sendCategoryDealClickEvent(getItems().get(getIndex()), position, DealsAnalytics.EVENT_CLICK_CATEGORY_DEALS);
                } else {
                    dealsAnalytics.sendDealClickEvent(getItems().get(getIndex()), position, DealsAnalytics.EVENT_CLICK_PRODUCT_BRAND);
                }

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
