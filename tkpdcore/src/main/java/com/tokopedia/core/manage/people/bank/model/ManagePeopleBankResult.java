
package com.tokopedia.core.manage.people.bank.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;

public class ManagePeopleBankResult {

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private java.util.List<BankAccount> list = new ArrayList<BankAccount>();

    /**
     * 
     * @return
     *     The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<BankAccount> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<BankAccount> list) {
        this.list = list;
    }

}
