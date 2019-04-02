package com.tokopedia.gm.statistic.data.source.cloud.model.graph;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author normansyahputa on 7/6/17.
 */
public class GetTransactionGraph implements GetDateGraph {
    @SerializedName("p_cpc_product")
    @Expose
    private long pCpcProduct;

    @SerializedName("p_cpc_shop")
    @Expose
    private long pCpcShop;

    @SerializedName("onhold_count")
    @Expose
    private long onholdCount;

    @SerializedName("onhold_amt")
    @Expose
    private long onholdAmt;

    @SerializedName("reso_count")
    @Expose
    private long resoCount;

    @SerializedName("success_trans")
    @Expose
    private int successTrans;

    @SerializedName("rejected_trans")
    @Expose
    private int rejectedTrans;

    @SerializedName("finished_trans")
    @Expose
    private int finishedTrans;

    @SerializedName("new_order")
    @Expose
    private int newOrder;

    @SerializedName("delivered_product")
    @Expose
    private int deliveredProduct;

    @SerializedName("gross_revenue")
    @Expose
    private long grossRevenue;

    @SerializedName("net_revenue")
    @Expose
    private long netRevenue;

    @SerializedName("rejected_amount")
    @Expose
    private long rejectedAmount;

    @SerializedName("shipping_cost")
    @Expose
    private long shippingCost;

    @SerializedName("cpc_product")
    @Expose
    private int cpcProduct;

    @SerializedName("cpc_shop")
    @Expose
    private int cpcShop;

    @SerializedName("diff_success_trans")
    @Expose
    private double diffSuccessTrans;

    @SerializedName("diff_rejected_trans")
    @Expose
    private double diffRejectedTrans;

    @SerializedName("diff_finished_trans")
    @Expose
    private double diffFinishedTrans;

    @SerializedName("diff_new_order")
    @Expose
    private double diffNewOrder;

    @SerializedName("diff_delivered_product")
    @Expose
    private double diffDeliveredProduct;

    @SerializedName("diff_gross_revenue")
    @Expose
    private double diffGrossRevenue;

    @SerializedName("diff_net_revenue")
    @Expose
    private double diffNetRevenue;

    @SerializedName("diff_rejected_amount")
    @Expose
    private double diffRejectedAmount;

    @SerializedName("diff_shipping_cost")
    @Expose
    private double diffShippingCost;

    @SerializedName("diff_cpc_shop")
    @Expose
    private double diffCpcShop;

    @SerializedName("diff_cpc_prod")
    @Expose
    private double diffCpcProd;

    @SerializedName("date_graph")
    @Expose
    private List<Integer> dateGraph = null;

    @SerializedName("success_trans_graph")
    @Expose
    private List<Integer> successTransGraph = null;

    @SerializedName("rejected_trans_graph")
    @Expose
    private List<Integer> rejectedTransGraph = null;

    @SerializedName("gross_graph")
    @Expose
    private List<Integer> grossGraph = null;

    @SerializedName("net_graph")
    @Expose
    private List<Integer> netGraph = null;

    @SerializedName("rejected_amt_graph")
    @Expose
    private List<Integer> rejectedAmtGraph = null;

    @SerializedName("shipping_graph")
    @Expose
    private List<Integer> shippingGraph = null;

    @SerializedName("ads_p_graph")
    @Expose
    private List<Integer> adsPGraph = null;

    @SerializedName("ads_s_graph")
    @Expose
    private List<Integer> adsSGraph = null;

    @SerializedName("p_date_graph")
    @Expose
    private List<Integer> pDateGraph = null;

    @SerializedName("p_success_trans_graph")
    @Expose
    private List<Integer> pSuccessTransGraph = null;

    @SerializedName("p_rejected_trans_graph")
    @Expose
    private List<Integer> pRejectedTransGraph = null;

    @SerializedName("p_gross_graph")
    @Expose
    private List<Integer> pGrossGraph = null;

    @SerializedName("p_net_graph")
    @Expose
    private List<Integer> pNetGraph = null;

    @SerializedName("p_rejected_amt_graph")
    @Expose
    private List<Integer> pRejectedAmtGraph = null;

    @SerializedName("p_shipping_graph")
    @Expose
    private List<Integer> pShippingGraph = null;

    @SerializedName("p_ads_p_graph")
    @Expose
    private List<Integer> pAdsPGraph = null;

    @SerializedName("p_ads_s_graph")
    @Expose
    private List<Integer> pAdsSGraph = null;

    public int getSuccessTrans() {
        return successTrans;
    }

    public void setSuccessTrans(int successTrans) {
        this.successTrans = successTrans;
    }

    public int getRejectedTrans() {
        return rejectedTrans;
    }

    public void setRejectedTrans(int rejectedTrans) {
        this.rejectedTrans = rejectedTrans;
    }

    public int getFinishedTrans() {
        return finishedTrans;
    }

    public void setFinishedTrans(int finishedTrans) {
        this.finishedTrans = finishedTrans;
    }

    public int getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(int newOrder) {
        this.newOrder = newOrder;
    }

    public int getDeliveredProduct() {
        return deliveredProduct;
    }

    public void setDeliveredProduct(int deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    public long getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(long grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public long getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(long netRevenue) {
        this.netRevenue = netRevenue;
    }

    public long getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(long rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public long getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(long shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getCpcProduct() {
        return cpcProduct;
    }

    public void setCpcProduct(int cpcProduct) {
        this.cpcProduct = cpcProduct;
    }

    public int getCpcShop() {
        return cpcShop;
    }

    public void setCpcShop(int cpcShop) {
        this.cpcShop = cpcShop;
    }

    public double getDiffSuccessTrans() {
        return diffSuccessTrans;
    }

    public void setDiffSuccessTrans(double diffSuccessTrans) {
        this.diffSuccessTrans = diffSuccessTrans;
    }

    public double getDiffRejectedTrans() {
        return diffRejectedTrans;
    }

    public void setDiffRejectedTrans(double diffRejectedTrans) {
        this.diffRejectedTrans = diffRejectedTrans;
    }

    public double getDiffFinishedTrans() {
        return diffFinishedTrans;
    }

    public void setDiffFinishedTrans(double diffFinishedTrans) {
        this.diffFinishedTrans = diffFinishedTrans;
    }

    public double getDiffNewOrder() {
        return diffNewOrder;
    }

    public void setDiffNewOrder(double diffNewOrder) {
        this.diffNewOrder = diffNewOrder;
    }

    public double getDiffDeliveredProduct() {
        return diffDeliveredProduct;
    }

    public void setDiffDeliveredProduct(double diffDeliveredProduct) {
        this.diffDeliveredProduct = diffDeliveredProduct;
    }

    public double getDiffGrossRevenue() {
        return diffGrossRevenue;
    }

    public void setDiffGrossRevenue(double diffGrossRevenue) {
        this.diffGrossRevenue = diffGrossRevenue;
    }

    public double getDiffNetRevenue() {
        return diffNetRevenue;
    }

    public void setDiffNetRevenue(double diffNetRevenue) {
        this.diffNetRevenue = diffNetRevenue;
    }

    public double getDiffRejectedAmount() {
        return diffRejectedAmount;
    }

    public void setDiffRejectedAmount(double diffRejectedAmount) {
        this.diffRejectedAmount = diffRejectedAmount;
    }

    public double getDiffShippingCost() {
        return diffShippingCost;
    }

    public void setDiffShippingCost(double diffShippingCost) {
        this.diffShippingCost = diffShippingCost;
    }

    public double getDiffCpcShop() {
        return diffCpcShop;
    }

    public void setDiffCpcShop(double diffCpcShop) {
        this.diffCpcShop = diffCpcShop;
    }

    public double getDiffCpcProd() {
        return diffCpcProd;
    }

    public void setDiffCpcProd(double diffCpcProd) {
        this.diffCpcProd = diffCpcProd;
    }

    public List<Integer> getDateGraph() {
        return dateGraph;
    }

    public void setDateGraph(List<Integer> dateGraph) {
        this.dateGraph = dateGraph;
    }

    public List<Integer> getSuccessTransGraph() {
        return successTransGraph;
    }

    public void setSuccessTransGraph(List<Integer> successTransGraph) {
        this.successTransGraph = successTransGraph;
    }

    public List<Integer> getRejectedTransGraph() {
        return rejectedTransGraph;
    }

    public void setRejectedTransGraph(List<Integer> rejectedTransGraph) {
        this.rejectedTransGraph = rejectedTransGraph;
    }

    public List<Integer> getGrossGraph() {
        return grossGraph;
    }

    public void setGrossGraph(List<Integer> grossGraph) {
        this.grossGraph = grossGraph;
    }

    public List<Integer> getNetGraph() {
        return netGraph;
    }

    public void setNetGraph(List<Integer> netGraph) {
        this.netGraph = netGraph;
    }

    public List<Integer> getRejectedAmtGraph() {
        return rejectedAmtGraph;
    }

    public void setRejectedAmtGraph(List<Integer> rejectedAmtGraph) {
        this.rejectedAmtGraph = rejectedAmtGraph;
    }

    public List<Integer> getShippingGraph() {
        return shippingGraph;
    }

    public void setShippingGraph(List<Integer> shippingGraph) {
        this.shippingGraph = shippingGraph;
    }

    public List<Integer> getAdsPGraph() {
        return adsPGraph;
    }

    public void setAdsPGraph(List<Integer> adsPGraph) {
        this.adsPGraph = adsPGraph;
    }

    public List<Integer> getAdsSGraph() {
        return adsSGraph;
    }

    public void setAdsSGraph(List<Integer> adsSGraph) {
        this.adsSGraph = adsSGraph;
    }

    public List<Integer> getPDateGraph() {
        return pDateGraph;
    }

    public void setPDateGraph(List<Integer> pDateGraph) {
        this.pDateGraph = pDateGraph;
    }

    public List<Integer> getPSuccessTransGraph() {
        return pSuccessTransGraph;
    }

    public void setPSuccessTransGraph(List<Integer> pSuccessTransGraph) {
        this.pSuccessTransGraph = pSuccessTransGraph;
    }

    public List<Integer> getPRejectedTransGraph() {
        return pRejectedTransGraph;
    }

    public void setPRejectedTransGraph(List<Integer> pRejectedTransGraph) {
        this.pRejectedTransGraph = pRejectedTransGraph;
    }

    public List<Integer> getPGrossGraph() {
        return pGrossGraph;
    }

    public void setPGrossGraph(List<Integer> pGrossGraph) {
        this.pGrossGraph = pGrossGraph;
    }

    public List<Integer> getPNetGraph() {
        return pNetGraph;
    }

    public void setPNetGraph(List<Integer> pNetGraph) {
        this.pNetGraph = pNetGraph;
    }

    public List<Integer> getPRejectedAmtGraph() {
        return pRejectedAmtGraph;
    }

    public void setPRejectedAmtGraph(List<Integer> pRejectedAmtGraph) {
        this.pRejectedAmtGraph = pRejectedAmtGraph;
    }

    public List<Integer> getPShippingGraph() {
        return pShippingGraph;
    }

    public void setPShippingGraph(List<Integer> pShippingGraph) {
        this.pShippingGraph = pShippingGraph;
    }

    public List<Integer> getPAdsPGraph() {
        return pAdsPGraph;
    }

    public void setPAdsPGraph(List<Integer> pAdsPGraph) {
        this.pAdsPGraph = pAdsPGraph;
    }

    public List<Integer> getPAdsSGraph() {
        return pAdsSGraph;
    }

    public void setPAdsSGraph(List<Integer> pAdsSGraph) {
        this.pAdsSGraph = pAdsSGraph;
    }

    public long getpCpcProduct() {
        return pCpcProduct;
    }

    public void setpCpcProduct(long pCpcProduct) {
        this.pCpcProduct = pCpcProduct;
    }

    public long getpCpcShop() {
        return pCpcShop;
    }

    public void setpCpcShop(long pCpcShop) {
        this.pCpcShop = pCpcShop;
    }

    public long getOnholdCount() {
        return onholdCount;
    }

    public void setOnholdCount(long onholdCount) {
        this.onholdCount = onholdCount;
    }

    public long getOnholdAmt() {
        return onholdAmt;
    }

    public void setOnholdAmt(long onholdAmt) {
        this.onholdAmt = onholdAmt;
    }

    public long getResoCount() {
        return resoCount;
    }

    public void setResoCount(long resoCount) {
        this.resoCount = resoCount;
    }
}
