package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailInfoDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class EPharmacyOrderDetailInfoViewHolder(
    val view: View
) : AbstractViewHolder<EPharmacyOrderDetailInfoDataModel>(view) {

    private val detailProductHeader = view.findViewById<Typography>(R.id.detail_product_header)
    private val serviceTypeValue = view.findViewById<Typography>(R.id.lblValueItemOfService)
    private val providerName = view.findViewById<Typography>(R.id.service_provider_value)
    private val durationValue = view.findViewById<Typography>(R.id.duration_value)
    private val feesValue = view.findViewById<Typography>(R.id.fee_value)
    private val validityTimeValue = view.findViewById<Typography>(R.id.validity_value)
    private val validityTime = view.findViewById<Typography>(R.id.validity_time)

    companion object {
        val LAYOUT = R.layout.epharmacy_name_detail_view_item
    }

    override fun bind(data: EPharmacyOrderDetailInfoDataModel) {
        serviceTypeValue.text = data.serviceType
        providerName.text = data.enablerName
        durationValue.text = data.duration
        feesValue.text = data.fees
        setUpValidity(data.validity)
        detailProductHeader.show()
    }

    private fun setUpValidity(validity: String) {
        validityTimeValue.displayTextOrHide(validity)
        if (validity.isNotBlank()) {
            validityTime.show()
        }
    }
}
