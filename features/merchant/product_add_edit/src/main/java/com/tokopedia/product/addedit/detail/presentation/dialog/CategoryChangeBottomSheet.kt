package com.tokopedia.product.addedit.detail.presentation.dialog

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.databinding.BottomsheetCategoryChangeLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class CategoryChangeBottomSheet : BottomSheetUnify() {

    companion object {

        private const val TAG = "CategoryChangeBottomSheet"

        private const val CTA_LENGTH = 21
        private const val CATEGORY_EDU_URL = "https://www.tokopedia.com/help/article/st-1023-faq-seputar-deteksi-kategori-produk"

        @JvmStatic
        fun createInstance(): CategoryChangeBottomSheet {
            return CategoryChangeBottomSheet()
        }
    }

    private var binding: BottomsheetCategoryChangeLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        setupServiceFeeContentText()
        val viewBinding = BottomsheetCategoryChangeLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
    }

    private fun setupView(binding: BottomsheetCategoryChangeLayoutBinding?) {

        binding?.root?.context?.run {
            val text = this.getString(R.string.content_category_change_bs)
            val ss = SpannableString(text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    RouteManager.route(
                        context,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, CATEGORY_EDU_URL)
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    context?.run {
                        ds.color = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ds.underlineColor = 0
                    }
                }
            }
            ss.setSpan(clickableSpan, (text.length - CTA_LENGTH), text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tpgCategoryChangeContent.text = ss
            binding.tpgCategoryChangeContent.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setupBottomSheet() {
        context?.run { setTitle(this.getString(R.string.label_category_change)) }
        isFullpage = false
        clearContentPadding = true
    }

    private fun setupServiceFeeContentText() {
        Typography.setUnifyTypographyOSO(true)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible && !isAdded) {
            show(fragmentManager, TAG)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
