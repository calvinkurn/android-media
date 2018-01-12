package com.tokopedia.digital.widget.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.widget.model.PreCheckoutDigitalWidget;
import com.tokopedia.digital.widget.model.category.Category;

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

    private boolean isInsant;

    @BindView(R2.id.buy_with_credit_checkbox)
    CheckBox creditCheckbox;
    @BindView(R2.id.btn_buy)
    Button buyButton;
    @BindView(R2.id.layout_checkbox)
    RelativeLayout layoutCheckbox;
    @BindView(R2.id.tooltip_instant_checkout)
    ImageView tooltip;

    private Category category;
    private OnBuyButtonListener listener;
    private BottomSheetView bottomSheetView;

    private String operatorLabel;

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
        setBottomSheetDialog();
        creditCheckbox.setOnCheckedChangeListener(checkedChangeListener());
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isInsant = isChecked;
                if (isChecked) {
                    buyButton.setText(getResources().getString(R.string.title_button_pay));
                } else {
                    buyButton.setText(operatorLabel);
                }

                listener.trackingCheckInstantSaldo(isChecked);
            }
        };
    }

    public void setBuyButtonText(String text) {
        if (!TextUtils.isEmpty(text)) {
            buyButton.setText(text);
            this.operatorLabel = text;
        } else{
            buyButton.setText(getContext().getString(R.string.title_buy));
            this.operatorLabel = getContext().getString(R.string.title_buy);
        }
    }

    public boolean isCreditCheckboxChecked() {
        return creditCheckbox.isChecked();
    }

    public void setCategory(Category category) {
        this.category = category;
        setVisibilityCheckbox();
    }

    private void setVisibilityCheckbox() {
        layoutCheckbox.setVisibility(
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

        UnifyTracking.eventRechargeBuy(category.getAttributes().getName(), isInsant ? "instant" : "no instant");
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

    private void setBottomSheetDialog() {
        bottomSheetView = new BottomSheetView(getContext());
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getContext().getString(com.tokopedia.digital.R.string.title_tooltip_instan_payment))
                .setBody(getContext().getString(com.tokopedia.digital.R.string.body_tooltip_instan_payment))
                .setImg(com.tokopedia.digital.R.drawable.ic_digital_instant_payment)
                .build());
    }

    public void renderInstantCheckoutOption(boolean isInstantCheckoutAvailable) {
        if (isInstantCheckoutAvailable) {
            layoutCheckbox.setVisibility(View.VISIBLE);
            creditCheckbox.setOnCheckedChangeListener(checkedChangeListener());
            creditCheckbox.setChecked(listener.isRecentInstantCheckoutUsed(String.valueOf(category.getId())));
        } else {
            creditCheckbox.setChecked(false);
            layoutCheckbox.setVisibility(View.GONE);
        }

        tooltip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetView.show();
            }
        });
    }

    public void resetInstantCheckout() {
        creditCheckbox.setChecked(false);
    }

    public interface OnBuyButtonListener {

        void goToNativeCheckout();

        void goToLoginPage();

        boolean isRecentInstantCheckoutUsed(String categoryId);

        void trackingCheckInstantSaldo(boolean isChecked);
    }
}