
package com.tokopedia.core.recharge.model.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Operator {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("minimum_length")
    @Expose
    private Integer minimumLength;
    @SerializedName("maximum_length")
    @Expose
    private Integer maximumLength;

    /**
     * 
     * @return
     *     The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The minimumLength
     */
    public Integer getMinimumLength() {
        return minimumLength;
    }

    /**
     *
     * @param minimumLength
     * The minimum_length
     */
    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    /**
     *
     * @return
     * The maximumLength
     */
    public Integer getMaximumLength() {
        return maximumLength;
    }

    /**
     *
     * @param maximumLength
     * The maximum_length
     */
    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

}
