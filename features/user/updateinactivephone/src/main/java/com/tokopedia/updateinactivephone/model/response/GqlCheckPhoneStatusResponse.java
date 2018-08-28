package com.tokopedia.updateinactivephone.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlCheckPhoneStatusResponse {

    @SerializedName("validateInactivePhone")
    @Expose
    private ValidateInactivePhone validateInactivePhone;

    public ValidateInactivePhone getValidateInactivePhone() {
        return validateInactivePhone;
    }

    public void setValidateInactivePhone(ValidateInactivePhone validateInactivePhone) {
        this.validateInactivePhone = validateInactivePhone;
    }

    @Override
    public String toString() {
        return "ClassPojo [ValidateInactivePhone = " + validateInactivePhone + "]";
    }


    public class ValidateInactivePhone {

        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("__typename")
        @Expose
        private String __typename;

        @SerializedName("is_success")
        @Expose
        private boolean success;

        @SerializedName("error")
        @Expose
        private String error;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

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
            return "ClassPojo [userId = " + userId + ", error = " + error + ", __typename = " + __typename + ", success = " + success + "]";
        }
    }
}
