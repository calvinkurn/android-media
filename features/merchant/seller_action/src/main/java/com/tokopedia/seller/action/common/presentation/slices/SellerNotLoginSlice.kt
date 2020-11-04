package com.tokopedia.seller.action.common.presentation.slices

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.utils.SellerActionUtils.getBitmap
import com.tokopedia.seller.action.order.const.SellerActionOrderIconUrl

class SellerNotLoginSlice(context: Context,
                          sliceUri: Uri,
                          private val titleText: String? = null): SellerSlice(context, sliceUri) {

    companion object {
        private const val REQUEST_CODE = 9231
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = titleText.orEmpty()
                }
                row {
                    setTitleItem(IconCompat.createWithBitmap(SellerActionOrderIconUrl.NotLogin.getBitmap(context)), ListBuilder.SMALL_IMAGE)
                    title = context.getString(R.string.seller_action_not_login_title)
                    subtitle = context.getString(R.string.seller_action_order_not_login_desc_short)
                    primaryAction = createLoginAction()
                }
                setIsError(true)
            }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createLoginAction(): SliceAction {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.SEAMLESS_LOGIN)
        return SliceAction.create(
                PendingIntent.getActivity(context, REQUEST_CODE, intent, 0),
                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                ListBuilder.SMALL_IMAGE,
                context.getString(R.string.seller_action_open_app)
        )
    }
}