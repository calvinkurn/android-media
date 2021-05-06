package com.tokopedia.travel_slice.hotel.ui

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.SMALL_IMAGE
import com.bumptech.glide.Glide
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.travel_slice.R
import com.tokopedia.travel_slice.hotel.data.HotelData
import com.tokopedia.travel_slice.hotel.data.HotelOrderListModel
import com.tokopedia.travel_slice.ui.provider.TravelSliceActivity
import kotlin.math.min

/**
 * @author by jessica on 25/11/20
 */

object HotelSliceProviderUtil {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getHotelRecommendationSlices(context: Context, sliceUri: Uri, cityName: String, checkIn: String,
                                     hotelList: List<HotelData>): Slice {

        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_get_hotel_result_title, cityName)
                primaryAction = SliceAction.create(
                        buildIntentFromHotelDashboard(context),
                        IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                        SMALL_IMAGE, "")
            }
            gridRow {
                hotelList.subList(0, min(4, hotelList.size)).forEach {
                    it.image.firstOrNull()?.urlMax300?.let { image ->
                        cell {
                            addImage(IconCompat.createWithBitmap(image.getBitmap(context)), ListBuilder.LARGE_IMAGE)
                            addTitleText(it.name)
                            addText(it.roomPrice.firstOrNull()?.totalPrice ?: "")
                            contentIntent = buildIntentFromHotelDetail(context, it.id, checkIn, it.location.cityName)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFailedFetchDataSlices(context: Context, sliceUri: Uri): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_hotel_failed_desc)
                primaryAction = SliceAction.create(
                        buildIntentFromHotelDashboard(context),
                        IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                        SMALL_IMAGE, "")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getEmptyDataSlices(context: Context, sliceUri: Uri): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_empty_hotel_desc)
                primaryAction = SliceAction.create(
                        buildIntentFromHotelDashboard(context),
                        IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                        SMALL_IMAGE, "")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getEmptyOrderListSlices(context: Context, sliceUri: Uri): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_empty_order_list)
                primaryAction = SliceAction.create(buildIntentFromHotelOrderListApplink(context),
                        IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                        SMALL_IMAGE, "")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getUserNotLoggedIn(context: Context, sliceUri: Uri): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_not_login_desc)
                primaryAction = PendingIntent.getActivity(
                        context, 0, RouteManager.getIntent(context, ApplinkConst.LOGIN), 0
                ).let {
                    SliceAction.create(it, IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                            SMALL_IMAGE, "")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getLoadingStateSlices(context: Context, sliceUri: Uri): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_loading_text)
            }
        }
    }

    private fun String.getBitmap(context: Context): Bitmap? = Glide.with(context).asBitmap().load(this).submit().get()

    private fun buildIntentFromHotelDetail(context: Context, hotelId: Long, checkIn: String, city: String): PendingIntent {
        return PendingIntent.getActivity(context, 0,
                TravelSliceActivity.createHotelDetailIntent(context, context.getString(R.string.hotel_detail_applink, hotelId.toString(), checkIn), city),
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildIntentFromHotelDashboard(context: Context): PendingIntent =
        PendingIntent.getActivity(context, 0,
                TravelSliceActivity.createHotelDashboardIntent(context, ApplinkConstInternalTravel.DASHBOARD_HOTEL),
                PendingIntent.FLAG_UPDATE_CURRENT)

    private fun buildIntentFromHotelOrderApplink(context: Context, applink: String, city: String): PendingIntent =
        PendingIntent.getActivity(context, 0,
                 TravelSliceActivity.createHotelOrderDetailIntent(context, applink, city),
                PendingIntent.FLAG_UPDATE_CURRENT)

    private fun buildIntentFromHotelOrderListApplink(context: Context): PendingIntent =
            PendingIntent.getActivity(context, 0,
                    TravelSliceActivity.createHotelOrderListIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT)

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getMyHotelOrderSlices(context: Context, sliceUri: Uri, orderList: List<HotelOrderListModel>): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_order_title)
                primaryAction = SliceAction.create(buildIntentFromHotelOrderListApplink(context),
                        IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                        SMALL_IMAGE, "")
            }

            orderList.forEach {
                row {
                    title = it.title
                    subtitle = it.statusStr

                    if (it.items.isNotEmpty()) {
                        setTitleItem(IconCompat.createWithBitmap(it.items[0].imageUrl.getBitmap(context)), SMALL_IMAGE)
                    } else {
                        setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_travel_slice_hotel), SMALL_IMAGE)
                    }

                    primaryAction = SliceAction.create(buildIntentFromHotelOrderApplink(context, it.applink, it.title),
                            IconCompat.createWithResource(context, com.tokopedia.abstraction.R.drawable.tab_indicator_ab_tokopedia),
                            ListBuilder.ICON_IMAGE, "")
                }
            }
        }
    }
}