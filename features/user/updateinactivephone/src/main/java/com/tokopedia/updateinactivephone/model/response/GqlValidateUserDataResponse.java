package com.tokopedia.updateinactivephone.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlValidateUserDataResponse {

    @SerializedName("validateInactiveNewPhone")
    @Expose
    private GqlValidateUserDataResponse.ValidateUserDataResponse validateUserDataResponse;

    public GqlValidateUserDataResponse.ValidateUserDataResponse getValidateUserDataResponse() {
        return validateUserDataResponse;
    }

    public void setValidateInactivePhone(GqlValidateUserDataResponse.ValidateUserDataResponse validateUserDataResponse) {
        this.validateUserDataResponse = validateUserDataResponse;
    }

    @Override
    public String toString() {
        return "ClassPojo [ValidateInactivePhone = " + validateUserDataResponse + "]";
    }


    public class ValidateUserDataResponse {

        @SerializedName("__typename")
        @Expose
        private String __typename;

        @SerializedName("is_success")
        @Expose
        private boolean success;

        @SerializedName("error")
        @Expose
        private String error;

        public String get__typename() {
            return __typename;
        }

        public void set__typename(String __typename) {
            this.__typename = __typename;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "ClassPojo [error = " + error + ", __typename = " + __typename + ", success = " + success + "]";
        }
    }

}
