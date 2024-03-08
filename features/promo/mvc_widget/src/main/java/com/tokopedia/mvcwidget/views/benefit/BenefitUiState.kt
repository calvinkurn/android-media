package com.tokopedia.mvcwidget.views.benefit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BenefitUiModel(
    val bgImgUrl: String = "",
    val bgColor: String = "",
    val estimatePrice: BenefitText = BenefitText(),
    val basePrice: BenefitText = BenefitText(),
    val usablePromo: List<UsablePromoModel> = listOf(),
    val tnc: BenefitTnc = BenefitTnc(),
) : Parcelable

@Parcelize
data class BenefitText(
    val title: String = "Perkiraan harga terhemat",
    val titleColor: String = "",
    val titleFormat: String = "normal",
    val text: String = "",
    val textColor: String = "",
    val textFormat: String = "bold"
) : Parcelable

@Parcelize
data class BenefitTnc(
    val tncTexts: List<String> = listOf(),
    val color: String = ""
) : Parcelable

@Parcelize
data class UsablePromoModel(
    val icon: String,
    val title: String,
    val text: String,
    val titleColor: String = "",
    val titleFormat: String = "normal",
    val textColor: String = "",
    val textFormat: String = "normal",
) : Parcelable
