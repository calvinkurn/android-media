package com.tokopedia.digital.widget.data.entity.response;

import java.util.List;

/**
 * @author rizkyfadillah on 10/11/2017.
 */

public class ResponseFavoriteList {
    private ResponseMetaFavoriteNumber responseMeta;
    private List<ResponseFavoriteNumber> responseNumberList;

    public ResponseFavoriteList(ResponseMetaFavoriteNumber responseMeta, List<ResponseFavoriteNumber> responseNumberList) {
        this.responseMeta = responseMeta;
        this.responseNumberList = responseNumberList;
    }

    public ResponseMetaFavoriteNumber getResponseMeta() {
        return responseMeta;
    }

    public List<ResponseFavoriteNumber> getResponseNumberList() {
        return responseNumberList;
    }
}
