package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/10/18.
 */

public class ShipmentStateProductPreorder {

    @SerializedName("duration_day")
    @Expose
    private int durationDay;

    public ShipmentStateProductPreorder(ShipmentStateProductPreorder.Builder builder) {
        durationDay = builder.durationDay;
    }

    public static final class Builder {
        private int durationDay;

        public Builder() {
        }

        public ShipmentStateProductPreorder.Builder durationDay(int durationDay) {
            this.durationDay = durationDay;
            return this;
        }

        public ShipmentStateProductPreorder build() {
            return new ShipmentStateProductPreorder(this);
        }
    }

}
