package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

class CalendarWidgetItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var calendarWidgetItemViewModel: CalendarWidgetItemViewModel
    private var calendarCardUnify: CardUnify = itemView.findViewById(R.id.calendar_card_unify)

    companion object {
        const val BLACK = "#000000"
    }

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
                calendarCardUnify.addView(LayoutInflater.from(itemView.context).inflate(R.layout.discovery_calendar_single_layout_item, calendarCardUnify, false))
            }
            else -> {
                calendarCardUnify.removeAllViews()
                calendarCardUnify.addView(LayoutInflater.from(itemView.context).inflate(R.layout.discovery_calendar_multiple_layout_item, calendarCardUnify, false))
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
                layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_240)
            }
            Calendar.DOUBLE -> {
                layoutParams.width = (width / 2)
                layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_280)
            }
            Calendar.TRIPLE -> {
                layoutParams.width = (width / 3)
                layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_220)
            }
        }
        imageLayoutParams.width = layoutParams.width - itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)
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
            if(Utils.isSaleOver(endDate?: "", TIMER_CALENDAR_DATE_FORMAT)){
                calendarExpiredAlpha.show()
                calendarButton.isEnabled = false
                calendarButton.text = itemView.context.getString(R.string.discovery_button_expired)
            } else {
                calendarExpiredAlpha.hide()
                calendarButton.isEnabled = true
            }
            calendarDate.text = textDate
            if (!boxColor.isNullOrEmpty() && boxColor != BLACK) {
                calendarDateAlpha.show()
                calendarParent.setBackgroundColor(Color.parseColor(boxColor))
            } else {
                calendarDateAlpha.gone()
                calendarDate.setBackgroundColor(
                    MethodChecker.getColor(
                        itemView.context,
                        R.color.discovery2_dms_clr_2F89FC
                    )
                )
            }
            calendarDesc.text = description
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetItemViewModel.getSyncPageLiveData().observe(it, { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            calendarWidgetItemViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}