package com.tokopedia.epharmacy.adapters.factory

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailInfoDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailPaymentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyStaticInfoDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.epharmacy.component.viewholder.EPharmacyAccordionProductItemViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyAttachmentViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyOrderDetailHeaderViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyOrderDetailInfoViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyOrderDetailPaymentViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyPrescriptionViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyProductViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyShimmerViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyStaticInfoViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyTickerViewHolder

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

    override fun type(data: EPharmacyOrderDetailHeaderDataModel): Int {
        return EPharmacyOrderDetailHeaderViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyOrderDetailInfoDataModel): Int {
        return EPharmacyOrderDetailInfoViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyOrderDetailPaymentDataModel): Int {
        return EPharmacyOrderDetailPaymentViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            EPharmacyStaticInfoViewHolder.LAYOUT -> EPharmacyStaticInfoViewHolder(view)
            EPharmacyPrescriptionViewHolder.LAYOUT -> EPharmacyPrescriptionViewHolder(view, ePharmacyListener)
            EPharmacyProductViewHolder.LAYOUT -> EPharmacyProductViewHolder(view)
            EPharmacyAttachmentViewHolder.LAYOUT -> EPharmacyAttachmentViewHolder(view, ePharmacyListener)
            EPharmacyAccordionProductItemViewHolder.LAYOUT -> EPharmacyAccordionProductItemViewHolder(view, ePharmacyListener)
            EPharmacyTickerViewHolder.LAYOUT -> EPharmacyTickerViewHolder(view, ePharmacyListener)
            EPharmacyShimmerViewHolder.LAYOUT -> EPharmacyShimmerViewHolder(view)
            EPharmacyOrderDetailHeaderViewHolder.LAYOUT -> EPharmacyOrderDetailHeaderViewHolder(view, ePharmacyListener)
            EPharmacyOrderDetailInfoViewHolder.LAYOUT -> EPharmacyOrderDetailInfoViewHolder(view)
            EPharmacyOrderDetailPaymentViewHolder.LAYOUT -> EPharmacyOrderDetailPaymentViewHolder(view, ePharmacyListener)
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
