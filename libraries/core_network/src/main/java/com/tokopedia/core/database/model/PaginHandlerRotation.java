package com.tokopedia.core.database.model;

import android.os.Bundle;

/**
 * Created by m.normansyah on 27/10/2015.
 */
@Deprecated
public interface PaginHandlerRotation {
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
