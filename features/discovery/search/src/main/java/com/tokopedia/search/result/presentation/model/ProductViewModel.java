package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel implements Parcelable {

    private List<ProductItemViewModel> productList = new ArrayList<>();
    private String additionalParams;
    private String autocompleteApplink;
    private String responseCode;
    private String keywordProcess;
    private String errorMessage;
    private TickerViewModel tickerModel;
    private SuggestionViewModel suggestionModel;
    private int totalData;
    private int totalItem;
    private boolean isQuerySafe;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;
    private GlobalNavViewModel globalNavViewModel;
    private List<InspirationCarouselViewModel> inspirationCarouselViewModel = new ArrayList<>();
    private List<InspirationCardViewModel> inspirationCardViewModel = new ArrayList<>();
    private int defaultView;
    private RelatedViewModel relatedViewModel;
    private String totalDataText = "";

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

    public void setIsQuerySafe(boolean querySafe) {
        isQuerySafe = querySafe;
    }

    public ProductViewModel() {
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

    public List<ProductItemViewModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItemViewModel> productList) {
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

    public TickerViewModel getTickerModel() {
        return tickerModel;
    }

    public void setTickerModel(TickerViewModel tickerModel) {
        this.tickerModel = tickerModel;
    }

    public SuggestionViewModel getSuggestionModel() {
        return suggestionModel;
    }

    public void setSuggestionModel(SuggestionViewModel suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public GlobalNavViewModel getGlobalNavViewModel() {
        return globalNavViewModel;
    }

    public void setGlobalNavViewModel(GlobalNavViewModel globalNavViewModel) {
        this.globalNavViewModel = globalNavViewModel;
    }

    public List<InspirationCarouselViewModel> getInspirationCarouselViewModel() {
        return inspirationCarouselViewModel;
    }

    public void setInspirationCarouselViewModel(List<InspirationCarouselViewModel> inspirationCarouselViewModel) {
        this.inspirationCarouselViewModel = inspirationCarouselViewModel;
    }

    public List<InspirationCardViewModel> getInspirationCardViewModel() {
        return inspirationCardViewModel;
    }

    public void setInspirationCardViewModel(List<InspirationCardViewModel> inspirationCardViewModel) {
        this.inspirationCardViewModel = inspirationCardViewModel;
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

    public void setRelatedViewModel(RelatedViewModel relatedViewModel) {
        this.relatedViewModel = relatedViewModel;
    }

    public RelatedViewModel getRelatedViewModel() {
        return this.relatedViewModel;
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
        dest.writeParcelable(this.globalNavViewModel, flags);
        dest.writeInt(this.defaultView);
    }

    protected ProductViewModel(Parcel in) {
        this.productList = in.createTypedArrayList(ProductItemViewModel.CREATOR);
        this.additionalParams = in.readString();
        this.autocompleteApplink = in.readString();
        this.responseCode = in.readString();
        this.keywordProcess = in.readString();
        this.tickerModel = in.readParcelable(TickerViewModel.class.getClassLoader());
        this.suggestionModel = in.readParcelable(SuggestionViewModel.class.getClassLoader());
        this.totalData = in.readInt();
        this.totalItem = in.readInt();
        this.isQuerySafe = in.readByte() != 0;
        this.adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        this.cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
        this.globalNavViewModel = in.readParcelable(GlobalNavViewModel.class.getClassLoader());
        this.defaultView = in.readInt();
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel source) {
            return new ProductViewModel(source);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };
}
