package com.tokopedia.sellerapp.gmstat.models;

/**
 * Created by normansyahputa on 11/2/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class GetTransactionGraph {

    @SerializedName("SuccessTrans")
    @Expose
    private Integer successTrans;
    @SerializedName("NewOrder")
    @Expose
    private Integer newOrder;
    @SerializedName("DeliveredProduct")
    @Expose
    private Integer deliveredProduct;
    @SerializedName("GrossRevenue")
    @Expose
    private Integer grossRevenue;
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
    private Integer diffRejectedAmount;
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
    private List<Integer> dateGraph = new ArrayList<Integer>();
    @SerializedName("SuccessTransGraph")
    @Expose
    private List<Integer> successTransGraph = new ArrayList<Integer>();
    @SerializedName("RejectedSumGraph")
    @Expose
    private List<Integer> rejectedSumGraph = new ArrayList<Integer>();
    @SerializedName("GrossGraph")
    @Expose
    private List<Integer> grossGraph = new ArrayList<Integer>();
    @SerializedName("NetGraph")
    @Expose
    private List<Integer> netGraph = new ArrayList<Integer>();
    @SerializedName("RejectedAmtGraph")
    @Expose
    private List<Integer> rejectedAmtGraph = new ArrayList<Integer>();
    @SerializedName("ShippingGraph")
    @Expose
    private List<Integer> shippingGraph = new ArrayList<Integer>();
    @SerializedName("AdsPGraph")
    @Expose
    private List<Integer> adsPGraph = new ArrayList<Integer>();
    @SerializedName("AdsSGraph")
    @Expose
    private List<Integer> adsSGraph = new ArrayList<Integer>();
    @SerializedName("PDateGraph")
    @Expose
    private List<Integer> pDateGraph = new ArrayList<Integer>();
    @SerializedName("PSuccessTransGraph")
    @Expose
    private List<Integer> pSuccessTransGraph = new ArrayList<Integer>();
    @SerializedName("PRejectedSumGraph")
    @Expose
    private List<Integer> pRejectedSumGraph = new ArrayList<Integer>();
    @SerializedName("PGrossGraph")
    @Expose
    private List<Integer> pGrossGraph = new ArrayList<Integer>();
    @SerializedName("PNetGraph")
    @Expose
    private List<Integer> pNetGraph = new ArrayList<Integer>();
    @SerializedName("PRejectedAmtGraph")
    @Expose
    private List<Integer> pRejectedAmtGraph = new ArrayList<Integer>();
    @SerializedName("PShippingGraph")
    @Expose
    private List<Integer> pShippingGraph = new ArrayList<Integer>();
    @SerializedName("PAdsPGraph")
    @Expose
    private List<Integer> pAdsPGraph = new ArrayList<Integer>();
    @SerializedName("PAdsSGraph")
    @Expose
    private List<Integer> pAdsSGraph = new ArrayList<Integer>();

    /**
     *
     * @return
     * The successTrans
     */
    public Integer getSuccessTrans() {
        return successTrans;
    }

    /**
     *
     * @param successTrans
     * The SuccessTrans
     */
    public void setSuccessTrans(Integer successTrans) {
        this.successTrans = successTrans;
    }

    /**
     *
     * @return
     * The newOrder
     */
    public Integer getNewOrder() {
        return newOrder;
    }

    /**
     *
     * @param newOrder
     * The NewOrder
     */
    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    /**
     *
     * @return
     * The deliveredProduct
     */
    public Integer getDeliveredProduct() {
        return deliveredProduct;
    }

    /**
     *
     * @param deliveredProduct
     * The DeliveredProduct
     */
    public void setDeliveredProduct(Integer deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    /**
     *
     * @return
     * The grossRevenue
     */
    public Integer getGrossRevenue() {
        return grossRevenue;
    }

    /**
     *
     * @param grossRevenue
     * The GrossRevenue
     */
    public void setGrossRevenue(Integer grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    /**
     *
     * @return
     * The netRevenue
     */
    public Integer getNetRevenue() {
        return netRevenue;
    }

    /**
     *
     * @param netRevenue
     * The NetRevenue
     */
    public void setNetRevenue(Integer netRevenue) {
        this.netRevenue = netRevenue;
    }

    /**
     *
     * @return
     * The rejectedAmount
     */
    public Integer getRejectedAmount() {
        return rejectedAmount;
    }

    /**
     *
     * @param rejectedAmount
     * The RejectedAmount
     */
    public void setRejectedAmount(Integer rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    /**
     *
     * @return
     * The shippingCost
     */
    public Integer getShippingCost() {
        return shippingCost;
    }

    /**
     *
     * @param shippingCost
     * The ShippingCost
     */
    public void setShippingCost(Integer shippingCost) {
        this.shippingCost = shippingCost;
    }

    /**
     *
     * @return
     * The cpcProduct
     */
    public Integer getCpcProduct() {
        return cpcProduct;
    }

    /**
     *
     * @param cpcProduct
     * The CpcProduct
     */
    public void setCpcProduct(Integer cpcProduct) {
        this.cpcProduct = cpcProduct;
    }

    /**
     *
     * @return
     * The cpcShop
     */
    public Integer getCpcShop() {
        return cpcShop;
    }

    /**
     *
     * @param cpcShop
     * The CpcShop
     */
    public void setCpcShop(Integer cpcShop) {
        this.cpcShop = cpcShop;
    }

    /**
     *
     * @return
     * The diffSuccessTrans
     */
    public Double getDiffSuccessTrans() {
        return diffSuccessTrans;
    }

    /**
     *
     * @param diffSuccessTrans
     * The DiffSuccessTrans
     */
    public void setDiffSuccessTrans(Double diffSuccessTrans) {
        this.diffSuccessTrans = diffSuccessTrans;
    }

    /**
     *
     * @return
     * The diffNewOrder
     */
    public Double getDiffNewOrder() {
        return diffNewOrder;
    }

    /**
     *
     * @param diffNewOrder
     * The DiffNewOrder
     */
    public void setDiffNewOrder(Double diffNewOrder) {
        this.diffNewOrder = diffNewOrder;
    }

    /**
     *
     * @return
     * The diffDeliveredProduct
     */
    public Double getDiffDeliveredProduct() {
        return diffDeliveredProduct;
    }

    /**
     *
     * @param diffDeliveredProduct
     * The DiffDeliveredProduct
     */
    public void setDiffDeliveredProduct(Double diffDeliveredProduct) {
        this.diffDeliveredProduct = diffDeliveredProduct;
    }

    /**
     *
     * @return
     * The diffGrossRevenue
     */
    public Double getDiffGrossRevenue() {
        return diffGrossRevenue;
    }

    /**
     *
     * @param diffGrossRevenue
     * The DiffGrossRevenue
     */
    public void setDiffGrossRevenue(Double diffGrossRevenue) {
        this.diffGrossRevenue = diffGrossRevenue;
    }

    /**
     *
     * @return
     * The diffNetRevenue
     */
    public Double getDiffNetRevenue() {
        return diffNetRevenue;
    }

    /**
     *
     * @param diffNetRevenue
     * The DiffNetRevenue
     */
    public void setDiffNetRevenue(Double diffNetRevenue) {
        this.diffNetRevenue = diffNetRevenue;
    }

    /**
     *
     * @return
     * The diffRejectedAmount
     */
    public Integer getDiffRejectedAmount() {
        return diffRejectedAmount;
    }

    /**
     *
     * @param diffRejectedAmount
     * The DiffRejectedAmount
     */
    public void setDiffRejectedAmount(Integer diffRejectedAmount) {
        this.diffRejectedAmount = diffRejectedAmount;
    }

    /**
     *
     * @return
     * The diffShippingCost
     */
    public Double getDiffShippingCost() {
        return diffShippingCost;
    }

    /**
     *
     * @param diffShippingCost
     * The DiffShippingCost
     */
    public void setDiffShippingCost(Double diffShippingCost) {
        this.diffShippingCost = diffShippingCost;
    }

    /**
     *
     * @return
     * The diffCpcShop
     */
    public Double getDiffCpcShop() {
        return diffCpcShop;
    }

    /**
     *
     * @param diffCpcShop
     * The DiffCpcShop
     */
    public void setDiffCpcShop(Double diffCpcShop) {
        this.diffCpcShop = diffCpcShop;
    }

    /**
     *
     * @return
     * The diffCpcProd
     */
    public Double getDiffCpcProd() {
        return diffCpcProd;
    }

    /**
     *
     * @param diffCpcProd
     * The DiffCpcProd
     */
    public void setDiffCpcProd(Double diffCpcProd) {
        this.diffCpcProd = diffCpcProd;
    }

    /**
     *
     * @return
     * The dateGraph
     */
    public List<Integer> getDateGraph() {
        return dateGraph;
    }

    /**
     *
     * @param dateGraph
     * The DateGraph
     */
    public void setDateGraph(List<Integer> dateGraph) {
        this.dateGraph = dateGraph;
    }

    /**
     *
     * @return
     * The successTransGraph
     */
    public List<Integer> getSuccessTransGraph() {
        return successTransGraph;
    }

    /**
     *
     * @param successTransGraph
     * The SuccessTransGraph
     */
    public void setSuccessTransGraph(List<Integer> successTransGraph) {
        this.successTransGraph = successTransGraph;
    }

    /**
     *
     * @return
     * The rejectedSumGraph
     */
    public List<Integer> getRejectedSumGraph() {
        return rejectedSumGraph;
    }

    /**
     *
     * @param rejectedSumGraph
     * The RejectedSumGraph
     */
    public void setRejectedSumGraph(List<Integer> rejectedSumGraph) {
        this.rejectedSumGraph = rejectedSumGraph;
    }

    /**
     *
     * @return
     * The grossGraph
     */
    public List<Integer> getGrossGraph() {
        return grossGraph;
    }

    /**
     *
     * @param grossGraph
     * The GrossGraph
     */
    public void setGrossGraph(List<Integer> grossGraph) {
        this.grossGraph = grossGraph;
    }

    /**
     *
     * @return
     * The netGraph
     */
    public List<Integer> getNetGraph() {
        return netGraph;
    }

    /**
     *
     * @param netGraph
     * The NetGraph
     */
    public void setNetGraph(List<Integer> netGraph) {
        this.netGraph = netGraph;
    }

    /**
     *
     * @return
     * The rejectedAmtGraph
     */
    public List<Integer> getRejectedAmtGraph() {
        return rejectedAmtGraph;
    }

    /**
     *
     * @param rejectedAmtGraph
     * The RejectedAmtGraph
     */
    public void setRejectedAmtGraph(List<Integer> rejectedAmtGraph) {
        this.rejectedAmtGraph = rejectedAmtGraph;
    }

    /**
     *
     * @return
     * The shippingGraph
     */
    public List<Integer> getShippingGraph() {
        return shippingGraph;
    }

    /**
     *
     * @param shippingGraph
     * The ShippingGraph
     */
    public void setShippingGraph(List<Integer> shippingGraph) {
        this.shippingGraph = shippingGraph;
    }

    /**
     *
     * @return
     * The adsPGraph
     */
    public List<Integer> getAdsPGraph() {
        return adsPGraph;
    }

    /**
     *
     * @param adsPGraph
     * The AdsPGraph
     */
    public void setAdsPGraph(List<Integer> adsPGraph) {
        this.adsPGraph = adsPGraph;
    }

    /**
     *
     * @return
     * The adsSGraph
     */
    public List<Integer> getAdsSGraph() {
        return adsSGraph;
    }

    /**
     *
     * @param adsSGraph
     * The AdsSGraph
     */
    public void setAdsSGraph(List<Integer> adsSGraph) {
        this.adsSGraph = adsSGraph;
    }

    /**
     *
     * @return
     * The pDateGraph
     */
    public List<Integer> getPDateGraph() {
        return pDateGraph;
    }

    /**
     *
     * @param pDateGraph
     * The PDateGraph
     */
    public void setPDateGraph(List<Integer> pDateGraph) {
        this.pDateGraph = pDateGraph;
    }

    /**
     *
     * @return
     * The pSuccessTransGraph
     */
    public List<Integer> getPSuccessTransGraph() {
        return pSuccessTransGraph;
    }

    /**
     *
     * @param pSuccessTransGraph
     * The PSuccessTransGraph
     */
    public void setPSuccessTransGraph(List<Integer> pSuccessTransGraph) {
        this.pSuccessTransGraph = pSuccessTransGraph;
    }

    /**
     *
     * @return
     * The pRejectedSumGraph
     */
    public List<Integer> getPRejectedSumGraph() {
        return pRejectedSumGraph;
    }

    /**
     *
     * @param pRejectedSumGraph
     * The PRejectedSumGraph
     */
    public void setPRejectedSumGraph(List<Integer> pRejectedSumGraph) {
        this.pRejectedSumGraph = pRejectedSumGraph;
    }

    /**
     *
     * @return
     * The pGrossGraph
     */
    public List<Integer> getPGrossGraph() {
        return pGrossGraph;
    }

    /**
     *
     * @param pGrossGraph
     * The PGrossGraph
     */
    public void setPGrossGraph(List<Integer> pGrossGraph) {
        this.pGrossGraph = pGrossGraph;
    }

    /**
     *
     * @return
     * The pNetGraph
     */
    public List<Integer> getPNetGraph() {
        return pNetGraph;
    }

    /**
     *
     * @param pNetGraph
     * The PNetGraph
     */
    public void setPNetGraph(List<Integer> pNetGraph) {
        this.pNetGraph = pNetGraph;
    }

    /**
     *
     * @return
     * The pRejectedAmtGraph
     */
    public List<Integer> getPRejectedAmtGraph() {
        return pRejectedAmtGraph;
    }

    /**
     *
     * @param pRejectedAmtGraph
     * The PRejectedAmtGraph
     */
    public void setPRejectedAmtGraph(List<Integer> pRejectedAmtGraph) {
        this.pRejectedAmtGraph = pRejectedAmtGraph;
    }

    /**
     *
     * @return
     * The pShippingGraph
     */
    public List<Integer> getPShippingGraph() {
        return pShippingGraph;
    }

    /**
     *
     * @param pShippingGraph
     * The PShippingGraph
     */
    public void setPShippingGraph(List<Integer> pShippingGraph) {
        this.pShippingGraph = pShippingGraph;
    }

    /**
     *
     * @return
     * The pAdsPGraph
     */
    public List<Integer> getPAdsPGraph() {
        return pAdsPGraph;
    }

    /**
     *
     * @param pAdsPGraph
     * The PAdsPGraph
     */
    public void setPAdsPGraph(List<Integer> pAdsPGraph) {
        this.pAdsPGraph = pAdsPGraph;
    }

    /**
     *
     * @return
     * The pAdsSGraph
     */
    public List<Integer> getPAdsSGraph() {
        return pAdsSGraph;
    }

    /**
     *
     * @param pAdsSGraph
     * The PAdsSGraph
     */
    public void setPAdsSGraph(List<Integer> pAdsSGraph) {
        this.pAdsSGraph = pAdsSGraph;
    }

}
