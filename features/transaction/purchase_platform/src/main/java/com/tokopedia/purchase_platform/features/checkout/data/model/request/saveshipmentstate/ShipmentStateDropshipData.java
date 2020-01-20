package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateDropshipData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("telp_no")
    @Expose
    private String telpNo;

    public ShipmentStateDropshipData(Builder builder) {
        name = builder.name;
        telpNo = builder.telpNo;
    }

    public static final class Builder {
        private String name;
        private String telpNo;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder telpNo(String telpNo) {
            this.telpNo = telpNo;
            return this;
        }

        public ShipmentStateDropshipData build() {
            return new ShipmentStateDropshipData(this);
        }
    }
}
