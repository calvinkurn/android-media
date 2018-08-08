package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 2/7/18.
 */

public class SendEmailEntity {
    @SerializedName("meta")
    private MetaEntity meta;

    public SendEmailEntity() {
    }

    public MetaEntity getMeta() {
        return meta;
    }

    public void setMeta(MetaEntity meta) {
        this.meta = meta;
    }

    public class MetaEntity {
        @SerializedName("status")
        private String status;

        public MetaEntity() {
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
