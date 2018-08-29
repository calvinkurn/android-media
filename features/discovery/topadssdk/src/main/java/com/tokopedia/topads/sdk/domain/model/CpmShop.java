package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmShop {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_TAGLINE = "tagline";
    private static final String KEY_SLOGAN = "slogan";
    private static final String KEY_PRODUCT = "product";

    private int id;
    private String name;
    private String domain;
    private String tagline;
    private String slogan;
    private List<Product> products = new ArrayList<>();

    public CpmShop(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getInt(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)){
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_DOMAIN)){
            setDomain(object.getString(KEY_DOMAIN));
        }
        if(!object.isNull(KEY_TAGLINE)){
            setTagline(object.getString(KEY_TAGLINE));
        }
        if(!object.isNull(KEY_SLOGAN)){
            setSlogan(object.getString(KEY_SLOGAN));
        }
        if(!object.isNull(KEY_PRODUCT)){
            JSONArray productArray = object.getJSONArray(KEY_PRODUCT);
            for (int i = 0; i < productArray.length(); i++) {
                products.add(new Product(productArray.getJSONObject(i)));
            }
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
