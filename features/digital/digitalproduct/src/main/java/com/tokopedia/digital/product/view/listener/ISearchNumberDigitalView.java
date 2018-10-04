package com.tokopedia.digital.product.view.listener;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public interface ISearchNumberDigitalView {

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams);

    void renderNumberList(List<OrderClientNumber> orderClientNumbers);

}
