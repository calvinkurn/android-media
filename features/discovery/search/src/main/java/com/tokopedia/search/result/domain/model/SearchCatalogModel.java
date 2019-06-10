package com.tokopedia.search.result.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchCatalogModel {

    @SerializedName("search_url")
    @Expose
    public String searchURL = "";

    @SerializedName("share_url")
    @Expose
    public String shareURL = "";

    @SerializedName("paging")
    @Expose
    public Paging paging = new Paging();

    @SerializedName("st")
    @Expose
    public String st = "";

    @SerializedName("total_record")
    @Expose
    public String totalRecord = "";

    @SerializedName("catalogs")
    @Expose
    public List<Catalog> catalogList = new ArrayList<>();

    @SerializedName("category")
    public List<BreadCrumb> breadCrumbList = new ArrayList<>();

    @SerializedName("has_catalog")
    @Expose
    public String hasCatalog = "";

    public static class Paging {

        @SerializedName("uri_next")
        @Expose
        public String uriNext = "";

        @SerializedName("uri_previous")
        @Expose
        public String uriPrevious = "";
    }

    public static class Catalog {
        @SerializedName("catalog_id")
        @Expose
        public String catalogId = "";

        @SerializedName("catalog_name")
        @Expose
        public String catalogName = "";

        @SerializedName("catalog_price")
        @Expose
        public String catalogPrice = "";

        @SerializedName("catalog_price_raw")
        @Expose
        public String catalogPriceRaw = "";

        @SerializedName("catalog_uri")
        @Expose
        public String catalogUri = "";

        @SerializedName("catalog_image")
        @Expose
        public String catalogImage = "";

        @SerializedName("catalog_image_300")
        @Expose
        public String catalogImage300 = "";

        @SerializedName("catalog_description")
        @Expose
        public String catalogDescription = "";

        @SerializedName("catalog_count_product")
        @Expose
        public String catalogCountProduct = "";
    }

    public static class BreadCrumb {

        @SerializedName("id")
        @Expose
        public String ID = "";

        @SerializedName("name")
        @Expose
        public String name = "";

        @SerializedName("total_data")
        @Expose
        public String totalData = "";

        @SerializedName("parent_id")
        @Expose
        public String parentId = "";

        @SerializedName("identifier")
        @Expose
        public String identifier = "";

        @SerializedName("child")
        @Expose
        public List<BreadCrumb> child = new ArrayList<>();

        @SerializedName("tree")
        @Expose
        public String tree = "";

        @SerializedName("href")
        @Expose
        public String href = "";

        @SerializedName("name_without_total")
        @Expose
        public String nameWithoutTotal = "";
    }
}
