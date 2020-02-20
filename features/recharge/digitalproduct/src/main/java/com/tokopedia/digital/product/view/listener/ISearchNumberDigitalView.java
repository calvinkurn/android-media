package com.tokopedia.digital.product.view.listener;

import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;
import java.util.Map;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public interface ISearchNumberDigitalView {

    Map<String, String> getGeneratedAuthParamNetwork(
            Map<String, String> originParams);

    void renderNumberList(List<OrderClientNumber> orderClientNumbers);

}
