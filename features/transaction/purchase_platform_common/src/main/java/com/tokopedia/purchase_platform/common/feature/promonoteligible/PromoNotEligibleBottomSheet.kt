package com.tokopedia.purchase_platform.common.feature.promonoteligible

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.DialogPromoNotEligibleBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class PromoNotEligibleBottomSheet(var notEligiblePromoHolderDataList: ArrayList<NotEligiblePromoHolderdata>,
                                  var actionListener: PromoNotEligibleActionListener) {

    var dismissListener: () -> Unit = {}
    private var bottomSheet: BottomSheetUnify? = null

    fun show(context: Context, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = true
            setTitle(context.getString(R.string.label_continue_to_payment))
            clearContentPadding = true

            val binding = DialogPromoNotEligibleBinding.inflate(LayoutInflater.from(context), null, false)
            setupChildView(binding, context)
            setChild(binding.root)

            setShowListener {
                actionListener.onShow()
            }
            setOnDismissListener { dismissListener.invoke() }
        }
        bottomSheet?.show(fragmentManager, "")
    }

    private fun setupChildView(binding: DialogPromoNotEligibleBinding, context: Context) {
        with(binding) {
            btnContinue.setOnClickListener {
                actionListener.onButtonContinueClicked()
            }
            btnChooseOtherPromo.setOnClickListener {
                actionListener.onButtonChooseOtherPromo()
            }

            val adapter = PromoNotEligibleAdapter()
            adapter.notEligiblePromoHolderDataList = notEligiblePromoHolderDataList
            val linearLayoutManager = LinearLayoutManager(context)
            rvPromoList.layoutManager = linearLayoutManager
            rvPromoList.adapter = adapter
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }
}