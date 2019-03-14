package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;

import java.util.ArrayList;
import java.util.List;

public class DealsBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Brand> brandItems;
    private Context context;
    private int MAX_BRANDS = 8;

    public static final int ITEM_BRAND_HOME = 1;
    public static final int ITEM_BRAND_SHORT = 2;
    public static final int ITEM_BRAND_NORMAL = 3;
    private static final int ITEM_FOOTER = 4;

    private int itemViewType;
    private boolean isFooterAdded = false;
    private boolean isPopularBrands;
    DealsAnalytics dealsAnalytics;
    private boolean fromSearchResult;


    public DealsBrandAdapter(List<Brand> brandItems, int itemViewType) {

        if (brandItems != null && itemViewType == ITEM_BRAND_SHORT) {
            this.brandItems = new ArrayList<>();
            MAX_BRANDS = brandItems.size() < MAX_BRANDS ? brandItems.size() : MAX_BRANDS;
            for (int i = 0; i < MAX_BRANDS; i++) {
                this.brandItems.add(brandItems.get(i));
            }
        } else {
            this.brandItems = (brandItems == null ? new ArrayList<>() : brandItems);
        }
        this.itemViewType = itemViewType;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder instanceof BrandViewHolder) {
            BrandViewHolder holder1 = ((BrandViewHolder) holder);
            if (fromSearchResult)
                holder1.setShown(false);
            if (!holder1.isShown()) {
                holder1.setShown(true);

                if (isPopularBrands) {
                    dealsAnalytics.sendEcommerceBrand(brandItems.get(holder1.getIndex()).getId(),
                            holder1.getIndex(), brandItems.get(holder1.getIndex()).getTitle(), DealsAnalytics.EVENT_PROMO_VIEW, DealsAnalytics.EVENT_IMPRESSION_POPULAR_BRAND, DealsAnalytics.LIST_DEALS_TRENDING);

                } else {
                    dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_VIEW_SEARCH_BRAND_RESULT,
                            String.format("%s - %s", brandItems.get(holder1.getIndex()).getTitle()
                                    , holder1.getIndex()));
                }
            }
        }

    }

    public void updateAdapter(List<Brand> brands, boolean fromSearchResult) {
        this.brandItems = new ArrayList<>(brands);
        this.fromSearchResult = fromSearchResult;
        notifyDataSetChanged();
    }

    public void setPopularBrands(boolean popularBrands) {
        this.isPopularBrands = popularBrands;
    }


    public class BrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView imageViewBrandItem;
        private TextView brandName;
        private int index;
        private boolean isShown;

        public BrandViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewBrandItem = itemView.findViewById(R.id.iv_brand);
            brandName = itemView.findViewById(R.id.brandName);
        }

        public void bindData(final Brand brand, int position) {
            if (itemViewType == ITEM_BRAND_HOME) {
                brandName.setText(brand.getTitle());
            }
            ImageHandler.loadImage(context, imageViewBrandItem, brand.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            itemView.setOnClickListener(this);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean isShown() {
            return isShown;
        }

        public void setShown(boolean shown) {
            isShown = shown;
        }

        @Override
        public void onClick(View v) {
            if (isPopularBrands) {
                dealsAnalytics.sendEcommerceBrand(brandItems.get(getIndex()).getId(),
                        getIndex(), brandItems.get(getIndex()).getTitle(), DealsAnalytics.EVENT_PROMO_CLICK, DealsAnalytics.EVENT_CLICK_POPULAR_BRAND, DealsAnalytics.LIST_DEALS_TRENDING);

            } else {
                dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_SEARCH_BRAND_RESULT,
                        String.format("%s - %s", brandItems.get(getIndex()).getTitle(), getIndex()));
            }

            Intent detailsIntent = new Intent(context, BrandDetailsActivity.class);
            detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, brandItems.get(getIndex()));
            context.startActivity(detailsIntent);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

    @Override
    public int getItemCount() {
        return (brandItems == null) ? 0 : brandItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? ITEM_FOOTER : itemViewType;
    }

    private boolean isLastPosition(int position) {
        return (position == brandItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new Brand(), true);
        }
    }

    public void add(Brand item, boolean refreshItem) {
        brandItems.add(item);
        if (refreshItem)
            notifyItemInserted(brandItems.size() - 1);
    }

    public void addAll(List<Brand> items, Boolean... refreshItems) {
        boolean refreshItem = true;
        if (refreshItems.length > 0)
            refreshItem = refreshItems[0];
        if (items != null) {
            for (Brand item : items) {
                add(item, refreshItem);
            }
        }
    }


    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = brandItems.size() - 1;
            Brand item = brandItems.get(position);

            if (item != null) {
                brandItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        dealsAnalytics = new DealsAnalytics(context.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View view = null;
        switch (viewType) {
            case ITEM_BRAND_HOME:
                view = inflater.inflate(R.layout.item_brand_home, parent, false);
                break;
            case ITEM_BRAND_SHORT:
                view = inflater.inflate(R.layout.item_brand_short, parent, false);
                break;
            case ITEM_BRAND_NORMAL:
                view = inflater.inflate(R.layout.item_brand_normal, parent, false);
                break;
            case ITEM_FOOTER:
                view = inflater.inflate(R.layout.footer_layout, parent, false);
                break;
        }
        return new DealsBrandAdapter.BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != ITEM_FOOTER) {
            ((DealsBrandAdapter.BrandViewHolder) holder).bindData(brandItems.get(position), position);
            ((DealsBrandAdapter.BrandViewHolder) holder).setIndex(position);
        }
    }
}
