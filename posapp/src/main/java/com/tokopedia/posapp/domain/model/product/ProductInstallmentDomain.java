package com.tokopedia.posapp.domain.model.product;



/**
 * Created by okasurya on 8/9/17.
 */

public class ProductInstallmentDomain {
    private String icon;
    private TermsDomain terms;
    private String name;
    private Integer id;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public TermsDomain getTerms() {
        return terms;
    }

    public void setTerms(TermsDomain terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
