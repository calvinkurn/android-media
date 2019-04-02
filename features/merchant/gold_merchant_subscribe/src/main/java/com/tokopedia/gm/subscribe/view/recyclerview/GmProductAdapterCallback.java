package com.tokopedia.gm.subscribe.view.recyclerview;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GmProductAdapterCallback {

    void selectedProductId(Integer gmProductViewModel);

    void selectedProductName(String gmProductViewModel);

    void selectedProductPrice(String gmProductViewModel);

    Integer getSelectedProductId();
}
