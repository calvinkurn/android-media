package com.tokopedia.epharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.viewholder.EPharmacyAttachmentViewHolder
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.EventKeys.Companion.TRACKER_ID
import com.tokopedia.epharmacy.utils.TrackerId.Companion.TICKER_TRACKER_ID
import com.tokopedia.track.builder.Tracker

class EPharmacyAdapter(
    asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel>,
    private val ePharmacyAdapterTypeFactory: EPharmacyAdapterFactory
) :
    ListAdapter<BaseEPharmacyDataModel, AbstractViewHolder<*>>(asyncDifferConfig) {

    private val itemImpressionSet = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return ePharmacyAdapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseEPharmacyDataModel>, getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(holder: AbstractViewHolder<BaseEPharmacyDataModel>, item: BaseEPharmacyDataModel) {
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else {
            currentList[position]?.type(ePharmacyAdapterTypeFactory) ?: HideViewHolder.LAYOUT
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        handleImpressions(holder)
        super.onViewAttachedToWindow(holder)
    }

    private fun handleImpressions(
        holder: AbstractViewHolder<*>
    ) {
        when (holder) {
            is EPharmacyAttachmentViewHolder -> {
                val item =
                    currentList.getOrNull(holder.bindingAdapterPosition) as? EPharmacyAttachmentDataModel
                if (item != null && itemImpressionSet.add(item.hashCode())) {
                    sendTickerImpression(item.ticker?.message, item.enablerName, item.epharmacyGroupId, item.tokoConsultationId)
                }
            }
        }
    }

    private fun sendTickerImpression(
        message: String?,
        enablerName: String?,
        eGroupId: String?,
        tConsultationId: String?
    ) {
        sendViewChangedQuantityTickerEvent("$enablerName - $eGroupId - $tConsultationId - $message")
    }

    private fun sendViewChangedQuantityTickerEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view changed quantity ticker")
            .setEventCategory(CategoryKeys.ATTACH_PRESCRIPTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TICKER_TRACKER_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
