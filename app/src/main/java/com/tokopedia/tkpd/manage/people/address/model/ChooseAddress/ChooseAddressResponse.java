package com.tokopedia.tkpd.manage.people.address.model.ChooseAddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.addtocart.model.responseatcform.Destination;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.ArrayList;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressResponse {

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private java.util.List<Destination> list = new ArrayList<Destination>();

    /**
     *
     * @return
     * The list
     */
    public java.util.List<Destination> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<Destination> list) {
        this.list = list;
    }

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

}
