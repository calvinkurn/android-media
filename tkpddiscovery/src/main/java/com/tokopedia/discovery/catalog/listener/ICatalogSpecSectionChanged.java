package com.tokopedia.discovery.catalog.listener;

import com.tokopedia.discovery.catalog.adapter.CatalogSpecAdapterHelper;

public interface ICatalogSpecSectionChanged {
    /**
     * action perubahan expand collapse item di recyclerview
     *
     * @param section object section
     * @param isOpen  dia expand atau collapse
     */
    void onSectionStateChanged(CatalogSpecAdapterHelper.Section section, boolean isOpen);
}