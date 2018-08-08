package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.NetworkModel;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 2/3/16.
 */
public class ListBank {
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
    String server_process_time;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;



    public class Data {
        @SerializedName("list")
        @Expose
        ArrayList<Bank> list;

        @SerializedName("paging")
        @Expose
        PagingHandler.PagingHandlerModel paging;

        public ArrayList<Bank> getList() {
            return list;
        }

        public void setList(ArrayList<Bank> list) {
            this.list = list;
        }

        public PagingHandler.PagingHandlerModel getPaging() {
            return paging;
        }

        public void setPaging(PagingHandler.PagingHandlerModel paging) {
            this.paging = paging;
        }
    }

    public class Bank extends NetworkModel {
        @SerializedName("bank_id")
        @Expose
        String bankId;

        @SerializedName("bank_name")
        @Expose
        String bankName;

        @SerializedName("bank_clearing_code")
        @Expose
        String bankClearingCode;

        @SerializedName("bank_abbreviation")
        @Expose
        String bankAbbreviation;

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankClearingCode() {
            return bankClearingCode;
        }

        public void setBankClearingCode(String bankClearingCode) {
            this.bankClearingCode = bankClearingCode;
        }

        public String getBankAbbreviation() {
            return bankAbbreviation;
        }

        public void setBankAbbreviation(String bankAbbreviation) {
            this.bankAbbreviation = bankAbbreviation;
        }
    }
}
