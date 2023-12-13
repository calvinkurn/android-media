package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.databinding.BottomSheetsBcaInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.recharge_pdp_emoney.R as recharge_pdp_emoneyR

class EmoneyBCAInfoBottomSheets: BottomSheetUnify()  {

    init {
        clearContentPadding = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = BottomSheetsBcaInfoBinding.inflate(LayoutInflater.from(context))
        setChild(view.root)
        initView(view)
    }

    private fun initView(binding: BottomSheetsBcaInfoBinding) {
        with(binding) {
            setTitle(getString(recharge_pdp_emoneyR.string.recharge_pdp_emoney_bca_info_title))
            imgInfoGenOne.loadImage("https://images.tokopedia.net/img/digital/flazz_logo_gen1.png")
            imgInfoGenTwo.loadImage("https://images.tokopedia.net/img/digital/flazz_logo_gen2.png")
        }
    }

    companion object {
        fun newInstance(): EmoneyBCAInfoBottomSheets {
            return EmoneyBCAInfoBottomSheets()
        }
    }
}
