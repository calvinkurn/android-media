package com.tokopedia.buyerorder.detail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.ApplinkOMSConstant;
import com.tokopedia.buyerorder.common.util.BuyerUtils;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.EntityAddress;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.MetaDataInfo;
import com.tokopedia.buyerorder.detail.data.OrderDetails;
import com.tokopedia.buyerorder.detail.data.Title;
import com.tokopedia.buyerorder.detail.view.activity.OrderListwebViewActivity;
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView;
import com.tokopedia.buyerorder.detail.view.customview.CustomTicketView;
import com.tokopedia.buyerorder.detail.view.customview.RedeemVoucherView;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract;
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.utils.view.DoubleTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OrderListDetailContract.ActionInterface, RedeemVoucherView.SetTapActionDeals {

    public static final String KEY_BUTTON = "button";
    public static final String KEY_REFRESH = "refresh";
    public static final String KEY_TEXT = "text";
    public static final String KEY_VOUCHER_CODE = "vouchercodes";
    public static final String KEY_REDIRECT = "redirect";
    public static final String CONTENT_TYPE = "application/pdf";
    public static final String KEY_QRCODE = "qrcode";
    public static final String KEY_POPUP = "popup";
    public static final String KEY_RETRY = "Cek Ulang";
    public static final int DEALS_CATEGORY_ID = 35;
    private static final int EVENTS_CATEGORY_ID_1 = 32;
    private static final int EVENTS_CATEGORY_ID_2 = 23;
    private static final int EVENTS_CATEGORY_INSURANCE = 1301;
    private boolean isShortLayout;
    private List<Items> itemsList;
    private Context context;
    public static final int ITEM_DEALS = 1;
    public static final int ITEM_DEALS_SHORT = 2;
    public static final int ITEM_EVENTS = 3;
    private static final int ITEM_DEFAULT = 4;
    private static final int ITEM_INSURANCE = 5;
    OrderListDetailPresenter presenter;
    public static String categoryDeals = "deal";
    public static String categoryEvents = "event";
    private static final String CATEGORY_PRODUCT = "Kategori Produk";
    SetEventDetails setEventDetails;
    private int position;
    private String orderId;
    private PermissionCheckerHelper permissionCheckerHelper;
    public static final int TEXT_SIZE_LARGE = 14;
    private int totalTicketCount;
    private OrderDetails orderDetails;

    public ItemsAdapter(Context context, List<Items> itemsList, boolean isShortLayout, OrderListDetailPresenter presenter, SetEventDetails setEventDetails, String orderId, OrderDetails orderDetails) {
        this.context = context;
        this.itemsList = itemsList;
        this.isShortLayout = isShortLayout;
        this.presenter = presenter;
        this.setEventDetails = setEventDetails;
        this.orderId = orderId;
        this.orderDetails = orderDetails;
    }

    @Override
    public int getItemCount() {
        if (itemsList != null) {
            return itemsList.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM_DEALS:
                v = inflater.inflate(R.layout.voucher_item_card_deals, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_DEALS_SHORT:
                v = inflater.inflate(R.layout.voucher_item_card_deals_short, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_EVENTS:
                v = inflater.inflate(R.layout.voucher_item_card_events, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_INSURANCE:
                v = inflater.inflate(R.layout.voucher_item_insurance, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_DEFAULT:
                v = inflater.inflate(R.layout.voucher_item_default, parent, false);
                holder = new DefaultViewHolder(v, viewType);
                break;
            default:
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.position = position;
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setIndex(position);
            ((ItemViewHolder) holder).bindData(orderDetails,itemsList.get(position), holder.getItemViewType());
        } else {
            ((DefaultViewHolder) holder).setIndex(position);
            ((DefaultViewHolder) holder).bindData(itemsList.get(position), holder.getItemViewType());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsList.get(position).getCategory().equalsIgnoreCase(categoryDeals) || itemsList.get(position).getCategoryID() == DEALS_CATEGORY_ID) {
            if (isShortLayout)
                return ITEM_DEALS_SHORT;
            else
                return ITEM_DEALS;
        } else if (itemsList.get(position).getCategoryID() == EVENTS_CATEGORY_ID_1 || itemsList.get(position).getCategoryID() == EVENTS_CATEGORY_ID_2
                || itemsList.get(position).getCategory().equalsIgnoreCase(OrderCategory.EVENT)) {
            return ITEM_EVENTS;
        } else if (itemsList.get(position).getCategoryID() == EVENTS_CATEGORY_INSURANCE) {
            return ITEM_INSURANCE;
        } else {
            return ITEM_DEFAULT;
        }
    }

    @Override
    public void setActionButton(int position, List<ActionButton> actionButtons) {
        itemsList.get(position).setActionButtons(actionButtons);
        itemsList.get(position).setActionButtonLoaded(true);
        notifyItemChanged(position);
    }

    @Override
    public void setTapActionButton(int position, List<ActionButton> actionButtons) {
        itemsList.get(position).setTapActions(actionButtons);
        itemsList.get(position).setTapActionsLoaded(true);
        notifyItemChanged(position);
    }

    private View.OnClickListener getActionButtonClickListener(final String uri, Boolean isDownloadable, String downloadFileName) {
        if (BuyerUtils.isUridownloadable(uri, isDownloadable)) {
            setEventDetails.askPermission(uri, isDownloadable, downloadFileName);
        } else {
            if (context != null) {
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, uri);
            }
        }
        return view -> presenter.pdfUri = uri;
    }

    @Override
    public void tapActionClicked(TextView view, ActionButton actionButton, Items item, int count, int pos) {
        if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON) || actionButton.getControl().equalsIgnoreCase(KEY_REFRESH)) {
            if (context != null) {
                presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getTapActions(), ItemsAdapter.this, pos, true);
            }
        } else {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (!actionButton.getBody().equals("") && !actionButton.getBody().getAppURL().equals("")) {
                    if (view == null)
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    else {
                        Intent intent = null;
                        try {
                            intent = OrderListwebViewActivity.getWebViewIntent(context, URLDecoder.decode(
                                    actionButton.getBody().getAppURL(), "UTF-8"), "Redeem Voucher");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                    }
                }
            } else if (actionButton.getControl().equalsIgnoreCase(KEY_QRCODE)) {
                setEventDetails.openShowQRFragment(actionButton, item);
            }
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView cityName;
        private TextView validDate;
        private TextView productQuantity;
        private TextView productPrice;
        private ProgressBar progressBar;
        private LinearLayout tapActionLayoutDeals, tapActionLayoutEvents;
        private LinearLayout statusDetail;
        private LinearLayout voucherCodeLayout;
        private CustomTicketView customTicketView;
        private CustomTicketView customTicketView1;
        private View llValid;
        private TextView tvValidTill;
        private TextView tanggalEventsTitle, tanggalEvents, eventCity, eventAddress;
        private int index;


        public ItemViewHolder(View itemView, int itemType) {
            super(itemView);
            this.itemView = itemView;
            if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT || itemType == ITEM_EVENTS) {
                dealImage = itemView.findViewById(R.id.iv_deal);
                dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
                brandName = itemView.findViewById(R.id.tv_brand_name);
                cityName = itemView.findViewById(R.id.tv_redeem_locations);
                tanggalEventsTitle = itemView.findViewById(R.id.tanggal_events_title);
                tanggalEvents = itemView.findViewById(R.id.tanggal_events);
                eventCity = itemView.findViewById(R.id.city_event);
                eventAddress = itemView.findViewById(R.id.address_event);
                voucherCodeLayout = itemView.findViewById(R.id.voucerCodeLayout);
                customTicketView = itemView.findViewById(R.id.customView2);
                customTicketView1 = itemView.findViewById(R.id.customView1);
            }

            if (itemType == ITEM_DEALS || itemType == ITEM_EVENTS) {
                tvValidTill = itemView.findViewById(R.id.tv_valid_till);
                validDate = itemView.findViewById(R.id.tv_valid_till_date);
                tapActionLayoutDeals = itemView.findViewById(R.id.tapAction_deals);
                tapActionLayoutEvents = itemView.findViewById(R.id.tapAction_events);
                llValid = itemView.findViewById(R.id.ll_valid);
            }

            if (itemType == ITEM_INSURANCE) {
                dealImage = itemView.findViewById(R.id.img_product);
                dealsDetails = itemView.findViewById(R.id.txt_product_name);
                brandName = itemView.findViewById(R.id.txt_product_name);
                productQuantity = itemView.findViewById(R.id.txt_quantity);
                productPrice = itemView.findViewById(R.id.txt_item_price);
                statusDetail = itemView.findViewById(R.id.status_detail);

            } else if (itemType != ITEM_INSURANCE) {
                itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                progressBar = itemView.findViewById(R.id.prog_bar);
            }

        }

        private void setProductDetails(Map<String, String> map) {

            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                DoubleTextView doubleTextView = new DoubleTextView(context, LinearLayout.HORIZONTAL);
                doubleTextView.setTopText(entry.getKey());
                doubleTextView.setTopTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
                doubleTextView.setBottomText(entry.getValue());
                doubleTextView.setBottomTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                doubleTextView.setBottomTextStyle("bold");
                doubleTextView.setBottomTextSize(TEXT_SIZE_LARGE);
                statusDetail.addView(doubleTextView);
            }

        }

        @SuppressLint("SetTextI18n")
        void bindData(OrderDetails orderDetails, final Items item, int itemType) {

            MetaDataInfo metaDataInfo = null;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }
            if (metaDataInfo != null) {
                if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityImage())) {
                        ImageHandler.loadImage(context, dealImage, item.getImageUrl(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    } else {
                        ImageHandler.loadImage(context, dealImage, metaDataInfo.getEntityImage(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    }
                    if (TextUtils.isEmpty(metaDataInfo.getEntityProductName())) {
                        dealsDetails.setText(item.getTitle());
                    } else {
                        dealsDetails.setText(metaDataInfo.getEntityProductName());
                    }
                }

                if(itemType == ITEM_EVENTS){
                    if (TextUtils.isEmpty(metaDataInfo.getProductImage())) {
                        ImageHandler.loadImage(context, dealImage, item.getImageUrl(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    } else {
                        ImageHandler.loadImage(context, dealImage, metaDataInfo.getProductImage(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    }
                    if (TextUtils.isEmpty(metaDataInfo.getProductName())) {
                        dealsDetails.setText(item.getTitle());
                    } else {
                        dealsDetails.setText(metaDataInfo.getProductName());
                    }
                }

                if (itemType == ITEM_INSURANCE) {

                    if (TextUtils.isEmpty(metaDataInfo.getProuductImage())) {
                        ImageHandler.loadImage(context, dealImage, item.getImageUrl(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    } else {
                        ImageHandler.loadImage(context, dealImage, metaDataInfo.getProuductImage(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
                    }
                    if (TextUtils.isEmpty(metaDataInfo.getProductName())) {
                        dealsDetails.setText(item.getTitle());
                    } else {

                        dealsDetails.setText(metaDataInfo.getProductName());
                    }

                    setEventDetails.setDetailTitle(context.getResources().getString(R.string.insurance_detail_label));
                    productQuantity.setText(metaDataInfo.getProductQuantity());
                    productPrice.setText(metaDataInfo.getProductPrice());

                    String productCategory = "";
                    if (orderDetails.title() != null) {
                        for (Title title : orderDetails.title()) {
                            if (title.label().equalsIgnoreCase(CATEGORY_PRODUCT)) {
                                productCategory = title.value();
                            }
                        }
                    }

                    Map<String, String> map = new LinkedHashMap<>();
                    map.put(context.getResources().getString(R.string.product_category), productCategory);
                    map.put(context.getResources().getString(R.string.insurance_type), metaDataInfo.getInsuranceType());
                    map.put(context.getResources().getString(R.string.insurance_quantity), metaDataInfo.getProductQuantity());
                    map.put(context.getResources().getString(R.string.insurance_length), metaDataInfo.getInsuranceLength());
                    map.put(context.getResources().getString(R.string.insurance_price), metaDataInfo.getPremiumPrice());
                    setProductDetails(map);
                    setEventDetails.setInsuranceDetail();


                }
                if (itemType == ITEM_DEALS) {
                    setEventDetails.sendThankYouEvent(metaDataInfo, ITEM_DEALS, orderDetails);
                    final MetaDataInfo metaDataInfo1 = metaDataInfo;
                    if (!TextUtils.isEmpty(metaDataInfo.getEndDate())) {
                        validDate.setText(" ".concat(metaDataInfo.getEndDate()));
                        llValid.setVisibility(View.VISIBLE);
                    } else {
                        llValid.setVisibility(View.GONE);
                    }
                    setEventDetails.setDealsBanner(item);
                    if (item.getActionButtons() != null && item.getActionButtons().size() > 0) {
                        setEventDetails.setEventDetails(item.getActionButtons().get(0), item);
                    }
                    brandName.setText(metaDataInfo.getEntityBrandName());
                    setEventDetails.setDetailTitle(context.getResources().getString(R.string.detail_label));
                    customTicketView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RouteManager.route(context, ApplinkOMSConstant.INTERNAL_DEALS + metaDataInfo1.getSeoUrl());
                        }
                    });
                }

                if (itemType == ITEM_EVENTS) {
                    setEventDetails.sendThankYouEvent(metaDataInfo, ITEM_EVENTS, orderDetails);
                    final MetaDataInfo metaDataInfo1 = metaDataInfo;
                    if (!TextUtils.isEmpty(metaDataInfo.getLocationName())) {
                        eventCity.setText(metaDataInfo.getLocationName());
                    }
                    if (!TextUtils.isEmpty(metaDataInfo.getLocationDesc())) {
                        eventAddress.setText(metaDataInfo.getLocationDesc());
                    }

                    if (metaDataInfo.getIsHiburan() == 1) {
                        if (!TextUtils.isEmpty(metaDataInfo.getEndTime())) {
                            tanggalEventsTitle.setVisibility(View.VISIBLE);
                            tanggalEventsTitle.setText(context.getResources().getString(R.string.text_valid_till));
                            tanggalEvents.setText(metaDataInfo.getEndTime());
                        }
                    } else if (metaDataInfo.getIsHiburan() == 0) {
                        if (!TextUtils.isEmpty(metaDataInfo.getEndTime()) && !TextUtils.isEmpty(metaDataInfo.getStartTime())) {
                            tanggalEventsTitle.setVisibility(View.VISIBLE);
                            tanggalEventsTitle.setText(context.getResources().getString(R.string.tanggal_events));
                            tanggalEvents.setText(metaDataInfo.getStartTime() + " - " + metaDataInfo.getEndTime());
                        }
                    }

                    if (!TextUtils.isEmpty(item.getCategory())) {
                        validDate.setText(" ".concat(metaDataInfo.getName()));
                        llValid.setVisibility(View.VISIBLE);
                    } else {
                        llValid.setVisibility(View.GONE);
                    }
                    if (ItemsAdapter.this.orderDetails.actionButtons() != null && ItemsAdapter.this.orderDetails.actionButtons().size() > 0) {
                        setEventDetails.setActionButtonEvent(item, ItemsAdapter.this.orderDetails.actionButtons().get(0), ItemsAdapter.this.orderDetails);
                    }
                    setEventDetails.setPassengerEvent(item);
                    setEventDetails.setDetailTitle(context.getResources().getString(R.string.detail_label_events));
                    customTicketView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RouteManager.route(context, metaDataInfo1.getProductAppUrl());
                        }
                    });
                }
                EntityAddress entityAddress = metaDataInfo.getEntityAddress();
                if (entityAddress != null) {
                    if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT)
                        if (entityAddress.getName() != null) {
                            cityName.setText(entityAddress.getName());
                        }
                }
            }

            if (itemType == ITEM_DEALS) {
                if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.GONE);
                    if (context != null) {
                        presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                    }
                } else if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    if (!TextUtils.isEmpty(item.getTrackingNumber())) {
                        String[] voucherCodes = item.getTrackingNumber().split(",");
                        if (voucherCodes.length > 0) {
                            voucherCodeLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < voucherCodes.length; i++) {
                                BookingCodeView bookingCodeView = new BookingCodeView(context, voucherCodes[i], i, context.getResources().getString(R.string.voucher_code_title), voucherCodes.length);
                                bookingCodeView.setBackground(null);
                                voucherCodeLayout.addView(bookingCodeView);
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        customTicketView.setVisibility(View.GONE);
                        tapActionLayoutDeals.setVisibility(View.GONE);
                    }
                }
                if (item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.removeAllViews();
                    int size = item.getTapActions().size();

                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getTapActions().get(i);
                        if (!actionButton.getControl().equalsIgnoreCase(KEY_TEXT)) {
                            RedeemVoucherView redeemVoucherView;
                            redeemVoucherView = new RedeemVoucherView(context, i, actionButton, item, actionButton.getBody(), presenter, getIndex(), ItemsAdapter.this, setEventDetails);
                            tapActionLayoutDeals.addView(redeemVoucherView);
                        } else {
                            String[] voucherCodes = actionButton.getHeaderObject().getVoucherCodes().split(",");
                            for (int j = 0; j < voucherCodes.length; j++) {
                                BookingCodeView bookingCodeView = new BookingCodeView(context, voucherCodes[j], j, actionButton.getHeaderObject().getItemLabel(), voucherCodes.length);
                                tapActionLayoutDeals.addView(bookingCodeView);
                            }
                        }
                    }
                }

            } else if (itemType == ITEM_EVENTS) {
                if(!item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.VISIBLE);
                    tapActionLayoutEvents.setVisibility(View.VISIBLE);
                    tapActionLayoutEvents.removeAllViews();
                    totalTicketCount = metaDataInfo.getQuantity();
                    int size = item.getActionButtons().size();
                    tapActionLayoutEvents.removeAllViews();
                    if(size==0) setETiket(totalTicketCount);
                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getActionButtons().get(i);
                        TextView tapActionTextView = renderActionButtons(i, actionButton, item);
                        if (actionButton.getControl().equalsIgnoreCase(KEY_REFRESH)) {
                            RedeemVoucherView redeemVoucherView = new RedeemVoucherView(context, i, actionButton, item, actionButton.getBody(), presenter, getIndex(), ItemsAdapter.this, setEventDetails);
                            tapActionLayoutEvents.addView(redeemVoucherView);
                        } else if(actionButton.getControl().equalsIgnoreCase(KEY_VOUCHER_CODE)){
                            if (!actionButton.getBody().getBody().isEmpty()) {
                                String[] voucherCodes = actionButton.getBody().getBody().split(",");
                                if (voucherCodes.length > 0) {
                                    voucherCodeLayout.setVisibility(View.VISIBLE);
                                    for (int j = 0; j < voucherCodes.length; j++) {
                                        BookingCodeView bookingCodeView = new BookingCodeView(context, voucherCodes[i], i, context.getResources().getString(R.string.voucher_code_title), voucherCodes.length);
                                        bookingCodeView.setBackground(null);
                                        voucherCodeLayout.addView(bookingCodeView);
                                    }
                                }
                            }
                        } else {
                            if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON) && context != null) {
                                presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                            } else {
                                setActionButtonClick(tapActionTextView, actionButton, item, metaDataInfo.getQuantity());
                            }
                            tapActionLayoutEvents.addView(tapActionTextView);
                        }
                        setEventInfo(actionButton, totalTicketCount);
                    }
                }
             }
        }

        private void setEventInfo(ActionButton actionButton, int totalTicketCount) {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT) || actionButton.getControl().equalsIgnoreCase(KEY_REFRESH)) {
                setETiket(totalTicketCount);
            } else if (actionButton.getControl().equalsIgnoreCase(KEY_POPUP)) {
                if (totalTicketCount > 0) {
                    brandName.setText(String.format("%s %s", totalTicketCount, context.getResources().getString(R.string.event_ticket_qrcode_multiple)));
                } else {
                    brandName.setText(context.getResources().getString(R.string.event_ticket_qrcode_count));
                }
            } else if(actionButton.getControl().equalsIgnoreCase(KEY_VOUCHER_CODE)){
                if (totalTicketCount > 0) {
                    brandName.setText(String.format("%s %s", totalTicketCount, context.getResources().getString(R.string.event_ticket_booking_multiple)));
                } else {
                    brandName.setText(context.getResources().getString(R.string.event_ticket_booking_count));
                }
            }
        }

        private void setETiket(int totalTicketCount){
            if (totalTicketCount > 0) {
                brandName.setText(String.format("%s %s", totalTicketCount, context.getResources().getString(R.string.event_ticket_voucher_multiple)));
            } else {
                brandName.setText(context.getResources().getString(R.string.event_ticket_voucher_count));
            }
        }

        private void setActionButtonClick(TextView view, ActionButton actionButton, Items item, int totalTicketCount) {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (!actionButton.getBody().equals("") && !actionButton.getBody().getAppURL().equals("")) {
                    if (view == null) {
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    } else if (isDownloadable(actionButton)) {
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL(), true, "Tokopedia E-Ticket"));
                    } else {
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL(), false, ""));
                    }
                }
            } else if (actionButton.getControl().equalsIgnoreCase(KEY_QRCODE)) {
                view.setOnClickListener(v -> {
                    setEventDetails.openShowQRFragment(actionButton, item);
                });
            } else if (actionButton.getControl().equalsIgnoreCase(KEY_POPUP)) {
                view.setOnClickListener(v -> {
                    setEventDetails.openShowQRFragment(actionButton, item);
                });
            }
        }

        private boolean isDownloadable(ActionButton actionButton) {
            if (!TextUtils.isEmpty(actionButton.getHeader())) {
                return actionButton.getHeaderObject() != null && actionButton.getHeaderObject().getContentType() != null
                        && actionButton.getHeaderObject().getContentType().equalsIgnoreCase(CONTENT_TYPE);
            }
            return false;
        }

        private TextView renderActionButtons(int position, ActionButton actionButton, Items item) {
            TextView tapActionTextView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) context.getResources().getDimension(com.tokopedia.resources.common.R.dimen.dp_8), 0, 0);
            tapActionTextView.setPadding((int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16));
            tapActionTextView.setLayoutParams(params);
            tapActionTextView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0));
            tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            tapActionTextView.setText(actionButton.getLabel());
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            if (!actionButton.getActionColor().getBackground().equals("")) {
                shape.setColor(android.graphics.Color.parseColor(actionButton.getActionColor().getBackground()));
            } else {
                shape.setColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400));
            }
            if (!actionButton.getActionColor().getBorder().equals("")) {
                shape.setStroke(1, android.graphics.Color.parseColor(actionButton.getActionColor().getBorder()));
            }
            tapActionTextView.setBackground(shape);
            if (!actionButton.getActionColor().getTextColor().equals("")) {
                tapActionTextView.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
            } else {
                tapActionTextView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0));
            }


            if (position == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                float radius = context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4);
                shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

            } else {

                shape.setCornerRadius(context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4));
            }

            tapActionTextView.setBackground(shape);

            return tapActionTextView;
        }

        /*
        private void setButtonLayout(List<ActionButton> tapActions, )*/


        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface SetEventDetails {
        void setEventDetails(ActionButton actionButton, Items item);

        void openShowQRFragment(ActionButton actionButton, Items item);

        void setDetailTitle(String title);

        void setInsuranceDetail();

        void setPassengerEvent(Items item);

        void setActionButtonEvent(Items item,ActionButton actionButton, OrderDetails orderDetails);

        void setDealsBanner(Items item);

        void askPermission(String uri, Boolean isDownloadable, String downloadFileName);

        void sendThankYouEvent(MetaDataInfo metaDataInfo, int categoryType, OrderDetails orderDetails);

        void showRetryButtonToaster(String msg);


    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView validDate;
        private ProgressBar progressBar;
        private CustomTicketView customTicketView1;
        private CustomTicketView customTicketView2;
        private View llValid;
        private View llTanggalEvent;
        private TextView tvEventDate;
        private TextView tvRightTypeofEvents;
        private TextView tvRightAddress;
        private TextView tvRightCategoryTicket;
        private TextView tvRightNumberOfBooking;
        private LinearLayout tapActionLayout;
        private LinearLayout actionLayout;
        private TextView tvValidTill;
        private int index;

        public DefaultViewHolder(View itemView, int itemType) {
            super(itemView);
            this.itemView = itemView;
            customTicketView1 = itemView.findViewById(R.id.customView1);
            customTicketView2 = itemView.findViewById(R.id.customView2);

            tvValidTill = itemView.findViewById(R.id.tv_valid_till);
            validDate = itemView.findViewById(R.id.tv_valid_till_date);
            llValid = itemView.findViewById(R.id.ll_valid);
            itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            progressBar = itemView.findViewById(R.id.prog_bar);
            tvRightTypeofEvents = itemView.findViewById(R.id.right_event);
            tvRightAddress = itemView.findViewById(R.id.right_address);
            tvRightCategoryTicket = itemView.findViewById(R.id.right_category_ticket);
            tvRightNumberOfBooking = itemView.findViewById(R.id.right_total_ticket);
            llTanggalEvent = itemView.findViewById(R.id.ll_tanggal_event);
            tvEventDate = itemView.findViewById(R.id.tv_start_date);
            tapActionLayout = itemView.findViewById(R.id.tapAction);
            actionLayout = itemView.findViewById(R.id.actionButton);
        }

        void bindData(final Items item, int itemType) {
            MetaDataInfo metaDataInfo = null;
            boolean hasViews = false;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }

            if (metaDataInfo != null) {
                setEventDetails.sendThankYouEvent(metaDataInfo, ITEM_DEALS, orderDetails);
                setEventDetails.setDetailTitle(context.getResources().getString(R.string.purchase_detail));
                if (!TextUtils.isEmpty(metaDataInfo.getEndDate())) {
                    validDate.setText(" ".concat(metaDataInfo.getEndDate()));
                    llValid.setVisibility(View.VISIBLE);
                    hasViews = true;
                } else {
                    llValid.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(metaDataInfo.getEntityProductName())) {
                    itemView.findViewById(R.id.ll_event).setVisibility(View.GONE);
                } else {
                    hasViews = true;
                    tvRightTypeofEvents.setText(metaDataInfo.getEntityProductName());
                }
                if (metaDataInfo.getEntityPackages() != null && metaDataInfo.getEntityPackages().size() > 0) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getAddress())) {
                        itemView.findViewById(R.id.ll_address).setVisibility(View.GONE);
                    } else {
                        hasViews = true;
                        tvRightAddress.setText(metaDataInfo.getEntityPackages().get(0).getAddress());
                    }
                } else {
                    itemView.findViewById(R.id.ll_address).setVisibility(View.GONE);
                }
                if (metaDataInfo.getEntityPackages() != null && metaDataInfo.getEntityPackages().size() > 0) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getDisplayName())) {
                        itemView.findViewById(R.id.ll_category_ticket).setVisibility(View.GONE);
                    } else {
                        tvRightCategoryTicket.setText(metaDataInfo.getEntityPackages().get(0).getDisplayName());
                        hasViews = true;
                    }
                } else {
                    itemView.findViewById(R.id.ll_category_ticket).setVisibility(View.GONE);
                }
                if (item.getQuantity() == 0) {
                    itemView.findViewById(R.id.ll_total_ticket).setVisibility(View.GONE);
                } else {
                    hasViews = true;
                    tvRightNumberOfBooking.setText(String.valueOf(metaDataInfo.getTotalTicketCount()));
                }
                if (!TextUtils.isEmpty(metaDataInfo.getStartDate())) {
                    hasViews = true;
                    tvEventDate.setText(" ".concat(metaDataInfo.getStartDate()));
                    llTanggalEvent.setVisibility(View.VISIBLE);
                } else {
                    llTanggalEvent.setVisibility(View.GONE);
                }
                if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded() && context != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    tapActionLayout.setVisibility(View.GONE);
                    presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                }
                if (!hasViews) {
                    customTicketView1.setVisibility(View.GONE);
                    itemView.findViewById(R.id.divider1).setVisibility(View.GONE);
                } else {
                    customTicketView1.setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.divider1).setVisibility(View.VISIBLE);
                }

                if (item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                        tapActionLayout.setVisibility(View.GONE);
                    } else {
                        tapActionLayout.setVisibility(View.VISIBLE);
                        tapActionLayout.removeAllViews();
                        int size = item.getTapActions().size();
                        for (int i = 0; i < size; i++) {
                            ActionButton actionButton = item.getTapActions().get(i);
                            TextView tapActionTextView = renderActionButtons(i, actionButton, item);
                            if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON) && context != null) {
                                presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                            } else {
                                setActionButtonClick(tapActionTextView, actionButton);
                            }
                            tapActionLayout.addView(tapActionTextView);
                        }
                    }
                } else if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    progressBar.setVisibility(View.GONE);
                }

                if (item.getActionButtons() == null || item.getActionButtons().size() == 0) {
                    actionLayout.setVisibility(View.GONE);
                } else {
                    actionLayout.setVisibility(View.VISIBLE);
                    actionLayout.removeAllViews();
                    int size = item.getActionButtons().size();
                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getActionButtons().get(i);

                        TextView actionTextView = renderActionButtons(i, actionButton, item);
                        if (!actionButton.getControl().equalsIgnoreCase(KEY_TEXT)) {
                            if (item.isActionButtonLoaded()) {
                                setActionButtonClick(null, actionButton);
                            } else {
                                actionTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON) && context != null) {
                                            presenter.getActionButtonGql(GraphqlHelper.loadRawString(context.getResources(), R.raw.tapactions), item.getActionButtons(), ItemsAdapter.this, getIndex(), false);
                                        } else {
                                            setActionButtonClick(actionTextView, actionButton);
                                        }

                                    }
                                });
                            }
                        }
                        actionLayout.addView(actionTextView);
                    }
                }
            }
        }

        private void setActionButtonClick(TextView view, ActionButton actionButton) {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (!TextUtils.isEmpty(actionButton.getBody().toString()) && !TextUtils.isEmpty(actionButton.getBody().getAppURL())) {
                    if (view == null)
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    else
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL(), false, ""));
                }
            }
        }

        private TextView renderActionButtons(int position, ActionButton actionButton, Items item) {
            TextView tapActionTextView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) context.getResources().getDimension(com.tokopedia.resources.common.R.dimen.dp_8), 0, 0);
            tapActionTextView.setPadding((int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), (int) context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16));
            tapActionTextView.setLayoutParams(params);
            tapActionTextView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0));
            tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            tapActionTextView.setText(actionButton.getLabel());
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            if (!TextUtils.isEmpty(actionButton.getActionColor().getBackground())) {
                shape.setColor(android.graphics.Color.parseColor(actionButton.getActionColor().getBackground()));
            } else {
                shape.setColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400));
            }
            if (!TextUtils.isEmpty(actionButton.getActionColor().getBorder())) {
                shape.setStroke(1, android.graphics.Color.parseColor(actionButton.getActionColor().getBorder()));
            }
            tapActionTextView.setBackground(shape);
            if (!TextUtils.isEmpty(actionButton.getActionColor().getTextColor())) {
                tapActionTextView.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
            } else {
                tapActionTextView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0));
            }


            if (position == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                float radius = context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4);
                shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

            } else {

                shape.setCornerRadius(context.getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4));
            }

            tapActionTextView.setBackground(shape);

            return tapActionTextView;
        }


        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}