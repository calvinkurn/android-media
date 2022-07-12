package com.tokopedia.shopdiscount.info.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountSellerInfoCardItemBinding
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountSellerInfoSectionItemBinding
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.unixToMs
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.date.DateUtil
import java.util.*

class ShopDiscountSellerInfoSectionView : ConstraintLayout {

    private var binding: LayoutBottomSheetShopDiscountSellerInfoSectionItemBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        binding = LayoutBottomSheetShopDiscountSellerInfoSectionItemBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun setSectionData(
        titleText: String,
        contentText: String,
        descriptionText: String
    ) {
        binding?.apply {
            container.show()
            textSectionTitle.text = titleText
            textSectionContent.text = MethodChecker.fromHtml(contentText)
            textSectionDescription.apply {
                if (descriptionText.isNotEmpty()) {
                    show()
                    text = descriptionText
                } else {
                    hide()
                }
            }
        }
    }

    fun hideDivider() {
        binding?.divider?.gone()
    }

    fun setOptionData(data: ShopDiscountSellerInfoUiModel) {
        if (data.listSlashPriceBenefitData.size > 1) {
            binding?.apply {
                val nonVpsData = data.listSlashPriceBenefitData.getOrNull(0)
                setCardOptionData(
                    cardFirstOption,
                    nonVpsData?.packageName.orEmpty(),
                    context.getString(R.string.seller_info_bottom_sheet_package_option_non_vps_description)
                )
                val vpsData = data.listSlashPriceBenefitData.getOrNull(1)
                val formattedExpiryDate = Date(
                    vpsData?.expiredAtUnix.orZero().unixToMs()
                ).parseTo(DateUtil.DEFAULT_VIEW_FORMAT)
                val vpsPackageDescription = String.format(
                    context.getString(R.string.seller_info_bottom_sheet_package_option_vps_description_format),
                    vpsData?.remainingQuota.orZero(),
                    vpsData?.maxQuota.orZero(),
                    formattedExpiryDate,
                )
                setCardOptionData(
                    cardSecondOption,
                    vpsData?.packageName.orEmpty(),
                    vpsPackageDescription
                )
                if (data.isUseVps) {
                    setCardTypeActive(cardSecondOption.cardOption)
                    setCardTypeDisabled(cardFirstOption.cardOption)
                } else {
                    setCardTypeActive(cardFirstOption.cardOption)
                    setCardTypeDisabled(cardSecondOption.cardOption)
                }
                imgChevron.show()
                imgChevron.setOnClickListener {
                    if (!optionContainer.isShown) {
                        optionContainer.show()
                    } else {
                        optionContainer.hide()
                    }
                }
            }
        }
    }

    private fun setCardTypeDisabled(cardOption: CardUnify2) {
        cardOption.cardType = CardUnify2.TYPE_BORDER_DISABLED
    }

    private fun setCardTypeActive(cardOption: CardUnify2) {
        cardOption.apply {
            cardType = CardUnify2.TYPE_BORDER_ACTIVE
            checkIcon.setImageDrawable(
                MethodChecker.getDrawable(
                    context,
                    R.drawable.ic_seller_info_option_check
                )
            )
            checkIcon.layoutParams.width = 10.toPx()
            checkIcon.layoutParams.height = 10.toPx()
        }
    }

    private fun setCardOptionData(
        cardFirstOption: LayoutBottomSheetShopDiscountSellerInfoCardItemBinding?,
        titleText: String,
        descriptionText: String
    ) {
        cardFirstOption?.apply {
            title.text = titleText
            description.text = descriptionText
            cardOption.cardType = CardUnify2.TYPE_BORDER
//            cardOption.checkIcon.setImageDrawable(
//                MethodChecker.getDrawable(
//                    context,
//                    R.drawable.ic_seller_info_option_check
//                )
//            )
            cardOption.hasCheckIcon = true
        }
    }


}