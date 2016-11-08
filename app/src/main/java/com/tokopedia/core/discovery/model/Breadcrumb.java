package com.tokopedia.core.discovery.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sebastianuskh on 9/1/16.
 */
@Parcel
public class Breadcrumb {
    @SerializedName("name_without_total")
    public String name_without_total;

    @SerializedName("child")
    public Breadcrumb[] child;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("tree")
    public String tree;

    @SerializedName("href")
    public String href;

    @SerializedName("parent_id")
    public String parentId;

    @SerializedName("identifier")
    public String identifier;
}
