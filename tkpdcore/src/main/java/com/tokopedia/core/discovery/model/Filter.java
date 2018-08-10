package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */
public class Filter implements Parcelable {

    public static final String TEMPLATE_NAME_LOCATION = "template_location";
    public static final String TEMPLATE_NAME_OTHER = "template_other";
    
    private static final String TEMPLATE_NAME_SEPARATOR = "template_separator";
    private static final String TEMPLATE_NAME_RATING = "template_rating";
    private static final String TEMPLATE_NAME_SIZE = "template_size";
    private static final String TEMPLATE_NAME_CATEGORY = "template_category";
    private static final String TEMPLATE_NAME_COLOR = "template_color";
    private static final String TEMPLATE_NAME_PRICE = "template_price";
    private static final String TEMPLATE_NAME_BRAND = "template_brand";

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("template_name")
    @Expose
    String templateName;
    @SerializedName("search")
    @Expose
    Search search;
    @SerializedName("options")
    @Expose
    List<Option> options = new ArrayList<>();

    public Filter() {
    }

    public boolean isSeparator() {
        return TEMPLATE_NAME_SEPARATOR.equals(templateName);
    }

    public boolean isCategoryFilter() {
        return TEMPLATE_NAME_CATEGORY.equals(templateName);
    }

    public boolean isColorFilter() {
        return TEMPLATE_NAME_COLOR.equals(templateName);
    }

    public boolean isPriceFilter() {
        return TEMPLATE_NAME_PRICE.equals(templateName);
    }

    public boolean isRatingFilter() {
        return TEMPLATE_NAME_RATING.equals(templateName);
    }

    public boolean isSizeFilter() {
        return TEMPLATE_NAME_SIZE.equals(templateName);
    }

    public boolean isLocationFilter() {
        return TEMPLATE_NAME_LOCATION.equals(templateName);
    }

    public boolean isBrandFilter() {
        return TEMPLATE_NAME_BRAND.equals(templateName);
    }

    public boolean isOtherFilter() {
        return TEMPLATE_NAME_OTHER.equals(templateName);
    }

    public boolean isExpandableFilter() {
        return isCategoryFilter() || isColorFilter() || isRatingFilter()
                || isSizeFilter() || isBrandFilter() || isLocationFilter()
                || isOtherFilter() || options.size() > 1;
    }

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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
        if (options == null) {
            options = new ArrayList<>();
        }
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.templateName);
        dest.writeParcelable(this.search, flags);
        dest.writeTypedList(this.options);
    }

    protected Filter(Parcel in) {
        this.title = in.readString();
        this.templateName = in.readString();
        this.search = in.readParcelable(Search.class.getClassLoader());
        this.options = in.createTypedArrayList(Option.CREATOR);
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
