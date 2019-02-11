package com.tokopedia.flight.review.domain;

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