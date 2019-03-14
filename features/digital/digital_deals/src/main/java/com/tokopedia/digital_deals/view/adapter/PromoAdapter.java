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

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.promo_item, parent, false);
        dealsAnalytics = new DealsAnalytics(context.getApplicationContext());

        RecyclerView.ViewHolder vh = new PromosViewHolder(imageLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PromosViewHolder) holder).bindData(productItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public class PromosViewHolder extends RecyclerView.ViewHolder {
        private ImageView promoImage;

        public PromosViewHolder(View itemView) {
            super(itemView);
            promoImage = itemView.findViewById(R.id.banner_item);
        }

        void bindData(ProductItem item, int position) {
            ImageHandler.loadImage(context, promoImage, item.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
            promoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.sendEventEcommerce(item.getId(), position, item.getDisplayName(), DealsAnalytics.EVENT_PROMO_CLICK
                            , DealsAnalytics.EVENT_CLICK_PROMO_BANNER, DealsAnalytics.LIST_DEALS_TOP_BANNER);
                    mPresenter.onClickBanner();
                }
            });
        }
    }
}
