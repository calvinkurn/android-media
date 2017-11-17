package com.tokopedia.digital.base;

/**
 * @author anggaprasetiyo on 9/6/17.
 */

public abstract class BaseItemHolderItemData<T> {

    protected T itemData;
    protected int layoutId;

    public BaseItemHolderItemData() {
        itemData = getItemData();
        layoutId = getHolderLayoutId();
    }

    public abstract int getHolderLayoutId();

    public abstract T getItemData();


}
