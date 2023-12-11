package com.tokopedia.developer_options.presentation.feedbackpage.ui.tickercreated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.feedbackpage.util.EXTRA_IS_TICKET_LINK
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class TicketCreatedFragment: Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(extra: Bundle): TicketCreatedFragment {
            return TicketCreatedFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_IS_TICKET_LINK, extra.getString(EXTRA_IS_TICKET_LINK))
                }
            }
        }

        private const val TICKET_CREATED_THANK_YOU = "https://images.tokopedia.net/android/res/xxhdpi/ic_thankyou_feedback.png"
    }

    private lateinit var imageCreated: AppCompatImageView
    private lateinit var ticketText1: Typography
    private lateinit var backButton: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_ticket_result, container, false)
        initView(mainView)
        return  mainView
    }

    private fun initView(mainView: View) {
        imageCreated = mainView.findViewById(R.id.iv_ticket_created)
        ticketText1 = mainView.findViewById(R.id.ticket_text_1)
        backButton = mainView.findViewById(R.id.button_back)

        ticketText1.text = MethodChecker.fromHtml(
            mainView.context.getString(R.string.ticket_text_new)
        )
        imageCreated.loadImage(TICKET_CREATED_THANK_YOU)
        backButton.setOnClickListener {
            activity?.finish()
        }
    }
}
