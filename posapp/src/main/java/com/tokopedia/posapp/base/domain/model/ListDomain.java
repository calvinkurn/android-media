package com.tokopedia.posapp.base.domain.model;

import java.util.List;

/**
 * Created by okasurya on 9/19/17.
 */

@Deprecated
public class ListDomain<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
