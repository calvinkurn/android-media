
package com.tokopedia.tkpd.recharge.model.operator;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("additional_form")
    @Expose
    private List<Object> additionalForm = new ArrayList<Object>();
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("maximum_length")
    @Expose
    private Integer maximumLength;
    @SerializedName("minimum_length")
    @Expose
    private Integer minimumLength;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("prefix")
    @Expose
    private List<String> prefix = new ArrayList<String>();
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("weight")
    @Expose
    private Integer weight;

    /**
     * 
     * @return
     *     The additionalForm
     */
    public List<Object> getAdditionalForm() {
        return additionalForm;
    }

    /**
     * 
     * @param additionalForm
     *     The additional_form
     */
    public void setAdditionalForm(List<Object> additionalForm) {
        this.additionalForm = additionalForm;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The maximumLength
     */
    public Integer getMaximumLength() {
        return maximumLength;
    }

    /**
     * 
     * @param maximumLength
     *     The maximum_length
     */
    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    /**
     * 
     * @return
     *     The minimumLength
     */
    public Integer getMinimumLength() {
        return minimumLength;
    }

    /**
     * 
     * @param minimumLength
     *     The minimum_length
     */
    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The prefix
     */
    public List<String> getPrefix() {
        return prefix;
    }

    /**
     * 
     * @param prefix
     *     The prefix
     */
    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    /**
     * 
     * @return
     *     The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * 
     * @param slug
     *     The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 
     * @param weight
     *     The weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
