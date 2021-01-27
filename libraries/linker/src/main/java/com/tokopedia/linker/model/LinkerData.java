package com.tokopedia.linker.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.linker.LinkerUtils;


public class LinkerData implements Parcelable {
    public static final String TAG = LinkerData.class.getSimpleName();
    public static final String CATALOG_TYPE = "Catalog";
    public static final String SHOP_TYPE = "Shop";
    public static final String PRODUCT_TYPE = "Product";
    public static final String CATEGORY_TYPE = "Directory";
    public static final String DISCOVERY_TYPE = "Discovery";
    public static final String HOTLIST_TYPE = "Hotlist";
    public static final String RIDE_TYPE = "Ride";
    public static final String PROMO_TYPE = "Promo";
    public static final String HOTEL_TYPE = "Hotel";

    public static final String ARG_UTM_MEDIUM = "Share";
    private static final String DEFAULT_EMPTY_FIELD = "";
    public static final String APP_SHARE_TYPE = "App";
    public static final String REFERRAL_TYPE = "Referral";
    public static final String ARG_UTM_SOURCE = "Android";
    public static final String FEED_TYPE = "feed";
    public static final String GROUPCHAT_TYPE = "tokopedia_play";
    public static final String INDI_CHALLENGE_TYPE = "tokopedia_challenge";
    public static final String PLAY_BROADCASTER = "play_broadcaster";
    public static final String MERCHANT_VOUCHER = "merchant_voucher";

    private String type = "";
    private String typeUrl = "";
    private String name;
    private String price;
    private String uri;
    private String description;
    private String imgUri;
    private String textContent;
    private String source;
    private String id = "";
    private String shareUrl;
    private String pathSticker;
    private String ogUrl;
    private String ogTitle;
    private String ogDescription;
    private String ogImageUrl;
    private String desktopUrl;
    private String deepLink;
    private String shopId;
    private String currency;
    private String catLvl1;
    private String userId;
    private String quantity;
    private String custmMsg;
    private String productCategory;
    private String productName;
    private String journeyId;
    private String invoiceId;
    private String paymentId;
    private boolean throwOnError;
    private String content;
    private String contentType;
    private String level1Name;
    private String level1Id;
    private String level2Name;
    private String level2Id;
    private String level3Name;
    private String level3Id;
    private String sku;
    private String contentId;

    public String getCustmMsg() {
        return custmMsg;
    }

    public void setCustmMsg(String custmMsg) {
        this.custmMsg = custmMsg;
    }

    public LinkerData() {
    }

    protected LinkerData(Parcel in) {
        type = in.readString();
        typeUrl = in.readString();
        name = in.readString();
        price = in.readString();
        uri = in.readString();
        description = in.readString();
        imgUri = in.readString();
        textContent = in.readString();
        source = in.readString();
        id = in.readString();
        shareUrl = in.readString();
        pathSticker = in.readString();
        ogUrl = in.readString();
        ogTitle = in.readString();
        ogDescription = in.readString();
        ogImageUrl = in.readString();
        desktopUrl = in.readString();
        deepLink = in.readString();
        shopId = in.readString();
        currency = in.readString();
        catLvl1 = in.readString();
        userId = in.readString();
        quantity = in.readString();
        productCategory = in.readString();
        productName = in.readString();
        journeyId = in.readString();
        invoiceId = in.readString();
        paymentId = in.readString();
        throwOnError = in.readByte() != 0;
        content = in.readString();
        contentType = in.readString();
        level1Name = in.readString();
        level1Id = in.readString();
        level2Name = in.readString();
        level2Id = in.readString();
        level3Name = in.readString();
        level3Id = in.readString();
        sku = in.readString();
        contentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(typeUrl);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(uri);
        dest.writeString(description);
        dest.writeString(imgUri);
        dest.writeString(textContent);
        dest.writeString(source);
        dest.writeString(id);
        dest.writeString(shareUrl);
        dest.writeString(pathSticker);
        dest.writeString(ogUrl);
        dest.writeString(ogTitle);
        dest.writeString(ogDescription);
        dest.writeString(ogImageUrl);
        dest.writeString(desktopUrl);
        dest.writeString(deepLink);
        dest.writeString(shopId);
        dest.writeString(currency);
        dest.writeString(catLvl1);
        dest.writeString(userId);
        dest.writeString(quantity);
        dest.writeString(productCategory);
        dest.writeString(productName);
        dest.writeString(journeyId);
        dest.writeString(invoiceId);
        dest.writeString(paymentId);
        dest.writeByte((byte) (throwOnError ? 1 : 0));
        dest.writeString(content);
        dest.writeString(contentType);
        dest.writeString(level1Name);
        dest.writeString(level1Id);
        dest.writeString(level2Name);
        dest.writeString(level2Id);
        dest.writeString(level3Name);
        dest.writeString(level3Id);
        dest.writeString(sku);
        dest.writeString(contentId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LinkerData> CREATOR = new Creator<LinkerData>() {
        @Override
        public LinkerData createFromParcel(Parcel in) {
            return new LinkerData(in);
        }

        @Override
        public LinkerData[] newArray(int size) {
            return new LinkerData[size];
        }
    };

    public String getDesktopUrl() {
        return desktopUrl;
    }

    public void setDesktopUrl(String desktopUrl) {
        this.desktopUrl = desktopUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeUrl() {
        return typeUrl;
    }

    public void setTypeUrl(String typeUrl) {
        this.typeUrl = typeUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getshareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String renderShareUri() {
        if (getUri() == null) {
            return "";
        }
        String campaign = getCampaignName();

        String renderedUrl;
        if (!getType().equalsIgnoreCase(RIDE_TYPE)) {
            if (getUri().contains("?")) {
                Uri uri = Uri.parse(String.format("%s&utm_source=%s&utm_medium=%s&utm_campaign=%s",
                        getUri(), ARG_UTM_SOURCE, ARG_UTM_MEDIUM, campaign));
                renderedUrl = uri.toString();
            } else {
                Uri uri = Uri.parse(String.format("%s?utm_source=%s&utm_medium=%s&utm_campaign=%s",
                        getUri(), ARG_UTM_SOURCE, ARG_UTM_MEDIUM, campaign));
                renderedUrl = uri.toString();
            }
        } else {
            renderedUrl = getUri();
        }
        return renderedUrl;
    }

    public String getCampaignName() {
        String campaign = "Product Share";
        if (getType() != null)
            campaign = getType() + "%20Share";
        return campaign;
    }

    public String getOriginalTextContent() {
        return textContent;
    }

    public String getTextContent() {
        if (getType() != null) {
            return (this.textContent != null) ? (this.textContent + "\n" + renderShareUri()) : renderShareUri();
        }
        return String.valueOf(LinkerUtils.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri + "\n"));
    }

    public String getTextContentForBranch(String shortUrl) {
        if (getType() != null) {
            return (this.textContent != null) ? (this.textContent + "\n" + shortUrl) : shortUrl;
        }
        return String.valueOf(LinkerUtils.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri + "\n"));
    }

    public String getDescription() {
        if (TextUtils.isEmpty(description)) {
            return DEFAULT_EMPTY_FIELD;
        } else {
            return String.valueOf(LinkerUtils.fromHtml(description));
        }
    }

    public String[] getSplittedDescription(String splitWith) {
        if (description.contains(splitWith))
            return description.split(splitWith);
        else
            return new String[0];
    }


    public String getPathSticker() {
        return pathSticker;
    }

    public void setPathSticker(String pathSticker) {
        this.pathSticker = pathSticker;
    }

    public String getOgUrl() {
        return ogUrl;
    }

    public void setOgUrl(String ogUrl) {
        this.ogUrl = ogUrl;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImageUrl() {
        return ogImageUrl;
    }

    public void setOgImageUrl(String ogImageUrl) {
        this.ogImageUrl = ogImageUrl;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getShopId(){
        return shopId;
    }

    public void setShopId(String shopId){
        this.shopId = shopId;
    }

    public String getCurrency(){
        return currency;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public String getCatLvl1(){
        return catLvl1;
    }

    public void setCatLvl1(String catLvl1){
        this.catLvl1 = catLvl1;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public boolean isThrowOnError() {
        return throwOnError;
    }

    public void setThrowOnError(boolean throwOnError) {
        this.throwOnError = throwOnError;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public String getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(String level1Id) {
        this.level1Id = level1Id;
    }

    public String getLevel2Name() {
        return level2Name;
    }

    public void setLevel2Name(String level2Name) {
        this.level2Name = level2Name;
    }

    public String getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(String level2Id) {
        this.level2Id = level2Id;
    }

    public String getLevel3Name() {
        return level3Name;
    }

    public void setLevel3Name(String level3Name) {
        this.level3Name = level3Name;
    }

    public String getLevel3Id() {
        return level3Id;
    }

    public void setLevel3Id(String level3Id) {
        this.level3Id = level3Id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public static class Builder {
        private String name;
        private String price;
        private String uri;
        private String description;
        private String imgUri;
        private String type;
        private String typeUrl;
        private String textContent;
        private String source;
        private String id;
        private String shareUrl;
        private String pathSticker;
        private String ogUrl;
        private String ogTitle;
        private String ogDescription;
        private String ogImageUrl;
        private String desktopUrl;
        private String deepLink;
        private String shopId;
        private String currency;
        private String catLvl1;
        private String userId;
        private String quantity;
        private String custmMsg;
        private String productCategory;
        private String productName;
        private String journeyId;
        private String invoiceId;
        private String paymentId;
        private boolean throwOnError;
        private String content;
        private String contentType;
        private String level1Name;
        private String level1Id;
        private String level2Name;
        private String level2Id;
        private String level3Name;
        private String level3Id;
        private String sku;
        private String contentId;

        private Builder() {
        }

        public static Builder getLinkerBuilder() {
            return new Builder();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setImgUri(String imgUri) {
            this.imgUri = imgUri;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setTypeUrl(String typeUrl) {
            this.typeUrl = typeUrl;
            return this;
        }

        public Builder setTextContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }
        public Builder setPathSticker(String pathSticker) {
            this.pathSticker = pathSticker;
            return this;
        }

        public Builder setOgUrl(String ogUrl) {
            this.ogUrl = ogUrl;
            return this;
        }

        public Builder setDesktopUrl(String desktopUrl) {
            this.desktopUrl = desktopUrl;
            return this;
        }

        public Builder setOgTitle(String ogTitle) {
            this.ogTitle = ogTitle;
            return this;
        }

        public Builder setOgDescription(String ogDescription) {
            this.ogDescription = ogDescription;
            return this;
        }

        public Builder setOgImageUrl(String ogImageUrl) {
            this.ogImageUrl = ogImageUrl;
            return this;
        }

        public Builder setDeepLink(String deepLink) {
            this.deepLink = deepLink;
            return this;
        }

        public Builder setShopId(String shopId){
            this.shopId = shopId;
            return this;
        }

        public Builder setCurrency(String currency){
            this.currency = currency;
            return this;
        }

        public Builder setCatLvl1(String catLvl1){
            this.catLvl1 = catLvl1;
            return this;
        }

        public Builder setUserId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder setQuantity(String quantity){
            this.quantity = quantity;
            return this;
        }

        public Builder setCustMsg(String custmMsg){
            this.custmMsg = custmMsg;
            return this;
        }

        public Builder setProductCategory(String productCategory){
            this.productCategory = productCategory;
            return this;
        }

        public Builder setProductName(String productName){
            this.productName = productName;
            return this;
        }

        public Builder setJourneyId(String journeyId){
            this.journeyId = journeyId;
            return this;
        }

        public Builder setInvoiceId(String invoiceId){
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder setPaymentId(String paymentId){
            this.paymentId = paymentId;
            return this;
        }

        public Builder setThrowOnError(boolean throwOnError){
            this.throwOnError = throwOnError;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setLevel1Name(String level1Name) {
            this.level1Name = level1Name;
            return this;
        }

        public Builder setLevel1Id(String level1Id) {
            this.level1Id = level1Id;
            return this;
        }

        public Builder setLevel2Name(String level2Name) {
            this.level2Name = level2Name;
            return this;
        }

        public Builder setLevel2Id(String level2Id) {
            this.level2Id = level2Id;
            return this;
        }

        public Builder setLevel3Name(String level3Name) {
            this.level3Name = level3Name;
            return this;
        }

        public Builder setLevel3Id(String level3Id) {
            this.level3Id = level3Id;
            return this;
        }

        public Builder setSku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder setContentId(String contentId) {
            this.contentId = contentId;
            return this;
        }


        public Builder but() {
            return getLinkerBuilder().setName(name).setPrice(price).setUri(uri).setDescription(description).setImgUri(imgUri).setShareUrl(shareUrl);
        }

        public LinkerData build() {
            LinkerData linkerData = new LinkerData();
            linkerData.setName(name);
            linkerData.setPrice(price);
            linkerData.setUri(uri);
            linkerData.setDescription(description);
            linkerData.setImgUri(imgUri);
            linkerData.setType(type);
            linkerData.setTextContent(textContent);
            linkerData.setSource(source);
            linkerData.setId(id);
            linkerData.setShareUrl(shareUrl);
            linkerData.setPathSticker(pathSticker);
            linkerData.setOgUrl(ogUrl);
            linkerData.setOgTitle(ogTitle);
            linkerData.setOgDescription(ogDescription);
            linkerData.setCustmMsg(custmMsg);
            linkerData.setOgImageUrl(ogImageUrl);
            linkerData.setDesktopUrl(desktopUrl);
            linkerData.setDeepLink(deepLink);
            linkerData.setShopId(shopId);
            linkerData.setCurrency(currency);
            linkerData.setCatLvl1(catLvl1);
            linkerData.setUserId(userId);
            linkerData.setQuantity(quantity);
            linkerData.setProductCategory(productCategory);
            linkerData.setProductName(productName);
            linkerData.setJourneyId(journeyId);
            linkerData.setInvoiceId(invoiceId);
            linkerData.setPaymentId(paymentId);
            linkerData.setThrowOnError(throwOnError);
            linkerData.setContent(content);
            linkerData.setContentType(contentType);
            linkerData.setLevel1Name(level1Name);
            linkerData.setLevel1Id(level1Id);
            linkerData.setLevel2Name(level2Name);
            linkerData.setLevel2Id(level2Id);
            linkerData.setLevel3Name(level3Name);
            linkerData.setLevel3Id(level3Id);
            linkerData.setSku(sku);
            linkerData.setContentId(contentId);
            return linkerData;
        }

    }
}