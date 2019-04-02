package com.tokopedia.core.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Hendry on 2/28/2017.
 */
// Example json Response
// {"errors":[  {"code":"","title":"Validate param.","detail":"Batas tertinggi biaya yang bisa Anda tentukan: Rp 2.000"},
//              {"code":"","title":"Validate param.","detail":"Anggaran minimal 5 kali lebih besar dari Penawaran Maks."}
//          ]}

@Deprecated
public class TopAdsResponseError extends BaseResponseError {
    private static final String ERROR_KEY = "errors";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<Error> errors = null;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public String getErrorKey() {
        return ERROR_KEY;
    }

    public boolean hasBody(){
        return (errors!= null && errors.size() > 0);
    }

    @Override
    public IOException createException() {
        return new ResponseErrorException(errors);
    }
}


