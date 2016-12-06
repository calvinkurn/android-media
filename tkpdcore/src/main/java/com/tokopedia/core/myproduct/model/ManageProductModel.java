package com.tokopedia.core.myproduct.model;

/**
 * Created by Toped18 on 5/31/2016.
 */
public class ManageProductModel {
    String ProdName;
    String ProdID;
    String ProdImgUri;
    String menuName;
    String menuID;
    String DepName;
    String EtalaseLoc;
    String CheckedItem;
    Boolean Checked;
    String PStatus;
    String EtalaseFilters;
    String EtalaseIdFilters;
    String CategoriesFilter;
    String CategoriesIdFilter;
    String Price;
    String CurrencyCode;
    Boolean ContainWholesale;
    Integer returnablePolicy ;
    Integer currencyCodeInt;

    public Integer getCurrencyCodeInt() {
        return currencyCodeInt;
    }

    public void setCurrencyCodeInt(Integer currencyCodeInt) {
        this.currencyCodeInt = currencyCodeInt;
    }

    public String getCategoriesFilter() {
        return CategoriesFilter;
    }

    public void setCategoriesFilter(String categoriesFilter) {
        CategoriesFilter = categoriesFilter;
    }

    public String getCategoriesIdFilter() {
        return CategoriesIdFilter;
    }

    public void setCategoriesIdFilter(String categoriesIdFilter) {
        CategoriesIdFilter = categoriesIdFilter;
    }

    public Boolean getChecked() {
        return Checked;
    }

    public void setChecked(Boolean checked) {
        Checked = checked;
    }

    public String getCheckedItem() {
        return CheckedItem;
    }

    public void setCheckedItem(String checkedItem) {
        CheckedItem = checkedItem;
    }

    public Boolean getContainWholesale() {
        return ContainWholesale;
    }

    public void setContainWholesale(Boolean containWholesale) {
        ContainWholesale = containWholesale;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getDepName() {
        return DepName;
    }

    public void setDepName(String depName) {
        DepName = depName;
    }

    public String getEtalaseFilters() {
        return EtalaseFilters;
    }

    public void setEtalaseFilters(String etalaseFilters) {
        EtalaseFilters = etalaseFilters;
    }

    public String getEtalaseIdFilters() {
        return EtalaseIdFilters;
    }

    public void setEtalaseIdFilters(String etalaseIdFilters) {
        EtalaseIdFilters = etalaseIdFilters;
    }

    public String getEtalaseLoc() {
        return EtalaseLoc;
    }

    public void setEtalaseLoc(String etalaseLoc) {
        EtalaseLoc = etalaseLoc;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getProdImgUri() {
        return ProdImgUri;
    }

    public void setProdImgUri(String prodImgUri) {
        ProdImgUri = prodImgUri;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getPStatus() {
        return PStatus;
    }

    public void setPStatus(String PStatus) {
        this.PStatus = PStatus;
    }

    public Integer getReturnablePolicy() {
        return returnablePolicy;
    }

    public void setReturnablePolicy(Integer returnablePolicy) {
        this.returnablePolicy = returnablePolicy;
    }
}
