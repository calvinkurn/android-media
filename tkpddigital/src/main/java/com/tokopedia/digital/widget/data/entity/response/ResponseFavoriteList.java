package com.tokopedia.digital.widget.data.entity.response;

import java.util.List;

/**
 * @author rizkyfadillah on 10/11/2017.
 */

public class ResponseFavoriteList {
    private ResponseMeta responseMeta;
    private List<ResponseFavoriteNumber> responseNumberList;

    public ResponseFavoriteList(ResponseMeta responseMeta, List<ResponseFavoriteNumber> responseFavoriteList) {
        this.responseMeta = responseMeta;
        this.responseNumberList = responseNumberList;
    }

    public ResponseMeta getResponseMeta() {
        return responseMeta;
    }

    public List<ResponseFavoriteNumber> getResponseNumberList() {
        return responseNumberList;
    }
}
