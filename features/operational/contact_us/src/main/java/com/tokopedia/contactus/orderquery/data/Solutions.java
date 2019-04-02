package com.tokopedia.contactus.orderquery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeepgoyal on 13/04/18.
 */

public class Solutions {

    @SerializedName("solutions")
    List<QueryTicket> solutions;

    public List<QueryTicket> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<QueryTicket> solutions) {
        this.solutions = solutions;
    }
}
