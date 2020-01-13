/*
 * Created By Kulomady on 11/25/16 11:12 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:12 PM
 */

package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.interfaces.Convert;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by noiz354 on 3/17/16.
 */

@Deprecated
public class BrowseCatalogModel {
    @SerializedName("data")
    public Result result;

    @SerializedName("status")
    public String status;

    @SerializedName("config")
    public BrowseProductModel.Config config;

    @SerializedName("server_process_time")
    public String serverProcessTime;

    public class Result {

        @SerializedName("share_url")
        public String shareUrl;

        @SerializedName("catalogs")
        public Catalogs[] catalogs;

        @SerializedName("paging")
        public PagingHandler.PagingHandlerModel paging;

        @SerializedName("category")
        public List<Breadcrumb> breadcrumb;
    }

    public static class Catalogs extends Convert.DefaultConvert<Catalogs, CatalogModel> {
        @SerializedName("catalog_name")
        public String catalogName;

        @SerializedName("catalog_description")
        public String catalogDescription;

        @SerializedName("catalog_image")
        public String catalogImage;

        @SerializedName("catalog_count_product")
        public String catalogCountProduct;

        @SerializedName("catalog_image_300")
        public String catalogImage300;

        @SerializedName("catalog_price")
        public String catalogPrice;

        @SerializedName("catalog_uri")
        public String catalogUri;

        @SerializedName("catalog_id")
        public String catalogId;

        @Override
        public CatalogModel from(Catalogs data) {
            return new CatalogModel(data);
        }
    }
}
