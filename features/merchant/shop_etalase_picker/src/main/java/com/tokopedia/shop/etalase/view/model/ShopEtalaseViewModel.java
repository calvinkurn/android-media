package com.tokopedia.shop.etalase.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.etalase.view.adapter.ShopEtalaseAdapterTypeFactory;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewModel implements Visitable<ShopEtalaseAdapterTypeFactory>, Parcelable{

    public static final int USE_ACE = 1;
    private boolean useAce;
    private String etalaseId;
    private String etalaseName;
    private long etalaseCount;
    private String etalaseBadge;
    private @ShopEtalaseTypeDef
    int type;
    private boolean highlight;

    private boolean isSelected;

    public ShopEtalaseViewModel(String id, String name, boolean isUseAce, @ShopEtalaseTypeDef int type,
                                boolean isHighlight) {
        setEtalaseId(id);
        setEtalaseName(name);
        setUseAce(isUseAce);
        setType(type);
        setHighlight(isHighlight);
    }

    public ShopEtalaseViewModel(ShopEtalaseModel shopEtalaseModel) {
        setEtalaseBadge(shopEtalaseModel.getBadge());
        String idOrAlias;
        if (shopEtalaseModel.getType() == ShopEtalaseTypeDef.ETALASE_DEFAULT) {
            idOrAlias = shopEtalaseModel.getAlias();
        } else {
            idOrAlias = shopEtalaseModel.getId();
        }
        setEtalaseId(idOrAlias);
        setEtalaseName(shopEtalaseModel.getName());
        setEtalaseCount(shopEtalaseModel.getCount());
        setUseAce(shopEtalaseModel.getUseAce());
        setType(shopEtalaseModel.getType());
        setHighlight(shopEtalaseModel.getHighlighted());
    }

    public void setType(@ShopEtalaseTypeDef int type) {
        this.type = type;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public int getType() {
        return type;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public boolean isCustomType(){
        return type == ShopEtalaseTypeDef.ETALASE_CUSTOM;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUseAce(){
        return useAce;
    }

    public void setUseAce(boolean useAce) {
        this.useAce = useAce;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName == null? "" : etalaseName;
    }

    public long getEtalaseCount() {
        return etalaseCount;
    }

    public void setEtalaseCount(long etalaseCount) {
        this.etalaseCount = etalaseCount;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge == null ? "" : etalaseBadge;
    }

    @Override
    public int type(ShopEtalaseAdapterTypeFactory shopEtalaseAdapterTypeFactory) {
        return shopEtalaseAdapterTypeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.useAce ? (byte) 1 : (byte) 0);
        dest.writeString(this.etalaseId);
        dest.writeString(this.etalaseName);
        dest.writeLong(this.etalaseCount);
        dest.writeString(this.etalaseBadge);
        dest.writeInt(this.type);
        dest.writeByte(this.highlight ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected ShopEtalaseViewModel(Parcel in) {
        this.useAce = in.readByte() != 0;
        this.etalaseId = in.readString();
        this.etalaseName = in.readString();
        this.etalaseCount = in.readLong();
        this.etalaseBadge = in.readString();
        this.type = in.readInt();
        this.highlight = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ShopEtalaseViewModel> CREATOR = new Creator<ShopEtalaseViewModel>() {
        @Override
        public ShopEtalaseViewModel createFromParcel(Parcel source) {
            return new ShopEtalaseViewModel(source);
        }

        @Override
        public ShopEtalaseViewModel[] newArray(int size) {
            return new ShopEtalaseViewModel[size];
        }
    };
}
