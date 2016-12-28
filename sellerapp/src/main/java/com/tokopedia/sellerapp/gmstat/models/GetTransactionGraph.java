package com.tokopedia.sellerapp.gmstat.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionGraph {

    @SerializedName("SuccessTrans")
    @Expose
    private Long successTrans;
    @SerializedName("NewOrder")
    @Expose
    private Integer newOrder;
    @SerializedName("DeliveredProduct")
    @Expose
    private Integer deliveredProduct;
    @SerializedName("GrossRevenue")
    @Expose
    private Long grossRevenue;
    @SerializedName("NetRevenue")
    @Expose
    private Integer netRevenue;
    @SerializedName("RejectedAmount")
    @Expose
    private Integer rejectedAmount;
    @SerializedName("ShippingCost")
    @Expose
    private Integer shippingCost;
    @SerializedName("CpcProduct")
    @Expose
    private Integer cpcProduct;
    @SerializedName("CpcShop")
    @Expose
    private Integer cpcShop;
    @SerializedName("DiffSuccessTrans")
    @Expose
    private Double diffSuccessTrans;
    @SerializedName("DiffNewOrder")
    @Expose
    private Double diffNewOrder;
    @SerializedName("DiffDeliveredProduct")
    @Expose
    private Double diffDeliveredProduct;
    @SerializedName("DiffGrossRevenue")
    @Expose
    private Double diffGrossRevenue;
    @SerializedName("DiffNetRevenue")
    @Expose
    private Double diffNetRevenue;
    @SerializedName("DiffRejectedAmount")
    @Expose
    private Double diffRejectedAmount;
    @SerializedName("DiffShippingCost")
    @Expose
    private Double diffShippingCost;
    @SerializedName("DiffCpcShop")
    @Expose
    private Double diffCpcShop;
    @SerializedName("DiffCpcProd")
    @Expose
    private Double diffCpcProd;
    @SerializedName("DateGraph")
    @Expose
    private List<Integer> dateGraph = null;
    @SerializedName("SuccessTransGraph")
    @Expose
    private List<Integer> successTransGraph = null;
    @SerializedName("RejectedSumGraph")
    @Expose
    private List<Integer> rejectedSumGraph = null;
    @SerializedName("GrossGraph")
    @Expose
    private List<Integer> grossGraph = null;
    @SerializedName("NetGraph")
    @Expose
    private List<Integer> netGraph = null;
    @SerializedName("RejectedAmtGraph")
    @Expose
    private List<Integer> rejectedAmtGraph = null;
    @SerializedName("ShippingGraph")
    @Expose
    private List<Integer> shippingGraph = null;
    @SerializedName("AdsPGraph")
    @Expose
    private List<Integer> adsPGraph = null;
    @SerializedName("AdsSGraph")
    @Expose
    private List<Integer> adsSGraph = null;
    @SerializedName("PDateGraph")
    @Expose
    private List<Integer> pDateGraph = null;
    @SerializedName("PSuccessTransGraph")
    @Expose
    private List<Integer> pSuccessTransGraph = null;
    @SerializedName("PRejectedSumGraph")
    @Expose
    private List<Integer> pRejectedSumGraph = null;
    @SerializedName("PGrossGraph")
    @Expose
    private List<Integer> pGrossGraph = null;
    @SerializedName("PNetGraph")
    @Expose
    private List<Integer> pNetGraph = null;
    @SerializedName("PRejectedAmtGraph")
    @Expose
    private List<Integer> pRejectedAmtGraph = null;
    @SerializedName("PShippingGraph")
    @Expose
    private List<Integer> pShippingGraph = null;
    @SerializedName("PAdsPGraph")
    @Expose
    private List<Integer> pAdsPGraph = null;
    @SerializedName("PAdsSGraph")
    @Expose
    private List<Integer> pAdsSGraph = null;

    public Long getSuccessTrans() {
        return successTrans;
    }

    public void setSuccessTrans(Long successTrans) {
        this.successTrans = successTrans;
    }

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    public Integer getDeliveredProduct() {
        return deliveredProduct;
    }

    public void setDeliveredProduct(Integer deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    public Long getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(Long grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public Integer getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(Integer netRevenue) {
        this.netRevenue = netRevenue;
    }

    public Integer getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(Integer rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public Integer getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Integer shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Integer getCpcProduct() {
        return cpcProduct;
    }

    public void setCpcProduct(Integer cpcProduct) {
        this.cpcProduct = cpcProduct;
    }

    public Integer getCpcShop() {
        return cpcShop;
    }

    public void setCpcShop(Integer cpcShop) {
        this.cpcShop = cpcShop;
    }

    public Double getDiffSuccessTrans() {
        return diffSuccessTrans;
    }

    public void setDiffSuccessTrans(Double diffSuccessTrans) {
        this.diffSuccessTrans = diffSuccessTrans;
    }

    public Double getDiffNewOrder() {
        return diffNewOrder;
    }

    public void setDiffNewOrder(Double diffNewOrder) {
        this.diffNewOrder = diffNewOrder;
    }

    public Double getDiffDeliveredProduct() {
        return diffDeliveredProduct;
    }

    public void setDiffDeliveredProduct(Double diffDeliveredProduct) {
        this.diffDeliveredProduct = diffDeliveredProduct;
    }

    public Double getDiffGrossRevenue() {
        return diffGrossRevenue;
    }

    public void setDiffGrossRevenue(Double diffGrossRevenue) {
        this.diffGrossRevenue = diffGrossRevenue;
    }

    public Double getDiffNetRevenue() {
        return diffNetRevenue;
    }

    public void setDiffNetRevenue(Double diffNetRevenue) {
        this.diffNetRevenue = diffNetRevenue;
    }

    public Double getDiffRejectedAmount() {
        return diffRejectedAmount;
    }

    public void setDiffRejectedAmount(Double diffRejectedAmount) {
        this.diffRejectedAmount = diffRejectedAmount;
    }

    public Double getDiffShippingCost() {
        return diffShippingCost;
    }

    public void setDiffShippingCost(Double diffShippingCost) {
        this.diffShippingCost = diffShippingCost;
    }

    public Double getDiffCpcShop() {
        return diffCpcShop;
    }

    public void setDiffCpcShop(Double diffCpcShop) {
        this.diffCpcShop = diffCpcShop;
    }

    public Double getDiffCpcProd() {
        return diffCpcProd;
    }

    public void setDiffCpcProd(Double diffCpcProd) {
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

    public List<Integer> getRejectedSumGraph() {
        return rejectedSumGraph;
    }

    public void setRejectedSumGraph(List<Integer> rejectedSumGraph) {
        this.rejectedSumGraph = rejectedSumGraph;
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

    public List<Integer> getPRejectedSumGraph() {
        return pRejectedSumGraph;
    }

    public void setPRejectedSumGraph(List<Integer> pRejectedSumGraph) {
        this.pRejectedSumGraph = pRejectedSumGraph;
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

    @Override
    public String toString() {
        return "GetTransactionGraph{" +
                "successTrans=" + successTrans +
                ", newOrder=" + newOrder +
                ", deliveredProduct=" + deliveredProduct +
                ", grossRevenue=" + grossRevenue +
                ", netRevenue=" + netRevenue +
                ", rejectedAmount=" + rejectedAmount +
                ", shippingCost=" + shippingCost +
                ", cpcProduct=" + cpcProduct +
                ", cpcShop=" + cpcShop +
                ", diffSuccessTrans=" + diffSuccessTrans +
                ", diffNewOrder=" + diffNewOrder +
                ", diffDeliveredProduct=" + diffDeliveredProduct +
                ", diffGrossRevenue=" + diffGrossRevenue +
                ", diffNetRevenue=" + diffNetRevenue +
                ", diffRejectedAmount=" + diffRejectedAmount +
                ", diffShippingCost=" + diffShippingCost +
                ", diffCpcShop=" + diffCpcShop +
                ", diffCpcProd=" + diffCpcProd +
                ", dateGraph=" + dateGraph +
                ", successTransGraph=" + successTransGraph +
                ", rejectedSumGraph=" + rejectedSumGraph +
                ", grossGraph=" + grossGraph +
                ", netGraph=" + netGraph +
                ", rejectedAmtGraph=" + rejectedAmtGraph +
                ", shippingGraph=" + shippingGraph +
                ", adsPGraph=" + adsPGraph +
                ", adsSGraph=" + adsSGraph +
                ", pDateGraph=" + pDateGraph +
                ", pSuccessTransGraph=" + pSuccessTransGraph +
                ", pRejectedSumGraph=" + pRejectedSumGraph +
                ", pGrossGraph=" + pGrossGraph +
                ", pNetGraph=" + pNetGraph +
                ", pRejectedAmtGraph=" + pRejectedAmtGraph +
                ", pShippingGraph=" + pShippingGraph +
                ", pAdsPGraph=" + pAdsPGraph +
                ", pAdsSGraph=" + pAdsSGraph +
                '}';
    }
}