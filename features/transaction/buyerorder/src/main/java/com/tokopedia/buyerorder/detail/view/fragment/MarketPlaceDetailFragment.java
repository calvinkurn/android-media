package com.tokopedia.buyerorder.detail.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalOrder;
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
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationResponse;
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.detail.view.activity.BuyerRequestCancelActivity;
import com.tokopedia.buyerorder.detail.view.activity.SeeInvoiceActivity;
import com.tokopedia.buyerorder.detail.view.adapter.ProductItemAdapter;
import com.tokopedia.buyerorder.detail.view.adapter.RecommendationMPAdapter;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter;
import com.tokopedia.buyerorder.list.data.ConditionalInfo;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.PaymentData;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.view.DoubleTextView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import timber.log.Timber;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.tokopedia.applink.internal.ApplinkConstInternalOrder.EXTRA_ORDER_ID;
import static com.tokopedia.applink.internal.ApplinkConstInternalOrder.EXTRA_USER_MODE;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.ACTION_FINISH_ORDER;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_CODE_INSTANT_CANCEL;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_MSG_INSTANT_CANCEL;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_POPUP_BODY_INSTANT_CANCEL;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_POPUP_TITLE_INSTANT_CANCEL;
import static com.tokopedia.buyerorder.common.util.BuyerUtils.formatTitleHtml;
import static com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE;
import static com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment.CREATE_REVIEW_ERROR_MESSAGE;

public class MarketPlaceDetailFragment extends BaseDaggerFragment implements RefreshHandler.OnRefreshHandlerListener, OrderListDetailContract.View {

    private static final int CREATE_RESCENTER_REQUEST_CODE = 789;

    public static final String KEY_ORDER_ID = "OrderId";
    public static final String ACTION_BUTTON_URL = "action_button_url";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    public static final String KEY_PAYMENT_ID = "PaymentId";
    public static final String KEY_CART_STRING = "CartString";
    public static final String KEY_FROM_PAYMENT = "from_payment";
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";
    public static final String NO_SALIN = "No. Resi";
    public static final String NAMA_TOKO = "Nama Toko";
    public static final String NO_SANIN_NEXT_LINE = "\n\nSalin No. Resi";
    public static final String BELI_LAGI = "Beli Lagi";
    public static final String KEY_TULIS_REVIEW = "give_review";
    public static final String INVOICE_URL = "invoiceUrl";
    public static final String TX_ASK_SELLER = "tx_ask_seller";
    public static final String STATUS_CODE_220 = "220";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_11 = "11";
    private static final String CLICK_REQUEST_CANCEL = "click request cancel";
    private static final String CLICK_TRACK = "click track";
    private static final String CLICK_ASK_SELLER = "click ask seller";
    private static final String CLICK_ASK_SELLER_CANCELATION = "click ask seller - cancelation";
    private static final String CLICK_KEMBALI = "click kembali - cancelation";
    private static final String CLICK_SUBMIT_CANCELATION = "click submit cancelation";
    private static final String CLICK_VIEW_COMPLAIN = "click view complain";
    private static final String TOTAL_SHIPPING_PRICE = "Total Ongkos Kirim";
    private static final String CLICK_LIHAT_PRODUK_SERUPA_LEVEL_ORDER = "click lihat produk serupa - order";

    public static final String SIMILAR_PRODUCTS_ACTION_BUTTON_KEY = "see_similar_products";
    public static final String WAITING_INVOICE_STATUS_TEXT = "Menunggu Invoice";

    public static final int REQUEST_CANCEL_ORDER = 101;
    public static final int INSTANT_CANCEL_BUYER_REQUEST = 100;
    public static final int CANCEL_ORDER_DISABLE = 102;
    public static final int TEXT_SIZE_MEDIUM = 12;
    public static final int TEXT_SIZE_LARGE = 14;
    public static final int REQUEST_CODE_WRITE_REVIEW = 103;

    @Inject
    OrderListDetailPresenter presenter;
    @Inject
    UserSessionInterface userSessionInterface;
    private LinearLayout mainView;
    private LinearLayout viewRecomendItems;
    private TextView statusLabel;
    private TextView statusValue;
    private TextView conditionalInfoText;
    private TextView invoiceView;
    private ImageView invoiceCopy;
    private View dividerInvoice;
    private RelativeLayout invoiceLayout;
    private TextView lihat;
    private TextView detailLabel;
    private TextView additionalText;
    private TextView infoLabel;
    private TextView helpLabel;
    private TextView recommendListTitle;
    private FrameLayout stickyButton;
    private LinearLayout statusDetail;
    private LinearLayout detailContent;
    private LinearLayout additionalInfoLayout;
    private LinearLayout infoValue;
    private LinearLayout totalPrice;
    private LinearLayout actionBtnLayout;
    private LinearLayout paymentMethod;
    private TextView primaryActionBtn;
    private TextView secondaryActionBtn;
    private FrameLayout parentLayout;
    private NestedScrollView nestedScrollView;
    private RecyclerView itemsRecyclerView;
    private TextView productInformationTitle;
    private TextView shopInformationTitle;
    private RelativeLayout rlShopInfo;
    private ImageView ivShopInfo;
    private boolean isSingleButton;
    private ClipboardManager myClipboard;
    private CardView driverLayout, dropShipperLayout;
    private TextView statusLihat;
    private FrameLayout progressBarLayout;
    private ClipData myClip;
    private SwipeToRefresh swipeToRefresh;
    private RefreshHandler refreshHandler;
    @Inject
    OrderListAnalytics orderListAnalytics;
    private ShopInfo shopInfo;
    private Status status;
    private Ticker mTickerInfos;
    private Ticker mTickerCancellationInfo;
    private RecyclerView recommendationList;
    private List<Items> listProducts;
    private String invoiceNum;
    private String invoiceUrl;
    private String boughtDate;
    private Boolean isRequestedCancel;
    private String _helplink;
    private View dividerDiscount;
    private LinearLayout llDiscount;
    private OrderDetails orderDetails;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(OrderDetailsComponent.class).inject(this);
    }

    public static Fragment getInstance(String orderId, String orderCategory, String paymentId, String cartString) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        bundle.putString(KEY_ORDER_CATEGORY, orderCategory);
        bundle.putString(KEY_PAYMENT_ID, paymentId);
        bundle.putString(KEY_CART_STRING, cartString);
        Fragment fragment = new MarketPlaceDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketplace_order_list_detail, container, false);
        parentLayout = view.findViewById(R.id.parentLayout);
        mainView = view.findViewById(R.id.main_view);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        statusLabel = view.findViewById(R.id.status_label);
        statusValue = view.findViewById(R.id.status_value);
        conditionalInfoText = view.findViewById(R.id.conditional_info);
        statusDetail = view.findViewById(R.id.status_detail);
        invoiceView = view.findViewById(R.id.invoice);
        invoiceCopy = view.findViewById(R.id.iv_copy_invoice);
        dividerInvoice = view.findViewById(R.id.divider_invoice);
        invoiceLayout = view.findViewById(R.id.rl_invoice);
        statusLihat = view.findViewById(R.id.lihat_status);
        lihat = view.findViewById(R.id.lihat);
        detailLabel = view.findViewById(R.id.detail_label);
        detailContent = view.findViewById(R.id.detail_content);
        additionalText = view.findViewById(R.id.additional);
        additionalInfoLayout = view.findViewById(R.id.additional_info);
        mTickerInfos = view.findViewById(R.id.additional_ticker_info);
        mTickerCancellationInfo = view.findViewById(R.id.ticker_info_cancellation);
        infoLabel = view.findViewById(R.id.info_label);
        infoValue = view.findViewById(R.id.info_value);
        totalPrice = view.findViewById(R.id.total_price);
        helpLabel = view.findViewById(R.id.help_label);
        actionBtnLayout = view.findViewById(R.id.actionBtnLayout);
        primaryActionBtn = view.findViewById(R.id.langannan);
        secondaryActionBtn = view.findViewById(R.id.beli_lagi);
        itemsRecyclerView = view.findViewById(R.id.rv_items);
        productInformationTitle = view.findViewById(R.id.product_info_label);
        shopInformationTitle = view.findViewById(R.id.shop_info_label);
        rlShopInfo = view.findViewById(R.id.rl_shop_info);
        ivShopInfo = view.findViewById(R.id.iv_shop_info);
        paymentMethod = view.findViewById(R.id.info_payment_method);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        stickyButton = view.findViewById(R.id.sticky_btn);
        if (getContext() != null) {
            myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        }
        recommendationList = view.findViewById(R.id.recommendation_list);
        recommendListTitle = view.findViewById(R.id.recommend_title);
        viewRecomendItems = view.findViewById(R.id.recommend_items);
        dividerDiscount = view.findViewById(R.id.divider_discount);
        llDiscount = view.findViewById(R.id.ll_info_discount);
        setMainViewVisible(View.GONE);
        itemsRecyclerView.setNestedScrollingEnabled(false);
        setUpScrollChangeListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            refreshHandler = new RefreshHandler(getActivity(), getView().findViewById(R.id.swipe_refresh_layout), this);
            refreshHandler.startRefresh();
            refreshHandler.setPullEnabled(true);
        }
    }

    @Override
    public void setDetailsData(OrderDetails details) {
        hideProgressBar();
        this.orderDetails = details;
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
            if (i == 2) {
                if (details.getDriverDetails() != null) {
                    showDriverInfo(details.getDriverDetails());
                }
            }
            if (i == details.detail().size() - 1) {
                if (!TextUtils.isEmpty(details.getDropShipper().getDropShipperName()) && !TextUtils.isEmpty(details.getDropShipper().getDropShipperPhone())) {
                    showDropshipperInfo(details.getDropShipper());
                }
            }
            setDetail(details.detail().get(i));
        }

        if (details.getRequestCancelInfo() != null && details.getRequestCancelInfo().getIsRequestedCancel() != null) {
            setIsRequestedCancel(details.getRequestCancelInfo().getIsRequestedCancel());
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

        if (details.discounts() != null && details.discounts().size() > 0) {
            setDiscountVisibility(View.VISIBLE);
            for (Discount discount : details.discounts()) {
                setDiscount(discount);
            }
        } else {
            setDiscountVisibility(View.GONE);
        }

        setPaymentData(details.paymentData());
        setContactUs(details.contactUs(), details.getHelpLink());

        setActionButtons(details.actionButtons());
        setMainViewVisible(View.VISIBLE);
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
        statusLabel.setText(status.statusLabel());
        statusValue.setText(status.statusText());
        if (!status.textColor().equals(""))
            statusValue.setTextColor(Color.parseColor(status.textColor()));

        if (status.statusText().equalsIgnoreCase(WAITING_INVOICE_STATUS_TEXT)) {
            statusLihat.setVisibility(View.GONE);
        } else {
            statusLihat.setVisibility(View.VISIBLE);
            statusLihat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderListAnalytics.sendLihatStatusClick(status.status());

                    if (getArguments() != null) {
                        startActivity(RouteManager.getIntent(getActivity(), ApplinkConstInternalOrder.TRACK, "")
                                .putExtra(EXTRA_ORDER_ID, getArguments().getString(KEY_ORDER_ID))
                                .putExtra(EXTRA_USER_MODE, 1));
                    }
                }
            });
        }
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
        conditionalInfoText.setText(conditionalInfo.text());
        if (!TextUtils.isEmpty(conditionalInfo.color().textColor())) {
            conditionalInfoText.setTextColor(Color.parseColor(conditionalInfo.color().textColor()));
        }

    }

    @SuppressLint("ResourceType")
    @Override
    public void setTitle(Title title) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(title.label());
        doubleTextView.setBottomText(title.value());
        doubleTextView.setBottomGravity(Gravity.END);
        if (!TextUtils.isEmpty(title.textColor())) {
            doubleTextView.setBottomTextColor(Color.parseColor(title.textColor()));
        } else {
            doubleTextView.setBottomTextColor(Color.parseColor(getResources().getString(com.tokopedia.unifyprinciples.R.color.Unify_N700_44)));
        }
        if (title.backgroundColor() != null && !title.backgroundColor().isEmpty() && getContext() != null) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.background_deadline_buyer);
            doubleTextView.setBottomTextBackground(drawable);
            doubleTextView.setBottomTextRightPadding(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_20), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_10), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_20), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_10));

            doubleTextView.setBottomTextBackgroundColor(Color.parseColor(title.backgroundColor()));
        }
        statusDetail.addView(doubleTextView);
    }

    @Override
    public void setInvoice(final Invoice invoice) {
        if (!invoice.invoiceRefNum().isEmpty()) {
            invoiceNum = invoice.invoiceRefNum();
            invoiceUrl = invoice.invoiceUrl();
            dividerInvoice.setVisibility(View.VISIBLE);
            invoiceLayout.setVisibility(View.VISIBLE);
            invoiceView.setText(invoice.invoiceRefNum());
            invoiceCopy.setOnClickListener(view -> {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.invoice_label), invoice.invoiceRefNum());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }
                Toaster.build(view, getString(R.string.invoice_copied), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, "", v -> {
                }).show();
            });
            if (!BuyerUtils.isValidUrl(invoice.invoiceUrl())) {
                lihat.setVisibility(View.GONE);
            }
            lihat.setOnClickListener(view -> {
                orderListAnalytics.sendViewInvoiceClickEvent();
                orderListAnalytics.sendLihatInvoiceClick(status.status());

                if (getContext() != null) {
                    Intent intent = SeeInvoiceActivity.newInstance(getContext(), status, invoice,
                            invoiceNum, boughtDate, getString(R.string.title_invoice), OrderCategory.MARKETPLACE);
                    startActivity(intent);
                }
            });
        } else {
            dividerInvoice.setVisibility(View.GONE);
            invoiceLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {
        if (getContext() != null) {
            detailLabel.setText(getContext().getResources().getString(R.string.detail_product));
        }
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        if (!detail.label().equalsIgnoreCase(NAMA_TOKO)) {
            if (!detail.label().equalsIgnoreCase(NO_SALIN)) {
                doubleTextView.setTopText(detail.label());
                doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
                doubleTextView.setBottomText(detail.value());
                doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                doubleTextView.setBottomTextStyle("bold");
                doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
            } else {
                doubleTextView.setTopText(detail.label());
                String text = detail.value() + NO_SANIN_NEXT_LINE;
                SpannableString spannableString = new SpannableString(text);
                doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
                int startIndexOfLink = text.indexOf("Salin");
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        try {
                            myClip = ClipData.newPlainText("text", detail.value());
                            myClipboard.setPrimaryClip(myClip);
                            if (getView() != null && getContext() != null) {
                                Toaster.build(getView(), getContext().getResources().getString(R.string.awb_number_copied),
                                        Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.design.R.string.close), v -> {
                                        }).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)); // specific color for this link
                    }
                }, startIndexOfLink, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                doubleTextView.setBottomText(spannableString);
            }

            detailContent.addView(doubleTextView);
        }
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
        if (getContext() != null && tickerInfos.size() > 0) {
            mTickerInfos.setTickerTitle(tickerInfos.get(0).getTitle());
            mTickerInfos.setHtmlDescription(tickerInfos.get(0).getNotes());
            mTickerInfos.setVisibility(View.VISIBLE);
            if (url != null) {
                mTickerInfos.setDescriptionClickEvent(new TickerCallback() {
                    @Override
                    public void onDescriptionViewClick(CharSequence charSequence) {
                        RouteManager.route(getContext(),
                                String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        }
    }

    @Override
    public void setTickerInfo(TickerInfo tickerInfo) {
        if (!tickerInfo.getText().isEmpty()) {
            mTickerCancellationInfo.setVisibility(View.VISIBLE);
            mTickerCancellationInfo.setTextDescription(tickerInfo.getText());
            mTickerCancellationInfo.setTickerShape(Ticker.SHAPE_LOOSE);
            mTickerCancellationInfo.setTickerType(BuyerUtils.getTickerType(tickerInfo.getType()));
        }
    }

    @Override
    public void setPricing(Pricing pricing) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(pricing.label());
        doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
        doubleTextView.setTopTextSize(TEXT_SIZE_MEDIUM);
        if (pricing.label().contains(TOTAL_SHIPPING_PRICE) && pricing.value().contains("Rp 0"))
            doubleTextView.setBottomText(getResources().getString(R.string.tkpdtransaction_bebas_ongkir));
        else
            doubleTextView.setBottomText(pricing.value());
        doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomGravity(Gravity.END);
        infoValue.addView(doubleTextView);
    }

    @Override
    public void setDiscount(Discount discount) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(discount.getLabel());
        doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
        doubleTextView.setTopTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomText(discount.getValue());
        doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomGravity(Gravity.END);
        llDiscount.addView(doubleTextView);
    }

    @Override
    public void setDiscountVisibility(int visibility) {
        dividerDiscount.setVisibility(visibility);
        llDiscount.setVisibility(visibility);
    }

    @Override
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setTopTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        doubleTextView.setBottomGravity(Gravity.END);
        paymentMethod.addView(doubleTextView);
    }

    @Override
    public void setButtonMargin() {
        this.isSingleButton = true;
    }

    @Override
    public void showDropshipperInfo(DropShipper dropShipper) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dropshipper_info, null);
        TextView dropShipperName = view.findViewById(R.id.dropShipper_name);
        TextView dropShipperNumber = view.findViewById(R.id.dropShipper_phone);
        dropShipperName.setText(Html.fromHtml(dropShipper.getDropShipperName()));
        dropShipperNumber.setText(dropShipper.getDropShipperPhone());
        detailContent.addView(view);
    }

    @Override
    public void showDriverInfo(DriverDetails driverDetails) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.driver_info, null);
        driverLayout = view.findViewById(R.id.driverLayout);
        ImageView driverImage = view.findViewById(R.id.driver_img);
        TextView driverName = view.findViewById(R.id.driver_name);
        TextView driverNum = view.findViewById(R.id.driver_phone);
        TextView driverlicense = view.findViewById(R.id.driver_license);
        Button callDriver = view.findViewById(R.id.call_driver);

        if (driverDetails != null) {
            if (!TextUtils.isEmpty(driverDetails.getPhotoUrl())) {
                ImageHandler.loadImageCircle2(getContext(), driverImage, driverDetails.getPhotoUrl());
                driverLayout.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(driverDetails.getDriverName())) {
                driverName.setText(Html.fromHtml(driverDetails.getDriverName()));
                driverLayout.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(driverDetails.getDriverPhone())) {
                driverNum.setText(driverDetails.getDriverPhone());
                driverLayout.setVisibility(View.VISIBLE);
                callDriver.setVisibility(View.VISIBLE);
                callDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialCaller(driverDetails.getDriverPhone());
                    }
                });
            }
            if (!TextUtils.isEmpty(driverDetails.getLicenseNumber())) {
                driverlicense.setText(driverDetails.getLicenseNumber());
                driverLayout.setVisibility(View.VISIBLE);
            }
            detailContent.addView(view);
        }
    }

    @Override
    public void showProgressBar() {
        if (!refreshHandler.isRefreshing()) {
            refreshHandler.setRefreshing(true);
            refreshHandler.setPullEnabled(false);
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        swipeToRefresh.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        progressBarLayout.setVisibility(View.GONE);
        swipeToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message,
                    Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, "", v -> {
                    }).show();
        }
    }

    @Override
    public void showSuccessMessageWithAction(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL,
                    getString(R.string.bom_check_cart), v -> RouteManager.route(getContext(), ApplinkConst.CART)).show();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        if (getView() != null) {
            Toaster.build(getView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, "", v -> {
            }).show();
        }
    }

    @Override
    public void clearDynamicViews() {
        statusDetail.removeAllViews();
        detailContent.removeAllViews();
        additionalInfoLayout.removeAllViews();
        infoValue.removeAllViews();
        totalPrice.removeAllViews();
        actionBtnLayout.removeAllViews();
        paymentMethod.removeAllViews();
        llDiscount.removeAllViews();
    }

    @Override
    public void setActionButtons(List<ActionButton> actionButtons) {
        actionBtnLayout.removeAllViews();
        actionBtnLayout.setOrientation(LinearLayout.VERTICAL);
        boolean stickyButtonAdded = false;
        for (ActionButton actionButton : actionButtons) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16));
            Typography textView = new Typography(getContext());
            textView.setText(actionButton.getLabel());
            textView.setPadding(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(Gravity.CENTER);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_4));
            if (!actionButton.getActionColor().getBackground().equals("")) {
                shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
            }
            if (!actionButton.getActionColor().getBorder().equals("")) {
                shape.setStroke(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
            }
            textView.setBackground(shape);
            textView.setLayoutParams(params);
            if (!actionButton.getActionColor().getTextColor().equals("")) {
                textView.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
            }
            if (actionButton.getLabel().equalsIgnoreCase(BELI_LAGI)) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null && getContext().getResources() != null) {
                            presenter.onBuyAgainItems(
                                    GraphqlHelper.loadRawString(getContext().getResources(),
                                            com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi),
                                    orderDetails.getItems(), " - order", status.status());
                        }
                    }
                });
            } else {
                if (!TextUtils.isEmpty(actionButton.getUri())) {
                    textView.setOnClickListener(clickActionButton(actionButton));
                }
            }
            actionBtnLayout.addView(textView);
            if (!stickyButtonAdded) {
                //Cant add the same textview as it has a parent already so making a new instance of the textview and adding it for the sticky
                Typography stickyTextView = new Typography(getContext());
                stickyTextView.setText(actionButton.getLabel());
                stickyTextView.setPadding(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16), getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16));
                stickyTextView.setTypeface(Typeface.DEFAULT_BOLD);
                stickyTextView.setGravity(Gravity.CENTER);
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_4));
                if (!actionButton.getActionColor().getBackground().equals("")) {
                    shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
                }
                if (!actionButton.getActionColor().getBorder().equals("")) {
                    shape.setStroke(getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
                }
                LinearLayout.LayoutParams stickyButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                stickyTextView.setBackground(shape);
                stickyTextView.setLayoutParams(stickyButtonParams);
                if (!actionButton.getActionColor().getTextColor().equals("")) {
                    stickyTextView.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
                }
                if (actionButton.getLabel().equalsIgnoreCase(BELI_LAGI)) {
                    stickyTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getContext() != null && getContext().getResources() != null) {
                                presenter.onBuyAgainItems(GraphqlHelper.loadRawString(getContext().getResources(),
                                        com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi),
                                        orderDetails.getItems(), " - order", status.status());
                            }
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(actionButton.getUri())) {
                        stickyTextView.setOnClickListener(clickActionButton(actionButton));
                    }
                }
                stickyButton.addView(stickyTextView);
                stickyButtonAdded = true;
            }
        }
    }

    @Override
    public void hitAnalyticsBuyAgain(List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct> listAtcProducts, Boolean isAtcMultiSuccess) {
        orderListAnalytics.sendBuyAgainEvent(orderDetails.getItems(), orderDetails.getShopInfo(), listAtcProducts, isAtcMultiSuccess, true, " - order", status.status());
    }

    @Override
    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    @Override
    public void setBoughtDate(String boughtDate) {
        this.boughtDate = boughtDate;
    }

    @Override
    public void setIsRequestedCancel(Boolean isRequestedCancel) {
        this.isRequestedCancel = isRequestedCancel;
    }

    @Override
    public void showReplacementView(List<String> reasons) {
    }

    @Override
    public void finishOrderDetail() {
        refreshHandler.startRefresh();
    }

    private View.OnClickListener clickActionButton(ActionButton actionButton) {
        return view -> {
            if (actionButton.getActionButtonPopUp() != null && !TextUtils.isEmpty(actionButton.getActionButtonPopUp().getTitle())) {
                if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Tanya Penjual")) {
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_REQUEST_CANCEL, this.status.status());
                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Komplain")) {
                    orderListAnalytics.sendActionButtonClickEvent("click complain");
                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("selesai")) {
                    orderListAnalytics.sendActionButtonClickEvent("selesai");
                }

                if (actionButton.getKey().equalsIgnoreCase(BuyerConsts.KEY_REQUEST_CANCEL)) {
                    Intent buyerReqCancelIntent = new Intent(getContext(), BuyerRequestCancelActivity.class);
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_NAME, shopInfo.getShopName());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE, invoiceNum);
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_LIST_PRODUCT, (Serializable) listProducts);
                    if (getArguments() != null) {
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_ORDER_ID, getArguments().getString(KEY_ORDER_ID));
                    }
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_URI, actionButton.getUri());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, isRequestedCancel);
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getTitle());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getBody());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_ID, shopInfo.getShopId());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BOUGHT_DATE, boughtDate);
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE_URL, invoiceUrl);
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_ID, status.status());
                    buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_INFO, status.statusText());
                    startActivityForResult(buyerReqCancelIntent, REQUEST_CANCEL_ORDER);

                } else {
                    // dialog finish order --> now bottomsheet
                    if (!TextUtils.isEmpty(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri())) {
                        if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Selesai") && getArguments() != null) {
                            BottomSheetUnify bottomSheetFinishOrder = new BottomSheetUnify();
                            View bottomSheetView = View.inflate(getContext(), R.layout.bottomsheet_finish_order_uoh, null);

                            IconUnify iconFinish1 = bottomSheetView.findViewById(R.id.ic_finish_detail_1);
                            iconFinish1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_bound_icon));

                            IconUnify iconFinish2 = bottomSheetView.findViewById(R.id.ic_finish_detail_2);
                            iconFinish2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_bound_icon));

                            UnifyButton finishBtn = bottomSheetView.findViewById(R.id.btn_finish_order);
                            finishBtn.setOnClickListener(view13 -> {
                                String actionStatus = "";
                                if (!status.status().isEmpty() && Integer.parseInt(status.status()) < 600) {
                                    actionStatus = ACTION_FINISH_ORDER;
                                }

                                presenter.finishOrderGql(
                                        GraphqlHelper.loadRawString(getView().getResources(), R.raw.uoh_finish_order),
                                        getArguments().getString(KEY_ORDER_ID),
                                        actionStatus,
                                        userSessionInterface.getUserId());
                                bottomSheetFinishOrder.dismiss();

                            });

                            UnifyButton komplainBtn = bottomSheetView.findViewById(R.id.btn_ajukan_komplain);
                            komplainBtn.setOnClickListener(view14 -> {
                                Intent newIntent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.WEBVIEW, String.format(TokopediaUrl.Companion.getInstance().getMOBILEWEB() + ApplinkConst.ResCenter.RESO_CREATE, getArguments().getString(KEY_ORDER_ID)));
                                startActivityForResult(newIntent, CREATE_RESCENTER_REQUEST_CODE);
                                bottomSheetFinishOrder.dismiss();
                            });

                            bottomSheetFinishOrder.setChild(bottomSheetView);
                            bottomSheetFinishOrder.setTitle(FINISH_ORDER_BOTTOMSHEET_TITLE);
                            bottomSheetFinishOrder.setCloseClickListener(view8 -> {
                                bottomSheetFinishOrder.dismiss();
                                return Unit.INSTANCE;
                            });
                            bottomSheetFinishOrder.show(getChildFragmentManager(), getString(R.string.show_bottomsheet));

                        } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Komplain") && getArguments() != null) {
                            Intent newIntent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.WEBVIEW, String.format(TokopediaUrl.Companion.getInstance().getMOBILEWEB() + ApplinkConst.ResCenter.RESO_CREATE, getArguments().getString(KEY_ORDER_ID)));
                            startActivityForResult(newIntent, CREATE_RESCENTER_REQUEST_CODE);
                        }
                    }
                }
            } else if (!TextUtils.isEmpty(actionButton.getUri())) {
                if (actionButton.getUri().contains("askseller")) {
                    startSellerAndAddInvoice();
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_ASK_SELLER, statusValue.getText().toString());
                } else if (actionButton.getKey().contains("view_complaint")) {
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_VIEW_COMPLAIN, statusValue.getText().toString());
                    RouteManager.route(getContext(), actionButton.getUri());
                } else if (actionButton.getKey().equalsIgnoreCase(SIMILAR_PRODUCTS_ACTION_BUTTON_KEY)) {

                    String firstProductId = "";
                    if (orderDetails != null && orderDetails.getItems() != null && !orderDetails.getItems().isEmpty()) {
                        firstProductId = String.valueOf(orderDetails.getItems().get(0).getId());
                    }
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_LIHAT_PRODUK_SERUPA_LEVEL_ORDER, firstProductId);
                    RouteManager.route(getContext(), actionButton.getUri());
                } else if (actionButton.getKey().equalsIgnoreCase(KEY_TULIS_REVIEW)) {
                    orderListAnalytics.sendTulisReviewEventData(status.status());
                    goToReview(actionButton.getUri());
                } else if (!TextUtils.isEmpty(actionButton.getUri())) {
                    if (this.status.status().equals(STATUS_CODE_220) || this.status.status().equals(STATUS_CODE_400)) {
                        Intent buyerReqCancelIntent = new Intent(getContext(), BuyerRequestCancelActivity.class);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_NAME, shopInfo.getShopName());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE, invoiceNum);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_LIST_PRODUCT, (Serializable) listProducts);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_ORDER_ID, getArguments().getString(KEY_ORDER_ID));
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_URI, actionButton.getUri());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, isRequestedCancel);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getTitle());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getBody());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_ID, shopInfo.getShopId());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BOUGHT_DATE, boughtDate);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE_URL, invoiceUrl);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_ID, status.status());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_INFO, status.statusText());
                        startActivityForResult(buyerReqCancelIntent, REQUEST_CANCEL_ORDER);

                        orderListAnalytics.sendActionButtonClickEvent(CLICK_REQUEST_CANCEL, this.status.status());

                    } else if (this.status.status().equals(STATUS_CODE_11)) {
                        Intent buyerReqCancelIntent = new Intent(getContext(), BuyerRequestCancelActivity.class);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_NAME, shopInfo.getShopName());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE, invoiceNum);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_LIST_PRODUCT, (Serializable) listProducts);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_ORDER_ID, getArguments().getString(KEY_ORDER_ID));
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_URI, actionButton.getUri());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, isRequestedCancel);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getTitle());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, actionButton.getActionButtonPopUp().getBody());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_SHOP_ID, shopInfo.getShopId());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_BOUGHT_DATE, boughtDate);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_INVOICE_URL, invoiceUrl);
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_ID, status.status());
                        buyerReqCancelIntent.putExtra(BuyerConsts.PARAM_STATUS_INFO, status.statusText());
                        startActivityForResult(buyerReqCancelIntent, REQUEST_CANCEL_ORDER);
                    } else if (actionButton.getLabel().equalsIgnoreCase("Lacak")) {

                        orderListAnalytics.sendActionButtonClickEvent(CLICK_TRACK);

                        String routingAppLink;
                        routingAppLink = ApplinkConst.ORDER_TRACKING.replace("{order_id}", getArguments().getString(KEY_ORDER_ID));

                        String trackingUrl;
                        Uri uri = Uri.parse(actionButton.getUri());
                        trackingUrl = uri.getQueryParameter("url");

                        Uri.Builder uriBuilder = new Uri.Builder();
                        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl);
                        routingAppLink += uriBuilder.toString();
                        RouteManager.route(getContext(), routingAppLink);
                    } else {
                        RouteManager.route(getContext(), actionButton.getUri());
                    }
                } else {
                    RouteManager.route(getContext(), actionButton.getUri());
                }
            }
        };
    }

    private void startSellerAndAddInvoice() {
        if (shopInfo != null) {
            String shopId = String.valueOf(this.shopInfo.getShopId());
            String applink = "tokopedia://topchat/askseller/" + shopId;
            Intent intent = RouteManager.getIntent(getContext(), applink);
            if (orderDetails != null) {
                intent.putExtra(ApplinkConst.Chat.INVOICE_ID, orderDetails.getInvoiceId());
                intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, orderDetails.getInvoiceCode());
                intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, orderDetails.getProductName());
                intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, orderDetails.getBoughtDate());
                intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, orderDetails.getProductImageUrl());
                intent.putExtra(ApplinkConst.Chat.INVOICE_URL, orderDetails.getInvoiceUrl());
                intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, orderDetails.getStatusId());
                intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, orderDetails.getStatusInfo());
                intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, orderDetails.getTotalPriceAmount());
            }
            intent.putExtra(ApplinkConst.Chat.SOURCE, TX_ASK_SELLER);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CANCEL_ORDER) {
            String reason = "";
            if (resultCode == INSTANT_CANCEL_BUYER_REQUEST) {
                String resultMsg = data.getStringExtra(RESULT_MSG_INSTANT_CANCEL);
                int result = data.getIntExtra(RESULT_CODE_INSTANT_CANCEL, 1);
                if (result == 1) {
                    if (resultMsg != null && getView() != null) {
                        Toaster.build(getView(), resultMsg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, "", v -> {
                        }).show();
                    }
                } else if (result == 3) {
                    String popupTitle = data.getStringExtra(RESULT_POPUP_TITLE_INSTANT_CANCEL);
                    String popupBody = data.getStringExtra(RESULT_POPUP_BODY_INSTANT_CANCEL);
                    if (getContext() != null && popupTitle != null && popupBody != null) {
                        DialogUnify dialogUnify = new DialogUnify(getContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE);
                        dialogUnify.setTitle(popupTitle);
                        dialogUnify.setDescription(popupBody);
                        dialogUnify.setPrimaryCTAText(getString(R.string.mengerti_button));
                        dialogUnify.setPrimaryCTAClickListener(() -> {
                            dialogUnify.dismiss();
                            return Unit.INSTANCE;
                        });
                        dialogUnify.setSecondaryCTAText(getString(R.string.pusat_bantuan_button));
                        dialogUnify.setSecondaryCTAClickListener(() -> {
                            dialogUnify.dismiss();
                            if (!_helplink.isEmpty()) {
                                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, _helplink);
                            }
                            return Unit.INSTANCE;
                        });
                        dialogUnify.show();
                    }
                }
                if (result != 0) {
                    finishOrderDetail();
                }
            } else if (resultCode == CANCEL_ORDER_DISABLE) {
                finishOrderDetail();
            }
            orderListAnalytics.sendActionButtonClickEvent(CLICK_SUBMIT_CANCELATION, statusValue.getText().toString() + "-" + reason);
        } else if (requestCode == REQUEST_CODE_WRITE_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateReview();
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                String errorMessage = data.getStringExtra(CREATE_REVIEW_ERROR_MESSAGE);
                if(errorMessage == null) {
                    onFailCreateReview(getString(R.string.uoh_review_create_invalid_to_review));
                } else {
                    onFailCreateReview(errorMessage);
                }
            }
        }
    }

    void openDialCaller(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setTopTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        doubleTextView.setTopTextSize(TEXT_SIZE_LARGE);
        doubleTextView.setTopTextStyle("bold");
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals(""))
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        doubleTextView.setBottomTextSize(TEXT_SIZE_LARGE);
        doubleTextView.setBottomTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_Y500));
        doubleTextView.setBottomGravity(Gravity.END);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs, String helpLink) {
        _helplink = helpLink;
        String text = getResources().getString(R.string.contact_us_text);
        String clickableLink = getResources().getString(R.string.contact_us_clickable_text);
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf(clickableLink);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                orderListAnalytics.sendHelpEventData(status.status());
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, helpLink);
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
            primaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getUri()));
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
            secondaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getUri()));
        }
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return view -> {
            if (uri != null && !uri.equals("")) {
                RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, uri);
            }
        };
    }

    @Override
    public void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility) {
        primaryActionBtn.setVisibility(topBtnVisibility);
        secondaryActionBtn.setVisibility(bottomBtnVisibility);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setItems(List<Items> items, boolean isTradeIn, OrderDetails orderDetails) {
        listProducts = items;
        rlShopInfo.setVisibility(View.VISIBLE);
        String labelShop = shopInformationTitle.getContext().getResources().getString(R.string.label_shop_title) + " ";
        int startLabelShop = labelShop.length();
        String shopName = shopInfo.getShopName();

        SpannableStringBuilder completeLabelShop = new SpannableStringBuilder();
        completeLabelShop.append(labelShop);
        completeLabelShop.append(MethodChecker.fromHtml(shopName));
        completeLabelShop.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLabelShop, completeLabelShop.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        shopInformationTitle.setText(completeLabelShop);

        if (getContext() != null) {
            ivShopInfo.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_right));
        }

        rlShopInfo.setOnClickListener(v -> {
            orderListAnalytics.sendClickShopName(status.status());
            String applink = ApplinkConst.SHOP.replace("{shop_id}", String.valueOf(shopInfo.getShopId()));
            RouteManager.route(getContext(), applink);
        });
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerView.setAdapter(new ProductItemAdapter(getContext(), items, presenter, isTradeIn, status, userSessionInterface.getUserId(), getArguments().getString(KEY_ORDER_ID)));
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setMainViewVisible(int visibility) {
        swipeToRefresh.setVisibility(visibility);
    }

    @Override
    public void onRefresh(View view) {
        if (getArguments() != null) {
            presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT), null, getArguments().getString(KEY_PAYMENT_ID), getArguments().getString(KEY_CART_STRING));
        }
    }

    private void setUpScrollChangeListener() {
        Rect scrollBounds = new Rect();
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, scrollX, scrollY, scrollOldX, scrollOldY) -> {
            nestedScrollView.getHitRect(scrollBounds);
            if (actionBtnLayout.getLocalVisibleRect(scrollBounds)) {
                // Any portion of the sticky button, even a single pixel, is within the visible window
                stickyButton.setVisibility(View.GONE);
            } else {
                // NONE of the sticky button is within the visible window
                stickyButton.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void setRecommendation(Object recommendationResponse) {
        RecommendationResponse rechargeWidgetResponse = (RecommendationResponse) recommendationResponse;
        if (rechargeWidgetResponse != null && rechargeWidgetResponse.getRechargeFavoriteRecommendationList() != null
                && rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getRecommendations() != null) {
            if (!rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getRecommendations().isEmpty() && getContext() != null) {
                viewRecomendItems.setVisibility(View.VISIBLE);
                recommendListTitle.setText(rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getTitle());
                recommendationList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recommendationList.setAdapter(new RecommendationMPAdapter(rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getRecommendations()));
            }
        }
    }

    @Override
    public JsonArray generateInputQueryBuyAgain(List<Items> items) {
        JsonArray jsonArray = new JsonArray();
        for (Items item : items) {
            JsonObject passenger = new JsonObject();

            String productId = "";
            int quantity = 0;
            int shopId = 0;
            String notes = "";
            String price = "";
            String category = "";
            String productName = "";
            try {
                productId = item.getId();
                quantity = item.getQuantity();
                shopId = shopInfo.getShopId();
                notes = item.getDescription();
                price = item.getPrice();
                category = item.getCategory();
                productName = item.getTitle();
            } catch (Exception e) {
                Timber.d("error parse - %s", e.getMessage());
            }
            passenger.addProperty(BuyerConsts.PRODUCT_ID, productId);
            passenger.addProperty(BuyerConsts.QUANTITY, quantity);
            passenger.addProperty(BuyerConsts.NOTES, notes);
            passenger.addProperty(BuyerConsts.SHOP_ID, shopId);
            passenger.addProperty(BuyerConsts.PRODUCT_PRICE, price);
            passenger.addProperty(BuyerConsts.CATEGORY, category);
            passenger.addProperty(BuyerConsts.PRODUCT_NAME, productName);
            jsonArray.add(passenger);
        }
        return jsonArray;
    }

    @Override
    public void setActionButtonLayoutClickable(Boolean isClickable) {
        // no op
    }

    @Override
    public void setActionButtonText(String txt) {
        // no op
    }

    private void goToReview(String applink) {
        Intent intent = RouteManager.getIntent(getContext(), applink);
        startActivityForResult(intent, REQUEST_CODE_WRITE_REVIEW);
    }

    private void onSuccessCreateReview() {
        if (getContext() != null && getView() != null) {
            Toaster.build(getView(), getString(R.string.uoh_review_create_success_toaster, userSessionInterface.getName()), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.uoh_review_oke), v -> {}).show();
            refreshHandler.startRefresh();
        }
    }

    private void onFailCreateReview(String errorMessage) {
        if(getView() != null) {
            Toaster.build(getView(), errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.uoh_review_oke), v -> {}).show();
        }
    }
}
