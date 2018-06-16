package com.tokopedia.reksadana.view.data.submit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @Expose
    @SerializedName("mf_register_user")
    RegisterUser mf_register_user;
    public Data(RegisterUser mf_register_user){
        this.mf_register_user = mf_register_user;
    }
    public RegisterUser mf_register_user(){
        return mf_register_user;
    }
}
