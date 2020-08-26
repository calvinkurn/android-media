package data

import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.data.pdp.PassengerForm

object MockMetaData {
    fun getMetaDataResponse(): MetaDataResponse {
        return MetaDataResponse(
                categoryName="event",
                error="",
                itemIds= listOf("6551"),
                itemMap= listOf(
                        ItemMapResponse(
                                basePrice="0",
                                categoryId="1",
                                childCategoryIds="6",
                                commission=0,
                                commissionType="",
                                currencyPrice=0,
                                description="Potongan 25% untuk satu kalipemesanan kelas olahraga diDOOgether",
                                email="hefdy.elprama+h5@tokopedia.com, endTime=31 Aug 2020 23:00",
                                error="",
                                flagId="0",
                                id="6551",
                                invoiceId="4655016",
                                invoiceItemId="4651050",
                                invoiceStatus="BOOKED",
                                locationDesc="Online",
                                locationName="Online",
                                mobile="081908878591",
                                name="Single Voucher",
                                orderTraceId="b18c0bda-0afe-4075-5890-634c50c7510e",
                                packageId="2300",
                                packageName="Single Voucher",
                                paymentType="-",
                                price=15000,
                                productAppUrl="tokopedia://events/doolive-ala-carte-booking-36116",
                                productId="36116",
                                productImage="https://ecs7.tokopedia.net/img/banner/2020/7/17/22166894/22166894_f4fb24c9-6637-4594-9464-c548840d076e.jpg",
                                productName="DOOlive Ala Carte Booking",
                                providerId="3",
                                providerInvoiceCode="",
                                providerOrderId="",
                                providerPackageId="",
                                providerScheduleId="oe:doo1",
                                providerTicketId="oe:doo1",
                                quantity=2,
                                scheduleTimestamp="1598806800",
                                startTime="15 Jul 2020 13:00",
                                totalPrice=30000,
                                webAppUrl="https://www.tokopedia.com/events/detail/doolive-ala-carte-booking-36116",
                                productWebUrl="https://www.tokopedia.com/events/detail/doolive-ala-carte-booking-36116",
                                passengerForms= mutableListOf())),
                orderSubTitle="Single Voucher",
                orderTitle="DOOlive Ala Carte Booking",
                productIds= listOf("36116"),
                productNames= listOf("DOOlive Ala Carte Booking"),
                providerIds= listOf("3"),
                quantity=2,
                totalPrice=30000
        )
    }
}