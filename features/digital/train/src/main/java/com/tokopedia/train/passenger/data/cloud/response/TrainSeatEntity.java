package com.tokopedia.train.passenger.data.cloud.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainSeatEntity {
//    "seat": {
//        "class": "K",
//                "wagonNo": "1",
//                "row": "3",
//                "column": "E"
//    }
    @SerializedName("class")
    @Expose
    private String klass;
    @SerializedName("wagonNo")
    @Expose
    private String wagonNo;
    @SerializedName("row")
    @Expose
    private String row;
    @SerializedName("column")
    @Expose
    private String column;

    public String getKlass() {
        return klass;
    }

    public String getWagonNo() {
        return wagonNo;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }
}
