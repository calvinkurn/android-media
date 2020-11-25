package com.tokopedia.travel_slice.ui.provider

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.SMALL_IMAGE
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.travel_slice.data.HotelData
import com.tokopedia.travel_slice.data.HotelOrderListModel
import com.tokopedia.travel_slice.R
import kotlin.math.max

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
                        IconCompat.createWithResource(context, R.drawable.abc_tab_indicator_material),
                        ListBuilder.ICON_IMAGE, "")
            }
            gridRow {
                hotelList.subList(0, max(3, hotelList.size)).forEach {
                    it.image.firstOrNull()?.urlMax300?.let { image ->
                        cell {
                            addImage(IconCompat.createWithBitmap(image.getBitmap(context)), ListBuilder.LARGE_IMAGE)
                            addTitleText(it.name)
                            addText(it.roomPrice.firstOrNull()?.totalPrice ?: "")
                            contentIntent = buildIntentFromHotelDetail(context, it.id, checkIn)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFailedFetchDataSlices(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_failed_desc)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getUserNotLoggedIn(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_not_login_desc)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getLoadingStateSlices(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_title)
                subtitle = context.getString(R.string.slice_loading_text)
            }
        }
    }

    private fun String.getBitmap(context: Context): Bitmap? = Glide.with(context).asBitmap().load(this).submit().get()

    private fun buildIntentFromHotelDetail(context: Context, hotelId: Long, checkIn: String): PendingIntent {
        return PendingIntent.getActivity(context, 0,
                allowReads { RouteManager.getIntent(context, context.getString(R.string.hotel_detail_applink, hotelId.toString(), checkIn)) },
                0)
    }

    private fun buildIntentFromHotelDashboard(context: Context): PendingIntent = allowReads {
        PendingIntent.getActivity(context, 0,
                allowReads { RouteManager.getIntent(context, context.getString(R.string.hotel_dashboard_applink)) },
                0)
    }

    private fun buildIntentFromApplink(context: Context, applink: String): PendingIntent = allowReads {
        PendingIntent.getActivity(context, 0,
                allowReads { RouteManager.getIntent(context, applink) },
                0)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getMyHotelOrderSlices(context: Context, sliceUri: Uri, orderList: List<HotelOrderListModel>): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_hotel_order_title)
            }

            orderList.forEach {
                row {
                    title = it.title
                    subtitle = it.statusStr
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_hotel), SMALL_IMAGE)

                    primaryAction = SliceAction.create(buildIntentFromApplink(context, it.applink),
                            IconCompat.createWithResource(context, R.drawable.abc_tab_indicator_material),
                            ListBuilder.ICON_IMAGE, "")
                }
            }
        }
    }

    fun <T> allowReads(block: () -> T): T {
        val oldPolicy = StrictMode.allowThreadDiskReads()
        try {
            return block()
        } finally {
            StrictMode.setThreadPolicy(oldPolicy)
        }
    }

}