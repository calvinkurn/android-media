package com.tokopedia.promousage.view.viewmodel

import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel

sealed class GetPromoRecommendationUiAction {

    data class NotEmpty(
        val promoRecommendation: PromoRecommendationItem
    ) : GetPromoRecommendationUiAction()

    object Empty : GetPromoRecommendationUiAction()
}

sealed class UsePromoRecommendationUiAction {

    data class Success(
        val promoRecommendation: PromoRecommendationItem,
        val items: List<DelegateAdapterItem>,
        val isClickUseRecommendation: Boolean = false
    ) : UsePromoRecommendationUiAction()

    object Failed : UsePromoRecommendationUiAction()
}

sealed class ClearPromoUiAction {

    data class Success(
        val entryPoint: PromoPageEntryPoint,
        val clearPromo: ClearPromoUiModel,
        val lastValidateUseRequest: ValidateUsePromoRequest,
        val clearedPromos: List<PromoItem>
    ) : ClearPromoUiAction()

    data class Failed(
        val throwable: Throwable
    ) : ClearPromoUiAction()
}

sealed class GetPromoListUiAction(
    val exception: Throwable? = null
) {

    object Success : GetPromoListUiAction()

    data class Failed(
        val throwable: Throwable
    ) : GetPromoListUiAction()
}

sealed class AttemptPromoUiAction {

    object Success : AttemptPromoUiAction()

    data class Failed(
        val errorMessage: String
    ) : AttemptPromoUiAction()
}

sealed class ApplyPromoUiAction {

    data class SuccessWithApplyPromo(
        val entryPoint: PromoPageEntryPoint,
        val validateUse: ValidateUsePromoRevampUiModel,
        val lastValidateUsePromoRequest: ValidateUsePromoRequest,
        val appliedPromos: List<PromoItem>
    ) : ApplyPromoUiAction()

    object SuccessNoAction : ApplyPromoUiAction()

    data class Failed(
        val throwable: Throwable,
        val shouldReload: Boolean = false
    ) : ApplyPromoUiAction()
}

sealed class PromoCtaUiAction {

    data class RegisterGoPayLaterCicil(val cta: PromoCta) : PromoCtaUiAction()
}

sealed class ClosePromoPageUiAction {

    data class SuccessWithApplyPromo(
        val entryPoint: PromoPageEntryPoint,
        val validateUse: ValidateUsePromoRevampUiModel,
        val lastValidateUsePromoRequest: ValidateUsePromoRequest,
        val appliedPromos: List<PromoItem>
    ) : ClosePromoPageUiAction()

    data class SuccessWithClearPromo(
        val entryPoint: PromoPageEntryPoint,
        val clearPromo: ClearPromoUiModel,
        val lastValidateUsePromoRequest: ValidateUsePromoRequest,
        val clearedPromos: List<PromoItem>
    ) : ClosePromoPageUiAction()

    object SuccessNoAction : ClosePromoPageUiAction()

    data class Failed(
        val throwable: Throwable
    ) : ClosePromoPageUiAction()
}

sealed class ClickPromoUiAction {

    data class Updated(val clickedPromo: PromoItem) : ClickPromoUiAction()
}

sealed class ClickTncUiAction {

    data class Success(val selectedPromos: List<PromoItem>) : ClickTncUiAction()
}
