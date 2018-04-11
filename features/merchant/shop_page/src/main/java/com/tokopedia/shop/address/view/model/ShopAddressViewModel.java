package com.tokopedia.shop.address.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.address.view.adapter.ShopAddressAdapterTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopAddressViewModel implements Visitable<ShopAddressAdapterTypeFactory> {

    private String id;
    private String name;
    private String content;
    private String area;
    private String email;
    private String phone;
    private String fax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public int type(ShopAddressAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
