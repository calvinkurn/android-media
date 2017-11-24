package com.tokopedia.core.base.adapter.model;


import com.tokopedia.core.base.adapter.AdapterTypeFactory;
import com.tokopedia.core.base.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class EmptyModel implements Visitable<AdapterTypeFactory> {

    private String message = "";

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
