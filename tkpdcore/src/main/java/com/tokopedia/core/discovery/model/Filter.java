package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */
public class Filter implements Serializable, Parcelable {
    
    private static final String TITLE_SEPARATOR = "Separator";
    private static final String TITLE_RATING = "Rating";
    private static final String TITLE_SIZE = "Ukuran";
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
        return TITLE_SEPARATOR.equals(title);
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
        return TITLE_RATING.equals(title);
    }

    public boolean isSizeFilter() {
        return TITLE_SIZE.equals(title);
    }

    public boolean isBrandFilter() {
        return TEMPLATE_NAME_BRAND.equals(templateName);
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

    protected Filter(Parcel in) {
        title = in.readString();
        templateName = in.readString();
        search = (Search) in.readValue(Search.class.getClassLoader());
        if (in.readByte() == 0x01) {
            options = new ArrayList<Option>();
            in.readList(options, Option.class.getClassLoader());
        } else {
            options = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(templateName);
        dest.writeValue(search);
        if (options == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(options);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
