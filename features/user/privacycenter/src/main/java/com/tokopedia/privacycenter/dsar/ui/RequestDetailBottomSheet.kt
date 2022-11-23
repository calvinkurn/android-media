package com.tokopedia.privacycenter.dsar.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.common.utils.formatDateLocalTimezone
import com.tokopedia.privacycenter.databinding.BottomSheetDetailsBinding
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
            setTitle("Detail Pengajuan")
            setChild(binding.root)
            binding.personalInfo.imgProfilePicture.loadImage(userSessionInterface.profilePicture)
            binding.personalInfo.txtProfileEmail.text = requestDetailResponse.email
            binding.personalInfo.txtProfileName.text = requestDetailResponse.firstName
            binding.personalInfo.txtProfilePhone.text = additionalData.phoneNumber
            binding.layoutSummary.ticker.setTextDescription(Html.fromHtml("Salinan data akan dikirim ke e-mail diatas paling lambat <b>${requestDetailResponse.deadline.formatDateLocalTimezone()}</b>"))
            binding.layoutSummary.txtSummary.text = Html.fromHtml(mapAdditionalData(additionalData))
        }
        bottomSheet.show(fragmentManager, "")
        return bottomSheet
    }

    fun mapAdditionalData(additionalData: RequestAdditionalData): String {
        val requestDetails = additionalData.requestDetails.split(",")
        var finalText = ""
        if (requestDetails.contains("full_name")) {
            finalText += "$PERSONAL_LABEL$HTML_NEW_LINE$HTML_NEW_LINE"
        }
        if (requestDetails.contains(FILTER_TYPE_PAYMENT)) {
            finalText += "$PAYMENT_LABEL$HTML_NEW_LINE$HTML_NEW_LINE"
        }

        val match = requestDetails.find { it.contains(TRANSACTION_HISTORY_PREFIX) }
        if (match?.isNotEmpty() == true) {
            val splitTransaction = match.split("_")
            val startDate = convertToDate(splitTransaction[2])
            val endDate = convertToDate(splitTransaction[3])

            if (startDate != null && endDate != null) {
                val diff = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time)

                val firstDayOfThisYear = GregorianCalendar(Locale.getDefault()).apply {
                    set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY)
                    set(GregorianCalendar.DAY_OF_MONTH, 1)
                }

                val thisYearDiff = TimeUnit.MILLISECONDS.toDays(endDate.time - firstDayOfThisYear.time.time)

                val dateString = when (diff) {
                    7L -> {
                        "7 Hari Terakhir"
                    }
                    30L -> {
                        "30 Hari Terakhir"
                    }
                    in 90L..92L -> {
                        "3 Bulan Terakhir"
                    }
                    1096L -> {
                        "3 Tahun Terakhir"
                    }
                    in (thisYearDiff-1)..(thisYearDiff+1) -> {
                        "Selama Tahun Ini"
                    }
                    else -> {
                        "${formatDate(startDate)} - ${formatDate(endDate)}"
                    }
                }
                finalText += "$TRANSACTION_LABEL$dateString<br>"
            }
        }
        return finalText
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    fun convertToDate(date: String): Date? {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.parse(date)
    }
}
