
package com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUsernameData {

    @SerializedName("bymeRegisterAffiliateName")
    @Expose
    private BymeRegisterAffiliateName bymeRegisterAffiliateName;

    public BymeRegisterAffiliateName getBymeRegisterAffiliateName() {
        return bymeRegisterAffiliateName;
    }

    public void setBymeRegisterAffiliateName(BymeRegisterAffiliateName bymeRegisterAffiliateName) {
        this.bymeRegisterAffiliateName = bymeRegisterAffiliateName;
    }

}
