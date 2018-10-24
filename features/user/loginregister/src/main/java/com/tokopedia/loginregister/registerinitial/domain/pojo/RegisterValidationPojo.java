package com.tokopedia.loginregister.registerinitial.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationPojo {

        @SerializedName("isExist")
        @Expose
        private Boolean isExist;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("view")
        @Expose
        private String view;

        public Boolean getExist() {
            return isExist;
        }

        public void setExist(Boolean exist) {
            isExist = exist;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }
}
