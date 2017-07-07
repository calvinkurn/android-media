package com.tokopedia.discovery.categorynav.domain.model;


import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class Category {

    private List<ChildCategory> children = null;
    private String id;
    private String name;
    private String iconImageUrl;
    private Boolean hasChild;

}
