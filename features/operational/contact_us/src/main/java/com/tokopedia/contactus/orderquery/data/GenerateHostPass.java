package com.tokopedia.contactus.orderquery.data;

import java.util.HashMap;

public class GenerateHostPass {

    private static final String PARAM_NEW_ADD = "new_add";


    String newAdd = "0";

    public String getNewAdd() {
        return newAdd;
    }

    public void setNewAdd(String newAdd) {
        this.newAdd = newAdd;
    }

    public HashMap<String,String> getGenerateHostParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_NEW_ADD, getNewAdd());
        return param;
    }
}