package com.tokopedia.flight.review.domain;

/**
 * @author by alvarisi on 4/3/18.
 */

public interface FlightVoucherCodeWrapper {

    int voucherResultCode();

    String voucherCode();

    String voucherMessage();

    String voucherDiscountAmount();

    int couponResultCode();

    String couponCode();

    String couponTitle();

    String couponMessage();

    String couponDiscountAmount();

}
