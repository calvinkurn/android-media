package com.tokopedia.digital_deals.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealCategoryAdapterPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class DealsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DealCategoryAdapterContract.View {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;
    private final int ITEM = 1;
    private final int FOOTER = 2;
    private final int ITEM2 = 3;
    private boolean isFooterAdded = false;
    private boolean isShortLayout;
    private boolean brandPageCard = false;
    @Inject
    DealCategoryAdapterPresenter mPresenter;

    public DealsCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems, Boolean... layoutType) {
        this.context = context;
        this.categoryItems = categoryItems;
        if (layoutType[0] != null) {
            this.isShortLayout = layoutType[0];
        }
        if (layoutType.length > 1) {
            if (layoutType[1] != null) {
                brandPageCard = layoutType[1];
            }
        }

    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
            default:
                break;
        }

        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(context))
                .build().inject(this);
        mPresenter.attachView(this);
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
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        return isShortLayout ? (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM2
                : (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new CategoryItemsViewModel());
        }
    }

    public void add(CategoryItemsViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<CategoryItemsViewModel> items) {
        for (CategoryItemsViewModel item : items) {
            add(item);
        }
    }


    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            CategoryItemsViewModel item = categoryItems.get(position);

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
        notifyItemChanged(position);
    }

    @Override
    public void showLoginSnackbar(String message) {
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).setAction(
                getActivity().getResources().getString(R.string.title_activity_login), (View.OnClickListener) v -> {
                    Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).
                            getLoginIntent(getActivity());
                    getActivity().startActivityForResult(intent, 1099);
                }
        ).show();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private ImageView brandImage;
        private TextView discount;
        private TextView likes;
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
            likes = itemView.findViewById(R.id.tv_wish_list);
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

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel) {
            dealsDetails.setText(categoryItemsViewModel.getDisplayName());
            ImageHandler.loadImage(context, dealImage, categoryItemsViewModel.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            if (!brandPageCard) {
                ImageHandler.loadImage(context, brandImage, categoryItemsViewModel.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
                brandName.setText(categoryItemsViewModel.getBrand().getTitle());
                if (categoryItemsViewModel.getBrand().getUrl() != null) {
                    cvBrand.setOnClickListener(this);
                }

            } else {
                cvBrand.setVisibility(View.GONE);
                brandName.setVisibility(View.GONE);
                Drawable img = getActivity().getResources().getDrawable(R.drawable.ic_location);
                dealavailableLocations.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                dealavailableLocations.setCompoundDrawablePadding(getActivity().getResources().getDimensionPixelSize(R.dimen.dp_8));

            }
            if (categoryItemsViewModel.getLikes() != 0) {
                likes.setVisibility(View.VISIBLE);
                likes.setText(String.valueOf(categoryItemsViewModel.getLikes()));
            } else {
                likes.setVisibility(View.GONE);
            }
            if (categoryItemsViewModel.isLiked()) {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_filled);
            } else {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_unfilled);
            }

            if (categoryItemsViewModel.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            LocationViewModel location = Utils.getSingletonInstance().getLocation(context);
            if (location != null) {
                dealavailableLocations.setText(location.getName());
            }
            dealListPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getMrp()));
            dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(categoryItemsViewModel.getSavingPercentage());
            dealSellingPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getSalesPrice()));
            itemView.setOnClickListener(this);
            ivShareVia.setOnClickListener(this);
            ivFavourite.setOnClickListener(this);
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
                mPresenter.setDealLike(categoryItems.get(getIndex()), getIndex());
            } else if (v.getId() == R.id.cv_brand) {
                Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
                detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, categoryItems.get(getIndex()).getBrand());
                context.startActivity(detailsIntent);
            } else {
                Intent detailsIntent = new Intent(context, DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItems.get(getIndex()).getSeoUrl());
                detailsIntent.putExtra(DealsHomeFragment.FROM_HOME, true);
                detailsIntent.putExtra(DealsHomeFragment.ADAPTER_POSITION, getIndex());
                getActivity().startActivityForResult(detailsIntent, DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY);
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

        public void bindData(final CategoryItemsViewModel categoryItemsViewModel) {
            dealsDetails.setText(categoryItemsViewModel.getDisplayName());
            ImageHandler.loadImage(context, dealImage, categoryItemsViewModel.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            ImageHandler.loadImage(context, brandImage, categoryItemsViewModel.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            brandName.setText(categoryItemsViewModel.getBrand().getTitle());
            if (categoryItemsViewModel.getDisplayTags() != null) {
                hotDeal.setVisibility(View.VISIBLE);
            } else {
                hotDeal.setVisibility(View.GONE);
            }
            if (categoryItemsViewModel.getBrand().getUrl() != null) {
                cvBrand.setOnClickListener(this);
            }
            dealListPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getMrp()));
            dealListPrice.setPaintFlags(dealListPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(categoryItemsViewModel.getSavingPercentage());
            dealSellingPrice.setText(Utils.convertToCurrencyString(categoryItemsViewModel.getSalesPrice()));
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

    public void unsubscribeUseCase() {
        mPresenter.onDestroy();
    }
}
