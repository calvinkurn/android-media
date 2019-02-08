package com.tokopedia.topads.sdk.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.BannerAdsContract;
import com.tokopedia.topads.sdk.view.adapter.BannerAdsAdapter;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class TopAdsBannerView extends LinearLayout implements BannerAdsContract.View {

    private static final String TAG = TopAdsBannerView.class.getSimpleName();
    private TopAdsListener adsListener;
    private TopAdsBannerClickListener topAdsBannerClickListener;
    private TopAdsItemImpressionListener impressionListener;
    private BannerAdsAdapter bannerAdsAdapter;
    private final int NO_TEMPLATE = 0;
    private final int SHOP_TEMPLATE = 1;
    private final int DIGITAL_TEMPLATE = 2;

    @Inject
    BannerAdsPresenter bannerPresenter;
    private RecyclerView recyclerView;
    private TextView promotedTxt;
    private TextView shopName;
    private LinearLayout badgeContainer;
    private ImageView iconImg;
    private TextView nameTxt;
    private TextView descriptionTxt;
    private TextView ctaTxt;
    private int template = NO_TEMPLATE;

    public TopAdsBannerView(Context context) {
        super(context);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static String escapeHTML(String s) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(StringEscapeUtils.unescapeHtml4(s), Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                return Html.fromHtml(StringEscapeUtils.unescapeHtml4(s)).toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void renderViewCpmShop(Context context, final CpmData cpmData, String appLink, String adsClickUrl) {
        if (activityIsFinishing(context))
            return;
        if (template == NO_TEMPLATE) {
            inflate(getContext(), R.layout.layout_ads_banner_shop_pager, this);
            recyclerView = findViewById(R.id.list);
            promotedTxt = (TextView) findViewById(R.id.title_promote);
            shopName = (TextView) findViewById(R.id.shop_name);
            badgeContainer = (LinearLayout) findViewById(R.id.badges_container);
            promotedTxt.setText(cpmData.getCpm().getPromotedText());
            shopName.setText(TopAdsBannerView.escapeHTML(cpmData.getCpm().getName()));
            bannerAdsAdapter = new BannerAdsAdapter(new BannerAdsAdapterTypeFactory(topAdsBannerClickListener, impressionListener));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(bannerAdsAdapter);
            final int mItemOffset = getResources().getDimensionPixelOffset(R.dimen.dp_8);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.left = mItemOffset;
                    }
                    outRect.right = mItemOffset;
                }
            });
            template = SHOP_TEMPLATE;
        }
        setHeadlineShopData(cpmData, appLink, adsClickUrl);
    }

    private void setHeadlineShopData(CpmData cpmData, String appLink, String adsClickUrl) {
        if (cpmData != null && cpmData.getCpm().getCpmShop() != null) {
            if (cpmData.getCpm().getBadges().size() > 0) {
                badgeContainer.removeAllViews();
                badgeContainer.setVisibility(View.VISIBLE);
                for (Badge badge : cpmData.getCpm().getBadges()) {
                    ImageView badgeImg = new ImageView(getContext());
                    badgeImg.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.badge_size_small),
                            getResources().getDimensionPixelSize(R.dimen.badge_size_small)));
                    if (!activityIsFinishing(getContext())) {
                        Glide.with(getContext().getApplicationContext()).load(badge.getImageUrl()).into(badgeImg);
                    }
                    badgeContainer.addView(badgeImg);
                }
            } else {
                badgeContainer.setVisibility(View.GONE);
            }
            ArrayList<Item> items = new ArrayList<>();
            items.add(new BannerShopViewModel(cpmData, appLink, adsClickUrl));
            for (int i = 0; i < cpmData.getCpm().getCpmShop().getProducts().size(); i++) {
                items.add(new BannerShopProductViewModel(cpmData, cpmData.getCpm().getCpmShop().getProducts().get(i),
                        appLink, adsClickUrl));
            }
            bannerAdsAdapter.setList(items);
        }
    }

    private boolean activityIsFinishing(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity.isFinishing();
        }
        return false;
    }

    public static void setTextColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new TypefaceSpan("sans-serif"), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void renderViewCpmDigital(Context context, final Cpm cpm) {
        if (activityIsFinishing(context))
            return;
        if (template == NO_TEMPLATE) {
            inflate(getContext(), R.layout.layout_ads_banner_digital, this);
            iconImg = (ImageView) findViewById(R.id.image);
            nameTxt = (TextView) findViewById(R.id.name);
            descriptionTxt = (TextView) findViewById(R.id.description);
            ctaTxt = (TextView) findViewById(R.id.cta_btn);
            template = DIGITAL_TEMPLATE;
        }
        setHeadlineDigitalData(context, cpm);
    }

    private void setHeadlineDigitalData(Context context, Cpm cpm) {
        Glide.with(context).load(cpm.getCpmImage().getFullEcs()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iconImg.setImageBitmap(resource);
                new ImpresionTask().execute(cpm.getCpmImage().getFullUrl());
            }
        });
        nameTxt.setText(escapeHTML(cpm.getName()));
        descriptionTxt.setText(escapeHTML(cpm.getDecription()));
        ctaTxt.setText(cpm.getCta());
    }

    public void setConfig(Config config) {
        bannerPresenter.setConfig(config);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setTopAdsBannerClickListener(TopAdsBannerClickListener topAdsBannerClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    public void setTopAdsImpressionListener(TopAdsItemImpressionListener adsImpressionListener) {
        this.impressionListener = adsImpressionListener;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void displayAds(CpmModel cpmModel) {
        if (cpmModel != null && cpmModel.getData().size() > 0) {
            final CpmData data = cpmModel.getData().get(0);
            if (data != null && data.getCpm() != null) {
                if (data.getCpm().getCpmShop() != null && isResponseValid(data)) {
                    renderViewCpmShop(getContext(), data, data.getApplinks(), data.getAdClickUrl());
                } else if (data.getCpm().getTemplateId() == 4) {
                    renderViewCpmDigital(getContext(), data.getCpm());
                    setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (topAdsBannerClickListener != null) {
                                topAdsBannerClickListener.onBannerAdsClicked(0, data.getApplinks(), data);
                                new ImpresionTask().execute(data.getAdClickUrl());
                            }
                        }
                    });
                }
            }
        }
        if (adsListener != null) {
            adsListener.onTopAdsLoaded(null);
        }
    }

    private boolean isResponseValid(CpmData data) {
        return !data.getCpm().getCta().isEmpty()
                && !data.getCpm().getPromotedText().isEmpty();
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void loadTopAds() {
        bannerPresenter.loadTopAds();
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void init() {
        BaseMainApplication application = ((BaseMainApplication) getContext().getApplicationContext());
        TopAdsComponent component = DaggerTopAdsComponent.builder()
                .baseAppComponent(application.getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(bannerPresenter);
        bannerPresenter.attachView(this);
    }

}
