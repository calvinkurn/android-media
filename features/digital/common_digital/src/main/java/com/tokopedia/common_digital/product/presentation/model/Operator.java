package com.tokopedia.common_digital.product.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Operator implements Parcelable, BaseWidgetItem {

    public static final String DEFAULT_TYPE_CONTRACT = "operator";

    private String operatorId;
    private String operatorType;
    private String name;
    private String image;
    private String lastorderUrl;
    private int defaultProductId;
    private Rule rule;
    private List<String> prefixList;
    private List<ClientNumber> clientNumberList;
    private List<Product> productList;
    private String ussdCode;

    public Operator(String operatorId, String operatorType, String name, String image, String lastorderUrl,
                    int defaultProductId, Rule rule, List<String> prefixList, List<ClientNumber> clientNumberList,
                    List<Product> productList, String ussdCode) {
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.name = name;
        this.image = image;
        this.lastorderUrl = lastorderUrl;
        this.defaultProductId = defaultProductId;
        this.rule = rule;
        this.prefixList = prefixList;
        this.clientNumberList = clientNumberList;
        this.productList = productList;
        this.ussdCode = ussdCode;
    }

    public String getUssdCode() {return ussdCode;}

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLastorderUrl() {
        return lastorderUrl;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public List<ClientNumber> getClientNumberList() {
        return clientNumberList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public Rule getRule() {
        return rule;
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

    public static final Creator<Operator> CREATOR = new Creator<Operator>() {
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
