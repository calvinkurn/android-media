package com.tokopedia.core.myproduct.customview.wholesale;

/**
 * Created by sebastianuskh on 12/5/16.
 */
public class WholesaleModel {
    private int qtyOne = 0;
    private int qtyTwo = 0;
    private double qtyPrice = 0;
    private WholesaleViewHolder viewHolder;

    public WholesaleModel(){}

    public WholesaleModel(int quantityOne, int quantityTwo, double wholeSalePrice) {
        this.qtyOne = quantityOne;
        this.qtyTwo = quantityTwo;
        this.qtyPrice = wholeSalePrice;
    }

    public int getQtyOne() {
        return qtyOne;
    }

    public void setQtyOne(int qtyOne) {
        this.qtyOne = qtyOne;
    }

    public int getQtyTwo() {
        return qtyTwo;
    }

    public void setQtyTwo(int qtyTwo) {
        this.qtyTwo = qtyTwo;
    }

    public double getQtyPrice() {
        return qtyPrice;
    }

    public void setQtyPrice(double qtyPrice) {
        this.qtyPrice = qtyPrice;
    }

    @Override
    public String toString() {
        return " qty one : " + qtyOne + " qty two : " + qtyTwo + " qty price : " + qtyPrice;
    }

    public WholesaleViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(WholesaleViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}
