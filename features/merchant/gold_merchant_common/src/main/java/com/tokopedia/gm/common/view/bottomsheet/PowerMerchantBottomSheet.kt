package com.tokopedia.gm.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_gmc_power_merchant.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class PowerMerchantBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "SahPowerMerchantBottomSheet"

        fun createInstance(): PowerMerchantBottomSheet {
            return PowerMerchantBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                showCloseIcon = false
            }
        }
    }

    private var data: PowerMerchantInterruptUiModel? = null
    private var childView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottom_sheet_gmc_power_merchant, container, false)
        childView = view
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    fun setData(data: PowerMerchantInterruptUiModel): PowerMerchantBottomSheet {
        this.data = data
        return this
    }

    fun show(fm: FragmentManager) {
        if (data != null) {
            show(fm, TAG)
        }
    }

    private fun setupView() = childView?.run {
        if (data == null) {
            dismiss()
            return@run
        }

        data?.let { data ->
            tvSahPmTitle.text = context.getString(R.string.gmc_power_merchant_bottom_sheet_title, data.pmNewUpdateDateFmt)
            slvSahPmShopLevel.show(data.shopLevel)
            tvSahPmShopScore.text = data.shopScore.toString()
        }
    }
}