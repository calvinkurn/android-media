package com.tokopedia.play.ui.productsheet.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.databinding.ItemContentProductSheetSectionBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 18/08/22
 */
class ProductSheetSectionViewHolder(
    internal val binding: ItemContentProductSheetSectionBinding,
    private val listener: Listener,
) : BaseViewHolder(binding.root) {

    private val bellColor = MethodChecker.getColor(itemView.context, unifyR.color.Unify_GN500)

    fun bind(item: ProductSheetAdapter.Item.Section) {
        binding.tvHeaderTitle.showWithCondition(item.section.config.title.isNotEmpty())
        binding.tvHeaderTitle.text = item.section.config.title

        when (item.section.config.reminder) {
            PlayUpcomingBellStatus.On -> {
                binding.btnSectionReminder.show()
                binding.btnSectionReminder.setImage(
                    newIconId = IconUnify.BELL_FILLED,
                    newDarkEnable = bellColor,
                    newLightEnable = bellColor,
                )
            }
            PlayUpcomingBellStatus.Off ->{
                binding.btnSectionReminder.show()
                binding.btnSectionReminder.setImage(
                    newIconId = IconUnify.BELL,
                    newDarkEnable = bellColor,
                    newLightEnable = bellColor,
                )
            }
            else -> {
                binding.btnSectionReminder.setImageDrawable(null)
                binding.btnSectionReminder.hide()
            }
        }

        binding.tvHeaderInfo.text = item.section.config.timerInfo

        when (item.section.config.type) {
            ProductSectionType.Active, ProductSectionType.Upcoming -> {
                binding.tvHeaderInfo.show()
                binding.sectionTimer.show()
                setupTimer(item.section.config)
                binding.btnInformation.hide()
            }
            ProductSectionType.Other -> {
                binding.tvHeaderInfo.hide()
                binding.sectionTimer.hide()
                binding.btnInformation.hide()
            }
            ProductSectionType.TokoNow -> {
                binding.btnInformation.showWithCondition(item.section.config.title.isNotEmpty())
                binding.tvHeaderInfo.hide()
                binding.sectionTimer.hide()
            }
            ProductSectionType.Unknown -> {
                // todo: handle unknown section
            }
        }

        binding.btnSectionReminder.setOnClickListener {
            listener.onReminderClicked(this, item.section)
        }

        binding.btnInformation.setOnClickListener {
            listener.onInformationClicked(this, item.section)
        }

        binding.root.addImpressionListener(item.section.impressHolder) {
            if (binding.btnSectionReminder.isVisible) {
                listener.onReminderImpressed(this, item.section)
            }

            if (binding.btnInformation.isVisible) {
                listener.onInformationImpressed(this, item.section)
            }
        }
    }

    @Suppress("MagicNumber")
    private fun setupTimer(config : ProductSectionUiModel.Section.ConfigUiModel) {
        binding.sectionTimer.timerVariant = if(config.type == ProductSectionType.Active) {
            TimerUnifySingle.VARIANT_MAIN
        } else TimerUnifySingle.VARIANT_INFORMATIVE

        val targetTime = if (config.type == ProductSectionType.Active) {
            config.endTime
        } else config.startTime

        if (targetTime == null || config.serverTime == null) return

        val dt = DateUtil.getCurrentCalendar().apply {
            time = Date(targetTime.time + (config.controlTime.time - config.serverTime.time))
        }

        binding.sectionTimer.targetDate = dt
    }

    private fun getTimeDiff(serverTime: Date, currentTime: Date): Date {
        val diff = serverTime.time - currentTime.time
        return currentTime.addTimeToSpesificDate(Calendar.MILLISECOND, diff.toInt())
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = ProductSheetSectionViewHolder(
            ItemContentProductSheetSectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
        )
    }

    interface Listener {
        fun onReminderClicked(
            holder: ProductSheetSectionViewHolder,
            section: ProductSectionUiModel.Section,
        )
        fun onReminderImpressed(
            holder: ProductSheetSectionViewHolder,
            section: ProductSectionUiModel.Section,
        )
        fun onInformationClicked(
            holder: ProductSheetSectionViewHolder,
            section: ProductSectionUiModel.Section,
        )
        fun onInformationImpressed(
            holder: ProductSheetSectionViewHolder,
            section: ProductSectionUiModel.Section,
        )
    }
}
