package com.tokopedia.core.myproduct.model;

import com.tkpd.library.ui.widget.Listable;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 12/9/15.
 */
@Parcel(parcelsIndex = false)
public class SimpleTextModel implements Listable{
    String text;
    int position;
    int level;
    String query;

    public SimpleTextModel(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SimpleTextModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTextModel that = (SimpleTextModel) o;

        return !(text != null ? !text.equals(that.text) : that.text != null);

    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SimpleTextModel{" +
                "text='" + text + '\'' +
                ", position=" + position +
                ", level=" + level +
                '}';
    }

    @Override
    public String getLabel() {
        return text;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
