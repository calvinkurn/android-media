package com.tokopedia.epharmacy.adapters.factory

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.*
import com.tokopedia.epharmacy.component.viewholder.*

class EPharmacyAdapterFactoryImpl(private val ePharmacyListener: EPharmacyListener?) : BaseAdapterTypeFactory(), EPharmacyAdapterFactory {

    override fun type(data: EPharmacyStaticInfoDataModel): Int {
        return EPharmacyStaticInfoViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyPrescriptionDataModel): Int {
        return EPharmacyPrescriptionViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyProductDataModel): Int {
        return EPharmacyProductViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyAttachmentDataModel): Int {
        return EPharmacyAttachmentViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyTickerDataModel): Int {
        return EPharmacyTickerViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyShimmerDataModel): Int {
        return EPharmacyShimmerViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyAccordionProductDataModel): Int {
        return EPharmacyAccordionProductItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            EPharmacyStaticInfoViewHolder.LAYOUT -> EPharmacyStaticInfoViewHolder(view)
            EPharmacyPrescriptionViewHolder.LAYOUT -> EPharmacyPrescriptionViewHolder(view, ePharmacyListener)
            EPharmacyProductViewHolder.LAYOUT -> EPharmacyProductViewHolder(view)
            EPharmacyAttachmentViewHolder.LAYOUT -> EPharmacyAttachmentViewHolder(view, ePharmacyListener)
            EPharmacyAccordionProductItemViewHolder.LAYOUT -> EPharmacyAccordionProductItemViewHolder(view)
            EPharmacyTickerViewHolder.LAYOUT -> EPharmacyTickerViewHolder(view)
            EPharmacyShimmerViewHolder.LAYOUT -> EPharmacyShimmerViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}

class EPharmacyDetailDiffUtil : DiffUtil.ItemCallback<BaseEPharmacyDataModel>() {

    override fun areItemsTheSame(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Boolean {
        return oldItem.name() == newItem.name()
    }

    override fun areContentsTheSame(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Any? {
        return oldItem.getChangePayload(newItem)
    }
}

class EPharmacyAttachmentDetailDiffUtil : DiffUtil.ItemCallback<BaseEPharmacyDataModel>() {

    override fun areItemsTheSame(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Boolean {
        return oldItem.name() == newItem.name()
    }

    override fun areContentsTheSame(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: BaseEPharmacyDataModel, newItem: BaseEPharmacyDataModel): Any? {
        return oldItem.getChangePayload(newItem)
    }
}
