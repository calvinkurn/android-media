package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 17/12/2015.
 */
@Parcel
public class GetEtalaseModel {
    /**
     * this is for parcelable
     */
    public GetEtalaseModel(){}

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("message_error")
    @Expose
    ArrayList<String> message_error;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @Parcel
    public static class Data{
        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("list")
        @Expose
        List<EtalaseModel> etalaseModels;

        @SerializedName("paging")
        @Expose
        Paging paging;

        @SerializedName("is_allow")
        @Expose
        int is_allow;

        public List<EtalaseModel> getEtalaseModels() {
            return etalaseModels;
        }

        public void setEtalaseModels(List<EtalaseModel> etalaseModels) {
            this.etalaseModels = etalaseModels;
        }
    }

    @Parcel
    public static class EtalaseModel{
        /**
         * This is for parcelable
         */
        public EtalaseModel(){}

        @SerializedName("etalase_name")
        @Expose
        String etalase_name;

        @SerializedName("etalase_id")
        @Expose
        String etalase_id;

        @SerializedName("etalase_num_product")
        @Expose
        String etalase_num_product;

        @SerializedName("etalase_total_product")
        @Expose
        String etalase_total_product;

        // this is local db id after insert
        long DbId;

        public long getDbId() {
            return DbId;
        }

        public void setDbId(long dbId) {
            DbId = dbId;
        }

        public String getEtalase_name() {
            return etalase_name;
        }

        public void setEtalase_name(String etalase_name) {
            this.etalase_name = etalase_name;
        }

        public String getEtalase_id() {
            return etalase_id;
        }

        public void setEtalase_id(String etalase_id) {
            this.etalase_id = etalase_id;
        }

        public String getEtalase_num_product() {
            return etalase_num_product;
        }

        public void setEtalase_num_product(String etalase_num_product) {
            this.etalase_num_product = etalase_num_product;
        }

        public String getEtalase_total_product() {
            return etalase_total_product;
        }

        public void setEtalase_total_product(String etalase_total_product) {
            this.etalase_total_product = etalase_total_product;
        }
    }

    @Parcel
    public static class Paging{

        /**
         * This is for parcelable
         */
        public Paging(){}

        @SerializedName("uri_next")
        @Expose
        String uri_previous;

        @SerializedName("uri_previous")
        @Expose
        String uri_next;
    }
}
