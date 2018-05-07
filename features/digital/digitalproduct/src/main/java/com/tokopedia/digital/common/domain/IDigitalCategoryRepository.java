package com.tokopedia.digital.common.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public interface IDigitalCategoryRepository {

    Observable<CategoryData> getCategory(String categoryId, TKPDMapParam<String, String> param);

    Observable<DigitalNumberList> getFavoriteList(TKPDMapParam<String, String> param);

    Observable<String> getHelpUrl(String categoryId);
}
