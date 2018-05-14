package com.tokopedia.checkout.view.view.multipleaddressform;

import com.tokopedia.abstraction.common.utils.TKPDMapParam; /**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressView {

    void successMakeShipmentData();

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> param);
}
