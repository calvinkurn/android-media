package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Operator implements Parcelable {

    public static final String DEFAULT_TYPE_CONTRACT = "operator";
    private String operatorId;
    private String operatorType;
    private String name;
    private String image;
    private String lastorderUrl;
    private int defaultProductId;
    private Rule rule;
    private List<String> prefixList = new ArrayList<>();
    private List<ClientNumber> clientNumberList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private String ussdCode;

    public void setUssdCode(String ussdCode) {
        this.ussdCode = ussdCode;
    }

    public String getUssdCode() {return ussdCode;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastorderUrl() {
        return lastorderUrl;
    }

    public void setLastorderUrl(String lastorderUrl) {
        this.lastorderUrl = lastorderUrl;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public void setDefaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    public List<ClientNumber> getClientNumberList() {
        return clientNumberList;
    }

    public void setClientNumberList(List<ClientNumber> clientNumberList) {
        this.clientNumberList = clientNumberList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.operatorId);
        dest.writeString(this.operatorType);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.lastorderUrl);
        dest.writeInt(this.defaultProductId);
        dest.writeParcelable(this.rule, flags);
        dest.writeStringList(this.prefixList);
        dest.writeList(this.clientNumberList);
        dest.writeList(this.productList);
        dest.writeString(this.ussdCode);
    }

    public Operator() {
    }

    protected Operator(Parcel in) {
        this.operatorId = in.readString();
        this.operatorType = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.lastorderUrl = in.readString();
        this.defaultProductId = in.readInt();
        this.rule = in.readParcelable(Rule.class.getClassLoader());
        this.prefixList = in.createStringArrayList();
        this.clientNumberList = new ArrayList<ClientNumber>();
        in.readList(this.clientNumberList, ClientNumber.class.getClassLoader());
        this.productList = new ArrayList<Product>();
        in.readList(this.productList, Product.class.getClassLoader());
        this.ussdCode = in.readString();
    }

    public static final Parcelable.Creator<Operator> CREATOR = new Parcelable.Creator<Operator>() {
        @Override
        public Operator createFromParcel(Parcel source) {
            return new Operator(source);
        }

        @Override
        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
