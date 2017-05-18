/*
 * Created By Kulomady on 11/26/16 1:07 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 1:07 AM
 */

package com.tokopedia.discovery.adapter.browseparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.TopAdsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.network.entity.discovery.ShopModel.SHOP_MODEL_TYPE;

/**
 * Created by Toped10 on 7/1/2016.
 */
public class BrowseShopAdapter extends ProductAdapter {


    PagingHandler.PagingHandlerModel pagingHandlerModel;
    int page = 1;

    public BrowseShopAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SHOP_ADAPTER", "viewType "+viewType);
        switch (viewType){
            case SHOP_MODEL_TYPE:
                return createViewShop(parent);
            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
                return createEmptySearch(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

//    public void addAll(boolean reload, List<RecyclerViewItem> datas) {
//        addAll(false, reload, datas);
//    }
//
//    public void addAll(boolean withClear, boolean reload, List<RecyclerViewItem> datas) {
//        int positionStart = getItemCount();
//        int itemCount = datas.size();
//        if (withClear) {
//            this.data.clear();
//            resetPaging();
//            positionStart = 0;
//        }
//        this.data.addAll(datas);
//        if (reload)
//            notifyItemRangeInserted(positionStart, itemCount);
//    }
//
//    public void addAll(List<RecyclerViewItem> datas){
//        this.data.addAll(datas);
//    }
//
//    public void resetPaging() {
//        page = 1;
//    }
//
//    public boolean checkHasNext() {
//        return pagingHandlerModel.getStartIndex() != -1;
//    }
//
//    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
//        return pagingHandlerModel;
//    }
//
//    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
//        this.pagingHandlerModel = pagingHandlerModel;
//    }
//
//    public int getPage() {
//        return page;
//    }
//
//    public void setPage(int page) {
//        this.page = page;
//    }
//
//    public void incrementPage() {
//        this.page++;
//    }
//
//    public boolean isEmptySearch(int position) {
//        if (checkDataSize(position))
//            return data.get(position).getType() == TkpdState.RecyclerView.VIEW_EMPTY_SEARCH;
//        return false;
//    }

//    public void setSearchNotFound() {
//        data.add(new EmptySearchItem());
//    }
//
//    public static class EmptySearchItem extends RecyclerViewItem {
//        public EmptySearchItem() {
//            setType(TkpdState.RecyclerView.VIEW_EMPTY_SEARCH);
//        }
//    }
//
//    public RecyclerView.ViewHolder createEmptySearch(ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty_state, parent, false);
//        return new TopAdsEmptyStateViewHolder(view);
//    }
//
//    public static class TopAdsEmptyStateViewHolder extends RecyclerView.ViewHolder implements
//            TopAdsItemClickListener {
//        @BindView(R2.id.topads)
//        TopAdsView topAdsView;
//        private Context context;
//
//        public TopAdsEmptyStateViewHolder(View itemView) {
//            super(itemView);
//            context = itemView.getContext();
//            ButterKnife.bind(this, itemView);
//            Config topAdsconfig = new Config.Builder()
//                    .setSessionId(GCMHandler.getRegistrationId(context))
//                    .setUserId(SessionHandler.getLoginID(context))
//                    .setEndpoint(Endpoint.SHOP)
//                    .withPreferedCategory()
//                    .build();
//            topAdsView.setConfig(topAdsconfig);
//            topAdsView.setAdsItemClickListener(this);
//        }
//
//        public void loadTopAds(){
//            topAdsView.loadTopAds();
//        }
//
//        @Override
//        public void onProductItemClicked(Product product) {
//            Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context,
//                    product.getId());
//            context.startActivity(intent);
//        }
//
//        @Override
//        public void onShopItemClicked(Shop shop) {
//            Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
//            Intent intent = new Intent(context, ShopInfoActivity.class);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
//        }
//
//        @Override
//        public void onAddFavorite(Data data) {
//
//        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case SHOP_MODEL_TYPE:
                ((ShopViewHolder)holder).bindData(context, getData().get(position), position);
                break;
//            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
//                ((TopAdsEmptyStateViewHolder) holder).loadTopAds();
//                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (checkDataSize(position)) {
//            RecyclerViewItem recyclerViewItem = data.get(position);
//            return isInType(recyclerViewItem);
//        } else {
//            return super.getItemViewType(position);
//        }
//    }

//    private boolean checkDataSize(int position) {
//        return data != null && data.size() > 0 && position > -1 && position < data.size();
//    }

    public static ShopViewHolder createViewShop(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_shop, parent, false);
        return new ShopViewHolder(itemLayoutView);
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        @BindView(R2.id.shop_1)
        LinearLayout mainContent;

        @BindView(R2.id.item_shop_image)
        SquareImageView itemShopImage;

        @BindView(R2.id.item_shop_gold)
        ImageView itemShopBadge;

        @BindView(R2.id.item_shop_lucky)
        ImageView itemShopLucky;

        @BindView(R2.id.item_shop_name)
        TextView itemShopName;

        @BindView(R2.id.item_shop_bought)
        TextView itemShopBought;

        @BindView(R2.id.item_shop_count_fav)
        TextView itemShopCountFav;

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
            itemShopBought.setText(shopModel.getTotalTransaction() + " " + context.getString(R.string.title_total_tx));
            itemShopCountFav.setText(shopModel.getNumberOfFavorite() + " " + context.getString(R.string.title_favorite));
            itemShopName.setText(shopModel.getShopName());
            if (shopModel.getIsGold() != null){
                LuckyShopImage.loadImage(itemShopLucky, shopModel.getLuckyImage());
            }
            if(shopModel.isOfficial() || shopModel.getIsGold().equals("1")){
                itemShopBadge.setVisibility(View.VISIBLE);
                if(shopModel.isOfficial()) {
                    itemShopBought.setVisibility(View.GONE);
                    itemShopBadge.setImageResource(R.drawable.ic_badge_official);
                } else if(shopModel.getIsGold().equals("1")){
                    itemShopBadge.setImageResource(R.drawable.ic_shop_gold);
                }
            } else {
                itemShopBought.setVisibility(View.VISIBLE);
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
        }
    }

    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()){
            case SHOP_MODEL_TYPE:
//            case TkpdState.RecyclerView.VIEW_EMPTY_SEARCH:
//                return recyclerViewItem.getType();
            default:
                return -1;
        }
    }



}
