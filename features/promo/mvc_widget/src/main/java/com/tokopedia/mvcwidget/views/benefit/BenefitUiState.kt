package com.tokopedia.mvcwidget.views.benefit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BenefitUiModel(
    val bgImgUrl: String = "",
    val bgColor: String = "#FFF5F6",
    val estimatePrice: BenefitText = BenefitText(),
    val basePrice: BenefitText = BenefitText(),
    val usablePromo: List<UsablePromoModel> = listOf(),
    val tnc: BenefitTnc = BenefitTnc(),
) : Parcelable

@Parcelize
data class BenefitText(
    val title: String = "Perkiraan harga terhemat",
    val titleColor: String = "#212121",
    val titleFormat: String = "normal",
    val text: String = "",
    val textColor: String = "#212121",
    val textFormat: String = "bold"
) : Parcelable

@Parcelize
data class BenefitTnc(
    val tncTexts: List<String> = listOf(),
    val color: String = "#6D7588"
) : Parcelable

@Parcelize
data class UsablePromoModel(
    val icon: String,
    val title: String,
    val text: String,
    val titleColor: String = "#6D7588",
    val titleFormat: String = "normal",
    val textColor: String = "#6D7588",
    val textFormat: String = "normal",
) : Parcelable
