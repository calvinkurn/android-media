package com.tokopedia.core.network.entity.discovery;

import com.tokopedia.core.var.RecyclerViewItem;

/**
 * @author kulomady on 11/26/16.
 */
public class CatalogModel extends RecyclerViewItem {
    public static final int CATALOG_MODEL_TYPE = 1_234_15;

    String catalogName;
    String catalogDescription;
    String catalogImage;
    String catalogCountProduct;
    String catalogImage300;
    String catalogPrice;
    String catalogUri;
    String catalogId;

    public CatalogModel() {
        setType(CATALOG_MODEL_TYPE);
    }

    public CatalogModel(BrowseCatalogModel.Catalogs catalogs) {
        this();
        catalogName = catalogs.catalogName;
        catalogDescription = catalogs.catalogDescription;
        catalogImage = catalogs.catalogImage;
        catalogCountProduct = catalogs.catalogCountProduct;
        catalogImage300 = catalogs.catalogImage300;
        catalogPrice = catalogs.catalogPrice;
        catalogUri = catalogs.catalogUri;
        catalogId = catalogs.catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public String getCatalogCountProduct() {
        return catalogCountProduct;
    }

    public String getCatalogImage300() {
        return catalogImage300;
    }

    public String getCatalogPrice() {
        return catalogPrice;
    }

    public String getCatalogUri() {
        return catalogUri;
    }

    public String getCatalogId() {
        return catalogId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.catalogName);
        dest.writeString(this.catalogDescription);
        dest.writeString(this.catalogImage);
        dest.writeString(this.catalogCountProduct);
        dest.writeString(this.catalogImage300);
        dest.writeString(this.catalogPrice);
        dest.writeString(this.catalogUri);
        dest.writeString(this.catalogId);
    }

    protected CatalogModel(android.os.Parcel in) {
        super(in);
        this.catalogName = in.readString();
        this.catalogDescription = in.readString();
        this.catalogImage = in.readString();
        this.catalogCountProduct = in.readString();
        this.catalogImage300 = in.readString();
        this.catalogPrice = in.readString();
        this.catalogUri = in.readString();
        this.catalogId = in.readString();
    }

    public static final Creator<CatalogModel> CREATOR = new Creator<CatalogModel>() {
        @Override
        public CatalogModel createFromParcel(android.os.Parcel source) {
            return new CatalogModel(source);
        }

        @Override
        public CatalogModel[] newArray(int size) {
            return new CatalogModel[size];
        }
    };
}
