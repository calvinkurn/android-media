package com.tokopedia.travel_slice.flight.ui

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
import com.tokopedia.travel_slice.R
import com.tokopedia.travel_slice.flight.data.FlightOrderListEntity
import com.tokopedia.travel_slice.ui.provider.HotelSliceProviderUtil.allowReads

/**
 * @author by jessica on 25/11/20
 */

object FlightSliceProviderUtil {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFailedFetchDataSlices(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_flight_title)
                subtitle = context.getString(R.string.slice_failed_desc)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getUserNotLoggedIn(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_flight_title)
                subtitle = context.getString(R.string.slice_not_login_desc)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getLoadingStateSlices(context: Context, sliceUri: Uri): Slice? {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = context.getString(R.string.slice_flight_title)
                subtitle = context.getString(R.string.slice_loading_text)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getFlightOrderSlices(context: Context, sliceUri: Uri, orderList: List<FlightOrderListEntity>): Slice? =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.slice_flight_title)
                }

                orderList.forEach {
                    row {
                        title = it.title
                        subtitle = it.statusStr
                        if (it.items.isNotEmpty()) {
                            setTitleItem(IconCompat.createWithBitmap(it.items[0].imageUrl.getBitmap(context)), SMALL_IMAGE)
                        } else {
                            setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_flight_icon), SMALL_IMAGE)
                        }

                        primaryAction = SliceAction.create(buildIntentFromApplink(context, String.format("%s/%s", ApplinkConst.FLIGHT_ORDER, it.id)),
                                IconCompat.createWithResource(context, R.drawable.abc_tab_indicator_material),
                                ListBuilder.ICON_IMAGE, "")
                    }
                    gridRow {
                        it.metaData.forEach { meta ->
                            cell {
                                addTitleText(meta.label)
                                addText(meta.value)
                            }
                        }

                        if (it.metaData.size == 1) {
                            cell {
                                addTitleText(it.paymentData.label.split(" ")
                                        .joinToString(separator = " ") { it.capitalize() })
                                addText(it.paymentData.value)
                            }
                        }

                        primaryAction = SliceAction.create(buildIntentFromApplink(context, String.format("%s/%s", ApplinkConst.FLIGHT_ORDER, it.id)),
                                IconCompat.createWithResource(context, R.drawable.abc_tab_indicator_material),
                                ListBuilder.ICON_IMAGE, "")
                    }
                }

                row {
                    title = context.getString(R.string.slice_flight_see_more)

                    primaryAction = SliceAction.create(buildIntentFromApplink(context, ApplinkConst.FLIGHT_ORDER),
                            IconCompat.createWithResource(context, R.drawable.abc_tab_indicator_material),
                            ListBuilder.ICON_IMAGE, "")
                }
            }

    private fun String.getBitmap(context: Context): Bitmap? = Glide.with(context).asBitmap().load(this).submit().get()

    private fun buildIntentFromApplink(context: Context, applink: String): PendingIntent = allowReads {
        PendingIntent.getActivity(context, 0,
                allowReads { RouteManager.getIntent(context, applink) },
                0)
    }

}