package com.tokopedia.navigation.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.RoundedCornerImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment.Companion.PARAM_CONTENT_IMAGE
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment.Companion.PARAM_CONTENT_IMAGE_TYPE
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment.Companion.PARAM_CONTENT_TEXT
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment.Companion.PARAM_CONTENT_TITLE
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment.Companion.PARAM_CTA_TEXT
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 18/02/19.
 */
class NotificationUpdateLongerTextFragment : BottomSheetDialogFragment() {
    private var isBottomSheetCloseable: Boolean = true

    lateinit var contentImageView: RoundedCornerImageView
    lateinit var contentTextView: Typography
    lateinit var contentTitleView: Typography
    lateinit var ctaButton: UnifyButton
    private var behavior: BottomSheetBehavior<FrameLayout?>? = null

    private var contentImageUrl = ""
    private var contentImageViewType = ""
    private var contentText = ""
    private var contentTitle = ""


    lateinit var closeButton: View

    companion object {

        fun createInstance(bundle: Bundle): NotificationUpdateLongerTextFragment2 {
            val fragment = NotificationUpdateLongerTextFragment2()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val temp = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        temp.setOnShowListener { dialog ->
            var finalDialog = dialog as BottomSheetDialog
            val bottomSheet = finalDialog.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            var behavior = BottomSheetBehavior.from(bottomSheet)
            this.behavior = behavior
            behavior.peekHeight = 0
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            isBottomSheetCloseable = true
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> finalDialog.cancel()
                    }
                }
            })
        }

        return temp
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            contentText = getParamString(PARAM_CONTENT_TEXT, arguments, null, "")
            contentImageUrl = getParamString(PARAM_CONTENT_IMAGE, arguments, null, "")
            contentImageViewType = getParamString(PARAM_CONTENT_IMAGE_TYPE, arguments, null, "")
            contentTitle = getParamString(PARAM_CONTENT_TITLE, arguments, null, "")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notification_update_longer, container, false)
        contentImageView = view.findViewById(R.id.content_image)
        contentTextView = view.findViewById(R.id.content_text)
        contentTitleView = view.findViewById(R.id.content_title)
        ctaButton = view.findViewById(R.id.cta_button)
        closeButton = view.findViewById(R.id.header)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(savedInstanceState)
        closeButton.setOnClickListener {
            dismiss()
        }

        contentTextView.text = contentText + "\n" + contentText + "\n" +contentText + "\n" + contentText
        if (contentImageUrl.isNotBlank()) {
            ImageHandler.loadImage2(contentImageView, contentImageUrl, R.drawable.ic_loading_toped_new)
            contentImageView.show()
        } else {
            contentImageView.hide()
        }

        contentTitleView.text = contentTitle
    }
}
