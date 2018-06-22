package com.tokopedia.gm.subscribe.view.fragment;

/**
 * Created by sebastianuskh on 1/23/17.
 */

public interface GmProductFragmentListener {

    void changeActionBarTitle(String title);

    void setDrawer(boolean isShown);

    void finishProductSelection(int productId, int returnType);
}
