package com.tokopedia.tkpd.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.customview.BannerView;
import com.tokopedia.tkpd.home.facade.FacadePromo;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by hangnadi on 7/24/17.
 */

public class BannerPagerAdapter extends RecyclerView.Adapter<BannerPagerAdapter.BannerViewHolder> {

    private static final String TAG = BannerPagerAdapter.class.getSimpleName();
    private List<BannerView.PromoItem> bannerList;
    private GetShopInfoRetrofit getShopInfoRetrofit;

    public BannerPagerAdapter(List<BannerView.PromoItem> promoList) {
        this.bannerList = promoList;
    }
    public void setBannerList(List<BannerView.PromoItem> bannerList) {
               this.bannerList = bannerList;
            }



    public class BannerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView bannerImage;
        CardView cardView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_slider_banner_category);
            bannerImage = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_slider_banner_category, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        if (bannerList.get(position).imgUrl!=null &&
                bannerList.get(position).promoUrl.length()>0) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            );
        }

        float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        int padding_8dp = (int) (4 * scale + 0.5f);
        holder.itemView.setPadding(padding_8dp, padding_8dp, padding_8dp, padding_8dp);

        int width320dp = (int) holder.itemView.getContext().getResources().getDimension(R.dimen.item_banner_width_category);
        int height160dp = (int) holder.itemView.getContext().getResources().getDimension(R.dimen.item_banner_height_category);

        ViewGroup.LayoutParams layoutParams = holder.bannerImage.getLayoutParams();
        layoutParams.height = height160dp;
        layoutParams.width = width320dp;
        holder.bannerImage.setLayoutParams(layoutParams);
        holder.bannerImage.requestLayout();

        if (bannerList.size() == 1) {
            holder.linearLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            holder.linearLayout.requestLayout();
            holder.linearLayout.setGravity(Gravity.CENTER);
        }

        ImageHandler.LoadImage(
                holder.bannerImage,
                bannerList.get(position).imgUrl
        );

    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    private View.OnClickListener getBannerImageOnClickListener(final int currentPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BannerView.PromoItem item = bannerList.get(currentPosition);

                trackingBannerClick(view.getContext(), item, currentPosition);

                if (view.getContext() != null
                        && view.getContext().getApplicationContext() instanceof IDigitalModuleRouter
                        && ((IDigitalModuleRouter) view.getContext().getApplicationContext()).isSupportedDelegateDeepLink(item.getPromoApplink())) {
                    ((IDigitalModuleRouter) view.getContext().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(view), item.getPromoApplink(), new Bundle());
                } else {

                    String url = item.getPromoUrl();
                    try {
                        UnifyTracking.eventSlideBannerClicked(url);
                        Uri uri = Uri.parse(url);
                        String host = uri.getHost();
                        List<String> linkSegment = uri.getPathSegments();
                        if (isBaseHost(host) && isShop(linkSegment)) {
                            String shopDomain = linkSegment.get(0);
                            getShopInfo(url, shopDomain, view.getContext());
                        } else if (isBaseHost(host) && isProduct(linkSegment)) {
                            String shopDomain = linkSegment.get(0);
                            openProductPageIfValid(url, shopDomain, view.getContext());
                        } else if (DeepLinkChecker.getDeepLinkType(url)==DeepLinkChecker.CATEGORY) {
                            DeepLinkChecker.openCategory(url, view.getContext());
                        } else {
                            openWebViewURL(url, view.getContext());
                        }

                    } catch (Exception e) {
                        openWebViewURL(url, view.getContext());
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void trackingBannerClick(Context context, BannerView.PromoItem item, int currentPosition) {
        Promotion promotion = new Promotion();
        promotion.setPromotionID(item.getPromoId());
        promotion.setPromotionName(item.getPromoTitle());
        promotion.setPromotionAlias(item.getPromoTitle());
        promotion.setPromotionPosition(currentPosition);

        PaymentTracking.eventPromoClick(promotion);
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

    public void openProductPageIfValid(final String url, final String shopDomain, final Context context) {
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
                        openWebViewURL(url, context);
                    }
                } catch (Exception e) {
                    openWebViewURL(url, context);
                }
            }

            @Override
            public void onError(String message) {
                openWebViewURL(url, context);
            }

            @Override
            public void onFailure() {
                openWebViewURL(url, context);
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void getShopInfo(final String url, final String shopDomain, final Context context) {
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
                        openWebViewURL(url, context);
                    }
                } catch (Exception e) {
                    openWebViewURL(url, context);
                }
            }

            @Override
            public void onError(String message) {
                openWebViewURL(url, context);
            }

            @Override
            public void onFailure() {
                openWebViewURL(url, context);
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}
