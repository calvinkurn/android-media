package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant.Calendar
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.R.id.calendar_card_unify
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
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

const val CAROUSEL_WIDTH_RATIO = 2.5
const val CAROUSEL_HEIGHT_RATIO = 2.2
const val DOUBLE_ASPECT_RATIO_NUM = 16
const val DOUBLE_ASPECT_RATIO_DENOM = 19
const val TRIPLE_WIDTH_RATIO = 3

class CalendarWidgetItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var calendarWidgetItemViewModel: CalendarWidgetItemViewModel? = null
    private var calendarCardUnify: CardUnify = itemView.findViewById(calendar_card_unify)
    private var mNotifyCampaignId = ""

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        calendarWidgetItemViewModel = discoveryBaseViewModel as CalendarWidgetItemViewModel
        calendarWidgetItemViewModel?.let {
            getSubComponent().inject(it)
        }

        calendarWidgetItemViewModel?.components.let {
            setView(it?.properties, it?.data?.firstOrNull())
            it?.data?.firstOrNull()?.apply {
                setUpCalendar(it, this)
            }
        }
    }

    private fun setUpCalendar(componentsItem: ComponentsItem, dataItem: DataItem) {
        if ((
            componentsItem.properties?.calendarLayout.equals(Calendar.CAROUSEL) ||
                componentsItem.properties?.calendarLayout.equals(Calendar.GRID)
            ) && componentsItem.properties?.calendarType.equals(Calendar.DYNAMIC)
        ) {
            setUpCalendarForImageBanner(dataItem)
        } else {
            setUpCalendarForMultipleView(dataItem)
        }
    }

    private fun setView(properties: Properties?, dataItem: DataItem?) {
        when (properties?.calendarLayout) {
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
            Calendar.GRID, Calendar.CAROUSEL -> {
                if (properties.calendarType == Calendar.DYNAMIC) {
                    calendarCardUnify.removeAllViews()
                    calendarCardUnify.addView(
                        LayoutInflater.from(itemView.context).inflate(
                            R.layout.discovery_calendar_carousel_grid_layout_item,
                            calendarCardUnify,
                            false
                        )
                    )
                    setGridAndCarouselLayoutWidth(properties, dataItem)
                } else {
                    setMultipleLayoutView(properties, dataItem)
                }
            }
            else -> {
                setMultipleLayoutView(properties, dataItem)
            }
        }
    }

    private fun setGridAndCarouselLayoutWidth(properties: Properties, dataItem: DataItem?) {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val layoutParams = calendarCardUnify.layoutParams
        when (properties.calendarLayout) {
            Calendar.CAROUSEL -> {
                layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.calendar_banner_widget_width)
                layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.calendar_banner_widget_height)
            }
            Calendar.GRID -> {
                layoutParams.width = ((width) / 2)
                layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_300)
            }
        }
        if (dataItem?.widgetHomeBanner.isNullOrEmpty()) {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        calendarCardUnify.layoutParams = layoutParams
    }

    private fun setMultipleLayoutView(properties: Properties?, dataItem: DataItem?) {
        calendarCardUnify.removeAllViews()
        calendarCardUnify.addView(
            LayoutInflater.from(itemView.context).inflate(
                R.layout.discovery_calendar_multiple_layout_item,
                calendarCardUnify,
                false
            )
        )
        setLayoutWidth(properties, dataItem)
    }

    private fun setLayoutWidth(properties: Properties?, dataItem: DataItem?) {
        val calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
        val width = Resources.getSystem().displayMetrics.widthPixels
        val layoutParams = calendarCardUnify.layoutParams
        val imageLayoutParams = calendarImage.layoutParams
        when (properties?.calendarLayout) {
            Calendar.CAROUSEL -> {
                layoutParams.width = (width / CAROUSEL_WIDTH_RATIO).roundToInt()
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_250)
                imageLayoutParams.height = (layoutParams.height / CAROUSEL_HEIGHT_RATIO).roundToInt()
                imageLayoutParams.width = imageLayoutParams.height
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_6)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_3)
            }
            Calendar.DOUBLE, Calendar.GRID -> {
                if (properties?.calendarLayout == Calendar.GRID) {
                    layoutParams.width =
                        ((width) / 2)
                    imageLayoutParams.width =
                        layoutParams.width - itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)
                } else {
                    layoutParams.width =
                        ((width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)) / 2)
                    imageLayoutParams.width =
                        layoutParams.width - itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)
                }
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_280)
                imageLayoutParams.height = ((imageLayoutParams.width * DOUBLE_ASPECT_RATIO_NUM) / DOUBLE_ASPECT_RATIO_DENOM)
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_4)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_2)
            }
            Calendar.TRIPLE -> {
                layoutParams.width = ((width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)) / TRIPLE_WIDTH_RATIO)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_250)
                imageLayoutParams.width =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_96)
                imageLayoutParams.height = imageLayoutParams.width
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_6)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_3)
            }
        }
        if (dataItem?.imageUrl.isNullOrEmpty()) {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        if (dataItem?.maxHeight != 0 && properties?.calendarType == Calendar.DYNAMIC || (properties?.calendarType == Calendar.STATIC && properties.calendarLayout == Calendar.CAROUSEL)) {
            layoutParams.height = dataItem?.maxHeight ?: 0
        }
        calendarCardUnify.layoutParams = layoutParams
    }

    private fun setUpCalendarForImageBanner(dataItem: DataItem) {
        val widgetHomeBannerImage: ImageUnify = itemView.findViewById(R.id.widget_home_banner)
        dataItem.apply {
            if (widgetHomeBanner.isNullOrEmpty()) {
                widgetHomeBannerImage.gone()
            } else {
                widgetHomeBannerImage.show()
                widgetHomeBannerImage.loadImage(widgetHomeBanner)
            }
            calendarCardUnify.setOnClickListener {
                if (!cta.isNullOrEmpty()) {
                    if (!Utils.isSaleOver(endDate ?: "", TIMER_DATE_FORMAT) && !Utils.isFutureSale(
                            startDate ?: "",
                            TIMER_DATE_FORMAT
                        )
                    ) {
                        RouteManager.route(itemView.context, cta)
                        calendarWidgetItemViewModel?.let { calendarWidgetItemViewModel ->
                            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                                .trackEventClickCalendarWidget(calendarWidgetItemViewModel.components, calendarWidgetItemViewModel.getUserId())
                        }
                    }
                }
            }
        }
    }

    private fun setUpCalendarForMultipleView(dataItem: DataItem) {
        val calendarParent: ConstraintLayout = itemView.findViewById(R.id.calendar_parent)
        val calendarExpiredAlpha: View = itemView.findViewById(R.id.calendar_expired_alpha)
        val calendarDateAlpha: View = itemView.findViewById(R.id.calendar_date_alpha)
        val calendarDate: Typography = itemView.findViewById(R.id.calendar_date)
        val calendarTitleImage: ImageUnify = itemView.findViewById(R.id.calendar_title_image)
        val calendarTitle: Typography = itemView.findViewById(R.id.calendar_title)
        val calendarDesc: Typography = itemView.findViewById(R.id.calendar_desc)
        val calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
        val calendarButton: UnifyButton = itemView.findViewById(R.id.calendar_button)
        val calendarButtonParent: CardView = itemView.findViewById(R.id.calendar_button_parent)
        dataItem.apply {
            if (imageUrl.isNullOrEmpty()) {
                calendarImage.gone()
            } else {
                calendarImage.show()
                calendarImage.loadImage(imageUrl)
            }
            if (!titleLogoUrl.isNullOrEmpty()) {
                calendarTitle.hide()
                calendarTitleImage.show()
                calendarTitleImage.loadImage(titleLogoUrl)
            } else {
                calendarTitle.show()
                calendarTitle.text = title
                calendarTitleImage.hide()
            }
            calendarCardUnify.setOnClickListener {
                if (!cta.isNullOrEmpty()) {
                    if (!Utils.isSaleOver(endDate ?: "", TIMER_DATE_FORMAT) && !Utils.isFutureSale(startDate ?: "", TIMER_DATE_FORMAT)) {
                        RouteManager.route(itemView.context, cta)
                        calendarWidgetItemViewModel?.let { calendarWidgetItemViewModel ->
                            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                                .trackEventClickCalendarWidget(
                                    calendarWidgetItemViewModel.components,
                                    calendarWidgetItemViewModel.getUserId()
                                )
                        }
                    }
                }
            }
            if (Utils.isSaleOver(endDate ?: "", TIMER_DATE_FORMAT)) {
                calendarButton.show()
                calendarExpiredAlpha.show()
                calendarButton.isEnabled = false
                calendarButton.text =
                    itemView.context.getString(R.string.discovery_button_event_expired)
                calendarButton.buttonType = UnifyButton.Type.ALTERNATE
            } else {
                calendarButton.show()
                calendarExpiredAlpha.hide()
                calendarButton.isEnabled = true
                if (!boxColor.isNullOrEmpty()) {
                    setColouredBackground(boxColor)
                } else {
                    calendarParent.setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                    calendarTitle.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                    calendarDesc.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                    calendarDateAlpha.gone()
                    calendarDate.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.discovery2_dms_clr_2F89FC))
                }
                if (Utils.isFutureSaleOngoing(startDate ?: "", endDate ?: "", TIMER_DATE_FORMAT)) {
                    calendarButton.text =
                        itemView.context.getString(R.string.discovery_button_event_ongoing)
                    calendarButton.buttonType = UnifyButton.Type.MAIN
                    calendarButton.setOnClickListener {
                        if (!Utils.isSaleOver(endDate ?: "", TIMER_DATE_FORMAT)) {
                            RouteManager.route(itemView.context, buttonApplink)
                            calendarWidgetItemViewModel?.let { calendarWidgetItemViewModel ->
                                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                                    .trackEventClickCalendarCTA(
                                        calendarWidgetItemViewModel.components,
                                        calendarWidgetItemViewModel.getUserId()
                                    )
                            }
                        }
                    }
                } else {
                    calendarButton.text = itemView.context.getString(R.string.discovery_button_event_reminder)
                    calendarButton.buttonType = UnifyButton.Type.ALTERNATE
                    if (calendarWidgetItemViewModel?.isUserLoggedIn() == true) {
                        calendarWidgetItemViewModel?.checkUserPushStatus(notifyCampaignId)
                    } else {
                        calendarButton.setOnClickListener {
                            (fragment as DiscoveryFragment).openLoginScreen()
                        }
                    }
                    mNotifyCampaignId = notifyCampaignId
                }
            }
            if (buttonApplink.isNullOrEmpty() && !Utils.isFutureSale(startDate ?: "", TIMER_DATE_FORMAT)) {
                calendarButtonParent.hide()
            } else {
                calendarButtonParent.show()
            }
            calendarDate.text = textDate
            calendarDesc.text = if (description.isNullOrEmpty()) itemView.context.getString(R.string.discovery_calendar_line) else description
        }
    }

    private fun setColouredBackground(boxColor: String) {
        calendarWidgetItemViewModel?.components?.properties?.calendarType?.let {
            if (it == Calendar.DYNAMIC) {
                itemView.findViewById<View>(R.id.calendar_date_alpha).show()
                itemView.findViewById<ConstraintLayout>(R.id.calendar_parent)
                    .setBackgroundColor(Color.parseColor(Utils.getValidHexCode(itemView.context, boxColor)))
                itemView.findViewById<Typography>(R.id.calendar_title)
                    .setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                itemView.findViewById<Typography>(R.id.calendar_date)
                    .setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                itemView.findViewById<Typography>(R.id.calendar_desc)
                    .setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            } else {
                itemView.findViewById<View>(R.id.calendar_date_alpha).hide()
                itemView.findViewById<ConstraintLayout>(R.id.calendar_parent)
                    .setBackgroundColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                itemView.findViewById<Typography>(R.id.calendar_title)
                    .setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                itemView.findViewById<Typography>(R.id.calendar_desc)
                    .setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                itemView.findViewById<Typography>(R.id.calendar_date)
                    .setBackgroundColor(Color.parseColor(Utils.getValidHexCode(itemView.context, boxColor)))
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetItemViewModel?.getSyncPageLiveData()?.observe(it) { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            }
            calendarWidgetItemViewModel?.getPushBannerSubscriptionData()?.observe(fragment.viewLifecycleOwner) {
                updateButton(it)
            }
            calendarWidgetItemViewModel?.getShowErrorToastData()?.observe(fragment.viewLifecycleOwner) {
                Toaster.build(
                    itemView,
                    it,
                    Toast.LENGTH_SHORT,
                    Toaster.TYPE_ERROR,
                    itemView.context.getString(R.string.discovery_calendar_push_action)
                ).show()
            }
            calendarWidgetItemViewModel?.getPushBannerStatusData()?.observe(fragment.viewLifecycleOwner) {
                updateButton(it.first)
                if (it.second.isNotEmpty()) {
                    Toaster.build(
                        itemView,
                        it.second,
                        Toast.LENGTH_SHORT,
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
            }
        }
    }

    private fun updateButton(isSubscribed: Boolean) {
        val button = itemView.findViewById<UnifyButton>(R.id.calendar_button)
        val tickButton = itemView.findViewById<UnifyButton>(R.id.calendar_button_tick)
        tickButton.setDrawable(MethodChecker.getDrawable(itemView.context, com.tokopedia.unifycomponents.R.drawable.unify_check_ic))
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
    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        calendarWidgetItemViewModel?.let { calendarWidgetItemViewModel ->
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .viewCalendarsList(
                    calendarWidgetItemViewModel.components,
                    calendarWidgetItemViewModel.getUserId()
                )
        }
    }
    private fun setOnClick(subscribed: Boolean) {
        calendarWidgetItemViewModel?.let { calendarWidgetItemViewModel ->
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackEventClickCalendarCTA(
                    calendarWidgetItemViewModel.components,
                    calendarWidgetItemViewModel.getUserId()
                )
        }
        if (subscribed) {
            calendarWidgetItemViewModel?.unSubscribeUserForPushNotification(mNotifyCampaignId)
        } else {
            calendarWidgetItemViewModel?.subscribeUserForPushNotification(mNotifyCampaignId)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            calendarWidgetItemViewModel?.getSyncPageLiveData()?.removeObservers(it)
            calendarWidgetItemViewModel?.getPushBannerSubscriptionData()?.removeObservers(it)
            calendarWidgetItemViewModel?.getPushBannerStatusData()?.removeObservers(it)
        }
    }
}
