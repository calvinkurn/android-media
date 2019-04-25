package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModerateSubmitInput {


        public ModerateSubmitInput(ArrayList<Integer> shopIDs, Integer status, String notes) {
                this.shopIDs = shopIDs;
                this.status = status;
                this.notes = notes;
        }

        @SerializedName("shopIDs")
        @Expose
        private ArrayList<Integer> shopIDs;

        @SerializedName("status")
        @Expose
        private Integer status;

        @SerializedName("notes")
        @Expose
        private String notes;


        public ArrayList<Integer> getShopIDs() {
                return shopIDs;
        }

        public void setShopIDs(ArrayList<Integer> shopIDs) {
                this.shopIDs = shopIDs;
        }

        public Integer getStatus() {
                return status;
        }

        public void setStatus(Integer status) {
                this.status = status;
        }

        public String getNotes() {
                return notes;
        }

        public void setNotes(String notes) {
                this.notes = notes;
        }
}





