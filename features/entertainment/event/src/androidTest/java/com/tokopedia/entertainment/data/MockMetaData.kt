package com.tokopedia.entertainment.data

import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse

object MockMetaData {
    fun getMetaDataResponse(): MetaDataResponse {
        return MetaDataResponse(
                categoryName="event",
                error="",
                itemIds= listOf("46195"),
                itemMap= listOf(
                        ItemMapResponse(
                                basePrice="0",
                                categoryId="1",
                                childCategoryIds="6",
                                commission=0,
                                commissionType="",
                                currencyPrice=111,
                                description="Potongan 25% untuk satu kalipemesanan kelas olahraga diDOOgether",
                                email="elly.susilowati+089@tokopedia.com",
                                endTime="08 Oct 2020 00:00",
                                error="",
                                flagId="1064960",
                                id="46195",
                                invoiceId="551834",
                                invoiceItemId="546895",
                                invoiceStatus="BOOKED",
                                locationDesc="Online",
                                locationName="7 Day JR All Shikoku Pass JR Pass",
                                mobile="081908878591",
                                name="Adult",
                                orderTraceId="df6681a0-19b3-4320-656e-4c462488866b",
                                packageId="2104",
                                packageName="7天 · 普通车厢 · 邮寄-中国大陆/日本/台湾/新加坡",
                                paymentType="-",
                                price=1750082,
                                productAppUrl="tokopedia://events/7-day-jr-all-shikoku-pass-23233",
                                productId="23233",
                                productImage="https://res.klook.com/images/fl_progressive.lossy,q_65,c_fill,w_374,h_208/w_80,x_15,y_15,g_south_east,l_klook_water/activities/jcdtfbgl5sb3i1xe3hpx/shikokuareapass,shikokurailwaypass,shikokuareapasstwdelivery.jpg",
                                productName="7 Day JR All Shikoku Pass",
                                providerId="7",
                                providerInvoiceCode="",
                                providerOrderId="",
                                providerPackageId="",
                                providerScheduleId="1600966800",
                                providerTicketId="8028030201~9000012946",
                                quantity=1,
                                scheduleTimestamp="1598806800",
                                startTime="08 Sep 2020 00:00",
                                totalPrice=1750082,
                                webAppUrl="https://staging.tokopedia.com/events/detail/7-day-jr-all-shikoku-pass-23233",
                                productWebUrl="https://staging.tokopedia.com/events/detail/7-day-jr-all-shikoku-pass-23233",
                                passengerForms= mutableListOf())),
                orderSubTitle="Adult",
                orderTitle="7 Day JR All Shikoku Pass",
                productIds= listOf("23233"),
                productNames= listOf("7 Day JR All Shikoku Pass"),
                providerIds= listOf("7"),
                quantity=1,
                totalPrice=1750082
        )
    }
}
