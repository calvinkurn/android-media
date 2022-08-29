package com.tokopedia.vouchercreation.shop.detail.view

import android.graphics.Bitmap

/**
 * Created By @ilhamsuaib on 06/05/20
 */

interface VoucherDetailListener {

    fun onFooterButtonClickListener()

    fun showTipsAndTrickBottomSheet()

    fun showDescriptionBottomSheet(title: String, content: String)

    fun onFooterCtaTextClickListener()

    fun onInfoContainerCtaClick(dataKey: String)

    fun showDownloadBottomSheet()

    fun onTickerClicked()

    fun onSuccessDrawPostVoucher(postVoucherBitmap: Bitmap)

    fun onErrorTryAgain()

    fun onImpression(dataKey: String)
}