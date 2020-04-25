package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.ImageVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.TipsItemUiModel

object TipsAndTrickStaticDataSource {

    fun getTipsAndTrickUiModelList(): List<TipsItemUiModel> =
            listOf(
                    TipsItemUiModel(
                            isOpen = true,
                            titleRes = R.string.mvc_create_tips_title_voucher_name,
                            tipsItemList = listOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_gratis_ongkir,
                                            titleRes = R.string.mvc_create_tips_subtitle_free_delivery,
                                            descRes = R.string.mvc_create_tips_desc_free_delivery
                                    ),
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_cashback,
                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
                                            descRes = R.string.mvc_create_tips_desc_cashback
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = true,
                            titleRes = R.string.mvc_create_tips_title_voucher_name,
                            tipsItemList = listOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_gratis_ongkir,
                                            titleRes = R.string.mvc_create_tips_subtitle_free_delivery,
                                            descRes = R.string.mvc_create_tips_desc_free_delivery
                                    ),
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_cashback,
                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
                                            descRes = R.string.mvc_create_tips_desc_cashback
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = false,
                            titleRes = R.string.mvc_create_tips_title_voucher_name,
                            tipsItemList = listOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_gratis_ongkir,
                                            titleRes = R.string.mvc_create_tips_subtitle_free_delivery,
                                            descRes = R.string.mvc_create_tips_desc_free_delivery
                                    ),
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_cashback,
                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
                                            descRes = R.string.mvc_create_tips_desc_cashback
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = true,
                            titleRes = R.string.mvc_create_tips_title_voucher_name,
                            tipsItemList = listOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_gratis_ongkir,
                                            titleRes = R.string.mvc_create_tips_subtitle_free_delivery,
                                            descRes = R.string.mvc_create_tips_desc_free_delivery
                                    ),
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_cashback,
                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
                                            descRes = R.string.mvc_create_tips_desc_cashback
                                    )
                            ))
            )
}