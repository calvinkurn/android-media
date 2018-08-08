package com.tokopedia.core.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.core.network.v4.NetworkHandler;
import com.tokopedia.core.var.ContainerAds;
import com.tokopedia.core.var.TkpdState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TopAdsUtil {
	
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int SINGLE = 2;


	/**
	 * 
	 * @param Result = JSONObject result
	 * @param stateView = state view browse product (grid, bucket, list)
	 * @param index = index ads, 0 for top 1 for bottom
	 * @return
	 * @throws JSONException 
	 */
	public static ContainerAds parseAds(JSONObject Result, int stateView, int index) throws JSONException {
		ContainerAds container = new ContainerAds();
		if (!Result.isNull("ad_product")) {
			int loop = 0;
			int start = 0;
			switch (stateView) {
			case TkpdState.StateView.GRID_3:
				loop = 3;
				if (index == TOP) start = 0;
				else start = 3;
				break;
			case TkpdState.StateView.GRID_2:
				loop = 2;
				if (index == TOP) start = 0;
				else start = 2;
				break;
			case TkpdState.StateView.LIST:
				loop = 1;
				if (index == TOP) start = 0;
				else start = 1;
				break;
			}
			JSONArray AdsCollection = new JSONArray(Result.getString("ad_product"));
			int k = start;
			do {
				int i = k;
				int l = 0;
				while (i < AdsCollection.length() && l < loop) {
					JSONObject AdsDetail = new JSONObject(AdsCollection.getString(i));
					container.ID.add(AdsDetail.getString("p_id"));
					container.Name.add(AdsDetail.getString("p_name_enc"));
					container.ImgUri.add(AdsDetail.getString("p_img"));
					container.Shop.add(AdsDetail.getString("s_name_enc"));
					container.ShopID.add(AdsDetail.optString("s_shop_id", ""));
					container.isOwner.add(AdsDetail.optString("s_is_owner", "0"));
					container.Var1.add(AdsDetail.getString("p_price_fmt"));
					container.IsGold.add(AdsDetail.getString("s_gold_shop"));
                    container.ShopLucky.add(AdsDetail.getString("s_lucky_shop"));
					container.CountReview.add(AdsDetail.getString("p_cnt_review_fmt"));
                    container.CountTalk.add(AdsDetail.getString("p_cnt_talk_fmt"));
					Uri uri=Uri.parse(AdsDetail.getString("s_uri_ad"));
					container.Key.add(AdsDetail.getString("ad_key"));
                    container.Referer.add(AdsDetail.getString("ad_r"));
					i++;
					l++;
				}
				k = k + (loop*2);
			}while (k < AdsCollection.length());
		}
		CommonUtils.dumper("name: " + container.Name + " state view: " + stateView + " index: " + index);
	return container;	
	}

	public static ContainerAds parseAds(TopAdsResponse Result, int stateView, int index) throws JSONException {
		ContainerAds container = new ContainerAds();
		if (Result!=null && Result.data.size()>0) {
			int loop = 0;
			int start = 0;
			switch (stateView) {
			case TkpdState.StateView.GRID_3:
				loop = 4;
				if (index == TOP) start = 0;
				else start = 4;
				break;
			case TkpdState.StateView.GRID_2:
				loop = 2;
				if (index == TOP) start = 0;
				else start = 2;
				break;
			case TkpdState.StateView.LIST:
				loop = 1;
				if (index == TOP) start = 0;
				else start = 1;
				break;
			}
			List<TopAdsResponse.Data> adsCollection = Result.data;
			int k = start;
			do {
				int i = k;
				int l = 0;
				while (i < adsCollection.size() && l < loop) {
					TopAdsResponse.Data adsDetail = adsCollection.get(i);
					container.ID.add(adsDetail.product.id);
					container.Name.add(adsDetail.product.name);
					container.ImgUri.add(adsDetail.product.image.mUrl);
					container.Shop.add(adsDetail.shop.name);
					container.ShopID.add(adsDetail.shop.id);
					String isOwner = "1";
					if(adsDetail.shop.isOwner) isOwner = "0";
					container.isOwner.add(isOwner);
					container.Var1.add(adsDetail.product.priceFormat);
					String isGold = "1";
					if(adsDetail.shop.goldShop) isGold = "0";
					container.IsGold.add(isGold);
                    container.ShopLucky.add(adsDetail.shop.luckyShop);
					container.CountReview.add(adsDetail.product.countReviewFormat);
                    container.CountTalk.add(adsDetail.product.countTalkFormat);
					Uri uri=Uri.parse(adsDetail.product.uri);
					container.Key.add(adsDetail.adRefKey);
                    container.Referer.add(adsDetail.adRefKey);
					i++;
					l++;
				}
				k = k + (loop*2);
			}while (k < adsCollection.size());
		}
		CommonUtils.dumper("name: " + container.Name + " state view: " + stateView + " index: " + index);
		return container;
	}

	public static ContainerAds parseAdsNew(TopAdsResponse Result, int stateView, int index) throws JSONException {
		ContainerAds container = new ContainerAds();
		if (Result!=null && Result.data.size()>0) {
			List<TopAdsResponse.Data> adsCollection = Result.data;
			int start;
			int end;
			int max;
			if(index == TopAdsUtil.TOP ||index == TopAdsUtil.BOTTOM ) {
				start = index * 2;
				end = adsCollection.size();
				max = 2;
			}
			else {
				start = 0;
				end = adsCollection.size();
				max =end;
			}
			int increment =0 ;
			for(int i=start; i<end && increment<max ;i++) {
				TopAdsResponse.Data adsDetail = adsCollection.get(i);
				container.ID.add(adsDetail.product.id);
				container.Name.add(adsDetail.product.name);
				container.ImgUri.add(adsDetail.product.image.mUrl);
				container.Shop.add(adsDetail.shop.name);
				container.ShopID.add(adsDetail.shop.id);
				String isOwner = "1";
				isOwner = adsDetail.shop.isOwner ? "1" : "0";
				container.isOwner.add(isOwner);
				container.Var1.add(adsDetail.product.priceFormat);
				String isGold = "1";
				isGold = adsDetail.shop.goldShop ? "1" : "0";
				container.IsGold.add(isGold);
				container.ShopLucky.add(adsDetail.shop.luckyShop);
				container.CountReview.add(adsDetail.product.countReviewFormat);
				container.CountTalk.add(adsDetail.product.countTalkFormat);
				Uri uri = Uri.parse(adsDetail.product.uri);
				container.Key.add(adsDetail.adRefKey);
				container.Referer.add(adsDetail.adRefKey);
                container.ProductClickUrl.add(adsDetail.productClickUrl);
				container.badges.add(adsDetail.shop.badges);
				increment++;
			}
		}
		return container;
	}

	public static int getInitPosition(int page, int stateView) {
		int totalList = 0;
		switch (stateView) {
		case 0:
			totalList = 4;
			break;
		case 1:
			totalList = 6;
			break;
		case 2:
			totalList = 12;
			break;
		}
		
		if (page == 0 || page == 1) {
			return 0;
		} else {
			return totalList * (page - 1);
		}

	}

	public static int getInitialPosition(int page) {
		return (page-1)*4;
	}

    public static void clickTopAdsAction(Context context, String ClickUrl) {
        Log.i("TOP ADS CLICK", "Action click!!: "+ClickUrl);
        NetworkHandler networkHandler = new NetworkHandler(context, ClickUrl);
        networkHandler.setMethod(NetworkHandler.METHOD_GET);
        networkHandler.commit();
    }
}
