package com.tokopedia.reksadana.view.data.initdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @Expose
    @SerializedName("register")
    private Register register;

    public Data(Register register) {
        this.register = register;
    }
    public Register register(){
        return register;
    }
}
