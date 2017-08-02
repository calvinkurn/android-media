package com.tokopedia.core.manage.people.bank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class PaymentListModel {

    @SerializedName("bca_oneklik_data")
    private List<BcaOneClickUserModel> bcaOneClickUserModels;

    public List<BcaOneClickUserModel> getBcaOneClickUserModels() {
        return bcaOneClickUserModels;
    }

    public void setBcaOneClickUserModels(List<BcaOneClickUserModel> bcaOneClickUserModels) {
        this.bcaOneClickUserModels = bcaOneClickUserModels;
    }
}
