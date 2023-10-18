package com.tokopedia.contactus.inboxtickets.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst.WEBVIEW
import com.tokopedia.applink.RouteManager
import com.tokopedia.contactus.R
import com.tokopedia.contactus.databinding.ServicePrioritiesBottomSheetLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ServicePrioritiesBottomSheet : BottomSheetUnify() {

    private var closeServicePrioritiesBottomSheet: CloseServicePrioritiesBottomSheet? = null

    fun setCloseButtonListener(closeServicePrioritiesBottomSheet: CloseServicePrioritiesBottomSheet){
        this.closeServicePrioritiesBottomSheet = closeServicePrioritiesBottomSheet
    }

    private var viewBinding by autoClearedNullable<ServicePrioritiesBottomSheetLayoutBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showKnob = false
        showCloseIcon = false
        showHeader = false
        viewBinding = ServicePrioritiesBottomSheetLayoutBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val link = viewBinding?.tvServicePrioritiesOfficialstoreDesc
        val close = viewBinding?.txtClose
        val context = viewBinding?.root?.context
        val text = context?.getString(R.string.service_priorities_officialstore_desc)?:return
        val spannableString = SpannableString(text)
        val startIndexOfLink = text.indexOf(LEARN_MORE_TEXT)
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    val appLink = context.getString(R.string.learn_more_link, WEBVIEW)
                    context.startActivity(
                        RouteManager.getIntent(
                            context,
                            appLink
                        )
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN300
                    )
                }
            },
            startIndexOfLink,
            startIndexOfLink + LEARN_MORE_TEXT.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        link?.highlightColor = Color.TRANSPARENT
        link?.movementMethod = LinkMovementMethod.getInstance()
        link?.setText(spannableString, TextView.BufferType.SPANNABLE)
        close?.setOnClickListener {
            closeServicePrioritiesBottomSheet?.onClickClose()
        }
    }

    interface CloseServicePrioritiesBottomSheet {
        fun onClickClose()
    }

    companion object {
        const val LEARN_MORE_TEXT = "Pelajari Selengkapnya"
    }
}
