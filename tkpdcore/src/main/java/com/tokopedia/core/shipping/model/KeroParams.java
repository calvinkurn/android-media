package com.tokopedia.core.shipping.model;

/**
 * Created by Herdi_WORK on 21.09.16.
 */

public class KeroParams {

    private String names;
    private String origin;
    private String destination;
    private String weight;
    private String type;
    private String from;
    private String token;
    private String ut;

    public KeroParams (){

    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getNames() {
        return names;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getUt() {
        return ut;
    }
}
