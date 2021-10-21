package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant.Calendar
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_CALENDAR_DATE_FORMAT
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

class CalendarWidgetItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var calendarWidgetItemViewModel: CalendarWidgetItemViewModel
    private var calendarCardUnify: CardUnify = itemView.findViewById(R.id.calendar_card_unify)
    private var mNotifyCampaignId = 0

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        calendarWidgetItemViewModel = discoveryBaseViewModel as CalendarWidgetItemViewModel
        getSubComponent().inject(calendarWidgetItemViewModel)

        calendarWidgetItemViewModel.components.let {
            setView(it.properties?.calendarLayout)
            it.data?.firstOrNull()?.apply {
                setUpCalendar(this)
            }
        }
    }

    private fun setView(calendarLayout: String?) {
        when (calendarLayout) {
            Calendar.SINGLE -> {
                calendarCardUnify.removeAllViews()
                calendarCardUnify.addView(
                    LayoutInflater.from(itemView.context).inflate(
                        R.layout.discovery_calendar_single_layout_item,
                        calendarCardUnify,
                        false
                    )
                )
            }
            else -> {
                calendarCardUnify.removeAllViews()
                calendarCardUnify.addView(
                    LayoutInflater.from(itemView.context).inflate(
                        R.layout.discovery_calendar_multiple_layout_item,
                        calendarCardUnify,
                        false
                    )
                )
                setLayoutWidth(calendarLayout)
            }
        }
    }

    private fun setLayoutWidth(calendarLayout: String?) {
        val calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
        val width = Resources.getSystem().displayMetrics.widthPixels
        val layoutParams = calendarCardUnify.layoutParams
        val imageLayoutParams = calendarImage.layoutParams
        when (calendarLayout) {
            Calendar.CAROUSEL -> {
                layoutParams.width = (width / 2.5).roundToInt()
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_250)
            }
            Calendar.DOUBLE, Calendar.GRID -> {
                layoutParams.width = (width / 2)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_280)
            }
            Calendar.TRIPLE -> {
                layoutParams.width = (width / 3)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_220)
            }
        }
        imageLayoutParams.width =
            layoutParams.width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        imageLayoutParams.height = (layoutParams.height / 2.2).roundToInt()
        calendarCardUnify.layoutParams = layoutParams
        calendarImage.layoutParams = imageLayoutParams
    }

    private fun setUpCalendar(dataItem: DataItem) {
        val calendarParent: ConstraintLayout = itemView.findViewById(R.id.calendar_parent)
        val calendarExpiredAlpha: View = itemView.findViewById(R.id.calendar_expired_alpha)
        val calendarDateAlpha: View = itemView.findViewById(R.id.calendar_date_alpha)
        val calendarDate: Typography = itemView.findViewById(R.id.calendar_date)
        val calendarTitleImage: ImageUnify = itemView.findViewById(R.id.calendar_title_image)
        val calendarTitle: Typography = itemView.findViewById(R.id.calendar_title)
        val calendarDesc: Typography = itemView.findViewById(R.id.calendar_desc)
        val calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
        val calendarButton: UnifyButton = itemView.findViewById(R.id.calendar_button)
        dataItem.apply {
            calendarImage.loadImage(imageUrl)
            if (!titleLogoUrl.isNullOrEmpty()) {
                calendarTitle.hide()
                calendarTitleImage.show()
                calendarTitleImage.loadImage(titleLogoUrl)
            } else {
                calendarTitle.show()
                calendarTitle.text = title
                calendarTitleImage.hide()
            }
            if (Utils.isSaleOver(endDate ?: "", TIMER_CALENDAR_DATE_FORMAT)) {
                calendarButton.show()
                calendarExpiredAlpha.show()
                calendarButton.isEnabled = false
                calendarButton.text =
                    itemView.context.getString(R.string.discovery_button_event_expired)
                calendarButton.buttonType = UnifyButton.Type.ALTERNATE
                if (!boxColor.isNullOrEmpty()) {
                    setColouredBackground(boxColor)
                }
            } else {
                calendarButton.show()
                calendarExpiredAlpha.hide()
                calendarButton.isEnabled = true
                if (!boxColor.isNullOrEmpty()) {
                    setColouredBackground(boxColor)
                } else {
                    calendarParent.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.Unify_N0))
                    calendarTitle.setTextColor(MethodChecker.getColor(itemView.context, R.color.Unify_N700))
                    calendarDesc.setTextColor(MethodChecker.getColor(itemView.context, R.color.Unify_N700))
                    calendarDateAlpha.gone()
                    calendarDate.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.discovery2_dms_clr_2F89FC))
                }
                if (Utils.isFutureSaleOngoing(startDate ?: "", endDate ?: "")) {
                    calendarButton.text =
                        itemView.context.getString(R.string.discovery_button_event_ongoing)
                    calendarButton.buttonType = UnifyButton.Type.MAIN
                    calendarButton.setOnClickListener {
                        RouteManager.route(itemView.context, buttonApplink)
                    }
                } else {
                    calendarButton.text = itemView.context.getString(R.string.discovery_button_event_reminder)
                    calendarButton.buttonType = UnifyButton.Type.ALTERNATE
                    calendarWidgetItemViewModel.checkUserPushStatus(notifyCampaignId)
                    mNotifyCampaignId = notifyCampaignId
                }
            }
            calendarDate.text = textDate
            calendarDesc.text = description
        }
    }

    private fun setColouredBackground(boxColor: String) {
        itemView.findViewById<View>(R.id.calendar_date_alpha).show()
        itemView.findViewById<ConstraintLayout>(R.id.calendar_parent).setBackgroundColor(Color.parseColor(boxColor))
        itemView.findViewById<Typography>(R.id.calendar_title).setTextColor(MethodChecker.getColor(
            itemView.context,
            R.color.Unify_N0))
        itemView.findViewById<Typography>(R.id.calendar_desc).setTextColor(MethodChecker.getColor(
            itemView.context,
            R.color.Unify_N0))
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetItemViewModel.getSyncPageLiveData().observe(it, { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
            calendarWidgetItemViewModel.getShowLoginData().observe(fragment.viewLifecycleOwner, {
                if (it) itemView.context.startActivity(
                    RouteManager.getIntent(
                        itemView.context,
                        ApplinkConst.LOGIN
                    )
                )
            })
            calendarWidgetItemViewModel.getPushBannerSubscriptionData().observe(fragment.viewLifecycleOwner, {
                updateButton(it)
            })
            calendarWidgetItemViewModel.getPushBannerStatusData().observe(fragment.viewLifecycleOwner, {
                updateButton(it.first)
                if (it.second.isNotEmpty()) {
                    Toaster.build(
                        itemView, it.second, Toast.LENGTH_SHORT,
                        Toaster.TYPE_NORMAL,
                        itemView.context.getString(R.string.discovery_calendar_push_action)
                    ).show()
                } else {
                    Toaster.build(
                        itemView,
                        itemView.context.getString(R.string.discovery_calendar_push_error),
                        Toast.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        itemView.context.getString(R.string.discovery_calendar_push_action)
                    ).show()
                }
            })
        }
    }

    private fun updateButton(isSubscribed: Boolean) {
        val button = itemView.findViewById<UnifyButton>(R.id.calendar_button)
        val tickButton = itemView.findViewById<UnifyButton>(R.id.calendar_button_tick)
        tickButton.setDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.unify_check_ic))
        if (isSubscribed) {
            button.gone()
            tickButton.show()
            tickButton.text = itemView.context.getString(R.string.discovery_button_event_active)
        } else {
            tickButton.gone()
            button.show()
            button.text = itemView.context.getString(R.string.discovery_button_event_reminder)
        }
        button.setOnClickListener {
            setOnClick(isSubscribed)
        }
        tickButton.setOnClickListener {
            setOnClick(isSubscribed)
        }
    }

    private fun setOnClick(subscribed: Boolean) {
        if (subscribed)
            calendarWidgetItemViewModel.unSubscribeUserForPushNotification(mNotifyCampaignId)
        else
            calendarWidgetItemViewModel.subscribeUserForPushNotification(mNotifyCampaignId)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            calendarWidgetItemViewModel.getSyncPageLiveData().removeObservers(it)
            calendarWidgetItemViewModel.getShowLoginData().removeObservers(it)
            calendarWidgetItemViewModel.getPushBannerSubscriptionData().removeObservers(it)
            calendarWidgetItemViewModel.getPushBannerStatusData().removeObservers(it)
        }
    }
}