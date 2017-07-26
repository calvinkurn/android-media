package com.tokopedia.tkpd.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.facade.FacadePromo;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by hangnadi on 7/24/17.
 */

public class BannerPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<FacadePromo.PromoItem> bannerList;
    private GetShopInfoRetrofit getShopInfoRetrofit;

    public BannerPagerAdapter(Context context, List<FacadePromo.PromoItem> promoList) {
        this.context = context;
        this.bannerList = promoList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_slider_banner_category, container, false);

        ImageView bannerImage = (ImageView) view.findViewById(R.id.image);
        if (bannerList.get(position).imgUrl!=null &&
                bannerList.get(position).promoUrl.length()>0) {
            bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(bannerList.get(position).promoUrl)
            );
        }

        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        int margin = (int) context.getResources().getDimension(R.dimen.slider_margin);
        params.setMargins(margin, 0, margin, 0);
        cardView.setLayoutParams(params);

        ImageHandler.LoadImage(
                bannerImage,
                bannerList.get(position).imgUrl
        );
        container.addView(view);
        return view;
    }

    private View.OnClickListener getBannerImageOnClickListener(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UnifyTracking.eventSlideBannerClicked(url);
                    Uri uri = Uri.parse(url);
                    String host = uri.getHost();
                    List<String> linkSegment = uri.getPathSegments();
                    if (isBaseHost(host) && isShop(linkSegment)) {
                        String shopDomain = linkSegment.get(0);
                        getShopInfo(url, shopDomain);
                    } else if (isBaseHost(host) && isProduct(linkSegment)) {
                        String shopDomain = linkSegment.get(0);
                        openProductPageIfValid(url, shopDomain);
                    } else if (DeepLinkChecker.getDeepLinkType(url)==DeepLinkChecker.CATEGORY) {
                        DeepLinkChecker.openCategory(url, context);
                    } else {
                        openWebViewURL(url);
                    }

                } catch (Exception e) {
                    openWebViewURL(url);
                }
            }
        };
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null && object instanceof View) container.removeView((View) object);
    }

    private boolean isBaseHost(String host) {
        return (host.contains(TkpdBaseURL.BASE_DOMAIN) || host.contains(TkpdBaseURL.MOBILE_DOMAIN));
    }

    private boolean isShop(List<String> linkSegment) {
        return linkSegment.size() == 1
                && !isReservedLink(linkSegment.get(0));
    }

    private boolean isProduct(List<String> linkSegment) {
        return linkSegment.size() == 2
                && !isReservedLink(linkSegment.get(0));
    }

    private boolean isReservedLink(String link) {
        return link.equals("pulsa")
                || link.equals("iklan")
                || link.equals("newemail.pl")
                || link.equals("search")
                || link.equals("hot")
                || link.equals("about")
                || link.equals("reset.pl")
                || link.equals("activation.pl")
                || link.equals("privacy.pl")
                || link.equals("terms.pl")
                || link.equals("p")
                || link.equals("catalog")
                || link.equals("toppicks")
                || link.equals("promo")
                || link.startsWith("invoice.pl");
    }

    public void openProductPageIfValid(final String url, final String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    ShopModel shopModel = new Gson().fromJson(result,
                            ShopModel.class);
                    if (shopModel.info != null) {
                        DeepLinkChecker.openProduct(url, context);
                    } else {
                        openWebViewURL(url);
                    }
                } catch (Exception e) {
                    openWebViewURL(url);
                }
            }

            @Override
            public void onError(String message) {
                openWebViewURL(url);
            }

            @Override
            public void onFailure() {
                openWebViewURL(url);
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void getShopInfo(final String url, final String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    ShopModel shopModel = new Gson().fromJson(result,
                            ShopModel.class);
                    if (shopModel.info != null) {
                        JSONObject shop = new JSONObject(result);
                        JSONObject shopInfo = new JSONObject(shop.getString("info"));
                        Bundle bundle = ShopInfoActivity.createBundle(
                                shopInfo.getString("shop_id"), shopInfo.getString("shop_domain"));
                        Intent intent = new Intent(context, ShopInfoActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        openWebViewURL(url);
                    }
                } catch (Exception e) {
                    openWebViewURL(url);
                }
            }

            @Override
            public void onError(String message) {
                openWebViewURL(url);
            }

            @Override
            public void onFailure() {
                openWebViewURL(url);
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void openWebViewURL(String url) {
        if (url != "") {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}
