package com.tokopedia.vouchercreation.product.create.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation

class BroadcastCouponBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"
        private const val BUNDLE_KEY_FREE_BROADCAST_QUOTA = "remaining-free-broadcast"
        private const val TPD_VOUCHER_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val ZERO: Long = 0

        fun newInstance(coupon: CouponInformation, freeBroadcastQuota: Int): BroadcastCouponBottomSheet {
            val args = Bundle()
            args.putInt(BUNDLE_KEY_FREE_BROADCAST_QUOTA, freeBroadcastQuota)
            args.putSerializable(BUNDLE_KEY_COUPON, coupon)

            val fragment = BroadcastCouponBottomSheet()
            fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            fragment.arguments = args

            return fragment
        }
    }

    private var onBroadCastClickAction: (CouponInformation) -> Unit = {}
    private var onShareToSocialMediaAction: (CouponInformation) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet(container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun initBottomSheet(container: ViewGroup?) {
        val child = LayoutInflater.from(context)
            .inflate(R.layout.bottomsheet_broadcast_coupon, container, false)
        setChild(child)
    }

    private fun setupView(view: View) {
        val coupon = arguments?.getSerializable(BUNDLE_KEY_COUPON) as CouponInformation

        val imgVoucher = view.findViewById<ImageView>(R.id.imgVoucher)
        imgVoucher?.loadImage(TPD_VOUCHER_IMAGE_URL)

        val layoutBroadcastCoupon: ConstraintLayout = view.findViewById(R.id.layoutBroadcastCoupon)
        layoutBroadcastCoupon.setOnClickListener { onBroadCastClickAction(coupon) }

        handleShareToSocialVisibility(view, coupon)
        handleFreeBroadcastLabelVisibility(view)
        displayCouponPeriod(view, coupon)
    }

    private fun handleShareToSocialVisibility(view: View, coupon: CouponInformation) {
        val layoutShareToSocialMedia: ConstraintLayout =
            view.findViewById(R.id.layoutShareToSocialMedia)
        layoutShareToSocialMedia.setOnClickListener { onShareToSocialMediaAction(coupon) }

        if (coupon.target == CouponInformation.Target.SPECIAL) {
            layoutShareToSocialMedia.visible()
        } else {
            layoutShareToSocialMedia.gone()
        }

    }

    private fun handleFreeBroadcastLabelVisibility(view: View) {
        val freeBroadcastQuota = arguments?.getInt(BUNDLE_KEY_FREE_BROADCAST_QUOTA).orZero()
        val freeTextView: TextView = view.findViewById(R.id.tgp_free)
        if (freeBroadcastQuota > ZERO) {
            freeTextView.visible()
        } else {
            freeTextView.gone()
        }
    }

    private fun displayCouponPeriod(view: View, coupon: CouponInformation) {
        val voucherPeriodInfoView: Typography = view.findViewById(R.id.tgp_voucher_period_info)

        val startDate = coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val period = String.format(
            getString(R.string.placeholder_scheduled_coupon_period),
            coupon.name,
            startDate,
            startHour,
            endDate,
            endHour
        )

        voucherPeriodInfoView.text = period
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

    fun setOnBroadCastClickListener(action: (CouponInformation) -> Unit) {
        onBroadCastClickAction = action
    }

    fun setOnShareToSocialMediaClickListener(action: (CouponInformation) -> Unit) {
        onShareToSocialMediaAction = action
    }
}