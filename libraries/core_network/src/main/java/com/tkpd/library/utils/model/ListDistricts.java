package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.NetworkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 2/2/16.
 */
@Deprecated
public class ListDistricts {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    public class Data {
        @SerializedName("districts")
        @Expose
        ArrayList<Districts> districts;

        public ArrayList<Districts> getDistricts() {
            return districts;
        }

        public void setDistricts(ArrayList<Districts> districts) {
            this.districts = districts;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "districts=" + districts +
                    '}';
        }
    }

    public class Districts extends NetworkModel {
        @SerializedName("district_name")
        @Expose
        String districtName;

        @SerializedName("district_id")
        @Expose
        String districtId;

        @SerializedName("jne_code")
        @Expose
        String jneCode;

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

        public String getJneCode() {
            return jneCode;
        }

        public void setJneCode(String jneCode) {
            this.jneCode = jneCode;
        }

        @Override
        public String toString() {
            return "Districts{" +
                    "districtName='" + districtName + '\'' +
                    ", districtId='" + districtId + '\'' +
                    ", jneCode='" + jneCode + '\'' +
                    '}';
        }
    }
}
