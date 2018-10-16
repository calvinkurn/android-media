package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductBreadcrumb implements Parcelable {
    private static final String TAG = ProductBreadcrumb.class.getSimpleName();

    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @SerializedName("department_identifier")
    @Expose
    private String departmentIdentifier;
    @SerializedName("department_dir_view")
    @Expose
    private Integer departmentDirView;
    @SerializedName("department_id")
    @Expose
    private String departmentId;
    @SerializedName("department_tree")
    @Expose
    private Integer departmentTree;

    public ProductBreadcrumb() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIdentifier() {
        return departmentIdentifier;
    }

    public void setDepartmentIdentifier(String departmentIdentifier) {
        this.departmentIdentifier = departmentIdentifier;
    }

    public Integer getDepartmentDirView() {
        return departmentDirView;
    }

    public void setDepartmentDirView(Integer departmentDirView) {
        this.departmentDirView = departmentDirView;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartmentTree() {
        return departmentTree;
    }

    public void setDepartmentTree(Integer departmentTree) {
        this.departmentTree = departmentTree;
    }

    protected ProductBreadcrumb(Parcel in) {
        departmentName = in.readString();
        departmentIdentifier = in.readString();
        departmentDirView = in.readByte() == 0x00 ? null : in.readInt();
        departmentId = in.readString();
        departmentTree = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departmentName);
        dest.writeString(departmentIdentifier);
        if (departmentDirView == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(departmentDirView);
        }
        dest.writeString(departmentId);
        if (departmentTree == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(departmentTree);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductBreadcrumb> CREATOR = new Creator<ProductBreadcrumb>() {
        @Override
        public ProductBreadcrumb createFromParcel(Parcel in) {
            return new ProductBreadcrumb(in);
        }

        @Override
        public ProductBreadcrumb[] newArray(int size) {
            return new ProductBreadcrumb[size];
        }
    };


    public static class Builder {
        private String departmentName;
        private String departmentIdentifier;
        private Integer departmentDirView;
        private String departmentId;
        private Integer departmentTree;

        private Builder() {
        }

        public static Builder aProductBreadcrumb() {
            return new Builder();
        }

        public Builder setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder setDepartmentIdentifier(String departmentIdentifier) {
            this.departmentIdentifier = departmentIdentifier;
            return this;
        }

        public Builder setDepartmentDirView(Integer departmentDirView) {
            this.departmentDirView = departmentDirView;
            return this;
        }

        public Builder setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder setDepartmentTree(Integer departmentTree) {
            this.departmentTree = departmentTree;
            return this;
        }

        public Builder but() {
            return aProductBreadcrumb().setDepartmentName(departmentName).setDepartmentIdentifier(departmentIdentifier).setDepartmentDirView(departmentDirView).setDepartmentId(departmentId).setDepartmentTree(departmentTree);
        }

        public ProductBreadcrumb build() {
            ProductBreadcrumb productBreadcrumb = new ProductBreadcrumb();
            productBreadcrumb.setDepartmentName(departmentName);
            productBreadcrumb.setDepartmentIdentifier(departmentIdentifier);
            productBreadcrumb.setDepartmentDirView(departmentDirView);
            productBreadcrumb.setDepartmentId(departmentId);
            productBreadcrumb.setDepartmentTree(departmentTree);
            return productBreadcrumb;
        }
    }
}
