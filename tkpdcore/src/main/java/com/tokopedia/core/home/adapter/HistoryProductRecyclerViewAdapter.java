package com.tokopedia.core.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Nisie on 4/06/15.
 * modified by m.normansyah on 06/01/2015 - set item id to distinct items
 */
public class HistoryProductRecyclerViewAdapter extends RecyclerView.Adapter<HistoryProductRecyclerViewAdapter.ViewHolder>{

    private List<ProductItem> data;
    private Context context;

    public void setData(List<ProductItem> productItems) {
        data.clear();
        data = productItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mainContent;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public ImageView productImage;
        public LinearLayout badgesContainer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mainContent = (LinearLayout) itemLayoutView.findViewById(R.id.main_content);
            productName = (TextView) itemLayoutView.findViewById(R.id.product_name);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.product_price);
            shopName = (TextView) itemLayoutView.findViewById(R.id.product_shop);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            badgesContainer = (LinearLayout) itemLayoutView.findViewById(R.id.badges_container);
        }

        public Context getContext(){
            return itemView.getContext();
        }
    }

    public HistoryProductRecyclerViewAdapter(Context context, List<ProductItem> data) {
        this.context = context;
        this.data = data;
//        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_product_item_small, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(data.get(position).getName()));
        holder.productPrice.setText(data.get(position).getPrice());
        holder.shopName.setText(data.get(position).getShop());

        ImageHandler.loadImageFit2(holder.getContext(),
                holder.productImage, data.get(position).getImgUri());

        setBadges(holder, data.get(position));

        holder.mainContent.setOnClickListener(onProductItemClicked(position));
    }

    private View.OnClickListener onProductItemClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < data.size()) {
                    UnifyTracking.eventFeedRecent(view.getContext(), data.get(position).getName());
                    Intent intent = RouteManager.getIntentInternal(getContext(),
                            UriUtil.buildUri(ApplinkConstInternal.Marketplace.PRODUCT_DETAIL, data.get(position).getId()));
                    context.startActivity(intent);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if(data.size()>4)return 4;
        else return data.size();
    }

    public List<ProductItem> getData() {
        return data;
    }

    public void addAll(List<ProductItem> newData) {
        data.clear();
        data.addAll(newData);
    }


    private void setBadges(ViewHolder holder, ProductItem data) {
        holder.badgesContainer.removeAllViews();
        if (data.getBadges() != null) {
            for (com.tokopedia.core.var.Badge badge : data.getBadges()) {
                View view = LayoutInflater.from(context).inflate(R.layout.badge_layout_small, null);
                ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
                holder.badgesContainer.addView(view);
                LuckyShopImage.loadImage(imageBadge, badge.getImageUrl());
            }
        }

    }

    private ProductPass getProductDataToPass(ProductItem recentView) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(recentView.getPrice())
                .setProductId(recentView.getId())
                .setProductName(recentView.getName())
                .setProductImage(recentView.getImgUri())
                .build();
    }
}