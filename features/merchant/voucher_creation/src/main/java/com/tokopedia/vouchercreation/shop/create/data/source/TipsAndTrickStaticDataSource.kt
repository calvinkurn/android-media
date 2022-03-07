package com.tokopedia.vouchercreation.shop.create.data.source

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget.VoucherTipsItemTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.BasicVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.DottedVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.ImageVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.TipsItemUiModel

object TipsAndTrickStaticDataSource {

    fun getTipsAndTrickUiModelList(): List<TipsItemUiModel> =
            listOf(
                    TipsItemUiModel(
                            isOpen = true,
                            titleRes = R.string.mvc_tips_title_cashback_benefit,
                            tipsItemList = arrayListOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_cashback,
                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
                                            descRes = R.string.mvc_tips_desc_cashback
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = false,
                            titleRes = R.string.mvc_create_tips_title_voucher_name,
                            tipsItemList = arrayListOf(
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_voucher_name_1
                                    ),
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_voucher_name_2
                                    ),
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_voucher_name_3
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = false,
                            titleRes = R.string.mvc_create_tips_title_cashback_type,
                            tipsItemList = arrayListOf(
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_rupiah,
                                            titleRes = R.string.mvc_create_tips_subtitle_rupiah,
                                            descRes = R.string.mvc_create_tips_desc_rupiah
                                    ),
                                    ImageVoucherTipsItemUiModel(
                                            iconRes = R.drawable.ic_im_persentase,
                                            titleRes = R.string.mvc_create_tips_subtitle_percentage,
                                            descRes = R.string.mvc_create_tips_desc_percentage
                                    )
                            )),
                    TipsItemUiModel(
                            isOpen = false,
                            titleRes = R.string.mvc_create_tips_title_voucher_max_estimation,
                            tipsItemList = arrayListOf(
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_max_estimation_1
                                    ),
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_max_estimation_2
                                    ),
                                    DottedVoucherTipsItemUiModel(
                                            descRes = R.string.mvc_create_tips_desc_max_estimation_3
                                    ),
                                    BasicVoucherTipsItemUiModel(
                                            titleRes = R.string.mvc_create_tips_calculation,
                                            descRes = R.string.mvc_create_tips_estimation
                                    )
                            ))
            )

    // temporarily changed
//    fun getTipsAndTrickUiModelList(): List<TipsItemUiModel> =
//            listOf(
//                    TipsItemUiModel(
//                            isOpen = false,
//                            titleRes = R.string.mvc_create_tips_title_voucher_name,
//                            tipsItemList = arrayListOf(
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_voucher_name_1
//                                    ),
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_voucher_name_2
//                                    ),
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_voucher_name_3
//                                    )
//                            )),
//                    TipsItemUiModel(
//                            isOpen = true,
//                            titleRes = R.string.mvc_create_tips_title_voucher_type,
//                            tipsItemList = arrayListOf(
//                                    ImageVoucherTipsItemUiModel(
//                                            iconRes = R.drawable.ic_im_gratis_ongkir,
//                                            titleRes = R.string.mvc_create_tips_subtitle_free_delivery,
//                                            descRes = R.string.mvc_create_tips_desc_free_delivery
//                                    ),
//                                    ImageVoucherTipsItemUiModel(
//                                            iconRes = R.drawable.ic_im_cashback,
//                                            titleRes = R.string.mvc_create_tips_subtitle_cashback,
//                                            descRes = R.string.mvc_create_tips_desc_cashback
//                                    )
//                            )),
//                    TipsItemUiModel(
//                            isOpen = false,
//                            titleRes = R.string.mvc_create_tips_title_cashback_type,
//                            tipsItemList = arrayListOf(
//                                    ImageVoucherTipsItemUiModel(
//                                            iconRes = R.drawable.ic_im_rupiah,
//                                            titleRes = R.string.mvc_create_tips_subtitle_rupiah,
//                                            descRes = R.string.mvc_create_tips_desc_rupiah
//                                    ),
//                                    ImageVoucherTipsItemUiModel(
//                                            iconRes = R.drawable.ic_im_persentase,
//                                            titleRes = R.string.mvc_create_tips_subtitle_percentage,
//                                            descRes = R.string.mvc_create_tips_desc_percentage
//                                    )
//                            )),
//                    TipsItemUiModel(
//                            isOpen = false,
//                            titleRes = R.string.mvc_create_tips_title_voucher_max_estimation,
//                            tipsItemList = arrayListOf(
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_max_estimation_1
//                                    ),
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_max_estimation_2
//                                    ),
//                                    DottedVoucherTipsItemUiModel(
//                                            descRes = R.string.mvc_create_tips_desc_max_estimation_3
//                                    ),
//                                    BasicVoucherTipsItemUiModel(
//                                            titleRes = R.string.mvc_create_tips_calculation,
//                                            descRes = R.string.mvc_create_tips_estimation
//                                    )
//                            ))
//            )

    fun getGeneralExpenseUiModelList(): List<Visitable<VoucherTipsItemTypeFactory>> =
            arrayListOf(
                    DottedVoucherTipsItemUiModel(
                            descRes = R.string.mvc_create_tips_desc_max_estimation_1
                    ),
                    DottedVoucherTipsItemUiModel(
                            descRes = R.string.mvc_create_tips_desc_max_estimation_2
                    ),
                    DottedVoucherTipsItemUiModel(
                            descRes = R.string.mvc_create_tips_desc_max_estimation_3
                    ),
                    BasicVoucherTipsItemUiModel(
                            titleRes = R.string.mvc_create_tips_calculation,
                            descRes = R.string.mvc_create_tips_estimation
                    )
            )
}