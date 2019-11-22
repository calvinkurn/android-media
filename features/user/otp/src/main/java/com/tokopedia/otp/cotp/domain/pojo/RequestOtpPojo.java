
package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestOtpPojo {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    @SerializedName("client_view")
    @Expose
    private ClientView clientView;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ClientView getClientView() { return clientView; }

    public void setClientView(ClientView clientView) { this.clientView = clientView; }

    public class ClientView {

        @SerializedName("miscall_phone_number")
        @Expose
        private String phoneHint;

        public String getPhoneHint() { return phoneHint; }

        public void setPhoneHint(String phoneHint) { this.phoneHint = phoneHint; }
    }
}
