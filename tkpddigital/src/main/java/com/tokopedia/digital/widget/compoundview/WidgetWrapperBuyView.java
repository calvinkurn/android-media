package com.tokopedia.digital.widget.compoundview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nabillasabbaha on 7/18/17.
 */

public class WidgetWrapperBuyView extends LinearLayout {

    public static final String ARG_UTM_SOURCE = "ARG_UTM_SOURCE";
    public static final String ARG_UTM_MEDIUM = "ARG_UTM_MEDIUM";
    public static final String ARG_UTM_CAMPAIGN = "ARG_UTM_CAMPAIGN";
    public static final String ARG_UTM_CONTENT = "ARG_UTM_CONTENT";
    private static final String ARG_UTM_SOURCE_VALUE = "android";
    private static final String ARG_UTM_MEDIUM_VALUE = "widget";

    @BindView(R2.id.buy_with_credit_checkbox)
    CheckBox creditCheckbox;
    @BindView(R2.id.btn_buy)
    Button buyButton;

    private Category category;
    private OnBuyButtonListener listener;
    private String selectedOperatorName = "";

    public WidgetWrapperBuyView(Context context) {
        super(context);
        init();
    }

    public WidgetWrapperBuyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetWrapperBuyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnBuyButtonListener listener) {
        this.listener = listener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_wrapper_buy, this);
        ButterKnife.bind(this);
        creditCheckbox.setOnCheckedChangeListener(checkedChangeListener());
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buyButton.setText(
                        isChecked ? getResources().getString(R.string.title_button_pay)
                                : getResources().getString(R.string.title_buy));

                listener.trackingCheckInstantSaldo(isChecked);
            }
        };
    }

    public void setSelectedOperatorName(String selectedOperatorName) {
        this.selectedOperatorName = selectedOperatorName;
    }

    public boolean isCreditCheckboxChecked() {
        return creditCheckbox.isChecked();
    }

    public void setCategory(Category category) {
        this.category = category;
        setVisibilityCheckbox();
    }

    private void setVisibilityCheckbox() {
        creditCheckbox.setVisibility(
                category.getAttributes().isInstantCheckoutAvailable() ? View.VISIBLE : View.GONE);
    }


    @OnClick(R2.id.btn_buy)
    public void buttonBuyClicked() {
        if (SessionHandler.isV4Login(getContext())) {
            sendGTMClickBeli();
            goToNativeCheckout();
        } else {
            goToLoginPage();
        }
    }

    public void goToNativeCheckout() {
        listener.goToNativeCheckout();
    }

    public void goToLoginPage() {
        listener.goToLoginPage();
    }

    private void sendGTMClickBeli() {
        CommonUtils.dumper("GAv4 category clicked " + category.getId());
        CommonUtils.dumper("GAv4 clicked beli Pulsa");
        String labelBeli;
        switch (category.getId()) {
            case 1:
                labelBeli = AppEventTracking.EventLabel.PULSA_WIDGET;
                break;
            case 2:
                labelBeli = AppEventTracking.EventLabel.PAKET_DATA_WIDGET;
                break;
            case 3:
                labelBeli = AppEventTracking.EventLabel.PLN_WIDGET;
                break;
            default:
                labelBeli = AppEventTracking.EventLabel.PULSA_BELI;
        }
        UnifyTracking.eventRechargeBuy(labelBeli);
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return SessionHandler.getLoginID(getContext()) + "_"
                + (token.isEmpty() ? timeMillis : token);
    }

    @NonNull
    public DigitalCheckoutPassData getGeneratedCheckoutPassData(PreCheckoutDigitalWidget preCheckoutDigitalWidget) {
        return new DigitalCheckoutPassData.Builder()
                .action("init_data")
                .categoryId(String.valueOf(category.getId()))
                .clientNumber(preCheckoutDigitalWidget.getClientNumber())
                .instantCheckout(creditCheckbox.isChecked() ? "1" : "0")
                .isPromo(preCheckoutDigitalWidget.isPromoProduct() ? "1" : "0")
                .operatorId(preCheckoutDigitalWidget.getOperatorId())
                .productId(preCheckoutDigitalWidget.getProductId())
                .utmCampaign(preCheckoutDigitalWidget.getBundle().getString(ARG_UTM_CAMPAIGN,
                        category.getAttributes().getName()))
                .utmContent(preCheckoutDigitalWidget.getBundle().getString(ARG_UTM_CONTENT,
                        VersionInfo.getVersionInfo(getContext())))
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(preCheckoutDigitalWidget.getBundle().getString(ARG_UTM_SOURCE, ARG_UTM_SOURCE_VALUE))
                .utmMedium(preCheckoutDigitalWidget.getBundle().getString(ARG_UTM_MEDIUM, ARG_UTM_MEDIUM_VALUE))
                .build();
    }

    public void renderInstantCheckoutOption(boolean isInstantCheckoutAvailable) {
        creditCheckbox.setVisibility(isInstantCheckoutAvailable ? View.VISIBLE : View.GONE);
        if (isInstantCheckoutAvailable) {
            creditCheckbox.setVisibility(View.VISIBLE);
            creditCheckbox.setOnCheckedChangeListener(checkedChangeListener());
            creditCheckbox.setChecked(listener.isRecentInstantCheckoutUsed(String.valueOf(category.getId())));
        } else {
            creditCheckbox.setChecked(false);
            creditCheckbox.setVisibility(View.GONE);
        }
    }

    public interface OnBuyButtonListener {

        void goToNativeCheckout();

        void goToLoginPage();

        boolean isRecentInstantCheckoutUsed(String categoryId);

        void trackingCheckInstantSaldo(boolean isChecked);
    }
}