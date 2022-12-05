package com.tokopedia.mvc.presentation

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import org.junit.Assert.*
import org.junit.Test

class VoucherBuyerFinderTest {

    private val sut = VoucherBuyerFinder()

    //region Shop Voucher
    //Test case 1
    @Test
    fun `When voucher type is shop voucher, target is public and promo type is cashback, should return all buyer and new follower as a result`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.CASHBACK

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 2
    @Test
    fun `When voucher type is shop voucher, target is public and promo type is free shipping, should return all buyer as a result`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.FREE_SHIPPING

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 3
    @Test
    fun `When voucher type is shop voucher, target is public and promo type is discount, should return all buyer and new follower as a result`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.DISCOUNT

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 4
    @Test
    fun `When voucher type is shop voucher, target is private and promo type is cashback, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.CASHBACK

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 5
    @Test
    fun `When voucher type is shop voucher, target is private and promo type is free shipping, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.FREE_SHIPPING

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 6
    @Test
    fun `When voucher type is shop voucher, target is private and promo type is discount, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.SHOP_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.DISCOUNT

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }
    //endregion

    //region Product Voucher
    //Test case 7
    @Test
    fun `When voucher type is product voucher, target is public and promo type is cashback, should return all buyer and new follower`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.CASHBACK

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 8
    @Test
    fun `When voucher type is product voucher, target is public and promo type is free shipping, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.FREE_SHIPPING

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 9
    @Test
    fun `When voucher type is product voucher, target is public and promo type is discount, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PUBLIC
        val promoType = PromoType.DISCOUNT

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 10
    @Test
    fun `When voucher type is product voucher, target is private and promo type is cashback, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.CASHBACK

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 11
    @Test
    fun `When voucher type is product voucher, target is private and promo type is free shipping, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.FREE_SHIPPING

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }

    //Test case 12
    @Test
    fun `When voucher type is product voucher, target is private and promo type is discount, should return all buyer`() {
        //Given
        val voucherType = VoucherServiceType.PRODUCT_VOUCHER
        val voucherTargetBuyer = VoucherTarget.PRIVATE
        val promoType = PromoType.DISCOUNT

        val expected = listOf(VoucherTargetBuyer.ALL_BUYER)

        //When
        val actual = sut.findBuyerTarget(voucherType, voucherTargetBuyer, promoType)

        //Then
        assertEquals(expected, actual)
    }
    //endregion
}
