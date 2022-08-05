package com.tokopedia.shop.flashsale.presentation.cancelation

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.presentation.draft.bottomsheet.DraftDeleteBottomSheet
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel

class CancelCampaignBottomSheet(
    private val campaignId: Long? = null,
    private val campaignName: String = "",
    private val status: CampaignStatus? = null,
    private val onSuccessDeleteCampaign: () -> Unit = {}
): DraftDeleteBottomSheet(DraftItemModel(campaignId.orZero(), campaignName), onSuccessDeleteCampaign) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCampaignCancelationTitle()
    }

    private fun setupCampaignCancelationTitle() {
        if (status == CampaignStatus.ONGOING) {
            setTitle(getString(R.string.cancelcampaign_stop_title))
            typographyDraftDeleteDesc?.text = getString(R.string.cancelcampaign_stop_desc, campaignName)
            typographyQuestionTitle?.text = getString(R.string.cancelcampaign_title_stop_question)
            btnStop?.text = getString(R.string.cancelcampaign_action_yes_stop)
        } else {
            setTitle(getString(R.string.cancelcampaign_title))
            typographyDraftDeleteDesc?.text = getString(R.string.cancelcampaign_desc, campaignName)
            typographyQuestionTitle?.text = getString(R.string.cancelcampaign_title_question)
            btnStop?.text = getString(R.string.cancelcampaign_action_yes_cancel)
        }
    }

}