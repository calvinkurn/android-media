package com.tokopedia.reksadana.view.data.submit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.reksadana.view.data.common.Result;
import com.tokopedia.reksadana.view.data.initdata.Register;

public class RegisterUser {
    @Expose
    @SerializedName("result")
    Result result;
    @Expose
    @SerializedName("message")
    String message;
    public RegisterUser(Result result, String message){
        this.result = result;
        this.message = message;
    }
    public Result result(){
        return result;
    }
    public String message(){
        return message;
    }
}
