package com.tokopedia.play.ui.productsheet.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.databinding.ItemPlayProductSectionHeaderBinding
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.view.setGradientBackground
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 27/01/22
 */
class ProductSectionViewHolder(
    binding: ItemPlayProductSectionHeaderBinding,
    private val listener: Listener,
) : BaseViewHolder(binding.root) {

    private val tvSectionTitle: TextView = binding.tvHeaderTitle
    private val ivBg: ImageView = binding.ivBg
    private val tvTimerInfo: TextView = binding.tvHeaderInfo
    private val timerSection: TimerUnifySingle = binding.sectionTimer
    private val rvProducts: RecyclerView = binding.rvProduct
    private val btnReminder: IconUnify = binding.btnSectionReminder
    private val btnInfo: IconUnify = binding.btnInformation

    private var timerTime = ""

    private lateinit var adapter: ProductLineAdapter

    private fun setupListener(sectionInfo: ProductSectionUiModel.Section) = object : ProductLineViewHolder.Listener {
        override fun onBuyProduct(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        ) {
            listener.onBuyProduct(product, sectionInfo)
        }

        override fun onAtcProduct(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        ) {
            listener.onATCProduct(product, sectionInfo)
        }

        override fun onProductClicked(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        ) {
            listener.onClickProductCard(product, sectionInfo, viewHolder.adapterPosition)
        }

        override fun onProductImpressed(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section
        ) {

        }
    }

    fun bind(item: ProductSectionUiModel.Section) {
        resetBackground()
        adapter = ProductLineAdapter(setupListener(item))
        rvProducts.adapter = adapter

        tvSectionTitle.shouldShowWithAction(item.config.title.isNotEmpty()){
            tvSectionTitle.text = item.config.title
        }

        val color = MethodChecker.getColor(itemView.context, unifyR.color.Unify_GN500)
        when (item.config.reminder) {
            is PlayUpcomingBellStatus.On -> {
                btnReminder.show()
                btnReminder.setImage(newIconId = IconUnify.BELL_FILLED, newDarkEnable = color, newLightEnable = color)
            }
            is PlayUpcomingBellStatus.Off ->{
                btnReminder.show()
                btnReminder.setImage(newIconId = IconUnify.BELL, newDarkEnable = color, newLightEnable = color)
            }
            else -> btnReminder.hide()
        }
        tvTimerInfo.text = item.config.timerInfo

        when (item.config.type) {
            ProductSectionType.Active, ProductSectionType.Upcoming -> {
                tvTimerInfo.show()
                timerSection.show()
                setupBackground(item.config.background)
                setupTimer(item)
                btnInfo.hide()
            }
            ProductSectionType.Other -> {
                tvTimerInfo.hide()
                timerSection.hide()
                btnInfo.hide()
            }
            ProductSectionType.TokoNow -> {
                btnInfo.showWithCondition(item.config.title.isNotEmpty())
                tvTimerInfo.hide()
                timerSection.hide()
            }
            ProductSectionType.Unknown -> {
                // todo: handle unknown section
            }
        }
        adapter.setItemsAndAnimateChanges(itemList = item.productList)
        if (isProductCountChanged(item.productList.size)) listener.onProductChanged()

        btnReminder.setOnClickListener {
            listener.onReminderClicked(item)
        }

        if(btnReminder.isVisible && item.config.type == ProductSectionType.Upcoming) listener.onReminderImpressed(item)

        btnInfo.setOnClickListener {
            listener.onInformationClicked(item)
        }

        itemView.addOnImpressionListener(item.impressHolder){
            listener.onProductImpressed(sectionInfo = item, product = getFinalProduct(item.productList))
        }

        if(btnInfo.isVisible && item.config.type == ProductSectionType.TokoNow) listener.onInformationImpressed()
    }

    private fun getFinalProduct(productList: List<PlayProductUiModel.Product>): List<Pair<PlayProductUiModel.Product, Int>> =
        productList.mapIndexed { index, product -> Pair(product, index) }

    private fun setupBackground(background: ProductSectionUiModel.Section.BackgroundUiModel) {
        if (background.gradients.isNotEmpty()) {
            itemView.setGradientBackground(background.gradients)
        } else {
            ivBg.loadImage(background.imageUrl)
        }
    }

    private fun setupTimer(item : ProductSectionUiModel.Section) {
        timerTime = if(item.config.type == ProductSectionType.Active) item.config.endTime else item.config.startTime

        timerSection.timerVariant = if(item.config.type == ProductSectionType.Active) TimerUnifySingle.VARIANT_MAIN else TimerUnifySingle.VARIANT_INFORMATIVE

        val convertedServerTime = item.config.serverTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS)
        val convertedTimerTime = timerTime.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS)

        val dt = DateUtil.getCurrentCalendar().apply {
                val diff = convertedTimerTime.time - getTimeDiff(serverTime = convertedServerTime, currentTime = time).time
                add(Calendar.SECOND, ((diff / 1000) % 60).toInt())
                add(Calendar.MINUTE, (((diff / 1000) / 60) % 60).toInt())
                add(Calendar.HOUR, (((diff / 1000) / 60) / 60).toInt())
            }
            timerSection.targetDate = dt
    }

    private fun isProductCountChanged(productSize: Int): Boolean {
        return adapter.getItems().isNotEmpty() &&
                adapter.getItems().first() is PlayProductUiModel.Product &&
                adapter.itemCount != productSize
    }

    private fun getTimeDiff(serverTime: Date, currentTime: Date): Date {
        val diff = serverTime.time - currentTime.time
        return currentTime.addTimeToSpesificDate(Calendar.MILLISECOND, diff.toInt())
    }

    private fun resetBackground(){
        ivBg.setImageDrawable(null)
        itemView.background = null
        btnReminder.setImageDrawable(null)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = ProductSectionViewHolder(
            ItemPlayProductSectionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
        )
    }

    interface Listener {
        fun onBuyProduct(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section)
        fun onATCProduct(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section)
        fun onClickProductCard(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, position: Int)
        fun onProductImpressed(product: List<Pair<PlayProductUiModel.Product, Int>>, sectionInfo: ProductSectionUiModel.Section)
        fun onProductChanged()
        fun onReminderClicked(section: ProductSectionUiModel.Section)
        fun onReminderImpressed(section: ProductSectionUiModel.Section)
        fun onInformationClicked(section: ProductSectionUiModel.Section)
        fun onInformationImpressed()
    }
}

