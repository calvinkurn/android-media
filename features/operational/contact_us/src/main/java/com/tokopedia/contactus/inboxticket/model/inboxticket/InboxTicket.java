
package com.tokopedia.contactus.inboxticket.model.inboxticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

public class InboxTicket {

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private List<InboxTicketItem> list = new ArrayList<>();

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
    public List<InboxTicketItem> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(List<InboxTicketItem> list) {
        this.list = list;
    }

}
