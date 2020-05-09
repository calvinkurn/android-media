package com.tokopedia.vouchercreation.detail.view

/**
 * Created By @ilhamsuaib on 06/05/20
 */

interface VoucherDetailListener {

    fun onFooterButtonClickListener()

    fun showTipsAndTrickBottomSheet()

    fun showDescriptionBottomSheet(title: String, content: String)

    fun onFooterCtaTextClickListener()
}