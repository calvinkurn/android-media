package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
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
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.CardUnify.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.CardUnify.Companion.TYPE_CLEAR
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import java.util.Date
import kotlin.math.roundToInt
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR


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
                val layoutParams = calendarCardUnify.layoutParams
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                calendarCardUnify.layoutParams = layoutParams
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
                layoutParams.width =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.calendar_banner_widget_width)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.calendar_banner_widget_height)
            }

            Calendar.GRID -> {
                layoutParams.width =
                    ((width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_24)) / 2)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_300)
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
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_280)
                imageLayoutParams.height =
                    (layoutParams.height / CAROUSEL_HEIGHT_RATIO).roundToInt()
                imageLayoutParams.width = imageLayoutParams.height
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_6)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_3)
            }

            Calendar.DOUBLE, Calendar.GRID -> {
                layoutParams.width =
                    ((width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_24)) / 2)
                imageLayoutParams.width =
                    layoutParams.width - itemView.context.resources.getDimensionPixelSize(
                        unifyprinciplesR.dimen.spacing_lvl8
                    )
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_300)
                imageLayoutParams.height =
                    ((imageLayoutParams.width * DOUBLE_ASPECT_RATIO_NUM) / DOUBLE_ASPECT_RATIO_DENOM)
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_4)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_2)
            }

            Calendar.TRIPLE -> {
                layoutParams.width =
                    ((width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_24)) / TRIPLE_WIDTH_RATIO)
                layoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_250)
                imageLayoutParams.height =
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_96)
                calendarImage.layoutParams = imageLayoutParams
                itemView.findViewById<Typography>(R.id.calendar_title).setType(Typography.HEADING_6)
                itemView.findViewById<Typography>(R.id.calendar_date).setType(Typography.BODY_3)
            }
            // CALENDAR.SINGLE
            else -> {
                layoutParams.width =
                    width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16) * 2
                imageLayoutParams.width = 150.toPx()
                calendarImage.layoutParams = imageLayoutParams
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
                        if (dataItem.moveAction?.type != null) {
                            Utils.routingBasedOnMoveAction(dataItem.moveAction, fragment)
                        } else {
                            RouteManager.route(itemView.context, cta)
                        }
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
        }
    }

    @SuppressLint("ResourcePackage")
    private fun setUpCalendarForMultipleView(dataItem: DataItem) {
        val calendarParent: ConstraintLayout = itemView.findViewById(R.id.calendar_parent)
        val calendarDateAlpha: View = itemView.findViewById(R.id.calendar_date_alpha)
        val calendarDate: Typography = itemView.findViewById(R.id.calendar_date)
        val calendarTitleImage: ImageUnify = itemView.findViewById(R.id.calendar_title_image)
        val calendarTitle: Typography = itemView.findViewById(R.id.calendar_title)
        val calendarDesc: Typography = itemView.findViewById(R.id.calendar_desc)
        val calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
        val calendarButton: UnifyButton = itemView.findViewById(R.id.calendar_button)
        val calendarButtonParent: CardView = itemView.findViewById(R.id.calendar_button_parent)
        val calendarFullImage: ImageUnify = itemView.findViewById(R.id.calendar_full_image)
        dataItem.apply {
            if (isShowCTAButton(this)
            ) {
                calendarButtonParent.show()
            } else {
                calendarButtonParent.hide()
            }
            val useFullImage = !calendarImageUrl.isNullOrEmpty()
            if (useFullImage) {
                calendarCardUnify.cardType = TYPE_CLEAR
                calendarFullImage.show()
                setImageAdjustViewBoundPost(calendarFullImage, calendarImageUrl!!, dataItem)

                //render full image will hide other component
                calendarImage.invisible()
                calendarTitle.hide()
                calendarTitleImage.hide()

                itemView.minimumHeight = 150.toPx()

            } else { // no full image, do logic as usual
                itemView.minimumHeight = 0
                calendarFullImage.gone()
                if (boxColor?.isNotEmpty() == true && itemView.context.isDarkMode().not()) {
                    calendarCardUnify.cardType = TYPE_BORDER
                } else {
                    calendarCardUnify.cardType = TYPE_CLEAR
                }
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
            } // end if no full image
            // if background color is not empty, card border should be clear
            if (Utils.isSaleOver(endDate ?: "", TIMER_DATE_FORMAT)) {
                renderExpiredImageView(true, calendarImage)
                if (useFullImage) {
                    renderExpiredImageView(true, calendarFullImage)
                } else {
                    calendarDateAlpha.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            if (itemView.context.isDarkMode()) {
                                R.color.discovery2_dms_header_expired_dark
                            } else {
                                R.color.discovery2_dms_header_expired
                            }
                        )
                    )
                    calendarParent.apply {
                        setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                if (itemView.context.isDarkMode()) {
                                    R.color.discovery2_dms_body_expired_dark
                                } else {
                                    R.color.discovery2_dms_body_expired
                                }
                            )
                        )
                    }
                }
                calendarTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (itemView.context.isDarkMode()) {
                            R.color.discovery2_dms_copy_header_expired_dark
                        } else {
                            R.color.discovery2_dms_copy_header_expired
                        }
                    )
                )
                calendarDesc.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (itemView.context.isDarkMode()) {
                            R.color.discovery2_dms_copy_body_expired_dark
                        } else {
                            R.color.discovery2_dms_copy_body_expired
                        }
                    )
                )
                calendarButton.show()
                calendarButton.isEnabled = false
                calendarButton.text =
                    calendarWidgetItemViewModel?.getCtaText(itemView.context.getString(R.string.discovery_button_event_expired))
                calendarButton.buttonType = UnifyButton.Type.ALTERNATE
                calendarButton.buttonVariant = UnifyButton.Variant.GHOST
            }  // end if sale over
            else {
                renderExpiredImageView(false, calendarImage)
                if (useFullImage) {
                    renderExpiredImageView(false, calendarFullImage)
                }
                calendarButton.show()
                calendarButton.isEnabled = true
                val isDynamic =
                    calendarWidgetItemViewModel?.components?.properties?.calendarType?.let { it == Calendar.DYNAMIC }
                if (!headerColor.isNullOrEmpty()) {
                    if (isDynamic == true) {
                        calendarDateAlpha.show()
                    } else {
                        calendarDateAlpha.gone()
                    }
                } else {
                    calendarDateAlpha.gone()
                }
                // header bg
                calendarDate.setBackgroundColor(
                    getColorBackendOrDefault(
                        headerColor,
                        unifyprinciplesR.color.Unify_NN0
                    )
                )
                // header text
                calendarDate.setTextColor(
                    getColorBackendOrDefault(
                        textHeaderColor,
                        unifyprinciplesR.color.Unify_NN950
                    )
                )
                // content BG
                calendarParent.setBackgroundColor(
                    getColorBackendOrDefault(
                        boxColor,
                        unifyprinciplesR.color.Unify_NN0
                    )
                )
                // content text
                val contentColor = getColorBackendOrDefault(
                    textContentColor,
                    unifyprinciplesR.color.Unify_NN950
                )
                calendarTitle.setTextColor(contentColor)
                calendarDesc.setTextColor(contentColor)

                if (Utils.isFutureSaleOngoing(
                        startDate ?: "",
                        endDate ?: "",
                        TIMER_DATE_FORMAT
                    )
                ) {
                    calendarButton.text =
                        calendarWidgetItemViewModel?.getCtaText(itemView.context.getString(R.string.discovery_button_event_ongoing))
                    calendarButton.buttonType = UnifyButton.Type.MAIN
                    calendarButton.buttonVariant = UnifyButton.Variant.FILLED
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
                }// end if sale is ongoing
                else {
                    calendarButton.text =
                        calendarWidgetItemViewModel?.getCtaText(itemView.context.getString(R.string.discovery_button_event_reminder))
                    calendarButton.buttonVariant = UnifyButton.Variant.FILLED
                    calendarButton.buttonType = UnifyButton.Type.ALTERNATE
                    if (calendarWidgetItemViewModel?.isUserLoggedIn() == true) {
                        calendarWidgetItemViewModel?.checkUserPushStatus(notifyCampaignId)
                    } else {
                        calendarButton.setOnClickListener {
                            (fragment as DiscoveryFragment).openLoginScreen()
                        }
                    }
                    mNotifyCampaignId = notifyCampaignId
                } // end if sale is not ongoing
            }// end if sale is not over

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
                                .trackEventClickCalendarWidget(
                                    calendarWidgetItemViewModel.components,
                                    calendarWidgetItemViewModel.getUserId()
                                )
                        }
                    }
                }
            }

            if (useFullImage) {
                calendarDate.hide()
                calendarDesc.hide()
            } else {
                calendarDate.show()
                calendarDate.text = textDate

                calendarDesc.text =
                    if (description.isNullOrEmpty()) {
                        itemView.context.getString(R.string.discovery_calendar_line)
                    } else description
                calendarDesc.show()
            }

            when (calendarWidgetItemViewModel?.components?.properties?.calendarLayout) {
                Calendar.TRIPLE -> {
                    calendarDate.setType(Typography.SMALL)
                }

                else -> {
                    calendarDate.setType(Typography.BODY_3)
                }
            }
        }
    }

    private fun getColorBackendOrDefault(backEndColor: String?, defaultColor: Int): Int {
        return if (backEndColor.isNullOrEmpty()) {
            MethodChecker.getColor(
                itemView.context,
                defaultColor
            )
        } else {
            Color.parseColor(
                Utils.getValidHexCode(
                    itemView.context,
                    backEndColor
                )
            )
        }
    }

    private fun setImageAdjustViewBoundPost(
        calendarFullImage: ImageView,
        calendarImageUrl: String,
        dataItem: DataItem?
    ) {
        // we use getBitmap to get width/height in case calendar single to allow adjustViewBound
        calendarCardUnify.addOneTimeGlobalLayoutListener {
            setImageAdjustViewBound(
                calendarFullImage,
                calendarImageUrl,
                dataItem
            )
        }
    }

    private fun setImageAdjustViewBound(
        calendarFullImage: ImageView,
        calendarImageUrl: String,
        dataItem: DataItem?
    ) {
        // we use getBitmap to get width/height in case calendar single to allow adjustViewBound
        calendarImageUrl.getBitmapImageUrl(itemView.context, properties = {
            listener(onSuccess = { bitmap, _ ->
                if (bitmap == null) {
                    // fallback to use string url
                    calendarFullImage.loadImage(calendarImageUrl)
                } else {
                    if (calendarWidgetItemViewModel?.components?.properties?.calendarLayout == Calendar.SINGLE) {
                        val viewWidth = calendarFullImage.measuredWidth
                        val bitmapWidth = bitmap.width
                        if (viewWidth == 0) {
                            calendarFullImage.setImageBitmap(bitmap)
                        } else {
                            val height = viewWidth * bitmap.height / bitmapWidth
                            val newBitmap =
                                Bitmap.createScaledBitmap(bitmap, viewWidth, height, false)
                            calendarFullImage.adjustViewBounds = true
                            calendarFullImage.setImageBitmap(newBitmap)
                        }
                    } else {
                        val maxHeight = dataItem?.maxHeight ?: 0
                        if (maxHeight > 0) {
                            calendarFullImage.adjustViewBounds = false
                            val calendarLayoutParams = calendarFullImage.layoutParams
                            calendarLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                            calendarFullImage.layoutParams = calendarLayoutParams

                            val layoutParams = calendarCardUnify.layoutParams
                            layoutParams.height = maxHeight

                            /* when (calendarWidgetItemViewModel?.components?.properties?.calendarLayout) {
                                Calendar.DOUBLE -> {
                                    calendarFullImage.scaleType = ImageView.ScaleType.FIT_CENTER
                                }

                                else -> {
                                    calendarFullImage.scaleType = ImageView.ScaleType.CENTER_CROP
                                }
                            } */

                            // in case we want to adjust width
                            val bitmapWidth = bitmap.width
                            when (calendarWidgetItemViewModel?.components?.properties?.calendarLayout) {
                                Calendar.CAROUSEL -> {
                                    // scale the layout to keep ratio
                                    layoutParams.width =
                                        (maxHeight * bitmapWidth / bitmap.height) + 8.toPx()
                                }
                            }
                            calendarCardUnify.layoutParams = layoutParams
                            calendarFullImage.setImageBitmap(bitmap)
                        } else {
                            calendarFullImage.adjustViewBounds = true
                            val calendarLayoutParams = calendarFullImage.layoutParams
                            calendarLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            calendarFullImage.layoutParams = calendarLayoutParams

                            calendarFullImage.setImageBitmap(bitmap)
                        }
                    }
                }
            }, onError = {
                calendarFullImage.setImageBitmap(null)
            })
        })
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetItemViewModel?.getSyncPageLiveData()?.observe(it) { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            }
            calendarWidgetItemViewModel?.getPushBannerSubscriptionData()
                ?.observe(fragment.viewLifecycleOwner) {
                    updateButton(it)
                }
            calendarWidgetItemViewModel?.getShowErrorToastData()
                ?.observe(fragment.viewLifecycleOwner) {
                    Toaster.build(
                        itemView,
                        it,
                        Toast.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        itemView.context.getString(R.string.discovery_calendar_push_action)
                    ).show()
                }
            calendarWidgetItemViewModel?.getPushBannerStatusData()
                ?.observe(fragment.viewLifecycleOwner) {
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
        tickButton.setDrawable(
            MethodChecker.getDrawable(
                itemView.context,
                unifycomponentsR.drawable.unify_check_ic
            )
        )
        if (isSubscribed) {
            button.gone()
            tickButton.show()
            tickButton.text =
                calendarWidgetItemViewModel?.getNotifiedCtaText(itemView.context.getString(R.string.discovery_button_event_active))
        } else {
            tickButton.gone()
            button.show()
            button.text =
                calendarWidgetItemViewModel?.getCtaText(itemView.context.getString(R.string.discovery_button_event_reminder))
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

    private fun renderExpiredImageView(isExpired: Boolean, imageView: ImageView) {
        if (isExpired) {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val cf = ColorMatrixColorFilter(matrix)
            imageView.colorFilter = cf
            imageView.imageAlpha = if (itemView.context.isDarkMode()) 255 else 125
        } else {
            imageView.colorFilter = null;
            imageView.imageAlpha = 255;
        }
    }

    private fun isShowCTAButton(dataItem: DataItem?): Boolean {
        val startDate = Utils.parseData(dataItem?.startDate, TIMER_DATE_FORMAT)
        val endDate = Utils.parseData(dataItem?.endDate, TIMER_DATE_FORMAT)
        val currentSystemTime = java.util.Calendar.getInstance().time
        return if (endDate != null && startDate != null) {
            when {
                endDate < currentSystemTime || startDate < currentSystemTime -> true
                else -> false
            }
        } else {
            false
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
