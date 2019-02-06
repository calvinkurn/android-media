package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class Acf {

    @SerializedName("promo_codes")
    @Expose
    private List<PromoCode> promoCodeList = new ArrayList<>();
    @SerializedName("multiple_promo_code")
    @Expose
    private boolean multiplePromoCode;

    public List<PromoCode> getPromoCodeList() {
        return promoCodeList;
    }

    public boolean isMultiplePromoCode() {
        return multiplePromoCode;
    }
}
