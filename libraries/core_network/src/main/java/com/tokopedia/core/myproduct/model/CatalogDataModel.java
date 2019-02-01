package com.tokopedia.core.myproduct.model;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.myproduct.adapter.ItemImageAndText;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 18/01/2016.
 */

@Deprecated
public class CatalogDataModel {

    @SerializedName("data")
    @Expose
    ArrayList<Catalog> list;

    @SerializedName("message_error")
    @Expose
    ArrayList<String> messageError;

    public ArrayList<Catalog> getList() {
        return list;
    }

    public void setList(ArrayList<Catalog> list) {
        this.list = list;
    }

    public ArrayList<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(ArrayList<String> messageError) {
        this.messageError = messageError;
    }

    @Parcel
    public static class Catalog extends ItemImageAndText {
        public Catalog(){}

        @SerializedName("name")
        @Expose
        String catalogName;

        @SerializedName("description")
        @Expose
        String catalogDescription;

        @SerializedName("image_uri")
        @Expose
        String catalogImage;

        @SerializedName("count_product")
        @Expose
        String catalogCountShop;

        @SerializedName("price_min")
        @Expose
        String catalogPrice;

        @SerializedName("id")
        @Expose
        String catalogId;

        @SerializedName("uri")
        @Expose
        String catalogUri;

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        public String getCatalogDescription() {
            return catalogDescription;
        }

        public void setCatalogDescription(String catalogDescription) {
            this.catalogDescription = catalogDescription;
        }

        public String getCatalogImage() {
            return catalogImage;
        }

        public void setCatalogImage(String catalogImage) {
            this.catalogImage = catalogImage;
        }

        public String getCatalogCountShop() {
            return catalogCountShop;
        }

        public void setCatalogCountShop(String catalogCountShop) {
            this.catalogCountShop = catalogCountShop;
        }

        public String getCatalogPrice() {
            return catalogPrice;
        }

        public void setCatalogPrice(String catalogPrice) {
            this.catalogPrice = catalogPrice;
        }

        public String getCatalogId() {
            return catalogId;
        }

        public void setCatalogId(String catalogId) {
            this.catalogId = catalogId;
        }

        public String getCatalogUri() {
            return catalogUri;
        }

        public void setCatalogUri(String catalogUri) {
            this.catalogUri = catalogUri;
        }

        @Override
        public String toString() {
            return "Catalog{" +
                    "catalogName='" + catalogName + '\'' +
                    ", catalogDescription='" + catalogDescription + '\'' +
                    ", catalogImage='" + catalogImage + '\'' +
                    ", catalogCountShop='" + catalogCountShop + '\'' +
                    ", catalogPrice='" + catalogPrice + '\'' +
                    ", catalogId='" + catalogId + '\'' +
                    ", catalogUri='" + catalogUri + '\'' +
                    '}';
        }

        @Override
        public Pair<String, String> getData() {
            return new Pair<>(catalogImage, catalogName);
        }
    }
}
