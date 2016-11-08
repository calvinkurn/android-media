package com.tokopedia.core.discovery.adapter.browseparent;

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
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.discovery.adapter.ProductAdapter;
import com.tokopedia.core.discovery.model.BrowseShopModel;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 7/1/2016.
 */
public class BrowseShopAdapter extends ProductAdapter {

    public static final int SHOP_MODEL_TYPE = 1_912_123;

    public BrowseShopAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SHOP_MODEL_TYPE:
                return createViewShop(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case SHOP_MODEL_TYPE:
                ((ShopViewHolder)holder).bindData(getData().get(position), position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public static ShopViewHolder createViewShop(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_shop, parent, false);
        return new ShopViewHolder(itemLayoutView);
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        @Bind(R2.id.shop_1)
        LinearLayout mainContent;

        @Bind(R2.id.item_shop_image)
        SquareImageView itemShopImage;

        @Bind(R2.id.item_shop_gold)
        ImageView itemShopGold;

        @Bind(R2.id.item_shop_lucky)
        ImageView itemShopLucky;

        @Bind(R2.id.item_shop_name)
        TextView itemShopName;

        @Bind(R2.id.item_shop_bought)
        TextView itemShopBought;

        @Bind(R2.id.item_shop_count_fav)
        TextView itemShopCountFav;

        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(RecyclerViewItem recyclerViewItem, int position){
            if(recyclerViewItem != null && recyclerViewItem instanceof ShopModel){
                bindData((ShopModel) recyclerViewItem, position);
            }
        }

        public void bindData(final ShopModel shopModel, int position){
            final Context context = itemView.getContext();
            ImageHandler.loadImageThumbs(context, itemShopImage, shopModel.shopImage);
            itemShopBought.setText(shopModel.totalTransaction + " " + context.getString(R.string.title_total_tx));
            itemShopCountFav.setText(shopModel.numberOfFavorite + " " + context.getString(R.string.title_favorite));
            itemShopName.setText(shopModel.shopName);
            if (shopModel.isGold.equals("1")) {
                itemShopGold.setVisibility(View.VISIBLE);
            } else {
                itemShopGold.setVisibility(View.GONE);
            }
            if (shopModel.luckyImage != null){
                LuckyShopImage.loadImage(itemShopLucky, shopModel.luckyImage);
            }
            if(shopModel.isOfficial){
                itemShopBought.setVisibility(View.GONE);
            } else {
                itemShopBought.setVisibility(View.VISIBLE);
            }
            mainContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopInfoActivity.class);
                    intent.putExtras(ShopInfoActivity.createBundle(shopModel.shopId, ""));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()){
            case SHOP_MODEL_TYPE:
                return recyclerViewItem.getType();
        }

        return super.isInType(recyclerViewItem);
    }

    @Parcel
    public static class ShopModel extends RecyclerViewItem{
        String shopImage;
        String shopName;
        String totalTransaction;
        String numberOfFavorite;
        String shopId;
        String isGold;
        String luckyImage;
        boolean isOfficial;

        public ShopModel() {
            setType(SHOP_MODEL_TYPE);
        }

        public ShopModel(BrowseShopModel.Shops shop){
            this();
            shopImage = shop.shopImage;
            shopName = shop.shopName;
            totalTransaction = shop.shopTotalTransaction;
            numberOfFavorite = shop.shopTotalFavorite;
            shopId = shop.shopId;
            isGold = shop.shopGoldShop;
            luckyImage = shop.shopLucky;
            isOfficial = shop.isOfficial;
        }
    }

}
