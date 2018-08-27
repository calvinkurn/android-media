package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/8/17.
 */

public class ProductListManageModelView implements Parcelable {
    boolean hasNextPage;
    List<ProductManageViewModel> productManageViewModels;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<ProductManageViewModel> getProductManageViewModels() {
        return productManageViewModels;
    }

    public void setProductListPickerViewModels(List<ProductManageViewModel> productManageViewModels) {
        this.productManageViewModels = productManageViewModels;
    }

    public ProductListManageModelView() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.hasNextPage ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.productManageViewModels);
    }

    protected ProductListManageModelView(Parcel in) {
        this.hasNextPage = in.readByte() != 0;
        this.productManageViewModels = in.createTypedArrayList(ProductManageViewModel.CREATOR);
    }

    public static final Creator<ProductListManageModelView> CREATOR = new Creator<ProductListManageModelView>() {
        @Override
        public ProductListManageModelView createFromParcel(Parcel source) {
            return new ProductListManageModelView(source);
        }

        @Override
        public ProductListManageModelView[] newArray(int size) {
            return new ProductListManageModelView[size];
        }
    };
}
