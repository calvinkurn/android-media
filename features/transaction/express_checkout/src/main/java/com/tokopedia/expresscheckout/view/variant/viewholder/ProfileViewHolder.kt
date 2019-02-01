package com.tokopedia.expresscheckout.view.variant.viewholder

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.item_profile_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ProfileViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<ProfileViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_profile_detail_product_page

        val ANIM_ALPHA_START = 0.0F
        val ANIM_ALPHA_STOP = 1.0F
        val ANIM_DURATION = 1000L
        val ANIM_START_OFFSET = 100L
        val ANIM_REPEAT_COUNT = 3
    }

    override fun bind(element: ProfileViewModel?) {
        if (element != null) {
            if (element.isStateHasRemovedProfile) {
                itemView.ll_profile_container.visibility = View.GONE
                itemView.ll_no_profile_container.visibility = View.VISIBLE
                itemView.tv_change_no_profile.setOnClickListener { listener.onClickEditProfile() }
            } else {
                itemView.ll_no_profile_container.visibility = View.GONE
                itemView.ll_profile_container.visibility = View.VISIBLE

                itemView.tv_profile_address_name.text = getHtmlFormat("<b>${element.addressTitle}</b> ${element.addressDetail}")

                ImageHandler.loadImageRounded2(itemView.context, itemView.img_profile_payment_method, element.paymentOptionImageUrl)
                itemView.tv_profile_payment_detail.text = element.paymentDetail
                itemView.tv_change_profile.setOnClickListener { listener.onClickEditProfile() }
                itemView.img_bt_profile_show_more_shipping_duration.setOnClickListener { listener.onClickEditDuration() }
                if (element.isDurationError) {
                    itemView.ll_profile_courier.visibility = View.GONE
                    itemView.tv_profile_shipping_duration_error.text = element.durationErrorMessage
                    itemView.tv_profile_shipping_duration_error.visibility = View.VISIBLE
                    itemView.tv_profile_shipping_courier_error.visibility = View.GONE
                    itemView.img_bt_profile_show_more_shipping_courier.setOnClickListener { }
                } else if (element.isCourierError) {
                    itemView.tv_profile_shipping_duration_value.text = getHtmlFormat("Durasi <b>${element.shippingDuration}</b>")
                    itemView.tv_profile_shipping_duration_error.visibility = View.GONE
                    itemView.tv_profile_shipping_courier_error.text = element.courierErrorMessage
                    itemView.tv_profile_shipping_courier_error.visibility = View.VISIBLE
                    itemView.img_bt_profile_show_more_shipping_courier.setOnClickListener { listener.onClickEditCourier() }
                } else {
                    itemView.tv_profile_shipping_duration_value.text = getHtmlFormat("Durasi <b>${element.shippingDuration}</b>")
                    itemView.tv_profile_shipping_duration_error.visibility = View.GONE
                    itemView.tv_profile_shipping_courier_error.visibility = View.GONE
                    if (element.shippingCourier.isNotEmpty()) {
                        itemView.ll_profile_courier.visibility = View.VISIBLE
                        itemView.img_bt_profile_show_more_shipping_courier.setOnClickListener { listener.onClickEditCourier() }
                        itemView.tv_profile_shipping_courier.text = getHtmlFormat("Kurir <b>${element.shippingCourier}</b>")
                    } else {
                        itemView.ll_profile_courier.visibility = View.GONE
                    }
                }
                listener.onNeedToValidateButtonBuyVisibility()

                if (element.isShowDefaultProfileCheckBox) {
                    itemView.v_profile_separator_bottom.visibility = View.VISIBLE
                    itemView.ll_profile_default_checkbox_container.visibility = View.VISIBLE
                    itemView.cb_profile_set_default.setOnCheckedChangeListener { buttonView, isChecked -> element.isDefaultProfileCheckboxChecked = isChecked }
                } else {
                    itemView.v_profile_separator_bottom.visibility = View.GONE
                    itemView.ll_profile_default_checkbox_container.visibility = View.GONE
                    itemView.cb_profile_set_default.setOnClickListener { }
                }
            }

            if (element.isFirstTimeShowProfile) {
                element.isFirstTimeShowProfile = false
                val anim = AlphaAnimation(ANIM_ALPHA_START, ANIM_ALPHA_STOP)
                anim.duration = ANIM_DURATION
                anim.repeatMode = Animation.REVERSE
                anim.startOffset = ANIM_START_OFFSET
                anim.repeatCount = ANIM_REPEAT_COUNT
                itemView.startAnimation(anim)
                listener.onNeedToUpdateOnboardingStatus()
            }

            if (element.isStateHasChangedProfile) {
                listener.onNeedToRecalculateRatesAfterChangeTemplate()
            }
        }
    }

    private fun getHtmlFormat(text: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(text)
        }
    }

}