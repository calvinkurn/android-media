package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherUrl.NO_VOUCHER_RESULT_URL
import com.tokopedia.vouchercreation.voucherlist.model.ui.NoResultStateUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list_no_result_state.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 26/04/20
 */

class NoResultStateViewHolder(itemView: View?,
                              private val listener: VoucherViewHolder.Listener) : AbstractViewHolder<NoResultStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list_no_result_state
    }

    override fun bind(element: NoResultStateUiModel) {
        with(itemView) {
            try {
                imgMvcNoResultState.loadImage(NO_VOUCHER_RESULT_URL)
                addOnImpressionListener(element.impressHolder) {
                    listener.onImpressionListener(NoResultStateUiModel.DATA_KEY)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}