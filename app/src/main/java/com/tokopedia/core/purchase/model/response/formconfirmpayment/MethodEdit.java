package com.tokopedia.core.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class MethodEdit {

    @SerializedName("method_id_chosen")
    @Expose
    private String methodIdChosen;
    @SerializedName("method_list")
    @Expose
    private List<Method> methodList = new ArrayList<>();

    public String getMethodIdChosen() {
        return methodIdChosen;
    }

    public void setMethodIdChosen(String methodIdChosen) {
        this.methodIdChosen = methodIdChosen;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public List<Method> getMethodList(boolean removeId5) {
        Iterator<Method> iter = methodList.iterator();

        while (iter.hasNext()) {
            Method method = iter.next();

            if (method.getMethodId().equals("5") && removeId5)
                iter.remove();
        }
        /*        for (Method data : methodList) {
            if (data.getMethodId().equals("5") && removeId5) {
                methodList.remove(data);
            }
        }*/
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }
}
