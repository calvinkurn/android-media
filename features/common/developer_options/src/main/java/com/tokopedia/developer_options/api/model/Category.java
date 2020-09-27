package com.tokopedia.developer_options.api.model;

public class Category {
    private String _label;
    private int _value;

    public Category(){
        this._label = "";
        this._value = -0;
    }

    public void setId(int value){
        this._value = value;
    }

    public int getId(){
        return this._value;
    }

    public void setName(String label){
        this._label = label;
    }

    public String getName(){
        return this._label;
    }

}
