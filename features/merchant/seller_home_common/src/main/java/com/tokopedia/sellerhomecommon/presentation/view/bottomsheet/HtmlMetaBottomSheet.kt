package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetHtmlMetaBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HtmlMetaBottomSheet: BaseBottomSheet<ShcBottomSheetHtmlMetaBinding>() {

    companion object {
        private const val META_KEY = "meta"

        private const val NUNITO_TYPOGRAPHY_FONT = "NunitoSansExtraBold.ttf"

        private const val TAG = "HtmlMetaBottomSheet"

        fun createInstance(meta: TableRowsUiModel.RowColumnHtmlWithMeta.HtmlMeta): HtmlMetaBottomSheet {
            return HtmlMetaBottomSheet().also { bottomSheet ->
                bottomSheet.arguments = Bundle().apply {
                    putParcelable(META_KEY, meta)
                }
            }
        }
    }

    private val meta by lazy {
        arguments?.getParcelable(META_KEY) as? TableRowsUiModel.RowColumnHtmlWithMeta.HtmlMeta
    }

    private var onHtmlMetaLinkClicked: (String) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetHtmlMetaBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() {
        setTitle(meta?.title.orEmpty())
        setDescription()
    }

    fun setOnMetaLinkClicked(onClick: (String) -> Unit) {
        onHtmlMetaLinkClicked = onClick
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved && isVisible) return
        show(fm, TAG)
    }

    private fun setDescription() {
        binding?.tvShcHtmlMeta?.text = getSpannableStringBuilder(
            meta?.value.orEmpty(),
            meta?.cta.orEmpty(),
            meta?.url.orEmpty()
        )
    }

    private fun getSpannableStringBuilder(
        fullText: String,
        spannedString: String,
        url: String
    ): SpannableStringBuilder? {
        val start = fullText.indexOf(spannedString, ignoreCase = true)
        val end = start + spannedString.length

        return start.takeIf { it >= Int.ZERO }?.let {
            val stringBuilder = SpannableStringBuilder(fullText)
            val urlSpan =
                ClickableSpanWithCustomStyle(
                    url,
                    context,
                    onHtmlMetaLinkClicked
                )
            stringBuilder.setSpan(
                urlSpan,
                start,
                end,
                stringBuilder.getSpanFlags(urlSpan)
            )
            stringBuilder
        }
    }

    inner class ClickableSpanWithCustomStyle(applink: String,
                                             private val context: Context?,
                                             private val onUrlClicked: (String) -> Unit) : URLSpan(applink) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            try {
                if (context != null) {
                    with(ds){
                        val textColorInt = MethodChecker.getColor(
                            context, unifyprinciplesR.color.Unify_GN500
                        )
                        isUnderlineText = false
                        color = textColorInt
                        typeface = com.tokopedia.unifyprinciples.getTypeface(
                            context,
                            NUNITO_TYPOGRAPHY_FONT
                        )
                    }
                }
            } catch (ignored: Exception) {
                //No-op
            }
        }

        override fun onClick(widget: View) {
            onUrlClicked(url)
        }
    }

}
