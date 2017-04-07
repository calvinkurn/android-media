package com.tokopedia.tkpd.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.facade.FacadePromo;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by erry on 16/01/17.
 */

public class FragmentBanner extends Fragment implements View.OnTouchListener {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.card_view)
    CardView cardView;
    private Unbinder unbind;
    private static final String TAG = FragmentBanner.class.getSimpleName();
    private static final String PROMO_ITEM = "PROMO_ITEM";
    private static final String BASE_URL = "www.tokopedia.com";
    private static final String BASE_MOBILE_URL = "m.tokopedia.com";
    private GetShopInfoRetrofit getShopInfoRetrofit;
    private FacadePromo.PromoItem promoItem;

    public static FragmentBanner newInstance(FacadePromo.PromoItem promoItem) {

        Bundle args = new Bundle();
        args.putParcelable(PROMO_ITEM, promoItem);
        FragmentBanner fragment = new FragmentBanner();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promoItem = getArguments().getParcelable(PROMO_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_slider, container, false);
        unbind = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageHandler.LoadImage(image, promoItem.imgUrl);
        int margin = (int) getResources().getDimension(R.dimen.slider_margin);
        setMargin(margin, margin);
        image.setOnTouchListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @OnClick(R.id.image)
    public void onClick() {
        String url = promoItem.promoUrl;
        try {
            UnifyTracking.eventSlideBannerClicked(url);
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            List<String> linkSegment = uri.getPathSegments();
            if (isBaseHost(host) && isShop(linkSegment)) {
                String shopDomain = linkSegment.get(0);
                getShopInfo(url, shopDomain);
            } else {
                openWebViewURL(url);
            }

        } catch (Exception e) {
            openWebViewURL(url);
        }
    }

    private boolean isBaseHost(String host) {
        return (host.contains(BASE_URL) || host.contains(BASE_MOBILE_URL));
    }

    private boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() == 1
                && !linkSegment.get(0).equals("pulsa")
                && !linkSegment.get(0).equals("iklan")
                && !linkSegment.get(0).equals("newemail.pl")
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("reset.pl")
                && !linkSegment.get(0).equals("activation.pl")
                && !linkSegment.get(0).equals("privacy.pl")
                && !linkSegment.get(0).equals("terms.pl")
                && !linkSegment.get(0).startsWith("invoice.pl"));
    }

    public void getShopInfo(final String url, final String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(getActivity(), "", shopDomain);
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
                        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    public void setMargin(int left, int right) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        params.setMargins(left, 0, right, 0);
        cardView.setLayoutParams(params);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            getActivity().sendBroadcast(new Intent(FragmentIndexCategory.BANNER_RECEIVER_INTENT));
        }
        return false;
    }
}
