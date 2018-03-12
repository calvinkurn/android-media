//package com.tokopedia.core.network.entity.discovery;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Created by sachinbansal on 1/10/18.
// */
//
//public class ImageSearchResponse implements Serializable {
//
//
//    @SerializedName("Auctions")
//    @Expose
//    private List<Auction> auctions;
//
//    @SerializedName("Head")
//    @Expose
//    private Head head;
//
//    @SerializedName("PicInfo")
//    @Expose
//    private PicInfo picInfo;
//
//    public List<Auction> getAuctions() {
//        return auctions;
//    }
//
//    public void setAuctions(List<Auction> auctions) {
//        this.auctions = auctions;
//    }
//
//
//    public static class Head {
//
//        @SerializedName("SearchTime")
//        @Expose
//        private Integer searchTime;
//
//        @SerializedName("DocsFound")
//        @Expose
//        private Integer docsFound;
//
//        @SerializedName("DocsReturn")
//        @Expose
//        private Integer docsReturn;
//
//        public Head() {
//        }
//
//        public Integer getSearchTime() {
//            return this.searchTime;
//        }
//
//        public void setSearchTime(Integer searchTime) {
//            this.searchTime = searchTime;
//        }
//
//        public Integer getDocsFound() {
//            return this.docsFound;
//        }
//
//        public void setDocsFound(Integer docsFound) {
//            this.docsFound = docsFound;
//        }
//
//        public Integer getDocsReturn() {
//            return this.docsReturn;
//        }
//
//        public void setDocsReturn(Integer docsReturn) {
//            this.docsReturn = docsReturn;
//        }
//    }
//
//    public static class PicInfo {
//        private String category;
//        private String region;
//        private List<PicInfo.Category> allCategory;
//
//        public PicInfo() {
//        }
//
//        public String getCategory() {
//            return this.category;
//        }
//
//        public void setCategory(String category) {
//            this.category = category;
//        }
//
//        public String getRegion() {
//            return this.region;
//        }
//
//        public void setRegion(String region) {
//            this.region = region;
//        }
//
//        public List<PicInfo.Category> getAllCategory() {
//            return this.allCategory;
//        }
//
//        public void setAllCategory(List<PicInfo.Category> allCategory) {
//            this.allCategory = allCategory;
//        }
//
//        public static class Category {
//            private String name;
//            private String id;
//
//            public Category() {
//            }
//
//            public String getName() {
//                return this.name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public String getId() {
//                return this.id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//        }
//    }
//
//
//    public class Auction implements Serializable {
//
//        @SerializedName("sortExprValues")
//        @Expose
//        private String sortExprValues;
//        @SerializedName("CUSTOM_finalScore")
//        @Expose
//        private String cUSTOMFinalScore;
//        @SerializedName("cnnScore")
//        @Expose
//        private String cnnScore;
//        @SerializedName("cust_content")
//        @Expose
//        private String custContent;
//        @SerializedName("pic_name")
//        @Expose
//        private String picName;
//        @SerializedName("product_id")
//        @Expose
//        private String productId;
//        @SerializedName("cat_id")
//        @Expose
//        private String catId;
//
//        public String getSortExprValues() {
//            return sortExprValues;
//        }
//
//        public void setSortExprValues(String sortExprValues) {
//            this.sortExprValues = sortExprValues;
//        }
//
//        public String getCUSTOMFinalScore() {
//            return cUSTOMFinalScore;
//        }
//
//        public void setCUSTOMFinalScore(String cUSTOMFinalScore) {
//            this.cUSTOMFinalScore = cUSTOMFinalScore;
//        }
//
//        public String getCnnScore() {
//            return cnnScore;
//        }
//
//        public void setCnnScore(String cnnScore) {
//            this.cnnScore = cnnScore;
//        }
//
//        public String getCustContent() {
//            return custContent;
//        }
//
//        public void setCustContent(String custContent) {
//            this.custContent = custContent;
//        }
//
//        public String getPicName() {
//            return picName;
//        }
//
//        public void setPicName(String picName) {
//            this.picName = picName;
//        }
//
//        public String getProductId() {
//            return productId;
//        }
//
//        public void setProductId(String productId) {
//            this.productId = productId;
//        }
//
//        public String getCatId() {
//            return catId;
//        }
//
//        public void setCatId(String catId) {
//            this.catId = catId;
//        }
//
//    }
//
//
//    public static class Search implements Serializable {
//
//        @SerializedName("id")
//        @Expose
//        private String id;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//    }
//
//    public static class AllCategory implements Serializable {
//
//        @SerializedName("id")
//        @Expose
//        private String id;
//        @SerializedName("name")
//        @Expose
//        private String name;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//    }
//
//
//}
