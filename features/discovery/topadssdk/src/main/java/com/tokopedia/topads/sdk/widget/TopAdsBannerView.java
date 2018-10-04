package com.tokopedia.topads.sdk.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.BannerAdsContract;
import com.tokopedia.topads.sdk.view.adapter.BannerAdsAdapter;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class TopAdsBannerView extends LinearLayout implements BannerAdsContract.View {

    private static final String TAG = TopAdsBannerView.class.getSimpleName();
    private BannerAdsPresenter presenter;
    private TopAdsListener adsListener;
    private TopAdsBannerClickListener topAdsBannerClickListener;
    private ImageLoader imageLoader;
    private BannerAdsAdapter bannerAdsAdapter;

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

    private void createViewCpmShop(Context context, final Cpm cpm, String appLink, String adsClickUrl) {
        if (activityIsFinishing(context))
            return;

        final ArrayList<ImageView> indicatorItems = new ArrayList<>();
        inflate(getContext(), R.layout.layout_ads_banner_shop_pager, this);
        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayout indicatorContainer = findViewById(R.id.indicator);
        bannerAdsAdapter = new BannerAdsAdapter(new BannerAdsAdapterTypeFactory(topAdsBannerClickListener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(bannerAdsAdapter);
        recyclerView.setOnFlingListener(null);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        if (cpm != null && cpm.getCpmShop() != null) {
            ArrayList<Item> items = new ArrayList<>();
            items.add(new BannerShopViewModel(cpm, appLink, adsClickUrl));
            if (cpm.getCpmShop().getProducts().size() > 1) {
                items.add(new BannerShopProductViewModel(cpm, appLink, adsClickUrl));
                for (int i = 0; i < 2; i++) {
                    ImageView pointView = new ImageView(getContext());
                    pointView.setPadding(10, 0, 10, 5);
                    if (i == 0) {
                        pointView.setImageResource(R.drawable.dot_green);
                    } else {
                        pointView.setImageResource(R.drawable.dot_grey);
                    }
                    indicatorItems.add(pointView);
                    indicatorContainer.addView(pointView);
                }
            }
            bannerAdsAdapter.setList(items);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentPosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                for (int i = 0; i < indicatorItems.size(); i++) {
                    if (currentPosition != i) {
                        indicatorItems.get(i).setImageResource(R.drawable.dot_grey);
                    } else {
                        indicatorItems.get(i).setImageResource(R.drawable.dot_green);
                    }
                }
            }
        });
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

    private void createViewCpmDigital(Context context, final Cpm cpm) {
        if (activityIsFinishing(context))
            return;
        inflate(getContext(), R.layout.layout_ads_banner_digital, this);
        final ImageView iconImg = (ImageView) findViewById(R.id.image);
        TextView nameTxt = (TextView) findViewById(R.id.name);
        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        TextView ctaTxt = (TextView) findViewById(R.id.cta_btn);
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
        presenter.setConfig(config);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setTopAdsBannerClickListener(TopAdsBannerClickListener topAdsBannerClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
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
                    createViewCpmShop(getContext(), data.getCpm(), data.getApplinks(), data.getAdClickUrl());
                } else if (data.getCpm().getTemplateId() == 4) {
                    createViewCpmDigital(getContext(), data.getCpm());
                }
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (topAdsBannerClickListener != null) {
                            topAdsBannerClickListener.onBannerAdsClicked(data.getApplinks());
                            new ImpresionTask().execute(data.getAdClickUrl());
                        }
                    }
                });
            }
        }
        if (adsListener != null) {
            adsListener.onTopAdsLoaded();
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
        presenter.loadTopAds();
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void init() {
        imageLoader = new ImageLoader(getContext());
        presenter = new BannerAdsPresenter(getContext());
        presenter.attachView(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }
}
