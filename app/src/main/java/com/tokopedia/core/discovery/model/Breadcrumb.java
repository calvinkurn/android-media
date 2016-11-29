/*
 * Created By Kulomady on 11/25/16 11:30 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:30 PM
 */

package com.tokopedia.core.discovery.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sebastianuskh on 9/1/16.
 */
@Parcel
public class Breadcrumb {
    @SerializedName("name_without_total")
    public String name_without_total;

    @SerializedName("child")
    public List<Breadcrumb> child;

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
