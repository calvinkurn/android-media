package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseCustomView;
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
    private LinearLayout layoutDeposit;
    private ImageView imageShop;
    private ImageView badge;
    private TextView textShopName;
    private ImageView shopReputation;
    private TextView textSaldoAmount;
    private WarningTickerView warningTickerView;
    private TextView shopStatus;

    // topads widget
    private ImageView topadsIcon;
    private TextView topadsSaldoTextView;
    private TextView topadsSaldoAmountTextView;
    private TextView topadsClaimTextView;
    private TextView topadsClaimDescrTextView;
    private TextView topadsCreditDescrTextView;

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
        layoutDeposit = view.findViewById(R.id.layout_deposit);
        imageShop = view.findViewById(R.id.image_shop);
        badge = view.findViewById(R.id.image_badge);
        textShopName = view.findViewById(R.id.text_shop_name);
        shopReputation = view.findViewById(R.id.shop_reputation);
        textSaldoAmount = view.findViewById(R.id.text_saldo_amount);
        warningTickerView = view.findViewById(R.id.verification_warning_ticker);
        shopStatus = view.findViewById(R.id.text_shop_verification_status);

        topadsIcon = view.findViewById(R.id.image_topads);
        topadsSaldoTextView = view.findViewById(R.id.text_topads_credit);
        topadsSaldoAmountTextView = view.findViewById(R.id.text_topads_saldo);
        topadsClaimTextView = view.findViewById(R.id.text_topads_claim);
        topadsClaimDescrTextView = view.findViewById(R.id.text_topads_claim_descr);
        topadsCreditDescrTextView = view.findViewById(R.id.text_topads_credit_descr);
        iniGoneTopads();
    }

    private void iniGoneTopads() {
        getRootView().findViewById(R.id.separator_2).setVisibility(GONE);
        topadsIcon.setVisibility(GONE);
        topadsSaldoTextView.setVisibility(GONE);
        getRootView().findViewById(R.id.topads_right_content).setVisibility(GONE);
        topadsCreditDescrTextView.setVisibility(GONE);
        topadsClaimDescrTextView.setVisibility(GONE);
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
            badge.setImageResource(resource);
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }

    }

    public void setShopName(String shopName) {
        textShopName.setText(MethodChecker.fromHtml(shopName));
    }

    public void setBalance(String balance) {
        textSaldoAmount.setText(balance);
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

    public void setOnClickDeposit(View.OnClickListener listener) {
        layoutDeposit.setOnClickListener(listener);
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
        } else {
            shopStatus.setVisibility(View.VISIBLE);
            shopStatus.setText(verificationStatusName);
        }
    }

    public void setTopAdsDeposit(DataDeposit dataDeposit) {
        if (dataDeposit == null)
            return;

        getRootView().findViewById(R.id.separator_2).setVisibility(VISIBLE);
        topadsIcon.setVisibility(VISIBLE);
        topadsSaldoTextView.setVisibility(VISIBLE);
        getRootView().findViewById(R.id.topads_right_content).setVisibility(VISIBLE);
        FreeDeposit freeDeposit = dataDeposit.getFreeDeposit();
        if (freeDeposit.getStatus() == 1 && freeDeposit.getNominal() > 0){
            topadsSaldoAmountTextView.setVisibility(GONE);
            topadsClaimTextView.setVisibility(VISIBLE);
            topadsClaimTextView.setOnClickListener(v -> {
                if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
                    openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, TopAdsCommonConstant.TOPADS_FREE_CLAIM_URL));
                }
            });
            topadsSaldoTextView.setText(getContext().getString(R.string.top_ads_credit_label, freeDeposit.getNominalFmt()));
            topadsClaimDescrTextView.setVisibility(VISIBLE);
            topadsClaimDescrTextView.setText(getContext().getString(R.string.claim_expired, freeDeposit.getRemainingDays()));
            topadsCreditDescrTextView.setVisibility(GONE);
        } else {
            topadsSaldoAmountTextView.setVisibility(VISIBLE);
            topadsClaimTextView.setVisibility(GONE);
            topadsSaldoTextView.setText(getContext().getString(R.string.top_ads_credit_label, ""));
            topadsSaldoAmountTextView.setText(dataDeposit.getAmountFmt());
            if (freeDeposit.getStatus() == 2 && freeDeposit.getUsage() > 0){
                topadsCreditDescrTextView.setVisibility(VISIBLE);
                topadsCreditDescrTextView.setText(getContext().getString(R.string.bonus_expired, freeDeposit.getUsageFmt(),
                        freeDeposit.getRemainingDays()));
            } else {
                topadsCreditDescrTextView.setVisibility(GONE);
            }
            topadsClaimDescrTextView.setVisibility(GONE);
        }
    }

    private void openApplink(String applinkUrl) {
        if (getContext() != null && !TextUtils.isEmpty(applinkUrl)) {
            RouteManager.route(getContext(), applinkUrl);
        }
    }
}