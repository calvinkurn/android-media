package com.tokopedia.navigation.presentation.fragment


import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.navigation.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_notification_update_longer.view.*

/**
 * @author by nisie on 18/02/19.
 */
class NotificationUpdateLongerTextFragment : BottomSheetDialogFragment() {

    lateinit var contentImageView: ImageView
    lateinit var contentTextView: Typography
    lateinit var contentTitleView: Typography
    lateinit var ctaButton: UnifyButton
    lateinit var closeButton: ImageView

    private var contentImageUrl = ""
    private var contentImageViewType = ""
    private var contentText = ""
    private var contentTitle = ""
    private var btnText = ""
    private var appLink = ""

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_update_longer, container, false).apply {
            contentImageView = findViewById(R.id.content_image)
            contentTextView = findViewById(R.id.content_text)
            contentTitleView = findViewById(R.id.content_title)
            ctaButton = findViewById(R.id.cta_button)
            closeButton = findViewById(R.id.iv_close)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener {
            //To Anchor View Bottom
            val dialog = it as BottomSheetDialog
            val containerLayout: FrameLayout? = dialog.findViewById(R.id.container)

            //To Expand Dialog when dialog showed
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.skipCollapsed = true

            //To Make NestedScrollview as main concern and drag down when reach the top
            containerLayout?.scroll_main?.viewTreeObserver
                    ?.addOnScrollChangedListener {
                        //When Scroll view reach the bottom
                        if (!containerLayout.scroll_main.canScrollVertically(1)) {
                            containerLayout.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        //When Scroll view reach the top
                        if (!containerLayout.scroll_main.canScrollVertically(-1)) {
                            containerLayout.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(savedInstanceState)
        setupContentPadding()
        setupCtaButton()
        contentTitleView.text = contentTitle
        contentTextView.text = contentText

        if (contentImageUrl.isNotBlank()) {
            ImageHandler.loadImage2(contentImageView, contentImageUrl, R.drawable.ic_loading_toped_new)
            contentImageView.show()
        } else {
            contentImageView.hide()
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        ctaButton.setOnClickListener {
            RouteManager.route(it.context, appLink)
            dismiss()
        }
    }

    private fun setupCtaButton() {
        if (btnText.isEmpty()) {
            btnText = DEFAULT_CTA_BUTTON
        }
        ctaButton.text = btnText
    }

    private fun setupContentPadding() {
        val paddingText = contentTextView.paddingBottom
        val titleHeight = contentTitleView.height
        val totalPaddingBottom = paddingText + titleHeight
        with(contentTextView) {
            setPadding(paddingLeft, paddingTop, paddingRight, totalPaddingBottom)
        }
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            contentText = getParamString(NotificationUpdateFragment.PARAM_CONTENT_TEXT, arguments, null, "")
            contentImageUrl = getParamString(NotificationUpdateFragment.PARAM_CONTENT_IMAGE, arguments, null, "")
            contentImageViewType = getParamString(NotificationUpdateFragment.PARAM_CONTENT_IMAGE_TYPE, arguments, null, "")
            contentTitle = getParamString(NotificationUpdateFragment.PARAM_CONTENT_TITLE, arguments, null, "")
            btnText = getParamString(NotificationUpdateFragment.PARAM_BUTTON_TEXT, arguments, null, DEFAULT_CTA_BUTTON)
            appLink = getParamString(NotificationUpdateFragment.PARAM_CTA_APPLINK, arguments, null, "")
        }
    }

    companion object {

        fun createInstance(bundle: Bundle): NotificationUpdateLongerTextFragment {
            val fragment = NotificationUpdateLongerTextFragment()
            fragment.arguments = bundle
            return fragment
        }

        val DEFAULT_CTA_BUTTON = "Klik disini"

    }
}
