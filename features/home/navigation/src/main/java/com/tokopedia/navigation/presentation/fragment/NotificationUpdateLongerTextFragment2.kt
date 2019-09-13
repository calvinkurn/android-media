package com.tokopedia.navigation.presentation.fragment


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.RoundedCornerImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.navigation.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_notification_update_longer2.view.*
import kotlin.math.max

/**
 * @author by nisie on 18/02/19.
 */
class NotificationUpdateLongerTextFragment2 : BottomSheetDialogFragment() {

    lateinit var contentImageView: RoundedCornerImageView
    lateinit var contentTextView: Typography
    lateinit var contentTitleView: Typography
    lateinit var ctaButton: UnifyButton

    private var contentImageUrl = ""
    private var contentImageViewType = ""
    private var contentText = ""
    private var contentTitle = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view =
                inflater.inflate(R.layout.fragment_notification_update_longer2, container, false)


        contentImageView = view.findViewById(R.id.content_image)
        contentTextView = view.findViewById(R.id.content_text)
        contentTitleView = view.findViewById(R.id.content_title)
        ctaButton = view.findViewById(R.id.cta_button)

        return view
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //create dialog
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener {
            //To Anchor View Bottom
            val dialog = it as BottomSheetDialog
            val containerLayout: FrameLayout =
                    dialog.findViewById<FrameLayout>(R.id.container) as FrameLayout

            //To Expand Dialog when dialog showed
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.skipCollapsed = true


            val param = contentTextView.layoutParams as LinearLayout.LayoutParams
            val height = ctaButton.height
            val margin = (ctaButton.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin



            //To Make NestedScrollview as main concern and drag down when reach the top
            containerLayout.scroll_main.viewTreeObserver
                    .addOnScrollChangedListener {
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

//            param.setMargins(0,0,0,height + margin)
//            contentTextView.layoutParams = param
//            contentTextView.setPadding(0,0,0,height+margin)
        }
        return bottomSheetDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(savedInstanceState)
        contentTextView.text = contentText
        var lineCount = contentTextView.lineCount
        var a = 4



        if (contentImageUrl.isNotBlank()) {
            ImageHandler.loadImage2(contentImageView, contentImageUrl, R.drawable.ic_loading_toped_new)
            contentImageView.show()
            a = 8
        } else {
            contentImageView.hide()
        }

        contentTextView.text = contentTextView.text.toString()+ ("\n").repeat(a)
        contentTitleView.text = contentTitle
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            contentText = getParamString(NotificationUpdateFragment.PARAM_CONTENT_TEXT, arguments, null, "")
            contentImageUrl = getParamString(NotificationUpdateFragment.PARAM_CONTENT_IMAGE, arguments, null, "")
            contentImageViewType = getParamString(NotificationUpdateFragment.PARAM_CONTENT_IMAGE_TYPE, arguments, null, "")
            contentTitle = getParamString(NotificationUpdateFragment.PARAM_CONTENT_TITLE, arguments, null, "")
        }
    }

    companion object {

        fun createInstance(bundle: Bundle): NotificationUpdateLongerTextFragment2 {
            val fragment = NotificationUpdateLongerTextFragment2()
            fragment.arguments = bundle
            return fragment
        }
    }
}
