package com.tokopedia.notifcenter.presentation.fragment


import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.Resources.BottomSheetDialogTheme
import com.tokopedia.notifcenter.data.consts.Resources.designBottomSheet
import com.tokopedia.notifcenter.presentation.BaseNotificationFragment
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 18/02/19.
 */
class NotificationLongerTextDialog : BottomSheetDialogFragment() {

    lateinit var contentImageView: ImageView
    lateinit var contentTextView: Typography
    lateinit var contentTitleView: Typography
    lateinit var ctaButton: UnifyButton
    lateinit var closeButton: ImageView
    lateinit var ctaButtonContainer: FrameLayout

    private var contentImageUrl = ""
    private var contentImageViewType = ""
    private var contentText = ""
    private var contentTitle = ""
    private var btnText = ""
    private var appLink = ""
    private var templateKey = ""
    private var notificationId = ""

    private var listener: LongerContentListener? = null

    interface LongerContentListener {
        fun trackOnClickCtaButton(templateKey: String, notificationId: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is LongerContentListener) {
            listener = parent
        } else if (context is LongerContentListener) {
            listener = context
        }
    }

    override fun getTheme(): Int = BottomSheetDialogTheme

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_notification_update_longer, container, false).apply {
            contentImageView = findViewById(R.id.content_image)
            contentTextView = findViewById(R.id.content_text)
            contentTitleView = findViewById(R.id.content_title)
            ctaButton = findViewById(R.id.cta_button)
            closeButton = findViewById(R.id.iv_close)
            ctaButtonContainer = findViewById(R.id.fl_btn)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog(dialog)
        setupContentPadding()
        setupViewModel(savedInstanceState)
        setupCtaButton()
        contentTitleView.text = contentTitle
        contentTextView.text = fromHtml(contentText)

        if (contentImageUrl.isNotBlank()) {
            ImageHandler.loadImage2(contentImageView, contentImageUrl, R.drawable.ic_notifcenter_loading_toped)
            contentImageView.show()
        } else {
            contentImageView.hide()
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        ctaButton.setOnClickListener {
            listener?.trackOnClickCtaButton(templateKey, notificationId)
            RouteManager.route(it.context, appLink)
            dismiss()
        }
    }

    private fun setupContentPadding() {
        ctaButtonContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                ctaButtonContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setContentPadding()
            }
        })
    }

    private fun setContentPadding() {
        val ctaButtonHeight = ctaButtonContainer.height
        with(contentTextView) {
            setPadding(paddingLeft, paddingTop, paddingRight, ctaButtonHeight)
        }
    }

    private fun setupDialog(dialog: Dialog?) {
        if (dialog == null || dialog !is BottomSheetDialog) return
        dialog.setOnShowListener {
            //To Anchor View Bottom
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(designBottomSheet)
            val containerLayout: FrameLayout? = bottomSheetDialog.findViewById(R.id.container)

            bottomSheet?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = bottomSheet.height
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                containerLayout?.parent?.requestLayout()
            }
        }
    }

    private fun setupCtaButton() {
        if (btnText.isEmpty()) {
            btnText = DEFAULT_CTA_BUTTON
        }
        ctaButton.text = btnText
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            contentText = getParamString(BaseNotificationFragment.PARAM_CONTENT_TEXT, arguments, null, "")
            contentImageUrl = getParamString(BaseNotificationFragment.PARAM_CONTENT_IMAGE, arguments, null, "")
            contentImageViewType = getParamString(BaseNotificationFragment.PARAM_CONTENT_IMAGE_TYPE, arguments, null, "")
            contentTitle = getParamString(BaseNotificationFragment.PARAM_CONTENT_TITLE, arguments, null, "")
            btnText = getParamString(BaseNotificationFragment.PARAM_BUTTON_TEXT, arguments, null, DEFAULT_CTA_BUTTON)
            appLink = getParamString(BaseNotificationFragment.PARAM_CTA_APPLINK, arguments, null, "")
            templateKey = getParamString(BaseNotificationFragment.PARAM_TEMPLATE_KEY, arguments, null, "")
            notificationId = getParamString(BaseNotificationFragment.PARAM_NOTIF_ID, arguments, null, "")
        }
    }

    fun fromHtml(content: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(content)
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): NotificationLongerTextDialog {
            val fragment = NotificationLongerTextDialog()
            fragment.arguments = bundle
            return fragment
        }

        private const val DEFAULT_CTA_BUTTON = "Klik disini"
    }
}
