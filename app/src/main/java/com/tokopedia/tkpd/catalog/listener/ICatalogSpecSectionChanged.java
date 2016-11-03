package com.tokopedia.tkpd.catalog.listener;

import com.tokopedia.tkpd.catalog.adapter.CatalogSpecAdapterHelper;

public interface ICatalogSpecSectionChanged {
    /**
     * action perubahan expand collapse item di recyclerview
     *
     * @param section object section
     * @param isOpen  dia expand atau collapse
     */
    void onSectionStateChanged(CatalogSpecAdapterHelper.Section section, boolean isOpen);
}