package com.tokopedia.digital.widget.view.model.operator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Attributes implements Parcelable {

    private String image;
    private int maximumLength;
    private int minimumLength;
    private String name;
    private List<String> prefix = new ArrayList<>();
    private String slug;
    private int status;
    private int weight;
    private Rule rule;
    private int defaultProductId;

    public Attributes() {
    }

    protected Attributes(Parcel in) {
        image = in.readString();
        maximumLength = in.readInt();
        minimumLength = in.readInt();
        name = in.readString();
        prefix = in.createStringArrayList();
        slug = in.readString();
        status = in.readInt();
        weight = in.readInt();
        rule = in.readParcelable(Rule.class.getClassLoader());
        defaultProductId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(maximumLength);
        dest.writeInt(minimumLength);
        dest.writeString(name);
        dest.writeStringList(prefix);
        dest.writeString(slug);
        dest.writeInt(status);
        dest.writeInt(weight);
        dest.writeParcelable(rule, flags);
        dest.writeInt(defaultProductId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public int getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public void setDefaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
    }
}