package com.tokopedia.gm.statistic.data.source.cloud.model.graph;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 7/20/17.
 */
public class GetBuyerGraph implements GetDateGraph{

    @SerializedName("total_buyer")
    @Expose
    private long totalBuyer;
    @SerializedName("male_buyer")
    @Expose
    private int maleBuyer;
    @SerializedName("female_buyer")
    @Expose
    private int femaleBuyer;
    @SerializedName("success_trans")
    @Expose
    private int successTrans;
    @SerializedName("product_sold")
    @Expose
    private int productSold;
    @SerializedName("age1")
    @Expose
    private int age1;
    @SerializedName("age2")
    @Expose
    private int age2;
    @SerializedName("age3")
    @Expose
    private int age3;
    @SerializedName("age4")
    @Expose
    private int age4;
    @SerializedName("age5")
    @Expose
    private int age5;
    @SerializedName("diff_total")
    @Expose
    private double diffTotal;
    @SerializedName("date_graph")
    @Expose
    private List<Integer> dateGraph = new ArrayList<>();
    @SerializedName("male_graph")
    @Expose
    private List<Integer> maleGraph = new ArrayList<>();
    @SerializedName("female_graph")
    @Expose
    private List<Integer> femaleGraph = new ArrayList<>();
    @SerializedName("total_graph")
    @Expose
    private List<Integer> totalGraph = new ArrayList<>();
    @SerializedName("p_date_graph")
    @Expose
    private List<Integer> pDateGraph = new ArrayList<>();
    @SerializedName("p_male_graph")
    @Expose
    private List<Integer> pMaleGraph = new ArrayList<>();
    @SerializedName("p_female_graph")
    @Expose
    private List<Integer> pFemaleGraph = new ArrayList<>();
    @SerializedName("p_total_graph")
    @Expose
    private List<Integer> pTotalGraph = new ArrayList<>();

    /**
     * @return The totalBuyer
     */
    public long getTotalBuyer() {
        return totalBuyer;
    }

    /**
     * @param totalBuyer The TotalBuyer
     */
    public void setTotalBuyer(long totalBuyer) {
        this.totalBuyer = totalBuyer;
    }

    /**
     * @return The maleBuyer
     */
    public int getMaleBuyer() {
        return maleBuyer;
    }

    /**
     * @param maleBuyer The MaleBuyer
     */
    public void setMaleBuyer(int maleBuyer) {
        this.maleBuyer = maleBuyer;
    }

    /**
     * @return The femaleBuyer
     */
    public int getFemaleBuyer() {
        return femaleBuyer;
    }

    /**
     * @param femaleBuyer The FemaleBuyer
     */
    public void setFemaleBuyer(int femaleBuyer) {
        this.femaleBuyer = femaleBuyer;
    }

    /**
     * @return The successTrans
     */
    public int getSuccessTrans() {
        return successTrans;
    }

    /**
     * @param successTrans The SuccessTrans
     */
    public void setSuccessTrans(int successTrans) {
        this.successTrans = successTrans;
    }

    /**
     * @return The productSold
     */
    public int getProductSold() {
        return productSold;
    }

    /**
     * @param productSold The ProductSold
     */
    public void setProductSold(int productSold) {
        this.productSold = productSold;
    }

    /**
     * @return The age1
     */
    public int getAge1() {
        return age1;
    }

    /**
     * @param age1 The Age1
     */
    public void setAge1(int age1) {
        this.age1 = age1;
    }

    /**
     * @return The age2
     */
    public int getAge2() {
        return age2;
    }

    /**
     * @param age2 The Age2
     */
    public void setAge2(int age2) {
        this.age2 = age2;
    }

    /**
     * @return The age3
     */
    public int getAge3() {
        return age3;
    }

    /**
     * @param age3 The Age3
     */
    public void setAge3(int age3) {
        this.age3 = age3;
    }

    /**
     * @return The age4
     */
    public int getAge4() {
        return age4;
    }

    /**
     * @param age4 The Age4
     */
    public void setAge4(int age4) {
        this.age4 = age4;
    }

    /**
     * @return The age5
     */
    public int getAge5() {
        return age5;
    }

    /**
     * @param age5 The Age5
     */
    public void setAge5(int age5) {
        this.age5 = age5;
    }

    /**
     * @return The diffTotal
     */
    public double getDiffTotal() {
        return diffTotal;
    }

    /**
     * @param diffTotal The DiffTotal
     */
    public void setDiffTotal(double diffTotal) {
        this.diffTotal = diffTotal;
    }

    /**
     * @return The dateGraph
     */
    public List<Integer> getDateGraph() {
        return dateGraph;
    }

    /**
     * @param dateGraph The DateGraph
     */
    public void setDateGraph(List<Integer> dateGraph) {
        this.dateGraph = dateGraph;
    }

    /**
     * @return The maleGraph
     */
    public List<Integer> getMaleGraph() {
        return maleGraph;
    }

    /**
     * @param maleGraph The MaleGraph
     */
    public void setMaleGraph(List<Integer> maleGraph) {
        this.maleGraph = maleGraph;
    }

    /**
     * @return The femaleGraph
     */
    public List<Integer> getFemaleGraph() {
        return femaleGraph;
    }

    /**
     * @param femaleGraph The FemaleGraph
     */
    public void setFemaleGraph(List<Integer> femaleGraph) {
        this.femaleGraph = femaleGraph;
    }

    /**
     * @return The totalGraph
     */
    public List<Integer> getTotalGraph() {
        return totalGraph;
    }

    /**
     * @param totalGraph The TotalGraph
     */
    public void setTotalGraph(List<Integer> totalGraph) {
        this.totalGraph = totalGraph;
    }

    /**
     * @return The pDateGraph
     */
    public List<Integer> getPDateGraph() {
        return pDateGraph;
    }

    /**
     * @param pDateGraph The PDateGraph
     */
    public void setPDateGraph(List<Integer> pDateGraph) {
        this.pDateGraph = pDateGraph;
    }

    /**
     * @return The pMaleGraph
     */
    public List<Integer> getPMaleGraph() {
        return pMaleGraph;
    }

    /**
     * @param pMaleGraph The PMaleGraph
     */
    public void setPMaleGraph(List<Integer> pMaleGraph) {
        this.pMaleGraph = pMaleGraph;
    }

    /**
     * @return The pFemaleGraph
     */
    public List<Integer> getPFemaleGraph() {
        return pFemaleGraph;
    }

    /**
     * @param pFemaleGraph The PFemaleGraph
     */
    public void setPFemaleGraph(List<Integer> pFemaleGraph) {
        this.pFemaleGraph = pFemaleGraph;
    }

    /**
     * @return The pTotalGraph
     */
    public List<Integer> getPTotalGraph() {
        return pTotalGraph;
    }

    /**
     * @param pTotalGraph The PTotalGraph
     */
    public void setPTotalGraph(List<Integer> pTotalGraph) {
        this.pTotalGraph = pTotalGraph;
    }

}
