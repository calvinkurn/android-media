/*
 * Created By Kulomady on 11/25/16 11:30 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:30 PM
 */

package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 9/1/16.
 */

public class Breadcrumb implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name_without_total);
        dest.writeList(this.child);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.tree);
        dest.writeString(this.href);
        dest.writeString(this.parentId);
        dest.writeString(this.identifier);
    }

    public Breadcrumb() {
    }

    protected Breadcrumb(Parcel in) {
        this.name_without_total = in.readString();
        this.child = new ArrayList<Breadcrumb>();
        in.readList(this.child, Breadcrumb.class.getClassLoader());
        this.id = in.readString();
        this.name = in.readString();
        this.tree = in.readString();
        this.href = in.readString();
        this.parentId = in.readString();
        this.identifier = in.readString();
    }

    public static final Parcelable.Creator<Breadcrumb> CREATOR
            = new Parcelable.Creator<Breadcrumb>() {

        @Override
        public Breadcrumb createFromParcel(Parcel source) {
            return new Breadcrumb(source);
        }

        @Override
        public Breadcrumb[] newArray(int size) {
            return new Breadcrumb[size];
        }
    };
}
