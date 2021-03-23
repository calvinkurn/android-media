package com.tokopedia.buyerorder.detail.view.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
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
import com.tokopedia.buyerorder.detail.data.Flags;
import com.tokopedia.buyerorder.detail.data.Invoice;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.OrderToken;
import com.tokopedia.buyerorder.detail.data.PayMethod;
import com.tokopedia.buyerorder.detail.data.Pricing;
import com.tokopedia.buyerorder.detail.data.ShopInfo;
import com.tokopedia.buyerorder.detail.data.Status;
import com.tokopedia.buyerorder.detail.data.TickerInfo;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.detail.data.recommendationPojo.RechargeWidgetResponse;
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent;
import com.tokopedia.buyerorder.detail.view.activity.SeeInvoiceActivity;
import com.tokopedia.buyerorder.detail.view.adapter.RechargeWidgetAdapter;
import com.tokopedia.buyerorder.detail.view.customview.CopyableDetailItemView;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter;
import com.tokopedia.buyerorder.list.data.ConditionalInfo;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.PaymentData;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.utils.view.DoubleTextView;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.buyerorder.common.util.BuyerUtils.formatTitleHtml;

/**
 * Created by baghira on 09/05/18.
 */
public class OrderListDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View {

    public static final String KEY_ORDER_ID = "OrderId";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    public static final String KEY_FROM_PAYMENT = "from_payment";
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";
    public static final String VOUCHER_CODE = "Kode Voucher";

    @Inject
    OrderListDetailPresenter presenter;
    OrderDetailsComponent orderListComponent;

    LinearLayout mainView;
    TextView statusLabel;
    TextView statusValue;
    TextView conditionalInfoText;
    LinearLayout statusDetail;
    TextView invoiceView;
    TextView lihat;
    TextView detailLabel;
    LinearLayout detailContent;
    TextView additionalText;
    LinearLayout additionalInfoLayout;
    TextView infoLabel;
    LinearLayout infoValue;
    LinearLayout totalPrice;
    TextView helpLabel;
    TextView primaryActionBtn;
    TextView secondaryActionBtn;
    FrameLayout progressBarLayout;
    private boolean isSingleButton;
    private RecyclerView recommendationList;
    private TextView recommendListTitle;
    private LinearLayout ViewRecomendItems;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(OrderDetailsComponent.class).inject(this);
    }

    public static Fragment getInstance(String orderId, String orderCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        bundle.putString(KEY_ORDER_CATEGORY, orderCategory);
        Fragment fragment = new OrderListDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list_detail, container, false);
        mainView = view.findViewById(R.id.main_view);
        statusLabel = view.findViewById(R.id.status_label);
        statusValue = view.findViewById(R.id.status_value);
        conditionalInfoText = view.findViewById(R.id.conditional_info);
        statusDetail = view.findViewById(R.id.status_detail);
        invoiceView = view.findViewById(R.id.invoice);
        lihat = view.findViewById(R.id.lihat);
        detailLabel = view.findViewById(R.id.detail_label);
        detailContent = view.findViewById(R.id.detail_content);
        additionalText = view.findViewById(R.id.additional);
        additionalInfoLayout = view.findViewById(R.id.additional_info);
        infoLabel = view.findViewById(R.id.info_label);
        infoValue = view.findViewById(R.id.info_value);
        totalPrice = view.findViewById(R.id.total_price);
        helpLabel = view.findViewById(R.id.help_label);
        primaryActionBtn = view.findViewById(R.id.langannan);
        secondaryActionBtn = view.findViewById(R.id.beli_lagi);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        recommendationList = view.findViewById(R.id.recommendation_list);
        recommendListTitle = view.findViewById(R.id.recommend_title);
        ViewRecomendItems = view.findViewById(R.id.recommend_items);
        setMainViewVisible(View.GONE);
        presenter.attachView(this);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT), null, "", "");
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
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8));
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
        String orderId = (getArguments().getString(KEY_ORDER_ID) != null) ?
                getArguments().getString(KEY_ORDER_ID) : "";
        invoiceView.setText(invoice.invoiceRefNum());
        if (!BuyerUtils.isValidUrl(invoice.invoiceUrl())) {
            lihat.setVisibility(View.GONE);
        }
        lihat.setOnClickListener(view -> {
            String orderCategory = getArguments().getString(KEY_ORDER_CATEGORY) != null ?
                    getArguments().getString(KEY_ORDER_CATEGORY) : "";
            presenter.onLihatInvoiceButtonClick(invoice.invoiceUrl());
            if (orderCategory != null && orderCategory.equals(OrderCategory.DIGITAL)) {
                startActivity(SeeInvoiceActivity.newInstance(getContext(),
                        presenter.getOrderCategoryName(),
                        presenter.getOrderProductName(),
                        orderId,
                        invoice.invoiceUrl(),
                        invoice.invoiceRefNum(),
                        getString(R.string.title_invoice),
                        OrderCategory.DIGITAL));
            } else {
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, invoice.invoiceUrl());
            }
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {
        if (VOUCHER_CODE.equalsIgnoreCase(detail.label())) {
            CopyableDetailItemView itemView = new CopyableDetailItemView(getActivity());
            itemView.setTitle(detail.label());
            itemView.setDescription(detail.value());
            itemView.setListener(new CopyableDetailItemView.Listener() {
                @Override
                public void onCopyValue() {
                    if (getContext() != null) {
                        presenter.onCopyButtonClick(detail.value());
                        BuyerUtils.copyTextToClipBoard("voucher code", detail.value(), getContext());
                        BuyerUtils.vibrate(getContext());
                        Toaster.build(itemView, getString(R.string.title_voucher_code_copied), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show();
                    }
                }
            });
            detailContent.addView(itemView);
        } else {
            DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
            doubleTextView.setTopText(detail.label());
            doubleTextView.setBottomText(detail.value());
            detailContent.addView(doubleTextView);
        }
    }

    @Override
    public void setIsRequestedCancel(Boolean isRequestedCancel) {
        // no-op
    }

    @Override
    public void setAdditionInfoVisibility(int visibility) {
        additionalText.setVisibility(visibility);
        additionalText.setOnClickListener(view -> {
            additionalText.setOnClickListener(null);
            additionalText.setText(getResources().getString(R.string.additional_text));
            additionalText.setTypeface(Typeface.DEFAULT_BOLD);
            additionalText.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            additionalInfoLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(additionalInfo.label());
        doubleTextView.setBottomText(additionalInfo.value());
        additionalInfoLayout.addView(doubleTextView);
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
        // no-op
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
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.END);
        infoValue.addView(doubleTextView);

    }

    @Override
    public void setButtonMargin() {
        this.isSingleButton = true;
    }

    @Override
    public void showDropshipperInfo(DropShipper dropShipper) {
        // no-op
    }

    @Override
    public void showDriverInfo(DriverDetails driverDetails) {
        // no-op
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
        // no-op
    }

    @Override
    public void setShopInfo(ShopInfo shopInfo) {
        // no-op
    }

    @Override
    public void setBoughtDate(String boughtDate) {
        // no-op
    }

    @Override
    public void showReplacementView(List<String> reasons) {
        // no-op
    }

    @Override
    public void finishOrderDetail() {
        // no-op
    }

    @Override
    public void showSuccessMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show();
        }
    }

    @Override
    public void showSuccessMessageWithAction(String message) {
        // no-op
    }

    @Override
    public void showErrorMessage(String message) {
        // no-op
    }

    @Override
    public void clearDynamicViews() {
        // no-op
    }

    @Override
    public JsonArray generateInputQueryBuyAgain(List<Items> items) {
        return null;
    }

    @Override
    public void hitAnalyticsBuyAgain(List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct> listAtcProducts, Boolean isAtcMultiSuccess) {
        // no-op
    }

    @Override
    public void setRecommendation(Object recommendationResponse) {
        RechargeWidgetResponse rechargeWidgetResponse = (RechargeWidgetResponse) recommendationResponse;
        if (rechargeWidgetResponse.getHomeWidget() != null && rechargeWidgetResponse.getHomeWidget().getWidgetGrid() != null) {
            if (rechargeWidgetResponse.getHomeWidget().getWidgetGrid().isEmpty()) {
                ViewRecomendItems.setVisibility(View.GONE);
            } else {
                if (getContext() != null) {
                    recommendListTitle.setText(getContext().getString(R.string.tkpdtransaction_widget_title));
                    recommendationList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recommendationList.setAdapter(new RechargeWidgetAdapter(rechargeWidgetResponse.getHomeWidget().getWidgetGrid()));
                }
            }
        }
    }

    @Override
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals(""))
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.END);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs, String helpLink) {
        String text = Html.fromHtml(contactUs.helpText()).toString();
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf("disini");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, contactUs.helpUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)); // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + "disini".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpLabel.setHighlightColor(Color.TRANSPARENT);
        helpLabel.setMovementMethod(LinkMovementMethod.getInstance());
        helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void setTopActionButton(ActionButton actionButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_24));
        primaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_4));
        if (!actionButton.getActionColor().getBackground().equals("")) {
            shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
        }
        if (!actionButton.getActionColor().getBorder().equals("")) {
            shape.setStroke(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
        }
        primaryActionBtn.setBackground(shape);
        if (isSingleButton) {
            primaryActionBtn.setLayoutParams(params);
        }
        if (!actionButton.getActionColor().getTextColor().equals("")) {
            primaryActionBtn.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
        }
        if (!TextUtils.isEmpty(actionButton.getUri())) {
            primaryActionBtn.setOnClickListener(v -> {
                presenter.onActionButtonClick(ActionButton.PRIMARY_BUTTON, actionButton.getLabel());
                onActionButtonClick(actionButton.getUri());
            });
        }
    }

    @Override
    public void setBottomActionButton(ActionButton actionButton) {
        secondaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_4));
        if (!actionButton.getActionColor().getBackground().equals("")) {
            shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
        }
        if (!actionButton.getActionColor().getBorder().equals("")) {
            shape.setStroke(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
        }
        secondaryActionBtn.setBackground(shape);
        if (!actionButton.getActionColor().getTextColor().equals("")) {
            secondaryActionBtn.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
        }
        if (!TextUtils.isEmpty(actionButton.getUri())) {
            secondaryActionBtn.setOnClickListener(v -> {
                presenter.onActionButtonClick(ActionButton.SECONDARY_BUTTON, actionButton.getLabel());
                onActionButtonClick(actionButton.getUri());
            });
        }
    }

    private void onActionButtonClick(final String uri) {
        String newUri = uri;
        if (uri != null && uri.startsWith("tokopedia")) {
            Uri url = Uri.parse(newUri);

            if (newUri.contains("idem_potency_key")) {
                newUri = newUri.replace(url.getQueryParameter("idem_potency_key"), "");
                newUri = newUri.replace("idem_potency_key=", "");
            }
            RouteManager.route(getActivity(), newUri);
        } else if (uri != null && !uri.equals("")) {
            RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
        }
    }

    @Override
    public void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility) {
        primaryActionBtn.setVisibility(topBtnVisibility);
        secondaryActionBtn.setVisibility(bottomBtnVisibility);
    }

    @Override
    public void setItems(List<Items> items, boolean isTradeIn, OrderDetails orderDetails) {
        // no-op
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }

    @Override
    public void setActionButtonLayoutClickable(Boolean isClickable) {
        // no op
    }

    @Override
    public void setActionButtonText(String txt) {
        // no op
    }
}
