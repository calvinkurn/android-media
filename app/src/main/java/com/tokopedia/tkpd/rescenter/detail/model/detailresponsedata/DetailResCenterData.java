package com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 8/13/16.
 */
public class DetailResCenterData implements Parcelable {

    @SerializedName("detail")
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public static class Detail implements Parcelable {
        @SerializedName("resolution_last")
        private ResolutionLast resolutionLast;
        @SerializedName("resolution_conversation_count")
        private String resolutionConversationCount;
        @SerializedName("resolution_by")
        private ResolutionBy resolutionBy;
        @SerializedName("resolution_shop")
        private ResolutionShop resolutionShop;
        @SerializedName("resolution_can_conversation")
        private Integer resolutionCanConversation;
        @SerializedName("resolution_order")
        private ResolutionOrder resolutionOrder;
        @SerializedName("resolution_link")
        private String resolutionLink;
        @SerializedName("resolution_link_encode")
        private String resolutionLinkEncode;

        public ResolutionLast getResolutionLast() {
            return resolutionLast;
        }

        public void setResolutionLast(ResolutionLast resolutionLast) {
            this.resolutionLast = resolutionLast;
        }

        public String getResolutionConversationCount() {
            return resolutionConversationCount;
        }

        public void setResolutionConversationCount(String resolutionConversationCount) {
            this.resolutionConversationCount = resolutionConversationCount;
        }

        public ResolutionBy getResolutionBy() {
            return resolutionBy;
        }

        public void setResolutionBy(ResolutionBy resolutionBy) {
            this.resolutionBy = resolutionBy;
        }

        public ResolutionShop getResolutionShop() {
            return resolutionShop;
        }

        public void setResolutionShop(ResolutionShop resolutionShop) {
            this.resolutionShop = resolutionShop;
        }

        public Integer getResolutionCanConversation() {
            return resolutionCanConversation;
        }

        public void setResolutionCanConversation(Integer resolutionCanConversation) {
            this.resolutionCanConversation = resolutionCanConversation;
        }

        public ResolutionOrder getResolutionOrder() {
            return resolutionOrder;
        }

        public void setResolutionOrder(ResolutionOrder resolutionOrder) {
            this.resolutionOrder = resolutionOrder;
        }

        public String getResolutionLink() {
            return resolutionLink;
        }

        public void setResolutionLink(String resolutionLink) {
            this.resolutionLink = resolutionLink;
        }

        public String getResolutionLinkEncode() {
            return resolutionLinkEncode;
        }

        public void setResolutionLinkEncode(String resolutionLinkEncode) {
            this.resolutionLinkEncode = resolutionLinkEncode;
        }

        public Detail() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.resolutionLast, flags);
            dest.writeString(this.resolutionConversationCount);
            dest.writeParcelable(this.resolutionBy, flags);
            dest.writeParcelable(this.resolutionShop, flags);
            dest.writeValue(this.resolutionCanConversation);
            dest.writeParcelable(this.resolutionOrder, flags);
            dest.writeString(this.resolutionLink);
            dest.writeString(this.resolutionLinkEncode);
        }

        protected Detail(Parcel in) {
            this.resolutionLast = in.readParcelable(ResolutionLast.class.getClassLoader());
            this.resolutionConversationCount = in.readString();
            this.resolutionBy = in.readParcelable(ResolutionBy.class.getClassLoader());
            this.resolutionShop = in.readParcelable(ResolutionShop.class.getClassLoader());
            this.resolutionCanConversation = (Integer) in.readValue(Integer.class.getClassLoader());
            this.resolutionOrder = in.readParcelable(ResolutionOrder.class.getClassLoader());
            this.resolutionLink = in.readString();
            this.resolutionLinkEncode = in.readString();
        }

        public static final Creator<Detail> CREATOR = new Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel source) {
                return new Detail(source);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };
    }

    public static class ResolutionLast implements Parcelable {
        @SerializedName("last_resolution_id")
        private String lastResolutionId;
        @SerializedName("last_show_input_addr_button")
        private Integer lastShowInputAddrButton;
        @SerializedName("last_show_appeal_button")
        private Integer lastShowAppealButton;
        @SerializedName("last_refund_amt")
        private String lastRefundAmt;
        @SerializedName("last_user_name")
        private String lastUserName;
        @SerializedName("last_solution")
        private Integer lastSolution;
        @SerializedName("last_user_url")
        private String lastUserUrl;
        @SerializedName("last_show_input_resi_button")
        private Integer lastShowInputResiButton;
        @SerializedName("last_show_accept_button")
        private Integer lastShowAcceptButton;
        @SerializedName("last_user_label_id")
        private Integer lastUserLabelId;
        @SerializedName("last_action_by")
        private Integer lastActionBy;
        @SerializedName("last_refund_amt_idr")
        private String lastRefundAmtIdr;
        @SerializedName("last_rival_accepted")
        private Integer lastRivalAccepted;
        @SerializedName("last_create_time_str")
        private String lastCreateTimeStr;
        @SerializedName("last_category_trouble_string")
        private String lastCategoryTroubleString;
        @SerializedName("last_trouble_type")
        private Integer lastTroubleType;
        @SerializedName("last_trouble_type_string")
        private String lastTroubleTypeString;
        @SerializedName("last_show_finish_button")
        private Integer lastShowFinishButton;
        @SerializedName("last_show_accept_admin_button")
        private Integer lastShowAcceptAdminButton;
        @SerializedName("last_flag_received")
        private Integer lastFlagReceived;
        @SerializedName("last_user_label")
        private String lastUserLabel;
        @SerializedName("last_solution_string")
        private String lastSolutionString;
        @SerializedName("last_category_trouble_type")
        private Integer lastCategoryTroubleType;

        public String getLastResolutionId() {
            return lastResolutionId;
        }

        public void setLastResolutionId(String lastResolutionId) {
            this.lastResolutionId = lastResolutionId;
        }

        public Integer getLastShowInputAddrButton() {
            return lastShowInputAddrButton;
        }

        public void setLastShowInputAddrButton(Integer lastShowInputAddrButton) {
            this.lastShowInputAddrButton = lastShowInputAddrButton;
        }

        public Integer getLastShowAppealButton() {
            return lastShowAppealButton;
        }

        public void setLastShowAppealButton(Integer lastShowAppealButton) {
            this.lastShowAppealButton = lastShowAppealButton;
        }

        public String getLastRefundAmt() {
            return lastRefundAmt;
        }

        public void setLastRefundAmt(String lastRefundAmt) {
            this.lastRefundAmt = lastRefundAmt;
        }

        public String getLastUserName() {
            return lastUserName;
        }

        public void setLastUserName(String lastUserName) {
            this.lastUserName = lastUserName;
        }

        public Integer getLastSolution() {
            return lastSolution;
        }

        public void setLastSolution(Integer lastSolution) {
            this.lastSolution = lastSolution;
        }

        public String getLastUserUrl() {
            return lastUserUrl;
        }

        public void setLastUserUrl(String lastUserUrl) {
            this.lastUserUrl = lastUserUrl;
        }

        public Integer getLastShowInputResiButton() {
            return lastShowInputResiButton;
        }

        public void setLastShowInputResiButton(Integer lastShowInputResiButton) {
            this.lastShowInputResiButton = lastShowInputResiButton;
        }

        public Integer getLastShowAcceptButton() {
            return lastShowAcceptButton;
        }

        public void setLastShowAcceptButton(Integer lastShowAcceptButton) {
            this.lastShowAcceptButton = lastShowAcceptButton;
        }

        public Integer getLastUserLabelId() {
            return lastUserLabelId;
        }

        public void setLastUserLabelId(Integer lastUserLabelId) {
            this.lastUserLabelId = lastUserLabelId;
        }

        public Integer getLastActionBy() {
            return lastActionBy;
        }

        public void setLastActionBy(Integer lastActionBy) {
            this.lastActionBy = lastActionBy;
        }

        public String getLastRefundAmtIdr() {
            return lastRefundAmtIdr;
        }

        public void setLastRefundAmtIdr(String lastRefundAmtIdr) {
            this.lastRefundAmtIdr = lastRefundAmtIdr;
        }

        public Integer getLastRivalAccepted() {
            return lastRivalAccepted;
        }

        public void setLastRivalAccepted(Integer lastRivalAccepted) {
            this.lastRivalAccepted = lastRivalAccepted;
        }

        public String getLastCreateTimeStr() {
            return lastCreateTimeStr;
        }

        public void setLastCreateTimeStr(String lastCreateTimeStr) {
            this.lastCreateTimeStr = lastCreateTimeStr;
        }

        public String getLastCategoryTroubleString() {
            return lastCategoryTroubleString;
        }

        public void setLastCategoryTroubleString(String lastCategoryTroubleString) {
            this.lastCategoryTroubleString = lastCategoryTroubleString;
        }

        public Integer getLastTroubleType() {
            return lastTroubleType;
        }

        public void setLastTroubleType(Integer lastTroubleType) {
            this.lastTroubleType = lastTroubleType;
        }

        public String getLastTroubleTypeString() {
            return lastTroubleTypeString;
        }

        public void setLastTroubleTypeString(String lastTroubleTypeString) {
            this.lastTroubleTypeString = lastTroubleTypeString;
        }

        public Integer getLastShowFinishButton() {
            return lastShowFinishButton;
        }

        public void setLastShowFinishButton(Integer lastShowFinishButton) {
            this.lastShowFinishButton = lastShowFinishButton;
        }

        public Integer getLastShowAcceptAdminButton() {
            return lastShowAcceptAdminButton;
        }

        public void setLastShowAcceptAdminButton(Integer lastShowAcceptAdminButton) {
            this.lastShowAcceptAdminButton = lastShowAcceptAdminButton;
        }

        public Integer getLastFlagReceived() {
            return lastFlagReceived;
        }

        public void setLastFlagReceived(Integer lastFlagReceived) {
            this.lastFlagReceived = lastFlagReceived;
        }

        public String getLastUserLabel() {
            return lastUserLabel;
        }

        public void setLastUserLabel(String lastUserLabel) {
            this.lastUserLabel = lastUserLabel;
        }

        public String getLastSolutionString() {
            return lastSolutionString;
        }

        public void setLastSolutionString(String lastSolutionString) {
            this.lastSolutionString = lastSolutionString;
        }

        public Integer getLastCategoryTroubleType() {
            return lastCategoryTroubleType;
        }

        public void setLastCategoryTroubleType(Integer lastCategoryTroubleType) {
            this.lastCategoryTroubleType = lastCategoryTroubleType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.lastResolutionId);
            dest.writeValue(this.lastShowInputAddrButton);
            dest.writeValue(this.lastShowAppealButton);
            dest.writeString(this.lastRefundAmt);
            dest.writeString(this.lastUserName);
            dest.writeValue(this.lastSolution);
            dest.writeString(this.lastUserUrl);
            dest.writeValue(this.lastShowInputResiButton);
            dest.writeValue(this.lastShowAcceptButton);
            dest.writeValue(this.lastUserLabelId);
            dest.writeValue(this.lastActionBy);
            dest.writeString(this.lastRefundAmtIdr);
            dest.writeValue(this.lastRivalAccepted);
            dest.writeString(this.lastCreateTimeStr);
            dest.writeString(this.lastCategoryTroubleString);
            dest.writeValue(this.lastTroubleType);
            dest.writeString(this.lastTroubleTypeString);
            dest.writeValue(this.lastShowFinishButton);
            dest.writeValue(this.lastShowAcceptAdminButton);
            dest.writeValue(this.lastFlagReceived);
            dest.writeString(this.lastUserLabel);
            dest.writeString(this.lastSolutionString);
            dest.writeValue(this.lastCategoryTroubleType);
        }

        public ResolutionLast() {
        }

        protected ResolutionLast(Parcel in) {
            this.lastResolutionId = in.readString();
            this.lastShowInputAddrButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAppealButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmt = in.readString();
            this.lastUserName = in.readString();
            this.lastSolution = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastUserUrl = in.readString();
            this.lastShowInputResiButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAcceptButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastUserLabelId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastActionBy = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmtIdr = in.readString();
            this.lastRivalAccepted = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastCreateTimeStr = in.readString();
            this.lastCategoryTroubleString = in.readString();
            this.lastTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastTroubleTypeString = in.readString();
            this.lastShowFinishButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastShowAcceptAdminButton = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastFlagReceived = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastUserLabel = in.readString();
            this.lastSolutionString = in.readString();
            this.lastCategoryTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<ResolutionLast> CREATOR = new Creator<ResolutionLast>() {
            @Override
            public ResolutionLast createFromParcel(Parcel source) {
                return new ResolutionLast(source);
            }

            @Override
            public ResolutionLast[] newArray(int size) {
                return new ResolutionLast[size];
            }
        };
    }

    public static class ResolutionBy implements Parcelable {
        @SerializedName("user_label_id")
        private String userLabelId;
        @SerializedName("user_label")
        private String userLabel;
        @SerializedName("by_customer")
        private Integer byCustomer;
        @SerializedName("by_seller")
        private Integer bySeller;

        public String getUserLabelId() {
            return userLabelId;
        }

        public void setUserLabelId(String userLabelId) {
            this.userLabelId = userLabelId;
        }

        public String getUserLabel() {
            return userLabel;
        }

        public void setUserLabel(String userLabel) {
            this.userLabel = userLabel;
        }

        public Integer getByCustomer() {
            return byCustomer;
        }

        public void setByCustomer(Integer byCustomer) {
            this.byCustomer = byCustomer;
        }

        public Integer getBySeller() {
            return bySeller;
        }

        public void setBySeller(Integer bySeller) {
            this.bySeller = bySeller;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userLabelId);
            dest.writeString(this.userLabel);
            dest.writeValue(this.byCustomer);
            dest.writeValue(this.bySeller);
        }

        public ResolutionBy() {
        }

        protected ResolutionBy(Parcel in) {
            this.userLabelId = in.readString();
            this.userLabel = in.readString();
            this.byCustomer = (Integer) in.readValue(Integer.class.getClassLoader());
            this.bySeller = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<ResolutionBy> CREATOR = new Creator<ResolutionBy>() {
            @Override
            public ResolutionBy createFromParcel(Parcel source) {
                return new ResolutionBy(source);
            }

            @Override
            public ResolutionBy[] newArray(int size) {
                return new ResolutionBy[size];
            }
        };
    }

    public static class ResolutionShop implements Parcelable {
        @SerializedName("shop_image_300")
        private String shopImage300;
        @SerializedName("shop_image")
        private String shopImage;
        @SerializedName("shop_id")
        private String shopId;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("shop_url")
        private String shopUrl;
        @SerializedName("shop_reputation")
        private ShopReputation shopReputation;

        public String getShopImage300() {
            return shopImage300;
        }

        public void setShopImage300(String shopImage300) {
            this.shopImage300 = shopImage300;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public ShopReputation getShopReputation() {
            return shopReputation;
        }

        public void setShopReputation(ShopReputation shopReputation) {
            this.shopReputation = shopReputation;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.shopImage300);
            dest.writeString(this.shopImage);
            dest.writeString(this.shopId);
            dest.writeString(this.shopName);
            dest.writeString(this.shopUrl);
            dest.writeParcelable(this.shopReputation, flags);
        }

        public ResolutionShop() {
        }

        protected ResolutionShop(Parcel in) {
            this.shopImage300 = in.readString();
            this.shopImage = in.readString();
            this.shopId = in.readString();
            this.shopName = in.readString();
            this.shopUrl = in.readString();
            this.shopReputation = in.readParcelable(ShopReputation.class.getClassLoader());
        }

        public static final Creator<ResolutionShop> CREATOR = new Creator<ResolutionShop>() {
            @Override
            public ResolutionShop createFromParcel(Parcel source) {
                return new ResolutionShop(source);
            }

            @Override
            public ResolutionShop[] newArray(int size) {
                return new ResolutionShop[size];
            }
        };
    }

    public static class ResolutionOrder implements Parcelable {
        @SerializedName("order_shipping_price_idr")
        private String orderShippingPriceIdr;
        @SerializedName("order_product_price")
        private Integer orderProductPrice;
        @SerializedName("order_shipping_price")
        private Integer orderShippingPrice;
        @SerializedName("order_open_amount")
        private Integer orderOpenAmount;
        @SerializedName("order_pdf_url")
        private String orderPdfUrl;
        @SerializedName("order_is_free_return")
        private Integer orderIsFreeReturn;
        @SerializedName("order_open_amount_idr")
        private String orderOpenAmountIdr;
        @SerializedName("order_product_price_idr")
        private Integer orderProductPriceIdr;
        @SerializedName("order_is_free_return_text")
        private String orderIsFreeReturnText;
        @SerializedName("order_invoice_ref_num")
        private String orderInvoiceRefNum;
        @SerializedName("order_id")
        private String orderId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderShippingPriceIdr() {
            return orderShippingPriceIdr;
        }

        public void setOrderShippingPriceIdr(String orderShippingPriceIdr) {
            this.orderShippingPriceIdr = orderShippingPriceIdr;
        }

        public Integer getOrderProductPrice() {
            return orderProductPrice;
        }

        public void setOrderProductPrice(Integer orderProductPrice) {
            this.orderProductPrice = orderProductPrice;
        }

        public Integer getOrderShippingPrice() {
            return orderShippingPrice;
        }

        public void setOrderShippingPrice(Integer orderShippingPrice) {
            this.orderShippingPrice = orderShippingPrice;
        }

        public Integer getOrderOpenAmount() {
            return orderOpenAmount;
        }

        public void setOrderOpenAmount(Integer orderOpenAmount) {
            this.orderOpenAmount = orderOpenAmount;
        }

        public String getOrderPdfUrl() {
            return orderPdfUrl;
        }

        public void setOrderPdfUrl(String orderPdfUrl) {
            this.orderPdfUrl = orderPdfUrl;
        }

        public Integer getOrderIsFreeReturn() {
            return orderIsFreeReturn;
        }

        public void setOrderIsFreeReturn(Integer orderIsFreeReturn) {
            this.orderIsFreeReturn = orderIsFreeReturn;
        }

        public String getOrderOpenAmountIdr() {
            return orderOpenAmountIdr;
        }

        public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
            this.orderOpenAmountIdr = orderOpenAmountIdr;
        }

        public Integer getOrderProductPriceIdr() {
            return orderProductPriceIdr;
        }

        public void setOrderProductPriceIdr(Integer orderProductPriceIdr) {
            this.orderProductPriceIdr = orderProductPriceIdr;
        }

        public String getOrderIsFreeReturnText() {
            return orderIsFreeReturnText;
        }

        public void setOrderIsFreeReturnText(String orderIsFreeReturnText) {
            this.orderIsFreeReturnText = orderIsFreeReturnText;
        }

        public String getOrderInvoiceRefNum() {
            return orderInvoiceRefNum;
        }

        public void setOrderInvoiceRefNum(String orderInvoiceRefNum) {
            this.orderInvoiceRefNum = orderInvoiceRefNum;
        }

        public ResolutionOrder() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderShippingPriceIdr);
            dest.writeValue(this.orderProductPrice);
            dest.writeValue(this.orderShippingPrice);
            dest.writeValue(this.orderOpenAmount);
            dest.writeString(this.orderPdfUrl);
            dest.writeValue(this.orderIsFreeReturn);
            dest.writeString(this.orderOpenAmountIdr);
            dest.writeValue(this.orderProductPriceIdr);
            dest.writeString(this.orderIsFreeReturnText);
            dest.writeString(this.orderInvoiceRefNum);
            dest.writeString(this.orderId);
        }

        protected ResolutionOrder(Parcel in) {
            this.orderShippingPriceIdr = in.readString();
            this.orderProductPrice = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderShippingPrice = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderOpenAmount = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderPdfUrl = in.readString();
            this.orderIsFreeReturn = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderOpenAmountIdr = in.readString();
            this.orderProductPriceIdr = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderIsFreeReturnText = in.readString();
            this.orderInvoiceRefNum = in.readString();
            this.orderId = in.readString();
        }

        public static final Creator<ResolutionOrder> CREATOR = new Creator<ResolutionOrder>() {
            @Override
            public ResolutionOrder createFromParcel(Parcel source) {
                return new ResolutionOrder(source);
            }

            @Override
            public ResolutionOrder[] newArray(int size) {
                return new ResolutionOrder[size];
            }
        };
    }

    public static class ShopReputation implements Parcelable {
        @SerializedName("tooltip")
        private String tooltip;
        @SerializedName("reputation_badge")
        private ReputationBadge reputationBadge;
        @SerializedName("reputation_score")
        private String reputationScore;
        @SerializedName("score")
        private String score;
        @SerializedName("min_badge_score")
        private Integer minBadgeScore;

        public String getTooltip() {
            return tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public ReputationBadge getReputationBadge() {
            return reputationBadge;
        }

        public void setReputationBadge(ReputationBadge reputationBadge) {
            this.reputationBadge = reputationBadge;
        }

        public String getReputationScore() {
            return reputationScore;
        }

        public void setReputationScore(String reputationScore) {
            this.reputationScore = reputationScore;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public Integer getMinBadgeScore() {
            return minBadgeScore;
        }

        public void setMinBadgeScore(Integer minBadgeScore) {
            this.minBadgeScore = minBadgeScore;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tooltip);
            dest.writeParcelable(this.reputationBadge, flags);
            dest.writeString(this.reputationScore);
            dest.writeString(this.score);
            dest.writeValue(this.minBadgeScore);
        }

        public ShopReputation() {
        }

        protected ShopReputation(Parcel in) {
            this.tooltip = in.readString();
            this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
            this.reputationScore = in.readString();
            this.score = in.readString();
            this.minBadgeScore = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<ShopReputation> CREATOR = new Creator<ShopReputation>() {
            @Override
            public ShopReputation createFromParcel(Parcel source) {
                return new ShopReputation(source);
            }

            @Override
            public ShopReputation[] newArray(int size) {
                return new ShopReputation[size];
            }
        };
    }

    public static class ReputationBadge implements Parcelable {
        @SerializedName("level")
        private Integer level;
        @SerializedName("set")
        private Integer set;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getSet() {
            return set;
        }

        public void setSet(Integer set) {
            this.set = set;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.level);
            dest.writeValue(this.set);
        }

        public ReputationBadge() {
        }

        protected ReputationBadge(Parcel in) {
            this.level = (Integer) in.readValue(Integer.class.getClassLoader());
            this.set = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<ReputationBadge> CREATOR = new Creator<ReputationBadge>() {
            @Override
            public ReputationBadge createFromParcel(Parcel source) {
                return new ReputationBadge(source);
            }

            @Override
            public ReputationBadge[] newArray(int size) {
                return new ReputationBadge[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detail, flags);
    }

    public DetailResCenterData() {
    }

    protected DetailResCenterData(Parcel in) {
        this.detail = in.readParcelable(Detail.class.getClassLoader());
    }

    public static final Parcelable.Creator<DetailResCenterData> CREATOR = new Parcelable.Creator<DetailResCenterData>() {
        @Override
        public DetailResCenterData createFromParcel(Parcel source) {
            return new DetailResCenterData(source);
        }

        @Override
        public DetailResCenterData[] newArray(int size) {
            return new DetailResCenterData[size];
        }
    };
}
