package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromosViewHolder> {

    private List<ProductItem> productItems;
    private Context context;
    private DealsHomePresenter mPresenter;
    DealsAnalytics dealsAnalytics;

    public PromoAdapter(List<ProductItem> productItems, DealsHomePresenter presenter) {
        this.productItems = productItems;
        this.mPresenter = presenter;
    }

    @NonNull
    @Override
    public PromosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.promo_item, parent, false);
        dealsAnalytics = new DealsAnalytics();

        PromosViewHolder vh = new PromosViewHolder(imageLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PromosViewHolder holder, int position) {
        holder.setShown(productItems.get(position).isTrack());
        holder.setIndex(position);
        holder.bindData(productItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PromosViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (!holder.isShown()) {
            holder.setShown(true);
            productItems.get(holder.getAdapterPosition()).setTrack(true);
            dealsAnalytics.sendPromoImpressionEvent(productItems.get(holder.getIndex()), holder.getIndex());
        }

    }

    public class PromosViewHolder extends RecyclerView.ViewHolder {
        private ImageView promoImage;
        private boolean isShown;
        private int index;

        public PromosViewHolder(View itemView) {
            super(itemView);
            promoImage = itemView.findViewById(R.id.banner_item);
        }

        boolean isShown() {
            return isShown;
        }

        void setShown(boolean isShown) {
            this.isShown = isShown;
        }

        void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        void bindData(ProductItem item, int position) {
            ImageHandler.loadImage(context, promoImage, item.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            promoImage.setOnClickListener(view -> {
                mPresenter.sendEventEcommerce(item, position, item.getDisplayName(), DealsAnalytics.EVENT_PROMO_CLICK
                        , DealsAnalytics.EVENT_CLICK_PROMO_BANNER, DealsAnalytics.LIST_SUGGESTED_DEALS);
                mPresenter.onClickBanner(item);
            });
        }
    }
}
