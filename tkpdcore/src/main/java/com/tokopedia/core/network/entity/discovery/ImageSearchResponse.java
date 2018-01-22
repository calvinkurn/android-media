package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchResponse implements Serializable {


    @SerializedName("oas_search")
    @Expose
    private OasSearch oasSearch;

    public OasSearch getOasSearch() {
        return oasSearch;
    }

    public void setOasSearch(OasSearch oasSearch) {
        this.oasSearch = oasSearch;
    }


    public static class OasSearch implements Serializable {

        @SerializedName("trace")
        @Expose
        private Trace trace;
        @SerializedName("auctions")
        @Expose
        private List<Auction> auctions = null;
        @SerializedName("pict_pp")
        @Expose
        private PictPp pictPp;

        public Trace getTrace() {
            return trace;
        }

        public void setTrace(Trace trace) {
            this.trace = trace;
        }

        public List<Auction> getAuctions() {
            return auctions;
        }

        public void setAuctions(List<Auction> auctions) {
            this.auctions = auctions;
        }

        public PictPp getPictPp() {
            return pictPp;
        }

        public void setPictPp(PictPp pictPp) {
            this.pictPp = pictPp;
        }
    }

    public static class Trace implements Serializable {

        @SerializedName("search")
        @Expose
        private Search search;

        public Search getSearch() {
            return search;
        }

        public void setSearch(Search search) {
            this.search = search;
        }

    }

    public class Auction implements Serializable {

        @SerializedName("sortExprValues")
        @Expose
        private String sortExprValues;
        @SerializedName("CUSTOM_finalScore")
        @Expose
        private String cUSTOMFinalScore;
        @SerializedName("cnnScore")
        @Expose
        private String cnnScore;
        @SerializedName("cust_content")
        @Expose
        private String custContent;
        @SerializedName("pic_name")
        @Expose
        private String picName;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("cat_id")
        @Expose
        private String catId;

        public String getSortExprValues() {
            return sortExprValues;
        }

        public void setSortExprValues(String sortExprValues) {
            this.sortExprValues = sortExprValues;
        }

        public String getCUSTOMFinalScore() {
            return cUSTOMFinalScore;
        }

        public void setCUSTOMFinalScore(String cUSTOMFinalScore) {
            this.cUSTOMFinalScore = cUSTOMFinalScore;
        }

        public String getCnnScore() {
            return cnnScore;
        }

        public void setCnnScore(String cnnScore) {
            this.cnnScore = cnnScore;
        }

        public String getCustContent() {
            return custContent;
        }

        public void setCustContent(String custContent) {
            this.custContent = custContent;
        }

        public String getPicName() {
            return picName;
        }

        public void setPicName(String picName) {
            this.picName = picName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

    }

    public static class PictPp implements Serializable {

        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("region")
        @Expose
        private String region;
        @SerializedName("all_category")
        @Expose
        private List<AllCategory> allCategory = null;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<AllCategory> getAllCategory() {
            return allCategory;
        }

        public void setAllCategory(List<AllCategory> allCategory) {
            this.allCategory = allCategory;
        }

    }

    public static class Search implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class AllCategory implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }


}
