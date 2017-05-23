/*
 * Created By Kulomady on 11/26/16 1:07 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 1:07 AM
 */

package com.tokopedia.discovery.adapter.browseparent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.discovery.adapter.ProductAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.network.entity.discovery.ShopModel.SHOP_MODEL_TYPE;

/**
 * Created by Toped10 on 7/1/2016.
 */
public class BrowseShopAdapter extends ProductAdapter {


    private BrowseProductRouter.GridType gridType;

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
                ((ShopViewHolder)holder).bindData(context, getData().get(position), position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public ShopViewHolder createViewShop(ViewGroup parent) {
        int layoutResId;
        switch (gridType) {
            case GRID_1:
                layoutResId = R.layout.layout_item_shop_list;
                break;
            case GRID_2:
            case GRID_3:
            default:
                layoutResId = R.layout.layout_item_shop;
                break;
        }

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new ShopViewHolder(itemLayoutView);
    }

    public void setViewType(BrowseProductRouter.GridType gridType) {
        this.gridType = gridType;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        @BindView(R2.id.shop_1)
        LinearLayout mainContent;

        @BindView(R2.id.item_shop_image)
        SquareImageView itemShopImage;

        @BindView(R2.id.item_shop_gold)
        ImageView itemShopBadge;

        @BindView(R2.id.item_shop_name)
        TextView itemShopName;

        @BindView(R2.id.reputation_view)
        ImageView reputationView;

        @BindView(R2.id.shop_location)
        TextView shopLocation;

        @BindView(R2.id.shop_item_preview_1)
        ImageView itemPreview1;

        @BindView(R2.id.shop_item_preview_2)
        ImageView itemPreview2;

        @BindView(R2.id.shop_item_preview_3)
        ImageView itemPreview3;

        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Context context, RecyclerViewItem recyclerViewItem, int position){
            if(recyclerViewItem != null && recyclerViewItem instanceof ShopModel){
                bindData(context, (ShopModel) recyclerViewItem, position);
            }
        }

        public void bindData(final Context context, final ShopModel shopModel, int position){
            ImageHandler.loadImageThumbs(context, itemShopImage, shopModel.getShopImage());
            itemShopName.setText(shopModel.getShopName());
            if(shopModel.isOfficial() || shopModel.getIsGold().equals("1")){
                itemShopBadge.setVisibility(View.VISIBLE);
                if(shopModel.isOfficial()) {
                    itemShopBadge.setImageResource(R.drawable.ic_badge_official);
                } else if(shopModel.getIsGold().equals("1")){
                    itemShopBadge.setImageResource(R.drawable.ic_shop_gold);
                }
            } else {
                itemShopBadge.setVisibility(View.GONE);
            }
            mainContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopInfoActivity.class);
                    intent.putExtras(ShopInfoActivity.createBundle(shopModel.getShopId(), ""));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            shopLocation.setText(shopModel.getLocation());
            ImageHandler.LoadImage(reputationView, shopModel.getReputationImageUrl());

            try{
                itemPreview1.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(itemPreview1, shopModel.getProductImages().get(0));
            } catch (IndexOutOfBoundsException e) {
                itemPreview1.setVisibility(View.INVISIBLE);
            }

            try{
                itemPreview2.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(itemPreview2, shopModel.getProductImages().get(1));
            } catch (IndexOutOfBoundsException e) {
                itemPreview2.setVisibility(View.INVISIBLE);
            }

            try{
                itemPreview3.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(itemPreview3, shopModel.getProductImages().get(2));
            } catch (IndexOutOfBoundsException e) {
                itemPreview3.setVisibility(View.INVISIBLE);
            }
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



}
