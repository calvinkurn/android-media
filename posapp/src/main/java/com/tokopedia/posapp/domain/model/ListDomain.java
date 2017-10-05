package com.tokopedia.posapp.domain.model;

import java.util.List;

/**
 * Created by okasurya on 9/19/17.
 */

public class ListDomain<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
