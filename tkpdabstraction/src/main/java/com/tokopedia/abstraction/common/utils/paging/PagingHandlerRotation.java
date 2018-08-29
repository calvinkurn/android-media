package com.tokopedia.abstraction.common.utils.paging;
import android.os.Bundle;

/**
 * Created by m.normansyah on 27/10/2015.
 */
public interface PagingHandlerRotation {
    int defaultPagingIndex = -1;
    String PAGING_INDEX = "paging_index";
    String PAGING_HASNEXT = "paging_hasNext";
    /**
     * before leaving UI lifecycle
     * @param saved
     */
    void onSavedInstanceState(Bundle saved);

    /**
     * when first time create UI
     * @param retrieved
     * @return
     */
    int onCreate(Bundle retrieved);

    /**
     * when first time create UI on Fragment
     * @param retrieved
     * @return
     */
    int onCreateView(Bundle retrieved);
}