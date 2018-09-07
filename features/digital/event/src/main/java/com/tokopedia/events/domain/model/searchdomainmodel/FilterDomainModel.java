package com.tokopedia.events.domain.model.searchdomainmodel;


import java.util.List;

/**
 * Created by pranaymohapatra on 15/01/18.
 */

public class FilterDomainModel {
    private String name;
    private String attributeName;
    private String label;
    private String kind;
    private List<ValuesItemDomain> valuesItems;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<ValuesItemDomain> getValuesItems() {
        return valuesItems;
    }

    public void setValuesItems(List<ValuesItemDomain> valuesItems) {
        this.valuesItems = valuesItems;
    }


}
