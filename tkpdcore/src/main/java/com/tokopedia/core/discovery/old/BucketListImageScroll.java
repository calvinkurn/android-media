/*
 * Created By Kulomady on 11/26/16 12:47 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 12:47 AM
 */

package com.tokopedia.core.discovery.old;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.TopAdsUtil;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.ContainerAds;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class BucketListImageScroll {

    protected ArrayList<String> Name;
    protected ArrayList<String> ImgUri;
    protected ArrayList<String> ID;
    protected ArrayList<String> IsGold;
    protected ArrayList<String> Key;
    protected ArrayList<String> Referrer;
    protected ArrayList<String> ShopLucky;
    protected ArrayList<String> ProductClickUrl;
    protected ArrayList<List<Badge>> badges;
    protected int pos;
    protected Context context;
    public LayoutInflater inflater;
    protected int VI = 2;
    protected String st = "product";
    protected boolean isInit = false;
    //private View convertView = null;
    private String adSrc = "";

    public BucketListImageScroll(Context context) {
        this.context = context;
    }

	public void setContain(HorizontalProductList horizontalProductList){
		Name = new ArrayList<>();
		ImgUri = new ArrayList<>();
		ID = new ArrayList<>();
		IsGold = new ArrayList<>();
		Key = new ArrayList<>();
		Referrer = new ArrayList<>();
		ShopLucky = new ArrayList<>();
		ProductClickUrl = new ArrayList<>();
		for (ProductItem productItem:
			horizontalProductList.getListProduct()) {
			Name.add(productItem.getName());
			ImgUri.add(productItem.getImgUri());
			ID.add(productItem.getId());
			IsGold.add(productItem.getIsGold());
			Key.add(productItem.getTopAds().getAdRefKey());// please parse this
			Referrer.add(productItem.getTopAds().getAdRefKey());
			ShopLucky.add(productItem.getLuckyShop());
			ProductClickUrl.add(productItem.getTopAds().getProductClickUrl());
		}
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Deprecated
	public void setContain(ContainerAds container){
		Name = container.Name;
		ImgUri = container.ImgUri;
		ID = container.ID;
		IsGold = container.IsGold;
		Key = container.Key;
        Referrer = container.Referer;
        ShopLucky = container.ShopLucky;
        ProductClickUrl = container.ProductClickUrl;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public void setAdapter(LinearLayout layout) {
        //CommonUtils.dumper(layout.getChildCount());
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }

        for (int i = 0; i < Name.size(); i++) {
            final int pos = i;
            View convertView = inflater.inflate(R.layout.single_gridview_bucket, null);
            ImageView ImgProd = (ImageView) convertView.findViewById(R.id.image_prod);
            ImageView TopAdsView = (ImageView) convertView.findViewById(R.id.topads);
            LinearLayout badgesContainer = (LinearLayout) convertView.findViewById(R.id.badges_container);

            ImageHandler.loadImageFit2(ImgProd.getContext(), ImgProd, ImgUri.get(pos));
            setBadges(badgesContainer, pos);

            ImgProd.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Intent intent = null;
                    if (VI == 2) {
                        TopAdsUtil.clickTopAdsAction(context, ProductClickUrl.get(pos));
                        intent = ProductDetailRouter
                                .createInstanceProductDetailInfoActivity(context, ID.get(pos));

                        bundle.putString("ad_key", Key.get(pos));
                        bundle.putString("ad_r", Referrer.get(pos));
						bundle.putString("src", adSrc);
						intent.putExtras(bundle);
					} else if (VI == 1) {
//						intent = new Intent(context, Catalog.class);
//						bundle.putString("ctg_id", ID.get(pos));
//						intent.putExtras(bundle);
                        intent = DetailProductRouter.getCatalogDetailActivity(context, ID.get(pos));
                    }
					//intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					context.startActivity(intent);
				}
				
			});
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) context).getWindowManager()
					.getDefaultDisplay()
					.getMetrics(displaymetrics);
			int width = displaymetrics.widthPixels;
			ImgProd.setLayoutParams(new RelativeLayout.LayoutParams(width/3,width/3));
			ImgProd.setPadding(5,5,5,5);
			layout.addView(convertView);
		}
	}
	
	public Boolean isInit() {
		if (Name.size() > 0) {
			return true;
		}
		 return false;
	}

    public void setAdSrc(String adSrc) {
        this.adSrc = adSrc;
    }

    /*private void setGoldShop(LinearLayout containerBadges, int position) {
        if (IsGold.size() > 0 && IsGold.get(position).equals("1")) {
            View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
            ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
            imageBadge.setImageResource(R.drawable.ic_shop_gold);
            containerBadges.addView(view);
        }
    }

    private void setLuckyShop(LinearLayout containerBadges, int position) {
        if (!ShopLucky.get(position).equals("")) {
            View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
            ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
            containerBadges.addView(view);
            LuckyShopImage.loadImage(imageBadge, ShopLucky.get(position));
        }
    }*/

    private void setBadges(LinearLayout containerBadges, int position) {
        if (badges!= null) {
            List<Badge> badgeList = badges.get(position);
            for (Badge badge : badgeList) {
                View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
                ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
                containerBadges.addView(view);
                LuckyShopImage.loadImage(imageBadge, badge.getImageUrl());
            }
        }
    }
}
