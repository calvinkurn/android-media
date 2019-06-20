package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.discovery.common.data.DataValue;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel implements Parcelable {

    private List<ProductItemViewModel> productList = new ArrayList<>();
    private boolean hasCatalog;
    private String query;
    private String shareUrl;
    private String additionalParams;
    private SuggestionViewModel suggestionModel;
    private int totalData;
    private int totalItem;
    private boolean imageSearch;
    private DynamicFilterModel dynamicFilterModel;
    private GuidedSearchViewModel guidedSearchViewModel;
    private DataValue quickFilterModel;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;
    private RelatedSearchViewModel relatedSearchModel;
    private GlobalNavViewModel globalNavViewModel;

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

    public boolean isImageSearch() {
        return imageSearch;
    }

    public void setImageSearch(boolean imageSearch) {
        this.imageSearch = imageSearch;
    }

    public DynamicFilterModel getDynamicFilterModel() {
        return dynamicFilterModel;
    }

    public void setDynamicFilterModel(DynamicFilterModel dynamicFilterModel) {
        this.dynamicFilterModel = dynamicFilterModel;
    }

    public GuidedSearchViewModel getGuidedSearchViewModel() {
        return guidedSearchViewModel;
    }

    public void setGuidedSearchViewModel(GuidedSearchViewModel guidedSearchViewModel) {
        this.guidedSearchViewModel = guidedSearchViewModel;
    }

    public DataValue getQuickFilterModel() {
        return quickFilterModel;
    }

    public void setQuickFilterModel(DataValue quickFilterModel) {
        this.quickFilterModel = quickFilterModel;
    }

    public ProductViewModel() {
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<ProductItemViewModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItemViewModel> productList) {
        this.productList = productList;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public SuggestionViewModel getSuggestionModel() {
        return suggestionModel;
    }

    public void setSuggestionModel(SuggestionViewModel suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public RelatedSearchViewModel getRelatedSearchModel() {
        return relatedSearchModel;
    }

    public void setRelatedSearchModel(RelatedSearchViewModel relatedSearchModel) {
        this.relatedSearchModel = relatedSearchModel;
    }

    public GlobalNavViewModel getGlobalNavViewModel() {
        return globalNavViewModel;
    }

    public void setGlobalNavViewModel(GlobalNavViewModel globalNavViewModel) {
        this.globalNavViewModel = globalNavViewModel;
    }

    public int getTotalItem() {
        return getProductList().size() + getAdsModel().getData().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productList);
        dest.writeByte(this.hasCatalog ? (byte) 1 : (byte) 0);
        dest.writeString(this.query);
        dest.writeString(this.shareUrl);
        dest.writeString(this.additionalParams);
        dest.writeParcelable(this.suggestionModel, flags);
        dest.writeInt(this.totalData);
        dest.writeInt(this.totalItem);
        dest.writeByte(this.imageSearch ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.dynamicFilterModel, flags);
        dest.writeParcelable(this.guidedSearchViewModel, flags);
        dest.writeParcelable(this.quickFilterModel, flags);
        dest.writeParcelable(this.adsModel, flags);
        dest.writeParcelable(this.cpmModel, flags);
        dest.writeParcelable(this.relatedSearchModel, flags);
        dest.writeParcelable(this.globalNavViewModel, flags);
    }

    protected ProductViewModel(Parcel in) {
        this.productList = in.createTypedArrayList(ProductItemViewModel.CREATOR);
        this.hasCatalog = in.readByte() != 0;
        this.query = in.readString();
        this.shareUrl = in.readString();
        this.additionalParams = in.readString();
        this.suggestionModel = in.readParcelable(SuggestionViewModel.class.getClassLoader());
        this.totalData = in.readInt();
        this.totalItem = in.readInt();
        this.imageSearch = in.readByte() != 0;
        this.dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        this.guidedSearchViewModel = in.readParcelable(GuidedSearchViewModel.class.getClassLoader());
        this.quickFilterModel = in.readParcelable(DataValue.class.getClassLoader());
        this.adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        this.cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
        this.relatedSearchModel = in.readParcelable(RelatedSearchViewModel.class.getClassLoader());
        this.globalNavViewModel = in.readParcelable(GlobalNavViewModel.class.getClassLoader());
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
