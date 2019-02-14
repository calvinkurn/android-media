package com.tokopedia.checkout.domain.datamodel.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

public class PeopleAddressModel implements Parcelable {

    private List<RecipientAddressModel> recipientAddressModelList;
    private Token token;
    private Paging paging;

    public List<RecipientAddressModel> getRecipientAddressModelList() {
        return recipientAddressModelList;
    }

    public void setRecipientAddressModelList(List<RecipientAddressModel> recipientAddressModelList) {
        this.recipientAddressModelList = recipientAddressModelList;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.recipientAddressModelList);
        dest.writeParcelable(this.token, flags);
        dest.writeParcelable(this.paging, flags);
    }

    public PeopleAddressModel() {
    }

    protected PeopleAddressModel(Parcel in) {
        this.recipientAddressModelList = in.createTypedArrayList(RecipientAddressModel.CREATOR);
        this.token = in.readParcelable(Token.class.getClassLoader());
        this.paging = in.readParcelable(Paging.class.getClassLoader());
    }

    public static final Creator<PeopleAddressModel> CREATOR = new Creator<PeopleAddressModel>() {
        @Override
        public PeopleAddressModel createFromParcel(Parcel source) {
            return new PeopleAddressModel(source);
        }

        @Override
        public PeopleAddressModel[] newArray(int size) {
            return new PeopleAddressModel[size];
        }
    };
}
