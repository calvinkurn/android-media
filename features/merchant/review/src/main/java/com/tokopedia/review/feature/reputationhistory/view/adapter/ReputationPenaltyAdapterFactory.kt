package com.tokopedia.review.feature.reputationhistory.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationDateUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationShopUiModel

class ReputationPenaltyAdapterFactory: BaseAdapterTypeFactory(), ReputationPenaltyTypeFactory {

    override fun type(reputationDateUiModel: ReputationDateUiModel): Int {

    }

    override fun type(reputationShopUiModel: ReputationShopUiModel): Int {
    }

    override fun type(reputationPenaltyUiModel: ReputationPenaltyUiModel): Int {
    }

}