package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDataView implements Parcelable {

    private List<ProductItemDataView> productList = new ArrayList<>();
    private String additionalParams = "";
    private String autocompleteApplink;
    private String responseCode;
    private String keywordProcess;
    private String errorMessage;
    private TickerDataView tickerModel;
    private SuggestionDataView suggestionModel;
    private int totalData;
    private int totalItem;
    private boolean isQuerySafe;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;
    private GlobalNavDataView globalNavDataView;
    private List<InspirationCarouselDataView> inspirationCarouselDataView = new ArrayList<>();
    private List<InspirationCardDataView> inspirationCardDataView = new ArrayList<>();
    private int defaultView;
    private RelatedDataView relatedDataView;
    private String totalDataText = "";
    private BannerDataView bannerDataView = new BannerDataView();

    public TopAdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(TopAdsModel adsModel) {
        this.adsModel = adsModel;
    }

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }

    public boolean isQuerySafe() {
        return isQuerySafe;
    }

    public void setQuerySafe(boolean querySafe) {
        isQuerySafe = querySafe;
    }

    public ProductDataView() {
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public String getTotalDataText() {
        return totalDataText;
    }

    public void setTotalDataText(String totalDataText) {
        this.totalDataText = totalDataText;
    }

    public List<ProductItemDataView> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItemDataView> productList) {
        this.productList = productList;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public String getAutocompleteApplink() {
        return autocompleteApplink;
    }

    public void setAutocompleteApplink(String autocompleteApplink) {
        this.autocompleteApplink = autocompleteApplink;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getKeywordProcess() {
        return keywordProcess;
    }

    public void setKeywordProcess(String keywordProcess) {
        this.keywordProcess = keywordProcess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TickerDataView getTickerModel() {
        return tickerModel;
    }

    public void setTickerModel(TickerDataView tickerModel) {
        this.tickerModel = tickerModel;
    }

    public SuggestionDataView getSuggestionModel() {
        return suggestionModel;
    }

    public void setSuggestionModel(SuggestionDataView suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public GlobalNavDataView getGlobalNavDataView() {
        return globalNavDataView;
    }

    public void setGlobalNavDataView(GlobalNavDataView globalNavDataView) {
        this.globalNavDataView = globalNavDataView;
    }

    public List<InspirationCarouselDataView> getInspirationCarouselDataView() {
        return inspirationCarouselDataView;
    }

    public void setInspirationCarouselDataView(List<InspirationCarouselDataView> inspirationCarouselDataView) {
        this.inspirationCarouselDataView = inspirationCarouselDataView;
    }

    public List<InspirationCardDataView> getInspirationCardDataView() {
        return inspirationCardDataView;
    }

    public void setInspirationCardDataView(List<InspirationCardDataView> inspirationCardDataView) {
        this.inspirationCardDataView = inspirationCardDataView;
    }

    public int getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(int defaultView) {
        this.defaultView = defaultView;
    }

    public int getTotalItem() {
        return getProductList().size() + getAdsModel().getData().size();
    }

    public void setRelatedDataView(RelatedDataView relatedDataView) {
        this.relatedDataView = relatedDataView;
    }

    public RelatedDataView getRelatedDataView() {
        return this.relatedDataView;
    }

    public void setBannerDataView(BannerDataView bannerDataView) {
        this.bannerDataView = bannerDataView;
    }

    public BannerDataView getBannerDataView() {
        return this.bannerDataView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productList);
        dest.writeString(this.additionalParams);
        dest.writeString(this.autocompleteApplink);
        dest.writeString(this.responseCode);
        dest.writeString(this.keywordProcess);
        dest.writeParcelable(this.tickerModel, flags);
        dest.writeParcelable(this.suggestionModel, flags);
        dest.writeInt(this.totalData);
        dest.writeInt(this.totalItem);
        dest.writeByte(this.isQuerySafe ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.adsModel, flags);
        dest.writeParcelable(this.cpmModel, flags);
        dest.writeParcelable(this.globalNavDataView, flags);
        dest.writeInt(this.defaultView);
    }

    protected ProductDataView(Parcel in) {
        this.productList = in.createTypedArrayList(ProductItemDataView.CREATOR);
        this.additionalParams = in.readString();
        this.autocompleteApplink = in.readString();
        this.responseCode = in.readString();
        this.keywordProcess = in.readString();
        this.tickerModel = in.readParcelable(TickerDataView.class.getClassLoader());
        this.suggestionModel = in.readParcelable(SuggestionDataView.class.getClassLoader());
        this.totalData = in.readInt();
        this.totalItem = in.readInt();
        this.isQuerySafe = in.readByte() != 0;
        this.adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        this.cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
        this.globalNavDataView = in.readParcelable(GlobalNavDataView.class.getClassLoader());
        this.defaultView = in.readInt();
    }

    public static final Creator<ProductDataView> CREATOR = new Creator<ProductDataView>() {
        @Override
        public ProductDataView createFromParcel(Parcel source) {
            return new ProductDataView(source);
        }

        @Override
        public ProductDataView[] newArray(int size) {
            return new ProductDataView[size];
        }
    };
}
