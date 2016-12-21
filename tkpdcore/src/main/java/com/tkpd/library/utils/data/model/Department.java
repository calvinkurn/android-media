package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 2/9/16.
 */
public class Department {
    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("categories")
        @Expose
        ArrayList<Categories> categories;

        public ArrayList<Categories> getCategories() {
            return categories;
        }

        public void setCategories(ArrayList<Categories> categories) {
            this.categories = categories;
        }
    }

    public static class Categories {
        @SerializedName("child")
        @Expose
        ArrayList<Child> child;

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("weight")
        @Expose
        String weight;

        @SerializedName("tree")
        @Expose
        String tree;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("has_catalog")
        @Expose
        String has_catalog;

        @SerializedName("parent")
        @Expose
        String parent;

        @SerializedName("identifier")
        @Expose
        String identifier;

        @SerializedName("url")
        @Expose
        String url;

        public ArrayList<Child> getChild() {
            return child;
        }

        public void setChild(ArrayList<Child> child) {
            this.child = child;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getTree() {
            return tree;
        }

        public void setTree(String tree) {
            this.tree = tree;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHas_catalog() {
            return has_catalog;
        }

        public void setHas_catalog(String has_catalog) {
            this.has_catalog = has_catalog;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Child {
        @SerializedName("child")
        @Expose
        ArrayList<Child> child;

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("weight")
        @Expose
        String weight;

        @SerializedName("tree")
        @Expose
        String tree;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("has_catalog")
        @Expose
        String has_catalog;

        @SerializedName("parent")
        @Expose
        String parent;

        @SerializedName("identifier")
        @Expose
        String identifier;

        @SerializedName("url")
        @Expose
        String url;

        public ArrayList<Child> getChild() {
            return child;
        }

        public void setChild(ArrayList<Child> child) {
            this.child = child;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getTree() {
            return tree;
        }

        public void setTree(String tree) {
            this.tree = tree;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHas_catalog() {
            return has_catalog;
        }

        public void setHas_catalog(String has_catalog) {
            this.has_catalog = has_catalog;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
