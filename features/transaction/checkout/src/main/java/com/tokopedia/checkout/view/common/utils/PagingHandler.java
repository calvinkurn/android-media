package com.tokopedia.checkout.view.common.utils;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;

/**
 * @author anggaprasetiyo on 07/05/18.
 */
public class PagingHandler extends com.tokopedia.abstraction.common.utils.paging.PagingHandler {
    public void setNewParameter(PagingHandlerModel pagingHanderModel) {
        hasNext = pagingHanderModel != null && CommonUtils.checkStringNotNull(pagingHanderModel.uriNext);
    }
}
