package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DynamicFilterModel implements Parcelable {

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


    public static class Data implements Parcelable, Serializable {


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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.selected);
            dest.writeString(this.selectedOb);
            dest.writeList(this.filter);
            dest.writeList(this.sort);
        }

        public Data() {
        }

        protected Data(android.os.Parcel in) {
            this.selected = in.readString();
            this.selectedOb = in.readString();
            this.filter = new ArrayList<Filter>();
            in.readList(this.filter, Filter.class.getClassLoader());
            this.sort = new ArrayList<Sort>();
            in.readList(this.sort, Sort.class.getClassLoader());
        }

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(android.os.Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }

    public static class Filter implements Parcelable, Serializable {
        public static final String TITLE_CATEGORY = "Kategori";


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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeParcelable(this.search, flags);
            dest.writeList(this.options);
        }

        public Filter() {
        }

        protected Filter(android.os.Parcel in) {
            this.title = in.readString();
            this.search = in.readParcelable(Search.class.getClassLoader());
            this.options = new ArrayList<Option>();
            in.readList(this.options, Option.class.getClassLoader());
        }

        public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
            @Override
            public Filter createFromParcel(android.os.Parcel source) {
                return new Filter(source);
            }

            @Override
            public Filter[] newArray(int size) {
                return new Filter[size];
            }
        };
    }


    public static class Option implements Parcelable, Serializable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
        }

        public Option() {
        }

        protected Option(android.os.Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
        }

        public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
            @Override
            public Option createFromParcel(android.os.Parcel source) {
                return new Option(source);
            }

            @Override
            public Option[] newArray(int size) {
                return new Option[size];
            }
        };
    }


    public static class Search implements Parcelable, Serializable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeValue(this.searchable);
            dest.writeString(this.placeholder);
        }

        public Search() {
        }

        protected Search(android.os.Parcel in) {
            this.searchable = (Integer) in.readValue(Integer.class.getClassLoader());
            this.placeholder = in.readString();
        }

        public static final Parcelable.Creator<Search> CREATOR = new Parcelable.Creator<Search>() {
            @Override
            public Search createFromParcel(android.os.Parcel source) {
                return new Search(source);
            }

            @Override
            public Search[] newArray(int size) {
                return new Search[size];
            }
        };
    }


    public static class Sort implements Parcelable, Serializable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
        }

        public Sort() {
        }

        protected Sort(Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
        }

        public static final Parcelable.Creator<Sort> CREATOR = new Parcelable.Creator<Sort>() {
            @Override
            public Sort createFromParcel(Parcel source) {
                return new Sort(source);
            }

            @Override
            public Sort[] newArray(int size) {
                return new Sort[size];
            }
        };
    }

    /**
     * use this for listener
     */
    public static final class DynamicFilterContainer implements ObjContainer<DynamicFilterModel>, Parcelable {

        DynamicFilterModel dynamicFilterModel;

        public DynamicFilterContainer(DynamicFilterModel dynamicFilterModel) {
            this.dynamicFilterModel = dynamicFilterModel;
        }

        @Override
        public DynamicFilterModel body() {
            return dynamicFilterModel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.dynamicFilterModel, flags);
        }

        protected DynamicFilterContainer(Parcel in) {
            this.dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        }

        public static final Parcelable.Creator<DynamicFilterContainer> CREATOR = new Parcelable.Creator<DynamicFilterContainer>() {
            @Override
            public DynamicFilterContainer createFromParcel(Parcel source) {
                return new DynamicFilterContainer(source);
            }

            @Override
            public DynamicFilterContainer[] newArray(int size) {
                return new DynamicFilterContainer[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.processTime);
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.status);
    }

    public DynamicFilterModel() {
    }

    protected DynamicFilterModel(Parcel in) {
        this.processTime = in.readString();
        this.data = in.readParcelable(Data.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Parcelable.Creator<DynamicFilterModel> CREATOR = new Parcelable.Creator<DynamicFilterModel>() {
        @Override
        public DynamicFilterModel createFromParcel(Parcel source) {
            return new DynamicFilterModel(source);
        }

        @Override
        public DynamicFilterModel[] newArray(int size) {
            return new DynamicFilterModel[size];
        }
    };
}
