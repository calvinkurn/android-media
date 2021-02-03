package com.tokopedia.buyerorder.detail.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.atc_common.domain.model.response.AtcMultiData;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerConsts;
import com.tokopedia.buyerorder.common.util.BuyerUtils;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.AdditionalInfo;
import com.tokopedia.buyerorder.detail.data.AdditionalTickerInfo;
import com.tokopedia.buyerorder.detail.data.ContactUs;
import com.tokopedia.buyerorder.detail.data.Detail;
import com.tokopedia.buyerorder.detail.data.Discount;
import com.tokopedia.buyerorder.detail.data.DriverDetails;
import com.tokopedia.buyerorder.detail.data.DropShipper;
import com.tokopedia.buyerorder.detail.data.EntityPessenger;
import com.tokopedia.buyerorder.detail.data.Flags;
import com.tokopedia.buyerorder.detail.data.Header;
import com.tokopedia.buyerorder.detail.data.Invoice;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.MetaDataInfo;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.OrderToken;
import com.tokopedia.buyerorder.detail.data.PassengerForm;
import com.tokopedia.buyerorder.detail.data.PassengerInformation;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.ShopInfo;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.data.TickerInfo;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.activity.OrderListwebViewActivity;
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter;
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter;
import com.tokopedia.buyerorder.list.data.ConditionalInfo;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.PaymentData;
import com.tokopedia.coachmark.CoachMark;
import com.tokopedia.coachmark.CoachMarkBuilder;
import com.tokopedia.coachmark.CoachMarkItem;
import com.tokopedia.kotlin.util.DownloadHelper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.view.DoubleTextView;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import kotlin.Unit;

import static com.tokopedia.buyerorder.common.util.BuyerUtils.formatTitleHtml;

/**
 * Created by baghira on 09/05/18.
 */

public class OmsDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View, ItemsAdapter.SetEventDetails {

    public static final String KEY_BUTTON = "button";
    public static final String KEY_TEXT = "text";
    public static final String KEY_REDIRECT = "redirect";
    public static final String KEY_ORDER_ID = "OrderId";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    private static final String KEY_FROM_PAYMENT = "from_payment";
    private static final String KEY_UPSTREAM = "upstream";
    private static final String KEY_URI = "tokopedia";
    private static final String KEY_URI_PARAMETER = "idem_potency_key";
    private static final String KEY_URI_PARAMETER_EQUAL = "idem_potency_key=";
    public static final String CATEGORY_GIFT_CARD = "Gift-card";
    private static final String INSURANCE_CLAIM = "tokopedia://webview?allow_override=false&url=https://www.tokopedia.com/asuransi/klaim";
    private static final String PREFERENCES_NAME = "deals_banner_preferences";
    private static final String SHOW_COACH_MARK_KEY = "show_coach_mark_key_deals_banner";

    public static int RETRY_COUNT = 0;
    private static long DELAY_COACH_MARK_START = 500L;

    @Inject
    OrderListDetailPresenter presenter;

    @Inject
    OrderListAnalytics orderListAnalytics;

    OrderDetailsComponent orderListComponent;
    private LinearLayout mainView;
    private TextView statusLabel;
    private TextView statusValue;
    private TextView conditionalInfoText;
    private LinearLayout statusDetail;
    private TextView invoiceView;
    private TextView lihat;
    private TextView detailLabel;
    private LinearLayout detailsLayout;
    private TextView infoLabel;
    private LinearLayout infoValue;
    private LinearLayout totalPrice;
    private TextView helpLabel;
    private TextView primaryActionBtn;
    private TextView secondaryActionBtn;
    private RecyclerView recyclerView;
    LinearLayout paymentMethodInfo;
    FrameLayout progressBarLayout;
    private boolean isSingleButton;
    private PermissionCheckerHelper permissionCheckerHelper;
    RelativeLayout actionButtonLayout;
    TextView actionButtonText;
    LinearLayout userInfo;
    TextView userInfoLabel;
    private String categoryName;
    View dividerUserInfo, dividerActionBtn, dividerInfoLabel, dividerBannerDeals;
    private CardView policy;
    private CardView claim;
    private View bannerDeals;
    private Typography bannerMainTitle;
    private Typography bannerSubTitle;
    private NestedScrollView parentScroll;
    private Boolean _isDownloadable;

    private LocalCacheHandler localCacheHandler;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(OrderDetailsComponent.class).inject(this);
    }

    public static Fragment getInstance(String orderId, String orderCategory, String fromPayment, String upstream) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        bundle.putString(KEY_ORDER_CATEGORY, orderCategory);
        bundle.putString(KEY_FROM_PAYMENT, fromPayment);
        bundle.putString(KEY_UPSTREAM, upstream);
        Fragment fragment = new OmsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oms_list_detail, container, false);
        mainView = view.findViewById(R.id.main_view);
        statusLabel = view.findViewById(R.id.status_label);
        statusValue = view.findViewById(R.id.status_value);
        conditionalInfoText = view.findViewById(R.id.conditional_info);
        statusDetail = view.findViewById(R.id.status_detail);
        invoiceView = view.findViewById(R.id.invoice);
        lihat = view.findViewById(R.id.lihat);
        detailLabel = view.findViewById(R.id.detail_label);
        detailsLayout = view.findViewById(R.id.details_section);
        infoLabel = view.findViewById(R.id.info_label);
        infoValue = view.findViewById(R.id.info_value);
        totalPrice = view.findViewById(R.id.total_price);
        helpLabel = view.findViewById(R.id.help_label);
        primaryActionBtn = view.findViewById(R.id.langannan);
        secondaryActionBtn = view.findViewById(R.id.beli_lagi);
        recyclerView = view.findViewById(R.id.recycler_view);
        paymentMethodInfo = view.findViewById(R.id.info_payment);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        actionButtonLayout = view.findViewById(R.id.actionButton);
        userInfo = view.findViewById(R.id.user_information_layout);
        userInfoLabel = view.findViewById(R.id.user_label);
        dividerUserInfo = view.findViewById(R.id.divider_above_userInfo);
        dividerActionBtn = view.findViewById(R.id.divider_above_actionButton);
        dividerInfoLabel = view.findViewById(R.id.divider_above_info_label);
        dividerBannerDeals = view.findViewById(R.id.divider_above_banner_deals);
        actionButtonText = view.findViewById(R.id.actionButton_text);
        recyclerView.setNestedScrollingEnabled(false);
        policy = view.findViewById(R.id.policy);
        claim = view.findViewById(R.id.claim);
        bannerDeals = view.findViewById(R.id.banner_deals_order_detail);
        bannerMainTitle = view.findViewById(R.id.tg_deal_banner_title);
        bannerSubTitle = view.findViewById(R.id.tg_deal_banner_sub_title);
        parentScroll = view.findViewById(R.id.parentScroll);

        localCacheHandler = new LocalCacheHandler(getContext(),PREFERENCES_NAME);

        initInjector();
        setMainViewVisible(View.GONE);

        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString("from_payment"), (String) getArguments().get(KEY_UPSTREAM), "", "");
    }

    @Override
    public void setDetailsData(OrderDetails details) {
        hideProgressBar();
        setStatus(details.status());
        clearDynamicViews();
        if (details.conditionalInfo().text() != null && !details.conditionalInfo().text().equals("")) {
            setConditionalInfo(details.conditionalInfo());
        }
        for (Title title : details.title()) {
            setTitle(title);
        }
        setInvoice(details.invoice());
        setOrderToken(details.orderToken());
        for (int i = 0; i < details.detail().size(); i++) {
            setDetail(details.detail().get(i));
        }

        setBoughtDate(details.getBoughtDate());
        if (details.getShopInfo() != null) {
            setShopInfo(details.getShopInfo());
        }
        if (details.getItems() != null && details.getItems().size() > 0) {
            Flags flags = details.getFlags();
            if (flags != null)
                setItems(details.getItems(), flags.isIsOrderTradeIn(), details);
            else
                setItems(details.getItems(), false, details);
        }
        if (details.additionalInfo().size() > 0) {
            setAdditionInfoVisibility(View.VISIBLE);
        }
        for (AdditionalInfo additionalInfo : details.additionalInfo()) {
            setAdditionalInfo(additionalInfo);
        }

        if (details.getAdditionalTickerInfos() != null
                && details.getAdditionalTickerInfos().size() > 0) {
            String url = null;
            for (AdditionalTickerInfo tickerInfo : details.getAdditionalTickerInfos()) {
                if (tickerInfo.getUrlDetail() != null && !tickerInfo.getUrlDetail().isEmpty()) {
                    String formattedTitle = formatTitleHtml(
                            tickerInfo.getNotes(),
                            tickerInfo.getUrlDetail(),
                            tickerInfo.getUrlText()
                    );
                    tickerInfo.setNotes(formattedTitle);
                    url = tickerInfo.getUrlDetail();
                }
            }
            setAdditionalTickerInfo(details.getAdditionalTickerInfos(), url);
        }

        if (details.getTickerInfo() != null) {
            setTickerInfo(details.getTickerInfo());
        }

        for (PayMethod payMethod : details.getPayMethods()) {
            if (!TextUtils.isEmpty(payMethod.getValue()))
                setPayMethodInfo(payMethod);
        }

        for (Pricing pricing : details.pricing()) {
            setPricing(pricing);
        }

        setPaymentData(details.paymentData());
        setContactUs(details.contactUs(), details.getHelpLink());

        if (details.getItems() != null && details.getItems().size() > 0 && details.getItems().get(0).getCategory().equalsIgnoreCase(OrderCategory.EVENT)) {
            setActionButtonsVisibility(View.GONE, View.GONE);
        } else {
            if (details.actionButtons().size() == 2) {
                ActionButton leftActionButton = details.actionButtons().get(0);
                ActionButton rightActionButton = details.actionButtons().get(1);
                setTopActionButton(leftActionButton);
                setBottomActionButton(rightActionButton);
            } else if (details.actionButtons().size() == 1) {
                ActionButton actionButton = details.actionButtons().get(0);
                setButtonMargin();
                if (actionButton.getLabel().equals(BuyerConsts.INVOICE)) {
                    setBottomActionButton(actionButton);
                    setActionButtonsVisibility(View.GONE, View.VISIBLE);
                } else {
                    setTopActionButton(actionButton);
                    setActionButtonsVisibility(View.VISIBLE, View.GONE);

                }
            } else {
                setActionButtonsVisibility(View.GONE, View.GONE);
            }
        }
        setMainViewVisible(View.VISIBLE);
    }

    @Override
    public void setStatus(Status status) {
        statusLabel.setText(status.statusLabel());
        statusValue.setText(status.statusText());
        if (!status.textColor().equals(""))
            statusValue.setTextColor(Color.parseColor(status.textColor()));
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        if (!status.backgroundColor().equals("")) {
            shape.setColor(Color.parseColor(status.backgroundColor()));
        }
        statusValue.setBackground(shape);
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(9);
        if (!TextUtils.isEmpty(conditionalInfo.color().background())) {
            shape.setColor(Color.parseColor(conditionalInfo.color().background()));
        }
        if (!TextUtils.isEmpty(conditionalInfo.color().border())) {
            shape.setStroke(getResources().getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_2), Color.parseColor(conditionalInfo.color().border()));
        }
        conditionalInfoText.setBackground(shape);
        conditionalInfoText.setPadding(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16));
        conditionalInfoText.setText(conditionalInfo.text());
        if (!TextUtils.isEmpty(conditionalInfo.color().textColor())) {
            conditionalInfoText.setTextColor(Color.parseColor(conditionalInfo.color().textColor()));
        }

    }

    @Override
    public void setTitle(Title title) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(title.label());
        doubleTextView.setBottomText(title.value());
        statusDetail.addView(doubleTextView);
    }

    @Override
    public void setInvoice(final Invoice invoice) {
        invoiceView.setText(invoice.invoiceRefNum());
        if (!BuyerUtils.isValidUrl(invoice.invoiceUrl())) {
            lihat.setVisibility(View.GONE);
        }
        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, invoice.invoiceUrl());
            }
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {
        // no-op
    }

    @Override
    public void setDetail(Detail detail) {
        // no-op
    }

    @Override
    public void setIsRequestedCancel(Boolean isRequestedCancel) {
        // no-op
    }

    @Override
    public void setAdditionInfoVisibility(int visibility) {
        // no-op
    }

    @Override
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        // no-op
    }

    @Override
    public void setAdditionalTickerInfo(List<AdditionalTickerInfo> tickerInfos, @Nullable String url) {
        // no-op
    }

    @Override
    public void setTickerInfo(TickerInfo tickerInfo) {
        // no-op
    }

    @Override
    public void setPricing(Pricing pricing) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(pricing.label());
        doubleTextView.setBottomText(pricing.value());
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        infoValue.addView(doubleTextView);
    }

    @Override
    public void setDiscount(Discount discount) {
        // no-op
    }

    @Override
    public void setDiscountVisibility(int visibility) {
        // no-op
    }

    @Override
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals("")) {
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        }
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs, String helpLink) {
        String text = getResources().getString(R.string.contact_us_text);
        String clickableLink = getResources().getString(R.string.contact_us_clickable_text);
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf(clickableLink);
        if (startIndexOfLink != -1) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(helpLink)) {
                        RouteManager.route(getContext(), helpLink);
                    } else {

                        Intent intent = null;
                        try {
                            intent = OrderListwebViewActivity.getWebViewIntent(getContext(), URLDecoder.decode(
                                    getContext().getResources().getString(R.string.contact_us_applink), "UTF-8"), "Help Centre");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)); // specific color for this link
                }
            }, startIndexOfLink, startIndexOfLink + clickableLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helpLabel.setHighlightColor(Color.TRANSPARENT);
            helpLabel.setMovementMethod(LinkMovementMethod.getInstance());

            helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public void setTopActionButton(ActionButton actionButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_24));
        primaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
        shape.setStroke(2, getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100));
        primaryActionBtn.setBackground(shape);
        if (isSingleButton) {
            primaryActionBtn.setLayoutParams(params);
        }
        if (!TextUtils.isEmpty(actionButton.getBody().getAppURL())) {
            primaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
        }
    }

    @Override
    public void setBottomActionButton(ActionButton actionButton) {
        secondaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500));
        secondaryActionBtn.setBackground(shape);
        secondaryActionBtn.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
        if (!TextUtils.isEmpty(actionButton.getBody().getAppURL())) {
            secondaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
        }
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUri = uri;
                if (uri.startsWith(KEY_URI)) {
                    if (newUri.contains(KEY_URI_PARAMETER)) {
                        Uri url = Uri.parse(newUri);
                        newUri = newUri.replace(url.getQueryParameter(KEY_URI_PARAMETER), "");
                        newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "");
                    }
                    RouteManager.route(getActivity(), newUri);
                } else if (uri != null && !uri.equals("")) {
                    RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
                }
            }
        };
    }

    @Override
    public void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility) {
        primaryActionBtn.setVisibility(topBtnVisibility);
        secondaryActionBtn.setVisibility(bottomBtnVisibility);
    }

    @Override
    public void setItems(List<Items> items, boolean isTradeIn, OrderDetails orderDetails) {
        List<Items> itemsList = new ArrayList<>();
        boolean metadataEmpty = true;
        for (Items item : items) {
            if (!CATEGORY_GIFT_CARD.equalsIgnoreCase(item.getCategory())) {
                itemsList.add(item);
            }
        }
        for (Items item : itemsList) {
            if (!item.getMetaData().isEmpty()) {
                metadataEmpty = false;
                break;
            }
        }
        if (itemsList.size() > 0 && !metadataEmpty) {
            recyclerView.setAdapter(new ItemsAdapter(getContext(), items, false, presenter, OmsDetailFragment.this, getArguments().getString(KEY_ORDER_ID), orderDetails));
        } else {
            detailsLayout.setVisibility(View.GONE);
            dividerInfoLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomGravity(Gravity.END);
        paymentMethodInfo.addView(doubleTextView);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setButtonMargin() {
        isSingleButton = true;
    }

    @Override
    public void showDropshipperInfo(DropShipper dropShipper) {

    }

    @Override
    public void showDriverInfo(DriverDetails driverDetails) {

    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setActionButtons(List<ActionButton> actionButtons) {
        actionButtonLayout.setVisibility(View.VISIBLE);
        actionButtonText.setText(actionButtons.get(0).getLabel());
        actionButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButtons.get(0).getControl().equalsIgnoreCase(KEY_BUTTON) && getContext() != null) {
                    presenter.getActionButtonGql(GraphqlHelper.loadRawString(getContext().getResources(), R.raw.tapactions), actionButtons, null, 0, false);
                } else if (actionButtons.get(0).getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                    RouteManager.route(getContext(), actionButtons.get(0).getBody().getAppURL());
                }
            }
        });
    }

    @Override
    public void setShopInfo(ShopInfo shopInfo) {

    }

    @Override
    public void setBoughtDate(String boughtDate) {

    }

    @Override
    public void showReplacementView(List<String> reasons) {

    }

    @Override
    public void finishOrderDetail() {

    }

    @Override
    public void showSuccessMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL, "", v -> { }).show();
        }
    }

    @Override
    public void showSuccessMessageWithAction(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_INDEFINITE, Toaster.TYPE_NORMAL, "Oke", v1 -> { }).show();
        }
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void clearDynamicViews() {

    }

    @Override
    public void askPermission(String uri, Boolean isDownloadable, String downloadFileName) {
        permissionCheckerHelper = new PermissionCheckerHelper();
        permissionCheckerHelper.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(@NotNull String permissionText) {

            }

            @Override
            public void onNeverAskAgain(@NotNull String permissionText) {

            }

            @Override
            public void onPermissionGranted() {
                permissionGrantedContinueDownload(uri, downloadFileName, isDownloadable);
            }
        }, "");
    }

    @Override
    public void sendThankYouEvent(MetaDataInfo metaDataInfo, int categoryType, OrderDetails orderDetails) {
        if ("true".equalsIgnoreCase((String) getArguments().getString(KEY_FROM_PAYMENT))) {
            String paymentStatus = "", paymentMethod = "";
            if (orderDetails != null && orderDetails.status() != null && !TextUtils.isEmpty(orderDetails.status().statusText())) {
                paymentStatus = orderDetails.status().statusText();
            }
            if (orderDetails != null && orderDetails.getPayMethods() != null && orderDetails.getPayMethods().size() > 0 && !TextUtils.isEmpty(orderDetails.getPayMethods().get(0).getValue())) {
                paymentMethod = orderDetails.getPayMethods().get(0).getValue();
            }
            if (categoryType == 3) {
                orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getProductName(), metaDataInfo.getTotalPrice(), metaDataInfo.getQuantity(), metaDataInfo.getEntityBrandName(), (String) getArguments().get(KEY_ORDER_ID), categoryType, paymentMethod, paymentStatus);
            } else {
                orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getEntityProductName(), metaDataInfo.getTotalTicketPrice(), metaDataInfo.getTotalTicketCount(), metaDataInfo.getEntityBrandName(), (String) getArguments().get(KEY_ORDER_ID), categoryType, paymentMethod, paymentStatus);
            }
        }
    }

    @Override
    public void showRetryButtonToaster(String msg) {
        if (getView() != null) {
            Toaster.build(getView(), msg, Toaster.LENGTH_INDEFINITE, Toaster.TYPE_NORMAL, "Oke", v1 -> { }).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void permissionGrantedContinueDownload(String uri, String fileName, Boolean isDownloadable) {
        _isDownloadable = isDownloadable;
        DownloadHelper downloadHelper = new DownloadHelper(getContext(), uri, fileName, () -> {
            // download success call back

        });
        downloadHelper.downloadFile(this::isUridownloadable);
    }

    private Boolean isUridownloadable(String uri) {
        Pattern pattern = Pattern.compile("^.+\\.([pP][dD][fF])$");
        Matcher matcher = pattern.matcher(uri);
        return matcher.find() || _isDownloadable;
    }

    @Override
    public void setRecommendation(Object rechargeWidgetResponse) {

    }

    @Override
    public JsonArray generateInputQueryBuyAgain(List<Items> items) {
        return null;
    }

    @Override
    public void hitAnalyticsBuyAgain(List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct> listAtcProducts, Boolean isAtcMultiSuccess) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
        }
    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }

    @Override
    public void setDealsBanner(Items item){
        MetaDataInfo metaDataInfo = new Gson().fromJson(item.getMetaData(), MetaDataInfo.class);
        if(metaDataInfo != null){
            if (metaDataInfo.getCustomLinkType() != null) {
                if (metaDataInfo.getCustomLinkType().equalsIgnoreCase(KEY_REDIRECT)) {
                    UserSession userSession = new UserSession(getContext());
                    dividerBannerDeals.setVisibility(View.VISIBLE);
                    bannerDeals.setVisibility(View.VISIBLE);

                    if (isCoachmarkAlreadyShowed()) {
                        bannerDeals.post(new Runnable() {
                            @Override
                            public void run() {
                                int scrollTo = ((View) bannerDeals.getParent()).getTop() + bannerDeals.getTop();
                                parentScroll.smoothScrollTo(0, scrollTo);
                                addCoachmarkBannerDeals();
                            }
                        });
                    }

                    bannerMainTitle.setText(getResources().getString(R.string.banner_deals_main_title, userSession.getName()));
                    if (!metaDataInfo.getCustomLinkLabel().isEmpty()) {
                        bannerSubTitle.setText(metaDataInfo.getCustomLinkLabel());
                    }
                    bannerDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!metaDataInfo.getCustomLinkAppUrl().isEmpty()) {
                                RouteManager.route(getActivity(), metaDataInfo.getCustomLinkAppUrl());
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void setEventDetails(ActionButton actionButton, Items item) {

        MetaDataInfo metaDataInfo = new Gson().fromJson(item.getMetaData(), MetaDataInfo.class);
        if (item.getActionButtons() == null || item.getActionButtons().size() == 0) {
            actionButtonLayout.setVisibility(View.GONE);
        } else {
            dividerActionBtn.setVisibility(View.VISIBLE);
            actionButtonLayout.setVisibility(View.VISIBLE);
            actionButtonText.setText(actionButton.getLabel());
            actionButtonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
                        if (!TextUtils.isEmpty(item.getCategory()) && "Deal".equalsIgnoreCase(item.getCategory())) {
                            if (getView() != null) {
                                Toaster.build(getView(), String.format("%s %s", getContext().getResources().getString(R.string.deal_voucher_code_copied), metaDataInfo.getEntityaddress().getEmail()), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, "Ok", v1 -> { }).show();
                            }
                        } else {
                            if (getView() != null) {
                                Toaster.build(getView(), String.format("%s %s", getContext().getResources().getString(R.string.event_voucher_code_copied), metaDataInfo.getEntityaddress().getEmail()), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, "Ok", v1 -> { }).show();
                            }
                        }
                        if (getContext() != null) {
                            presenter.getActionButtonGql(GraphqlHelper.loadRawString(getContext().getResources(), R.raw.tapactions), item.getActionButtons(), null, 0, false);
                        }
                    } else if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                        RouteManager.route(getContext(), actionButton.getBody().getAppURL());
                    }
                }
            });

        }

        if (!item.getCategory().equalsIgnoreCase(OrderCategory.EVENT)){
            if (metaDataInfo != null && metaDataInfo.getEntityPessengers() != null && metaDataInfo.getEntityPessengers().size() > 0) {
                userInfoLabel.setVisibility(View.VISIBLE);
                userInfo.setVisibility(View.VISIBLE);
                dividerUserInfo.setVisibility(View.VISIBLE);
                userInfo.removeAllViews();
                for (EntityPessenger entityPessenger : metaDataInfo.getEntityPessengers()) {
                    DoubleTextView doubleTextView = new DoubleTextView(getContext(), LinearLayout.VERTICAL);
                    doubleTextView.setTopText(entityPessenger.getTitle());
                    doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                    doubleTextView.setBottomText(entityPessenger.getValue());
                    doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
                    doubleTextView.setBottomTextStyle("bold");

                    userInfo.addView(doubleTextView);
                }
            } else {
                userInfoLabel.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
                dividerUserInfo.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void setActionButtonEvent(Items item, ActionButton actionButton,OrderDetails orderDetails){
        MetaDataInfo metaDataInfo = new Gson().fromJson(item.getMetaData(), MetaDataInfo.class);

        if (orderDetails.actionButtons() == null || orderDetails.actionButtons().size() == 0) {
            actionButtonLayout.setVisibility(View.GONE);
            dividerActionBtn.setVisibility(View.GONE);
        } else {
            dividerActionBtn.setVisibility(View.VISIBLE);
            actionButtonLayout.setVisibility(View.VISIBLE);
            actionButtonText.setText(actionButton.getLabel());
            actionButtonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON) &&
                            actionButton.getName().equalsIgnoreCase("customer_notification")) {
                        presenter.hitEventEmail(actionButton,orderDetails.getMetadata());
                    } else if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                        RouteManager.route(getContext(), actionButton.getBody().getAppURL());
                    }
                }
            });

        }
    }

    @Override
    public void setPassengerEvent(Items item){
        if (item.getCategory().equalsIgnoreCase(OrderCategory.EVENT)) {
            MetaDataInfo metaDataInfo = new Gson().fromJson(item.getMetaData(), MetaDataInfo.class);

            if (metaDataInfo != null && !metaDataInfo.getPassengerForms().isEmpty()) {
                userInfoLabel.setVisibility(View.VISIBLE);
                userInfo.setVisibility(View.VISIBLE);
                dividerUserInfo.setVisibility(View.VISIBLE);
                userInfo.removeAllViews();
                for (PassengerForm passengerForm : metaDataInfo.getPassengerForms()) {
                    if(passengerForm.getPassengerInformations() != null) {
                        for (PassengerInformation passengerInformation : passengerForm.getPassengerInformations()) {
                            DoubleTextView doubleTextView = new DoubleTextView(getContext(), LinearLayout.VERTICAL);
                            doubleTextView.setTopText(passengerInformation.getTitle());
                            doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                            doubleTextView.setBottomText(passengerInformation.getValue());
                            doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
                            doubleTextView.setBottomTextStyle("bold");

                            userInfo.addView(doubleTextView);
                        }
                    }
                }
            } else {
                userInfoLabel.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
                dividerUserInfo.setVisibility(View.GONE);
            }
        }
    }

    private void showDealsQR(ActionButton actionButton) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.deals_qr_code_layout, mainView, false);

        BottomSheetUnify bottomSheetUnify = new BottomSheetUnify();
        bottomSheetUnify.setTitle(getString(R.string.text_redeem_voucher));
        bottomSheetUnify.setCloseClickListener(v -> {
            bottomSheetUnify.dismiss();
            return Unit.INSTANCE;
        });

        ImageView qrCode = view.findViewById(R.id.qrCode);
        Typography voucherNo = view.findViewById(R.id.redeem_dialog_voucher_code);
        Typography poweredBy = view.findViewById(R.id.redeem_dialog_shop_name);
        Typography poweredByPrefix = view.findViewById(R.id.redeem_dialog_powered_by);

        Header header = actionButton.getHeaderObject();

        if (!header.getStatusLabel().isEmpty()) {
            Typography disableText = view.findViewById(R.id.redeem_dialog_expired_text);
            View expiredOverlay = view.findViewById(R.id.redeem_dialog_expired_view);
            expiredOverlay.setVisibility(View.VISIBLE);
            disableText.setVisibility(View.VISIBLE);
            disableText.setText(header.getStatusLabel());
        }

        ImageHandler.loadImage(getContext(), qrCode, actionButton.getBody().getAppURL(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);

        if (actionButton.getHeaderObject() != null) {
            poweredBy.setText(header.getPoweredBy());
            voucherNo.setText(header.getVoucherCodes());

            if (header.getPoweredBy().isEmpty()) {
                poweredBy.setVisibility(View.GONE);
                poweredByPrefix.setVisibility(View.GONE);
            }
        }
        if (getActivity() != null) {
            bottomSheetUnify.setChild(view);
            bottomSheetUnify.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    private void showEventQR(ActionButton actionButton, Items item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.scan_qr_code_layout, mainView, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(com.tokopedia.design.R.color.transparent);

        ImageView qrCode = view.findViewById(R.id.qrCode);
        LinearLayout voucherCodeLayout = view.findViewById(R.id.booking_code_view);
        TextView closeButton = view.findViewById(R.id.redeem_ticket);
        ImageHandler.loadImage(getContext(), qrCode, actionButton.getBody().getAppURL(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);

        if (!actionButton.getBody().getBody().isEmpty()) {
            String[] voucherCodes = actionButton.getBody().getBody().split(",");
            if (voucherCodes.length > 0) {
                voucherCodeLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < voucherCodes.length; i++) {
                    BookingCodeView bookingCodeView = new BookingCodeView(getContext(), voucherCodes[i], 0, getContext().getResources().getString(R.string.voucher_code_title), voucherCodes[i].length());
                    bookingCodeView.setBackground(getContext().getResources().getDrawable(com.tokopedia.design.R.drawable.bg_search_input_text_area));
                    voucherCodeLayout.addView(bookingCodeView);
                }
            }
        }
        closeButton.setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();
    }

    private void addCoachmarkBannerDeals(){
        CoachMarkItem coachMarkItem = new CoachMarkItem(bannerDeals,
                getResources().getString(R.string.banner_deals_coachmark_title),
                getResources().getString(R.string.banner_deals_coachmark_sub_title));

        ArrayList<CoachMarkItem> listCoachMark = new ArrayList<>();
        listCoachMark.add(coachMarkItem);

        CoachMarkBuilder coachMarkBuilder = new CoachMarkBuilder();
        CoachMark coachMark =  coachMarkBuilder.build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                coachMark.show(getActivity(),"",listCoachMark);
            }
        }, DELAY_COACH_MARK_START);

        localCacheHandler.putBoolean(SHOW_COACH_MARK_KEY,false);
        localCacheHandler.applyEditor();

    }

    private Boolean isCoachmarkAlreadyShowed(){
        return localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY,true);
    }

    @Override
    public void openShowQRFragment(ActionButton actionButton, Items item) {
        if (item.getCategory().equalsIgnoreCase(ItemsAdapter.categoryDeals) || item.getCategoryID() == ItemsAdapter.DEALS_CATEGORY_ID) {
            showDealsQR(actionButton);
        } else {
            showEventQR(actionButton, item);
        }
    }

    @Override
    public void setDetailTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            detailLabel.setText(title);
        }
    }

    @Override
    public void setInsuranceDetail() {
        policy.setVisibility(View.VISIBLE);
        claim.setVisibility(View.VISIBLE);
        dividerInfoLabel.setVisibility(View.GONE);
        claim.setOnClickListener(view -> {
                    RouteManager.route(
                            getContext(), INSURANCE_CLAIM
                    );
                }
        );
    }

    @Override
    public void setActionButtonLayoutClickable(Boolean isClickable) {
        actionButtonLayout.setClickable(isClickable);
    }

    @Override
    public void setActionButtonText(String txt) {
        actionButtonText.setText(txt);
    }
}
