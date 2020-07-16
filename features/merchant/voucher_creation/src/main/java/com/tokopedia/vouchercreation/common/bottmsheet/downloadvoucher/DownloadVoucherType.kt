package com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher

import androidx.annotation.DimenRes
import com.tokopedia.vouchercreation.R

sealed class DownloadVoucherType(val imageUrl: String,
                                 @DimenRes val widthRes: Int) {
    class Square(imageUrl: String): DownloadVoucherType(imageUrl, R.dimen.mvc_download_voucher_square_width)
    class InstaStory(imageUrl: String): DownloadVoucherType(imageUrl, R.dimen.mvc_download_voucher_instastory_width)
    class Banner(imageUrl: String): DownloadVoucherType(imageUrl, R.dimen.mvc_download_voucher_banner_width)
}