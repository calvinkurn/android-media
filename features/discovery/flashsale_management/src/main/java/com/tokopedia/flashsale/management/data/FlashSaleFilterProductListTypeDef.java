package com.tokopedia.flashsale.management.data;

public enum FlashSaleFilterProductListTypeDef {
    TYPE_ALL(0, -1),
    TYPE_SUBMITTED(1, 0),
    TYPE_NON_SUBMITTED(3, 1);

    private int id;
    private int index;

    FlashSaleFilterProductListTypeDef(int id, int index) {
        this.id = id;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }
}
