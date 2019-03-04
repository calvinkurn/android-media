package com.tokopedia.checkout.view.feature.promostacking

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.text.Editable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.checkout.R
import org.xml.sax.XMLReader

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantPromoBottomSheetFragment : BottomSheetDialogFragment() {

    private var mTitle: String? = null
    private var mMessage: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): MerchantPromoBottomSheetFragment {
            val fragment = MerchantPromoBottomSheetFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            mTitle = activity!!.getString(R.string.label_cod)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<FrameLayout>(R.id.bottomsheet_container)
        View.inflate(context, R.layout.bottom_sheet_cod_notification, container)

        val textViewTitle = view.findViewById<TextView>(com.tokopedia.design.R.id.tv_title)
        textViewTitle.text = mTitle
        val textViewContent = view.findViewById<TextView>(R.id.text_view_content)
        textViewContent.text = Html.fromHtml(mMessage, null, UlTagHandler())

        view.findViewById<View>(R.id.button_bottom_sheet_cod).setOnClickListener { view1 ->
            dismiss()
        }

        val layoutTitle = view.findViewById<View>(com.tokopedia.design.R.id.layout_title)
        layoutTitle.setOnClickListener { view1 ->
            dismiss()
        }
    }

    /*
    // This class handles <li> and <ul> tag from server response to be displayed correctly
     */
    private inner class UlTagHandler : Html.TagHandler {
        override fun handleTag(opening: Boolean, tag: String, output: Editable,
                               xmlReader: XMLReader) {
            if (tag == "ul" && !opening) output.append("\n")
            if (tag == "li" && opening) output.append("\n\n\t•")
        }
    }
}