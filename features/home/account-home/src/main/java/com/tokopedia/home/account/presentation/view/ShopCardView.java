package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.widget.WarningTickerView;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.data.model.FreeDeposit;
import com.tokopedia.user_identification_common.KycWidgetUtil;

/**
 * @author okasurya on 7/26/18.
 */
public class ShopCardView extends BaseCustomView {
    private ImageView imageShop;
    private ImageView badge;
    private TextView textShopName;
    private ImageView shopReputation;
    private WarningTickerView warningTickerView;
    private TextView shopStatus;
    private View infoButton;
    private LabelView labelViewTopAds;

    public ShopCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShopCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShopCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_shop_card, this);
        imageShop = view.findViewById(R.id.image_shop);
        badge = view.findViewById(R.id.image_badge);
        textShopName = view.findViewById(R.id.text_shop_name);
        shopReputation = view.findViewById(R.id.shop_reputation);
        warningTickerView = view.findViewById(R.id.verification_warning_ticker);
        shopStatus = view.findViewById(R.id.text_shop_verification_status);
        infoButton = view.findViewById(R.id.shop_status_info_button);
        labelViewTopAds = view.findViewById(R.id.label_view_topads);
        labelViewTopAds.setVisibility(GONE);
        view.findViewById(R.id.separator).setVisibility(GONE);
    }

    public void setShopImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            ImageHandler.loadImageCircle2(getContext(), imageShop, url);
        }
    }

    public void setBadgeImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            ImageHandler.loadImageCircle2(getContext(), badge, url);
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }
    }

    public void setBadgeImage(@DrawableRes int resource) {
        if (resource != 0) {
            badge.setImageDrawable(ContextCompat.getDrawable(getContext(), resource));
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }

    }

    public void setShopName(String shopName) {
        textShopName.setText(MethodChecker.fromHtml(shopName));
    }

    public void setShopReputation(String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(getContext())
                    .load(url)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(shopReputation);
            shopReputation.setVisibility(VISIBLE);
        } else {
            shopReputation.setVisibility(GONE);
        }
    }

    public void setOnClickShopAvatar(View.OnClickListener listener) {
        imageShop.setOnClickListener(listener);
    }

    public void setOnClickShopName(View.OnClickListener listener) {
        textShopName.setOnClickListener(listener);
    }

    public void setOnClickTopAdsView(View.OnClickListener listener) {
        labelViewTopAds.setOnClickListener(listener);
    }

    public void setOnClickShopStatusInfo(View.OnClickListener listener) {
        infoButton.setOnClickListener(listener);
    }

    public void setKyc(int verificationStatus, String verificationStatusName,
                       WarningTickerView.LinkClickListener listener) {

        setShopStatus(verificationStatusName);

        warningTickerView.setDescriptionWithLink(
                KycWidgetUtil.getDescription(getContext(), verificationStatus),
                KycWidgetUtil.getHighlight(getContext(), verificationStatus),
                listener);

        if (TextUtils.isEmpty(KycWidgetUtil.getDescription(getContext(), verificationStatus))) {
            warningTickerView.setVisibility(View.GONE);
        } else {
            warningTickerView.setVisibility(View.VISIBLE);
        }
    }

    private void setShopStatus(String verificationStatusName) {
        if (TextUtils.isEmpty(verificationStatusName)) {
            shopStatus.setVisibility(View.GONE);
            infoButton.setVisibility(View.GONE);
        } else {
            shopStatus.setVisibility(View.VISIBLE);
            infoButton.setVisibility(VISIBLE);
            shopStatus.setText(verificationStatusName);
        }
    }

    public void setTopAdsDeposit(DataDeposit dataDeposit) {
        if (dataDeposit == null)
            return;

        FreeDeposit freeDeposit = dataDeposit.getFreeDeposit();
        if (freeDeposit.getStatus() == 1 && freeDeposit.getNominal() > 0) {
            labelViewTopAds.setContentClick(v -> {
                if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
                    openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, TopAdsCommonConstant.TOPADS_FREE_CLAIM_URL));
                }
            });
            SpannableString claimKredits = new SpannableString(getContext().getString(R.string.top_ads_free_claim_label, freeDeposit.getNominalFmt()));
            claimKredits.setSpan(new StyleSpan(Typeface.BOLD), claimKredits.length() - freeDeposit.getNominalFmt().length(), claimKredits.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            labelViewTopAds.setTitle(claimKredits);
            labelViewTopAds.setContent(getContext().getString(R.string.topads_claim_label));
            labelViewTopAds.setContentColorValue(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            String nDays = getContext().getString(R.string.template_claim_n_days, freeDeposit.getRemainingDays());
            SpannableString subtitle = new SpannableString(getContext().getString(R.string.claim_expired) + " " + nDays);
            subtitle.setSpan(new StyleSpan(Typeface.BOLD), getContext().getString(R.string.claim_expired).length() + 1,
                    subtitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            labelViewTopAds.setSubTitle(subtitle);
        } else {
            labelViewTopAds.setTitle(getContext().getString(R.string.top_ads_credit_label));
            labelViewTopAds.setContent(dataDeposit.getAmountFmt());
            labelViewTopAds.setContentColorValue(ContextCompat.getColor(getContext(), R.color.light_primary));
            labelViewTopAds.setContentClick(null);
            if (freeDeposit.getStatus() == 2 && freeDeposit.getUsage() > 0) {
                String bonusTopAds = getContext().getString(R.string.bonus_expired, freeDeposit.getUsageFmt());
                String nDays = getContext().getString(R.string.template_claim_n_days, freeDeposit.getRemainingDays());
                SpannableString subtitle = new SpannableString(bonusTopAds + " berlaku " + nDays);
                subtitle.setSpan(new StyleSpan(Typeface.BOLD), bonusTopAds.length() - freeDeposit.getUsageFmt().length(),
                        bonusTopAds.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                subtitle.setSpan(new StyleSpan(Typeface.BOLD), subtitle.length() - nDays.length(),
                        subtitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                labelViewTopAds.setSubTitle(subtitle);
            } else {
                labelViewTopAds.setSubTitle("");
            }
        }
        if (getRootView() != null) {
            getRootView().findViewById(R.id.separator).setVisibility(VISIBLE);
        }
        labelViewTopAds.setVisibility(VISIBLE);
    }

    private void openApplink(String applinkUrl) {
        if (getContext() != null && !TextUtils.isEmpty(applinkUrl)) {
            RouteManager.route(getContext(), applinkUrl);
        }
    }
}
