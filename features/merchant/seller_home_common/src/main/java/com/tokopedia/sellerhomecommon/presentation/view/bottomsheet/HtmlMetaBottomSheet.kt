package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetHtmlMetaBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HtmlMetaBottomSheet : BaseBottomSheet<ShcBottomSheetHtmlMetaBinding>() {

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

    private var onHtmlMetaLinkClicked: (String, String) -> Unit = { _, _ -> }
    private var onCloseButtonClicked: (String) -> Unit = {}

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
        setMovementMethod()
        setDescription()
        setCloseButtonListener()
    }

    fun setOnMetaLinkClicked(onClick: (String, String) -> Unit) {
        onHtmlMetaLinkClicked = onClick
    }

    fun setOnCloseButtonClicked(onClick: (String) -> Unit) {
        onCloseButtonClicked = onClick
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved && isVisible) return
        show(fm, TAG)
    }

    private fun setMovementMethod() {
        binding?.tvShcHtmlMeta?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setCloseButtonListener() {
        setCloseClickListener {
            onCloseButtonClicked(meta?.title.orEmpty())
            dismiss()
        }
    }

    /**
     * Set the description of bottomsheet.
     *
     * If the cta text is not blank and the full text contains the cta text,
     * this code will apply the spannable string so the cta text can be clicked.
     */
    private fun setDescription() {
        val cta = meta?.cta
        val shouldUseSpannedString = cta?.takeIf { it.isNotBlank() }?.let {
            meta?.value.orEmpty().contains(it)
        }.orFalse()
        val metaText =
            if (shouldUseSpannedString) {
                getSpannableStringBuilder(
                    meta?.value.orEmpty(),
                    cta.orEmpty(),
                    meta?.url.orEmpty()
                )
            } else {
                meta?.value.orEmpty()
            }
        binding?.tvShcHtmlMeta?.text = metaText
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
                    ::clickHtmlMetaLink
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

    private fun clickHtmlMetaLink(applink: String) {
        onHtmlMetaLinkClicked(
            meta?.title.orEmpty(),
            applink
        )
    }

    inner class ClickableSpanWithCustomStyle(
        private val applink: String,
        private val context: Context?,
        private val onUrlClicked: (String) -> Unit
    ) : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            try {
                if (context != null) {
                    with(ds) {
                        val textColorInt = MethodChecker.getColor(
                            context,
                            unifyprinciplesR.color.Unify_GN500
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
                // No-op
            }
        }

        override fun onClick(widget: View) {
            onUrlClicked(applink)
        }
    }
}
