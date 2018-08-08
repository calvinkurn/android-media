package com.tokopedia.topads.dashboard.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 2/13/17.
 */

public abstract class StateTypeBasedModel implements ItemType {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
