package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 05/11/18.
 */

public class FlightCancellationRequestAttachment {

    @SerializedName("docs_id")
    @Expose
    private long docsId;
    @SerializedName("docs_link")
    @Expose
    private List<String> docsLinks;

    public long getDocsId() {
        return docsId;
    }

    public void setDocsId(long docsId) {
        this.docsId = docsId;
    }

    public List<String> getDocsLinks() {
        return docsLinks;
    }

    public void setDocsLinks(List<String> docsLinks) {
        this.docsLinks = docsLinks;
    }
}
