package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

/**
 * Created by Nisie on 10/06/15.
 */
public class WishlistRecyclerViewAdapter extends RecyclerView.Adapter<WishlistRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public ImageView productImage;
        public LinearLayout productLayout;
        public LinearLayout badgesContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            productLayout = (LinearLayout) itemView.findViewById(R.id.main_content);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            shopName = (TextView) itemView.findViewById(R.id.product_shop);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        }

        public Context getContext(){
            return itemView.getContext();
        }
    }

    private Context context;
    private List<ProductItem> data;

    public WishlistRecyclerViewAdapter(Context context, List<ProductItem> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public WishlistRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_product_item_small, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(WishlistRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(data.get(position).name));
        holder.productPrice.setText(data.get(position).price);
        holder.shopName.setText(data.get(position).shop);
        setProductImage(holder, data.get(position));
        setBadges(holder, data.get(position));
        holder.productLayout.setOnClickListener(onProductClicked(data.get(position)));
    }

    private View.OnClickListener onProductClicked(final ProductItem item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventFavoriteView(item.getName());

                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, ProductInfoActivity.class);
                bundle.putString("product_id", item.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }

    private void setProductImage(ViewHolder holder, ProductItem data) {
        ImageHandler.loadImageFit2(holder.getContext(), holder.productImage, data.imgUri);
    }

    @Override
    public int getItemCount() {
       if(data.size()>4)return 4;
        else return data.size();
    }

    private void setBadges(ViewHolder holder, ProductItem data) {
        if (data.getBadges() != null && holder.badgesContainer.getChildCount() == 0)
            for (ProductItem.Badge badges : data.getBadges()) {
                LuckyShopImage.loadImage(context, badges.getImageUrl(), holder.badgesContainer);
            }
    }
}