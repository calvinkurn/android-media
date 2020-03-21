package com.tokopedia.notifcenter.presentation.fragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.presentation.BaseBottomSheetDialog
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductStockReminderDialog(
        private val context: Context,
        fragmentManager: FragmentManager
): BaseBottomSheetDialog<NotificationItemViewBean>(context, fragmentManager) {

    private val txtTitle = container?.findViewById<Typography>(R.id.txt_title)
    private val txtDescription = container?.findViewById<Typography>(R.id.txt_description)

    private val thumbnail = container?.findViewById<ImageView>(R.id.iv_thumbnail)
    private val productName = container?.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = container?.findViewById<TextView>(R.id.tv_product_price)
    private val productCampaign = container?.findViewById<CampaignRedView>(R.id.cl_campaign)
    private val campaignTag = container?.findViewById<ImageView>(R.id.img_campaign)
    private val btnReminder = container?.findViewById<UnifyButton>(R.id.btn_reminder)

    override fun resourceId(): Int {
        return R.layout.dialog_product_stock_handler
    }

    override fun show(element: NotificationItemViewBean) {
        txtTitle?.text = element.title
        txtDescription?.text = element.body

        element.getAtcProduct()?.let {
            ImageHandler.loadImage2(
                    thumbnail,
                    it.imageUrl,
                    R.drawable.ic_notifcenter_loading_toped
            )

            productName?.text = it.name
            productPrice?.text = it.priceFormat
            productCampaign?.setCampaign(it.campaign)

            if (it.shop?.freeShippingIcon != null &&
                it.shop.freeShippingIcon.isNotEmpty()) {
                campaignTag?.loadImage(it.shop.freeShippingIcon)
                campaignTag?.show()
            }
        }

        btnReminder?.setOnClickListener {
            showToast()
        }
    }

    private fun showToast() {
        container?.let {
            Toaster.make(
                    it,
                    context.getString(R.string.product_reminder_success),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context.getString(R.string.notifcenter_btn_title_ok),
                    View.OnClickListener {  }
            )
        }
    }

}