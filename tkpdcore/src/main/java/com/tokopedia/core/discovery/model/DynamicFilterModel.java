package com.tokopedia.core.discovery.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

public class DynamicFilterModel {

    @SerializedName("process_time")
    @Expose
    String processTime;
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("status")
    @Expose
    String status;

    /**
     * @return The processTime
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * @param processTime The process_time
     */
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

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

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Parcel
    public static class Data {

        String selected;
        String selectedOb;

        @SerializedName("filter")
        @Expose
        List<Filter> filter = new ArrayList<>();
        @SerializedName("sort")
        @Expose
        List<Sort> sort = new ArrayList<>();

        /**
         * @return The filter
         */
        public List<Filter> getFilter() {
            return filter;
        }

        /**
         * @param filter The filter
         */
        public void setFilter(List<Filter> filter) {
            this.filter = filter;
        }

        /**
         * @return The sort
         */
        public List<Sort> getSort() {
            return sort;
        }

        /**
         * @param sort The sort
         */
        public void setSort(List<Sort> sort) {
            this.sort = sort;
        }

        public String getSelectedOb() {
            return selectedOb;
        }

        public void setSelectedOb(String selectedOb) {
            this.selectedOb = selectedOb;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Parcel
    public static class Filter {
        public static final String TITLE_CATEGORY = "Kategori";
        public static final String TITLE_SHOP = "Toko";

        @SerializedName("title")
        @Expose
        String title;
        @SerializedName("search")
        @Expose
        Search search;
        @SerializedName("options")
        @Expose
        List<Option> options = new ArrayList<>();

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return The search
         */
        public Search getSearch() {
            return search;
        }

        /**
         * @param search The search
         */
        public void setSearch(Search search) {
            this.search = search;
        }

        /**
         * @return The options
         */
        public List<Option> getOptions() {
            return options;
        }

        /**
         * @param options The options
         */
        public void setOptions(List<Option> options) {
            this.options = options;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static Filter createCategory() {
            Filter filter = new Filter();
            filter.setTitle(TITLE_CATEGORY);
            filter.setOptions(new ArrayList<Option>());
            Search search = new Search();
            search.setPlaceholder("");
            search.setSearchable(0);
            filter.setSearch(search);
            return filter;
        }

        public static Filter createShop(){
            Filter filter = new Filter();
            filter.setTitle(TITLE_SHOP);
            filter.setOptions(new ArrayList<Option>());
            Search search = new Search();
            search.setPlaceholder("");
            search.setSearchable(0);
            filter.setSearch(search);
            return filter;
        }
    }


    @Parcel
    public static class Option {

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;

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
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

    }

    @Parcel
    public static class Search {

        @SerializedName("searchable")
        @Expose
        Integer searchable;
        @SerializedName("placeholder")
        @Expose
        String placeholder;

        /**
         * @return The searchable
         */
        public Integer getSearchable() {
            return searchable;
        }

        /**
         * @param searchable The searchable
         */
        public void setSearchable(Integer searchable) {
            this.searchable = searchable;
        }

        /**
         * @return The placeholder
         */
        public String getPlaceholder() {
            return placeholder;
        }

        /**
         * @param placeholder The placeholder
         */
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

    }

    @Parcel
    public static class Sort {

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;

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
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

    }

    /**
     * use this for listener
     */
    public static final class DynamicFilterContainer implements ObjContainer<DynamicFilterModel> {

        DynamicFilterModel dynamicFilterModel;

        public DynamicFilterContainer(DynamicFilterModel dynamicFilterModel) {
            this.dynamicFilterModel = dynamicFilterModel;
        }

        @Override
        public DynamicFilterModel body() {
            return dynamicFilterModel;
        }
    }

}
