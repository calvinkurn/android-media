package com.tokopedia.core.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Tkpd_Eka on 11/9/2015.
 */
public class GetShopProductParam implements Parcelable {

    public static final String DEFAULT_ALL_ETALASE_ID = "etalase";

    private int page = 1;
    private String keyword = "";
    private String etalaseId = DEFAULT_ALL_ETALASE_ID;
    private String orderBy = "";
    private int selectedEtalase = 0;
    private int listState = 3;
    private int perPage = 8;
    private boolean useAce;

    public GetShopProductParam() {

    }


    protected GetShopProductParam(Parcel in) {
        page = in.readInt();
        keyword = in.readString();
        etalaseId = in.readString();
        orderBy = in.readString();
        selectedEtalase = in.readInt();
        listState = in.readInt();
        perPage = in.readInt();
        useAce = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(keyword);
        dest.writeString(etalaseId);
        dest.writeString(orderBy);
        dest.writeInt(selectedEtalase);
        dest.writeInt(listState);
        dest.writeInt(perPage);
        dest.writeByte((byte) (useAce ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetShopProductParam> CREATOR = new Creator<GetShopProductParam>() {
        @Override
        public GetShopProductParam createFromParcel(Parcel in) {
            return new GetShopProductParam(in);
        }

        @Override
        public GetShopProductParam[] newArray(int size) {
            return new GetShopProductParam[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getSelectedEtalase() {
        return selectedEtalase;
    }

    public void setSelectedEtalase(int selectedEtalase) {
        this.selectedEtalase = selectedEtalase;
    }

    public int getListState() {
        return listState;
    }

    public void setListState(int listState) {
        this.listState = listState;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public boolean isUseAce() {
        return useAce;
    }

    public void setUseAce(boolean useAce) {
        this.useAce = useAce;
    }
}
