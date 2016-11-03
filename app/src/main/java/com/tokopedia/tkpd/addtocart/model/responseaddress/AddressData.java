package com.tokopedia.tkpd.addtocart.model.responseaddress;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.addtocart.model.responseatcform.Destination;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 21/03/2016.
 */
public class AddressData implements Parcelable {
    private static final String TAG = AddressData.class.getSimpleName();

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private List<Destination> addressList = new ArrayList<>();

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    public List<Destination> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Destination> addressList) {
        this.addressList = addressList;
    }

    protected AddressData(Parcel in) {
        paging = (PagingHandler.PagingHandlerModel)
                in.readValue(PagingHandler.PagingHandlerModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            addressList = new ArrayList<>();
            in.readList(addressList, Destination.class.getClassLoader());
        } else {
            addressList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paging);
        if (addressList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(addressList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddressData> CREATOR =
            new Parcelable.Creator<AddressData>() {
                @Override
                public AddressData createFromParcel(Parcel in) {
                    return new AddressData(in);
                }

                @Override
                public AddressData[] newArray(int size) {
                    return new AddressData[size];
                }
            };


}
