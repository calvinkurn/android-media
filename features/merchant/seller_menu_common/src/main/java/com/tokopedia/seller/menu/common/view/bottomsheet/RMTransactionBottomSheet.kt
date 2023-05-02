package com.tokopedia.seller.menu.common.view.bottomsheet

import android.os.Bundle
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.databinding.SellerRmTransactionBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RMTransactionBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SellerRmTransactionBottomsheetBinding>()

    private val linkTextColor by lazy(LazyThreadSafetyMode.NONE) {
        context?.let {
            MethodChecker.getColor(
                it,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getBottomSheetTitle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SellerRmTransactionBottomsheetBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransactionDetail()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun getBottomSheetTitle(): String {
        return context?.getString(com.tokopedia.seller.menu.common.R.string.seller_rm_transaction_title).orEmpty()
    }

    private fun setTransactionDetail() {
        setTransactionInfo()
        setTransactionEdu()
    }

    private fun setTransactionInfo() {
        binding?.tvSellerRmTransactionInfo?.text = getRmTransactionInfoString().parseAsHtml()
    }

    private fun setTransactionEdu() {
        binding?.tvSellerRmTransactionEdu?.setClickableUrlHtml(
            htmlText = getRmTransactionEduString(),
            applyCustomStyling = {
                applyStyling()
            },
            onUrlClicked = { _,_ ->
                goToNewMembershipScheme()
            }
        )
    }

    private fun getRmTransactionInfoString(): String {
        val currentTransactionString = getCurrentTransactionString()
        return context?.getString(
            com.tokopedia.seller.menu.common.R.string.seller_rm_transaction_info,
            currentTransactionString).orEmpty()
    }

    private fun getRmTransactionEduString(): String {
        return context?.getString(com.tokopedia.seller.menu.common.R.string.seller_rm_transaction_edu)
            .orEmpty()
    }

    private fun getCurrentTransactionString(): String {
        return arguments?.getLong(CURRENT_TRANSACTION_KEY).orZero().toString()
    }

    private fun goToNewMembershipScheme() {
        context?.let {
            RouteManager.route(it, SellerBaseUrl.getRegularMerchantEduApplink())
        }
    }

    private fun TextPaint.applyStyling() {
        isUnderlineText = false
        linkTextColor?.let {
            color = it
        }
        applyFont()
    }

    private fun TextPaint.applyFont() {
        context?.let {
            com.tokopedia.unifyprinciples.Typography.getFontType(it, true, Typography.PARAGRAPH_2)?.let { fontType ->
                typeface = fontType
            }
        }
    }

    companion object {
        private const val CURRENT_TRANSACTION_KEY = "current_transaction"

        private const val TAG = "rm_transaction_bottom_sheet"

        @JvmStatic
        fun createInstance(currentTransaction: Long): RMTransactionBottomSheet {
            return RMTransactionBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(CURRENT_TRANSACTION_KEY, currentTransaction)
                }
            }
        }
    }

}
