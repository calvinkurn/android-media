
/*
 * Created By Kulomady on 11/25/16 11:54 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:54 PM
 */

package com.tokopedia.core.discovery.dynamicfilter.facade.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.Breadcrumb;

import java.util.ArrayList;
import java.util.List;

public class HadesV1Model {

    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * @return The data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public void setData(List<Breadcrumb> breadCrumb) {

        List<Category> categories = new ArrayList<>();


        data.setCategories(categories);

    }

    public static class Data {

        @SerializedName("categories")
        @Expose
        private List<Category> categories = new ArrayList<Category>();

        /**
         * @return The categories
         */
        public List<Category> getCategories() {
            return categories;
        }

        /**
         * @param categories The categories
         */
        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

    }


    public static class Category {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("parent")
        @Expose
        private Integer parent;
        @SerializedName("tree")
        @Expose
        private Integer tree;
        @SerializedName("identifier")
        @Expose
        private String identifier;
        @SerializedName("child")
        @Expose
        private List<Category> childList = new ArrayList<>();

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The parent
         */
        public Integer getParent() {
            return parent;
        }

        /**
         * @param parent The parent
         */
        public void setParent(Integer parent) {
            this.parent = parent;
        }

        /**
         * @return The tree
         */
        public Integer getTree() {
            return tree;
        }

        /**
         * @param tree The tree
         */
        public void setTree(Integer tree) {
            this.tree = tree;
        }

        /**
         * @return The identifier
         */
        public String getIdentifier() {
            return identifier;
        }

        /**
         * @param identifier The identifier
         */
        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public List<Category> getChildList() {
            return childList;
        }

        public void setChildList(List<Category> childList) {
            this.childList = childList;
        }

    }
}
