package com.tokopedia.tokopedianow.common.bottomsheet

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowOnBoard20mBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowOnBoard20mBottomSheet: BottomSheetUnify() {

    companion object {
        private const val IMG_TIME = TokopediaImageUrl.IMG_TIME
        private const val IMG_GUARANTEED_QUALITY = TokopediaImageUrl.IMG_GUARANTEED_QUALITY
        private val TAG = TokoNowOnBoard20mBottomSheet::class.simpleName

        fun newInstance(): TokoNowOnBoard20mBottomSheet {
            return TokoNowOnBoard20mBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowOnBoard20mBinding>()

    private var listener: OnBoard20mBottomSheetListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onDismiss()
        super.onDismiss(dialog)
    }

    fun show(fm: FragmentManager, bottomSheetListener: OnBoard20mBottomSheetListener) {
        listener = bottomSheetListener
        show(fm, TAG)
    }

    private fun initView() {
        binding = BottomsheetTokopedianowOnBoard20mBinding.inflate(LayoutInflater.from(context))

        binding?.tpTime?.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_on_boarding_first_content_detail_bottomsheet, context?.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950) ?: ""))
        binding?.tpChoiceProduct?.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_on_boarding_second_content_detail_bottomsheet, context?.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950) ?: ""))

        binding?.iuTime?.setImageUrl(IMG_TIME)
        binding?.iuChoiceProduct?.setImageUrl(IMG_GUARANTEED_QUALITY)

        binding?.btnTryNow?.setOnClickListener {
            dismiss()
        }

        binding?.tpBackTo2h?.setOnClickListener {
            listener?.onBackTo2hClicked()
            dismiss()
        }

        clearContentPadding = true
        isFullpage = false
        setTitle(getString(R.string.tokopedianow_on_boarding_title_bottomsheet))
        setChild(binding?.root)
    }

    interface OnBoard20mBottomSheetListener {
        fun onBackTo2hClicked()
        fun onDismiss()
    }
}