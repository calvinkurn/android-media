package com.tokopedia.updateinactivephone.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlUpdatePhoneStatusResponse {

    @SerializedName("changeInactivePhone")
    @Expose
    private GqlUpdatePhoneStatusResponse.ChangeInactivePhoneQuery changeInactivePhoneQuery;

    public GqlUpdatePhoneStatusResponse.ChangeInactivePhoneQuery getChangeInactivePhoneQuery() {
        return changeInactivePhoneQuery;
    }

    public void setChangeInactivePhoneQuery(GqlUpdatePhoneStatusResponse.ChangeInactivePhoneQuery validateInactivePhone) {
        this.changeInactivePhoneQuery = validateInactivePhone;
    }

    @Override
    public String toString() {
        return "ClassPojo [ChangeInactivePhone = " + changeInactivePhoneQuery + "]";
    }


    public class ChangeInactivePhoneQuery {

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
