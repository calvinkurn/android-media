package com.tokopedia.epharmacy.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_RESULT_EXTRA
import com.tokopedia.common_epharmacy.EPHARMACY_PPG_SOURCE_CHECKOUT
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CART_RESULT_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.ui.fragment.EPharmacyQuantityChangeFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.webview.ext.encode

internal object EPharmacyNavigator {

    internal fun createChooserAppLink(
        enablerImage: String?,
        ePharmacyGroupId: String?,
        enablerName: String?,
        price: String?,
        duration: String?,
        note: String?,
        isOutsideWorkingHours: Boolean?,
        isOnlyConsult: Boolean = false
    ): String {
        return UriUtil.buildUriAppendParam(
            EPHARMACY_CHOOSER_APPLINK,
            mapOf(
                ENABLER_IMAGE_URL to enablerImage?.encode(),
                EPHARMACY_GROUP_ID to ePharmacyGroupId?.encode(),
                EPHARMACY_ENABLER_NAME to enablerName,
                EPHARMACY_CONS_PRICE to price.orEmpty(),
                EPHARMACY_CONS_DURATION to duration.orEmpty(),
                EPHARMACY_NOTE to note?.encode().orEmpty(),
                EPHARMACY_IS_OUTSIDE_WORKING_HOURS to isOutsideWorkingHours?.orFalse().toString(),
                EPHARMACY_IS_ONLY_CONSULT to isOnlyConsult.toString()
            )
        )
    }

    internal fun prescriptionAttachmentDoneRedirection(activity: Activity?, appLink: String?, source: String, result: ArrayList<EPharmacyMiniConsultationResult>): PapCtaRedirection {
        if (appLink.isNullOrBlank()) {
            return PapCtaRedirection.None
        }

        if (appLink.contains(EPHARMACY_APP_CHECKOUT_APPLINK)) {
            if (source != EPHARMACY_PPG_SOURCE_CHECKOUT) {
                return PapCtaRedirection.RedirectionUpdateQuantity
            }
            activity?.setResult(
                EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE,
                Intent().apply {
                    putParcelableArrayListExtra(
                        EPHARMACY_CONSULTATION_RESULT_EXTRA,
                        result
                    )
                }
            )
            activity?.finish()
        } else if (appLink.contains(EPHARMACY_CART_APPLINK)) {
            activity?.setResult(
                EPHARMACY_REDIRECT_CART_RESULT_CODE,
                Intent()
            )
            activity?.finish()
        } else if (appLink.contains(EPHARMACY_QUANTITY_EDITOR)) {
            return PapCtaRedirection.RedirectionQuantityEditor(getTConsultationIds(result))
        } else {
            RouteManager.route(activity, appLink)
        }
        return PapCtaRedirection.None
    }

    private fun getTConsultationIds(result: ArrayList<EPharmacyMiniConsultationResult>): List<String> {
        return result
            .mapNotNull { it.tokoConsultationId?.takeIf { id -> id.isNotBlank() } }
    }

    fun navigateToQuantityBottomSheet(tConsultationIds: ArrayList<String>?, childFragmentManager: FragmentManager) {
        childFragmentManager.beginTransaction().replace(
            R.id.ep_frame_content,
            EPharmacyQuantityChangeFragment.newInstance(tConsultationIds)
        ).commit()
    }
}

sealed class PapCtaRedirection {
    object RedirectionUpdateQuantity : PapCtaRedirection()
    data class RedirectionQuantityEditor(val tConsultationIds: List<String>) : PapCtaRedirection()
    object None : PapCtaRedirection()
}
