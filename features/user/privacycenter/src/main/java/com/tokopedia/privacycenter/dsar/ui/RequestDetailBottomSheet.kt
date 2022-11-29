package com.tokopedia.privacycenter.dsar.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.BottomSheetDetailsBinding
import com.tokopedia.privacycenter.dsar.DsarConstants
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PAYMENT
import com.tokopedia.privacycenter.dsar.DsarConstants.HTML_NEW_LINE
import com.tokopedia.privacycenter.dsar.DsarConstants.PAYMENT_LABEL
import com.tokopedia.privacycenter.dsar.DsarConstants.PERSONAL_LABEL
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_HISTORY_PREFIX
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_LABEL
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.RequestAdditionalData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import com.tokopedia.utils.date.DateUtil.YYYYMMDD
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import java.util.*

object RequestDetailBottomSheet {

    fun showDetailsBottomSheet(
        fragmentManager: FragmentManager,
        context: Context,
        userSessionInterface: UserSessionInterface,
        requestDetailResponse: GetRequestDetailResponse
    ): BottomSheetUnify {
        val bottomSheet = BottomSheetUnify().apply {
            val additionalData = Gson().fromJson(
                requestDetailResponse.additionalData,
                RequestAdditionalData::class.java
            )
            val binding = BottomSheetDetailsBinding.inflate(LayoutInflater.from(context))
            setTitle(context.getString(R.string.dsar_bottom_sheet_title))
            setChild(binding.root)
            binding.personalInfo.run {
                imgProfilePicture.loadImage(userSessionInterface.profilePicture)
                txtProfileEmail.text = requestDetailResponse.email
                txtProfileName.text = requestDetailResponse.firstName
                txtProfilePhone.text = additionalData.phoneNumber
            }
            val formattedDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
                DateUtil.DEFAULT_VIEW_FORMAT,
                requestDetailResponse.deadline
            )
            val summaryInfoPrefix = context.getString(R.string.dsar_bottom_sheet_info_prefix)
            binding.layoutSummary.tickerInfoSummary.setTextDescription(Html.fromHtml("$summaryInfoPrefix <b>${formattedDate}</b>"))
            binding.layoutSummary.txtSummary.text = Html.fromHtml(mapAdditionalData(additionalData))
        }
        bottomSheet.show(fragmentManager, "")
        return bottomSheet
    }

    private fun mapAdditionalData(additionalData: RequestAdditionalData): String {
        var finalText = ""
        if (additionalData.requestDetails.contains(DsarConstants.DSAR_PERSONAL_DATA.first())) {
            finalText += "$PERSONAL_LABEL$HTML_NEW_LINE$HTML_NEW_LINE"
        }
        if (additionalData.requestDetails.contains(FILTER_TYPE_PAYMENT)) {
            finalText += "$PAYMENT_LABEL$HTML_NEW_LINE$HTML_NEW_LINE"
        }

        val requestDetails = additionalData.requestDetails.split(",")
        val match = requestDetails.find { it.contains(TRANSACTION_HISTORY_PREFIX) }
        if (match?.isNotEmpty() == true) {
            val splitTransaction = match.split("_")
            val startDate = splitTransaction[2].toDate(YYYYMMDD)
            val endDate = splitTransaction[3].toDate(YYYYMMDD)
            val dateString = "${startDate.toString(DEFAULT_VIEW_FORMAT)} - ${endDate.toString(DEFAULT_VIEW_FORMAT)}"
            finalText += "$TRANSACTION_LABEL$dateString$HTML_NEW_LINE"
        }
        return finalText
    }
}
