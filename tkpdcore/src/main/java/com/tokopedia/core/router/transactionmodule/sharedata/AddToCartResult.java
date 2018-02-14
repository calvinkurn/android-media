package com.tokopedia.core.router.transactionmodule.sharedata;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartResult {
    private boolean success;
    private String message;

    public AddToCartResult() {
    }

    private AddToCartResult(Builder builder) {
        setSuccess(builder.success);
        setMessage(builder.message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static final class Builder {
        private boolean success;
        private String message;

        public Builder() {
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public AddToCartResult build() {
            return new AddToCartResult(this);
        }
    }
}
