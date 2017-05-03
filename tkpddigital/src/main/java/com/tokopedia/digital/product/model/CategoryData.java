package com.tokopedia.digital.product.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class CategoryData {

    public static final String STYLE_PRODUCT_CATEGORY_1 = "style_1";
    public static final String STYLE_PRODUCT_CATEGORY_2 = "style_2";
    public static final String STYLE_PRODUCT_CATEGORY_3 = "style_3";
    public static final String STYLE_PRODUCT_CATEGORY_4 = "style_4";

    public static final String[] STYLE_COLLECTION_SUPPORTED = new String[]{
            STYLE_PRODUCT_CATEGORY_1, STYLE_PRODUCT_CATEGORY_2, STYLE_PRODUCT_CATEGORY_2,
            STYLE_PRODUCT_CATEGORY_3, STYLE_PRODUCT_CATEGORY_4
    };

    private String categoryId;
    private String categoryType;

    private String name;
    private String icon;
    private String iconUrl;
    private Teaser teaser;
    private boolean isNew;
    private boolean instantCheckout;
    private String slug;
    private String defaultOperatorId;
    private String operatorStyle;
    private List<Field> fieldList = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Teaser getTeaser() {
        return teaser;
    }

    public void setTeaser(Teaser teaser) {
        this.teaser = teaser;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public void setOperatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public List<Operator> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<Operator> operatorList) {
        this.operatorList = operatorList;
    }

    public boolean isSupportedStyle() {
        return (Arrays.asList(STYLE_COLLECTION_SUPPORTED).contains(operatorStyle));
    }
}
